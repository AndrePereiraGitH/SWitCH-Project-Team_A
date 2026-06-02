import { describe, test, expect } from "vitest";
import { render, screen, fireEvent, renderWithProviders } from "../../test-utils";
import { CartProvider, useCart } from "./CartContext";

// Helper component that exposes cart context state to the screen
function TestCart() {
    const { items, itemCount, addToCart, removeFromCart } = useCart();
    return (
        <div>
            <p data-testid="count">{itemCount}</p>
            <ul>
                {items.map(item => (
                    <li key={item.id} data-testid={`item-${item.id}`}>{item.title}</li>
                ))}
            </ul>
            <button onClick={() => addToCart({ id: "1", title: "Book A", price: 10 })}>
                Add Book A
            </button>
            <button onClick={() => addToCart({ id: "2", title: "Book B", price: 20 })}>
                Add Book B
            </button>
            <button onClick={() => removeFromCart("1")}>
                Remove Book A
            </button>
        </div>
    );
}

// renderWithProviders already includes CartProvider
function renderCart() {
    return renderWithProviders(<TestCart />);
}

// ── Initial state ──────────────────────────────────────────────────────────────

describe("CartContext — Initial State", () => {

    test("cart starts empty", () => {
        renderCart();
        expect(screen.getByTestId("count").textContent).toBe("0");
    });

    test("no items are rendered by default", () => {
        renderCart();
        expect(screen.queryByTestId("item-1")).not.toBeInTheDocument();
    });

});

// ── Add items ──────────────────────────────────────────────────────────────────

describe("CartContext — Add Items", () => {

    test("adding one item increments counter to 1", () => {
        renderCart();
        fireEvent.click(screen.getByText("Add Book A"));
        expect(screen.getByTestId("count").textContent).toBe("1");
    });

    test("added item appears in the list", () => {
        renderCart();
        fireEvent.click(screen.getByText("Add Book A"));
        expect(screen.getByTestId("item-1")).toBeInTheDocument();
        expect(screen.getByText("Book A")).toBeInTheDocument();
    });

    test("adding two items increments counter to 2", () => {
        renderCart();
        fireEvent.click(screen.getByText("Add Book A"));
        fireEvent.click(screen.getByText("Add Book B"));
        expect(screen.getByTestId("count").textContent).toBe("2");
    });

});

// ── Remove items ───────────────────────────────────────────────────────────────

describe("CartContext — Remove Items", () => {

    test("removing an existing item decrements the counter", () => {
        renderCart();
        fireEvent.click(screen.getByText("Add Book A"));
        fireEvent.click(screen.getByText("Remove Book A"));
        expect(screen.getByTestId("count").textContent).toBe("0");
    });

    test("removed item disappears from the list", () => {
        renderCart();
        fireEvent.click(screen.getByText("Add Book A"));
        fireEvent.click(screen.getByText("Remove Book A"));
        expect(screen.queryByTestId("item-1")).not.toBeInTheDocument();
    });

    test("removing one item keeps the remaining ones", () => {
        renderCart();
        fireEvent.click(screen.getByText("Add Book A"));
        fireEvent.click(screen.getByText("Add Book B"));
        fireEvent.click(screen.getByText("Remove Book A"));
        expect(screen.getByTestId("item-2")).toBeInTheDocument();
        expect(screen.getByTestId("count").textContent).toBe("1");
    });

});

// ── Error outside provider ─────────────────────────────────────────────────────

describe("CartContext — Error Handling", () => {

    test("useCart throws when used outside CartProvider", () => {
        const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

        function ComponentWithoutProvider() {
            useCart();
            return null;
        }

        // render directly (no providers) to test the missing context error
        expect(() => render(<ComponentWithoutProvider />)).toThrow(
            "useCart must be used within CartProvider"
        );

        consoleSpy.mockRestore();
    });

});
