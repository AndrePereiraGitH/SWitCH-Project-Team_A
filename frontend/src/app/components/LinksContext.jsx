import { createContext, useContext, useEffect, useState } from "react";

const LinksContext = createContext(undefined);

const BASE_URL = "http://localhost:8081";
const CURRENT_USER_ID = "user-001";

function expandTemplate(template, params = {}) {
    let url = template;

    Object.entries(params).forEach(([key, value]) => {
        url = url.replace(`{${key}}`, encodeURIComponent(value));
    });

    url = url.replace(/\{\?([^}]+)\}/g, (_, keys) => {
        const query = keys
            .split(",")
            .filter((key) => params[key] !== undefined && params[key] !== null)
            .map((key) => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join("&");

        return query ? `?${query}` : "";
    });

    return url;
}

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

    function resolve(rel, matcher) {
        const link = links?.[rel];

        if (!link) {
            throw new Error(`Missing link rel: ${rel}`);
        }

        if (Array.isArray(link)) {
            const matched = matcher
                ? link.find((candidate) => matcher(candidate.href))
                : link[0];

            if (!matched?.href) {
                throw new Error(`Missing matching link for rel: ${rel}`);
            }

            return matched.href;
        }

        if (!link.href) {
            throw new Error(`Missing href for rel: ${rel}`);
        }

        return link.href;
    }

    return (
        <LinksContext.Provider
            value={{
                links,
                loading,
                error,
                resolve,
                expandTemplate,
            }}
        >
            {children}
        </LinksContext.Provider>
    );
}

export function useLinks() {
    const ctx = useContext(LinksContext);

    if (!ctx) {
        throw new Error("useLinks must be used within LinksProvider");
    }

    return ctx;
}