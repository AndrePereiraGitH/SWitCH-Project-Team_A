import { createContext, useContext, useState } from "react";

const ListsContext = createContext(undefined);

export function ListsProvider({ children }) {
  const [lists, setLists] = useState([]);

  const addList = (list) => {
    const newList = { ...list, id: Date.now().toString(), publicationIds: [] };
    setLists((prev) => [...prev, newList]);
  };

  const updateList = (id, updates) => {
    setLists((prev) => prev.map((list) => (list.id === id ? { ...list, ...updates } : list)));
  };

  const deleteList = (id) => {
    setLists((prev) => prev.filter((list) => list.id !== id));
  };

  const addPublicationToList = (listId, publicationId) => {
    setLists((prev) =>
      prev.map((list) => {
        if (list.id === listId && !list.publicationIds.includes(publicationId)) {
          return { ...list, publicationIds: [...list.publicationIds, publicationId] };
        }
        return list;
      })
    );
  };

  const removePublicationFromList = (listId, publicationId) => {
    setLists((prev) =>
      prev.map((list) => {
        if (list.id === listId) {
          return { ...list, publicationIds: list.publicationIds.filter((id) => id !== publicationId) };
        }
        return list;
      })
    );
  };

  const isPublicationInList = (listId, publicationId) => {
    const list = lists.find((l) => l.id === listId);
    return list ? list.publicationIds.includes(publicationId) : false;
  };

  const getListById = (listId) => {
    return lists.find((l) => l.id === listId);
  };

  return (
    <ListsContext.Provider value={{ lists, addList, updateList, deleteList, addPublicationToList, removePublicationFromList, isPublicationInList, getListById }}>
      {children}
    </ListsContext.Provider>
  );
}

export function useLists() {
  const context = useContext(ListsContext);
  if (!context) {
    throw new Error("useLists must be used within ListsProvider");
  }
  return context;
}
