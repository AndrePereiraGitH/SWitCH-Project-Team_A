import { createContext, useContext, useEffect, useState } from "react";
import { useLinks } from "./LinksContext";

const LibraryContext = createContext(undefined);

/**
 * Hardcoded ID of the currently authenticated user.
 * Replace with a proper auth context once authentication is implemented.
 * @type {string}
 */
const CURRENT_USER_ID = "user-001";

/**
 * Context provider that fetches and exposes the current user's personal library.
 *
 * Responsibilities:
 * - Fetches the library from the backend on mount, once the HATEOAS root links
 *   are available from {@link LinksContext}.
 * - Treats a 404 response as "no library yet" (sets state to null) rather than
 *   an error, since a user may not have created a library yet.
 * - Any other non-OK response is treated as an error and stored in `error`.
 *
 * @param {{ children: React.ReactNode }} props
 * @returns {JSX.Element}
 */
export function LibraryProvider({ children }) {
    const { links: rootLinks, loading: linksLoading } = useLinks();

    /**
     * The user's library resource, or null if not yet loaded / not found.
     * @type {[object|null, Function]}
     */
    const [library, setLibrary] = useState(null);

    /** @type {[boolean, Function]} True while the library fetch is in-flight. */
    const [loading, setLoading] = useState(false);

    /** @type {[string|null, Function]} Error message from the last failed fetch, or null. */
    const [error, setError] = useState(null);

    /**
     * Fetches the current user's library from the backend using the
     * `get-library-by-user` HATEOAS link.
     *
     * Special cases:
     * - Logs a warning and exits early if the link is not yet available.
     * - A 404 response sets `library` to null without setting `error`,
     *   since the absence of a library is a valid state.
     *
     * @returns {Promise<void>}
     */
    const fetchLibrary = async () => {
        const link = rootLinks?.["get-library-by-user"]?.href;
        if (!link) {
            console.warn("[LibraryContext] no get-library-by-user link available");
            return;
        }

        setLoading(true);
        setError(null);
        try {
            const url = `${link}?requestingUserId=${CURRENT_USER_ID}`;
            const res = await fetch(url);
            if (res.status === 404) {
                // No library exists yet for this user — not an error condition.
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

    // Re-fetch whenever the root links finish loading or change (e.g. after login).
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

/**
 * Custom hook to access the {@link LibraryContext}.
 * Must be called from a component tree wrapped in {@link LibraryProvider}.
 *
 * @returns {{
 *   library: object|null,
 *   loading: boolean,
 *   error: string|null,
 *   fetchLibrary: Function,
 * }}
 * @throws {Error} If called outside of a {@link LibraryProvider}.
 */
export function useLibrary() {
    const ctx = useContext(LibraryContext);
    if (!ctx) throw new Error("useLibrary must be used within LibraryProvider");
    return ctx;
}