import { describe, test, expect, vi, beforeEach } from "vitest";
import { createTransaction, getMyTransactions } from "./transactions";

beforeEach(() => {
    vi.restoreAllMocks();
});

describe("transactions api", () => {
    test("createTransaction posts to backend", async () => {
        const payload = {
            buyerId: "user-001",
            price: "10",
            transactionDate: "2026-01-01T10:00:00.000Z",
            street: "Rua",
            number: "1",
            zipCode: "4000-000",
            cityId: "Porto",
            cardHolderName: "David Silva",
            maskedCardNumber: "**** **** **** 3456",
            expiryMonth: "09",
            expiryYear: "2027",
            listingIds: ["listing-123"],
        };

        vi.stubGlobal("fetch", vi.fn().mockResolvedValue({
            ok: true,
            json: async () => ({ transactionId: "transaction-001" }),
        }));

        const result = await createTransaction(payload);

        expect(fetch).toHaveBeenCalledWith(
            "http://localhost:8081/transactions?requesterId=user-001",
            expect.objectContaining({
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload),
            })
        );

        expect(result).toEqual({ transactionId: "transaction-001" });
    });

    test("getMyTransactions fetches buyer transactions", async () => {
        vi.stubGlobal("fetch", vi.fn().mockResolvedValue({
            ok: true,
            json: async () => [],
        }));

        const result = await getMyTransactions();

        expect(fetch).toHaveBeenCalledWith(
            "http://localhost:8081/transactions/buyer/user-001?requesterId=user-001"
        );

        expect(result).toEqual([]);
    });
});