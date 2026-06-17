import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, waitFor, render, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { SharedLists } from "./SharedLists";

// ── Mocks ─────────────────────────────────────────────────────────────────────

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

vi.mock("../../api/client", () => ({
    api: {
        get: vi.fn(),
    },
}));

import { api } from "../../api/client";

// ── Data ──────────────────────────────────────────────────────────────────────

const mockGenres = [
    { id: "genre-1", genreName: "Fiction" },
    { id: "genre-2", genreName: "Science" },
];

const mockLists = [
    { collectionId: "col-1", collectionName: "Summer Reads", ownerName: "Alice", publicUntil: null },
    { collectionId: "col-2", collectionName: "Sci-Fi Classics", ownerName: "Bob", publicUntil: "2026-12-31" },
];

// ── Setup ─────────────────────────────────────────────────────────────────────

function renderSharedLists() {
    render(
        <MemoryRouter>
            <SharedLists />
        </MemoryRouter>
    );
}

// ── Tests ─────────────────────────────────────────────────────────────────────

describe("SharedLists", () => {

    beforeEach(() => {
        vi.clearAllMocks();

        global.fetch = vi.fn(async (url) => {
            if (url.includes("/genres")) {
                return { ok: true, json: async () => mockGenres };
            }
            return { ok: false, status: 404 };
        });

        api.get.mockResolvedValue(mockLists);
    });

    // ── Rendering ──
    describe("Rendering", () => {
        test("renders the page heading", async () => {
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("Shared Lists")).toBeInTheDocument());
        });

        test("renders list cards with collection names", async () => {
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("Summer Reads")).toBeInTheDocument());
            expect(screen.getByText("Sci-Fi Classics")).toBeInTheDocument();
        });

        test("renders owner names", async () => {
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("by Alice")).toBeInTheDocument());
            expect(screen.getByText("by Bob")).toBeInTheDocument();
        });

        test("renders publicUntil date when present", async () => {
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("Shared until 2026-12-31")).toBeInTheDocument());
        });

        test("does not render publicUntil when null", async () => {
            api.get.mockResolvedValue([
                { collectionId: "col-1", collectionName: "Summer Reads", ownerName: "Alice", publicUntil: null },
            ]);
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("Summer Reads")).toBeInTheDocument());
            expect(screen.queryByText(/Shared until/i)).not.toBeInTheDocument();
        });

        test("renders genre dropdown with options", async () => {
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("Fiction")).toBeInTheDocument());
            expect(screen.getByText("Science")).toBeInTheDocument();
        });
    });

    // ── Empty state ──
    describe("Empty State", () => {
        test("shows empty message when no lists are returned", async () => {
            api.get.mockResolvedValue([]);
            renderSharedLists();
            await waitFor(() =>
                expect(screen.getByText("No public lists found.")).toBeInTheDocument()
            );
        });
    });

    // ── Error state ──
    describe("Error State", () => {
        test("shows error banner when API call fails", async () => {
            api.get.mockRejectedValue(new Error("Network error"));
            renderSharedLists();
            await waitFor(() =>
                expect(screen.getByText(/Could not load lists/i)).toBeInTheDocument()
            );
        });
    });

    // ── Navigation ──
    describe("Navigation", () => {
        test("navigates to list detail when a card is clicked", async () => {
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("Summer Reads")).toBeInTheDocument());
            fireEvent.click(screen.getByText("Summer Reads"));
            expect(mockNavigate).toHaveBeenCalledWith("/shared-lists/col-1");
        });

        test("navigates back when back button is clicked", async () => {
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("Shared Lists")).toBeInTheDocument());
            fireEvent.click(screen.getByText("Back"));
            expect(mockNavigate).toHaveBeenCalledWith(-1);
        });
    });

    // ── Genre filter ──
    describe("Genre Filter", () => {
        test("calls API with genre endpoint when a genre is selected", async () => {
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("Fiction")).toBeInTheDocument());
            fireEvent.change(screen.getByRole("combobox"), { target: { value: "genre-1" } });
            await waitFor(() =>
                expect(api.get).toHaveBeenCalledWith(
                    "/collection-of-publications/public/genre/genre-1"
                )
            );
        });

        test("calls public/all endpoint when genre is reset to all", async () => {
            renderSharedLists();
            await waitFor(() => expect(screen.getByText("Fiction")).toBeInTheDocument());
            fireEvent.change(screen.getByRole("combobox"), { target: { value: "" } });
            await waitFor(() =>
                expect(api.get).toHaveBeenCalledWith(
                    "/collection-of-publications/public/all"
                )
            );
        });
    });
});