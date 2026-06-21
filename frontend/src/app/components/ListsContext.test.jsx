import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { useLists } from "./ListsContext";
import { useLinks } from "./LinksContext";

// ── Root HATEOAS fixture ──────────────────────────────────────────────

/**
 * Simulated root response from the API, containing all HATEOAS links needed
 * by {@link ListsContext}. Mirrors the structure returned by the real backend.
 */
const rootResponse = {
    _links: {
        "find-own-collections": {
            href: "http://localhost:8081/collection-of-publications/owner?userId={userId}",
            templated: true
        },
        "create-collection-of-publications": {
            href: "http://localhost:8081/collection-of-publications/?ownerId={ownerId}",
            templated: true
        },
        "make-collection-public": {
            href: "http://localhost:8081/collection-of-publications/{collectionId}/make-public?userId={userId}",
            templated: true
        },
        "make-collection-private": {
            href: "http://localhost:8081/collection-of-publications/{collectionId}/make-private?userId={userId}",
            templated: true
        },
        "make-collection-public-until": {
            href: "http://localhost:8081/collection-of-publications/{collectionId}/make-public-until?publicUntil={publicUntil}&userId={userId}",
            templated: true
        },
        "assign-to-collection": {
            href: "http://localhost:8081/publicationSamples/{publicationSampleId}/collection?collectionId={collectionId}&userId={userId}",
            templated: true
        }
    }
};

// ── Helper components ─────────────────────────────────────────────────

/**
 * Wrapper that defers rendering `children` until {@link LinksContext} has
 * finished loading. Renders a `data-testid="links-loading"` sentinel while
 * waiting, which tests use as a synchronisation point via {@link waitForLinks}.
 *
 * @param {{ children: React.ReactNode }} props
 */
function LinksReady({ children }) {
    const { loading } = useLinks();
    return loading ? <p data-testid="links-loading">true</p> : children;
}

/**
 * Test component that exposes {@link ListsContext} state and actions to the
 * DOM so assertions can be made through rendered text and button clicks.
 *
 * Rendered elements:
 * - `data-testid="count"` — total number of lists in state.
 * - `data-testid="first-id"` — `collectionId` of the first list.
 * - `data-testid="first-name"` — `collectionName` of the first list.
 * - `data-testid="first-public"` — `"public"` or `"private"` for the first list.
 * - `data-testid="specific-list"` — name of the list with id `"list-1"`, or `"none"`.
 * - Buttons to trigger `makePublic`, `makePublicUntil`, `addPublicationToList`, and `addList`.
 */
function TestLists() {
    const {
        lists,
        addList,
        makePublic,
        makePublicUntil,
        addPublicationToList,
        getListById
    } = useLists();

    const first = lists[0];
    const specificList = getListById("list-1");

    return (
        <div>
            <p data-testid="count">{lists.length}</p>
            {first && (
                <>
                    <span data-testid="first-id">{first.collectionId}</span>
                    <span data-testid="first-name">{first.collectionName}</span>
                    <span data-testid="first-public">{first.private ? "private" : "public"}</span>
                    <button onClick={() => makePublic(first.collectionId)}>Make Public</button>
                    <button onClick={() => makePublicUntil(first.collectionId, "2026-12-31")}>Make Public Until</button>
                    <button onClick={() => addPublicationToList(first.collectionId, "pub-1")}>Add Pub to First</button>
                </>
            )}
            <span data-testid="specific-list">{specificList ? specificList.collectionName : "none"}</span>

            <button onClick={() => addList({ collectionName: "New List", genreId: "g1" })}>Add List</button>
        </div>
    );
}

/**
 * Renders {@link TestLists} inside the full provider tree (including
 * {@link ListsProvider} and {@link LinksContext}) using the shared
 * `renderWithProviders` test utility.
 *
 * @returns {import("@testing-library/react").RenderResult}
 */
function renderListsContext() {
    return renderWithProviders(
        <LinksReady>
            <TestLists />
        </LinksReady>
    );
}

/**
 * Waits until the {@link LinksReady} sentinel element is removed from the DOM,
 * indicating that {@link LinksContext} has finished loading and HATEOAS links
 * are available.
 *
 * @returns {Promise<void>}
 */
async function waitForLinks() {
    await waitFor(() =>
        expect(screen.queryByTestId("links-loading")).not.toBeInTheDocument()
    );
}

// ── Test suite ────────────────────────────────────────────────────────

