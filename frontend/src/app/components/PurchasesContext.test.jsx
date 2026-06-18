import { describe, test, expect, vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { PurchasesProvider, usePurchases } from "./PurchasesContext";

// ── Helper components ──────────────────────────────────────────────────────────

function TestPurchases() {
    const { purchases, soldItemIds, addPurchase } = usePurchases();

    const mockItems = [
        { id: "1", title: "Book A", price: 10 },
        { id: "2", title: "Book B", price: 20 },
    ];

    const mockShipping = {
        street: "Rua de Cedofeita",
        number: "123",
        zipCode: "4050-180",
        city: "Porto",
    };

    return (
        <div>
            <p data-testid="purchase-count">{purchases.length}</p>
            <p data-testid="sold-count">{soldItemIds.size}</p>
            <p data-testid="sold-1">{soldItemIds.has("1") ? "sold" : "available"}</p>
            <p data-testid="sold-2">{soldItemIds.has("2") ? "sold" : "available"}</p>
            <button onClick={() => addPurchase(mockItems, mockShipping, "**** **** **** 1234")}>
                Buy
            </button>
        </div>
    );
}

function renderPurchases() {
    return render(
        <PurchasesProvider>
            <TestPurchases />
        </PurchasesProvider>
    );
}

// ── Initial state ──────────────────────────────────────────────────────────────

describe("PurchasesContext — Initial State", () => {

    test("starts with no purchases", () => {
        renderPurchases();
        expect(screen.getByTestId("purchase-count").textContent).toBe("0");
    });

    test("starts with no sold items", () => {
        renderPurchases();
        expect(screen.getByTestId("sold-count").textContent).toBe("0");
    });

    test("items start as available", () => {
        renderPurchases();
        expect(screen.getByTestId("sold-1").textContent).toBe("available");
        expect(screen.getByTestId("sold-2").textContent).toBe("available");
    });

});

// ── Add purchase ───────────────────────────────────────────────────────────────

describe("PurchasesContext — Add Purchase", () => {

    test("adding a purchase increments purchase count", () => {
        renderPurchases();
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("purchase-count").textContent).toBe("1");
    });

    test("purchased item IDs are added to soldItemIds", () => {
        renderPurchases();
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("sold-count").textContent).toBe("2");
    });

    test("item 1 is marked as sold after purchase", () => {
        renderPurchases();
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("sold-1").textContent).toBe("sold");
    });

    test("item 2 is marked as sold after purchase", () => {
        renderPurchases();
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("sold-2").textContent).toBe("sold");
    });

    test("adding two purchases increments count to 2", () => {
        renderPurchases();
        fireEvent.click(screen.getByText("Buy"));
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("purchase-count").textContent).toBe("2");
    });

    test("soldItemIds does not grow on duplicate purchase", () => {
        renderPurchases();
        fireEvent.click(screen.getByText("Buy"));
        fireEvent.click(screen.getByText("Buy"));
        // Set deduplicates — still only 2 unique IDs
        expect(screen.getByTestId("sold-count").textContent).toBe("2");
    });

});

// ── Purchase record structure ──────────────────────────────────────────────────

describe("PurchasesContext — Purchase Record", () => {

    function TestRecord() {
        const { purchases, addPurchase } = usePurchases();

        const mockItems = [{ id: "1", title: "Book A", price: 10 }];
        const mockShipping = { street: "Rua A", number: "1", zipCode: "1000-001", city: "Lisboa" };

        const record = purchases[0];

        return (
            <div>
                <button onClick={() => addPurchase(mockItems, mockShipping, "**** **** **** 4242")}>
                    Buy
                </button>
                {record && (
                    <>
                        <p data-testid="order-id">{record.orderId}</p>
                        <p data-testid="total">{record.total}</p>
                        <p data-testid="masked-card">{record.maskedCard}</p>
                        <p data-testid="city">{record.shipping.city}</p>
                        <p data-testid="item-title">{record.items[0].title}</p>
                    </>
                )}
            </div>
        );
    }

    function renderRecord() {
        return render(
            <PurchasesProvider>
                <TestRecord />
            </PurchasesProvider>
        );
    }

    test("purchase record has an orderId starting with ORD-", () => {
        renderRecord();
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("order-id").textContent).toMatch(/^ORD-/);
    });

    test("purchase record calculates total correctly", () => {
        renderRecord();
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("total").textContent).toBe("10");
    });

    test("purchase record stores masked card", () => {
        renderRecord();
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("masked-card").textContent).toBe("**** **** **** 4242");
    });

    test("purchase record stores shipping city", () => {
        renderRecord();
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("city").textContent).toBe("Lisboa");
    });

    test("purchase record stores item title", () => {
        renderRecord();
        fireEvent.click(screen.getByText("Buy"));
        expect(screen.getByTestId("item-title").textContent).toBe("Book A");
    });

});

// ── Error outside provider ─────────────────────────────────────────────────────

describe("PurchasesContext — Error Handling", () => {

    test("usePurchases throws when used outside PurchasesProvider", () => {
        const consoleSpy = vi.spyOn(console, "error").mockImplementation(() => {});

        function ComponentWithoutProvider() {
            usePurchases();
            return null;
        }

        expect(() => render(<ComponentWithoutProvider />)).toThrow(
            "usePurchases must be used within PurchasesProvider"
        );

        consoleSpy.mockRestore();
    });

});