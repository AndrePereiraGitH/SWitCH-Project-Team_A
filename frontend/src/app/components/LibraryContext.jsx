import { createContext, useContext, useEffect, useState } from "react";
import { useLinks } from "./LinksContext";

const LibraryContext = createContext(undefined);

const CURRENT_USER_ID = "user-001";

export function LibraryProvider({ children }) {
    const { links: rootLinks, loading: linksLoading, expandTemplate } = useLinks();

    const [library, setLibrary] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchLibrary = async () => {
        const link = rootLinks?.["get-library-by-user"]?.href;
        if (!link) {
            console.warn("[LibraryContext] no get-library-by-user link available");
            return;
        }

        setLoading(true);
        setError(null);
        try {
            const url = expandTemplate(link, { userId: CURRENT_USER_ID, requestingUserId: CURRENT_USER_ID });
            const res = await fetch(url);
            if (res.status === 404) {
                setLibrary(null);
                return;
            }
            if (!res.ok) throw new Error(`GET library failed: ${res.status}`);
            const data = await res.json();
            setLibrary(data);
        } catch (e) {
            console.error("[LibraryContext] fetch failed:", e);
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (!linksLoading) {
            fetchLibrary();
        }
    }, [linksLoading, rootLinks]);

    return (
        <LibraryContext.Provider value={{ library, loading, error, fetchLibrary }}>
            {children}
        </LibraryContext.Provider>
    );
}

export function useLibrary() {
    const ctx = useContext(LibraryContext);
    if (!ctx) throw new Error("useLibrary must be used within LibraryProvider");
    return ctx;
}