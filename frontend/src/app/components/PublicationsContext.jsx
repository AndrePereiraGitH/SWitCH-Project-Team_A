import { createContext, useContext, useEffect, useState } from "react";
import { useLinks } from "./LinksContext";

const PublicationsContext = createContext(undefined);

/**
 * Hardcoded ID of the currently authenticated user.
 * Replace with a proper auth context once authentication is implemented.
 * @type {string}
 */
const CURRENT_USER_ID = "user-001";

/**
 * Converts a SCREAMING_SNAKE_CASE condition string into a human-readable label.
 *
 * @example
 * prettifyCondition("LIKE_NEW"); // → "Like New"
 * prettifyCondition("GOOD");     // → "Good"
 * prettifyCondition(null);       // → ""
 *
 * @param {string|null|undefined} c - The raw condition value from the API.
 * @returns {string} The formatted label, or an empty string if the input is falsy.
 */
function prettifyCondition(c) {
    if (!c) return "";
    return c.split("_").map(w => w[0] + w.slice(1).toLowerCase()).join(" ");
}

/**
 * Expands a URI template by replacing `{paramName}` placeholders with their
 * corresponding URL-encoded values.
 *
 * @example
 * expandUri("http://localhost:8081/publicationSamples?userId={userId}", { userId: "user-001" });
 * // → "http://localhost:8081/publicationSamples?userId=user-001"
 *
 * @param {string} href - The URI template containing `{key}` placeholders.
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
 * Context provider that manages the current user's publication samples.
 *
 * Responsibilities:
 * - Fetches the user's in-library publications on mount, once HATEOAS root links
 *   are available from {@link LinksContext}.
 * - Enriches raw API responses: fills missing titles, joins author/genre arrays
 *   into display strings, and prettifies condition labels.
 * - Orchestrates the three-step publication creation flow:
 *   bibliographical data → edition → publication sample.
 * - Exposes mutations for adding to library and marking publications for sale.
 *
 * @param {{ children: React.ReactNode }} props
 * @returns {JSX.Element}
 */
