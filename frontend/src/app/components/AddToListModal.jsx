import { X, Check } from "lucide-react";
import { useState } from "react";
import { useLists } from "./ListsContext";
import { usePublications } from "./PublicationsContext";

/**
 * Modal dialog that allows a logged-in user to assign a publication to one of
 * their existing lists.
 *
 * Business rules enforced by this component:
 * - The publication must already be in the user's library (`libraryId` present).
 * - A publication can only belong to one list at a time (`collectionId` must be null).
 * - On success, a brief confirmation screen is shown before the modal closes
 *   automatically after 1.5 s.
 *
 * @param {object}   props
 * @param {boolean}  props.isOpen        - Whether the modal is visible.
 * @param {Function} props.onClose       - Callback invoked when the modal should close
 *                                         (X button, Cancel button, or post-success timeout).
 * @param {string}   props.bookTitle     - Display title shown as the modal subtitle.
 * @param {string}   props.publicationId - ID of the publication being assigned to a list.
 * @returns {JSX.Element|null} The modal element, or null when {@link props.isOpen} is false.
 */
export function AddToListModal({ isOpen, onClose, bookTitle, publicationId }) {
    const { lists, addPublicationToList } = useLists();
    const { publications, refresh } = usePublications();

    /** Controls whether the success confirmation screen is shown. */
    const [showSuccess, setShowSuccess] = useState(false);

    /**
     * Tracks which collectionId is currently being submitted, so the
     * corresponding list row can display an "Adding…" indicator.
     * @type {string|null}
     */
    const [adding, setAdding] = useState(null);

    if (!isOpen) return null;

    /** The full publication object resolved from the publications context. */
    const pub = publications.find(p => p.id === publicationId);

    /**
     * The collectionId the publication currently belongs to, or null if it
     * has not yet been added to any list.
     * @type {string|null}
     */
    const currentCollectionId = pub?.collectionId ?? null;

    /**
     * Handles adding the publication to a chosen list.
     *
     * Guards:
     * - Exits immediately if the publication is already in a collection.
     * - Alerts the user if the API call fails.
     * After a successful call, refreshes the publications context and triggers
     * the success screen, which auto-closes the modal after 1.5 s.
     *
     * @param {string} collectionId - The ID of the target list/collection.
     * @returns {Promise<void>}
     */
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

                        {/* Guard: publication must be in the user's library before it can be listed */}
                        {!pub?.libraryId ? (
                            <div className="py-4">
                                <p className="text-sm text-amber-600 bg-amber-50 border border-amber-200 rounded-lg px-4 py-3">
                                    Add this publication to your library first before adding it to a list.
                                </p>
                            </div>
                        ) : (
                            <>
                                {/* Guard: publication is already assigned to a collection */}
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
                                        /** True when this row represents the list the publication already belongs to. */
                                        const isThisList = currentCollectionId === list.collectionId;

                                        /** True when clicking should be a no-op (publication is in any list). */
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
                                                {/* Checkbox indicator — filled only for the current list */}
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
                    /* Success screen — auto-dismisses after the setTimeout in handleAdd */
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