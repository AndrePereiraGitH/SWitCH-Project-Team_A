import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { useLibrary } from "./LibraryContext";

// ── Helper components ─────────────────────────────────────────────────

/**
 * Test component that exposes {@link LibraryContext} state to the DOM so
 * assertions can be made through rendered text.
 *
 * Rendered elements:
 * - `data-testid="loading"` — `"true"` while the fetch is in-flight, `"false"` otherwise.
 * - `data-testid="error"`   — the error message, or `"none"` if no error.
 * - `data-testid="exists"`  — `"exists"` when a library is loaded, `"empty"` when null.
 * - `data-testid="name"`    — `library.name`, or empty string when null.
 */
function TestLibrary() {
    const { library, loading, error } = useLibrary();
    return (
        <div>
            <p data-testid="loading">{loading ? "true" : "false"}</p>
            <p data-testid="error">{error ? error : "none"}</p>
            <p data-testid="exists">{library ? "exists" : "empty"}</p>
            <p data-testid="name">{library ? library.name : ""}</p>
        </div>
    );
}

// ── Root HATEOAS fixture ──────────────────────────────────────────────

/**
 * Minimal root response containing only the link needed by {@link LibraryProvider}.
 * The href is non-templated here — the provider appends `?requestingUserId` as a
 * plain query parameter rather than via URI template expansion.
 */
const rootResponse = {
    _links: {
        "get-library-by-user": {
            href: "http://localhost:8081/libraries/by-user/user-001"
        }
    }
};

// ── Test suite ────────────────────────────────────────────────────────

describe("LibraryContext", () => {

    beforeEach(() => {
        vi.clearAllMocks();

        /**
         * Default fetch mock: root succeeds and returns {@link rootResponse};
         * the library endpoint returns a valid library object.
         * Individual tests override this mock to simulate error scenarios.
         */
        global.fetch = vi.fn(async (url) => {
            // Root HATEOAS entry-point
            if (url.includes("/?userId=") || url.endsWith("/?userId=user-001")) {
                return {
                    ok: true,
                    status: 200,
                    json: async () => rootResponse
                };
            }
            // GET /libraries/by-user/{userId} — happy-path library fetch
            if (url.includes("/libraries/by-user/")) {
                return {
                    ok: true,
                    status: 200,
                    json: async () => ({ name: "My Personal Library", publicationIds: ["pub-1"] })
                };
            }
            return { ok: false, status: 404 };
        });
    });

    // ── Data Fetching ─────────────────────────────────────────────────

    test("fetches and exposes library data on mount", async () => {
        renderWithProviders(<TestLibrary />);

        await waitFor(() => expect(screen.getByTestId("exists").textContent).toBe("exists"));
        expect(screen.getByTestId("name").textContent).toBe("My Personal Library");
    });

    test("handles 404 correctly when user has no library", async () => {
        // Root succeeds; library endpoint returns 404 — valid "no library yet" state.
        global.fetch = vi.fn(async (url) => {
            if (url.includes("/?userId=")) {
                return { ok: true, status: 200, json: async () => rootResponse };
            }
            return { ok: false, status: 404 };
        });

        renderWithProviders(<TestLibrary />);

        // Wait for the fetch cycle to complete before asserting.
        await waitFor(() => expect(screen.getByTestId("loading").textContent).toBe("false"));

        // 404 must not surface as an error — library is simply absent.
        expect(screen.getByTestId("exists").textContent).toBe("empty");
        expect(screen.getByTestId("error").textContent).toBe("none");
    });

    test("handles server errors correctly", async () => {
        // Root succeeds; library endpoint returns a 500 — genuine error condition.
        global.fetch = vi.fn(async (url) => {
            if (url.includes("/?userId=")) {
                return { ok: true, status: 200, json: async () => rootResponse };
            }
            return { ok: false, status: 500 };
        });

        renderWithProviders(<TestLibrary />);

        await waitFor(() => {
            expect(screen.getByTestId("error").textContent).not.toBe("none");
        });
        // Library must remain null when the fetch fails.
        expect(screen.getByTestId("exists").textContent).toBe("empty");
    });

    // ── Error Handling ────────────────────────────────────────────────

    describe("Error Handling", () => {
        test("useLibrary throws when used outside LibraryProvider", () => {
            // Suppress the React error boundary console output during this intentional throw.
            const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

            function ComponentWithoutProvider() {
                useLibrary();
                return null;
            }

            expect(() => render(<ComponentWithoutProvider />)).toThrow(
                "useLibrary must be used within LibraryProvider"
            );

            consoleSpy.mockRestore();
        });
    });

});