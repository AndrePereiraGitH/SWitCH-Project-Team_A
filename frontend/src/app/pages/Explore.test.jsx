import { beforeEach, describe, test, expect, vi } from "vitest";
import { screen, fireEvent, waitFor, render } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { Explore } from "./Explore";

// ── Mocks ─────────────────────────────────────────────────────────────────────

const mockAddToCart = vi.fn();
vi.mock("../components/CartContext", () => ({
    useCart: () => ({ addToCart: mockAddToCart })
}));

vi.mock("../../api/constants", () => ({
    API_BASE_URL: "http://localhost:8081",
    CURRENT_USER_ID: "user-123"
}));

vi.mock("../components/PurchasesContext", () => ({
    usePurchases: () => ({ soldItemIds: new Set() })
}));

// ── Fixtures ──────────────────────────────────────────────────────────────────

const mockGenres = [
    { id: "mystery", genreName: "Mystery" },
    { id: "fantasy", genreName: "Fantasy" },
    { id: "technology", genreName: "Technology" },
];

const mockPublications = {
    _embedded: {
        responseBibliographicalDataDTOList: [
            { bdId: "bd-001", pubTitle: "The Lord of the Rings" },
            { bdId: "bd-002", pubTitle: "Foundation" },
        ]
    }
};

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
];

const mockPublicationListings = [
    {
        id: "4",
        listingTitle: "The Lord of the Rings - Direct Sale",
        authors: ["J. R. R. Tolkien"],
        genre: ["fantasy"],
        price: 24.99,
        condition: "Poor",
        saleType: "DIRECT_SALE",
    },
    {
        id: "5",
        listingTitle: "The Lord of the Rings - Auction",
        authors: ["J. R. R. Tolkien"],
        genre: ["fantasy"],
        price: 10.00,
        condition: "Very Good",
        saleType: "AUCTION",
    },
];

const mockFantasyGenreListings = [
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
        id: "6",
        listingTitle: "The Lord of the Rings - Auction",
        authors: ["J. R. R. Tolkien"],
        genre: ["fantasy"],
        price: 10.00,
        condition: "Very Good",
        saleType: "AUCTION",
    },
];

// ── Helpers ───────────────────────────────────────────────────────────────────

function setupFetch({ publicationListings = null, genreListings = null } = {}) {
    global.fetch = vi.fn(async (url) => {
        if (url.includes("/genres")) {
            return { ok: true, json: async () => mockGenres };
        }
        if (url.includes("/bibliographicalDatas")) {
            return { ok: true, json: async () => mockPublications };
        }
        if (url.includes("/listings/by-publication/")) {
            return {
                ok: true,
                json: async () => publicationListings ?? mockPublicationListings,
            };
        }
        if (url.includes("/listings/genre/")) {
            return {
                ok: true,
                json: async () => genreListings ?? mockFantasyGenreListings,
            };
        }
        if (url.includes("/listings")) {
            return { ok: true, json: async () => mockListings };
        }
        return { ok: false, status: 404 };
    });
}

async function renderLoadedExplore() {
    render(
        <MemoryRouter>
            <Explore />
        </MemoryRouter>
    );

    await waitFor(() => {
        expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
    });
}

const getGenreSelect       = () => screen.getAllByRole("combobox")[0];
const getPublicationSelect = () => screen.getAllByRole("combobox")[1];
const getTypeSelect        = () => screen.getAllByRole("combobox")[2];
const getSaleTypeSelect    = () => screen.getAllByRole("combobox")[3];
const getAuthorSelect      = () => screen.getAllByRole("combobox")[4];

// ── Tests ─────────────────────────────────────────────────────────────────────

