import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, waitFor, render, fireEvent } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { SharedListDetail } from "./SharedListDetail";

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

vi.mock("../components/BookCard", () => ({
    BookCard: ({ title }) => <div data-testid="book-card">{title}</div>,
}));

import { api } from "../../api/client";

// ── Data ──────────────────────────────────────────────────────────────────────

const mockPublications = [
    {
        id: "pub-1",
        title: "Dune",
        authors: ["Frank Herbert"],
        genres: ["Science Fiction"],
        condition: "Good",
        language: "en",
        code: "9780441013593",
    },
    {
        id: "pub-2",
        title: "1984",
        authors: ["George Orwell"],
        genres: ["Dystopian"],
        condition: "Like New",
        language: "en",
        code: "9780451524935",
    },
];

// ── Setup ─────────────────────────────────────────────────────────────────────

function renderWithListId(listId = "col-1") {
    render(
        <MemoryRouter initialEntries={[`/shared-lists/${listId}`]}>
            <Routes>
                <Route path="/shared-lists/:listId" element={<SharedListDetail />} />
            </Routes>
        </MemoryRouter>
    );
}

// ── Tests ─────────────────────────────────────────────────────────────────────

describe("SharedListDetail", () => {

    beforeEach(() => {
        vi.clearAllMocks();
        api.get.mockResolvedValue(mockPublications);
    });

    // ── Rendering ──
    describe("Rendering", () => {
        test("renders US066 label", async () => {
            renderWithListId();
            await waitFor(() =>
                expect(screen.getByText("US066: List items in a shared list")).toBeInTheDocument()
            );
        });

        test("renders publication count", async () => {
            renderWithListId();
            await waitFor(() =>
                expect(screen.getByText("2 publications")).toBeInTheDocument()
            );
        });

        test("renders a BookCard for each publication", async () => {
            renderWithListId();
            await waitFor(() =>
                expect(screen.getAllByTestId("book-card")).toHaveLength(2)
            );
        });

        test("renders the back button", async () => {
            renderWithListId();
            await waitFor(() =>
                expect(screen.getByText("Shared Lists")).toBeInTheDocument()
            );
        });
    });

    // ── Empty state ──
    describe("Empty State", () => {
        test("shows empty message when list has no publications", async () => {
            api.get.mockResolvedValue([]);
            renderWithListId();
            await waitFor(() =>
                expect(screen.getByText("No publications in this list.")).toBeInTheDocument()
            );
        });
    });

    // ── Error state ──
    describe("Error State", () => {
        test("shows error banner when API returns 403", async () => {
            api.get.mockRejectedValue(new Error("HTTP 403: Access denied"));
            renderWithListId();
            await waitFor(() =>
                expect(screen.getByText(/Could not load publications/i)).toBeInTheDocument()
            );
        });
    });

    // ── Detail Modal ──
    describe("Detail Modal", () => {
        test("opens detail modal when a book card is clicked", async () => {
            renderWithListId();
            await waitFor(() => expect(screen.getAllByTestId("book-card")).toHaveLength(2));
            fireEvent.click(screen.getAllByTestId("book-card")[0]);
            expect(screen.getByRole("heading", { name: "Dune" })).toBeInTheDocument();
        });

        test("modal shows correct publication details", async () => {
            renderWithListId();
            await waitFor(() => expect(screen.getAllByTestId("book-card")).toHaveLength(2));
            fireEvent.click(screen.getAllByTestId("book-card")[0]);
            expect(screen.getByText("Frank Herbert")).toBeInTheDocument();
            expect(screen.getByText("Science Fiction")).toBeInTheDocument();
            expect(screen.getByText("Good")).toBeInTheDocument();
            expect(screen.getByText("English")).toBeInTheDocument();
            expect(screen.getByText("9780441013593")).toBeInTheDocument();
        });

        test("closes modal when Close button is clicked", async () => {
            renderWithListId();
            await waitFor(() => expect(screen.getAllByTestId("book-card")).toHaveLength(2));
            fireEvent.click(screen.getAllByTestId("book-card")[0]);
            expect(screen.getByRole("heading", { name: "Dune" })).toBeInTheDocument();
            fireEvent.click(screen.getByRole("button", { name: "Close" }));
            expect(screen.queryByRole("heading", { name: "Dune" })).not.toBeInTheDocument();
        });
    });

    // ── Navigation ──
    describe("Navigation", () => {
        test("navigates back to shared lists when back button is clicked", async () => {
            renderWithListId();
            await waitFor(() => expect(screen.getByText("Shared Lists")).toBeInTheDocument());
            fireEvent.click(screen.getByText("Shared Lists"));
            expect(mockNavigate).toHaveBeenCalledWith("/shared-lists");
        });
    });
});