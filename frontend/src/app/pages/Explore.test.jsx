import { describe, test, expect, vi } from "vitest";
import { screen, fireEvent } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { Explore } from "./Explore";

// Mock BidModal to avoid useLanguage dependency
vi.mock("../components/BidModal", () => ({
    BidModal: ({ isOpen, itemTitle }) =>
        isOpen ? <div data-testid="bid-modal">{itemTitle}</div> : null,
}));

function renderExplore() {
    return renderWithProviders(<Explore />);
}

// ── Rendering ─────────────────────────────────────────────────────────────────

describe("Explore — Rendering", () => {

    test("renders the page title", () => {
        renderExplore();
        expect(screen.getByText("Explore Sales")).toBeInTheDocument();
    });

    test("renders the US045 description", () => {
        renderExplore();
        expect(screen.getByText(/US045/i)).toBeInTheDocument();
    });

    test("renders the search input", () => {
        renderExplore();
        expect(
            screen.getByPlaceholderText("Search by title or author...")
        ).toBeInTheDocument();
    });

    test("renders the back button", () => {
        renderExplore();
        expect(screen.getByText("Back")).toBeInTheDocument();
    });

    test("renders results count", () => {
        renderExplore();
        expect(screen.getByText(/results found/i)).toBeInTheDocument();
    });

    test("does not render price range slider", () => {
        renderExplore();
        expect(screen.queryByText("Min")).not.toBeInTheDocument();
        expect(screen.queryByText("Max")).not.toBeInTheDocument();
    });

});

// ── Books display ─────────────────────────────────────────────────────────────

describe("Explore — Books Display", () => {

    test("renders all 6 mock sales by default", () => {
        renderExplore();
        expect(screen.getByText("6 results found")).toBeInTheDocument();
    });

    test("renders The Da Vinci Code", () => {
        renderExplore();
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
    });

    test("renders Harry Potter", () => {
        renderExplore();
        expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
    });

    test("renders National Geographic Magazine", () => {
        renderExplore();
        expect(screen.getByText("National Geographic Magazine")).toBeInTheDocument();
    });

    test("renders Direct Sale badges", () => {
        renderExplore();
        const directSaleBadges = screen.getAllByText("Direct Sale");
        expect(directSaleBadges.length).toBeGreaterThan(0);
    });

    test("renders Auction badges", () => {
        renderExplore();
        const auctionBadges = screen.getAllByText("Auction");
        expect(auctionBadges.length).toBeGreaterThan(0);
    });

    test("renders Add to Cart buttons for direct sales", () => {
        renderExplore();
        const cartButtons = screen.getAllByText("Add to Cart");
        expect(cartButtons.length).toBeGreaterThan(0);
    });

    test("renders Make Offer buttons for auctions", () => {
        renderExplore();
        const offerButtons = screen.getAllByText("Make Offer");
        expect(offerButtons.length).toBeGreaterThan(0);
    });

});

// ── Search ────────────────────────────────────────────────────────────────────

describe("Explore — Search", () => {

    test("filters by title", () => {
        renderExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "Da Vinci" } });
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });

    test("filters by author", () => {
        renderExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "George R.R. Martin" } });
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
        expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
    });

    test("search is case insensitive", () => {
        renderExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "da vinci" } });
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
    });

    test("shows no results message when nothing matches", () => {
        renderExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "xyznotfound" } });
        expect(
            screen.getByText("No books found with the selected filters")
        ).toBeInTheDocument();
    });

    test("shows 0 results found when nothing matches", () => {
        renderExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "xyznotfound" } });
        expect(screen.getByText("0 results found")).toBeInTheDocument();
    });

    test("clears filter when search is cleared", () => {
        renderExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "Da Vinci" } });
        fireEvent.change(search, { target: { value: "" } });
        expect(screen.getByText("6 results found")).toBeInTheDocument();
    });

});

// ── Genre filter ──────────────────────────────────────────────────────────────

describe("Explore — Genre Filter", () => {

    test("renders genre dropdown", () => {
        renderExplore();
        expect(screen.getByDisplayValue("All Genres")).toBeInTheDocument();
    });

    test("filtering by Fantasy shows only Fantasy books", () => {
        renderExplore();
        const genreSelect = screen.getByDisplayValue("All Genres");
        fireEvent.change(genreSelect, { target: { value: "Fantasy" } });
        expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
        expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
    });

    test("filtering by Mystery shows only Mystery books", () => {
        renderExplore();
        const genreSelect = screen.getByDisplayValue("All Genres");
        fireEvent.change(genreSelect, { target: { value: "Mystery" } });
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });

    test("resetting to All Genres shows all books", () => {
        renderExplore();
        const genreSelect = screen.getByDisplayValue("All Genres");
        fireEvent.change(genreSelect, { target: { value: "Fantasy" } });
        fireEvent.change(genreSelect, { target: { value: "all" } });
        expect(screen.getByText("6 results found")).toBeInTheDocument();
    });

});

