import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { TransactionsProvider, useTransactions } from "./TransactionsContext";

const mockResolve = vi.hoisted(() => vi.fn());
const mockExpandTemplate = vi.hoisted(() => vi.fn());

vi.mock("./LinksContext", () => ({
    useLinks: () => ({
        resolve: mockResolve,
        expandTemplate: mockExpandTemplate,
    }),
}));

function TestComponent() {
    const {
        createTransaction,
        getMyTransactions,
        getListingById,
        markListingAsSold,
        getCities,
    } = useTransactions();

    return (
        <div>
            <button
                onClick={() =>
                    createTransaction({
                        buyerId: "user-001",
                        price: "10.00",
                        transactionDate: "2026-06-18T14:50:00",
                        street: "Rua",
                        number: "1",
                        zipCode: "4000-000",
                        cityId: "Lisbon",
                        cardHolderName: "David Silva",
                        maskedCardNumber: "**** **** **** 3456",
                        expiryMonth: "09",
                        expiryYear: "2027",
                        listingIds: ["listing-001"],
                    })
                }
            >
                Create Transaction
            </button>

            <button onClick={() => getMyTransactions()}>
                Get Transactions
            </button>

            <button onClick={() => getListingById("listing-001")}>
                Get Listing
            </button>

            <button onClick={() => markListingAsSold("listing-001")}>
                Mark Sold
            </button>

            <button onClick={() => getCities()}>
                Get Cities
            </button>
        </div>
    );
}

function renderWithTransactionsProvider() {
    return render(
        <TransactionsProvider>
            <TestComponent />
        </TransactionsProvider>
    );
}

beforeEach(() => {
    vi.clearAllMocks();

    global.fetch = vi.fn().mockResolvedValue({
        ok: true,
        status: 200,
        json: async () => ({}),
        text: async () => "",
    });

    mockResolve.mockImplementation((rel, matcher) => {
        const links = {
            "create-transaction": "http://localhost:8081/transactions?requesterId={requesterId}",
            "my-transactions": "http://localhost:8081/transactions/buyer/user-001?requesterId={requesterId}",
            "buy-listing": "http://localhost:8081/listings/{listingId}/sold?buyerId={buyerId}",
            "cities": "http://localhost:8081/cities",
        };

        if (rel === "find-by-id") {
            const candidates = [
                "http://localhost:8081/appraisal-entities/{appraisalEntityId}",
                "http://localhost:8081/collection-of-publications/{collectionId}",
                "http://localhost:8081/listings/{id}",
                "http://localhost:8081/publicationSamples/{publicationSampleId}",
            ];

            return matcher ? candidates.find(matcher) : candidates[0];
        }

        return links[rel];
    });

    mockExpandTemplate.mockImplementation((template, params = {}) => {
        let url = template;

        Object.entries(params).forEach(([key, value]) => {
            url = url.replace(`{${key}}`, encodeURIComponent(value));
        });

        return url;
    });
});

describe("TransactionsContext", () => {
    test("createTransaction posts transaction payload using HATEOAS link", async () => {
        renderWithTransactionsProvider();

        fireEvent.click(screen.getByText("Create Transaction"));

        await waitFor(() => {
            expect(mockResolve).toHaveBeenCalledWith("create-transaction");
            expect(mockExpandTemplate).toHaveBeenCalledWith(
                "http://localhost:8081/transactions?requesterId={requesterId}",
                { requesterId: "user-001" }
            );

            expect(fetch).toHaveBeenCalledWith(
                "http://localhost:8081/transactions?requesterId=user-001",
                expect.objectContaining({
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        buyerId: "user-001",
                        price: "10.00",
                        transactionDate: "2026-06-18T14:50:00",
                        street: "Rua",
                        number: "1",
                        zipCode: "4000-000",
                        cityId: "Lisbon",
                        cardHolderName: "David Silva",
                        maskedCardNumber: "**** **** **** 3456",
                        expiryMonth: "09",
                        expiryYear: "2027",
                        listingIds: ["listing-001"],
                    }),
                })
            );
        });
    });

    test("getMyTransactions fetches buyer transactions using HATEOAS link", async () => {
        renderWithTransactionsProvider();

        fireEvent.click(screen.getByText("Get Transactions"));

        await waitFor(() => {
            expect(mockResolve).toHaveBeenCalledWith("my-transactions");
            expect(fetch).toHaveBeenCalledWith(
                "http://localhost:8081/transactions/buyer/user-001?requesterId=user-001"
            );
        });
    });

    test("getListingById fetches listing details using HATEOAS link", async () => {
        renderWithTransactionsProvider();

        fireEvent.click(screen.getByText("Get Listing"));

        await waitFor(() => {
            expect(mockResolve).toHaveBeenCalledWith(
                "find-by-id",
                expect.any(Function)
            );
            expect(fetch).toHaveBeenCalledWith(
                "http://localhost:8081/listings/listing-001"
            );
        });
    });

    test("markListingAsSold patches listing sold endpoint using HATEOAS link", async () => {
        renderWithTransactionsProvider();

        fireEvent.click(screen.getByText("Mark Sold"));

        await waitFor(() => {
            expect(mockResolve).toHaveBeenCalledWith("buy-listing");
            expect(fetch).toHaveBeenCalledWith(
                "http://localhost:8081/listings/listing-001/sold?buyerId=user-001",
                expect.objectContaining({
                    method: "PATCH",
                })
            );
        });
    });

    test("getCities fetches cities using HATEOAS link", async () => {
        renderWithTransactionsProvider();

        fireEvent.click(screen.getByText("Get Cities"));

        await waitFor(() => {
            expect(mockResolve).toHaveBeenCalledWith("cities");
            expect(fetch).toHaveBeenCalledWith("http://localhost:8081/cities");
        });
    });

    test("throws error with response body when request fails", async () => {
        global.fetch = vi.fn().mockResolvedValue({
            ok: false,
            status: 400,
            text: async () => "Invalid transaction",
        });

        function ErrorTestComponent() {
            const { getCities } = useTransactions();

            return (
                <button
                    onClick={async () => {
                        try {
                            await getCities();
                        } catch (e) {
                            document.body.setAttribute("data-error", e.message);
                        }
                    }}
                >
                    Trigger Error
                </button>
            );
        }

        render(
            <TransactionsProvider>
                <ErrorTestComponent />
            </TransactionsProvider>
        );

        fireEvent.click(screen.getByText("Trigger Error"));

        await waitFor(() => {
            expect(document.body.getAttribute("data-error")).toBe(
                "Get cities failed: 400 - Invalid transaction"
            );
        });
    });

    test("useTransactions throws outside TransactionsProvider", () => {
        function BrokenComponent() {
            useTransactions();
            return <div>Broken</div>;
        }

        const spy = vi.spyOn(console, "error").mockImplementation(() => {});

        expect(() => render(<BrokenComponent />)).toThrow(
            "useTransactions must be used within a TransactionsProvider"
        );

        spy.mockRestore();
    });
});