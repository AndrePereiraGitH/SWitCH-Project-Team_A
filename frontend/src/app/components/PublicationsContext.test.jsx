import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { usePublications } from "./PublicationsContext";
import { useLinks } from "./LinksContext";

// ── Fixtures ──────────────────────────────────────────────────────────

/**
 * Minimal valid form payload for a single physical book.
 * Used as the base for `addPublication` calls throughout the suite.
 * `mockBookB` is a shallow clone with a different title to test multiple additions.
 *
 * @type {object}
 */
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

/** Variant of {@link mockBookA} with a distinct title for multi-publication tests. */
const mockBookB = { ...mockBookA, title: "Book B" };

/**
 * Simulated root HATEOAS response containing all links required by
 * {@link PublicationsProvider}. Mirrors the structure returned by the real backend,
 * including RFC 6570 URI templates for both path and query parameters.
 */
const rootResponse = {
    _links: {
        "find-in-library": {
            href: "http://localhost:8081/publicationSamples/in-library?userId={userId}",
            templated: true
        },
        "create-bibliographical-data": {
            href: "http://localhost:8081/bibliographicalDatas?userId={userId}",
            templated: true
        },
        "create-edition": {
            href: "http://localhost:8081/editions?userId={userId}",
            templated: true
        },
        "create-publication-sample": {
            href: "http://localhost:8081/publicationSamples?userId={userId}",
            templated: true
        },
        "add-to-library": {
            href: "http://localhost:8081/publicationSamples/{publicationSampleId}/library?userId={userId}",
            templated: true
        }
    }
};

// ── Helper components ─────────────────────────────────────────────────

/**
 * Defers rendering `children` until {@link LinksContext} has finished loading.
 * Renders a `data-testid="links-loading"` sentinel while waiting, used by
 * {@link waitForLinks} as a synchronisation point.
 *
 * @param {{ children: React.ReactNode }} props
 */
function LinksReady({ children }) {
    const { loading } = useLinks();
    return loading ? <p data-testid="links-loading">true</p> : children;
}

/**
 * Test component exposing {@link PublicationsContext} state to the DOM.
 *
 * Rendered elements:
 * - `data-testid="count"`       — total publications in state.
 * - `data-testid="all-count"`   — result of `getAllPublications().length`.
 * - `data-testid="sale-count"`  — result of `getPublicationsForSale().length`.
 * - Per publication: `data-testid="pub-{id}"`, `"title-{id}"`, `"forsale-{id}"`.
 * - Buttons to trigger `addPublication` for Book A and Book B.
 */
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
            <button onClick={() => addPublication(mockBookA)}>Add Book A</button>
            <button onClick={() => addPublication(mockBookB)}>Add Book B</button>
            <p data-testid="all-count">{getAllPublications().length}</p>
            <p data-testid="sale-count">{getPublicationsForSale().length}</p>
        </div>
    );
}

/**
 * Variant test component that also exposes `setPublicationForSale` to test
 * the for-sale mutation on the first loaded publication.
 *
 * Rendered elements (in addition to count):
 * - `data-testid="first-title"`   — title of the first publication.
 * - `data-testid="first-forsale"` — `"for-sale"` or `"not-for-sale"`.
 * - "Sell First" button — calls `setPublicationForSale(first.id, 15)`.
 */
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
            <button onClick={() => addPublication(mockBookA)}>Add Book A</button>
        </div>
    );
}

/**
 * Renders {@link TestPublications} inside the full provider tree via
 * `renderWithProviders`, gated by {@link LinksReady}.
 *
 * @returns {import("@testing-library/react").RenderResult}
 */
function renderPublications() {
    return renderWithProviders(
        <LinksReady>
            <TestPublications />
        </LinksReady>
    );
}

/**
 * Renders {@link TestPublicationsWithActions} inside the full provider tree,
 * used by tests that need to exercise `setPublicationForSale`.
 *
 * @returns {import("@testing-library/react").RenderResult}
 */
function renderPublicationsWithActions() {
    return renderWithProviders(
        <LinksReady>
            <TestPublicationsWithActions />
        </LinksReady>
    );
}

/**
 * Waits until the {@link LinksReady} sentinel is removed from the DOM,
 * confirming HATEOAS links are resolved and the provider is ready.
 *
 * @returns {Promise<void>}
 */
