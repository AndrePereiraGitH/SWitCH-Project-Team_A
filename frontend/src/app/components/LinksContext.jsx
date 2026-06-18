import { createContext, useContext, useEffect, useState } from "react";

const LinksContext = createContext(undefined);

const BASE_URL = "http://localhost:8081";
const CURRENT_USER_ID = "user-001";

export function LinksProvider({ children }) {
    const [links, setLinks] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchRoot = async () => {
            try {
                const res = await fetch(`${BASE_URL}/?userId=${CURRENT_USER_ID}`);
                if (!res.ok) throw new Error(`GET root failed: ${res.status}`);
                const data = await res.json();
                console.log("[LinksContext] root links:", data._links);
                setLinks(data._links ?? {});
            } catch (e) {
                console.error("[LinksContext] fetch failed:", e);
                setError(e.message);
            } finally {
                setLoading(false);
            }
        };
        fetchRoot();
    }, []);

    return (
        <LinksContext.Provider value={{ links, loading, error }}>
            {children}
        </LinksContext.Provider>
    );
}

export function useLinks() {
    const ctx = useContext(LinksContext);
    if (!ctx) throw new Error("useLinks must be used within LinksProvider");
    return ctx;
}