export function PublicationsProvider({ children }) {
    const { links: rootLinks, loading: linksLoading } = useLinks();

    /** @type {[Array<object>, Function]} Enriched list of the user's publication samples. */
    const [publications, setPublications] = useState([]);

    /** @type {[boolean, Function]} True while a fetch or refresh is in-flight. */
    const [loading, setLoading] = useState(false);

    /** @type {[string|null, Function]} Error message from the last failed operation, or null. */
    const [error, setError] = useState(null);

    /**
     * Fetches the current user's in-library publications and updates local state.
     * Each publication is enriched with display-friendly fields before being stored.
     * Logs a warning and exits early if the `find-in-library` HATEOAS link is missing.
     *
     * @returns {Promise<void>}
     */
    const refresh = async () => {
        const link = rootLinks?.["find-in-library"]?.href;
        if (!link) {
            console.warn("[PublicationsContext] no find-in-library link available");
            return;
        }

        setLoading(true);
        setError(null);
        try {
            const url = expandUri(link, { userId: CURRENT_USER_ID });
            const res = await fetch(url);
            if (!res.ok) throw new Error(`GET publications failed: ${res.status}`);

            const pubs = await res.json();

            /**
             * Enriches a raw publication sample from the API with display-friendly fields.
             * @param {object} p - Raw publication sample.
             * @returns {object} Enriched publication sample.
             */
            const enrich = (p) => ({
                ...p,
                title: p.title ?? "(untitled)",
                author: p.authors?.join(", ") ?? "",
                genre:  p.genres?.join(", ")  ?? "",
                condition: prettifyCondition(p.condition),
            });

            setPublications(pubs.map(enrich));
        } catch (e) {
            console.error("[PublicationsContext] refresh failed:", e);
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    // Re-fetch whenever the root links finish loading or change (e.g. after login).
    useEffect(() => {
        if (!linksLoading) {
            refresh();
        }
    }, [linksLoading, rootLinks]);

    // ── Private helpers (orchestration steps) ────────────────────────

    /**
     * Step 1 of 3 in {@link addPublication}.
     * Creates a bibliographical data record (title, authors, genres, language, date).
     *
     * @param {{ title: string, authorNames: string[], genreNames: string[],
     *            originalLanguage: string, publicationDate: string,
     *            chunksOfText?: string[]|null }} param0
     * @returns {Promise<{ bdId: string }>} The created bibliographical data resource.
     * @throws {Error} If the HATEOAS link is missing or the request fails.
     */
    const createBibliographicalData = async ({
                                                 title,
                                                 authorNames,
                                                 genreNames,
                                                 originalLanguage,
                                                 publicationDate,
                                                 chunksOfText,
                                             }) => {
        const link = rootLinks?.["create-bibliographical-data"]?.href;
        if (!link) throw new Error("create-bibliographical-data link not available");

        const url = expandUri(link, { userId: CURRENT_USER_ID });
        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                title,
                authorName: authorNames,
                originalLanguage,
                publicationDate,
                genreName: genreNames,
                chunksOfText: chunksOfText ?? null,
            }),
        });
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`BD creation failed (${res.status}): ${body}`);
        }
        return res.json();
    };

    /**
     * Step 2 of 3 in {@link addPublication}.
     * Creates an edition record (ISBN, dimensions, binding, publisher, etc.).
     *
     * @param {object} editionPayload - The full edition payload as expected by the API.
     * @returns {Promise<{ editionId: string }>} The created edition resource.
     * @throws {Error} If the HATEOAS link is missing or the request fails.
     */
    const createEdition = async (editionPayload) => {
        const link = rootLinks?.["create-edition"]?.href;
        if (!link) throw new Error("create-edition link not available");

        const url = expandUri(link, { userId: CURRENT_USER_ID });
        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(editionPayload),
        });
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`Edition creation failed (${res.status}): ${body}`);
        }
        return res.json();
    };

    /**
     * Step 3 of 3 in {@link addPublication}.
     * Creates a publication sample linking a bibliographical data record and an edition.
     *
     * @param {{ bibliographicalDataId: string, editionId: string, condition: string }} param0
     * @returns {Promise<object>} The created publication sample resource.
     * @throws {Error} If the HATEOAS link is missing or the request fails.
     */
    const createPublicationSample = async ({
                                               bibliographicalDataId,
                                               editionId,
                                               condition,
                                           }) => {
        const link = rootLinks?.["create-publication-sample"]?.href;
        if (!link) throw new Error("create-publication-sample link not available");

        const url = expandUri(link, { userId: CURRENT_USER_ID });
        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                bibliographicalDataId,
                editionId,
                condition,
            }),
        });
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`PublicationSample failed (${res.status}): ${body}`);
        }
        return res.json();
    };

    /**
     * Adds an existing publication sample to the current user's library via PATCH.
     * On success, updates the matching entry in local state with the API response,
     * re-joining `authors` and `genres` arrays into display strings.
     *
     * @param {string} publicationSampleId - ID of the publication sample to add.
     * @returns {Promise<object>} The updated publication sample resource.
     * @throws {Error} If the HATEOAS link is missing or the request fails.
     */
    const addToLibrary = async (publicationSampleId) => {
        const link = rootLinks?.["add-to-library"]?.href;
        if (!link) throw new Error("add-to-library link not available");

        const url = expandUri(link, {
            publicationSampleId,
            userId: CURRENT_USER_ID,
        });
        const res = await fetch(url, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
        });
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`Add to library failed (${res.status}): ${body}`);
        }
        const updated = await res.json();
        console.log("[addToLibrary] response:", updated);
        setPublications(prev =>
            prev.map(p => p.id === publicationSampleId
                ? {
                    ...p,
                    ...updated,
                    author: updated.authors?.join(", ") ?? p.author,
                    genre:  updated.genres?.join(", ")  ?? p.genre,
                }
                : p
            )
        );
        return updated;
    };

    // ── Orchestration ─────────────────────────────────────────────────

    /**
     * Orchestrates the full three-step publication creation flow:
     * 1. Creates bibliographical data (title, authors, genres).
     * 2. Creates an edition (ISBN, dimensions, publisher, binding).
     * 3. Creates a publication sample (links BD + edition, sets condition).
     *
     * On success, appends an enriched version of the new sample to local state
     * so the UI updates without a full refresh.
     *
     * @param {object} form - Form values collected from the add-publication UI.
     * @param {string}   form.title
     * @param {string[]} form.authorNames           - Internal author ID strings sent to the API.
     * @param {string[]} form.genreNames            - Internal genre ID strings sent to the API.
     * @param {string[]} form.authorDisplayNames    - Human-readable author names for local state.
     * @param {string[]} form.genreDisplayNames     - Human-readable genre names for local state.
     * @param {string}   form.originalLanguage
     * @param {string}   form.publishedDate         - ISO-8601 date string.
     * @param {string}   form.editionNumber
     * @param {string}   [form.isbn10]
     * @param {string}   [form.isbn13]
     * @param {string}   [form.issn]
     * @param {number}   form.height
     * @param {number}   form.width
     * @param {number}   form.depth
     * @param {number}   form.weight
     * @param {number}   form.numberOfPages
     * @param {string}   form.publicationTypeId
     * @param {string}   form.publisherId
     * @param {string}   form.publisherName         - Display name used only in local state.
     * @param {string}   form.mediaFormat
     * @param {string}   form.editionLanguage
     * @param {string}   form.typeOfBinding
     * @param {string}   form.condition
     * @returns {Promise<object>} The enriched publication sample added to local state.
     * @throws {Error} If any of the three API steps fails.
     */
    const addPublication = async (form) => {
        console.log("[addPublication] starting orchestration with:", form);

        const bd = await createBibliographicalData({
            title: form.title,
            authorNames: form.authorNames,
            genreNames:  form.genreNames,
            originalLanguage: form.originalLanguage,
            publicationDate: form.publishedDate,
            chunksOfText: null,
        });
        console.log("[addPublication] BD created:", bd);

        const edition = await createEdition({
            editionNumber: form.editionNumber,
            isbn10: form.isbn10 || null,
            isbn13: form.isbn13 || null,
            issn:   form.issn   || null,
            height: form.height, width: form.width, depth: form.depth,
            weight: form.weight, numberOfPages: form.numberOfPages,
            publicationTypeId: form.publicationTypeId,
            publisherId: form.publisherId,
            mediaFormat: form.mediaFormat,
            editionLanguage: form.editionLanguage,
            editionDate: form.publishedDate,
            typeOfBinding: form.typeOfBinding,
        });
        console.log("[addPublication] Edition created:", edition);

        const sample = await createPublicationSample({
            bibliographicalDataId: bd.bdId,
            editionId: edition.editionId,
            condition: form.condition,
        });
        console.log("[addPublication] Sample created:", sample);

        // Build an enriched entry for immediate local state update, falling back
        // to form values when the API response omits display fields.
        const enriched = {
            ...sample,
            title:  sample.title  ?? form.title,
            author: sample.authors?.join(", ") ?? form.authorDisplayNames.join(", "),
            genre:  sample.genres?.join(", ")  ?? form.genreDisplayNames.join(", "),
            condition: prettifyCondition(sample.condition ?? form.condition),
            isbn: form.isbn10 || form.isbn13 || form.issn || "",
            type: form.mediaFormat.toLowerCase(),
            publisher: form.publisherName,
            inLibrary: false,
            forSale: false,
        };
        setPublications((prev) => [...prev, enriched]);
        return enriched;
    };

    /**
     * Marks a publication sample as for sale at the given price.
     * Updates local state only — no API call is made.
     *
     * @param {string} id    - ID of the publication sample to update.
     * @param {number} price - Asking price in the user's currency.
     */
    const setPublicationForSale = (id, price) => {
        setPublications((prev) =>
            prev.map((p) => (p.id === id ? { ...p, forSale: true, price } : p))
        );
    };

    /**
     * Returns all publication samples currently marked as for sale.
     * Derived from local state — no API call.
     *
     * @returns {Array<object>}
     */
    const getPublicationsForSale = () => publications.filter((p) => p.forSale);

    /**
     * Returns the full list of publication samples in local state.
     *
     * @returns {Array<object>}
     */
    const getAllPublications = () => publications;

    return (
        <PublicationsContext.Provider
            value={{
                publications,
                loading,
                error,
                addPublication,
                addToLibrary,
                setPublicationForSale,
                getPublicationsForSale,
                getAllPublications,
                refresh,
            }}
        >
            {children}
        </PublicationsContext.Provider>
    );
}

/**
 * Custom hook to access the {@link PublicationsContext}.
 * Must be called from a component tree wrapped in {@link PublicationsProvider}.
 *
 * @returns {{
 *   publications: Array<object>,
 *   loading: boolean,
 *   error: string|null,
 *   addPublication: Function,
 *   addToLibrary: Function,
 *   setPublicationForSale: Function,
 *   getPublicationsForSale: Function,
 *   getAllPublications: Function,
 *   refresh: Function,
 * }}
 * @throws {Error} If called outside of a {@link PublicationsProvider}.
 */
export function usePublications() {
    const ctx = useContext(PublicationsContext);
    if (!ctx) throw new Error("usePublications must be used within PublicationsProvider");
    return ctx;
}