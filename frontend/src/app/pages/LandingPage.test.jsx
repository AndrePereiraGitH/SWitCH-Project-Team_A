import { describe, test, expect } from "vitest";
import { screen, renderWithProviders } from "../../test-utils";
import { LandingPage } from "./LandingPage";

// renderWithProviders includes CartProvider (needed for addToCart) and BrowserRouter
// (needed for <Link> and useNavigate inside the page)

function renderLanding() {
    return renderWithProviders(<LandingPage />);
}

// ── Hero Section ───────────────────────────────────────────────────────────────

describe("LandingPage — Hero", () => {

    test("shows the main heading", () => {
        renderLanding();
        expect(screen.getByText("Share the Passion")).toBeInTheDocument();
    });

    test("shows the highlighted subtitle", () => {
        renderLanding();
        expect(screen.getByText("for Reading")).toBeInTheDocument();
    });

    test("shows the search input", () => {
        renderLanding();
        expect(
            screen.getByPlaceholderText("Search by title, author, ISBN...")
        ).toBeInTheDocument();
    });

    test("shows the Buy Now button", () => {
        renderLanding();
        expect(screen.getByRole("link", { name: "Buy Now" })).toBeInTheDocument();
    });

    test("shows the Sell Now button", () => {
        renderLanding();
        expect(screen.getByRole("link", { name: "Sell Now" })).toBeInTheDocument();
    });

    test("Buy Now links to /explore", () => {
        renderLanding();
        expect(screen.getByRole("link", { name: "Buy Now" })).toHaveAttribute("href", "/explore");
    });

    test("Sell Now links to /dashboard", () => {
        renderLanding();
        expect(screen.getByRole("link", { name: "Sell Now" })).toHaveAttribute("href", "/dashboard");
    });

});

// ── Explore by Category ────────────────────────────────────────────────────────

describe("LandingPage — Categories", () => {

    test("shows the section heading", () => {
        renderLanding();
        expect(screen.getByText("Explore by Category")).toBeInTheDocument();
    });

    test("shows the Fiction category", () => {
        renderLanding();
        // "Fiction" also appears in mock sales genre, so we use getAllByText
        expect(screen.getAllByText("Fiction").length).toBeGreaterThan(0);
    });

    test("shows the Science category", () => {
        renderLanding();
        expect(screen.getByText("Science")).toBeInTheDocument();
    });

    test("shows all 6 categories", () => {
        renderLanding();
        // Categories that are unique on the page
        ["Non-Fiction", "Science", "History", "Art", "Children"].forEach(cat => {
            expect(screen.getByText(cat)).toBeInTheDocument();
        });
        // Fiction appears at least once (also in mock sales)
        expect(screen.getAllByText("Fiction").length).toBeGreaterThan(0);
    });

});

// ── How It Works ───────────────────────────────────────────────────────────────

describe("LandingPage — How It Works", () => {

    test("shows the section heading", () => {
        renderLanding();
        expect(screen.getByText("How It Works")).toBeInTheDocument();
    });

    test("shows the three feature cards: Buy & Sell, Organize, Community", () => {
        renderLanding();
        expect(screen.getByText("Buy & Sell")).toBeInTheDocument();
        expect(screen.getByText("Organize")).toBeInTheDocument();
        expect(screen.getByText("Community")).toBeInTheDocument();
    });

});

// ── Items For Sale (mock data) ─────────────────────────────────────────────────

describe("LandingPage — Items For Sale", () => {

    test("shows the Items For Sale section", () => {
        renderLanding();
        expect(screen.getByText("Items For Sale")).toBeInTheDocument();
    });

    test("View All link points to /explore", () => {
        renderLanding();
        expect(screen.getByRole("link", { name: /View All/i })).toHaveAttribute("href", "/explore");
    });

    test("shows The Great Gatsby from mock data", () => {
        renderLanding();
        expect(screen.getByText("The Great Gatsby")).toBeInTheDocument();
    });

    test("shows Add to Cart buttons for direct sales", () => {
        renderLanding();
        const cartBtns = screen.getAllByText("Add to Cart");
        expect(cartBtns.length).toBeGreaterThan(0);
    });

    test("shows Make Offer buttons for auctions", () => {
        renderLanding();
        const offerBtns = screen.getAllByText("Make Offer");
        expect(offerBtns.length).toBeGreaterThan(0);
    });

});

// ── Why MiteLovers ─────────────────────────────────────────────────────────────

describe("LandingPage — Why MiteLovers", () => {

    test("shows the section heading", () => {
        renderLanding();
        expect(screen.getByText("Why MiteLovers?")).toBeInTheDocument();
    });

    test("shows the three values: Sustainability, Security, Affordable Access", () => {
        renderLanding();
        expect(screen.getByText("Sustainability")).toBeInTheDocument();
        expect(screen.getByText("Security")).toBeInTheDocument();
        expect(screen.getByText("Affordable Access")).toBeInTheDocument();
    });

});

// ── Footer ─────────────────────────────────────────────────────────────────────

describe("LandingPage — Footer", () => {

    test("shows the brand name in the footer", () => {
        renderLanding();
        // getAllByText because the logo also renders MiteLovers
        const instances = screen.getAllByText("MiteLovers");
        expect(instances.length).toBeGreaterThan(0);
    });

    test("shows the copyright notice", () => {
        renderLanding();
        expect(screen.getByText(/© 2026 MiteLovers/i)).toBeInTheDocument();
    });

});
