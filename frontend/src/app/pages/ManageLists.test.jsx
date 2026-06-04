import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor, act } from "@testing-library/react";
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
const mockMakePrivate = vi.fn(); // Fixed: Added missing mock
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
        makePrivate: mockMakePrivate,
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

        mockListsData = [
            { collectionId: "1", collectionName: "Favourites", private: true },
            { collectionId: "2", collectionName: "Public Books", private: false }
        ];

        mockPublicationsData = [
            { id: "pub-1", title: "The Lord of the Rings", author: "Tolkien", libraryId: "lib-1", collectionId: null },
            { id: "pub-2", title: "Dune", author: "Herbert", libraryId: "lib-1", collectionId: "1" }
        ];

        global.fetch = vi.fn(async (url) => {
            if (url.includes("/genres")) {
                return {
                    ok: true,
                    json: async () => [{ id: "g1", genreName: "Fantasy" }]
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

    // ── Tests ──
    test("renders lists correctly", async () => {
        renderManageLists();
        expect(await screen.findByText("Favourites")).toBeInTheDocument();
        expect(screen.getByText("Public Books")).toBeInTheDocument();
        expect(screen.getByText("Private")).toBeInTheDocument();
        expect(screen.getByText("Public")).toBeInTheDocument();
    });

    test("toggles visibility to public", async () => {
        renderManageLists();

        // Find button by title for Private list "Favourites"
        const toggleBtn = await screen.findByTitle("Make Public");

        await act(async () => {
            fireEvent.click(toggleBtn);
        });

        expect(mockMakePublic).toHaveBeenCalledWith("1");
    });

    test("toggles visibility to private", async () => {
        renderManageLists();

        // Find button by title for Public list "Public Books"
        const toggleBtn = await screen.findByTitle("Make Private");

        await act(async () => {
            fireEvent.click(toggleBtn);
        });

        expect(mockMakePrivate).toHaveBeenCalledWith("2");
    });

    test("adds a publication to the list", async () => {
        renderManageLists();

        fireEvent.click(screen.getAllByTitle("Add publications")[0]);

        // "Add" button for 'pub-1'
        const addButton = await screen.findByRole("button", { name: "Add" });

        await act(async () => {
            fireEvent.click(addButton);
        });

        expect(mockAddPublicationToList).toHaveBeenCalledWith("1", "pub-1");

        await waitFor(() => {
            expect(mockRefresh).toHaveBeenCalled();
        });
    });
});