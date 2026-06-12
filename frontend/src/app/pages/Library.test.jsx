import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, fireEvent } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { Library } from "./Library";

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

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

const mockUseLists = vi.fn();
vi.mock("../components/ListsContext", () => ({
    useLists: () => mockUseLists(),
    ListsProvider: ({ children }) => children,
}));

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

const mockPublications = [
    { id: "1", title: "In Lib Book", author: "Auth 1", genre: "Fiction", libraryId: "lib-1", type: "book", isbn: "111" },
    { id: "2", title: "Not In Lib Book", author: "Auth 2", genre: "Science", libraryId: null, type: "book", isbn: "222" }
];

const mockLibraryObj = { libraryName: "My Awesome Library", id: "lib-1" };

describe("Library Component", () => {
    beforeEach(() => {
        vi.clearAllMocks();
        mockUseLists.mockReturnValue({ lists: [], loading: false, error: null });
    });

    describe("Loading State", () => {
        test("shows loading indicator", () => {
            mockUseLibrary.mockReturnValue({ library: null, loading: true, error: null });
            mockUsePublications.mockReturnValue({ publications: [], addToLibrary: vi.fn() });

            renderLibrary();
            expect(screen.getByText("Loading library…")).toBeInTheDocument();
        });
    });

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

    describe("Library Exists", () => {
        const mockAddToLibrary = vi.fn();

        beforeEach(() => {
            mockUseLibrary.mockReturnValue({ library: mockLibraryObj, loading: false, error: null });
            mockUsePublications.mockReturnValue({ publications: mockPublications, addToLibrary: mockAddToLibrary });
        });

        test("shows library name and publication count", () => {
            renderLibrary();
            expect(screen.getByText("My Awesome Library")).toBeInTheDocument();
            expect(screen.getByText("1 publication")).toBeInTheDocument();
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

            fireEvent.change(genreSelect, { target: { value: "Science" } });
            expect(screen.queryByText("In Lib Book")).not.toBeInTheDocument();

            fireEvent.change(genreSelect, { target: { value: "Fiction" } });
            expect(screen.getByText("In Lib Book")).toBeInTheDocument();
        });

        test("Sell button navigates to /sell/:id in grid view", () => {
            renderLibrary();
            const sellButtons = screen.getAllByRole("button", { name: /Sell/i });
            fireEvent.click(sellButtons[0]);
            expect(mockNavigate).toHaveBeenCalledWith("/sell/1");
        });

        test("filters publications by type", () => {
            renderLibrary();
            const typeSelect = screen.getByDisplayValue("All Types");

            fireEvent.change(typeSelect, { target: { value: "magazine" } });
            expect(screen.queryByText("In Lib Book")).not.toBeInTheDocument();

            fireEvent.change(typeSelect, { target: { value: "book" } });
            expect(screen.getByText("In Lib Book")).toBeInTheDocument();
        });
    });

    describe("Library Exists - List View", () => {
        const mockAddToLibrary = vi.fn();

        beforeEach(() => {
            mockUseLibrary.mockReturnValue({ library: mockLibraryObj, loading: false, error: null });
            mockUsePublications.mockReturnValue({ publications: mockPublications, addToLibrary: mockAddToLibrary });
        });

        test("Sell button navigates to /sell/:id in list view", () => {
            renderLibrary();
            const allButtons = screen.getAllByRole("button");
            const viewToggles = allButtons.filter(
                btn => btn.querySelector("svg") && btn.className.includes("rounded-lg")
            );
            fireEvent.click(viewToggles[1]);

            const sellButtons = screen.getAllByRole("button", { name: /Sell/i });
            fireEvent.click(sellButtons[0]);
            expect(mockNavigate).toHaveBeenCalledWith("/sell/1");
        });
    });

    describe("Library Exists - Detail Modal", () => {
        const mockAddToLibrary = vi.fn();

        beforeEach(() => {
            mockUseLibrary.mockReturnValue({ library: mockLibraryObj, loading: false, error: null });
            mockUsePublications.mockReturnValue({ publications: mockPublications, addToLibrary: mockAddToLibrary });
        });

        test("clicking a book card opens the detail modal", () => {
            renderLibrary();
            const bookCards = screen.getAllByTestId("book-card");
            fireEvent.click(bookCards[0]);
            // Modal should show a "Close" and "Sell" button (there will now be multiple "Sell" buttons)
            expect(screen.getAllByRole("button", { name: /Sell/i }).length).toBeGreaterThanOrEqual(2);
            expect(screen.getByRole("button", { name: /Close/i })).toBeInTheDocument();
        });

        test("Sell button in detail modal navigates to /sell/:id", () => {
            renderLibrary();
            const bookCards = screen.getAllByTestId("book-card");
            fireEvent.click(bookCards[0]);

            const sellButtons = screen.getAllByRole("button", { name: /Sell/i });
            fireEvent.click(sellButtons[sellButtons.length - 1]);
            expect(mockNavigate).toHaveBeenCalledWith("/sell/1");
        });

        test("Close button closes the detail modal without navigating", () => {
            renderLibrary();
            const bookCards = screen.getAllByTestId("book-card");
            fireEvent.click(bookCards[0]);

            expect(screen.getByRole("button", { name: /Close/i })).toBeInTheDocument();

            fireEvent.click(screen.getByRole("button", { name: /Close/i }));
            expect(screen.queryByRole("button", { name: /Close/i })).not.toBeInTheDocument();
            expect(mockNavigate).not.toHaveBeenCalled();
        });

        test("detail modal displays book information", () => {
            renderLibrary();
            const bookCards = screen.getAllByTestId("book-card");
            fireEvent.click(bookCards[0]);

            expect(screen.getAllByText("In Lib Book").length).toBeGreaterThanOrEqual(2);
            expect(screen.getAllByText("Auth 1").length).toBeGreaterThanOrEqual(2);
            expect(screen.getAllByText("Fiction").length).toBeGreaterThanOrEqual(2);
        });
    });
});