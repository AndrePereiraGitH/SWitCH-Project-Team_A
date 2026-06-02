import { describe, test, expect } from "vitest";
import { render, screen, fireEvent, renderWithProviders } from "../../test-utils";
import { LibraryProvider, useLibrary } from "./LibraryContext";

// Helper component that exposes library context state to the screen
function TestLibrary() {
    const { library, createLibrary, deleteLibrary, addPublicationToLibrary, removePublicationFromLibrary, isPublicationInLibrary } = useLibrary();
    return (
        <div>
            <p data-testid="exists">{library ? "exists" : "empty"}</p>
            <p data-testid="name">{library ? library.name : ""}</p>
            <p data-testid="pub-count">{library ? library.publicationIds.length : 0}</p>
            <p data-testid="has-pub-1">{isPublicationInLibrary("pub-1") ? "yes" : "no"}</p>
            <button onClick={() => createLibrary("My Library")}>Create Library</button>
            <button onClick={() => createLibrary("Second Library")}>Create Second Library</button>
            <button onClick={deleteLibrary}>Delete Library</button>
            <button onClick={() => addPublicationToLibrary("pub-1")}>Add Pub 1</button>
            <button onClick={() => addPublicationToLibrary("pub-2")}>Add Pub 2</button>
            <button onClick={() => removePublicationFromLibrary("pub-1")}>Remove Pub 1</button>
        </div>
    );
}

function renderLibraryContext() {
    return renderWithProviders(<TestLibrary />);
}

// ── Initial State ──────────────────────────────────────────────────────────────

describe("LibraryContext — Initial State", () => {

    test("library does not exist by default", () => {
        renderLibraryContext();
        expect(screen.getByTestId("exists").textContent).toBe("empty");
    });

    test("publication count is 0 by default", () => {
        renderLibraryContext();
        expect(screen.getByTestId("pub-count").textContent).toBe("0");
    });

    test("isPublicationInLibrary returns false when library does not exist", () => {
        renderLibraryContext();
        expect(screen.getByTestId("has-pub-1").textContent).toBe("no");
    });

});

// ── Create Library ─────────────────────────────────────────────────────────────

describe("LibraryContext — Create Library", () => {

    test("creating a library makes it exist", () => {
        renderLibraryContext();
        fireEvent.click(screen.getByText("Create Library"));
        expect(screen.getByTestId("exists").textContent).toBe("exists");
    });

    test("created library has the correct name", () => {
        renderLibraryContext();
        fireEvent.click(screen.getByText("Create Library"));
        expect(screen.getByTestId("name").textContent).toBe("My Library");
    });

    test("creating a library twice does not overwrite the first one", () => {
        renderLibraryContext();
        fireEvent.click(screen.getByText("Create Library"));
        fireEvent.click(screen.getByText("Create Second Library"));
        // First library should remain unchanged
        expect(screen.getByTestId("name").textContent).toBe("My Library");
    });

});


// ── Publications in Library ────────────────────────────────────────────────────

describe("LibraryContext — Publications in Library", () => {

    test("adding a publication increments the count", () => {
        renderLibraryContext();
        fireEvent.click(screen.getByText("Create Library"));
        fireEvent.click(screen.getByText("Add Pub 1"));
        expect(screen.getByTestId("pub-count").textContent).toBe("1");
    });

    test("isPublicationInLibrary returns true after adding", () => {
        renderLibraryContext();
        fireEvent.click(screen.getByText("Create Library"));
        fireEvent.click(screen.getByText("Add Pub 1"));
        expect(screen.getByTestId("has-pub-1").textContent).toBe("yes");
    });

    test("adding the same publication twice does not duplicate it", () => {
        renderLibraryContext();
        fireEvent.click(screen.getByText("Create Library"));
        fireEvent.click(screen.getByText("Add Pub 1"));
        fireEvent.click(screen.getByText("Add Pub 1"));
        expect(screen.getByTestId("pub-count").textContent).toBe("1");
    });

    test("adding two different publications increments count to 2", () => {
        renderLibraryContext();
        fireEvent.click(screen.getByText("Create Library"));
        fireEvent.click(screen.getByText("Add Pub 1"));
        fireEvent.click(screen.getByText("Add Pub 2"));
        expect(screen.getByTestId("pub-count").textContent).toBe("2");
    });

    test("adding a publication without a library does nothing", () => {
        renderLibraryContext();
        fireEvent.click(screen.getByText("Add Pub 1"));
        expect(screen.getByTestId("pub-count").textContent).toBe("0");
    });

});

// ── Error Handling ─────────────────────────────────────────────────────────────

describe("LibraryContext — Error Handling", () => {

    test("useLibrary throws when used outside LibraryProvider", () => {
        const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

        function ComponentWithoutProvider() {
            useLibrary();
            return null;
        }

        // render directly (no providers) to test the missing context error
        expect(() => render(<ComponentWithoutProvider />)).toThrow(
            "useLibrary must be used within a LibraryProvider"
        );

        consoleSpy.mockRestore();
    });

});
