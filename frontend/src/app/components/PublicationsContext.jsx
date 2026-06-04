import { createContext, useContext, useEffect, useState } from "react";

const PublicationsContext = createContext(undefined);

const BASE_URL = "http://localhost:8081";
const CURRENT_USER_ID = "user-001"; // TODO: replace with real auth

// Helper: "LIKE_NEW" → "Like New"
function prettifyCondition(c) {
    if (!c) return "";
    return c.split("_").map(w => w[0] + w.slice(1).toLowerCase()).join(" ");
}

export function PublicationsProvider({ children }) {
    const [publications, setPublications] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const refresh = async () => {
        setLoading(true);
        setError(null);
        try {
            const pubsRes = await fetch(`${BASE_URL}/publicationSamples/not-in-library?userId=${CURRENT_USER_ID}`);
            if (!pubsRes.ok) throw new Error(`GET publications failed: ${pubsRes.status}`);

            const pubs = await pubsRes.json();

            // Backend returns authors/genres as plain string arrays already resolved
            const enriched = pubs.map(p => ({
                ...p,
                title: p.title ?? "(untitled)",
                author: p.authors?.join(", ") ?? "",
                genre:  p.genres?.join(", ")  ?? "",
                condition: prettifyCondition(p.condition),
            }));

            setPublications(enriched);
        } catch (e) {
            console.error("[PublicationsContext] refresh failed:", e);
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        refresh();
    }, []);

    // ── Helpers ─────────────────────────────────────────────────────

    const createBibliographicalData = async ({
                                                 title,
                                                 authorNames,
                                                 genreNames,
                                                 originalLanguage,
                                                 publicationDate,
                                                 chunksOfText,
                                             }) => {
        const res = await fetch(
            `${BASE_URL}/bibliographicalDatas?userId=${CURRENT_USER_ID}`,
            {
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
            }
        );
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`BD creation failed (${res.status}): ${body}`);
        }
        return res.json(); // { bdId, pubTitle, authorName, ... }
    };

    const createEdition = async (editionPayload) => {
        const res = await fetch(
            `${BASE_URL}/editions?userId=${CURRENT_USER_ID}`,
            {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(editionPayload),
            }
        );
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`Edition creation failed (${res.status}): ${body}`);
        }
        return res.json(); // { editionId, ... }
    };

    const createPublicationSample = async ({
                                               bibliographicalDataId,
                                               editionId,
                                               condition,
                                           }) => {
        const res = await fetch(
            `${BASE_URL}/publicationSamples?userId=${CURRENT_USER_ID}`,
            {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    bibliographicalDataId,
                    editionId,
                    condition,
                }),
            }
        );
        if (!res.ok) {
            const body = await res.text();
            throw new Error(`PublicationSample failed (${res.status}): ${body}`);
        }
        return res.json();
    };

    // ── Orchestration ──────────────────────────────────────────────
    // Form sends rich payload → we run the 3-step chain.

    const addPublication = async (form) => {
        console.log("[addPublication] starting orchestration with:", form);

        // 1. BD — IDs are sent in *Names fields (backend wraps as AuthorId/GenreId
        //         and looks them up directly). TODO: backend bug.
        const bd = await createBibliographicalData({
            title: form.title,
            authorNames: form.authorNames,
            genreNames:  form.genreNames,
            originalLanguage: form.originalLanguage,
            publicationDate: form.publishedDate,
            chunksOfText: null,
        });
        console.log("[addPublication] BD created:", bd);

        // 2. Edition
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

        // 3. PublicationSample
        const sample = await createPublicationSample({
            bibliographicalDataId: bd.bdId,
            editionId: edition.editionId,
            condition: form.condition,
        });
        console.log("[addPublication] Sample created:", sample);

        // Enrich for display — prefer backend, fall back to form values
        const enriched = {
            ...sample,
            title:  sample.title  ?? form.title,
            author: sample.authors?.join(", ") ?? form.authorDisplayNames.join(", "),
            genre:  sample.genres?.join(", ")  ?? form.genreDisplayNames.join(", "),
            condition: prettifyCondition(sample.condition ?? form.condition),
            isbn: form.isbn10 || form.isbn13 || form.issn || "",
            type: form.mediaFormat.toLowerCase(),
            publisher: form.publisherName,
            forSale: false,
        };
        setPublications((prev) => [...prev, enriched]);
        return enriched;
    };

    const setPublicationForSale = (id, price) => {
        setPublications((prev) =>
            prev.map((p) => (p.id === id ? { ...p, forSale: true, price } : p))
        );
    };

    const getPublicationsForSale = () => publications.filter((p) => p.forSale);
    const getAllPublications = () => publications;

    return (
        <PublicationsContext.Provider
            value={{
                publications,
                loading,
                error,
                addPublication,
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

export function usePublications() {
    const ctx = useContext(PublicationsContext);
    if (!ctx) throw new Error("usePublications must be used within PublicationsProvider");
    return ctx;
}