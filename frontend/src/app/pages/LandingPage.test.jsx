import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, waitFor, renderWithProviders } from "../../test-utils";
import { LandingPage } from "./LandingPage";

// ── Mocks ─────────────────────────────────────────────────────────────────────

vi.mock("../../api/constants", () => ({
    API_BASE_URL: "http://localhost:8081",
    CURRENT_USER_ID: "user-123"
}));

const mockGenres = [
    { id: "fiction", genreName: "Fiction" },
    { id: "mystery", genreName: "Mystery" },
];

const mockListings = [
    {
        id: "1",
        listingTitle: "The Great Gatsby",
        authors: ["F. Scott Fitzgerald"],
        genre: ["fiction"],
        price: 12.5,
        condition: "Very Good",
        saleType: "DIRECT",
    },
    {
        id: "2",
        listingTitle: "1984",
        authors: ["George Orwell"],
        genre: ["fiction"],
        price: 15,
        condition: "Good",
        saleType: "AUCTION",
    },
];

beforeEach(() => {
    vi.clearAllMocks();
    global.fetch = vi.fn(async (url) => {
        if (url.includes("/genres")) {
            return { ok: true, json: async () => mockGenres };
        }
        if (url.includes("/listings")) {
            return { ok: true, json: async () => mockListings };
        }
        return { ok: false, status: 404 };
    });
});

async function renderLanding() {
    renderWithProviders(<LandingPage />);
    await waitFor(() => {
        expect(screen.getByText("The Great Gatsby")).toBeInTheDocument();
    });
}

// ── Hero Section ───────────────────────────────────────────────────────────────

describe("LandingPage — Hero", () => {

    test("shows the main heading", async () => {
        await renderLanding();
        expect(screen.getByText("Share the Passion")).toBeInTheDocument();
    });

    test("shows the highlighted subtitle", async () => {
        await renderLanding();
        expect(screen.getByText("for Reading")).toBeInTheDocument();
    });

    test("shows the search input", async () => {
        await renderLanding();
        expect(
            screen.getByPlaceholderText("Search by title, author, ISBN...")
        ).toBeInTheDocument();
    });

    test("shows the Buy Now button", async () => {
        await renderLanding();
        expect(screen.getByRole("link", { name: "Buy Now" })).toBeInTheDocument();
    });

    test("shows the Sell Now button", async () => {
        await renderLanding();
        expect(screen.getByRole("link", { name: "Sell Now" })).toBeInTheDocument();
    });

    test("Buy Now links to /explore", async () => {
        await renderLanding();
        expect(screen.getByRole("link", { name: "Buy Now" })).toHaveAttribute("href", "/explore");
    });

    test("Sell Now links to /dashboard", async () => {
        await renderLanding();
        expect(screen.getByRole("link", { name: "Sell Now" })).toHaveAttribute("href", "/dashboard");
    });

});

// ── Explore by Category ────────────────────────────────────────────────────────

describe("LandingPage — Categories", () => {

    test("shows the section heading", async () => {
        await renderLanding();
        expect(screen.getByText("Explore by Category")).toBeInTheDocument();
    });

    test("shows the Fiction category", async () => {
        await renderLanding();
        expect(screen.getAllByText("Fiction").length).toBeGreaterThan(0);
    });

    test("shows the Science category", async () => {
        await renderLanding();
        expect(screen.getByText("Science")).toBeInTheDocument();
    });

    test("shows all 6 categories", async () => {
        await renderLanding();
        ["Non-Fiction", "Science", "History", "Art", "Children"].forEach(cat => {
            expect(screen.getByText(cat)).toBeInTheDocument();
        });
        expect(screen.getAllByText("Fiction").length).toBeGreaterThan(0);
    });

});

// ── How It Works ───────────────────────────────────────────────────────────────

describe("LandingPage — How It Works", () => {

    test("shows the section heading", async () => {
        await renderLanding();
        expect(screen.getByText("How It Works")).toBeInTheDocument();
    });

    test("shows the three feature cards: Buy & Sell, Organize, Community", async () => {
        await renderLanding();
        expect(screen.getByText("Buy & Sell")).toBeInTheDocument();
        expect(screen.getByText("Organize")).toBeInTheDocument();
        expect(screen.getByText("Community")).toBeInTheDocument();
    });

});

// ── Items For Sale ─────────────────────────────────────────────────────────────

describe("LandingPage — Items For Sale", () => {

    test("shows the Items For Sale section", async () => {
        await renderLanding();
        expect(screen.getByText("Items For Sale")).toBeInTheDocument();
    });

    test("View All link points to /explore", async () => {
        await renderLanding();
        expect(screen.getByRole("link", { name: /View All/i })).toHaveAttribute("href", "/explore");
    });

    test("shows The Great Gatsby from API data", async () => {
        await renderLanding();
        expect(screen.getByText("The Great Gatsby")).toBeInTheDocument();
    });

    test("shows 1984 from API data", async () => {
        await renderLanding();
        expect(screen.getByText("1984")).toBeInTheDocument();
    });

    test("shows Add to Cart button for direct sale items", async () => {
        await renderLanding();
        expect(screen.getAllByText("Add to Cart").length).toBeGreaterThan(0);
    });

    test("shows Make Offer button for auction items", async () => {
        await renderLanding();
        expect(screen.getAllByText("Make Offer").length).toBeGreaterThan(0);
    });

    test("Sold button is disabled for purchased items", async () => {
        await renderLanding();
        const soldButtons = screen.queryAllByRole("button", { name: /sold/i });
        soldButtons.forEach(btn => expect(btn).toBeDisabled());
    });

});

// ── Why MiteLovers ─────────────────────────────────────────────────────────────

describe("LandingPage — Why MiteLovers", () => {

    test("shows the section heading", async () => {
        await renderLanding();
        expect(screen.getByText("Why MiteLovers?")).toBeInTheDocument();
    });

    test("shows the three values: Sustainability, Security, Affordable Access", async () => {
        await renderLanding();
        expect(screen.getByText("Sustainability")).toBeInTheDocument();
        expect(screen.getByText("Security")).toBeInTheDocument();
        expect(screen.getByText("Affordable Access")).toBeInTheDocument();
    });

});

// ── Footer ─────────────────────────────────────────────────────────────────────

describe("LandingPage — Footer", () => {

    test("shows the brand name in the footer", async () => {
        await renderLanding();
        const instances = screen.getAllByText("MiteLovers");
        expect(instances.length).toBeGreaterThan(0);
    });

    test("shows the copyright notice", async () => {
        await renderLanding();
        expect(screen.getByText(/© 2026 MiteLovers/i)).toBeInTheDocument();
    });

});