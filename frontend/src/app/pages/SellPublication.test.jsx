import { describe, test, expect } from "vitest";
import { screen, fireEvent, render, act } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "../components/AuthContext";
import { PublicationsProvider, usePublications } from "../components/PublicationsContext";
import { LibraryProvider } from "../components/LibraryContext";
import { CartProvider } from "../components/CartContext";
import { ListsProvider } from "../components/ListsContext";
import { SellPublication } from "./SellPublication";

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

// ── Wrapper that creates a publication then renders SellPublication ────────────

function TestApp() {
    const { addPublication, getAllPublications } = usePublications();
    const publications = getAllPublications();

    if (publications.length === 0) {
        return (
            <button
                onClick={() =>
                    addPublication({
                        title: "Clean Code",
                        author: "Robert C. Martin",
                        genre: "Science",
                        condition: "Good",
                        type: "book",
                    })
                }
            >
                init
            </button>
        );
    }

    const bookId = publications[0].id;

    return (
        <MemoryRouter initialEntries={[`/sell/${bookId}`]}>
            <Routes>
                <Route path="/sell/:bookId" element={<SellPublication />} />
            </Routes>
        </MemoryRouter>
    );
}

function renderWithRealPublication() {
    render(
        <AllProvidersNoRouter>
            <TestApp />
        </AllProvidersNoRouter>
    );
    act(() => {
        screen.getByText("init").click();
    });
}

function renderNotFound() {
    render(
        <AllProvidersNoRouter>
            <MemoryRouter initialEntries={["/sell/nonexistent"]}>
                <Routes>
                    <Route path="/sell/:bookId" element={<SellPublication />} />
                </Routes>
            </MemoryRouter>
        </AllProvidersNoRouter>
    );
}

// ── Publication not found ─────────────────────────────────────────────────────

describe("SellPublication — Not Found", () => {

    test("shows not found message when publication does not exist", () => {
        renderNotFound();
        expect(
            screen.getByText("Publicação não encontrada")
        ).toBeInTheDocument();
    });

    test("shows back button when publication not found", () => {
        renderNotFound();
        expect(screen.getByText("Voltar")).toBeInTheDocument();
    });

});

// ── Rendering ─────────────────────────────────────────────────────────────────

describe("SellPublication — Rendering", () => {

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
        expect(
            screen.getByRole("button", { name: /Publish Sale/i })
        ).toBeInTheDocument();
    });

    test("renders the Cancel button", () => {
        renderWithRealPublication();
        expect(
            screen.getByRole("button", { name: /Cancel/i })
        ).toBeInTheDocument();
    });

});

// ── Sale type selection ───────────────────────────────────────────────────────

describe("SellPublication — Sale Type Selection", () => {

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

// ── Direct sale form ──────────────────────────────────────────────────────────

describe("SellPublication — Direct Sale Form", () => {

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

// ── Auction form ──────────────────────────────────────────────────────────────

describe("SellPublication — Auction Form", () => {

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

// ── Image upload ──────────────────────────────────────────────────────────────

describe("SellPublication — Image Upload", () => {

    test("renders upload images section", () => {
        renderWithRealPublication();
        expect(screen.getByText("Upload Images")).toBeInTheDocument();
    });

    test("renders image count indicator", () => {
        renderWithRealPublication();
        expect(screen.getByText(/0\/5/i)).toBeInTheDocument();
    });

});

// ── Navigation ────────────────────────────────────────────────────────────────

describe("SellPublication — Navigation", () => {

    test("renders without crashing", () => {
        renderWithRealPublication();
        expect(document.body).toBeTruthy();
    });

});