import { beforeEach, afterEach, describe, test, expect, vi } from "vitest";
import { screen, fireEvent, waitFor } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { Explore } from "./Explore";

const mockGenres = [
    { id: "mystery", genreName: "Mystery" },
    { id: "fantasy", genreName: "Fantasy" },
    { id: "technology", genreName: "Technology" },
];

const mockListings = [
    {
        id: "1",
        listingTitle: "The Da Vinci Code",
        authors: ["Dan Brown"],
        genre: ["mystery"],
        price: 12.5,
        condition: "Good",
        saleType: "DIRECT",
    },
    {
        id: "2",
        listingTitle: "Harry Potter and the Philosopher's Stone",
        authors: ["J.K. Rowling"],
        genre: ["fantasy"],
        price: 18,
        condition: "Very Good",
        saleType: "AUCTION",
    },
    {
        id: "3",
        listingTitle: "Game of Thrones",
        authors: ["George R.R. Martin"],
        genre: ["fantasy"],
        price: 15,
        condition: "Good",
        saleType: "DIRECT",
    },
    {
        id: "4",
        listingTitle: "Clean Code",
        authors: ["Robert C. Martin"],
        genre: ["technology"],
        price: 22,
        condition: "Like New",
        saleType: "DIRECT",
    },
    {
        id: "5",
        listingTitle: "The Hobbit",
        authors: ["J.R.R. Tolkien"],
        genre: ["fantasy"],
        price: 20,
        condition: "Good",
        saleType: "AUCTION",
    },
    {
        id: "6",
        listingTitle: "Sherlock Holmes",
        authors: ["Arthur Conan Doyle"],
        genre: ["mystery"],
        price: 10,
        condition: "Used",
        saleType: "DIRECT",
    },
];

// Mock BookCard so these tests focus on Explore behaviour.
vi.mock("../components/BookCard", () => ({
    BookCard: ({ title, author, genre, price, condition }) => (
        <article>
            <h2>{title}</h2>
            <p>{author}</p>
            <p>{genre}</p>
            <p>{price}</p>
            <p>{condition}</p>
        </article>
    ),
}));

// Mock BidModal to avoid unrelated dependencies.
vi.mock("../components/BidModal", () => ({
    BidModal: ({ isOpen, itemTitle }) =>
        isOpen ? <div data-testid="bid-modal">{itemTitle}</div> : null,
}));

function mockJsonResponse(data) {
    return {
        ok: true,
        json: async () => data,
    };
}

function renderExplore() {
    return renderWithProviders(<Explore />);
}

async function renderLoadedExplore() {
    renderExplore();
    await screen.findByText("6 results found");
}

function getGenreSelect() {
    return screen.getByDisplayValue("All Genres");
}

function getPublicationTypeSelect() {
    return screen.getAllByDisplayValue("All Types")[0];
}

function getSaleTypeSelect() {
    const allTypeSelects = screen.getAllByDisplayValue("All Types");
    return allTypeSelects[allTypeSelects.length - 1];
}

beforeEach(() => {
    vi.stubGlobal(
        "fetch",
        vi.fn((url) => {
            if (url.toString().includes("/genres")) {
                return Promise.resolve(mockJsonResponse(mockGenres));
            }

            if (url.toString().includes("/listings")) {
                return Promise.resolve(mockJsonResponse(mockListings));
            }

            return Promise.resolve({
                ok: false,
                json: async () => ({}),
            });
        })
    );
});

afterEach(() => {
    vi.restoreAllMocks();
    vi.unstubAllGlobals();
});

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

    test("renders results count after loading listings", async () => {
        await renderLoadedExplore();
        expect(screen.getByText("6 results found")).toBeInTheDocument();
    });

    test("does not render price range slider", () => {
        renderExplore();
        expect(screen.queryByText("Min")).not.toBeInTheDocument();
        expect(screen.queryByText("Max")).not.toBeInTheDocument();
    });
});

// ── Books display ─────────────────────────────────────────────────────────────