describe("Explore", () => {
    beforeEach(() => {
        vi.clearAllMocks();
        setupFetch();
    });

    // ── Initial render ──
    describe("Initial render", () => {
        test("renders page title", async () => {
            await renderLoadedExplore();
            expect(screen.getByText("Explore Sales")).toBeInTheDocument();
        });

        test("renders all listings on load", async () => {
            await renderLoadedExplore();
            expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
            expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
            expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
        });

        test("renders result count", async () => {
            await renderLoadedExplore();
            expect(screen.getByText(/3 results found/i)).toBeInTheDocument();
        });

        test("renders Direct Sale and Auction badges", async () => {
            await renderLoadedExplore();
            expect(screen.getAllByText("Direct Sale").length).toBeGreaterThan(0);
            expect(screen.getAllByText("Auction").length).toBeGreaterThan(0);
        });
    });

    // ── Sold State ──
    describe("Sold State", () => {
        test("shows Sold button for purchased items", async () => {
            vi.mock("../components/PurchasesContext", () => ({
                usePurchases: () => ({ soldItemIds: new Set(["1"]) })
            }));

            await renderLoadedExplore();

            const soldButtons = screen.getAllByText("Sold");
            expect(soldButtons.length).toBeGreaterThan(0);
        });

        test("shows Add to Cart button for non-purchased direct sale items", async () => {
            await renderLoadedExplore();

            const addToCartButtons = screen.getAllByText("Add to Cart");
            expect(addToCartButtons.length).toBeGreaterThan(0);
        });

        test("shows Make Offer button for non-purchased auction items", async () => {
            await renderLoadedExplore();

            expect(screen.getByText("Make Offer")).toBeInTheDocument();
        });

        test("Sold button is disabled", async () => {
            vi.mock("../components/PurchasesContext", () => ({
                usePurchases: () => ({ soldItemIds: new Set(["1"]) })
            }));

            await renderLoadedExplore();

            const soldButtons = screen.getAllByRole("button", { name: /sold/i });
            soldButtons.forEach(btn => expect(btn).toBeDisabled());
        });
    });

    // ── API loading ──
    describe("API loading", () => {
        test("fetches genres, listings and bibliographical data on mount", async () => {
            await renderLoadedExplore();

            await waitFor(() => {
                expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/genres"));
                expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/listings"));
                expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/bibliographicalDatas"));
            });
        });

        test("renders genre options from API", async () => {
            await renderLoadedExplore();
            expect(screen.getByRole("option", { name: "Mystery" })).toBeInTheDocument();
            expect(screen.getByRole("option", { name: "Fantasy" })).toBeInTheDocument();
            expect(screen.getByRole("option", { name: "Technology" })).toBeInTheDocument();
        });

        test("renders publication options from API", async () => {
            await renderLoadedExplore();
            expect(screen.getByRole("option", { name: "The Lord of the Rings" })).toBeInTheDocument();
            expect(screen.getByRole("option", { name: "Foundation" })).toBeInTheDocument();
        });

        test("shows no items message when fetch fails", async () => {
            global.fetch = vi.fn(async () => ({ ok: false, status: 500 }));
            render(<MemoryRouter><Explore /></MemoryRouter>);
            await waitFor(() => {
                expect(screen.getByText(/No items found/i)).toBeInTheDocument();
            });
        });
    });

    // ── Genre filter (US062) ──
    describe("Genre filter (US062)", () => {
        test("calls genre endpoint when genre selected", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(global.fetch).toHaveBeenCalledWith(
                    expect.stringContaining("/listings/genre/fantasy")
                );
            });
        });

        test("shows listings returned by genre endpoint", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
                expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
                expect(screen.getByText("The Lord of the Rings - Auction")).toBeInTheDocument();
            });
        });

        test("includes both direct sale and auction listings for the genre", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(screen.getAllByText("Direct Sale").length).toBeGreaterThan(0);
                expect(screen.getAllByText("Auction").length).toBeGreaterThan(0);
            });
        });

        test("shows filtered by genre label", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(screen.getByText(/filtered by genre/i)).toBeInTheDocument();
            });
        });

        test("shows clear genre filter button when genre selected", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(screen.getByText("Clear genre filter")).toBeInTheDocument();
            });
        });

        test("restores all listings when genre filter cleared", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(screen.getByText("The Lord of the Rings - Auction")).toBeInTheDocument();
            });

            fireEvent.click(screen.getByText("Clear genre filter"));

            await waitFor(() => {
                expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
                expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
            });
        });

        test("disables publication filter when genre selected", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(getPublicationSelect()).toBeDisabled();
            });
        });

        test("disables author filter when genre selected", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(getAuthorSelect()).toBeDisabled();
            });
        });

        test("shows empty state when genre endpoint returns no listings", async () => {
            setupFetch({ genreListings: [] });
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(screen.getByText(/No items found/i)).toBeInTheDocument();
            });
        });
    });

    // ── Publication filter (US065) ──
    describe("Publication filter (US065)", () => {
        test("calls by-publication endpoint when publication selected", async () => {
            await renderLoadedExplore();
            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(global.fetch).toHaveBeenCalledWith(
                    expect.stringContaining("/listings/by-publication/bd-001")
                );
            });
        });

        test("shows listings returned by by-publication endpoint", async () => {
            await renderLoadedExplore();
            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(screen.getByText("The Lord of the Rings - Direct Sale")).toBeInTheDocument();
                expect(screen.getByText("The Lord of the Rings - Auction")).toBeInTheDocument();
            });
        });

        test("shows filtered by publication label", async () => {
            await renderLoadedExplore();
            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(screen.getByText(/filtered by publication/i)).toBeInTheDocument();
            });
        });

        test("shows clear publication filter button when publication selected", async () => {
            await renderLoadedExplore();
            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(screen.getByText("Clear publication filter")).toBeInTheDocument();
            });
        });

        test("restores all listings when publication filter cleared", async () => {
            await renderLoadedExplore();
            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(screen.getByText("The Lord of the Rings - Direct Sale")).toBeInTheDocument();
            });

            fireEvent.click(screen.getByText("Clear publication filter"));

            await waitFor(() => {
                expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
                expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
            });
        });

        test("disables genre filter when publication selected", async () => {
            await renderLoadedExplore();
            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(getGenreSelect()).toBeDisabled();
            });
        });

        test("disables author filter when publication selected", async () => {
            await renderLoadedExplore();
            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(getAuthorSelect()).toBeDisabled();
            });
        });

        test("shows empty state when by-publication returns no listings", async () => {
            setupFetch({ publicationListings: [] });
            await renderLoadedExplore();
            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(screen.getByText(/No items found/i)).toBeInTheDocument();
            });
        });
    });

    // ── Genre and Publication filters are mutually exclusive ──
    describe("Genre and Publication mutual exclusivity", () => {
        test("selecting a publication resets genre filter to all", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(screen.getByText(/filtered by genre/i)).toBeInTheDocument();
            });

            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(getGenreSelect().value).toBe("all");
                expect(screen.getByText(/filtered by publication/i)).toBeInTheDocument();
            });
        });

        test("selecting a genre resets publication filter to all", async () => {
            await renderLoadedExplore();
            fireEvent.change(getPublicationSelect(), { target: { value: "bd-001" } });

            await waitFor(() => {
                expect(screen.getByText(/filtered by publication/i)).toBeInTheDocument();
            });

            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(getPublicationSelect().value).toBe("all");
                expect(screen.getByText(/filtered by genre/i)).toBeInTheDocument();
            });
        });
    });

    // ── Sale type filter ──
    describe("Sale type filter", () => {
        test("filters by direct sale", async () => {
            await renderLoadedExplore();
            fireEvent.change(getSaleTypeSelect(), { target: { value: "direct" } });
            expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
            expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
            expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
        });

        test("filters by auction", async () => {
            await renderLoadedExplore();
            fireEvent.change(getSaleTypeSelect(), { target: { value: "auction" } });
            expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
            expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
            expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
        });
    });

    // ── Author filter ──
    describe("Author filter", () => {
        test("renders author options from listings", async () => {
            await renderLoadedExplore();
            expect(screen.getByRole("option", { name: "All Authors" })).toBeInTheDocument();
            expect(screen.getByRole("option", { name: "Dan Brown" })).toBeInTheDocument();
            expect(screen.getByRole("option", { name: "J.K. Rowling" })).toBeInTheDocument();
            expect(screen.getByRole("option", { name: "George R.R. Martin" })).toBeInTheDocument();
        });

        test("filters by author", async () => {
            await renderLoadedExplore();
            fireEvent.change(getAuthorSelect(), { target: { value: "J.K. Rowling" } });
            expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
            expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
            expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
        });
    });

    // ── Combined filters ──
    describe("Combined filters", () => {
        test("sale type and author work together", async () => {
            await renderLoadedExplore();
            fireEvent.change(getSaleTypeSelect(), { target: { value: "direct" } });
            fireEvent.change(getAuthorSelect(), { target: { value: "George R.R. Martin" } });
            expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
            expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
            expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
        });

        test("genre filter combined with sale type filter", async () => {
            await renderLoadedExplore();
            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            await waitFor(() => {
                expect(screen.getByText("The Lord of the Rings - Auction")).toBeInTheDocument();
            });

            fireEvent.change(getSaleTypeSelect(), { target: { value: "direct" } });

            expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
            expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
        });

        test("no results when filters produce empty set", async () => {
            await renderLoadedExplore();
            const search = screen.getByPlaceholderText("Search by title or author...");
            fireEvent.change(search, { target: { value: "xyznotfound" } });
            expect(screen.getByText(/No items found/i)).toBeInTheDocument();
        });
    });

    // ── Search ──
    describe("Search", () => {
        test("filters by title search", async () => {
            await renderLoadedExplore();
            fireEvent.change(
                screen.getByPlaceholderText("Search by title or author..."),
                { target: { value: "Da Vinci" } }
            );
            expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
            expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
        });

        test("filters by author search", async () => {
            await renderLoadedExplore();
            fireEvent.change(
                screen.getByPlaceholderText("Search by title or author..."),
                { target: { value: "Rowling" } }
            );
            expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
            expect(screen.queryByText("Game of Thrones")).not.toBeInTheDocument();
        });

        test("search is case insensitive", async () => {
            await renderLoadedExplore();
            fireEvent.change(
                screen.getByPlaceholderText("Search by title or author..."),
                { target: { value: "da vinci" } }
            );
            expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        });
    });

    // ── Cart ──
    describe("Cart actions", () => {
        test("calls addToCart when Add to Cart clicked on direct sale", async () => {
            await renderLoadedExplore();
            fireEvent.change(getSaleTypeSelect(), { target: { value: "direct" } });
            const buttons = screen.getAllByText("Add to Cart");
            fireEvent.click(buttons[0]);
            expect(mockAddToCart).toHaveBeenCalledTimes(1);
        });
    });
});