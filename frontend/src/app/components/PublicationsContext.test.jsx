import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { usePublications } from "./PublicationsContext";

// Provide minimal valid payloads that match what addPublication expects
const mockBookA = {
    title: "Book A",
    authorNames: ["AUTH1"],
    genreNames: ["GENRE1"],
    authorDisplayNames: ["Author A"],
    genreDisplayNames: ["Fiction"],
    mediaFormat: "Book",
    condition: "New",
    publisherName: "Pub A",
    originalLanguage: "en",
    publishedDate: "2024-01-01",
    editionNumber: "1",
    height: 10, width: 10, depth: 10, weight: 100, numberOfPages: 100,
    publicationTypeId: "pt-1",
    publisherId: "pub-1",
    editionLanguage: "en",
    typeOfBinding: "Paperback"
};

const mockBookB = {
    ...mockBookA,
    title: "Book B"
};

// Helper component that exposes publications context state to the screen
function TestPublications() {
    const { publications, addPublication, getPublicationsForSale, getAllPublications } = usePublications();
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
            <button onClick={() => addPublication(mockBookA)}>
                Add Book A
            </button>
            <button onClick={() => addPublication(mockBookB)}>
                Add Book B
            </button>
            <p data-testid="all-count">{getAllPublications().length}</p>
            <p data-testid="sale-count">{getPublicationsForSale().length}</p>
        </div>
    );
}

// Helper that also exposes sell actions, needs a publication id
function TestPublicationsWithActions() {
    const { publications, addPublication, setPublicationForSale } = usePublications();
    const first = publications[0];
    return (
        <div>
            <p data-testid="count">{publications.length}</p>
            {first && (
                <>
                    <span data-testid="first-title">{first.title}</span>
                    <span data-testid="first-forsale">{first.forSale ? "for-sale" : "not-for-sale"}</span>
                    <button onClick={() => setPublicationForSale(first.id, 15)}>Sell First</button>
                </>
            )}
            <button onClick={() => addPublication(mockBookA)}>
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

describe("PublicationsContext", () => {

    // Globally mock the fetch calls made by the PublicationsProvider
    beforeEach(() => {
        vi.clearAllMocks();

        global.fetch = vi.fn(async (url, options) => {
            if (url.includes("/publicationSamples") && (!options || options.method === "GET")) {
                return { ok: true, json: async () => [] };
            }
            // FIXED: Matches the URL currently being called in PublicationsContext.jsx
            if (url.includes("/bibliographicalDatas") || url.includes("/bibliographicalData/bibliographical-data")) {
                return { ok: true, json: async () => ({ bdId: "bd-mock" }) };
            }
            if (url.includes("/editions")) {
                return { ok: true, json: async () => ({ editionId: "ed-mock" }) };
            }
            if (url.includes("/publicationSamples") && options?.method === "POST") {
                return { ok: true, json: async () => ({ id: Math.random().toString() }) };
            }
            return { ok: false, status: 404, text: async () => "Not found" };
        });
    });

    // ── Initial State ──────────────────────────────────────────────────────────────

    describe("Initial State", () => {
        test("publications list starts empty", async () => {
            renderPublications();
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("0"));
        });

        test("getAllPublications returns empty array by default", async () => {
            renderPublications();
            await waitFor(() => expect(screen.getByTestId("all-count").textContent).toBe("0"));
        });

        test("getPublicationsForSale returns empty array by default", async () => {
            renderPublications();
            await waitFor(() => expect(screen.getByTestId("sale-count").textContent).toBe("0"));
        });
    });

    // ── Add Publication ────────────────────────────────────────────────────────────

    describe("Add Publication", () => {
        test("adding a publication increments the count to 1", async () => {
            renderPublications();
            fireEvent.click(screen.getByText("Add Book A"));
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));
        });

        test("added publication appears with correct title", async () => {
            renderPublications();
            fireEvent.click(screen.getByText("Add Book A"));
            expect(await screen.findByText("Book A")).toBeInTheDocument();
        });

        test("added publication is not for sale by default", async () => {
            renderPublications();
            fireEvent.click(screen.getByText("Add Book A"));
            const pub = await screen.findAllByText("not-for-sale");
            expect(pub.length).toBeGreaterThan(0);
        });

        test("adding two publications increments count to 2", async () => {
            renderPublications();
            fireEvent.click(screen.getByText("Add Book A"));
            fireEvent.click(screen.getByText("Add Book B"));
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("2"));
        });

        test("getAllPublications reflects the added publications", async () => {
            renderPublications();
            fireEvent.click(screen.getByText("Add Book A"));
            fireEvent.click(screen.getByText("Add Book B"));
            await waitFor(() => expect(screen.getByTestId("all-count").textContent).toBe("2"));
        });
    });

    // ── Set For Sale ───────────────────────────────────────────────────────────────

    describe("Set For Sale", () => {
        test("setPublicationForSale marks the publication as for sale", async () => {
            renderPublicationsWithActions();
            fireEvent.click(screen.getByText("Add Book A"));

            // Wait for the async save to finish and render the "Sell First" button
            const sellButton = await screen.findByText("Sell First");
            fireEvent.click(sellButton);

            await waitFor(() => expect(screen.getByTestId("first-forsale").textContent).toBe("for-sale"));
        });

        test("getPublicationsForSale count increases after setting for sale", async () => {
            renderPublications();
            await waitFor(() => expect(screen.getByTestId("sale-count").textContent).toBe("0"));
        });
    });

    // ── Error Handling ─────────────────────────────────────────────────────────────

    describe("Error Handling", () => {
        test("usePublications throws when used outside PublicationsProvider", () => {
            const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

            function ComponentWithoutProvider() {
                usePublications();
                return null;
            }

            expect(() => render(<ComponentWithoutProvider />)).toThrow(
                "usePublications must be used within PublicationsProvider"
            );

            consoleSpy.mockRestore();
        });
    });
});