const BASE_URL = "http://localhost:8081";

export const CURRENT_USER_ID = "user-001";

export async function createTransaction(payload) {

    const res = await fetch(
        `${BASE_URL}/transactions?requesterId=${CURRENT_USER_ID}`,
        {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        }
    );

    if (!res.ok) {
        const body = await res.text();
        throw new Error(`Create transaction failed: ${res.status} - ${body}`);
    }
    return res.json();
}

export async function getMyTransactions() {
    const res = await fetch(
        `${BASE_URL}/transactions/buyer/${CURRENT_USER_ID}?requesterId=${CURRENT_USER_ID}`
    );

    if (!res.ok) throw new Error(`Get transactions failed: ${res.status}`);
    return res.json();
}

export async function getListingById(listingId) {
    const res = await fetch(`${BASE_URL}/listings/${listingId}`);

    if (!res.ok) throw new Error(`Get listing failed: ${res.status}`);

    return res.json();
}

export async function getCities() {
    const res = await fetch(`${BASE_URL}/cities`);

    if (!res.ok) throw new Error(`Get cities failed: ${res.status}`);

    return res.json();
}

export async function markListingAsSold(listingId) {
    const res = await fetch(
        `${BASE_URL}/listings/${listingId}/sold?buyerId=${CURRENT_USER_ID}`,
        { method: "PATCH" }
    );

    if (!res.ok) {
        const body = await res.text();
        throw new Error(`Mark listing as sold failed: ${res.status} - ${body}`);
    }
    return res.json();
}