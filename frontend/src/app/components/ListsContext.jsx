import { createContext, useContext, useEffect, useState } from "react";
import { useLinks } from "./LinksContext";

const ListsContext = createContext(undefined);

/**
 * Hardcoded ID of the currently authenticated user.
 * Replace with a proper auth context once authentication is implemented.
 * @type {string}
 */
const CURRENT_USER_ID = "user-001";

/**
 * Expands a URI template by replacing `{paramName}` placeholders with their
 * corresponding URL-encoded values.
 *
 * @example
 * expandUri("http://localhost:8081/collections/{collectionId}?userId={userId}",
 *           { collectionId: "42", userId: "user-001" });
 * // → "http://localhost:8081/collections/42?userId=user-001"
 *
 * @param {string} href   - The URI template containing `{key}` placeholders.
 * @param {Record<string, string>} params - Map of placeholder names to values.
 * @returns {string} The fully expanded URL.
 */
function expandUri(href, params) {
    let url = href;
    Object.entries(params).forEach(([key, value]) => {
        url = url.replace(`{${key}}`, encodeURIComponent(value));
    });
    return url;
}

/**
 * Context provider that manages the current user's collection lists.
 *
 * Responsibilities:
 * - Fetches the user's lists from the backend on mount (once HATEOAS root links
 *   are available from {@link LinksContext}).
 * - Exposes CRUD-like operations: create a list, change its visibility, and
 *   assign a publication to it.
 * - All mutations rely on HATEOAS links resolved at runtime; missing links
 *   cause the operation to throw rather than silently fail.
 *
 * @param {{ children: React.ReactNode }} props
 * @returns {JSX.Element}
 */
export function ListsProvider({ children }) {
    const { links: rootLinks, loading: linksLoading } = useLinks();

    /** @type {[Array<object>, Function]} The user's collection lists. */
    const [lists, setLists] = useState([]);

    /** @type {[boolean, Function]} True while the lists fetch is in-flight. */
    const [loading, setLoading] = useState(false);

    /** @type {[string|null, Function]} Error message from the last failed fetch, or null. */
    const [error, setError] = useState(null);

    /**
     * Fetches the current user's lists from the backend using the
     * `find-own-collections` HATEOAS link.
     * Logs a warning and exits early if the link is not yet available.
     *
     * @returns {Promise<void>}
     */
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

    // Re-fetch whenever the root links finish loading or change (e.g. after login).
    useEffect(() => {
        if (!linksLoading) {
            fetchLists();
        }
    }, [linksLoading, rootLinks]);

    /**
     * Creates a new collection list for the current user.
     * Appends the created list to local state on success.
     *
     * @param {{ collectionName: string, genreId: string }} param0
     * @returns {Promise<object>} The newly created list resource returned by the API.
     * @throws {Error} If the HATEOAS link is missing or the request fails.
     */
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

    /**
     * Makes a collection publicly visible to all users.
     * Updates the matching entry in local state with the response from the API.
     *
     * @param {string} collectionId - ID of the collection to make public.
     * @returns {Promise<object>} The updated list resource.
     * @throws {Error} If the HATEOAS link is missing or the request fails.
     */
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

    /**
     * Makes a collection private (visible only to its owner).
     * Updates the matching entry in local state with the response from the API.
     *
     * @param {string} collectionId - ID of the collection to make private.
     * @returns {Promise<object>} The updated list resource.
     * @throws {Error} If the HATEOAS link is missing or the request fails.
     */
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

    /**
     * Makes a collection temporarily public until the specified date.
     * Updates the matching entry in local state with the response from the API.
     *
     * @param {string} collectionId - ID of the collection to update.
     * @param {string} publicUntil  - ISO-8601 date string (e.g. `"2026-12-31"`).
     * @returns {Promise<object>} The updated list resource.
     * @throws {Error} If the HATEOAS link is missing or the request fails.
     */
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

    /**
     * Assigns a publication sample to a collection via a PATCH request.
     * Does **not** update local list state directly — the caller is responsible
     * for refreshing the publications context if needed.
     *
     * @param {string} collectionId        - ID of the target collection.
     * @param {string} publicationSampleId - ID of the publication sample to assign.
     * @returns {Promise<object>} The API response body.
     * @throws {Error} If the HATEOAS link is missing or the request fails.
     */
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

    /**
     * Retrieves a single list from local state by its collection ID.
     *
     * @param {string} collectionId - The ID to look up.
     * @returns {object|undefined} The matching list, or `undefined` if not found.
     */
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

/**
 * Custom hook to access the {@link ListsContext}.
 * Must be called from a component tree wrapped in {@link ListsProvider}.
 *
 * @returns {{
 *   lists: Array<object>,
 *   loading: boolean,
 *   error: string|null,
 *   addList: Function,
 *   makePublic: Function,
 *   makePrivate: Function,
 *   makePublicUntil: Function,
 *   addPublicationToList: Function,
 *   getListById: Function,
 *   fetchLists: Function,
 * }}
 * @throws {Error} If called outside of a {@link ListsProvider}.
 */
export function useLists() {
    const ctx = useContext(ListsContext);
    if (!ctx) throw new Error("useLists must be used within ListsProvider");
    return ctx;
}