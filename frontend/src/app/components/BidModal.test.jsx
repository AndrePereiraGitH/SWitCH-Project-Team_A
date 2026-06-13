import { describe, test, expect, vi } from "vitest";
import { screen, fireEvent, renderWithProviders } from "../../test-utils";
import { BidModal } from "./BidModal";

// Default props reused across tests
const defaultProps = {
    isOpen: true,
    onClose: vi.fn(),
    itemTitle: "The Great Gatsby",
    currentBid: 10,
    buyoutPrice: 25,
    onSubmitBid: vi.fn(),
    onBuyNow: vi.fn(),
};

// BidModal has no provider dependencies, but renderWithProviders keeps the suite consistent
function renderModal(props = {}) {
    return renderWithProviders(<BidModal {...defaultProps} {...props} />);
}

// ── Visibility ─────────────────────────────────────────────────────────────────

describe("BidModal — Visibility", () => {

    test("renders nothing when isOpen is false", () => {
        renderModal({ isOpen: false });
        expect(screen.queryByText("Place Bid")).not.toBeInTheDocument();
    });

    test("renders the modal when isOpen is true", () => {
        renderModal();
        expect(screen.getByText("Place Bid")).toBeInTheDocument();
    });

});

// ── Content ────────────────────────────────────────────────────────────────────

describe("BidModal — Content", () => {

    test("shows the item title", () => {
        renderModal();
        expect(screen.getByText("The Great Gatsby")).toBeInTheDocument();
    });

    test("shows the current bid formatted to 2 decimal places", () => {
        renderModal({ currentBid: 10 });
        expect(screen.getByText("10.00")).toBeInTheDocument();
    });

    test("shows the buyout price when provided", () => {
        renderModal({ buyoutPrice: 25 });
        expect(screen.getByText("25.00")).toBeInTheDocument();
    });

    test("does not show buyout section when buyoutPrice is null", () => {
        renderModal({ buyoutPrice: null });
        expect(screen.queryByText("Buyout Price")).not.toBeInTheDocument();
    });

    test("shows the minimum bid calculated correctly", () => {
        renderModal({ currentBid: 10 });
        expect(screen.getByText("Minimum: €10.01")).toBeInTheDocument();
    });

});

// ── Interactions ───────────────────────────────────────────────────────────────

describe("BidModal — Interactions", () => {

    test("close button calls onClose", () => {
        const onClose = vi.fn();
        renderModal({ onClose });
        const closeBtn = screen.getByRole("button", { name: "" });
        fireEvent.click(closeBtn);
        expect(onClose).toHaveBeenCalled();
    });

    test("submitting a valid bid calls onSubmitBid with the value", async () => {
        const onSubmitBid = vi.fn();
        const onClose = vi.fn();
        renderModal({ onSubmitBid, onClose, currentBid: 10 });

        const input = screen.getByRole("spinbutton");
        fireEvent.change(input, { target: { value: "15" } });
        fireEvent.submit(input.closest("form"));

        expect(onSubmitBid).toHaveBeenCalledWith(15, expect.any(Function));
        expect(onClose).not.toHaveBeenCalled();
    });

    test("shows error message when onSubmitBid calls the error callback", async () => {
        const onSubmitBid = vi.fn((amount, onError) => onError("Something went wrong. Please try again."));
        renderModal({ onSubmitBid, currentBid: 10 });

        const input = screen.getByRole("spinbutton");
        fireEvent.change(input, { target: { value: "15" } });
        fireEvent.submit(input.closest("form"));

        expect(await screen.findByText("Something went wrong. Please try again.")).toBeInTheDocument();
    });

    test("does not call onSubmitBid if bid is lower than currentBid", () => {
        const onSubmitBid = vi.fn();
        renderModal({ onSubmitBid, currentBid: 10 });

        const input = screen.getByRole("spinbutton");
        fireEvent.change(input, { target: { value: "5" } });
        fireEvent.submit(input.closest("form"));

        expect(onSubmitBid).not.toHaveBeenCalled();
    });

    test("Buy Now button calls onBuyNow and does not call onClose directly", async () => {
        const onBuyNow = vi.fn();
        const onClose = vi.fn();
        renderModal({ onBuyNow, onClose });

        fireEvent.click(screen.getByText("Buy Now"));

        expect(onBuyNow).toHaveBeenCalled();
        expect(onClose).not.toHaveBeenCalled();
    });

    test("shows error when bid amount is empty", async () => {
        const onSubmitBid = vi.fn();
        renderModal({ onSubmitBid, currentBid: 10 });

        const input = screen.getByRole("spinbutton");
        fireEvent.change(input, { target: { value: "" } });
        fireEvent.submit(input.closest("form"));

        expect(onSubmitBid).not.toHaveBeenCalled();
        expect(await screen.findByText("Please enter a valid bid amount.")).toBeInTheDocument();
    });

    test("shows error when bid is equal to currentBid", async () => {
        const onSubmitBid = vi.fn();
        renderModal({ onSubmitBid, currentBid: 10 });

        const input = screen.getByRole("spinbutton");
        fireEvent.change(input, { target: { value: "10" } });
        fireEvent.submit(input.closest("form"));

        expect(onSubmitBid).not.toHaveBeenCalled();
        expect(await screen.findByText("Bid must be higher than the current bid of €10.00")).toBeInTheDocument();
    });

    test("does not call onSubmitBid if bid exceeds buyout price", async () => {
        const onSubmitBid = vi.fn();
        renderModal({ onSubmitBid, currentBid: 10, buyoutPrice: 25 });

        const input = screen.getByRole("spinbutton");
        fireEvent.change(input, { target: { value: "30" } });
        fireEvent.submit(input.closest("form"));

        expect(onSubmitBid).not.toHaveBeenCalled();
        expect(await screen.findByText("Bid cannot exceed the buyout price")).toBeInTheDocument();
    });
});
