import { render, screen, fireEvent } from "@testing-library/react";
import { vi } from "vitest";
import { SuccessModal } from "./SuccessModal";

describe("SuccessModal", () => {
    test("does not render when isOpen is false", () => {
        render(<SuccessModal isOpen={false} onClose={() => {}} message="Done!" />);
        expect(screen.queryByText("Done!")).not.toBeInTheDocument();
    });

    test("renders when isOpen is true", () => {
        render(<SuccessModal isOpen={true} onClose={() => {}} message="Done!" />);
        expect(screen.getByText("Done!")).toBeInTheDocument();
        expect(screen.getByText("Success!")).toBeInTheDocument();
    });

    test("calls onClose when OK button is clicked", () => {
        const onClose = vi.fn();
        render(<SuccessModal isOpen={true} onClose={onClose} message="Done!" />);
        fireEvent.click(screen.getByText("OK"));
        expect(onClose).toHaveBeenCalledTimes(1);
    });

    test("displays the correct message", () => {
        render(<SuccessModal isOpen={true} onClose={() => {}} message="Publication added!" />);
        expect(screen.getByText("Publication added!")).toBeInTheDocument();
    });
});