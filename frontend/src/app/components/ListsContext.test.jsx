import { describe, test, expect } from "vitest";
import { render, screen, fireEvent, renderWithProviders } from "../../test-utils";
import { ListsProvider, useLists } from "./ListsContext";

// Helper component that exposes lists context state to the screen
function TestLists() {
    const { lists, addList, updateList, deleteList, addPublicationToList, removePublicationFromList, isPublicationInList, getListById } = useLists();
    const first = lists[0];
    return (
        <div>
            <p data-testid="count">{lists.length}</p>
            {first && (
                <>
                    <span data-testid="first-name">{first.name}</span>
                    <span data-testid="first-shared">{first.shared ? "shared" : "private"}</span>
                    <span data-testid="first-pub-count">{first.publicationIds.length}</span>
                    <span data-testid="first-has-pub-1">{isPublicationInList(first.id, "pub-1") ? "yes" : "no"}</span>
                    <button onClick={() => updateList(first.id, { name: "Updated List" })}>Update First</button>
                    <button onClick={() => deleteList(first.id)}>Delete First</button>
                    <button onClick={() => addPublicationToList(first.id, "pub-1")}>Add Pub to First</button>
                    <button onClick={() => removePublicationFromList(first.id, "pub-1")}>Remove Pub from First</button>
                </>
            )}
            <button onClick={() => addList({ name: "My List", genre: "Fiction", shared: false })}>
                Add List
            </button>
            <button onClick={() => addList({ name: "Second List", genre: "Science", shared: true })}>
                Add Second List
            </button>
        </div>
    );
}

function renderLists() {
    return renderWithProviders(<TestLists />);
}

// ── Initial State ──────────────────────────────────────────────────────────────

describe("ListsContext — Initial State", () => {

    test("lists start empty", () => {
        renderLists();
        expect(screen.getByTestId("count").textContent).toBe("0");
    });

});

// ── Add List ───────────────────────────────────────────────────────────────────

describe("ListsContext — Add List", () => {

    test("adding a list increments count to 1", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        expect(screen.getByTestId("count").textContent).toBe("1");
    });

    test("added list appears with the correct name", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        expect(screen.getByTestId("first-name").textContent).toBe("My List");
    });

    test("new list starts as private", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        expect(screen.getByTestId("first-shared").textContent).toBe("private");
    });

    test("new list starts with no publications", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        expect(screen.getByTestId("first-pub-count").textContent).toBe("0");
    });

    test("adding two lists increments count to 2", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        fireEvent.click(screen.getByText("Add Second List"));
        expect(screen.getByTestId("count").textContent).toBe("2");
    });

});

// ── Update List ────────────────────────────────────────────────────────────────

describe("ListsContext — Update List", () => {

    test("updating a list changes its name", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        fireEvent.click(screen.getByText("Update First"));
        expect(screen.getByTestId("first-name").textContent).toBe("Updated List");
    });

});

// ── Delete List ────────────────────────────────────────────────────────────────

describe("ListsContext — Delete List", () => {

    test("deleting a list decrements the count", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        fireEvent.click(screen.getByText("Delete First"));
        expect(screen.getByTestId("count").textContent).toBe("0");
    });

    test("deleted list no longer appears", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        fireEvent.click(screen.getByText("Delete First"));
        expect(screen.queryByTestId("first-name")).not.toBeInTheDocument();
    });

});

// ── Publications in List ───────────────────────────────────────────────────────

describe("ListsContext — Publications in List", () => {

    test("adding a publication to a list increments its publication count", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        fireEvent.click(screen.getByText("Add Pub to First"));
        expect(screen.getByTestId("first-pub-count").textContent).toBe("1");
    });

    test("isPublicationInList returns true after adding", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        fireEvent.click(screen.getByText("Add Pub to First"));
        expect(screen.getByTestId("first-has-pub-1").textContent).toBe("yes");
    });

    test("adding the same publication twice does not duplicate it", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        fireEvent.click(screen.getByText("Add Pub to First"));
        fireEvent.click(screen.getByText("Add Pub to First"));
        expect(screen.getByTestId("first-pub-count").textContent).toBe("1");
    });

    test("removing a publication decrements the count", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        fireEvent.click(screen.getByText("Add Pub to First"));
        fireEvent.click(screen.getByText("Remove Pub from First"));
        expect(screen.getByTestId("first-pub-count").textContent).toBe("0");
    });

    test("isPublicationInList returns false after removing", () => {
        renderLists();
        fireEvent.click(screen.getByText("Add List"));
        fireEvent.click(screen.getByText("Add Pub to First"));
        fireEvent.click(screen.getByText("Remove Pub from First"));
        expect(screen.getByTestId("first-has-pub-1").textContent).toBe("no");
    });

});

// ── Error Handling ─────────────────────────────────────────────────────────────

describe("ListsContext — Error Handling", () => {

    test("useLists throws when used outside ListsProvider", () => {
        const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

        function ComponentWithoutProvider() {
            useLists();
            return null;
        }

        // render directly (no providers) to test the missing context error
        expect(() => render(<ComponentWithoutProvider />)).toThrow(
            "useLists must be used within ListsProvider"
        );

        consoleSpy.mockRestore();
    });

});
