import { describe, test, expect } from "vitest";
import { screen, fireEvent, render, act } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "../components/AuthContext";
import { PublicationsProvider } from "../components/PublicationsContext";
import { LibraryProvider } from "../components/LibraryContext";
import { CartProvider } from "../components/CartContext";
import { ListsProvider, useLists } from "../components/ListsContext";
import { ListDetail } from "./ListDetail";

// ── Custom render without BrowserRouter ───────────────────────────────────────

function AllProvidersNoRouter({ children }) {
    return (
        <AuthProvider>
            <PublicationsProvider>
                <LibraryProvider>
                    <CartProvider>
                        <ListsProvider>
                            {children}
                        </ListsProvider>
                    </CartProvider>
                </LibraryProvider>
            </PublicationsProvider>
        </AuthProvider>
    );
}

// ── Wrapper that creates a list then renders ListDetail ───────────────────────

function TestApp() {
    const { addList, lists } = useLists();

    if (lists.length === 0) {
        return (
            <button
                onClick={() =>
                    addList({ name: "My Fiction List", genre: "Fiction", isPublic: false })
                }
            >
                init
            </button>
        );
    }

    const listId = lists[0].id;

    return (
        <MemoryRouter initialEntries={[`/lists/${listId}`]}>
            <Routes>
                <Route path="/lists/:listId" element={<ListDetail />} />
            </Routes>
        </MemoryRouter>
    );
}

function renderWithRealList() {
    render(
        <AllProvidersNoRouter>
            <TestApp />
        </AllProvidersNoRouter>
    );
    act(() => {
        screen.getByText("init").click();
    });
}

function renderNotFound() {
    render(
        <AllProvidersNoRouter>
            <MemoryRouter initialEntries={["/lists/nonexistent"]}>
                <Routes>
                    <Route path="/lists/:listId" element={<ListDetail />} />
                </Routes>
            </MemoryRouter>
        </AllProvidersNoRouter>
    );
}

// ── List not found ────────────────────────────────────────────────────────────

describe("ListDetail — List Not Found", () => {

    test("shows not found message when list does not exist", () => {
        renderNotFound();
        expect(screen.getByText("List not found")).toBeInTheDocument();
    });

});

// ── Rendering ─────────────────────────────────────────────────────────────────

describe("ListDetail — Rendering", () => {

    test("renders the list name", () => {
        renderWithRealList();
        expect(screen.getByText("My Fiction List")).toBeInTheDocument();
    });

    test("renders the list genre", () => {
        renderWithRealList();
        expect(screen.getByText("Fiction")).toBeInTheDocument();
    });

    test("renders publication count", () => {
        renderWithRealList();
        expect(screen.getByText(/0 publications/i)).toBeInTheDocument();
    });

    test("renders the Manage Lists back button", () => {
        renderWithRealList();
        expect(screen.getByText("Manage Lists")).toBeInTheDocument();
    });

    test("renders the Add Publication button", () => {
        renderWithRealList();
        const buttons = screen.getAllByRole("button", { name: /Add Publication/i });
        expect(buttons.length).toBeGreaterThan(0);
    });

    test("does not show Public badge for private list", () => {
        renderWithRealList();
        expect(screen.queryByText("Public")).not.toBeInTheDocument();
    });

});

// ── Empty list ────────────────────────────────────────────────────────────────

describe("ListDetail — Empty List", () => {

    test("shows no publications message when list is empty", () => {
        renderWithRealList();
        expect(
            screen.getByText("No publications in this list")
        ).toBeInTheDocument();
    });

    test("shows Add Publications button when list is empty", () => {
        renderWithRealList();
        expect(
            screen.getByRole("button", { name: /Add Publications/i })
        ).toBeInTheDocument();
    });

});

// ── Manage Publications Modal ─────────────────────────────────────────────────

describe("ListDetail — Manage Publications Modal", () => {

    test("clicking Add Publication opens the modal", () => {
        renderWithRealList();
        fireEvent.click(
            screen.getByRole("button", { name: /^Add Publication$/ })
        );
        expect(
            screen.getByRole("heading", { name: "Add Publication" })
        ).toBeInTheDocument();
    });

    test("modal shows no publications message when context is empty", () => {
        renderWithRealList();
        fireEvent.click(
            screen.getByRole("button", { name: /^Add Publication$/ })
        );
        expect(screen.getByText("No publications found")).toBeInTheDocument();
    });

    test("modal shows Add First Publication button when empty", () => {
        renderWithRealList();
        fireEvent.click(
            screen.getByRole("button", { name: /^Add Publication$/ })
        );
        expect(
            screen.getByRole("button", { name: /Add First Publication/i })
        ).toBeInTheDocument();
    });

    test("clicking Add Publications from empty state opens modal", () => {
        renderWithRealList();
        fireEvent.click(
            screen.getByRole("button", { name: /Add Publications/i })
        );
        expect(
            screen.getByRole("heading", { name: "Add Publication" })
        ).toBeInTheDocument();
    });

});

// ── No remove button ──────────────────────────────────────────────────────────

describe("ListDetail — No Remove Functionality", () => {

    test("does not show remove button on publications", () => {
        renderWithRealList();
        expect(
            screen.queryByRole("button", { name: /remove/i })
        ).not.toBeInTheDocument();
    });

    test("does not show X button", () => {
        renderWithRealList();
        expect(
            screen.queryByTitle("Remove from list")
        ).not.toBeInTheDocument();
    });

});

// ── Navigation ────────────────────────────────────────────────────────────────

describe("ListDetail — Navigation", () => {

    test("renders without crashing", () => {
        renderWithRealList();
        expect(document.body).toBeTruthy();
    });

    test("renders Manage Lists back link", () => {
        renderWithRealList();
        expect(screen.getByText("Manage Lists")).toBeInTheDocument();
    });

});