describe("ListsContext", () => {

    beforeEach(() => {
        vi.clearAllMocks();

        /**
         * Global fetch mock that dispatches responses based on URL and HTTP method.
         * Each branch corresponds to one backend endpoint used by {@link ListsContext}.
         */
        global.fetch = vi.fn(async (url, options) => {
            // Root HATEOAS entry-point
            if (url.includes("/?userId=") || url.match(/^http:\/\/localhost:8081\/\?/)) {
                return { ok: true, status: 200, json: async () => rootResponse };
            }
            // GET /collection-of-publications/owner — initial list fetch
            if (url.includes("/collection-of-publications/owner") && (!options || options.method === "GET")) {
                return {
                    ok: true,
                    json: async () => [{ collectionId: "list-1", collectionName: "Initial List", private: true }]
                };
            }
            // PATCH /make-public-until — must be checked before /make-public (more specific path)
            if (url.includes("/make-public-until") && options?.method === "PATCH") {
                return {
                    ok: true,
                    json: async () => ({ collectionId: "list-1", collectionName: "Initial List", private: false })
                };
            }
            // PATCH /make-public
            if (url.includes("/make-public") && options?.method === "PATCH") {
                return {
                    ok: true,
                    json: async () => ({ collectionId: "list-1", collectionName: "Initial List", private: false })
                };
            }
            // PATCH /make-private
            if (url.includes("/make-private") && options?.method === "PATCH") {
                return {
                    ok: true,
                    json: async () => ({ collectionId: "list-1", collectionName: "Initial List", private: true })
                };
            }
            // PATCH /publicationSamples/{id}/collection — assign publication to list
            if (url.includes("/publicationSamples/") && url.includes("/collection") && options?.method === "PATCH") {
                return {
                    ok: true,
                    json: async () => ({ id: "pub-1" })
                };
            }
            // POST /collection-of-publications/ — create new list
            if (url.includes("/collection-of-publications/") && options?.method === "POST") {
                return {
                    ok: true,
                    json: async () => ({ collectionId: "list-2", collectionName: "New List", private: true })
                };
            }
            return { ok: false, status: 404, text: async () => "Not found" };
        });
    });

    // ── Initial State & Fetching ──────────────────────────────────────

    describe("Fetching and Initial State", () => {
        test("fetches and sets lists on mount", async () => {
            renderListsContext();
            await waitForLinks();

            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));
            expect(screen.getByTestId("first-name").textContent).toBe("Initial List");
            expect(screen.getByTestId("first-public").textContent).toBe("private");
        });

        test("getListById works correctly when data is loaded", async () => {
            renderListsContext();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("specific-list").textContent).toBe("Initial List"));
        });
    });

    // ── Add List ──────────────────────────────────────────────────────

    describe("Add List", () => {
        test("adds a new list and updates state", async () => {
            renderListsContext();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));

            fireEvent.click(screen.getByText("Add List"));
            // Count increments optimistically from the local state update in addList.
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("2"));
        });
    });

    // ── Visibility Management ─────────────────────────────────────────

    describe("Visibility Management", () => {
        test("makePublic updates the list to public", async () => {
            renderListsContext();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));

            expect(screen.getByTestId("first-public").textContent).toBe("private");

            fireEvent.click(screen.getByText("Make Public"));
            await waitFor(() => expect(screen.getByTestId("first-public").textContent).toBe("public"));
        });

        test("makePublicUntil updates the list to public", async () => {
            renderListsContext();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));

            expect(screen.getByTestId("first-public").textContent).toBe("private");

            fireEvent.click(screen.getByText("Make Public Until"));
            await waitFor(() => expect(screen.getByTestId("first-public").textContent).toBe("public"));
        });
    });

    // ── Add Publication ───────────────────────────────────────────────

    describe("Add Publication", () => {
        test("addPublicationToList successfully calls fetch", async () => {
            renderListsContext();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));

            fireEvent.click(screen.getByText("Add Pub to First"));

            await waitFor(() => {
                // Verify exactly one PATCH to the /collection endpoint was issued.
                const patchCalls = global.fetch.mock.calls.filter(
                    c => c[1]?.method === "PATCH" && c[0].includes("/collection")
                );
                expect(patchCalls.length).toBe(1);
            });
        });
    });

    // ── Error Handling ────────────────────────────────────────────────

    describe("Error Handling", () => {
        test("useLists throws when used outside ListsProvider", () => {
            // Suppress the React error boundary console output during this intentional throw.
            const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

            function ComponentWithoutProvider() {
                useLists();
                return null;
            }

            expect(() => render(<ComponentWithoutProvider />)).toThrow(
                "useLists must be used within ListsProvider"
            );

            consoleSpy.mockRestore();
        });
    });

});