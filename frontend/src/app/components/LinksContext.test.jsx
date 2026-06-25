import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { LinksProvider, useLinks } from "./LinksContext";

// Helper component that exposes the links context state to the screen
function TestLinks() {
    const { links, loading, error } = useLinks();
    return (
        <div>
            <p data-testid="loading">{loading ? "true" : "false"}</p>
            <p data-testid="error">{error ? error : "none"}</p>
            <p data-testid="link-count">{Object.keys(links).length}</p>
            <p data-testid="has-self">{links?.self ? "yes" : "no"}</p>
        </div>
    );
}

function renderLinks() {
    return render(
        <LinksProvider>
            <TestLinks />
        </LinksProvider>
    );
}

describe("LinksContext", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    // ── Initial Fetch ─────────────────────────────────────────────────────────────

    describe("Initial Fetch", () => {
        test("fetches and exposes links on mount", async () => {
            global.fetch = vi.fn(async () => ({
                ok: true,
                status: 200,
                json: async () => ({
                    _links: {
                        self: { href: "http://localhost:8081/?userId=user-001" },
                        "find-library": { href: "http://localhost:8081/libraries/{id}" }
                    }
                })
            }));

            renderLinks();

            await waitFor(() => expect(screen.getByTestId("loading").textContent).toBe("false"));
            expect(screen.getByTestId("link-count").textContent).toBe("2");
            expect(screen.getByTestId("has-self").textContent).toBe("yes");
            expect(screen.getByTestId("error").textContent).toBe("none");
        });

        test("starts with loading true", () => {
            global.fetch = vi.fn(() => new Promise(() => {})); // never resolves

            renderLinks();

            expect(screen.getByTestId("loading").textContent).toBe("true");
        });

        test("handles empty links response", async () => {
            global.fetch = vi.fn(async () => ({
                ok: true,
                status: 200,
                json: async () => ({})
            }));

            renderLinks();

            await waitFor(() => expect(screen.getByTestId("loading").textContent).toBe("false"));
            expect(screen.getByTestId("link-count").textContent).toBe("0");
        });
    });

    // ── Error Handling ────────────────────────────────────────────────────────────

    describe("Error Handling", () => {
        test("sets error when fetch fails with non-ok status", async () => {
            global.fetch = vi.fn(async () => ({ ok: false, status: 500 }));

            renderLinks();

            await waitFor(() => expect(screen.getByTestId("loading").textContent).toBe("false"));
            expect(screen.getByTestId("error").textContent).not.toBe("none");
            expect(screen.getByTestId("link-count").textContent).toBe("0");
        });

        test("sets error when fetch throws", async () => {
            global.fetch = vi.fn(async () => { throw new Error("network down"); });

            renderLinks();

            await waitFor(() => expect(screen.getByTestId("loading").textContent).toBe("false"));
            expect(screen.getByTestId("error").textContent).toBe("network down");
        });

        test("useLinks throws when used outside LinksProvider", () => {
            const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

            function ComponentWithoutProvider() {
                useLinks();
                return null;
            }

            expect(() => render(<ComponentWithoutProvider />)).toThrow(
                "useLinks must be used within LinksProvider"
            );

            consoleSpy.mockRestore();
        });
    });

});