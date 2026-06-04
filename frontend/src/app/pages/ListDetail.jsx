import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, Plus } from "lucide-react";
import { useLists } from "../components/ListsContext";
import { usePublications } from "../components/PublicationsContext";
import { BookCard } from "../components/BookCard";

export function ListDetail() {
    const navigate = useNavigate();
    const { listId } = useParams();
    const { getListById, addPublicationToList } = useLists();
    const { publications, refresh } = usePublications();
    const [showManageModal, setShowManageModal] = useState(false);

    const list = listId ? getListById(listId) : undefined;

    if (!list) {
        return (
            <div className="min-h-screen bg-background pt-24 pb-12">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <p className="text-center text-muted-foreground">List not found</p>
                </div>
            </div>
        );
    }

    // Publications in this collection — matched by collectionId on the publication
    const publicationsInList = publications.filter(p => p.collectionId === list.collectionId);
    const notInList = publications.filter(
        p => p.collectionId !== list.collectionId && p.libraryId != null
    );

    const handleAddPublication = async (publicationId) => {
        try {
            await addPublicationToList(list.collectionId, publicationId);
            await refresh(); // re-fetch so collectionId is updated on the publication
        } catch (e) {
            alert("Failed to add: " + e.message);
        }
    };

    return (
        <div className="min-h-screen bg-background pt-24 pb-12">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <button
                    onClick={() => navigate("/lists")}
                    className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
                >
                    <ArrowLeft className="w-4 h-4" />
                    My Lists
                </button>

                <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
                    <div>
                        <h1 className="font-serif text-4xl mb-2 text-foreground">
                            {list.collectionName}
                        </h1>
                        <div className="flex items-center gap-4 text-muted-foreground">
                            <span>
                                {publicationsInList.length}{" "}
                                {publicationsInList.length === 1 ? "publication" : "publications"}
                            </span>
                            {!list.private && (
                                <span className="text-accent font-medium">Public</span>
                            )}
                        </div>
                    </div>
                    <button
                        onClick={() => setShowManageModal(true)}
                        className="flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold shadow-lg"
                    >
                        <Plus className="w-5 h-5" />
                        Add Publication
                    </button>
                </div>

                {publicationsInList.length === 0 ? (
                    <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                        <p className="text-muted-foreground mb-4">No publications in this list</p>
                        <button
                            onClick={() => setShowManageModal(true)}
                            className="inline-flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                        >
                            <Plus className="w-5 h-5" />
                            Add Publications
                        </button>
                    </div>
                ) : (
                    <div className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {publicationsInList.map(pub => (
                            <BookCard
                                key={pub.id}
                                title={pub.title}
                                author={pub.author}
                                genre={pub.genre}
                                condition={pub.condition}
                                price={pub.price}
                            />
                        ))}
                    </div>
                )}

                {showManageModal && (
                    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                        <div className="bg-white rounded-2xl max-w-4xl w-full p-8 shadow-2xl max-h-[80vh] overflow-y-auto">
                            <h2 className="font-serif text-2xl mb-4 text-foreground">Add Publications</h2>
                            <p className="text-muted-foreground mb-6">
                                Select publications from your library to add to this list
                            </p>

                            {notInList.length === 0 ? (
                                <p className="text-center text-muted-foreground py-8">
                                    All your publications are already in this list.
                                </p>
                            ) : (
                                <div className="space-y-3 mb-6">
                                    {notInList.map(pub => (
                                        <div
                                            key={pub.id}
                                            className="flex items-center gap-4 p-4 border-2 border-border rounded-xl hover:border-primary/50 transition-all cursor-pointer"
                                            onClick={() => handleAddPublication(pub.id)}
                                        >
                                            <div className="flex-1">
                                                <h3 className="font-semibold text-foreground">{pub.title}</h3>
                                                <p className="text-sm text-muted-foreground">{pub.author}</p>
                                                {pub.genre && (
                                                    <span className="text-xs px-2 py-1 bg-primary/10 text-primary rounded-full">
                                                        {pub.genre}
                                                    </span>
                                                )}
                                            </div>
                                            <button className="px-4 py-2 bg-primary text-primary-foreground rounded-xl text-sm font-medium">
                                                Add
                                            </button>
                                        </div>
                                    ))}
                                </div>
                            )}

                            <button
                                onClick={() => setShowManageModal(false)}
                                className="w-full px-6 py-3 bg-muted rounded-xl hover:bg-muted/80 transition-colors font-semibold"
                            >
                                Done
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}