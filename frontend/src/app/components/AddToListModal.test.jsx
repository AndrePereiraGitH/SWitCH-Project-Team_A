import { render, screen, fireEvent, act } from "@testing-library/react";
import { vi } from "vitest";
import { AddToListModal } from "./AddToListModal";

const mockAddPublicationToList = vi.fn();
const mockIsPublicationInList = vi.fn();

let mockListsData = [
    { id: "1", name: "Favourites", publicationIds: [] },
    { id: "2", name: "To Read", publicationIds: ["pub-1"] },
];

vi.mock("./ListsContext", () => ({
    useLists: () => ({
        lists: mockListsData,
        addPublicationToList: mockAddPublicationToList,
        isPublicationInList: mockIsPublicationInList,
    }),
}));

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
        mockIsPublicationInList.mockReturnValue(false);
        mockListsData = [
            { id: "1", name: "Favourites", publicationIds: [] },
            { id: "2", name: "To Read", publicationIds: ["pub-1"] },
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

    test("renders all lists", () => {
        renderModal();
        expect(screen.getByText("Favourites")).toBeInTheDocument();
        expect(screen.getByText("To Read")).toBeInTheDocument();
    });

    test("shows empty message when no lists exist", () => {
        mockListsData = [];
        renderModal();
        expect(screen.getByText("No lists created yet.")).toBeInTheDocument();
    });

    test("shows checkbox as checked and disabled when publication is already in list", () => {
        mockIsPublicationInList.mockImplementation((listId) => listId === "1");
        renderModal();

        const checkboxes = screen.getAllByRole("checkbox");
        expect(checkboxes[0]).toBeChecked();
        expect(checkboxes[0]).toBeDisabled();
    });

    test("shows checkbox as unchecked and enabled when publication is not in list", () => {
        mockIsPublicationInList.mockReturnValue(false);
        renderModal();

        const checkboxes = screen.getAllByRole("checkbox");
        expect(checkboxes[0]).not.toBeChecked();
        expect(checkboxes[0]).not.toBeDisabled();
    });

    test("calls addPublicationToList when checking an unchecked list", () => {
        mockIsPublicationInList.mockReturnValue(false);
        renderModal();

        const checkboxes = screen.getAllByRole("checkbox");
        fireEvent.click(checkboxes[0]);

        expect(mockAddPublicationToList).toHaveBeenCalledWith("1", "pub-1");
    });

    test("does not call addPublicationToList when publication is already in list", () => {
        mockIsPublicationInList.mockReturnValue(true);
        renderModal();

        const checkboxes = screen.getAllByRole("checkbox");
        fireEvent.click(checkboxes[0]);

        expect(mockAddPublicationToList).not.toHaveBeenCalled();
    });

    test("shows success state after clicking Done", () => {
        renderModal();

        fireEvent.click(screen.getByText("Done"));

        expect(screen.getByText("Lists Updated!")).toBeInTheDocument();
    });

    test("calls onClose after success timeout", async () => {
        vi.useFakeTimers();
        const onClose = vi.fn();
        renderModal({ onClose });

        fireEvent.click(screen.getByText("Done"));
        expect(screen.getByText("Lists Updated!")).toBeInTheDocument();

        await act(async () => {
            vi.advanceTimersByTime(1500);
        });

        expect(onClose).toHaveBeenCalledTimes(1);
        vi.useRealTimers();
    });

    test("calls onClose when clicking the Close button", () => {
        const onClose = vi.fn();
        renderModal({ onClose });

        fireEvent.click(screen.getByRole("button", { name: "Close" }));

        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test("displays correct item count for each list", () => {
        renderModal();

        expect(screen.getByText("0 items")).toBeInTheDocument();
        expect(screen.getByText("1 item")).toBeInTheDocument();
    });
});