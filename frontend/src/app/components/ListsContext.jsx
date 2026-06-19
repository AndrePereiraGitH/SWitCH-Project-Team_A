import { createContext, useContext, useEffect, useState } from "react";
import { useLinks } from "./LinksContext";

const ListsContext = createContext(undefined);

const CURRENT_USER_ID = "user-001";

// Helper: replace templated URI parts like {userId}
function expandUri(href, params) {
    let url = href;
    Object.entries(params).forEach(([key, value]) => {
        url = url.replace(`{${key}}`, encodeURIComponent(value));
    });
    return url;
}

export function ListsProvider({ children }) {
    const { links: rootLinks, loading: linksLoading } = useLinks();
    const [lists, setLists] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchLists = async () => {
        const link = rootLinks?.["find-own-collections"]?.href;
        if (!link) {
            console.warn("[ListsContext] no find-own-collections link available");
            return;
        }

        setLoading(true);
        setError(null);
        try {
            const url = expandUri(link, { userId: CURRENT_USER_ID });
            const res = await fetch(url);
            if (!res.ok) throw new Error(`GET lists failed: ${res.status}`);
            const data = await res.json();
            setLists(data);
        } catch (e) {
            console.error("[ListsContext] fetch failed:", e);
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (!linksLoading) {
            fetchLists();
        }
    }, [linksLoading, rootLinks]);

    const addList = async ({ collectionName, genreId }) => {
        const link = rootLinks?.["create-collection-of-publications"]?.href;
        if (!link) throw new Error("create-collection-of-publications link not available");

        const url = expandUri(link, { ownerId: CURRENT_USER_ID });
        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ collectionName, genreId }),
        });
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`Create list failed (${res.status}): ${body}`);
        }
        const created = await res.json();
        setLists(prev => [...prev, created]);
        return created;
    };

    const makePublic = async (collectionId) => {
        const link = rootLinks?.["make-collection-public"]?.href;
        if (!link) throw new Error("make-collection-public link not available");

        const url = expandUri(link, { collectionId, userId: CURRENT_USER_ID });
        const res = await fetch(url, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
        });
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`Make public failed (${res.status}): ${body}`);
        }
        const updated = await res.json();
        setLists(prev => prev.map(l => l.collectionId === collectionId ? updated : l));
        return updated;
    };

    const makePrivate = async (collectionId) => {
        const link = rootLinks?.["make-collection-private"]?.href;
        if (!link) throw new Error("make-collection-private link not available");

        const url = expandUri(link, { collectionId, userId: CURRENT_USER_ID });
        const res = await fetch(url, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
        });
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`Make private failed (${res.status}): ${body}`);
        }
        const updated = await res.json();
        setLists(prev => prev.map(l => l.collectionId === collectionId ? updated : l));
        return updated;
    };

    const makePublicUntil = async (collectionId, publicUntil) => {
        const link = rootLinks?.["make-collection-public-until"]?.href;
        if (!link) throw new Error("make-collection-public-until link not available");

        const url = expandUri(link, {
            collectionId,
            publicUntil,
            userId: CURRENT_USER_ID,
        });
        const res = await fetch(url, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
        });
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`Make public until failed (${res.status}): ${body}`);
        }
        const updated = await res.json();
        setLists(prev => prev.map(l => l.collectionId === collectionId ? updated : l));
        return updated;
    };

    const addPublicationToList = async (collectionId, publicationSampleId) => {
        const link = rootLinks?.["assign-to-collection"]?.href;
        if (!link) throw new Error("assign-to-collection link not available");

        const url = expandUri(link, {
            publicationSampleId,
            collectionId,
            userId: CURRENT_USER_ID,
        });
        const res = await fetch(url, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
        });
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`Add to list failed (${res.status}): ${body}`);
        }
        return res.json();
    };

    const getListById = (collectionId) => lists.find(l => l.collectionId === collectionId);

    return (
        <ListsContext.Provider value={{
            lists,
            loading,
            error,
            addList,
            makePublic,
            makePrivate,
            makePublicUntil,
            addPublicationToList,
            getListById,
            fetchLists,
        }}>
            {children}
        </ListsContext.Provider>
    );
}

export function useLists() {
    const ctx = useContext(ListsContext);
    if (!ctx) throw new Error("useLists must be used within ListsProvider");
    return ctx;
}