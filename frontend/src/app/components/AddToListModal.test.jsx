import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { vi, describe, beforeEach, test, expect } from "vitest";
import { AddToListModal } from "./AddToListModal";

// ── Mocks ─────────────────────────────────────────────────────────────

const mockAddPublicationToList = vi.fn();
const mockRefresh = vi.fn();

let mockListsData = [];
let mockPublicationsData = [];

vi.mock("./ListsContext", () => ({
    useLists: () => ({
        lists: mockListsData,
        addPublicationToList: mockAddPublicationToList,
    }),
}));

vi.mock("./PublicationsContext", () => ({
    usePublications: () => ({
        publications: mockPublicationsData,
        refresh: mockRefresh,
    }),
}));

// ── Setup ─────────────────────────────────────────────────────────────

const defaultProps = {
    isOpen: true,
    onClose: vi.fn(),
    bookTitle: "The Lord of the Rings",
    publicationId: "pub-1",
};

function renderModal(props = {}) {
    return render(<AddToListModal {...defaultProps} {...props} />);
}

describe("AddToListModal", () => {
    beforeEach(() => {
        vi.clearAllMocks();

        mockListsData = [
            { collectionId: "1", collectionName: "Favourites" },
            { collectionId: "2", collectionName: "To Read" },
        ];

        mockPublicationsData = [
            { id: "pub-1", title: "The Lord of the Rings", libraryId: "lib-1", collectionId: null }
        ];
    });

    test("does not render when isOpen is false", () => {
        renderModal({ isOpen: false });
        expect(screen.queryByText("Add to List")).not.toBeInTheDocument();
    });

    test("renders modal with book title when isOpen is true", () => {
        renderModal();
        expect(screen.getByText("Add to List")).toBeInTheDocument();
        expect(screen.getByText("The Lord of the Rings")).toBeInTheDocument();
    });

    test("shows warning if publication is not in the library", () => {
        // Set libraryId to null to simulate a book not in the library
        mockPublicationsData = [{ id: "pub-1", libraryId: null, collectionId: null }];
        renderModal();

        expect(
            screen.getByText("Add this publication to your library first before adding it to a list.")
        ).toBeInTheDocument();
        // Ensure lists are NOT rendered
        expect(screen.queryByText("Favourites")).not.toBeInTheDocument();
    });

    test("shows warning if publication is already in a list", () => {
        // Set collectionId to simulate a book already in a list
        mockPublicationsData = [{ id: "pub-1", libraryId: "lib-1", collectionId: "1" }];
        renderModal();

        expect(
            screen.getByText("This publication is already in a list. Remove it from there first.")
        ).toBeInTheDocument();
    });

    test("renders all lists if available to add", () => {
        renderModal();
        expect(screen.getByText("Favourites")).toBeInTheDocument();
        expect(screen.getByText("To Read")).toBeInTheDocument();
    });

    test("shows empty message when no lists exist", () => {
        mockListsData = [];
        renderModal();
        expect(screen.getByText("No lists created yet.")).toBeInTheDocument();
    });

    test("does not call addPublicationToList if already in a list and another list is clicked", () => {
        mockPublicationsData = [{ id: "pub-1", libraryId: "lib-1", collectionId: "1" }];
        renderModal();

        // Clicking "To Read" (collection 2) should be disabled
        fireEvent.click(screen.getByText("To Read"));

        expect(mockAddPublicationToList).not.toHaveBeenCalled();
    });

    test("calls addPublicationToList and shows success state when clicking a list", async () => {
        renderModal();

        // Click "Favourites" list
        fireEvent.click(screen.getByText("Favourites"));

        // API calls should be made
        expect(mockAddPublicationToList).toHaveBeenCalledWith("1", "pub-1");

        await waitFor(() => {
            expect(mockRefresh).toHaveBeenCalled();
        });

        // Success UI should appear
        expect(await screen.findByText("Added to List!")).toBeInTheDocument();
    });

    test("calls onClose automatically after success timeout", async () => {
        const onCloseMock = vi.fn();
        renderModal({ onClose: onCloseMock });

        fireEvent.click(screen.getByText("Favourites"));

        // Wait for success screen to render
        expect(await screen.findByText("Added to List!")).toBeInTheDocument();

        // Wait for onClose to be called automatically
        // We set timeout to 2500ms to safely cover the component's internal 1500ms delay
        await waitFor(() => {
            expect(onCloseMock).toHaveBeenCalledTimes(1);
        }, { timeout: 2500 });
    });

    test("calls onClose when clicking the Cancel button", () => {
        const onCloseMock = vi.fn();
        renderModal({ onClose: onCloseMock });

        fireEvent.click(screen.getByText("Cancel"));

        expect(onCloseMock).toHaveBeenCalledTimes(1);
    });

    test("calls onClose when clicking the X icon", () => {
        const onCloseMock = vi.fn();
        renderModal({ onClose: onCloseMock });

        // Target the button containing the X icon
        fireEvent.click(screen.getByLabelText("Close"));

        expect(onCloseMock).toHaveBeenCalledTimes(1);
    });
});