describe("Explore — Books Display", () => {
    test("renders all 6 API listings by default", async () => {
        await renderLoadedExplore();
        expect(screen.getByText("6 results found")).toBeInTheDocument();
    });

    test("renders The Da Vinci Code", async () => {
        await renderLoadedExplore();
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
    });

    test("renders Harry Potter", async () => {
        await renderLoadedExplore();
        expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
    });

    test("renders Direct Sale badges", async () => {
        await renderLoadedExplore();
        const directSaleBadges = screen.getAllByText("Direct Sale");
        expect(directSaleBadges.length).toBeGreaterThan(0);
    });

    test("renders Auction badges", async () => {
        await renderLoadedExplore();
        const auctionBadges = screen.getAllByText("Auction");
        expect(auctionBadges.length).toBeGreaterThan(0);
    });

    test("renders Add to Cart buttons for direct sales", async () => {
        await renderLoadedExplore();
        const cartButtons = screen.getAllByText("Add to Cart");
        expect(cartButtons.length).toBeGreaterThan(0);
    });

    test("renders Make Offer buttons for auctions", async () => {
        await renderLoadedExplore();
        const offerButtons = screen.getAllByText("Make Offer");
        expect(offerButtons.length).toBeGreaterThan(0);
    });
});

// ── Search ────────────────────────────────────────────────────────────────────

describe("Explore — Search", () => {
    test("filters by title", async () => {
        await renderLoadedExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");

        fireEvent.change(search, { target: { value: "Da Vinci" } });

        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });

    test("filters by author", async () => {
        await renderLoadedExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");

        fireEvent.change(search, { target: { value: "George R.R. Martin" } });

        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
        expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
    });

    test("search is case insensitive", async () => {
        await renderLoadedExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");

        fireEvent.change(search, { target: { value: "da vinci" } });

        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
    });

    test("shows no results message when nothing matches", async () => {
        await renderLoadedExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");

        fireEvent.change(search, { target: { value: "xyznotfound" } });

        expect(
            screen.getByText("No books found with the selected filters")
        ).toBeInTheDocument();
    });

    test("shows 0 results found when nothing matches", async () => {
        await renderLoadedExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");

        fireEvent.change(search, { target: { value: "xyznotfound" } });

        expect(screen.getByText("0 results found")).toBeInTheDocument();
    });

    test("clears filter when search is cleared", async () => {
        await renderLoadedExplore();
        const search = screen.getByPlaceholderText("Search by title or author...");

        fireEvent.change(search, { target: { value: "Da Vinci" } });
        fireEvent.change(search, { target: { value: "" } });

        expect(screen.getByText("6 results found")).toBeInTheDocument();
    });
});

// ── Genre filter ──────────────────────────────────────────────────────────────

describe("Explore — Genre Filter", () => {
    test("renders genre dropdown with loaded genres", async () => {
        await renderLoadedExplore();

        expect(screen.getByDisplayValue("All Genres")).toBeInTheDocument();
        expect(screen.getByRole("option", { name: "Fantasy" })).toBeInTheDocument();
        expect(screen.getByRole("option", { name: "Mystery" })).toBeInTheDocument();
    });

    test("filtering by Fantasy shows only Fantasy books", async () => {
        await renderLoadedExplore();
        const genreSelect = getGenreSelect();

        fireEvent.change(genreSelect, { target: { value: "fantasy" } });

        expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
        expect(screen.getByText("The Hobbit")).toBeInTheDocument();
        expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
    });

    test("filtering by Mystery shows only Mystery books", async () => {
        await renderLoadedExplore();
        const genreSelect = getGenreSelect();

        fireEvent.change(genreSelect, { target: { value: "mystery" } });

        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        expect(screen.getByText("Sherlock Holmes")).toBeInTheDocument();
        expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });

    test("resetting to All Genres shows all books", async () => {
        await renderLoadedExplore();
        const genreSelect = getGenreSelect();

        fireEvent.change(genreSelect, { target: { value: "fantasy" } });
        fireEvent.change(genreSelect, { target: { value: "all" } });

        expect(screen.getByText("6 results found")).toBeInTheDocument();
    });
});

