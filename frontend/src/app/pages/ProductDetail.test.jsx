import { describe, test, expect } from "vitest";
import { screen, fireEvent, render } from "@testing-library/react";
import { vi } from "vitest";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "../components/AuthContext";
import { PublicationsProvider } from "../components/PublicationsContext";
import { LibraryProvider } from "../components/LibraryContext";
import { CartProvider } from "../components/CartContext";
import { ListsProvider } from "../components/ListsContext";
import { ProductDetail } from "./ProductDetail";

// ── Mock BidModal to avoid useLanguage dependency ─────────────────────────────

vi.mock("../components/BidModal", () => ({
    BidModal: ({ isOpen, itemTitle }) =>
        isOpen ? <div data-testid="bid-modal">{itemTitle}</div> : null,
}));

// ── Custom render without BrowserRouter ───────────────────────────────────────

function AllProvidersNoRouter({ children }) {
    return (
        <AuthProvider>
            <PublicationsProvider>
                <LibraryProvider>
                    <CartProvider>
                        <ListsProvider>
                            {children}
                        </ListsProvider>
                    </CartProvider>
                </LibraryProvider>
            </PublicationsProvider>
        </AuthProvider>
    );
}

function renderProduct(productId) {
    return render(
        <AllProvidersNoRouter>
            <MemoryRouter initialEntries={[`/product/${productId}`]}>
                <Routes>
                    <Route path="/product/:productId" element={<ProductDetail />} />
                </Routes>
            </MemoryRouter>
        </AllProvidersNoRouter>
    );
}

// ── Product not found ─────────────────────────────────────────────────────────

describe("ProductDetail — Not Found", () => {

    test("shows not found message for invalid product", () => {
        renderProduct("nonexistent");
        expect(screen.getByText("Product Not Found")).toBeInTheDocument();
    });

    test("shows back to marketplace button when not found", () => {
        renderProduct("nonexistent");
        expect(screen.getByText("Back to Marketplace")).toBeInTheDocument();
    });

});

// ── Rendering — Direct Sale (product id 1) ────────────────────────────────────

describe("ProductDetail — Direct Sale Rendering", () => {

    test("renders the product title", () => {
        renderProduct("1");
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
    });

    test("renders the product author", () => {
        renderProduct("1");
        expect(screen.getByText(/Dan Brown/i)).toBeInTheDocument();
    });

    test("renders the genre badge", () => {
        renderProduct("1");
        expect(screen.getByText("Mystery")).toBeInTheDocument();
    });

    test("renders the condition badge", () => {
        renderProduct("1");
        expect(screen.getAllByText("Good").length).toBeGreaterThan(0);
    });

    test("renders the price", () => {
        renderProduct("1");
        expect(screen.getByText("€12.50")).toBeInTheDocument();
    });

    test("renders Direct Sale badge", () => {
        renderProduct("1");
        expect(screen.getByText("Direct Sale")).toBeInTheDocument();
    });

    test("renders Add to Cart button for direct sale", () => {
        renderProduct("1");
        expect(
            screen.getByRole("button", { name: /Add to Cart/i })
        ).toBeInTheDocument();
    });

    test("does not render Make Offer button for direct sale", () => {
        renderProduct("1");
        expect(
            screen.queryByRole("button", { name: /Make Offer/i })
        ).not.toBeInTheDocument();
    });

    test("renders Back to Marketplace button", () => {
        renderProduct("1");
        expect(screen.getByText("Back to Marketplace")).toBeInTheDocument();
    });

    test("renders description", () => {
        renderProduct("1");
        expect(screen.getByText(/gripping mystery/i)).toBeInTheDocument();
    });

    test("renders seller name", () => {
        renderProduct("1");
        expect(screen.getByText("BookCollector123")).toBeInTheDocument();
    });

    test("renders seller location", () => {
        renderProduct("1");
        expect(screen.getByText("Lisbon, Portugal")).toBeInTheDocument();
    });

    test("renders ISBN", () => {
        renderProduct("1");
        expect(screen.getByText("978-0-307-47427-7")).toBeInTheDocument();
    });

    test("renders publisher", () => {
        renderProduct("1");
        expect(screen.getByText("Doubleday")).toBeInTheDocument();
    });

    test("renders year", () => {
        renderProduct("1");
        expect(screen.getByText("2003")).toBeInTheDocument();
    });

    test("renders page count", () => {
        renderProduct("1");
        expect(screen.getByText("689")).toBeInTheDocument();
    });

    test("renders language", () => {
        renderProduct("1");
        expect(screen.getAllByText("English").length).toBeGreaterThan(0);
    });

});

// ── Rendering — Auction (product id 2) ───────────────────────────────────────

describe("ProductDetail — Auction Rendering", () => {

    test("renders Auction badge", () => {
        renderProduct("2");
        expect(screen.getByText("Auction")).toBeInTheDocument();
    });

    test("renders Make Offer button for auction", () => {
        renderProduct("2");
        expect(
            screen.getByRole("button", { name: /Make Offer/i })
        ).toBeInTheDocument();
    });

    test("does not render Add to Cart button for auction", () => {
        renderProduct("2");
        expect(
            screen.queryByRole("button", { name: /Add to Cart/i })
        ).not.toBeInTheDocument();
    });

    test("renders auction ends section", () => {
        renderProduct("2");
        expect(screen.getByText("Auction Ends")).toBeInTheDocument();
    });

    test("renders auction time remaining", () => {
        renderProduct("2");
        expect(screen.getByText(/remaining/i)).toBeInTheDocument();
    });

    test("renders current offer label", () => {
        renderProduct("2");
        expect(screen.getByText("(Current Offer)")).toBeInTheDocument();
    });

    test("renders buyout price for auction", () => {
        renderProduct("2");
        expect(screen.getByText(/Buyout Price/i)).toBeInTheDocument();
    });

});

// ── BidModal ──────────────────────────────────────────────────────────────────

describe("ProductDetail — BidModal", () => {

    test("clicking Make Offer opens bid modal", () => {
        renderProduct("2");
        fireEvent.click(screen.getByRole("button", { name: /Make Offer/i }));
        expect(screen.getByTestId("bid-modal")).toBeInTheDocument();
    });

    test("bid modal shows the product title", () => {
        renderProduct("2");
        fireEvent.click(screen.getByRole("button", { name: /Make Offer/i }));
        const modal = screen.getByTestId("bid-modal");
        expect(modal).toHaveTextContent(/Harry Potter/i);
    });

});

// ── Magazine product (product id 3) ──────────────────────────────────────────

describe("ProductDetail — Magazine", () => {

    test("renders Magazine badge for magazine type", () => {
        renderProduct("3");
        expect(screen.getByText("Magazine")).toBeInTheDocument();
    });

    test("renders magazine title", () => {
        renderProduct("3");
        expect(screen.getByText("National Geographic Magazine")).toBeInTheDocument();
    });

    test("renders magazine publisher", () => {
        renderProduct("3");
        expect(
            screen.getByText("National Geographic Society")
        ).toBeInTheDocument();
    });

});

// ── Navigation ────────────────────────────────────────────────────────────────

describe("ProductDetail — Navigation", () => {

    test("renders without crashing for each product", () => {
        ["1", "2", "3", "4", "5", "6"].forEach(id => {
            const { unmount } = renderProduct(id);
            expect(document.body).toBeTruthy();
            unmount();
        });
    });

});