import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { vi } from "vitest";
import { ManageLists } from "../pages/ManageLists";

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const mockAddList = vi.fn();
const mockUpdateList = vi.fn();
const mockAddPublicationToList = vi.fn();
const mockIsPublicationInList = vi.fn();
const mockGetAllPublications = vi.fn();

let mockListsData = [];

vi.mock("../components/ListsContext", () => ({
    useLists: () => ({
        lists: mockListsData,
        addList: mockAddList,
        updateList: mockUpdateList,
        addPublicationToList: mockAddPublicationToList,
        isPublicationInList: mockIsPublicationInList,
    }),
}));

vi.mock("../components/PublicationsContext", () => ({
    usePublications: () => ({
        getAllPublications: mockGetAllPublications,
    }),
}));

const mockLists = [
    {
        id: "1",
        name: "Favourites",
        genre: "Fantasy",
        isPublic: false,
        publicationIds: [],
        sharedUntil: null,
    },
    {
        id: "2",
        name: "To Read",
        genre: "Fiction",
        isPublic: true,
        publicationIds: ["pub-1"],
        sharedUntil: null,
    },
];

const mockPublications = [
    { id: "pub-1", title: "The Lord of the Rings", author: "J.R.R. Tolkien" },
    { id: "pub-2", title: "Dune", author: "Frank Herbert" },
];

function renderManageLists() {
    return render(
        <MemoryRouter>
            <ManageLists />
        </MemoryRouter>
    );
}

