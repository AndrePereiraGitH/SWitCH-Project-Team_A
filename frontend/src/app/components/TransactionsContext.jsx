import { createContext, useContext } from "react";
import { useLinks } from "./LinksContext";


const TransactionsContext = createContext(undefined);

const CURRENT_USER_ID = "user-001";

async function handleResponse(res, action) {
    if (!res.ok) {
        const body = await res.text();
        throw new Error(`${action} failed: ${res.status}${body ? ` - ${body}` : ""}`);
    }

    if (res.status === 204) return null;

    return res.json();
}

export function TransactionsProvider({ children }) {
    const { resolve, expandTemplate } = useLinks();

    async function createTransaction(payload) {
        const url = expandTemplate(resolve("create-transaction"), {
            requesterId: CURRENT_USER_ID,
        });

        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        return handleResponse(res, "Create transaction");
    }

    async function getMyTransactions() {
        const url = expandTemplate(resolve("my-transactions"), {
            requesterId: CURRENT_USER_ID,
        });

        const res = await fetch(url);

        return handleResponse(res, "Get transactions");
    }

    async function getListingById(listingId) {
        const template = resolve("find-by-id", (href) =>
            href.includes("/listings/")
        );

        const url = expandTemplate(template, {
            id: listingId,
        });

        const res = await fetch(url);

        return handleResponse(res, "Get listing");
    }

    async function markListingAsSold(listingId) {
        const url = expandTemplate(resolve("buy-listing"), {
            listingId,
            buyerId: CURRENT_USER_ID,
        });

        const res = await fetch(url, {
            method: "PATCH",
        });

        return handleResponse(res, "Mark listing as sold");
    }

    async function getCities() {
        const res = await fetch(resolve("cities"));

        return handleResponse(res, "Get cities");
    }

    return (
        <TransactionsContext.Provider
            value={{
                createTransaction,
                getMyTransactions,
                getListingById,
                markListingAsSold,
                getCities,
            }}
        >
            {children}
        </TransactionsContext.Provider>
    );
}

export function useTransactions() {
    const context = useContext(TransactionsContext);

    if (context === undefined) {
        throw new Error("useTransactions must be used within a TransactionsProvider");
    }

    return context;
}