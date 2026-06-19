import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { useLists } from "./ListsContext";
import { useLinks } from "./LinksContext";

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

// Exposes the loading state of LinksContext to wait until links are ready
function LinksReady({ children }) {
    const { loading } = useLinks();
    return loading ? <p data-testid="links-loading">true</p> : children;
}

// Helper component that exposes lists context state to the screen
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

function renderListsContext() {
    return renderWithProviders(
        <LinksReady>
            <TestLists />
        </LinksReady>
    );
}

async function waitForLinks() {
    await waitFor(() =>
        expect(screen.queryByTestId("links-loading")).not.toBeInTheDocument()
    );
}

describe("ListsContext", () => {

    beforeEach(() => {
        vi.clearAllMocks();

        global.fetch = vi.fn(async (url, options) => {
            // Root endpoint
            if (url.includes("/?userId=") || url.match(/^http:\/\/localhost:8081\/\?/)) {
                return { ok: true, status: 200, json: async () => rootResponse };
            }
            if (url.includes("/collection-of-publications/owner") && (!options || options.method === "GET")) {
                return {
                    ok: true,
                    json: async () => [{ collectionId: "list-1", collectionName: "Initial List", private: true }]
                };
            }
            if (url.includes("/make-public-until") && options?.method === "PATCH") {
                return {
                    ok: true,
                    json: async () => ({ collectionId: "list-1", collectionName: "Initial List", private: false })
                };
            }
            if (url.includes("/make-public") && options?.method === "PATCH") {
                return {
                    ok: true,
                    json: async () => ({ collectionId: "list-1", collectionName: "Initial List", private: false })
                };
            }
            if (url.includes("/make-private") && options?.method === "PATCH") {
                return {
                    ok: true,
                    json: async () => ({ collectionId: "list-1", collectionName: "Initial List", private: true })
                };
            }
            if (url.includes("/publicationSamples/") && url.includes("/collection") && options?.method === "PATCH") {
                return {
                    ok: true,
                    json: async () => ({ id: "pub-1" })
                };
            }
            if (url.includes("/collection-of-publications/") && options?.method === "POST") {
                return {
                    ok: true,
                    json: async () => ({ collectionId: "list-2", collectionName: "New List", private: true })
                };
            }
            return { ok: false, status: 404, text: async () => "Not found" };
        });
    });

    // ── Initial State & Fetching ──────────────────────────────────────────────────

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

    // ── Add List ──────────────────────────────────────────────────────────────────

    describe("Add List", () => {
        test("adds a new list and updates state", async () => {
            renderListsContext();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));

            fireEvent.click(screen.getByText("Add List"));
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("2"));
        });
    });

    // ── Update Visibility ─────────────────────────────────────────────────────────

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

    // ── Add Publication ───────────────────────────────────────────────────────────

    describe("Add Publication", () => {
        test("addPublicationToList successfully calls fetch", async () => {
            renderListsContext();
            await waitForLinks();
            await waitFor(() => expect(screen.getByTestId("count").textContent).toBe("1"));

            fireEvent.click(screen.getByText("Add Pub to First"));

            await waitFor(() => {
                const patchCalls = global.fetch.mock.calls.filter(
                    c => c[1]?.method === "PATCH" && c[0].includes("/collection")
                );
                expect(patchCalls.length).toBe(1);
            });
        });
    });

    // ── Error Handling ────────────────────────────────────────────────────────────

    describe("Error Handling", () => {
        test("useLists throws when used outside ListsProvider", () => {
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