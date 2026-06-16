import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, fireEvent, waitFor } from "@testing-library/react";
import { renderWithProviders } from "../../test-utils";
import { Cart } from "./Cart";
import { useCart } from "../components/CartContext";

// ── Mocks ─────────────────────────────────────────────────────────────────────

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const mockAddPurchase = vi.fn();
vi.mock("../components/PurchasesContext", () => ({
    usePurchases: () => ({ addPurchase: mockAddPurchase }),
    PurchasesProvider: ({ children }) => children,
}));

beforeEach(() => {
    vi.clearAllMocks();
});

// ── Helpers ───────────────────────────────────────────────────────────────────

function renderCart() {
    return renderWithProviders(<Cart />);
}

function CartWithItem() {
    const { addToCart } = useCart();
    return (
        <div>
            <button onClick={() => addToCart({ id: "1", title: "Book A", price: 10 })}>
                Add Item
            </button>
            <Cart />
        </div>
    );
}

async function renderCartWithItem() {
    renderWithProviders(<CartWithItem />);
    fireEvent.click(screen.getByText("Add Item"));
}

async function proceedToShipping() {
    fireEvent.click(screen.getByText("Proceed to Checkout"));
    await waitFor(() => {
        expect(screen.getAllByText("Shipping Address").length).toBeGreaterThan(0);
    });
}

async function fillShipping() {
    fireEvent.change(screen.getByPlaceholderText("e.g. Main Street"), { target: { value: "Rua de Cedofeita" } });
    fireEvent.change(screen.getByPlaceholderText("e.g. 42"), { target: { value: "123" } });
    fireEvent.change(screen.getByPlaceholderText("e.g. 1000-001"), { target: { value: "4050-180" } });
    fireEvent.change(screen.getByPlaceholderText("e.g. Lisbon"), { target: { value: "Porto" } });
}

async function proceedToPayment() {
    await fillShipping();
    fireEvent.click(screen.getByText("Next: Payment"));
    await waitFor(() => {
        expect(screen.getAllByText("Payment Details").length).toBeGreaterThan(0);
    });
}

async function fillPayment() {
    fireEvent.change(screen.getByPlaceholderText("e.g. John Smith"), { target: { value: "David Silva" } });
    fireEvent.change(screen.getByPlaceholderText("1234 5678 9012 3456"), { target: { value: "1234567890123456" } });
    fireEvent.change(screen.getByPlaceholderText("MM (e.g. 09)"), { target: { value: "09" } });
    fireEvent.change(screen.getByPlaceholderText("YYYY (e.g. 2027)"), { target: { value: "2027" } });
}

// ── Empty State ───────────────────────────────────────────────────────────────

describe("Cart — Empty State", () => {

    test("shows empty cart message when no items", () => {
        renderCart();
        expect(screen.getByText("Your Cart is Empty")).toBeInTheDocument();
    });

    test("shows Browse Marketplace button when empty", () => {
        renderCart();
        expect(screen.getByText("Browse Marketplace")).toBeInTheDocument();
    });

    test("shows 0 items in cart header", () => {
        renderCart();
        expect(screen.getByText("0 items in cart")).toBeInTheDocument();
    });

    test("does not show Order Summary when cart is empty", () => {
        renderCart();
        expect(screen.queryByText("Order Summary")).not.toBeInTheDocument();
    });

    test("checkout modal is not visible initially", () => {
        renderCart();
        expect(screen.queryByText("Shipping Address")).not.toBeInTheDocument();
    });

    test("shows Shopping Cart heading", () => {
        renderCart();
        expect(screen.getByText("Shopping Cart")).toBeInTheDocument();
    });

});

// ── With Items ────────────────────────────────────────────────────────────────

describe("Cart — With Items", () => {

    test("shows item title when item is added", async () => {
        await renderCartWithItem();
        expect(screen.getByText("Book A")).toBeInTheDocument();
    });

    test("shows item price when item is added", async () => {
        await renderCartWithItem();
        expect(screen.getAllByText("€10.00").length).toBeGreaterThan(0);
    });

    test("shows 1 item in cart header after adding", async () => {
        await renderCartWithItem();
        expect(screen.getByText("1 item in cart")).toBeInTheDocument();
    });

    test("shows Order Summary after adding item", async () => {
        await renderCartWithItem();
        expect(screen.getByText("Order Summary")).toBeInTheDocument();
    });

    test("shows Proceed to Checkout button", async () => {
        await renderCartWithItem();
        expect(screen.getByText("Proceed to Checkout")).toBeInTheDocument();
    });

    test("removes item when trash button is clicked", async () => {
        await renderCartWithItem();
        fireEvent.click(screen.getByRole("button", { name: /remove/i }));
        expect(screen.queryByText("Book A")).not.toBeInTheDocument();
    });

});

// ── Shipping Step ─────────────────────────────────────────────────────────────