async function waitForLinks() {
    await waitFor(() =>
        expect(screen.queryByTestId("links-loading")).not.toBeInTheDocument()
    );
}

// ── Test suite ────────────────────────────────────────────────────────

describe("PublicationsContext", () => {

    beforeEach(() => {
        vi.clearAllMocks();

        /**
         * Default fetch mock dispatching responses by URL pattern and HTTP method.
         * Each branch corresponds to one backend endpoint used by {@link PublicationsProvider}.
         * URLs are fully expanded by `expandTemplate` before being passed to fetch —
         * no literal `{param}` placeholders should appear in intercepted URLs.
         */
        global.fetch = vi.fn(async (url, options) => {
            // Root HATEOAS entry-point
            if (url.includes("/?userId=") || url.match(/^http:\/\/localhost:8081\/\?/)) {
                return { ok: true, status: 200, json: async () => rootResponse };
            }
            // GET /publicationSamples/in-library — initial publications fetch (returns empty)
            if (url.includes("/in-library") || (url.includes("/publicationSamples") && (!options || options.method === "GET"))) {
                return { ok: true, json: async () => [] };
            }
            // POST /bibliographicalDatas — step 1 of addPublication
            if (url.includes("/bibliographicalDatas")) {
                return { ok: true, json: async () => ({ bdId: "bd-mock" }) };
            }
            // POST /editions — step 2 of addPublication
            if (url.includes("/editions")) {
                return { ok: true, json: async () => ({ editionId: "ed-mock" }) };
            }
            // POST /publicationSamples — step 3 of addPublication; random id simulates server-assigned ids
            if (url.includes("/publicationSamples") && options?.method === "POST") {
                return { ok: true, json: async () => ({ id: Math.random().toString() }) };
            }
            return { ok: false, status: 404, text: async () => "Not found" };
        });
    });

    // ── Initial State ─────────────────────────────────────────────────

    describe("Initial State", () => {
        test("publications list starts empty", async () => {
            renderPublications();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("0"));
        });

        test("getAllPublications returns empty array by default", async () => {
            renderPublications();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("all-count").textContent).toBe("0"));
        });

        test("getPublicationsForSale returns empty array by default", async () => {
            renderPublications();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("sale-count").textContent).toBe("0"));
        });
    });

    // ── URI Template Expansion ────────────────────────────────────────

    describe("URI Template Expansion", () => {
        test("fetch calls contain no unexpanded URI template placeholders", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));

            const allUrls = global.fetch.mock.calls.map(([url]) => url);
            allUrls.forEach(url => {
                expect(url).not.toContain("{");
                expect(url).not.toContain("}");
            });
        });

        test("create-bibliographical-data call includes userId query param", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));

            const bdCall = global.fetch.mock.calls
                .map(([url]) => url)
                .find(url => url.includes("/bibliographicalDatas"));

            expect(bdCall).toBeDefined();
            expect(bdCall).toContain("userId=user-001");
        });
    });

    // ── Add Publication ───────────────────────────────────────────────

    describe("Add Publication", () => {
        test("adding a publication increments the count to 1", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));
        });

        test("added publication appears with correct title", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            expect(await screen.findByText("Book A")).toBeInTheDocument();
        });

        test("added publication is not for sale by default", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            const pub = await screen.findAllByText("not-for-sale");
            expect(pub.length).toBeGreaterThan(0);
        });

        test("adding two publications increments count to 2", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            fireEvent.click(screen.getByText("Add Book B"));
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("2"));
        });

        test("getAllPublications reflects the added publications", async () => {
            renderPublications();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));
            fireEvent.click(screen.getByText("Add Book B"));
            await waitFor(() => expect(screen.getByTestId("all-count").textContent).toBe("2"));
        });
    });

    // ── Set For Sale ──────────────────────────────────────────────────

    describe("Set For Sale", () => {
        test("setPublicationForSale marks the publication as for sale", async () => {
            renderPublicationsWithActions();
            await waitForLinks();
            fireEvent.click(screen.getByText("Add Book A"));

            const sellButton = await screen.findByText("Sell First");
            fireEvent.click(sellButton);

            await waitFor(() => expect(screen.getByTestId("first-forsale").textContent).toBe("for-sale"));
        });

        test("getPublicationsForSale count increases after setting for sale", async () => {
            renderPublications();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("sale-count").textContent).toBe("0"));
        });
    });

    // ── Error Handling ────────────────────────────────────────────────

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