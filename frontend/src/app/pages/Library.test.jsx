import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, fireEvent } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { Library } from "./Library";

// Mock React Router
const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

// Mock Contexts directly to avoid async fetching issues during tests
// Added dummy Providers so renderWithProviders doesn't crash!
const mockUsePublications = vi.fn();
vi.mock("../components/PublicationsContext", () => ({
    usePublications: () => mockUsePublications(),
    PublicationsProvider: ({ children }) => children,
}));

const mockUseLibrary = vi.fn();
vi.mock("../components/LibraryContext", () => ({
    useLibrary: () => mockUseLibrary(),
    LibraryProvider: ({ children }) => children,
}));

// Mock BookCard to avoid deep rendering complexity
vi.mock("../components/BookCard", () => ({
    BookCard: ({ title, author, genre }) => (
        <div data-testid="book-card">
            <h4>{title}</h4>
            <p>{author}</p>
            <span>{genre}</span>
        </div>
    ),
}));

function renderLibrary() {
    return renderWithProviders(<Library />);
}

// ── Mock Data ──────────────────────────────────────────────────────────────────

const mockPublications = [
    { id: "1", title: "In Lib Book", author: "Auth 1", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "111" },
    { id: "2", title: "Not In Lib Book", author: "Auth 2", genre: "Science", libraryId: null, type: "book", isbn: "222" }
];

const mockLibraryObj = { libraryName: "My Awesome Library", id: "lib-1" };

// ── Tests ──────────────────────────────────────────────────────────────────────

