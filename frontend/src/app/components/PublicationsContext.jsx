import { createContext, useContext, useEffect, useState } from "react";

const PublicationsContext = createContext(undefined);

// TODO: replace with real auth once available
const CURRENT_USER_ID = "user-001";
const BASE_URL = "http://localhost:8081";

export function PublicationsProvider({ children }) {
  const [publications, setPublications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Load existing publications from DB on mount
  const refresh = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(
          `${BASE_URL}/publicationSamples?userId=${CURRENT_USER_ID}`
      );
      if (!res.ok) throw new Error(`GET failed: ${res.status}`);
      const data = await res.json();
      setPublications(data);
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

  // Create a publication in the backend.
  // The form sends a rich object; backend only accepts 3 fields,
  // so we split: strip down for the request, keep the rest in local state.
  const addPublication = async (formPublication) => {
    const {
      bibliographicalDataId,
      editionId,
      condition,
      ...displayOnly
    } = formPublication;

    const backendPayload = { bibliographicalDataId, editionId, condition };
    console.log("[addPublication] POST payload:", backendPayload);

    const res = await fetch(
        `${BASE_URL}/publicationSamples?userId=${CURRENT_USER_ID}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(backendPayload),
        }
    );

    if (!res.ok) {
      const body = await res.text();
      console.error("[addPublication] backend rejected:", res.status, body);
      throw new Error(`POST failed (${res.status}): ${body}`);
    }

    const created = await res.json();
    console.log("[addPublication] backend returned:", created);

    // Merge backend response with display-only form fields so the list
    // can still render title/author/etc.
    const enriched = { ...displayOnly, ...created, forSale: false };
    setPublications((prev) => [...prev, enriched]);
    return enriched;
  };

  // Local-only (no backend endpoint for this yet)
  const setPublicationForSale = (id, price) => {
    setPublications((prev) =>
        prev.map((pub) =>
            pub.id === id ? { ...pub, forSale: true, price } : pub
        )
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
  const context = useContext(PublicationsContext);
  if (context === undefined) {
    throw new Error("usePublications must be used within a PublicationsProvider");
  }
  return context;
}