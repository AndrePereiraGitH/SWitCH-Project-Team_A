import { createContext, useContext, useEffect, useState } from "react";
import { useLinks } from "./LinksContext";

const PublicationsContext = createContext(undefined);

const CURRENT_USER_ID = "user-001";

// Helper: "LIKE_NEW" → "Like New"
function prettifyCondition(c) {
    if (!c) return "";
    return c.split("_").map(w => w[0] + w.slice(1).toLowerCase()).join(" ");
}

// Helper: replace templated URI parts like {userId} or ?userId={userId}
function expandUri(href, params) {
    let url = href;
    Object.entries(params).forEach(([key, value]) => {
        url = url.replace(`{${key}}`, encodeURIComponent(value));
    });
    return url;
}

export function PublicationsProvider({ children }) {
    const { links: rootLinks, loading: linksLoading } = useLinks();
    const [publications, setPublications] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

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

    useEffect(() => {
        if (!linksLoading) {
            refresh();
        }
    }, [linksLoading, rootLinks]);

    // ── Helpers ─────────────────────────────────────────────────────

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

    // ── Orchestration ──────────────────────────────────────────────

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

export function usePublications() {
    const ctx = useContext(PublicationsContext);
    if (!ctx) throw new Error("usePublications must be used within PublicationsProvider");
    return ctx;
}