// ── Type filter ───────────────────────────────────────────────────────────────

describe("Explore — Publication Type Filter", () => {

    test("renders publication type dropdown", () => {
        renderExplore();
        expect(screen.getAllByDisplayValue("All Types").length).toBeGreaterThan(0);
    });

    test("filtering by Book shows only books", () => {
        renderExplore();
        const typeSelects = screen.getAllByDisplayValue("All Types");
        fireEvent.change(typeSelects[0], { target: { value: "book" } });
        expect(screen.queryByText("National Geographic Magazine")).not.toBeInTheDocument();
        expect(screen.queryByText("TIME Magazine Collection")).not.toBeInTheDocument();
    });

    test("filtering by Magazine shows only magazines", () => {
        renderExplore();
        const typeSelects = screen.getAllByDisplayValue("All Types");
        fireEvent.change(typeSelects[0], { target: { value: "magazine" } });
        expect(screen.getByText("National Geographic Magazine")).toBeInTheDocument();
        expect(screen.getByText("TIME Magazine Collection")).toBeInTheDocument();
        expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
    });

});

// ── Sale type filter ──────────────────────────────────────────────────────────

describe("Explore — Sale Type Filter", () => {

    test("filtering by Direct Sale shows only direct sales", () => {
        renderExplore();
        const allTypeSelects = screen.getAllByDisplayValue("All Types");
        const saleTypeSelect = allTypeSelects[allTypeSelects.length - 1];
        fireEvent.change(saleTypeSelect, { target: { value: "direct" } });
        const cartButtons = screen.getAllByText("Add to Cart");
        expect(cartButtons.length).toBeGreaterThan(0);
        expect(screen.queryAllByText("Make Offer").length).toBe(0);
    });

    test("filtering by Auction shows only auctions", () => {
        renderExplore();
        const allTypeSelects = screen.getAllByDisplayValue("All Types");
        const saleTypeSelect = allTypeSelects[allTypeSelects.length - 1];
        fireEvent.change(saleTypeSelect, { target: { value: "auction" } });
        const offerButtons = screen.getAllByText("Make Offer");
        expect(offerButtons.length).toBeGreaterThan(0);
        expect(screen.queryAllByText("Add to Cart").length).toBe(0);
    });

});

// ── BidModal ──────────────────────────────────────────────────────────────────

describe("Explore — BidModal", () => {

    test("clicking Make Offer opens the bid modal", () => {
        renderExplore();
        const offerButtons = screen.getAllByText("Make Offer");
        fireEvent.click(offerButtons[0]);
        expect(screen.getByTestId("bid-modal")).toBeInTheDocument();
    });

    test("bid modal shows the selected item title", () => {
        renderExplore();
        const offerButtons = screen.getAllByText("Make Offer");
        fireEvent.click(offerButtons[0]);
        const modal = screen.getByTestId("bid-modal");
        expect(modal).toHaveTextContent(/Harry Potter/i);
    });

});

// ── Combined filters ──────────────────────────────────────────────────────────

describe("Explore — Combined Filters", () => {

    test("genre and search work together", () => {
        renderExplore();
        const genreSelect = screen.getByDisplayValue("All Genres");
        fireEvent.change(genreSelect, { target: { value: "Fantasy" } });
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "Harry" } });
        expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
        expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });

    test("genre and sale type work together", () => {
        renderExplore();
        const genreSelect = screen.getByDisplayValue("All Genres");
        fireEvent.change(genreSelect, { target: { value: "Fantasy" } });
        const allTypeSelects = screen.getAllByDisplayValue("All Types");
        const saleTypeSelect = allTypeSelects[allTypeSelects.length - 1];
        fireEvent.change(saleTypeSelect, { target: { value: "direct" } });
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
        expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
    });

    test("no results when filters produce empty set", () => {
        renderExplore();
        const genreSelect = screen.getByDisplayValue("All Genres");
        fireEvent.change(genreSelect, { target: { value: "Mystery" } });
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "xyznotfound" } });
        expect(
            screen.getByText("No books found with the selected filters")
        ).toBeInTheDocument();
    });

});