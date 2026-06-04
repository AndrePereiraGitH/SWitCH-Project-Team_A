import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { ManageLists } from "./ManageLists";

// ── Mocks ─────────────────────────────────────────────────────────────

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const mockAddList = vi.fn();
const mockMakePublic = vi.fn();
const mockMakePublicUntil = vi.fn();
const mockAddPublicationToList = vi.fn();

let mockListsData = [];

vi.mock("../components/ListsContext", () => ({
    useLists: () => ({
        lists: mockListsData,
        loading: false,
        error: null,
        addList: mockAddList,
        makePublic: mockMakePublic,
        makePublicUntil: mockMakePublicUntil,
        addPublicationToList: mockAddPublicationToList,
    })
}));

const mockRefresh = vi.fn();
let mockPublicationsData = [];

vi.mock("../components/PublicationsContext", () => ({
    usePublications: () => ({
        publications: mockPublicationsData,
        refresh: mockRefresh
    })
}));

// ── Setup ─────────────────────────────────────────────────────────────

describe("ManageLists Component", () => {
    beforeEach(() => {
        vi.clearAllMocks();

        // Setup initial mock data
        mockListsData = [
            { collectionId: "1", collectionName: "Favourites", private: true },
            { collectionId: "2", collectionName: "Public Books", private: false }
        ];

        mockPublicationsData = [
            { id: "pub-1", title: "The Lord of the Rings", author: "Tolkien", libraryId: "lib-1", collectionId: null },
            { id: "pub-2", title: "Dune", author: "Herbert", libraryId: "lib-1", collectionId: "1" }
        ];

        // Mock the genres fetch
        global.fetch = vi.fn(async (url) => {
            if (url.includes("/genres")) {
                return {
                    ok: true,
                    json: async () => [{ id: "g1", genreName: "Fantasy" }, { id: "g2", genreName: "Sci-Fi" }]
                };
            }
            return { ok: false };
        });
    });

    function renderManageLists() {
        render(
            <MemoryRouter>
                <ManageLists />
            </MemoryRouter>
        );
    }

    // ── Rendering ──
    test("renders lists correctly", async () => {
        renderManageLists();

        expect(screen.getByText("Favourites")).toBeInTheDocument();
        expect(screen.getByText("Public Books")).toBeInTheDocument();

        // Checks visibility badges
        expect(screen.getByText("Private")).toBeInTheDocument();
        expect(screen.getByText("Public")).toBeInTheDocument();
    });

    // ── Create List ──
    test("opens create modal and submits new list", async () => {
        renderManageLists();

        fireEvent.click(screen.getByText("Create New List"));
        expect(screen.getByRole("heading", { name: "Create New List" })).toBeInTheDocument();

        // Fill form
        fireEvent.change(screen.getByLabelText(/List Name/i), { target: { value: "Summer Reads" } });

        // Wait for genres to load
        await waitFor(() => expect(screen.getByText("Fantasy")).toBeInTheDocument());
        fireEvent.change(screen.getByLabelText(/Genre/i), { target: { value: "g1" } });

        fireEvent.click(screen.getByRole("button", { name: "Create List" }));

        await waitFor(() => {
            expect(mockAddList).toHaveBeenCalledWith({
                collectionName: "Summer Reads",
                genreId: "g1"
            });
        });
    });

    // ── Visibility Toggle ──
    test("calls makePublic when clicking unlock on a private list", async () => {
        renderManageLists();

        // The first list "Favourites" is private. The button title should be "Make Public"
        const makePublicBtns = screen.getAllByTitle("Make Public");
        fireEvent.click(makePublicBtns[0]);

        expect(mockMakePublic).toHaveBeenCalledWith("1");
    });

    test("public list toggle button is disabled", async () => {
        renderManageLists();

        const alreadyPublicBtn = screen.getByTitle("Already public");
        expect(alreadyPublicBtn).toBeDisabled();
    });

    // ── Add Publications Modal ──
    test("opens add publications modal and shows list of available items", async () => {
        renderManageLists();

        // Click "Add publications" on the first list (Favourites)
        const addPubBtns = screen.getAllByTitle("Add publications");
        fireEvent.click(addPubBtns[0]);

        expect(screen.getByRole("heading", { name: "Add Publications" })).toBeInTheDocument();

        // Check if publications are rendered
        expect(screen.getByText("The Lord of the Rings")).toBeInTheDocument();
        expect(screen.getByText("Dune")).toBeInTheDocument();

        // Dune is already in list 1, so it shouldn't have an "Add" button, it should say "Added"
        expect(screen.getByText("Added")).toBeInTheDocument();
    });

    test("adds a publication to the list", async () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Add publications")[0]);

        // Lord of the Rings is NOT in list 1, so it should have an "Add" button
        const addButton = screen.getByRole("button", { name: "Add" });
        fireEvent.click(addButton);

        expect(mockAddPublicationToList).toHaveBeenCalledWith("1", "pub-1");

        // Ensure refresh is called to sync state
        await waitFor(() => {
            expect(mockRefresh).toHaveBeenCalled();
        });
    });

    test("filters publications in the add modal search bar", async () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Add publications")[0]);

        const searchInput = screen.getByPlaceholderText("Search publications…");
        fireEvent.change(searchInput, { target: { value: "Dune" } });

        expect(screen.getByText("Dune")).toBeInTheDocument();
        expect(screen.queryByText("The Lord of the Rings")).not.toBeInTheDocument();
    });

    // ── Navigation ──
    test("navigates back when back button is clicked", () => {
        renderManageLists();
        fireEvent.click(screen.getByText("Back"));
        expect(mockNavigate).toHaveBeenCalledWith(-1);
    });
});