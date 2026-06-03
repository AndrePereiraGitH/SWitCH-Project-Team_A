import { createContext, useContext, useState } from "react";

const PublicationsContext = createContext(undefined);

export function PublicationsProvider({ children }) {
  const [publications, setPublications] = useState([]);

  const addPublication = (publication) => {
    const newPublication = {
      ...publication,
      id: Math.random().toString(36).substr(2, 9),
      forSale: false,
    };
    setPublications((prev) => [...prev, newPublication]);
    return newPublication
  };

  const setPublicationForSale = (id, price) => {
    setPublications((prev) =>
      prev.map((pub) =>
        pub.id === id ? { ...pub, forSale: true, price } : pub
      )
    );
  };

  const getPublicationsForSale = () => {
    return publications.filter((pub) => pub.forSale);
  };

  const getAllPublications = () => {
    return publications;
  };

  return (
    <PublicationsContext.Provider
      value={{
        publications,
        addPublication,
        setPublicationForSale,
        getPublicationsForSale,
        getAllPublications,
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
