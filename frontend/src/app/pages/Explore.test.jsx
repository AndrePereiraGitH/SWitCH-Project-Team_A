import { beforeEach, describe, test, expect, vi } from "vitest";
import { screen, fireEvent, waitFor, render } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { Explore } from "./Explore";

// ── Mocks ─────────────────────────────────────────────────────────────────────

// Mock the cart context directly so we don't need the real CartProvider
const mockAddToCart = vi.fn();
vi.mock("../components/CartContext", () => ({
    useCart: () => ({ addToCart: mockAddToCart })
}));

// Optional: Mock the constants file to ensure consistent test environments
vi.mock("../../api/constants", () => ({
    API_BASE_URL: "http://localhost:8081",
    CURRENT_USER_ID: "user-123"
}));

const mockGenres = [
    { id: "mystery", genreName: "Mystery" },
    { id: "fantasy", genreName: "Fantasy" },
    { id: "technology", genreName: "Technology" },
];

const mockListings = [
    {
        id: "1",
        title: "The Da Vinci Code",
        listingTitle: "The Da Vinci Code",
        authors: ["Dan Brown"],
        genre: ["mystery"],
        price: 12.5,
        condition: "Good",
        saleType: "DIRECT",
    },
    {
        id: "2",
        title: "Harry Potter and the Philosopher's Stone",
        listingTitle: "Harry Potter and the Philosopher's Stone",
        authors: ["J.K. Rowling"],
        genre: ["fantasy"],
        price: 18,
        condition: "Very Good",
        saleType: "AUCTION",
    },
    {
        id: "3",
        title: "Game of Thrones",
        listingTitle: "Game of Thrones",
        authors: ["George R.R. Martin"],
        genre: ["fantasy"],
        price: 15,
        condition: "Good",
        saleType: "DIRECT",
    },
];

const mockAuctions = [
    {
        auctionId: "auction-001",
        listingId: "2",
        initialPrice: "18.00",
        buyoutPrice: null,
        currentHighestBid: null,
        auctionStartDateTime: "2026-06-01T10:00:00",
        auctionEndDateTime: "2026-06-20T10:00:00",
        auctionStatus: "ACTIVE",
    }
];

// ── Setup ─────────────────────────────────────────────────────────────────────

describe("Explore", () => {
    beforeEach(() => {
        vi.clearAllMocks();

        // Globally mock fetch for the exact calls Explore.jsx expects
        global.fetch = vi.fn(async (url) => {
            if (url.includes("/genres")) {
                return { ok: true, json: async () => mockGenres };
            }
            // FIX: Changed from /sales to /listings
            if (url.includes("/listings")) {
                return { ok: true, json: async () => mockListings };
            }
            if (url.includes("/auctions")) {
                return { ok: true, json: async () => mockAuctions };
            }
            return { ok: false, status: 404 };
        });
    });

    async function renderLoadedExplore() {
        render(
            <MemoryRouter>
                <Explore />
            </MemoryRouter>
        );

        // Wait for the loading state to disappear (if you add one, otherwise this is a quick safety check)
        await waitFor(() => {
            expect(screen.queryByText(/Loading/i)).not.toBeInTheDocument();
        });

        // Guarantee data is painted
        await waitFor(() => {
            expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
        });
    }

    const getGenreSelect = () => screen.getAllByRole("combobox")[0];
    const getSaleTypeSelect = () => {
        const selects = screen.getAllByRole("combobox");
        return selects[selects.length - 1]; // Assume the last combobox is Sale Type
    };

    // ── API loading ──
    describe("API loading", () => {
        test("fetches genres, listings and auctions on mount", async () => {
            await renderLoadedExplore();

            await waitFor(() => {
                expect(global.fetch).toHaveBeenCalledTimes(3);
            });

            expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/genres"));
            // FIX: Ensure test expects the correct URL string check too
            expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/listings"));
            expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining("/auctions"));
        });
    });

    // ── Filtering & Search ──
    describe("Filtering and Search", () => {
        test("filters by genre", async () => {
            await renderLoadedExplore();

            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });

            expect(screen.getByText(/Harry Potter/i)).toBeInTheDocument();
            expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
            expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
        });

        test("filters by sale type", async () => {
            await renderLoadedExplore();

            fireEvent.change(getSaleTypeSelect(), { target: { value: "direct" } });

            expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
            expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
            expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
        });

        test("genre and sale type work together", async () => {
            await renderLoadedExplore();

            fireEvent.change(getGenreSelect(), { target: { value: "fantasy" } });
            fireEvent.change(getSaleTypeSelect(), { target: { value: "direct" } });

            expect(screen.getByText("Game of Thrones")).toBeInTheDocument();
            expect(screen.queryByText(/Harry Potter/i)).not.toBeInTheDocument();
            expect(screen.queryByText("The Da Vinci Code")).not.toBeInTheDocument();
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
});