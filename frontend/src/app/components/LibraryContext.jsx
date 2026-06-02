import { createContext, useContext, useState } from "react";

const LibraryContext = createContext(undefined);

export function LibraryProvider({ children }) {
  const [library, setLibrary] = useState(null);

  const createLibrary = (name) => {
    if (!library) {
      setLibrary({ name, publicationIds: [] });
    }
  };

  const addPublicationToLibrary = (publicationId) => {
    if (library && !library.publicationIds.includes(publicationId)) {
      setLibrary({ ...library, publicationIds: [...library.publicationIds, publicationId] });
    }
  };

  const isPublicationInLibrary = (publicationId) => {
    return library ? library.publicationIds.includes(publicationId) : false;
  };

  return (
    <LibraryContext.Provider
      value={{ library, createLibrary, addPublicationToLibrary, isPublicationInLibrary }}
    >
      {children}
    </LibraryContext.Provider>
  );
}

export function useLibrary() {
  const context = useContext(LibraryContext);
  if (context === undefined) {
    throw new Error("useLibrary must be used within a LibraryProvider");
  }
  return context;
}
