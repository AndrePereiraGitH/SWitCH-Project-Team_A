import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { ArrowLeft, Plus, BookOpen, Lock, Unlock, X, Search, Eye } from "lucide-react";
import { useLists } from "../components/ListsContext";
import { usePublications } from "../components/PublicationsContext";

const API_BASE_URL = "http://localhost:8081";

export function ManageLists() {
    const navigate = useNavigate();
    const { lists, loading, error, addList, makePublic, makePublicUntil, addPublicationToList } = useLists();
    const { publications, refresh } = usePublications();

    const [genres, setGenres] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showAddPubModal, setShowAddPubModal] = useState(false);
    const [activeListId, setActiveListId] = useState(null);
    const [searchQuery, setSearchQuery] = useState("");
    const [submitting, setSubmitting] = useState(false);

    const [newList, setNewList] = useState({
        name: "",
        genreId: "",
        isPublic: false,
        shareDuration: "",
    });

    // Fetch genres from backend
    useEffect(() => {
        fetch(`${API_BASE_URL}/genres`)
            .then(r => r.json())
            .then(setGenres)
            .catch(console.error);
    }, []);

    const handleCreateList = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        try {
            await addList({
                collectionName: newList.name,
                genreId: newList.genreId,
            });

            // If public with duration, make it public until that date
            // We do this after creation using the returned collectionId
            // The context's addList returns the created DTO
            // Share handling is done separately via makePublicUntil / makePublic
            // For now: if isPublic is checked, call make-public after creation
            // (handled below in a follow-up step — see note)

            setShowCreateModal(false);
            setNewList({ name: "", genreId: "", isPublic: false, shareDuration: "" });
        } catch (err) {
            alert("Failed to create list: " + err.message);
        } finally {
            setSubmitting(false);
        }
    };

    const handleTogglePublic = async (list) => {
        try {
            if (list.private) {
                await makePublic(list.collectionId);
            }
        } catch (err) {
            alert("Failed to update visibility: " + err.message);
        }
    };

    const handleAddPublication = async (publicationId) => {
        if (!activeListId) return;
        try {
            await addPublicationToList(activeListId, publicationId);
            await refresh(); // re-fetch so collectionId is updated on the publication
        } catch (err) {
            alert("Failed to add publication: " + err.message);
        }
    };

    const filteredPublications = publications
        .filter(p => p.libraryId != null)
        .filter(pub =>
            pub.title?.toLowerCase().includes(searchQuery.toLowerCase()) ||
            pub.author?.toLowerCase().includes(searchQuery.toLowerCase())
        );

    // Publications already in the active list (by collectionId on the publication)
    const pubsInActiveList = publications.filter(
        p => p.collectionId === activeListId
    );

    return (
        <div className="min-h-screen bg-background pt-24 pb-12">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <button
                    onClick={() => navigate(-1)}
                    className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
                >
                    <ArrowLeft className="w-4 h-4" />
                    Back
                </button>

                <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
                    <div>
                        <h1 className="font-serif text-4xl mb-2 text-foreground">My Lists</h1>
                        <p className="text-muted-foreground">Create, organize and share publication lists</p>
                    </div>
                    <button
                        onClick={() => setShowCreateModal(true)}
                        className="flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold shadow-lg"
                    >
                        <Plus className="w-5 h-5" />
                        New List
                    </button>
                </div>

                {error && (
                    <div className="bg-destructive/10 border-2 border-destructive text-destructive rounded-2xl p-4 mb-6">
                        Could not load lists: {error}
                    </div>
                )}

                {/* Lists Grid */}
                {loading ? (
                    <div className="text-center text-muted-foreground py-12">Loading lists…</div>
                ) : (
                    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {lists.map(list => (
                            <div key={list.collectionId} className="bg-white border-2 border-border rounded-2xl p-6 hover:shadow-xl transition-shadow">
                                <div className="flex items-start justify-between mb-4">
                                    <div className="flex-1">
                                        <h3 className="font-serif text-xl mb-2 text-foreground">{list.collectionName}</h3>
                                        <div className="flex items-center gap-2 mb-2 flex-wrap">
                                            <span className={`text-xs px-3 py-1 rounded-full font-medium flex items-center gap-1 ${
                                                !list.private
                                                    ? "bg-accent/10 text-accent"
                                                    : "bg-muted text-muted-foreground"
                                            }`}>
                                                {!list.private
                                                    ? <><Unlock className="w-3 h-3" /> Public</>
                                                    : <><Lock className="w-3 h-3" /> Private</>
                                                }
                                            </span>
                                        </div>
                                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                            <BookOpen className="w-4 h-4" />
                                            <span>
                                                {publications.filter(p => p.collectionId === list.collectionId).length} publications
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div className="grid grid-cols-3 gap-2 pt-4 border-t-2 border-border">
                                    <Link
                                        to={`/lists/${list.collectionId}`}
                                        className="flex items-center justify-center gap-1 px-3 py-2 bg-primary hover:bg-primary/90 text-white rounded-lg transition-colors text-sm font-medium"
                                        title="View list"
                                    >
                                        <Eye className="w-4 h-4" />
                                    </Link>
                                    <button
                                        onClick={() => handleTogglePublic(list)}
                                        disabled={!list.private}
                                        className={`flex items-center justify-center gap-1 px-3 py-2 rounded-lg transition-colors text-sm font-medium ${
                                            !list.private
                                                ? "bg-muted text-muted-foreground cursor-not-allowed opacity-50"
                                                : "bg-accent hover:bg-accent/90 text-white"
                                        }`}
                                        title={!list.private ? "Already public" : "Make Public"}
                                    >
                                        {!list.private ? <Lock className="w-4 h-4" /> : <Unlock className="w-4 h-4" />}
                                    </button>
                                    <button
                                        onClick={() => {
                                            setActiveListId(list.collectionId);
                                            setShowAddPubModal(true);
                                        }}
                                        className="flex items-center justify-center gap-1 px-3 py-2 bg-muted hover:bg-muted/80 rounded-lg transition-colors text-sm font-medium"
                                        title="Add publications"
                                    >
                                        <Plus className="w-4 h-4" />
                                    </button>
                                </div>
                            </div>
                        ))}

                        {/* Create New Card */}
                        <button
                            onClick={() => setShowCreateModal(true)}
                            className="border-2 border-dashed border-border rounded-2xl p-6 hover:border-primary hover:bg-primary/5 transition-colors flex flex-col items-center justify-center min-h-[200px] group"
                        >
                            <Plus className="w-12 h-12 text-muted-foreground group-hover:text-primary mb-3 transition-colors" />
                            <span className="font-semibold text-muted-foreground group-hover:text-primary transition-colors">
                                Create New List
                            </span>
                        </button>
                    </div>
                )}

                {/* Create List Modal */}
                {showCreateModal && (
                    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                        <div className="bg-white rounded-2xl p-8 max-w-md w-full shadow-2xl">
                            <div className="flex justify-between items-center mb-6">
                                <h2 className="font-serif text-2xl text-foreground">Create New List</h2>
                                <button
                                    aria-label="Close"
                                    onClick={() => setShowCreateModal(false)}
                                    className="p-2 hover:bg-muted rounded-lg transition-colors"
                                >
                                    <X className="w-5 h-5" />
                                </button>
                            </div>
                            <form onSubmit={handleCreateList} className="space-y-4">
                                <div>
                                    <label htmlFor="listName" className="block mb-2 font-semibold">List Name *</label>
                                    <input
                                        id="listName"
                                        type="text"
                                        value={newList.name}
                                        onChange={(e) => setNewList(prev => ({ ...prev, name: e.target.value }))}
                                        placeholder="Ex: Summer Books 2026"
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary"
                                        required
                                    />
                                </div>

                                <div>
                                    <label htmlFor="listGenre" className="block mb-2 font-semibold">Genre *</label>
                                    <select
                                        id="listGenre"
                                        value={newList.genreId}
                                        onChange={(e) => setNewList(prev => ({ ...prev, genreId: e.target.value }))}
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary"
                                        required
                                    >
                                        <option value="">Select genre</option>
                                        {genres.map(g => (
                                            <option key={g.id} value={g.id}>{g.genreName}</option>
                                        ))}
                                    </select>
                                </div>

                                <div className="flex items-center gap-3 p-4 bg-muted rounded-xl">
                                    <input
                                        type="checkbox"
                                        id="isPublic"
                                        checked={newList.isPublic}
                                        onChange={(e) => setNewList(prev => ({ ...prev, isPublic: e.target.checked }))}
                                        className="w-4 h-4"
                                    />
                                    <label htmlFor="isPublic" className="flex-1">
                                        <span className="font-semibold">Share publicly</span>
                                        <p className="text-sm text-muted-foreground">Other users will be able to see this list</p>
                                    </label>
                                </div>

                                {newList.isPublic && (
                                    <div>
                                        <label htmlFor="shareDuration" className="block mb-2 font-semibold">Share Duration (days)</label>
                                        <input
                                            id="shareDuration"
                                            type="number"
                                            value={newList.shareDuration}
                                            onChange={(e) => setNewList(prev => ({ ...prev, shareDuration: e.target.value }))}
                                            placeholder="30"
                                            min="1"
                                            className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary"
                                        />
                                        <p className="text-xs text-muted-foreground mt-1">Leave blank to share indefinitely</p>
                                    </div>
                                )}

                                <div className="flex gap-3 pt-4">
                                    <button
                                        type="submit"
                                        disabled={submitting}
                                        className="flex-1 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold disabled:opacity-50"
                                    >
                                        {submitting ? "Creating…" : "Create List"}
                                    </button>
                                    <button
                                        type="button"
                                        onClick={() => setShowCreateModal(false)}
                                        className="flex-1 px-6 py-3 bg-muted rounded-xl hover:bg-muted/80 transition-colors font-semibold"
                                    >
                                        Cancel
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}

                {/* Add Publications Modal */}
                {showAddPubModal && (
                    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                        <div className="bg-white rounded-2xl max-w-2xl w-full p-8 shadow-2xl max-h-[80vh] overflow-y-auto">
                            <div className="flex justify-between items-center mb-6">
                                <h2 className="font-serif text-2xl text-foreground">Add Publications</h2>
                                <button onClick={() => setShowAddPubModal(false)} className="p-2 hover:bg-muted rounded-lg">
                                    <X className="w-5 h-5" />
                                </button>
                            </div>

                            <div className="relative mb-4">
                                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                                <input
                                    type="text"
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                    placeholder="Search publications…"
                                    className="w-full pl-10 pr-4 py-2 bg-input-background border-2 border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary text-sm"
                                />
                            </div>

                            <div className="space-y-2 mb-6">
                                {filteredPublications.map(pub => {
                                    const alreadyIn = pubsInActiveList.some(p => p.id === pub.id);
                                    return (
                                        <div
                                            key={pub.id}
                                            className={`flex items-center gap-3 p-3 border-2 rounded-lg transition-all ${
                                                alreadyIn
                                                    ? "border-primary bg-primary/5 opacity-60 cursor-not-allowed"
                                                    : "border-border hover:border-primary hover:bg-primary/5 cursor-pointer"
                                            }`}
                                            onClick={() => !alreadyIn && handleAddPublication(pub.id)}
                                        >
                                            <div className="flex-1">
                                                <p className="font-medium text-sm">{pub.title}</p>
                                                <p className="text-xs text-muted-foreground">{pub.author}</p>
                                            </div>
                                            {alreadyIn ? (
                                                <span className="text-xs text-primary font-medium">Added</span>
                                            ) : (
                                                <button className="px-3 py-1 bg-primary text-white rounded-lg text-sm font-medium">
                                                    Add
                                                </button>
                                            )}
                                        </div>
                                    );
                                })}
                            </div>

                            <button
                                onClick={() => setShowAddPubModal(false)}
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