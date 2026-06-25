import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { vi, describe, beforeEach, test, expect } from "vitest";
import { AddToListModal } from "./AddToListModal";

// ── Mocks ─────────────────────────────────────────────────────────────

/** Spy for {@link useLists.addPublicationToList}. Resolves by default. */
const mockAddPublicationToList = vi.fn();

/** Spy for {@link usePublications.refresh}. Resolves by default. */
const mockRefresh = vi.fn();

/**
 * Mutable backing array for the lists returned by {@link useLists}.
 * Reset to a standard two-list fixture in {@link beforeEach}.
 * @type {Array<{collectionId: string, collectionName: string}>}
 */
let mockListsData = [];

/**
 * Mutable backing array for the publications returned by {@link usePublications}.
 * Reset to a single in-library, unassigned publication in {@link beforeEach}.
 * @type {Array<{id: string, libraryId: string|null, collectionId: string|null}>}
 */
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

/**
 * Default props used across all tests.
 * Individual tests can override any prop via {@link renderModal}.
 */
const defaultProps = {
    isOpen: true,
    onClose: vi.fn(),
    bookTitle: "The Lord of the Rings",
    publicationId: "pub-1",
};

/**
 * Renders {@link AddToListModal} with {@link defaultProps} merged with any
 * additional props supplied by the caller.
 *
 * @param {Partial<typeof defaultProps>} [props={}] - Props to override.
 * @returns {import("@testing-library/react").RenderResult}
 */
function renderModal(props = {}) {
    return render(<AddToListModal {...defaultProps} {...props} />);
}

// ── Test suite ────────────────────────────────────────────────────────

describe("AddToListModal", () => {
    beforeEach(() => {
        vi.clearAllMocks();

        // Standard fixture: two lists available.
        mockListsData = [
            { collectionId: "1", collectionName: "Favourites" },
            { collectionId: "2", collectionName: "To Read" },
        ];

        // Standard fixture: publication is in the library and not yet in any list.
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
        // libraryId: null — the publication has not been added to the library yet.
        mockPublicationsData = [{ id: "pub-1", libraryId: null, collectionId: null }];
        renderModal();

        expect(
            screen.getByText("Add this publication to your library first before adding it to a list.")
        ).toBeInTheDocument();
        // List rows must not be rendered while the library guard is active.
        expect(screen.queryByText("Favourites")).not.toBeInTheDocument();
    });

    test("shows warning if publication is already in a list", () => {
        // collectionId: "1" — the publication is already assigned to a list.
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
        // Publication is in collection "1"; clicking "To Read" (collection "2") must be a no-op.
        mockPublicationsData = [{ id: "pub-1", libraryId: "lib-1", collectionId: "1" }];
        renderModal();

        fireEvent.click(screen.getByText("To Read"));

        expect(mockAddPublicationToList).not.toHaveBeenCalled();
    });

    test("calls addPublicationToList and shows success state when clicking a list", async () => {
        renderModal();

        fireEvent.click(screen.getByText("Favourites"));

        // The service call must receive the correct collectionId and publicationId.
        expect(mockAddPublicationToList).toHaveBeenCalledWith("1", "pub-1");

        await waitFor(() => {
            expect(mockRefresh).toHaveBeenCalled();
        });

        // The success screen replaces the list UI after the async flow completes.
        expect(await screen.findByText("Added to List!")).toBeInTheDocument();
    });

    test("calls onClose automatically after success timeout", async () => {
        const onCloseMock = vi.fn();
        renderModal({ onClose: onCloseMock });

        fireEvent.click(screen.getByText("Favourites"));

        // Confirm the success screen is visible before waiting for auto-close.
        expect(await screen.findByText("Added to List!")).toBeInTheDocument();

        // The component's internal delay is 1500 ms; allow 2500 ms for safety.
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

        // The close button is identified by its aria-label to avoid coupling to icon internals.
        fireEvent.click(screen.getByLabelText("Close"));

        expect(onCloseMock).toHaveBeenCalledTimes(1);
    });
});