// ── Type filter ───────────────────────────────────────────────────────────────

describe("Explore — Publication Type Filter", () => {
    test("renders publication type dropdown", async () => {
        await renderLoadedExplore();
        expect(screen.getAllByDisplayValue("All Types").length).toBeGreaterThan(0);
    });

    test("filtering by Book keeps all current API listings", async () => {
        await renderLoadedExplore();
        const publicationTypeSelect = getPublicationTypeSelect();

        fireEvent.change(publicationTypeSelect, { target: { value: "book" } });

        expect(screen.getByText("6 results found")).toBeInTheDocument();
    });

    test("filtering by Magazine shows no results because listings are mapped as books", async () => {
        await renderLoadedExplore();
        const publicationTypeSelect = getPublicationTypeSelect();

        fireEvent.change(publicationTypeSelect, { target: { value: "magazine" } });

        expect(screen.getByText("0 results found")).toBeInTheDocument();
        expect(
            screen.getByText("No books found with the selected filters")
        ).toBeInTheDocument();
    });
});

// ── Sale type filter ──────────────────────────────────────────────────────────

describe("Explore — Sale Type Filter", () => {
    test("filtering by Direct Sale shows only direct sales", async () => {
        await renderLoadedExplore();
        const saleTypeSelect = getSaleTypeSelect();

        fireEvent.change(saleTypeSelect, { target: { value: "direct" } });

        const cartButtons = screen.getAllByText("Add to Cart");
        expect(cartButtons.length).toBeGreaterThan(0);
        expect(screen.queryAllByText("Make Offer").length).toBe(0);
        expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
    });

    test("filtering by Auction shows only auctions", async () => {
        await renderLoadedExplore();
        const saleTypeSelect = getSaleTypeSelect();

        fireEvent.change(saleTypeSelect, { target: { value: "auction" } });

        const offerButtons = screen.getAllByText("Make Offer");
        expect(offerButtons.length).toBeGreaterThan(0);
        expect(screen.queryAllByText("Add to Cart").length).toBe(0);
        expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });
});

// ── BidModal ──────────────────────────────────────────────────────────────────

describe("Explore — BidModal", () => {
    test("clicking Make Offer opens the bid modal", async () => {
        await renderLoadedExplore();

        const offerButtons = screen.getAllByText("Make Offer");
        fireEvent.click(offerButtons[0]);

        expect(screen.getByTestId("bid-modal")).toBeInTheDocument();
    });

    test("bid modal shows the selected item title", async () => {
        await renderLoadedExplore();

        const offerButtons = screen.getAllByText("Make Offer");
        fireEvent.click(offerButtons[0]);

        const modal = screen.getByTestId("bid-modal");
        expect(modal).toHaveTextContent(/Harry Potter/i);
    });
});

// ── Combined filters ──────────────────────────────────────────────────────────

describe("Explore — Combined Filters", () => {
    test("genre and search work together", async () => {
        await renderLoadedExplore();

        fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "Harry" } });

        expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
        expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
    });

    test("genre and sale type work together", async () => {
        await renderLoadedExplore();

        fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });
        fireEvent.change(getSaleTypeSelect(), { target: { value: "direct" } });

        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
        expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
    });

    test("no results when filters produce empty set", async () => {
        await renderLoadedExplore();

        fireEvent.change(getGenreSelect(), { target: { value: "mystery" } });
        const search = screen.getByPlaceholderText("Search by title or author...");
        fireEvent.change(search, { target: { value: "xyznotfound" } });

        expect(
            screen.getByText("No books found with the selected filters")
        ).toBeInTheDocument();
    });
});

// ── API loading ────────────────────────────────────────────────────────────────

describe("Explore — API loading", () => {
    test("fetches genres and listings on mount", async () => {
        await renderLoadedExplore();

        await waitFor(() => {
            expect(fetch).toHaveBeenCalledTimes(2);
        });

        expect(fetch).toHaveBeenCalledWith(expect.stringContaining("/genres"));
        expect(fetch).toHaveBeenCalledWith(expect.stringContaining("/listings?userId="));
    });
});