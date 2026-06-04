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

describe("LibraryContext", () => {

    // Globally mock the fetch calls made by the LibraryProvider
    beforeEach(() => {
        vi.clearAllMocks();

        global.fetch = vi.fn(async (url) => {
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

        // Wait for loading to finish and library to be populated
        await waitFor(() => expect(screen.getByTestId("exists").textContent).toBe("exists"));
        expect(screen.getByTestId("name").textContent).toBe("My Personal Library");
    });

    test("handles 404 correctly when user has no library", async () => {
        // Override mock for this specific test to return a 404
        global.fetch = vi.fn(async () => ({ ok: false, status: 404 }));

        renderWithProviders(<TestLibrary />);

        // Wait for loading to resolve
        await waitFor(() => expect(screen.getByTestId("loading").textContent).toBe("false"));

        // State should be clean/empty
        expect(screen.getByTestId("exists").textContent).toBe("empty");
        expect(screen.getByTestId("error").textContent).toBe("none");
    });

    test("handles server errors correctly", async () => {
        // Override mock to return a 500 error
        global.fetch = vi.fn(async () => ({ ok: false, status: 500 }));

        renderWithProviders(<TestLibrary />);

        await waitFor(() => expect(screen.getByTestId("loading").textContent).toBe("false"));

        // The error text should no longer be 'none'
        expect(screen.getByTestId("error").textContent).not.toBe("none");
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

            // Render directly from RTL (no providers) to test the missing context error
            expect(() => render(<ComponentWithoutProvider />)).toThrow(
                "useLibrary must be used within LibraryProvider"
            );

            consoleSpy.mockRestore();
        });
    });

});