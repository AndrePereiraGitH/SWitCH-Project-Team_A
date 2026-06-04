import { createContext, useContext, useEffect, useState } from "react";

const ListsContext = createContext(undefined);

const BASE_URL = "http://localhost:8081";
const CURRENT_USER_ID = "user-001"; // TODO: replace with real auth

export function ListsProvider({ children }) {
  const [lists, setLists] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchLists = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(
          `${BASE_URL}/collection-of-publications/owner?userId=${CURRENT_USER_ID}`
      );
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
    fetchLists();
  }, []);

  const addList = async ({ collectionName, genreId }) => {
    const res = await fetch(
        `${BASE_URL}/collection-of-publications/?ownerId=${CURRENT_USER_ID}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ collectionName, genreId }),
        }
    );
    if (!res.ok) {
      const body = await res.text();
      throw new Error(`Create list failed (${res.status}): ${body}`);
    }
    const created = await res.json();
    setLists(prev => [...prev, created]);
    return created;
  };

  const makePublic = async (collectionId) => {
    const res = await fetch(
        `${BASE_URL}/collection-of-publications/${collectionId}/make-public?userId=${CURRENT_USER_ID}`,
        { method: "PATCH", headers: { "Content-Type": "application/json" } }
    );
    if (!res.ok) {
      const body = await res.text();
      throw new Error(`Make public failed (${res.status}): ${body}`);
    }
    const updated = await res.json();
    setLists(prev => prev.map(l => l.collectionId === collectionId ? updated : l));
    return updated;
  };

  const makePublicUntil = async (collectionId, publicUntil) => {
    const res = await fetch(
        `${BASE_URL}/collection-of-publications/${collectionId}/make-public-until?publicUntil=${publicUntil}&userId=${CURRENT_USER_ID}`,
        { method: "PATCH", headers: { "Content-Type": "application/json" } }
    );
    if (!res.ok) {
      const body = await res.text();
      throw new Error(`Make public until failed (${res.status}): ${body}`);
    }
    const updated = await res.json();
    setLists(prev => prev.map(l => l.collectionId === collectionId ? updated : l));
    return updated;
  };

  const addPublicationToList = async (collectionId, publicationSampleId) => {
    const res = await fetch(
        `${BASE_URL}/publicationSamples/${publicationSampleId}/collection?collectionId=${collectionId}&userId=${CURRENT_USER_ID}`,
        { method: "PATCH", headers: { "Content-Type": "application/json" } }
    );
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