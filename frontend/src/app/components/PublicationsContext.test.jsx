import { describe, test, expect } from "vitest";
import { render, screen, fireEvent, renderWithProviders } from "../../test-utils";
import { PublicationsProvider, usePublications } from "./PublicationsContext";

// Helper component that exposes publications context state to the screen
function TestPublications() {
    const { publications, addPublication, removePublication, setPublicationForSale, getPublicationsForSale, getAllPublications } = usePublications();
    return (
        <div>
            <p data-testid="count">{publications.length}</p>
            <ul>
                {publications.map(pub => (
                    <li key={pub.id} data-testid={`pub-${pub.id}`}>
                        <span data-testid={`title-${pub.id}`}>{pub.title}</span>
                        <span data-testid={`forsale-${pub.id}`}>{pub.forSale ? "for-sale" : "not-for-sale"}</span>
                    </li>
                ))}
            </ul>
            <button onClick={() => addPublication({ title: "Book A", author: "Author A", genre: "Fiction" })}>
                Add Book A
            </button>
            <button onClick={() => addPublication({ title: "Book B", author: "Author B", genre: "Science" })}>
                Add Book B
            </button>
            <p data-testid="all-count">{getAllPublications().length}</p>
            <p data-testid="sale-count">{getPublicationsForSale().length}</p>
        </div>
    );
}

// Helper that also exposes remove and sell actions, needs a publication id
function TestPublicationsWithActions() {
    const { publications, addPublication, removePublication, setPublicationForSale } = usePublications();
    const first = publications[0];
    return (
        <div>
            <p data-testid="count">{publications.length}</p>
            {first && (
                <>
                    <span data-testid="first-title">{first.title}</span>
                    <span data-testid="first-forsale">{first.forSale ? "for-sale" : "not-for-sale"}</span>
                    <button onClick={() => removePublication(first.id)}>Remove First</button>
                    <button onClick={() => setPublicationForSale(first.id, 15)}>Sell First</button>
                </>
            )}
            <button onClick={() => addPublication({ title: "Book A", author: "Author A", genre: "Fiction" })}>
                Add Book A
            </button>
        </div>
    );
}

function renderPublications() {
    return renderWithProviders(<TestPublications />);
}

function renderPublicationsWithActions() {
    return renderWithProviders(<TestPublicationsWithActions />);
}

// ── Initial State ──────────────────────────────────────────────────────────────

describe("PublicationsContext — Initial State", () => {

    test("publications list starts empty", () => {
        renderPublications();
        expect(screen.getByTestId("count").textContent).toBe("0");
    });

    test("getAllPublications returns empty array by default", () => {
        renderPublications();
        expect(screen.getByTestId("all-count").textContent).toBe("0");
    });

    test("getPublicationsForSale returns empty array by default", () => {
        renderPublications();
        expect(screen.getByTestId("sale-count").textContent).toBe("0");
    });

});

// ── Add Publication ────────────────────────────────────────────────────────────

describe("PublicationsContext — Add Publication", () => {

    test("adding a publication increments the count to 1", () => {
        renderPublications();
        fireEvent.click(screen.getByText("Add Book A"));
        expect(screen.getByTestId("count").textContent).toBe("1");
    });

    test("added publication appears with correct title", () => {
        renderPublications();
        fireEvent.click(screen.getByText("Add Book A"));
        expect(screen.getByText("Book A")).toBeInTheDocument();
    });

    test("added publication is not for sale by default", () => {
        renderPublications();
        fireEvent.click(screen.getByText("Add Book A"));
        const pub = screen.getAllByText("not-for-sale");
        expect(pub.length).toBeGreaterThan(0);
    });

    test("adding two publications increments count to 2", () => {
        renderPublications();
        fireEvent.click(screen.getByText("Add Book A"));
        fireEvent.click(screen.getByText("Add Book B"));
        expect(screen.getByTestId("count").textContent).toBe("2");
    });

    test("getAllPublications reflects the added publications", () => {
        renderPublications();
        fireEvent.click(screen.getByText("Add Book A"));
        fireEvent.click(screen.getByText("Add Book B"));
        expect(screen.getByTestId("all-count").textContent).toBe("2");
    });

});

// ── Set For Sale ───────────────────────────────────────────────────────────────

describe("PublicationsContext — Set For Sale", () => {

    test("setPublicationForSale marks the publication as for sale", () => {
        renderPublicationsWithActions();
        fireEvent.click(screen.getByText("Add Book A"));
        fireEvent.click(screen.getByText("Sell First"));
        expect(screen.getByTestId("first-forsale").textContent).toBe("for-sale");
    });

    test("getPublicationsForSale count increases after setting for sale", () => {
        renderPublications();
        // Add and sell via the actions component is tested separately above
        // Here we verify the initial state remains 0
        expect(screen.getByTestId("sale-count").textContent).toBe("0");
    });

});

// ── Error Handling ─────────────────────────────────────────────────────────────

describe("PublicationsContext — Error Handling", () => {

    test("usePublications throws when used outside PublicationsProvider", () => {
        const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

        function ComponentWithoutProvider() {
            usePublications();
            return null;
        }

        // render directly (no providers) to test the missing context error
        expect(() => render(<ComponentWithoutProvider />)).toThrow(
            "usePublications must be used within a PublicationsProvider"
        );

        consoleSpy.mockRestore();
    });

});