describe("ManageLists", () => {
    beforeEach(() => {
        vi.clearAllMocks();
        mockListsData = [...mockLists];
        mockGetAllPublications.mockReturnValue(mockPublications);
        mockIsPublicationInList.mockReturnValue(false);
    });

    test("renders page title and lists", () => {
        renderManageLists();

        expect(screen.getByText("Manage Lists")).toBeInTheDocument();
        expect(screen.getByText("Favourites")).toBeInTheDocument();
        expect(screen.getByText("To Read")).toBeInTheDocument();
    });

    test("renders Create New List card", () => {
        renderManageLists();

        expect(screen.getByText("Create New List")).toBeInTheDocument();
    });

    test("shows only create card when no lists exist", () => {
        mockListsData = [];
        renderManageLists();

        expect(screen.getByText("Create New List")).toBeInTheDocument();
        expect(screen.queryByText("Favourites")).not.toBeInTheDocument();
    });

    test("displays public/private status for each list", () => {
        renderManageLists();

        expect(screen.getByText("Private")).toBeInTheDocument();
        expect(screen.getByText("Public")).toBeInTheDocument();
    });

    test("displays book count for each list", () => {
        renderManageLists();

        expect(screen.getByText("0 books")).toBeInTheDocument();
        expect(screen.getByText("1 books")).toBeInTheDocument();
    });

    test("navigates back when clicking Back button", () => {
        renderManageLists();

        fireEvent.click(screen.getByText("Back"));

        expect(mockNavigate).toHaveBeenCalledWith(-1);
    });

    // Create modal tests
    test("opens create modal when clicking New List button", () => {
        renderManageLists();

        fireEvent.click(screen.getByText("New List"));

        expect(screen.getByLabelText("List Name *")).toBeInTheDocument();
    });

    test("opens create modal when clicking Create New List card", () => {
        renderManageLists();

        fireEvent.click(screen.getByText("Create New List"));

        expect(screen.getByLabelText("List Name *")).toBeInTheDocument();
    });

    test("closes create modal when clicking Cancel", () => {
        renderManageLists();

        fireEvent.click(screen.getByText("New List"));
        expect(screen.getByLabelText("List Name *")).toBeInTheDocument();

        fireEvent.click(screen.getByText("Cancel"));

        expect(screen.queryByLabelText("List Name *")).not.toBeInTheDocument();
    });

    test("closes create modal when clicking X button", () => {
        renderManageLists();

        fireEvent.click(screen.getByText("New List"));
        fireEvent.click(screen.getByRole("button", { name: "Close create modal" }));

        expect(screen.queryByLabelText("List Name *")).not.toBeInTheDocument();
    });

    test("calls addList with correct data on valid submit", () => {
        renderManageLists();

        fireEvent.click(screen.getByText("New List"));
        fireEvent.change(screen.getByLabelText("List Name *"), {
            target: { value: "My New List" },
        });
        fireEvent.change(screen.getByLabelText("Genre *"), {
            target: { value: "Fantasy" },
        });
        fireEvent.submit(screen.getByText("Create List"));

        expect(mockAddList).toHaveBeenCalledWith(
            expect.objectContaining({
                name: "My New List",
                genre: "Fantasy",
                isPublic: false,
            })
        );
    });

    test("shows share duration field when list is set to public", () => {
        renderManageLists();

        fireEvent.click(screen.getByText("New List"));
        fireEvent.click(screen.getByRole("checkbox", { name: /share publicly/i }));

        expect(screen.getByLabelText("Share Duration (days)")).toBeInTheDocument();
    });

    test("hides share duration field when list is private", () => {
        renderManageLists();

        fireEvent.click(screen.getByText("New List"));

        expect(screen.queryByLabelText("Share Duration (days)")).not.toBeInTheDocument();
    });

    test("resets form after creating a list", () => {
        renderManageLists();

        fireEvent.click(screen.getByText("New List"));
        fireEvent.change(screen.getByLabelText("List Name *"), {
            target: { value: "My New List" },
        });
        fireEvent.change(screen.getByLabelText("Genre *"), {
            target: { value: "Fantasy" },
        });
        fireEvent.submit(screen.getByText("Create List"));

        fireEvent.click(screen.getByText("New List"));
        expect(screen.getByLabelText("List Name *").value).toBe("");
    });

    // Toggle public tests
    test("calls updateList when toggling public/private", () => {
        renderManageLists();

        const toggleButtons = screen.getAllByTitle(/Make Public|Make Private/);
        fireEvent.click(toggleButtons[0]);

        expect(mockUpdateList).toHaveBeenCalledWith("1", { isPublic: true });
    });

    // Edit modal tests
    test("opens edit modal when clicking Edit button", () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Edit list")[0]);

        expect(screen.getByText("Edit List")).toBeInTheDocument();
        expect(screen.getByLabelText("List Name *")).toBeInTheDocument();
    });

    test("closes edit modal when clicking Cancel", () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Edit list")[0]);
        expect(screen.getByText("Edit List")).toBeInTheDocument();

        fireEvent.click(screen.getByText("Cancel"));

        expect(screen.queryByText("Edit List")).not.toBeInTheDocument();
    });

    test("closes edit modal when clicking X button", () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Edit list")[0]);
        fireEvent.click(screen.getByRole("button", { name: "Close edit modal" }));

        expect(screen.queryByText("Edit List")).not.toBeInTheDocument();
    });

    test("calls updateList when editing list name", () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Edit list")[0]);
        fireEvent.change(screen.getByLabelText("List Name *"), {
            target: { value: "Updated Name" },
        });

        expect(mockUpdateList).toHaveBeenCalledWith("1", { name: "Updated Name" });
    });

    test("shows publications in edit modal", () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Edit list")[0]);

        expect(screen.getByText("The Lord of the Rings")).toBeInTheDocument();
        expect(screen.getByText("Dune")).toBeInTheDocument();
    });

    test("filters publications by search query in edit modal", () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Edit list")[0]);
        fireEvent.change(screen.getByPlaceholderText("Search publications..."), {
            target: { value: "Dune" },
        });

        expect(screen.getByText("Dune")).toBeInTheDocument();
        expect(screen.queryByText("The Lord of the Rings")).not.toBeInTheDocument();
    });

    test("calls addPublicationToList when checking a publication in edit modal", () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Edit list")[0]);

        const pubCheckbox = screen.getByRole("checkbox", { name: /the lord of the rings/i });
        fireEvent.click(pubCheckbox);

        expect(mockAddPublicationToList).toHaveBeenCalledWith("1", "pub-1");
    });

    test("does not call addPublicationToList when publication is already in list", () => {
        mockIsPublicationInList.mockReturnValue(true);
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Edit list")[0]);

        const pubCheckbox = screen.getByRole("checkbox", { name: /the lord of the rings/i });
        fireEvent.click(pubCheckbox);

        expect(mockAddPublicationToList).not.toHaveBeenCalled();
    });

    test("saves changes and closes edit modal when clicking Save Changes", () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Edit list")[0]);
        fireEvent.submit(screen.getByText("Save Changes"));

        expect(screen.queryByText("Edit List")).not.toBeInTheDocument();
    });
});