import { describe, test, expect, vi, beforeEach } from "vitest";
import { screen, fireEvent, waitFor } from "@testing-library/react";
import { renderWithProviders } from "@/test-utils.jsx";
import { MyPurchases } from "./MyPurchases";

const mockGetMyTransactions = vi.hoisted(() => vi.fn());
const mockGetListingById = vi.hoisted(() => vi.fn());

vi.mock("@/api/transactions.js", () => ({
    getMyTransactions: mockGetMyTransactions,
    getListingById: mockGetListingById,
}));

const mockNavigate = vi.fn();

vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return { ...actual, useNavigate: () => mockNavigate };
});

const mockTransactions = [
    {
        transactionId: "ORD-001",
        buyerId: "user-001",
        price: "27.5",
        transactionDate: "2026-06-01T10:00:00",
        street: "Rua de Cedofeita",
        number: "123",
        zipCode: "4050-180",
        cityId: "Porto",
        cardHolderName: "David Silva",
        maskedCardNumber: "**** **** **** 1234",
        expiryMonth: "09",
        expiryYear: "2027",
        status: "COMPLETED",
        listingIds: ["listing-001", "listing-002"],
    },
    {
        transactionId: "ORD-002",
        buyerId: "user-001",
        price: "18.0",
        transactionDate: "2026-06-05T14:30:00",
        street: "Avenida da Liberdade",
        number: "45",
        zipCode: "1250-096",
        cityId: "Lisboa",
        cardHolderName: "David Silva",
        maskedCardNumber: "**** **** **** 5678",
        expiryMonth: "09",
        expiryYear: "2027",
        status: "COMPLETED",
        listingIds: ["listing-003"],
    },
];

const mockListings = {
    "listing-001": {
        id: "listing-001",
        listingTitle: "The Da Vinci Code",
        price: "12.50",
    },
    "listing-002": {
        id: "listing-002",
        listingTitle: "1984",
        price: "15.00",
    },
    "listing-003": {
        id: "listing-003",
        listingTitle: "Harry Potter",
        price: "18.00",
    },
};

beforeEach(() => {
    vi.clearAllMocks();

    mockGetMyTransactions.mockResolvedValue([]);

    mockGetListingById.mockImplementation(async (id) => {
        return mockListings[id];
    });
});

function renderEmpty() {
    mockGetMyTransactions.mockResolvedValue([]);
    return renderWithProviders(<MyPurchases />);
}

function renderWithPurchases(transactions = mockTransactions) {
    mockGetMyTransactions.mockResolvedValue(transactions);
    return renderWithProviders(<MyPurchases />);
}

describe("MyPurchases — Empty State", () => {
    test("shows No Purchases Yet heading", async () => {
        renderEmpty();

        await waitFor(() => {
            expect(screen.getByText("No Purchases Yet")).toBeInTheDocument();
        });
    });

    test("shows 0 orders placed", async () => {
        renderEmpty();

        await waitFor(() => {
            expect(screen.getByText("0 orders placed")).toBeInTheDocument();
        });
    });

    test("shows Browse Marketplace button", async () => {
        renderEmpty();

        await waitFor(() => {
            expect(screen.getByText("Browse Marketplace")).toBeInTheDocument();
        });
    });

    test("Browse Marketplace navigates to /explore", async () => {
        renderEmpty();

        await waitFor(() => {
            expect(screen.getByText("Browse Marketplace")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Browse Marketplace"));

        expect(mockNavigate).toHaveBeenCalledWith("/explore");
    });

    test("shows My Purchases heading", async () => {
        renderEmpty();

        await waitFor(() => {
            expect(screen.getByText("My Purchases")).toBeInTheDocument();
        });
    });
});

describe("MyPurchases — With Purchases", () => {
    test("loads purchases from backend transactions API", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(mockGetMyTransactions).toHaveBeenCalledTimes(1);
        });
    });

    test("loads listing details for purchased listing ids", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(mockGetListingById).toHaveBeenCalledWith("listing-001");
            expect(mockGetListingById).toHaveBeenCalledWith("listing-002");
            expect(mockGetListingById).toHaveBeenCalledWith("listing-003");
        });
    });

    test("shows correct order count", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.getByText("2 orders placed")).toBeInTheDocument();
        });
    });

    test("shows order ID", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.getByText("ORD-001")).toBeInTheDocument();
        });
    });

    test("shows item titles", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.getByText("The Da Vinci Code")).toBeInTheDocument();
            expect(screen.getByText("1984")).toBeInTheDocument();
        });
    });

    test("shows shipping address", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.getByText(/Rua de Cedofeita/i)).toBeInTheDocument();
        });
    });

    test("shows masked card", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.getByText("**** **** **** 1234")).toBeInTheDocument();
        });
    });

    test("shows order total", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.getAllByText("€27.50").length).toBeGreaterThan(0);
        });
    });

    test("shows Purchased badge", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.getAllByText("Purchased").length).toBeGreaterThan(0);
        });
    });

    test("shows multiple orders", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.getByText("ORD-001")).toBeInTheDocument();
            expect(screen.getByText("ORD-002")).toBeInTheDocument();
        });
    });

    test("shows Harry Potter in second order", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.getByText("Harry Potter")).toBeInTheDocument();
        });
    });

    test("does not show empty state when purchases exist", async () => {
        renderWithPurchases();

        await waitFor(() => {
            expect(screen.queryByText("No Purchases Yet")).not.toBeInTheDocument();
        });
    });
});

describe("MyPurchases — Navigation", () => {
    test("Back button calls navigate(-1)", async () => {
        renderEmpty();

        await waitFor(() => {
            expect(screen.getByText("Back")).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText("Back"));

        expect(mockNavigate).toHaveBeenCalledWith(-1);
    });
});

describe("MyPurchases — Order Count Text", () => {
    test("shows singular order when count is 1", async () => {
        renderWithPurchases([mockTransactions[0]]);

        await waitFor(() => {
            expect(screen.getByText("1 order placed")).toBeInTheDocument();
        });
    });

    test("shows plural orders when count is 2", async () => {
        renderWithPurchases(mockTransactions);

        await waitFor(() => {
            expect(screen.getByText("2 orders placed")).toBeInTheDocument();
        });
    });
});