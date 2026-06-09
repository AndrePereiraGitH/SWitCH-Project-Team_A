import { beforeEach, describe, test, expect, vi } from "vitest";
import { screen, fireEvent, render, waitFor } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { ProductDetail } from "./ProductDetail";

vi.mock("../components/BidModal", () => ({
    BidModal: ({ isOpen, itemTitle }) =>
        isOpen ? <div data-testid="bid-modal">{itemTitle}</div> : null,
}));

const mockAddToCart = vi.fn();

vi.mock("../components/CartContext", () => ({
    useCart: () => ({
        addToCart: mockAddToCart,
    }),
}));

vi.mock("../../api/constants", () => ({
    API_BASE_URL: "http://localhost:8081",
    CURRENT_USER_ID: "user-001",
}));

const mockProducts = {
    "1": {
        id: "1",
        listingTitle: "The Da Vinci Code",
        authors: ["Dan Brown"],
        genre: ["Mystery"],
        price: "12.50",
        condition: "Good",
        saleType: "DIRECT",
        shortDescription: "A mystery novel.",
        sellerId: "BookCollector123",
        isbn: "978-0-307-47427-7",
        publisher: "Doubleday",
        year: "2003",
        pages: 689,
        language: "English",
    },
    "2": {
        id: "2",
        listingTitle: "Harry Potter and the Philosopher's Stone",
        authors: ["J.K. Rowling"],
        genre: ["Fantasy"],
        price: "18.00",
        condition: "Very Good",
        saleType: "AUCTION",
        shortDescription: "A fantasy novel.",
        sellerId: "MagicBooks",
        isbn: "978-0-7475-3269-9",
        publisher: "Bloomsbury",
        year: "1997",
        pages: 223,
        language: "English",
    },
    "3": {
        id: "3",
        listingTitle: "National Geographic Magazine",
        authors: [],
        genre: ["Science", "Nature"],
        price: "5.00",
        condition: "Like New",
        saleType: "DIRECT",
        shortDescription: "Science magazine.",
        sellerId: "GeoSeller",
        isbn: "ISSN 0027-9358",
        publisher: "National Geographic Society",
        year: "2024",
        pages: 140,
        language: "English",
    },
};

describe("ProductDetail", () => {
    beforeEach(() => {
        vi.clearAllMocks();

        global.fetch = vi.fn(async (url) => {
            const match = String(url).match(/\/listings\/([^?]+)/);
            const productId = match?.[1];

            if (productId && mockProducts[productId]) {
                return {
                    ok: true,
                    status: 200,
                    json: async () => mockProducts[productId],
                };
            }

            return {
                ok: false,
                status: 404,
                json: async () => ({}),
            };
        });
    });

    async function renderLoadedProduct(productId) {
        render(
            <MemoryRouter initialEntries={[`/product/${productId}`]}>
                <Routes>
                    <Route path="/product/:productId" element={<ProductDetail />} />
                </Routes>
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.queryByText(/Loading product details/i)).not.toBeInTheDocument();
        });
    }

    describe("Product Not Found", () => {
        test("shows not found message for unknown product", async () => {
            await renderLoadedProduct("999");
            expect(screen.getByText("Product Not Found")).toBeInTheDocument();
        });
    });

    describe("Direct Sale Details", () => {
        test("renders back link", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("Back to Marketplace")).toBeInTheDocument();
        });

        test("renders product title", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        });

        test("renders product author", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText(/Dan Brown/i)).toBeInTheDocument();
        });

        test("renders product genre", async () => {
            await renderLoadedProduct("1");
            expect(screen.getAllByText("Mystery")[0]).toBeInTheDocument();
        });

        test("renders product condition", async () => {
            await renderLoadedProduct("1");
            expect(screen.getAllByText("Good").length).toBeGreaterThan(0);
        });

        test("renders product price", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("€12.50")).toBeInTheDocument();
        });

        test("renders sale type badge", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("Direct Sale")).toBeInTheDocument();
        });

        test("renders Add to Cart button", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByRole("button", { name: /Add to Cart/i })).toBeInTheDocument();
        });

        test("calls addToCart when clicked", async () => {
            await renderLoadedProduct("1");

            fireEvent.click(screen.getByRole("button", { name: /Add to Cart/i }));

            expect(mockAddToCart).toHaveBeenCalledWith({
                id: "1",
                title: "The Da Vinci Code",
                price: 12.5,
            });
        });

        test("renders seller", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("BookCollector123")).toBeInTheDocument();
        });

        test("renders ISBN", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("978-0-307-47427-7")).toBeInTheDocument();
        });

        test("renders publisher", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("Doubleday")).toBeInTheDocument();
        });

        test("renders year", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("2003")).toBeInTheDocument();
        });

        test("renders pages", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("689")).toBeInTheDocument();
        });

        test("renders language", async () => {
            await renderLoadedProduct("1");
            expect(screen.getByText("English")).toBeInTheDocument();
        });
    });

    describe("Auction Details", () => {
        test("renders Auction badge", async () => {
            await renderLoadedProduct("2");
            expect(screen.getByText("Auction")).toBeInTheDocument();
        });

        test("renders Make Offer button", async () => {
            await renderLoadedProduct("2");
            expect(screen.getByRole("button", { name: /Make Offer/i })).toBeInTheDocument();
        });

        test("renders current offer text", async () => {
            await renderLoadedProduct("2");
            expect(screen.getByText("(Current Offer)")).toBeInTheDocument();
        });

        test("renders buyout price", async () => {
            await renderLoadedProduct("2");
            expect(screen.getByText(/Buyout Price: €27.00/i)).toBeInTheDocument();
        });

        test("clicking Make Offer opens bid modal", async () => {
            await renderLoadedProduct("2");

            fireEvent.click(screen.getByRole("button", { name: /Make Offer/i }));

            expect(screen.getByTestId("bid-modal")).toBeInTheDocument();
        });

        test("bid modal shows product title", async () => {
            await renderLoadedProduct("2");

            fireEvent.click(screen.getByRole("button", { name: /Make Offer/i }));

            expect(screen.getByTestId("bid-modal")).toHaveTextContent(
                "Harry Potter and the Philosopher's Stone"
            );
        });
    });

    describe("Magazine", () => {
        test("renders magazine title", async () => {
            await renderLoadedProduct("3");
            expect(screen.getByText("National Geographic Magazine")).toBeInTheDocument();
        });

        test("renders magazine publisher", async () => {
            await renderLoadedProduct("3");
            expect(screen.getByText("National Geographic Society")).toBeInTheDocument();
        });

        test("renders Book badge because component hardcodes type as book", async () => {
            await renderLoadedProduct("3");
            expect(screen.getByText("Book")).toBeInTheDocument();
        });
    });
});