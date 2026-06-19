import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { useLibrary } from "./LibraryContext";

// Helper component that exposes library context state to the screen
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

// Double root response with the link the LibraryProvider needs
const rootResponse = {
    _links: {
        "get-library-by-user": {
            href: "http://localhost:8081/libraries/by-user/user-001"
        }
    }
};

describe("LibraryContext", () => {

    // Globally mock the fetch calls made by the providers
    beforeEach(() => {
        vi.clearAllMocks();

        global.fetch = vi.fn(async (url) => {
            // Root endpoint — returns the links the LibraryProvider follows
            if (url.includes("/?userId=") || url.endsWith("/?userId=user-001")) {
                return {
                    ok: true,
                    status: 200,
                    json: async () => rootResponse
                };
            }
            // Library endpoint
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

    // ── Data Fetching ──────────────────────────────────────────────────────────────

    test("fetches and exposes library data on mount", async () => {
        renderWithProviders(<TestLibrary />);

        await waitFor(() => expect(screen.getByTestId("exists").textContent).toBe("exists"));
        expect(screen.getByTestId("name").textContent).toBe("My Personal Library");
    });

    test("handles 404 correctly when user has no library", async () => {
        // Root succeeds, library returns 404
        global.fetch = vi.fn(async (url) => {
            if (url.includes("/?userId=")) {
                return { ok: true, status: 200, json: async () => rootResponse };
            }
            return { ok: false, status: 404 };
        });

        renderWithProviders(<TestLibrary />);

        await waitFor(() => expect(screen.getByTestId("loading").textContent).toBe("false"));

        expect(screen.getByTestId("exists").textContent).toBe("empty");
        expect(screen.getByTestId("error").textContent).toBe("none");
    });

    test("handles server errors correctly", async () => {
        global.fetch = vi.fn(async (url) => {
            if (url.includes("/?userId=")) {
                return { ok: true, status: 200, json: async () => rootResponse };
            }
            return { ok: false, status: 500 };
        });

        renderWithProviders(<TestLibrary />);

        // Wait for error to appear
        await waitFor(() => {
            expect(screen.getByTestId("error").textContent).not.toBe("none");
        });
        expect(screen.getByTestId("exists").textContent).toBe("empty");
    });

    // ── Error Handling ─────────────────────────────────────────────────────────────

    describe("Error Handling", () => {
        test("useLibrary throws when used outside LibraryProvider", () => {
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