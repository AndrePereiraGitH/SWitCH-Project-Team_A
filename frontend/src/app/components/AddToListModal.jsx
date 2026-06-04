import { X, Check } from "lucide-react";
import { useState } from "react";
import { useLists } from "./ListsContext";
import { usePublications } from "./PublicationsContext";

export function AddToListModal({ isOpen, onClose, bookTitle, publicationId }) {
    const { lists, addPublicationToList } = useLists();
    const { publications, refresh } = usePublications();
    const [showSuccess, setShowSuccess] = useState(false);
    const [adding, setAdding] = useState(null);

    if (!isOpen) return null;

    const pub = publications.find(p => p.id === publicationId);
    const currentCollectionId = pub?.collectionId ?? null;

    const handleAdd = async (collectionId) => {
        if (currentCollectionId) return;
        setAdding(collectionId);
        try {
            await addPublicationToList(collectionId, publicationId);
            await refresh();
            setShowSuccess(true);
            setTimeout(() => {
                setShowSuccess(false);
                onClose();
            }, 1500);
        } catch (e) {
            alert("Failed to add to list: " + e.message);
        } finally {
            setAdding(null);
        }
    };

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-2xl max-w-md w-full p-8 relative shadow-2xl">
                {!showSuccess ? (
                    <>
                        <button
                            aria-label="Close"
                            onClick={onClose}
                            className="absolute top-4 right-4 p-2 hover:bg-muted rounded-lg transition-colors"
                        >
                            <X className="w-5 h-5" />
                        </button>
                        <h2 className="font-serif text-2xl mb-2 text-foreground">Add to List</h2>
                        <p className="text-sm text-muted-foreground mb-6">{bookTitle}</p>

                        {/* Guard: must be in library first */}
                        {!pub?.libraryId ? (
                            <div className="py-4">
                                <p className="text-sm text-amber-600 bg-amber-50 border border-amber-200 rounded-lg px-4 py-3">
                                    Add this publication to your library first before adding it to a list.
                                </p>
                            </div>
                        ) : (
                            <>
                                {currentCollectionId && (
                                    <p className="text-sm text-amber-600 bg-amber-50 border border-amber-200 rounded-lg px-4 py-3 mb-4">
                                        This publication is already in a list. Remove it from there first.
                                    </p>
                                )}

                                <div className="space-y-3 max-h-96 overflow-y-auto">
                                    {lists.length === 0 && (
                                        <p className="text-sm text-muted-foreground text-center py-4">
                                            No lists created yet.
                                        </p>
                                    )}
                                    {lists.map((list) => {
                                        const isThisList = currentCollectionId === list.collectionId;
                                        const isDisabled = currentCollectionId != null;
                                        return (
                                            <div
                                                key={list.collectionId}
                                                onClick={() => !isDisabled && handleAdd(list.collectionId)}
                                                className={`flex items-center gap-3 p-4 border-2 rounded-xl transition-all ${
                                                    isThisList
                                                        ? "border-primary bg-primary/5 cursor-not-allowed opacity-60"
                                                        : isDisabled
                                                            ? "border-border opacity-40 cursor-not-allowed"
                                                            : "border-border hover:border-primary hover:bg-primary/5 cursor-pointer"
                                                }`}
                                            >
                                                <div className="w-5 h-5 rounded border-2 border-current flex items-center justify-center flex-shrink-0">
                                                    {isThisList && <Check className="w-3 h-3 text-primary" />}
                                                </div>
                                                <span className="flex-1 font-medium">{list.collectionName}</span>
                                                {adding === list.collectionId && (
                                                    <span className="text-xs text-muted-foreground">Adding…</span>
                                                )}
                                            </div>
                                        );
                                    })}
                                </div>
                            </>
                        )}

                        <button
                            onClick={onClose}
                            className="w-full mt-6 px-6 py-3 bg-muted rounded-xl hover:bg-muted/80 transition-colors font-semibold"
                        >
                            Cancel
                        </button>
                    </>
                ) : (
                    <div className="text-center py-8">
                        <div className="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-br from-green-400 to-emerald-500 rounded-full mb-4">
                            <Check className="w-8 h-8 text-white" strokeWidth={3} />
                        </div>
                        <h3 className="font-serif text-xl text-foreground">Added to List!</h3>
                    </div>
                )}
            </div>
        </div>
    );
}