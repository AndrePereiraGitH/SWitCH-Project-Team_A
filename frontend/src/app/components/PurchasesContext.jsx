import { createContext, useContext, useState } from "react";

const PurchasesContext = createContext(undefined);

export function PurchasesProvider({ children }) {
    const [purchases, setPurchases] = useState([]);
    const [soldItemIds, setSoldItemIds] = useState(new Set());

    const addPurchase = (items, shipping, maskedCard) => {
        const record = {
            orderId: `ORD-${Date.now()}`,
            date: new Date(),
            items,
            total: items.reduce((s, i) => s + i.price, 0),
            shipping,
            maskedCard,
        };

        setPurchases(prev => [record, ...prev]);
        setSoldItemIds(prev => {
            const next = new Set(prev);
            items.forEach(i => next.add(i.id));
            return next;
        });
    };

    return (
        <PurchasesContext.Provider value={{ purchases, soldItemIds, addPurchase }}>
            {children}
        </PurchasesContext.Provider>
    );
}

export function usePurchases() {
    const ctx = useContext(PurchasesContext);
    if (!ctx) throw new Error("usePurchases must be used within PurchasesProvider");
    return ctx;
}
