import { describe, test, expect } from "vitest";
import { screen, renderWithProviders } from "../../test-utils";
import { Dashboard } from "./Dashboard";

// renderWithProviders wraps with all required providers (Auth, Publications,
// Library, Cart, Lists) and BrowserRouter — no additional setup needed here.

function renderDashboard() {
    return renderWithProviders(<Dashboard />);
}

// ── Rendering ──────────────────────────────────────────────────────────────────

describe("Dashboard — Rendering", () => {

    test("shows the main heading", () => {
        renderDashboard();
        expect(screen.getByText("My Dashboard")).toBeInTheDocument();
    });

    test("shows the page description", () => {
        renderDashboard();
        expect(screen.getByText("Manage your library and sales")).toBeInTheDocument();
    });

    test("shows the back to Home link", () => {
        renderDashboard();
        expect(screen.getByText("Home")).toBeInTheDocument();
    });

});

// ── Stats ──────────────────────────────────────────────────────────────────────

describe("Dashboard — Stats", () => {

    // All counters start at 0 because providers are initialised empty
    test("shows 'Books in Library' label", () => {
        renderDashboard();
        expect(screen.getByText("Books in Library")).toBeInTheDocument();
    });

    test("shows 'Active Lists' label", () => {
        renderDashboard();
        expect(screen.getByText("Active Lists")).toBeInTheDocument();
    });

    test("shows 'Items For Sale' label", () => {
        renderDashboard();
        expect(screen.getByText("Items For Sale")).toBeInTheDocument();
    });

    test("all three counters start at 0", () => {
        renderDashboard();
        const zeros = screen.getAllByText("0");
        expect(zeros.length).toBeGreaterThanOrEqual(3);
    });

});

// ── Quick Actions ──────────────────────────────────────────────────────────────

describe("Dashboard — Quick Actions", () => {

    test("shows Add Publication link", () => {
        renderDashboard();
        expect(screen.getByText("Add Publication")).toBeInTheDocument();
    });

    test("shows My Publications link", () => {
        renderDashboard();
        expect(screen.getByText("My Publications")).toBeInTheDocument();
    });

    test("shows View Library link", () => {
        renderDashboard();
        expect(screen.getByText("View Library")).toBeInTheDocument();
    });

    test("shows Manage Lists link", () => {
        renderDashboard();
        expect(screen.getByText("Manage Lists")).toBeInTheDocument();
    });

    test("shows Explore Sales link", () => {
        renderDashboard();
        expect(screen.getByText("Explore Sales")).toBeInTheDocument();
    });

});

// ── Recent Activity ────────────────────────────────────────────────────────────

describe("Dashboard — Recent Activity", () => {

    test("shows the Recent Activity section", () => {
        renderDashboard();
        expect(screen.getByText("Recent Activity")).toBeInTheDocument();
    });

    test("shows empty state message when there is no activity yet", () => {
        renderDashboard();
        expect(
            screen.getByText("Start by adding publications to your library")
        ).toBeInTheDocument();
    });

});
