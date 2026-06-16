import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, fireEvent } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { MyPurchases } from "./MyPurchases";

// ── Mock data ─────────────────────────────────────────────────────────────────

const mockPurchases = [
    {
        orderId: "ORD-001",
        date: new Date("2026-06-01T10:00:00"),
        items: [
            { id: "1", title: "The Da Vinci Code", price: 12.5 },
            { id: "2", title: "1984", price: 15.0 },
        ],
        total: 27.5,
        shipping: { street: "Rua de Cedofeita", number: "123", zipCode: "4050-180", city: "Porto" },
        maskedCard: "**** **** **** 1234",
    },
    {
        orderId: "ORD-002",
        date: new Date("2026-06-05T14:30:00"),
        items: [
            { id: "3", title: "Harry Potter", price: 18.0 },
        ],
        total: 18.0,
        shipping: { street: "Avenida da Liberdade", number: "45", zipCode: "1250-096", city: "Lisboa" },
        maskedCard: "**** **** **** 5678",
    },
];

// ── Mocks ─────────────────────────────────────────────────────────────────────

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return { ...actual, useNavigate: () => mockNavigate };
});

let mockPurchasesValue = { purchases: [] };
vi.mock("../components/PurchasesContext", () => ({
    usePurchases: () => mockPurchasesValue,
    PurchasesProvider: ({ children }) => children,
    PurchasesContext: { Provider: ({ children }) => children },
}));

beforeEach(() => {
    mockPurchasesValue = { purchases: [] };
    vi.clearAllMocks();
});

// ── Helpers ───────────────────────────────────────────────────────────────────

function renderEmpty() {
    return renderWithProviders(<MyPurchases />);
}

function renderWithPurchases(purchases = mockPurchases) {
    mockPurchasesValue = { purchases, soldItemIds: new Set(), addPurchase: vi.fn() };
    return renderWithProviders(<MyPurchases />);
}

// ── Empty State ───────────────────────────────────────────────────────────────

describe("MyPurchases — Empty State", () => {

    test("shows No Purchases Yet heading", () => {
        renderEmpty();
        expect(screen.getByText("No Purchases Yet")).toBeInTheDocument();
    });

    test("shows 0 orders placed", () => {
        renderEmpty();
        expect(screen.getByText("0 orders placed")).toBeInTheDocument();
    });

    test("shows Browse Marketplace button", () => {
        renderEmpty();
        expect(screen.getByText("Browse Marketplace")).toBeInTheDocument();
    });

    test("Browse Marketplace navigates to /explore", () => {
        renderEmpty();
        fireEvent.click(screen.getByText("Browse Marketplace"));
        expect(mockNavigate).toHaveBeenCalledWith("/explore");
    });

    test("shows My Purchases heading", () => {
        renderEmpty();
        expect(screen.getByText("My Purchases")).toBeInTheDocument();
    });

});

// ── With Purchases ────────────────────────────────────────────────────────────

describe("MyPurchases — With Purchases", () => {

    test("shows correct order count", () => {
        renderWithPurchases();
        expect(screen.getByText("2 orders placed")).toBeInTheDocument();
    });

    test("shows order ID", () => {
        renderWithPurchases();
        expect(screen.getByText("ORD-001")).toBeInTheDocument();
    });

    test("shows item titles", () => {
        renderWithPurchases();
        expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
        expect(screen.getByText("1984")).toBeInTheDocument();
    });

    test("shows shipping address", () => {
        renderWithPurchases();
        expect(screen.getByText(/Rua de Cedofeita/i)).toBeInTheDocument();
    });

    test("shows masked card", () => {
        renderWithPurchases();
        expect(screen.getByText("**** **** **** 1234")).toBeInTheDocument();
    });

    test("shows order total", () => {
        renderWithPurchases();
        const totals = screen.getAllByText("€27.50");
        expect(totals.length).toBeGreaterThan(0);
    });

    test("shows Purchased badge", () => {
        renderWithPurchases();
        const badges = screen.getAllByText("Purchased");
        expect(badges.length).toBeGreaterThan(0);
    });

    test("shows multiple orders", () => {
        renderWithPurchases();
        expect(screen.getByText("ORD-001")).toBeInTheDocument();
        expect(screen.getByText("ORD-002")).toBeInTheDocument();
    });

    test("shows Harry Potter in second order", () => {
        renderWithPurchases();
        expect(screen.getByText("Harry Potter")).toBeInTheDocument();
    });

    test("does not show empty state when purchases exist", () => {
        renderWithPurchases();
        expect(screen.queryByText("No Purchases Yet")).not.toBeInTheDocument();
    });

});

// ── Navigation ────────────────────────────────────────────────────────────────

describe("MyPurchases — Navigation", () => {

    test("Back button calls navigate(-1)", () => {
        renderEmpty();
        fireEvent.click(screen.getByText("Back"));
        expect(mockNavigate).toHaveBeenCalledWith(-1);
    });

});

// ── Singular vs Plural ────────────────────────────────────────────────────────

describe("MyPurchases — Order Count Text", () => {

    test("shows singular order when count is 1", () => {
        renderWithPurchases([mockPurchases[0]]);
        expect(screen.getByText("1 order placed")).toBeInTheDocument();
    });

    test("shows plural orders when count is 2", () => {
        renderWithPurchases(mockPurchases);
        expect(screen.getByText("2 orders placed")).toBeInTheDocument();
    });

});