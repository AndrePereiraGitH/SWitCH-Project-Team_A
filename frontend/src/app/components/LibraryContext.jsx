import { createContext, useContext, useEffect, useState } from "react";

const LibraryContext = createContext(undefined);

const BASE_URL = "http://localhost:8081";
const CURRENT_USER_ID = "user-001"; // TODO: replace with real auth

export function LibraryProvider({ children }) {
  const [library, setLibrary] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchLibrary = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`${BASE_URL}/libraries/by-user/${CURRENT_USER_ID}`);
      if (res.status === 404) {
        setLibrary(null);
        return;
      }
      if (!res.ok) throw new Error(`GET library failed: ${res.status}`);
      const data = await res.json();
      console.log("[LibraryContext] library:", data);
      setLibrary(data);
    } catch (e) {
      console.error("[LibraryContext] fetch failed:", e);
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLibrary();
  }, []);

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