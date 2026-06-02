import { X, Check } from "lucide-react";
import { useState } from "react";
import { useLists } from "./ListsContext";

export function AddToListModal({ isOpen, onClose, bookTitle, publicationId }) {
    const { lists, addPublicationToList, isPublicationInList } = useLists();
    const [showSuccess, setShowSuccess] = useState(false);

    if (!isOpen) return null;

    const handleToggle = (listId) => {
        if (!isPublicationInList(listId, publicationId)) {
            addPublicationToList(listId, publicationId);
        }
    };

    const handleDone = () => {
        setShowSuccess(true);
        setTimeout(() => {
            setShowSuccess(false);
            onClose();
        }, 1500);
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
                        <div className="space-y-3 max-h-96 overflow-y-auto">
                            {lists.length === 0 && (
                                <p className="text-sm text-muted-foreground text-center py-4">No lists created yet.</p>
                            )}
                            {lists.map((list) => {
                                const alreadyInList = isPublicationInList(list.id, publicationId);
                                return (
                                    <label
                                        key={list.id}
                                        className={`flex items-center gap-3 p-4 border-2 rounded-xl transition-all ${
                                            alreadyInList
                                                ? "border-primary bg-primary/5 cursor-not-allowed opacity-60"
                                                : "border-border hover:border-primary hover:bg-primary/5 cursor-pointer"
                                        }`}
                                    >
                                        <input
                                            type="checkbox"
                                            checked={alreadyInList}
                                            onChange={() => handleToggle(list.id)}
                                            disabled={alreadyInList}
                                            className="w-5 h-5 text-primary rounded focus:ring-2 focus:ring-primary cursor-pointer"
                                        />
                                        <span className="flex-1 font-medium">{list.name}</span>
                                        <span className="text-xs text-muted-foreground">
                                            {list.publicationIds.length} {list.publicationIds.length === 1 ? "item" : "items"}
                                        </span>
                                    </label>
                                );
                            })}
                        </div>
                        <button
                            onClick={handleDone}
                            className="w-full mt-6 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                        >
                            Done
                        </button>
                    </>
                ) : (
                    <div className="text-center py-8">
                        <div className="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-br from-green-400 to-emerald-500 rounded-full mb-4">
                            <Check className="w-8 h-8 text-white" strokeWidth={3} />
                        </div>
                        <h3 className="font-serif text-xl text-foreground">Lists Updated!</h3>
                    </div>
                )}
            </div>
        </div>
    );
}