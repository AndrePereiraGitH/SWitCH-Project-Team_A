import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, fireEvent, render, waitFor } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { SellPublication } from "./SellPublication";

const mockNavigate = vi.fn();

vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const mockGetAllPublications = vi.fn();

vi.mock("../components/PublicationsContext", () => ({
    usePublications: () => ({
        getAllPublications: mockGetAllPublications,
    }),
}));

vi.mock("../../api/constants", () => ({
    API_BASE_URL: "http://localhost:8081",
    CURRENT_USER_ID: "user-001",
}));

const mockBook = {
    id: "book-1",
    title: "Clean Code",
    author: "Robert C. Martin",
    condition: "Good",
};

function renderWithRealPublication() {
    render(
        <MemoryRouter initialEntries={["/sell/book-1"]}>
            <Routes>
                <Route path="/sell/:bookId" element={<SellPublication />} />
            </Routes>
        </MemoryRouter>
    );
}

function renderNotFound() {
    render(
        <MemoryRouter initialEntries={["/sell/nonexistent"]}>
            <Routes>
                <Route path="/sell/:bookId" element={<SellPublication />} />
            </Routes>
        </MemoryRouter>
    );
}

describe("SellPublication", () => {
    beforeEach(() => {
        vi.clearAllMocks();

        mockGetAllPublications.mockReturnValue([mockBook]);

        global.fetch = vi.fn().mockResolvedValue({
            ok: true,
            text: vi.fn().mockResolvedValue(""),
        });
    });

    describe("Not Found", () => {
        test("shows not found message when publication does not exist", () => {
            renderNotFound();
            expect(screen.getByText("Publicação não encontrada")).toBeInTheDocument();
        });

        test("shows back button when publication not found", () => {
            renderNotFound();
            expect(screen.getByText("Voltar")).toBeInTheDocument();
        });
    });

    describe("Rendering", () => {
        test("renders the page title", () => {
            renderWithRealPublication();
            expect(screen.getByText("Put For Sale")).toBeInTheDocument();
        });

        test("renders the page description", () => {
            renderWithRealPublication();
            expect(screen.getByText("Create listing for this publication")).toBeInTheDocument();
        });

        test("renders the book details", () => {
            renderWithRealPublication();

            expect(screen.getByText("Clean Code")).toBeInTheDocument();
            expect(screen.getByText("Robert C. Martin")).toBeInTheDocument();
            expect(screen.getByText(/Condition:\s*Good/i)).toBeInTheDocument();
        });

        test("renders action buttons", () => {
            renderWithRealPublication();

            expect(screen.getByText("Back")).toBeInTheDocument();
            expect(screen.getByRole("button", { name: /Publish Sale/i })).toBeInTheDocument();
            expect(screen.getByRole("button", { name: /Cancel/i })).toBeInTheDocument();
        });
    });

    describe("Sale Type Selection", () => {
        test("Direct Sale is selected by default", () => {
            renderWithRealPublication();
            expect(screen.getByText("Sale Price *")).toBeInTheDocument();
        });

        test("clicking Auction shows auction fields", () => {
            renderWithRealPublication();

            fireEvent.click(screen.getByText("Auction"));

            expect(screen.getByText("Starting Price *")).toBeInTheDocument();
            expect(screen.getByText("Start Date *")).toBeInTheDocument();
            expect(screen.getByText("Start Time *")).toBeInTheDocument();
            expect(screen.getByText("End Date *")).toBeInTheDocument();
            expect(screen.getByText("End Time *")).toBeInTheDocument();
            expect(screen.getByText("Buy Out Price")).toBeInTheDocument();
        });

        test("clicking Direct Sale hides auction fields", () => {
            renderWithRealPublication();

            fireEvent.click(screen.getByText("Auction"));
            fireEvent.click(screen.getByText("Direct Sale"));

            expect(screen.queryByText("Starting Price *")).not.toBeInTheDocument();
            expect(screen.getByText("Sale Price *")).toBeInTheDocument();
        });
    });

    describe("Direct Sale Form", () => {
        test("user can type a price", () => {
            renderWithRealPublication();

            const priceInput = screen.getByPlaceholderText("25.00");
            fireEvent.change(priceInput, { target: { value: "15.99" } });

            expect(priceInput.value).toBe("15.99");
        });

        test("user can type a description", () => {
            renderWithRealPublication();

            const descriptionInput = screen.getByPlaceholderText(
                "Describe the condition, edition, or any useful details..."
            );

            fireEvent.change(descriptionInput, {
                target: { value: "Almost new." },
            });

            expect(descriptionInput.value).toBe("Almost new.");
        });
    });

    describe("Auction Form", () => {
        test("user can type auction values", () => {
            renderWithRealPublication();

            fireEvent.click(screen.getByText("Auction"));

            const startingPriceInput = screen.getByPlaceholderText("10.00");
            const buyoutPriceInput = screen.getByPlaceholderText("20.00");

            fireEvent.change(startingPriceInput, { target: { value: "5.00" } });
            fireEvent.change(buyoutPriceInput, { target: { value: "20.00" } });

            expect(startingPriceInput.value).toBe("5.00");
            expect(buyoutPriceInput.value).toBe("20.00");
        });
    });

    describe("Image Upload", () => {
        test("renders upload images section", () => {
            renderWithRealPublication();
            expect(screen.getByText("Upload Images")).toBeInTheDocument();
        });

        test("renders image count indicator", () => {
            renderWithRealPublication();
            expect(screen.getByText(/0\/5/i)).toBeInTheDocument();
        });
    });

    describe("Navigation and Submit", () => {
        test("navigates back when Cancel is clicked", () => {
            renderWithRealPublication();

            fireEvent.click(screen.getByRole("button", { name: /Cancel/i }));

            expect(mockNavigate).toHaveBeenCalledWith(-1);
        });

        test("creates direct sale listing and navigates to explore", async () => {
            renderWithRealPublication();

            fireEvent.change(screen.getByPlaceholderText("25.00"), {
                target: { value: "15.99" },
            });

            fireEvent.click(screen.getByRole("button", { name: /Publish Sale/i }));

            await waitFor(() => {
                expect(global.fetch).toHaveBeenCalledWith(
                    "http://localhost:8081/listings?userId=user-001",
                    expect.objectContaining({
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                    })
                );
            });

            const [, options] = global.fetch.mock.calls[0];

            expect(JSON.parse(options.body)).toMatchObject({
                listingTitle: "Clean Code",
                publicationIds: ["book-1"],
                saleType: "Direct Sale",
                price: "15.99",
                shortDescription: "",
                pictures: [],
            });

            expect(mockNavigate).toHaveBeenCalledWith("/explore");
        });

        test("creates auction listing with starting price", async () => {
            renderWithRealPublication();

            fireEvent.click(screen.getByText("Auction"));

            const form = document.querySelector("form");

            fireEvent.change(form.querySelector('input[name="startingPrice"]'), {
                target: { value: "5.00" },
            });

            fireEvent.change(form.querySelector('input[name="buyoutPrice"]'), {
                target: { value: "20.00" },
            });

            fireEvent.change(form.querySelector('input[name="auctionStartDate"]'), {
                target: { value: "2026-06-10" },
            });

            fireEvent.change(form.querySelector('input[name="auctionStartTime"]'), {
                target: { value: "10:00" },
            });

            fireEvent.change(form.querySelector('input[name="auctionEndDate"]'), {
                target: { value: "2026-06-11" },
            });

            fireEvent.change(form.querySelector('input[name="auctionEndTime"]'), {
                target: { value: "10:00" },
            });

            fireEvent.click(screen.getByRole("button", { name: /Publish Sale/i }));

            await waitFor(() => {
                expect(global.fetch).toHaveBeenCalled();
            });

            const [, options] = global.fetch.mock.calls[0];

            expect(JSON.parse(options.body)).toMatchObject({
                listingTitle: "Clean Code",
                publicationIds: ["book-1"],
                saleType: "Auction",
                price: "5.00",
                shortDescription: "",
                pictures: [],
            });

            expect(mockNavigate).toHaveBeenCalledWith("/explore");
        });

        test("shows error when listing creation fails", async () => {
            global.fetch.mockResolvedValueOnce({
                ok: false,
                text: vi.fn().mockResolvedValue("Failed to create listing"),
            });

            renderWithRealPublication();

            fireEvent.change(screen.getByPlaceholderText("25.00"), {
                target: { value: "15.99" },
            });

            fireEvent.click(screen.getByRole("button", { name: /Publish Sale/i }));

            expect(await screen.findByText("Failed to create listing")).toBeInTheDocument();
            expect(mockNavigate).not.toHaveBeenCalledWith("/explore");
        });
    });
});