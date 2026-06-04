import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, fireEvent, render } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { SellPublication } from "./SellPublication";

// ── Mocks ─────────────────────────────────────────────────────────────────────

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const mockSetPublicationForSale = vi.fn();
const mockGetAllPublications = vi.fn();

vi.mock("../components/PublicationsContext", () => ({
    usePublications: () => ({
        getAllPublications: mockGetAllPublications,
        setPublicationForSale: mockSetPublicationForSale
    })
}));

const mockBook = {
    id: "book-1",
    title: "Clean Code",
    author: "Robert C. Martin",
    condition: "Good"
};

// ── Test Wrappers ─────────────────────────────────────────────────────────────

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

// ── Tests ─────────────────────────────────────────────────────────────────────

describe("SellPublication", () => {
    beforeEach(() => {
        vi.clearAllMocks();
        mockGetAllPublications.mockReturnValue([mockBook]);
    });

    // ── Publication not found ──
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

    // ── Rendering ──
    describe("Rendering", () => {
        test("renders the page title", () => {
            renderWithRealPublication();
            expect(screen.getByText("Put For Sale")).toBeInTheDocument();
        });

        test("renders the US044 description", () => {
            renderWithRealPublication();
            expect(screen.getByText(/US044/i)).toBeInTheDocument();
        });

        test("renders the book title", () => {
            renderWithRealPublication();
            expect(screen.getByText("Clean Code")).toBeInTheDocument();
        });

        test("renders the book author", () => {
            renderWithRealPublication();
            expect(screen.getByText("Robert C. Martin")).toBeInTheDocument();
        });

        test("renders the book condition", () => {
            renderWithRealPublication();
            expect(screen.getByText(/Good/i)).toBeInTheDocument();
        });

        test("renders the back button", () => {
            renderWithRealPublication();
            expect(screen.getByText("Back")).toBeInTheDocument();
        });

        test("renders the Publish Sale button", () => {
            renderWithRealPublication();
            expect(screen.getByRole("button", { name: /Publish Sale/i })).toBeInTheDocument();
        });

        test("renders the Cancel button", () => {
            renderWithRealPublication();
            expect(screen.getByRole("button", { name: /Cancel/i })).toBeInTheDocument();
        });
    });

    // ── Sale type selection ──
    describe("Sale Type Selection", () => {
        test("renders Direct Sale option", () => {
            renderWithRealPublication();
            expect(screen.getByText("Direct Sale")).toBeInTheDocument();
        });

        test("renders Auction option", () => {
            renderWithRealPublication();
            expect(screen.getByText("Auction")).toBeInTheDocument();
        });

        test("Direct Sale is selected by default", () => {
            renderWithRealPublication();
            expect(screen.getByText("Sale Price *")).toBeInTheDocument();
        });

        test("clicking Auction shows auction fields", () => {
            renderWithRealPublication();
            fireEvent.click(screen.getByText("Auction"));
            expect(screen.getByText("Starting Price *")).toBeInTheDocument();
        });

        test("clicking Auction shows Start Date field", () => {
            renderWithRealPublication();
            fireEvent.click(screen.getByText("Auction"));
            expect(screen.getByText("Start Date *")).toBeInTheDocument();
        });

        test("clicking Auction shows End Date field", () => {
            renderWithRealPublication();
            fireEvent.click(screen.getByText("Auction"));
            expect(screen.getByText("End Date *")).toBeInTheDocument();
        });

        test("clicking Auction shows Buy Out Price field", () => {
            renderWithRealPublication();
            fireEvent.click(screen.getByText("Auction"));
            const buyOutTexts = screen.getAllByText(/Buy Out Price/i);
            expect(buyOutTexts.length).toBeGreaterThan(0);
        });

        test("clicking Direct Sale hides auction fields", () => {
            renderWithRealPublication();
            fireEvent.click(screen.getByText("Auction"));
            fireEvent.click(screen.getByText("Direct Sale"));
            expect(screen.queryByText("Starting Price *")).not.toBeInTheDocument();
        });

        test("clicking Direct Sale shows sale price field", () => {
            renderWithRealPublication();
            fireEvent.click(screen.getByText("Auction"));
            fireEvent.click(screen.getByText("Direct Sale"));
            expect(screen.getByText("Sale Price *")).toBeInTheDocument();
        });
    });

    // ── Direct sale form ──
    describe("Direct Sale Form", () => {
        test("renders sale price input with placeholder", () => {
            renderWithRealPublication();
            expect(screen.getByPlaceholderText("25.00")).toBeInTheDocument();
        });

        test("user can type a price", () => {
            renderWithRealPublication();
            const priceInput = screen.getByPlaceholderText("25.00");
            fireEvent.change(priceInput, { target: { value: "15.99" } });
            expect(priceInput.value).toBe("15.99");
        });
    });

    // ── Auction form ──
    describe("Auction Form", () => {
        function selectAuction() {
            fireEvent.click(screen.getByText("Auction"));
        }

        test("renders starting price input", () => {
            renderWithRealPublication();
            selectAuction();
            expect(screen.getByPlaceholderText("10.00")).toBeInTheDocument();
        });

        test("renders buyout price input", () => {
            renderWithRealPublication();
            selectAuction();
            expect(screen.getByPlaceholderText("20.00")).toBeInTheDocument();
        });

        test("user can type a starting price", () => {
            renderWithRealPublication();
            selectAuction();
            const startingPriceInput = screen.getByPlaceholderText("10.00");
            fireEvent.change(startingPriceInput, { target: { value: "5.00" } });
            expect(startingPriceInput.value).toBe("5.00");
        });

        test("shows info note about buy out price", () => {
            renderWithRealPublication();
            selectAuction();
            expect(screen.getByText(/Buy Out Price allows buyers/i)).toBeInTheDocument();
        });
    });

    // ── Image upload ──
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

    // ── Navigation ──
    describe("Navigation", () => {
        test("renders without crashing", () => {
            renderWithRealPublication();
            expect(document.body).toBeTruthy();
        });

        test("navigates back when Cancel is clicked", () => {
            renderWithRealPublication();
            fireEvent.click(screen.getByRole("button", { name: /Cancel/i }));
            expect(mockNavigate).toHaveBeenCalledWith(-1);
        });

        test("calls setPublicationForSale and navigates when published", () => {
            renderWithRealPublication();

            const priceInput = screen.getByPlaceholderText("25.00");
            fireEvent.change(priceInput, { target: { value: "15.99" } });

            fireEvent.click(screen.getByRole("button", { name: /Publish Sale/i }));

            expect(mockSetPublicationForSale).toHaveBeenCalledWith("book-1", 15.99);
            expect(mockNavigate).toHaveBeenCalledWith("/my-publications");
        });
    });
});