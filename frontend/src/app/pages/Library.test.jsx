import { describe, test, expect } from "vitest";
import { screen, fireEvent } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { Library } from "./Library";

function renderLibrary() {
    return renderWithProviders(<Library />);
}

// ── No library state ───────────────────────────────────────────────────────────

describe("Library — No Library Created", () => {

    test("shows No Library Created heading", () => {
        renderLibrary();
        expect(screen.getByText("No Library Created")).toBeInTheDocument();
    });

    test("shows descriptive message", () => {
        renderLibrary();
        expect(
            screen.getByText(/Create your personal library/i)
        ).toBeInTheDocument();
    });

    test("shows Create Library button", () => {
        renderLibrary();
        expect(
            screen.getByRole("button", { name: /Create Library/i })
        ).toBeInTheDocument();
    });

    test("shows back to dashboard button", () => {
        renderLibrary();
        expect(screen.getByText(/My Dashboard/i)).toBeInTheDocument();
    });

});

// ── Create library modal ───────────────────────────────────────────────────────

describe("Library — Create Library Modal", () => {

    test("clicking Create Library opens the modal", () => {
        renderLibrary();
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        expect(screen.getByText("Create New Library")).toBeInTheDocument();
    });

    test("modal shows library name input", () => {
        renderLibrary();
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        expect(
            screen.getByPlaceholderText(/Ex: My Collection/i)
        ).toBeInTheDocument();
    });

    test("Create Library button is disabled when name is empty", () => {
        renderLibrary();
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        const createBtn = screen.getAllByRole("button", { name: /Create Library/i });
        const modalBtn = createBtn[createBtn.length - 1];
        expect(modalBtn).toBeDisabled();
    });

    test("Create Library button enables when name is typed", () => {
        renderLibrary();
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        const input = screen.getByPlaceholderText(/Ex: My Collection/i);
        fireEvent.change(input, { target: { value: "My Books" } });
        const buttons = screen.getAllByRole("button", { name: /Create Library/i });
        const modalBtn = buttons[buttons.length - 1];
        expect(modalBtn).not.toBeDisabled();
    });

    test("Cancel button closes the modal", () => {
        renderLibrary();
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        expect(screen.getByText("Create New Library")).toBeInTheDocument();
        fireEvent.click(screen.getByRole("button", { name: /Cancel/i }));
        expect(screen.queryByText("Create New Library")).not.toBeInTheDocument();
    });

    test("modal shows Library Name label", () => {
        renderLibrary();
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        expect(screen.getByText(/Library Name/i)).toBeInTheDocument();
    });

});

// ── Library exists ────────────────────────────────────────────────────────────

describe("Library — Library Exists", () => {

    function createLibraryHelper() {
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        const input = screen.getByPlaceholderText(/Ex: My Collection/i);
        fireEvent.change(input, { target: { value: "My Books" } });
        const buttons = screen.getAllByRole("button", { name: /Create Library/i });
        fireEvent.click(buttons[buttons.length - 1]);
    }

    test("shows library name after creation", () => {
        renderLibrary();
        createLibraryHelper();
        expect(screen.getByText("My Books")).toBeInTheDocument();
    });

    test("shows publication count after creation", () => {
        renderLibrary();
        createLibraryHelper();
        expect(screen.getByText(/0 publications/i)).toBeInTheDocument();
    });

    test("shows Manage Publications button", () => {
        renderLibrary();
        createLibraryHelper();
        expect(
            screen.getByRole("button", { name: /Manage Publications/i })
        ).toBeInTheDocument();
    });

    test("does not show delete button", () => {
        renderLibrary();
        createLibraryHelper();
        expect(
            screen.queryByRole("button", { name: /^Delete$/i })
        ).not.toBeInTheDocument();
    });

});

// ── Filters ───────────────────────────────────────────────────────────────────

describe("Library — Filters", () => {

    function createLibraryHelper() {
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        const input = screen.getByPlaceholderText(/Ex: My Collection/i);
        fireEvent.change(input, { target: { value: "My Books" } });
        const buttons = screen.getAllByRole("button", { name: /Create Library/i });
        fireEvent.click(buttons[buttons.length - 1]);
    }

    test("shows no publications message when library is empty", () => {
        renderLibrary();
        createLibraryHelper();
        expect(
            screen.getByText(/No publications in library/i)
        ).toBeInTheDocument();
    });

    test("shows Add Publications button when library is empty", () => {
        renderLibrary();
        createLibraryHelper();
        expect(
            screen.getByRole("button", { name: /Add Publications/i })
        ).toBeInTheDocument();
    });

});

// ── Manage Publications Modal ─────────────────────────────────────────────────

describe("Library — Manage Publications Modal", () => {

    function createLibraryHelper() {
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        const input = screen.getByPlaceholderText(/Ex: My Collection/i);
        fireEvent.change(input, { target: { value: "My Books" } });
        const buttons = screen.getAllByRole("button", { name: /Create Library/i });
        fireEvent.click(buttons[buttons.length - 1]);
    }

    test("clicking Manage Publications opens modal", () => {
        renderLibrary();
        createLibraryHelper();
        fireEvent.click(
            screen.getByRole("button", { name: /Manage Publications/i })
        );
        expect(
            screen.getByText("Add Publications to Library")
        ).toBeInTheDocument();
    });

    test("modal shows no books message when publications context is empty", () => {
        renderLibrary();
        createLibraryHelper();
        fireEvent.click(
            screen.getByRole("button", { name: /Manage Publications/i })
        );
        expect(screen.getByText(/No books found/i)).toBeInTheDocument();
    });

    test("modal shows Add First Publication button when empty", () => {
        renderLibrary();
        createLibraryHelper();
        fireEvent.click(
            screen.getByRole("button", { name: /Manage Publications/i })
        );
        expect(
            screen.getByRole("button", { name: /Add First Publication/i })
        ).toBeInTheDocument();
    });

    test("modal does not show Remove button", () => {
        renderLibrary();
        createLibraryHelper();
        fireEvent.click(
            screen.getByRole("button", { name: /Manage Publications/i })
        );
        expect(
            screen.queryByRole("button", { name: /Remove/i })
        ).not.toBeInTheDocument();
    });

});

// ── View Modes ────────────────────────────────────────────────────────────────

describe("Library — View Modes", () => {

    function createLibraryHelper() {
        fireEvent.click(screen.getByRole("button", { name: /Create Library/i }));
        const input = screen.getByPlaceholderText(/Ex: My Collection/i);
        fireEvent.change(input, { target: { value: "My Books" } });
        const buttons = screen.getAllByRole("button", { name: /Create Library/i });
        fireEvent.click(buttons[buttons.length - 1]);
    }

    test("shows no publications message in default view", () => {
        renderLibrary();
        createLibraryHelper();
        expect(
            screen.getByText(/No publications in library/i)
        ).toBeInTheDocument();
    });

    test("shows Add Publications button in default view", () => {
        renderLibrary();
        createLibraryHelper();
        expect(
            screen.getByRole("button", { name: /Add Publications/i })
        ).toBeInTheDocument();
    });

});

// ── Navigation ────────────────────────────────────────────────────────────────

describe("Library — Navigation", () => {

    test("renders without crashing", () => {
        renderLibrary();
        expect(document.body).toBeTruthy();
    });

    test("shows the library page content", () => {
        renderLibrary();
        const hasNoLibrary = screen.queryByText("No Library Created");
        const hasDashboardLink = screen.queryByText(/My Dashboard/i);
        expect(hasNoLibrary || hasDashboardLink).toBeTruthy();
    });

});