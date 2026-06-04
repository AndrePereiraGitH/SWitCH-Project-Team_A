import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, fireEvent, render, waitFor } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { ListDetail } from "./ListDetail";

// ── Mocks ─────────────────────────────────────────────────────────────────────

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const mockGetListById = vi.fn();
const mockAddPublicationToList = vi.fn();
vi.mock("../components/ListsContext", () => ({
    useLists: () => ({
        getListById: mockGetListById,
        addPublicationToList: mockAddPublicationToList
    })
}));

const mockUsePublications = vi.fn();
vi.mock("../components/PublicationsContext", () => ({
    usePublications: () => mockUsePublications()
}));

vi.mock("../components/BookCard", () => ({
    BookCard: ({ title }) => <div data-testid="book-card">{title}</div>
}));

// ── Setup ─────────────────────────────────────────────────────────────────────

const mockList = {
    collectionId: "list-1",
    collectionName: "My Fiction List",
    private: true
};

function renderWithRealList() {
    mockGetListById.mockReturnValue(mockList);
    render(
        <MemoryRouter initialEntries={["/lists/list-1"]}>
            <Routes>
                <Route path="/lists/:listId" element={<ListDetail />} />
            </Routes>
        </MemoryRouter>
    );
}

function renderNotFound() {
    mockGetListById.mockReturnValue(undefined);
    render(
        <MemoryRouter initialEntries={["/lists/nonexistent"]}>
            <Routes>
                <Route path="/lists/:listId" element={<ListDetail />} />
            </Routes>
        </MemoryRouter>
    );
}

// ── Tests ─────────────────────────────────────────────────────────────────────

describe("ListDetail", () => {

    beforeEach(() => {
        vi.clearAllMocks();
        // Default to empty publications
        mockUsePublications.mockReturnValue({
            publications: [],
            refresh: vi.fn()
        });
    });

    // ── List not found ──
    describe("List Not Found", () => {
        test("shows not found message when list does not exist", () => {
            renderNotFound();
            expect(screen.getByText("List not found")).toBeInTheDocument();
        });
    });

    // ── Rendering ──
    describe("Rendering", () => {
        test("renders the list name", () => {
            renderWithRealList();
            expect(screen.getByText("My Fiction List")).toBeInTheDocument();
        });

        test("renders publication count", () => {
            renderWithRealList();
            expect(screen.getByText(/0 publications/i)).toBeInTheDocument();
        });

        test("renders the My Lists back button", () => {
            renderWithRealList();
            expect(screen.getByText("My Lists")).toBeInTheDocument();
        });

        test("renders the Add Publication button", () => {
            renderWithRealList();
            const buttons = screen.getAllByRole("button", { name: /Add Publication/i });
            expect(buttons.length).toBeGreaterThan(0);
        });

        test("does not show Public badge for private list", () => {
            renderWithRealList();
            expect(screen.queryByText("Public")).not.toBeInTheDocument();
        });

        test("shows Public badge for public list", () => {
            mockGetListById.mockReturnValue({ ...mockList, private: false });
            render(
                <MemoryRouter initialEntries={["/lists/list-1"]}>
                    <Routes>
                        <Route path="/lists/:listId" element={<ListDetail />} />
                    </Routes>
                </MemoryRouter>
            );
            expect(screen.getByText("Public")).toBeInTheDocument();
        });
    });

    // ── Empty list ──
    describe("Empty List", () => {
        test("shows no publications message when list is empty", () => {
            renderWithRealList();
            expect(
                screen.getByText("No publications in this list")
            ).toBeInTheDocument();
        });
    });

    // ── Manage Publications Modal ──
    describe("Manage Publications Modal", () => {
        test("clicking Add Publication opens the modal", () => {
            renderWithRealList();
            fireEvent.click(screen.getByRole("button", { name: "Add Publication" }));
            expect(
                screen.getByRole("heading", { name: "Add Publications" })
            ).toBeInTheDocument();
        });

        test("modal shows empty message when there are no available publications in library", () => {
            renderWithRealList();
            fireEvent.click(screen.getByRole("button", { name: "Add Publication" }));
            expect(screen.getByText("All your publications are already in this list.")).toBeInTheDocument();
        });

        test("modal shows available publications to add", () => {
            mockUsePublications.mockReturnValue({
                publications: [
                    { id: "pub-1", title: "Available Book", author: "Author", collectionId: null, libraryId: "lib-1" }
                ],
                refresh: vi.fn()
            });
            renderWithRealList();

            fireEvent.click(screen.getByRole("button", { name: "Add Publication" }));

            expect(screen.getByText("Available Book")).toBeInTheDocument();
            expect(screen.getByRole("button", { name: "Add" })).toBeInTheDocument();
        });

        test("calls addPublicationToList and refresh when Add is clicked", async () => {
            const mockRefresh = vi.fn();
            mockUsePublications.mockReturnValue({
                publications: [
                    { id: "pub-1", title: "Available Book", author: "Author", collectionId: null, libraryId: "lib-1" }
                ],
                refresh: mockRefresh
            });
            renderWithRealList();

            fireEvent.click(screen.getByRole("button", { name: "Add Publication" }));
            fireEvent.click(screen.getByRole("button", { name: "Add" }));

            expect(mockAddPublicationToList).toHaveBeenCalledWith("list-1", "pub-1");

            // Wait for the async refresh to trigger
            await waitFor(() => {
                expect(mockRefresh).toHaveBeenCalled();
            });
        });

        test("closes modal when Done is clicked", () => {
            renderWithRealList();
            fireEvent.click(screen.getByRole("button", { name: "Add Publication" }));
            expect(screen.getByRole("heading", { name: "Add Publications" })).toBeInTheDocument();

            fireEvent.click(screen.getByRole("button", { name: "Done" }));
            expect(screen.queryByRole("heading", { name: "Add Publications" })).not.toBeInTheDocument();
        });
    });

    // ── Navigation ──
    describe("Navigation", () => {
        test("navigates to lists when back button is clicked", () => {
            renderWithRealList();
            fireEvent.click(screen.getByText("My Lists"));
            expect(mockNavigate).toHaveBeenCalledWith("/lists");
        });
    });
});