describe("Cart — Shipping Step", () => {

    test("shows shipping form after clicking Proceed to Checkout", async () => {
        await renderCartWithItem();
        await proceedToShipping();
        expect(screen.getAllByText("Shipping Address").length).toBeGreaterThan(0);
    });

    test("shows all shipping form fields", async () => {
        await renderCartWithItem();
        await proceedToShipping();
        expect(screen.getByPlaceholderText("e.g. Main Street")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("e.g. 42")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("e.g. 1000-001")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("e.g. Lisbon")).toBeInTheDocument();
    });

    test("shows validation errors when shipping fields are empty", async () => {
        await renderCartWithItem();
        await proceedToShipping();
        fireEvent.click(screen.getByText("Next: Payment"));
        expect(screen.getByText("Street is required")).toBeInTheDocument();
        expect(screen.getByText("Number is required")).toBeInTheDocument();
        expect(screen.getByText("Zip code is required")).toBeInTheDocument();
        expect(screen.getByText("City is required")).toBeInTheDocument();
    });

    test("does not proceed to payment when shipping is invalid", async () => {
        await renderCartWithItem();
        await proceedToShipping();
        fireEvent.click(screen.getByText("Next: Payment"));
        expect(screen.queryByRole("heading", { name: /Payment Details/i })).not.toBeInTheDocument();
    });

    test("proceeds to payment when shipping is valid", async () => {
        await renderCartWithItem();
        await proceedToShipping();
        await proceedToPayment();
        expect(screen.getAllByText("Payment Details").length).toBeGreaterThan(0);
    });

    test("Cancel button closes the modal", async () => {
        await renderCartWithItem();
        await proceedToShipping();
        fireEvent.click(screen.getByText("Cancel"));
        expect(screen.queryByText("Shipping Address")).not.toBeInTheDocument();
    });

    test("shows progress bar with Shipping Address and Payment Details tabs", async () => {
        await renderCartWithItem();
        await proceedToShipping();
        expect(screen.getAllByText("Shipping Address").length).toBeGreaterThan(0);
        expect(screen.getAllByText("Payment Details").length).toBeGreaterThan(0);
    });

});

// ── Payment Step ──────────────────────────────────────────────────────────────

describe("Cart — Payment Step", () => {

    async function renderAtPayment() {
        await renderCartWithItem();
        await proceedToShipping();
        await proceedToPayment();
    }

    test("shows all payment form fields", async () => {
        await renderAtPayment();
        expect(screen.getByPlaceholderText("e.g. John Smith")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("1234 5678 9012 3456")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("MM (e.g. 09)")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("YYYY (e.g. 2027)")).toBeInTheDocument();
    });

    test("shows validation errors when payment fields are empty", async () => {
        await renderAtPayment();
        fireEvent.click(screen.getByText("Confirm Purchase"));
        expect(screen.getByText("Card holder name is required")).toBeInTheDocument();
        expect(screen.getByText("Enter a valid 16-digit card number")).toBeInTheDocument();
        expect(screen.getByText("Enter a valid month (01–12)")).toBeInTheDocument();
        expect(screen.getByText("Enter a valid 4-digit year")).toBeInTheDocument();
    });

    test("shows error for invalid expiry month above 12", async () => {
        await renderAtPayment();
        fireEvent.change(screen.getByPlaceholderText("MM (e.g. 09)"), { target: { value: "13" } });
        fireEvent.click(screen.getByText("Confirm Purchase"));
        expect(screen.getByText("Enter a valid month (01–12)")).toBeInTheDocument();
    });

    test("shows error for card number with fewer than 16 digits", async () => {
        await renderAtPayment();
        fireEvent.change(screen.getByPlaceholderText("1234 5678 9012 3456"), { target: { value: "123" } });
        fireEvent.click(screen.getByText("Confirm Purchase"));
        expect(screen.getByText("Enter a valid 16-digit card number")).toBeInTheDocument();
    });

    test("Back button returns to shipping step", async () => {
        await renderAtPayment();
        fireEvent.click(screen.getAllByText("Back")[1]);
        expect(screen.getAllByText("Shipping Address").length).toBeGreaterThan(0);
    });

    test("shows order total recap in payment step", async () => {
        await renderAtPayment();
        expect(screen.getByText("Order Total")).toBeInTheDocument();
    });

    test("does not proceed to success when payment is invalid", async () => {
        await renderAtPayment();
        fireEvent.click(screen.getByText("Confirm Purchase"));
        expect(screen.queryByText("Purchase Successful!")).not.toBeInTheDocument();
    });

});

// ── Success Step ──────────────────────────────────────────────────────────────

describe("Cart — Success Step", () => {

    async function renderAtSuccess() {
        await renderCartWithItem();
        await proceedToShipping();
        await proceedToPayment();
        await fillPayment();
        fireEvent.click(screen.getByText("Confirm Purchase"));
        await waitFor(() => {
            expect(screen.getByText("Purchase Successful!")).toBeInTheDocument();
        });
    }

    test("shows success message after confirming purchase", async () => {
        await renderAtSuccess();
        expect(screen.getByText("Purchase Successful!")).toBeInTheDocument();
    });

    test("shows order placed confirmation text", async () => {
        await renderAtSuccess();
        expect(screen.getByText("Your order has been placed successfully.")).toBeInTheDocument();
    });

    test("shows View My Purchases button", async () => {
        await renderAtSuccess();
        expect(screen.getByText("View My Purchases")).toBeInTheDocument();
    });

    test("calls addPurchase on confirm", async () => {
        await renderAtSuccess();
        expect(mockAddPurchase).toHaveBeenCalledTimes(1);
    });

    test("View My Purchases navigates to /my-purchases", async () => {
        await renderAtSuccess();
        fireEvent.click(screen.getByText("View My Purchases"));
        expect(mockNavigate).toHaveBeenCalledWith("/my-purchases");
    });

    test("shows shipping address in success recap", async () => {
        await renderAtSuccess();
        expect(screen.getByText(/Rua de Cedofeita/i)).toBeInTheDocument();
    });

    test("shows masked card in success recap", async () => {
        await renderAtSuccess();
        expect(screen.getByText(/\*{4} \*{4} \*{4} 3456/)).toBeInTheDocument();
    });

    test("shows total charged in success recap", async () => {
        await renderAtSuccess();
        expect(screen.getByText(/0\.00/)).toBeInTheDocument();
    });

});