describe("Library Component", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    // ── Loading State ──
    describe("Loading State", () => {
        test("shows loading indicator", () => {
            mockUseLibrary.mockReturnValue({ library: null, loading: true, error: null });
            mockUsePublications.mockReturnValue({ publications: [], addToLibrary: vi.fn() });

            renderLibrary();
            expect(screen.getByText("Loading library…")).toBeInTheDocument();
        });
    });

    // ── No Library State ──
    describe("No Library Found", () => {
        beforeEach(() => {
            mockUseLibrary.mockReturnValue({ library: null, loading: false, error: null });
            mockUsePublications.mockReturnValue({ publications: [], addToLibrary: vi.fn() });
        });

        test("shows No Library Found heading", () => {
            renderLibrary();
            expect(screen.getByText("No Library Found")).toBeInTheDocument();
        });

        test("shows descriptive message and button", () => {
            renderLibrary();
            expect(screen.getByText(/You don't have a library yet/i)).toBeInTheDocument();
            expect(screen.getByRole("button", { name: /Go to My Publications/i })).toBeInTheDocument();
        });

        test("navigates to dashboard when back button is clicked", () => {
            renderLibrary();
            fireEvent.click(screen.getByText(/My Dashboard/i));
            expect(mockNavigate).toHaveBeenCalledWith("/dashboard");
        });

        test("navigates to my-publications when Go to My Publications is clicked", () => {
            renderLibrary();
            fireEvent.click(screen.getByRole("button", { name: /Go to My Publications/i }));
            expect(mockNavigate).toHaveBeenCalledWith("/my-publications");
        });
    });

    // ── Library Exists State ──
    describe("Library Exists", () => {
        const mockAddToLibrary = vi.fn();

        beforeEach(() => {
            mockUseLibrary.mockReturnValue({ library: mockLibraryObj, loading: false, error: null });
            mockUsePublications.mockReturnValue({ publications: mockPublications, addToLibrary: mockAddToLibrary });
        });

        test("shows library name and publication count", () => {
            renderLibrary();
            expect(screen.getByText("My Awesome Library")).toBeInTheDocument();
            expect(screen.getByText("1 publication")).toBeInTheDocument(); // Only 1 has libraryId set in mock
        });

        test("shows publications that are in the library", () => {
            renderLibrary();
            expect(screen.getByText("In Lib Book")).toBeInTheDocument();
            expect(screen.queryByText("Not In Lib Book")).not.toBeInTheDocument();
        });

        test("opens Manage Publications modal when Add Publications is clicked", () => {
            renderLibrary();
            fireEvent.click(screen.getByRole("button", { name: /Add Publications/i }));
            expect(screen.getByText("Add Publications to Library")).toBeInTheDocument();
        });

        test("modal shows publications not in the library", () => {
            renderLibrary();
            fireEvent.click(screen.getByRole("button", { name: /Add Publications/i }));
            // The modal lists publications not in the library
            expect(screen.getByText("Not In Lib Book")).toBeInTheDocument();
        });

        test("calls addToLibrary when Add is clicked in modal", async () => {
            renderLibrary();
            fireEvent.click(screen.getByRole("button", { name: /Add Publications/i }));
            const addButtons = screen.getAllByRole("button", { name: "Add" });

            fireEvent.click(addButtons[0]);
            expect(mockAddToLibrary).toHaveBeenCalledWith("2");
        });

        test("closes modal when Done is clicked", () => {
            renderLibrary();
            fireEvent.click(screen.getByRole("button", { name: /Add Publications/i }));
            expect(screen.getByText("Add Publications to Library")).toBeInTheDocument();

            fireEvent.click(screen.getByRole("button", { name: /Done/i }));
            expect(screen.queryByText("Add Publications to Library")).not.toBeInTheDocument();
        });

        test("filters publications by genre", () => {
            renderLibrary();
            const genreSelect = screen.getByDisplayValue("All Genres");

            // "Science" shouldn't match "In Lib Book" which is "Fiction"
            fireEvent.change(genreSelect, { target: { value: "Science" } });
            expect(screen.queryByText("In Lib Book")).not.toBeInTheDocument();

            // Change back to Fiction
            fireEvent.change(genreSelect, { target: { value: "Fiction" } });
            expect(screen.getByText("In Lib Book")).toBeInTheDocument();
        });
    });

    // ── Sorting ──
    describe("Sorting", () => {
        beforeEach(() => {
            mockUseLibrary.mockReturnValue({ library: mockLibraryObj, loading: false, error: null });
            mockUsePublications.mockReturnValue({
                publications: [
                    { id: "1", title: "Zen and the Art", author: "Pirsig", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "111" },
                    { id: "2", title: "Anna Karenina", author: "Tolstoy", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "222" },
                    { id: "3", title: "Middlemarch", author: "Eliot", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "333" },
                ],
                addToLibrary: vi.fn(),
            });
        });

        test("renders sort select with Default option", () => {
            renderLibrary();
            expect(screen.getByDisplayValue("Default")).toBeInTheDocument();
        });

        test("sorts by title ascending", () => {
            renderLibrary();
            fireEvent.change(screen.getByDisplayValue("Default"), { target: { value: "title" } });
            const cards = screen.getAllByTestId("book-card");
            expect(cards[0]).toHaveTextContent("Anna Karenina");
            expect(cards[1]).toHaveTextContent("Middlemarch");
            expect(cards[2]).toHaveTextContent("Zen and the Art");
        });

        test("sorts by author ascending", () => {
            renderLibrary();
            fireEvent.change(screen.getByDisplayValue("Default"), { target: { value: "author" } });
            const cards = screen.getAllByTestId("book-card");
            expect(cards[0]).toHaveTextContent("Eliot");
            expect(cards[1]).toHaveTextContent("Pirsig");
            expect(cards[2]).toHaveTextContent("Tolstoy");
        });

        test("sorts by type ascending", () => {
            mockUsePublications.mockReturnValue({
                publications: [
                    { id: "1", title: "Zen and the Art", author: "Pirsig", genre: "Fiction", libraryId: "lib-1", type: "magazine", isbn: "111" },
                    { id: "2", title: "Anna Karenina", author: "Tolstoy", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "222" },
                    { id: "3", title: "Middlemarch", author: "Eliot", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "333" },
                ],
                addToLibrary: vi.fn(),
            });

            renderLibrary();
            fireEvent.change(screen.getByDisplayValue("Default"), { target: { value: "type" } });
            const cards = screen.getAllByTestId("book-card");
            expect(cards[0]).toHaveTextContent("Anna Karenina");
            expect(cards[1]).toHaveTextContent("Middlemarch");
            expect(cards[2]).toHaveTextContent("Zen and the Art");
        });

        test("sorts by isbn ascending", () => {
            mockUsePublications.mockReturnValue({
                publications: [
                    { id: "1", title: "Zen and the Art", author: "Pirsig", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "333" },
                    { id: "2", title: "Anna Karenina", author: "Tolstoy", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "111" },
                    { id: "3", title: "Middlemarch", author: "Eliot", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "222" },
                ],
                addToLibrary: vi.fn(),
            });

            renderLibrary();
            fireEvent.change(screen.getByDisplayValue("Default"), { target: { value: "isbn" } });
            const cards = screen.getAllByTestId("book-card");
            expect(cards[0]).toHaveTextContent("Anna Karenina");
            expect(cards[1]).toHaveTextContent("Middlemarch");
            expect(cards[2]).toHaveTextContent("Zen and the Art");
        });
    });
});