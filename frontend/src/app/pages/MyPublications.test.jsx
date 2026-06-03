import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { vi } from "vitest";
import { MyPublications } from "../pages/MyPublications";

// Mock useNavigate
const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

// Mock PublicationsContext
const mockUsePublications = vi.fn();
vi.mock("../components/PublicationsContext", () => ({
    usePublications: () => mockUsePublications(),
}));

// Mock ListsContext
vi.mock("../components/ListsContext", () => ({
    useLists: () => ({
        lists: [],
        addPublicationToList: vi.fn(),
        removePublicationFromList: vi.fn(),
        isPublicationInList: vi.fn(() => false),
    }),
}));

// Mock AddToListModal
vi.mock("../components/AddToListModal", () => ({
    AddToListModal: ({ isOpen, onClose, bookTitle }) =>
        isOpen ? (
            <div data-testid="add-to-list-modal">
                <p>{bookTitle}</p>
                <button onClick={onClose}>Close Modal</button>
            </div>
        ) : null,
}));

const mockPublications = [
    {
        id: "1",
        title: "The Lord of the Rings",
        author: "J.R.R. Tolkien",
        genre: "Fantasy",
        type: "book",
        isbn: "9780261103573",
        condition: "good",
        price: 15,
    },
    {
        id: "2",
        title: "Dune",
        author: "Frank Herbert",
        genre: "Science Fiction",
        type: "book",
        isbn: "9780441013593",
        condition: "veryGood",
        price: 12,
    },
];

function renderMyPublications() {
    return render(
        <MemoryRouter>
            <MyPublications />
        </MemoryRouter>
    );
}

describe("MyPublications", () => {
    beforeEach(() => {
        vi.clearAllMocks();
        // Setup default mock return structure
        mockUsePublications.mockReturnValue({
            publications: mockPublications,
            loading: false,
            error: null,
        });
    });

    test("renders page title and publications", () => {
        renderMyPublications();

        expect(screen.getByText("My Publications")).toBeInTheDocument();
        expect(screen.getByText("The Lord of the Rings")).toBeInTheDocument();
        expect(screen.getByText("Dune")).toBeInTheDocument();
    });

    test("shows empty state when no publications exist", () => {
        mockUsePublications.mockReturnValue({ publications: [], loading: false, error: null });
        renderMyPublications();

        expect(screen.getByText("No publications found")).toBeInTheDocument();
        expect(screen.getByText("Add First Publication")).toBeInTheDocument();
    });

    test("navigates to add publication when clicking Add Publication", () => {
        renderMyPublications();

        fireEvent.click(screen.getByText("Add Publication"));

        expect(mockNavigate).toHaveBeenCalledWith("/add-publication");
    });

    test("navigates to add publication when clicking Add First Publication in empty state", () => {
        mockUsePublications.mockReturnValue({ publications: [], loading: false, error: null });
        renderMyPublications();

        fireEvent.click(screen.getByText("Add First Publication"));

        expect(mockNavigate).toHaveBeenCalledWith("/add-publication");
    });

    test("navigates to dashboard when clicking back button", () => {
        renderMyPublications();

        fireEvent.click(screen.getByText("My Dashboard"));

        expect(mockNavigate).toHaveBeenCalledWith("/dashboard");
    });

    test("filters publications by genre", () => {
        renderMyPublications();

        const genreSelect = screen.getByDisplayValue("All Genres");
        fireEvent.change(genreSelect, { target: { value: "Fantasy" } });

        expect(screen.getByText("The Lord of the Rings")).toBeInTheDocument();
        expect(screen.queryByText("Dune")).not.toBeInTheDocument();
    });

    test("filters publications by type", () => {
        mockUsePublications.mockReturnValue({
            publications: [
                ...mockPublications,
                {
                    id: "3",
                    title: "National Geographic",
                    author: "Various",
                    genre: "Science",
                    type: "magazine",
                    isbn: "12345678",
                    condition: "good",
                    price: 5,
                },
            ],
            loading: false,
            error: null,
        });

        renderMyPublications();

        const typeSelect = screen.getByDisplayValue("All Types");
        fireEvent.change(typeSelect, { target: { value: "magazine" } });

        expect(screen.getByText("National Geographic")).toBeInTheDocument();
        expect(screen.queryByText("The Lord of the Rings")).not.toBeInTheDocument();
    });

    test("switches between grid and list view", () => {
        renderMyPublications();

        const listViewButton = screen.getByTitle("List view");
        fireEvent.click(listViewButton);

        expect(screen.getByText("J.R.R. Tolkien")).toBeInTheDocument();

        const gridViewButton = screen.getByTitle("Grid view");
        fireEvent.click(gridViewButton);

        expect(screen.getByText("The Lord of the Rings")).toBeInTheDocument();
    });

    test("navigates to sell page when clicking Sell", () => {
        renderMyPublications();

        const sellButtons = screen.getAllByText("Sell");
        fireEvent.click(sellButtons[0]);

        expect(mockNavigate).toHaveBeenCalledWith("/sell/1");
    });

    test("opens AddToListModal when clicking add to list button", () => {
        renderMyPublications();

        const addToListButtons = screen.getAllByTitle("Add to List");
        fireEvent.click(addToListButtons[0]);

        expect(screen.getByTestId("add-to-list-modal")).toBeInTheDocument();
    });

    test("closes AddToListModal when clicking close", () => {
        renderMyPublications();

        fireEvent.click(screen.getAllByTitle("Add to List")[0]);
        expect(screen.getByTestId("add-to-list-modal")).toBeInTheDocument();

        fireEvent.click(screen.getByText("Close Modal"));
        expect(screen.queryByTestId("add-to-list-modal")).not.toBeInTheDocument();
    });
});