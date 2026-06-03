import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { ArrowLeft, Plus, Edit2, BookOpen, Lock, Unlock, X, Save, Search, Eye } from "lucide-react";
import { useLists } from "../components/ListsContext";
import { usePublications } from "../components/PublicationsContext";

export function ManageLists() {
    const navigate = useNavigate();
    const { lists, addList, updateList, addPublicationToList, isPublicationInList } = useLists();
    const { getAllPublications } = usePublications();
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);
    const [editingListId, setEditingListId] = useState(null);
    const [searchQuery, setSearchQuery] = useState("");
    const [newList, setNewList] = useState({
        name: "",
        genre: "",
        isPublic: false,
        shareDuration: ""
    });

    const genres = [
        "Fiction", "Romance", "Mystery", "Thriller", "Fantasy", "Science Fiction",
        "Biography", "History", "Science", "Philosophy", "Poetry", "Drama"
    ];

    const togglePublic = (listId) => {
        const list = lists.find(l => l.id === listId);
        if (list) {
            updateList(listId, { isPublic: !list.isPublic });
        }
    };

    const handleCreateList = (e) => {
        e.preventDefault();
        const sharedUntil = newList.shareDuration
            ? new Date(Date.now() + parseInt(newList.shareDuration) * 24 * 60 * 60 * 1000)
            : undefined;

        addList({
            name: newList.name,
            genre: newList.genre,
            isPublic: newList.isPublic,
            sharedUntil
        });
        setShowCreateModal(false);
        setNewList({ name: "", genre: "", isPublic: false, shareDuration: "" });
    };

    const handleEditList = (e) => {
        e.preventDefault();
        if (editingListId) {
            setShowEditModal(false);
            setEditingListId(null);
        }
    };

    const allPublications = getAllPublications();

    const filteredPublications = allPublications.filter(pub =>
        pub.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        pub.author.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const addPublicationToEditingList = (pubId) => {
        if (editingListId && !isPublicationInList(editingListId, pubId)) {
            addPublicationToList(editingListId, pubId);
        }
    };

    const editingList = editingListId ? lists.find(l => l.id === editingListId) : null;

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
                        <h1 className="font-serif text-4xl mb-2 text-foreground">Manage Lists</h1>
                        <p className="text-muted-foreground">
                            US040, US042, US043: Create, organize and share publication lists
                        </p>
                    </div>

                    <button
                        onClick={() => setShowCreateModal(true)}
                        className="flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold shadow-lg"
                    >
                        <Plus className="w-5 h-5" />
                        New List
                    </button>
                </div>

                {/* Lists Grid */}
                <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {lists.map(list => (
                        <div key={list.id} className="bg-white border-2 border-border rounded-2xl p-6 hover:shadow-xl transition-shadow">
                            <div className="flex items-start justify-between mb-4">
                                <div className="flex-1">
                                    <h3 className="font-serif text-xl mb-2 text-foreground">{list.name}</h3>
                                    <div className="flex items-center gap-2 mb-2 flex-wrap">
                                        <span className="text-xs bg-primary/10 text-primary px-3 py-1 rounded-full font-medium">
                                            {list.genre}
                                        </span>
                                        <span className={`text-xs px-3 py-1 rounded-full font-medium flex items-center gap-1 ${
                                            list.isPublic
                                                ? "bg-accent/10 text-accent"
                                                : "bg-muted text-muted-foreground"
                                        }`}>
                                            {list.isPublic ? <Unlock className="w-3 h-3" /> : <Lock className="w-3 h-3" />}
                                            {list.isPublic ? "Public" : "Private"}
                                        </span>
                                    </div>
                                    <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                        <BookOpen className="w-4 h-4" />
                                        <span>{list.publicationIds.length} books</span>
                                    </div>
                                    {list.sharedUntil && (
                                        <p className="text-xs text-muted-foreground mt-2">
                                            Shared until {list.sharedUntil.toLocaleDateString()}
                                        </p>
                                    )}
                                </div>
                            </div>

                            <div className="grid grid-cols-3 gap-2 pt-4 border-t-2 border-border">
                                <Link
                                    to={`/lists/${list.id}`}
                                    className="flex items-center justify-center gap-1 px-3 py-2 bg-primary hover:bg-primary/90 text-white rounded-lg transition-colors text-sm font-medium"
                                    title="View list"
                                >
                                    <Eye className="w-4 h-4" />
                                </Link>
                                <button
                                    onClick={() => togglePublic(list.id)}
                                    className={`flex items-center justify-center gap-1 px-3 py-2 rounded-lg transition-colors text-sm font-medium ${
                                        list.isPublic
                                            ? "bg-muted hover:bg-muted/80 text-foreground"
                                            : "bg-accent hover:bg-accent/90 text-white"
                                    }`}
                                    title={list.isPublic ? "Make Private" : "Make Public"}
                                >
                                    {list.isPublic ? <Lock className="w-4 h-4" /> : <Unlock className="w-4 h-4" />}
                                </button>
                                <button
                                    onClick={() => {
                                        setEditingListId(list.id);
                                        setShowEditModal(true);
                                    }}
                                    className="flex items-center justify-center gap-1 px-3 py-2 bg-muted hover:bg-muted/80 rounded-lg transition-colors text-sm font-medium"
                                    title="Edit list"
                                >
                                    <Edit2 className="w-4 h-4" />
                                </button>
                            </div>
                        </div>
                    ))}

                    {/* Create New Card */}
                    <button
                        onClick={() => setShowCreateModal(true)}
                        className="border-2 border-dashed border-border rounded-2xl p-6 hover:border-primary hover:bg-primary/5 transition-colors flex flex-col items-center justify-center min-h-[280px] group"
                    >
                        <Plus className="w-12 h-12 text-muted-foreground group-hover:text-primary mb-3 transition-colors" />
                        <span className="font-semibold text-muted-foreground group-hover:text-primary transition-colors">
                            Create New List
                        </span>
                    </button>
                </div>

                {/* Create List Modal */}
                {showCreateModal && (
                    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                        <div className="bg-white rounded-2xl p-8 max-w-md w-full shadow-2xl">
                            <div className="flex justify-between items-center mb-6">
                                <h2 className="font-serif text-2xl text-foreground">Create New List</h2>
                                <button
                                    aria-label="Close create modal"
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
                                        value={newList.genre}
                                        onChange={(e) => setNewList(prev => ({ ...prev, genre: e.target.value }))}
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary"
                                        required
                                    >
                                        <option value="">Select genre</option>
                                        {genres.map(genre => (
                                            <option key={genre} value={genre}>{genre}</option>
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
                                        <p className="text-xs text-muted-foreground mt-1">
                                            Leave blank to share indefinitely
                                        </p>
                                    </div>
                                )}

                                <div className="flex gap-3 pt-4">
                                    <button
                                        type="submit"
                                        className="flex-1 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                                    >
                                        Create List
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

                {/* Edit List Modal */}
                {showEditModal && editingList && (
                    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                        <div className="bg-white rounded-2xl p-8 max-w-md w-full shadow-2xl">
                            <div className="flex justify-between items-center mb-6">
                                <h2 className="font-serif text-2xl text-foreground">Edit List</h2>
                                <button
                                    aria-label="Close edit modal"
                                    onClick={() => {
                                        setShowEditModal(false);
                                        setEditingListId(null);
                                    }}
                                    className="p-2 hover:bg-muted rounded-lg transition-colors"
                                >
                                    <X className="w-5 h-5" />
                                </button>
                            </div>
                            <form onSubmit={handleEditList} className="space-y-4">
                                <div>
                                    <label htmlFor="editListName" className="block mb-2 font-semibold">List Name *</label>
                                    <input
                                        id="editListName"
                                        type="text"
                                        value={editingList?.name || ""}
                                        onChange={(e) => {
                                            if (editingListId) {
                                                updateList(editingListId, { name: e.target.value });
                                            }
                                        }}
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary"
                                        required
                                    />
                                </div>

                                <div>
                                    <label htmlFor="editListGenre" className="block mb-2 font-semibold">Genre *</label>
                                    <select
                                        id="editListGenre"
                                        value={editingList?.genre || ""}
                                        onChange={(e) => {
                                            if (editingListId) {
                                                updateList(editingListId, { genre: e.target.value });
                                            }
                                        }}
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary"
                                        required
                                    >
                                        {genres.map(genre => (
                                            <option key={genre} value={genre}>{genre}</option>
                                        ))}
                                    </select>
                                </div>

                                <div className="flex items-center gap-3 p-4 bg-muted rounded-xl">
                                    <input
                                        type="checkbox"
                                        id="editIsPublic"
                                        checked={editingList?.isPublic || false}
                                        onChange={(e) => {
                                            if (editingListId) {
                                                updateList(editingListId, { isPublic: e.target.checked });
                                            }
                                        }}
                                        className="w-4 h-4"
                                    />
                                    <label htmlFor="editIsPublic" className="flex-1">
                                        <span className="font-semibold">Share publicly</span>
                                    </label>
                                </div>

                                {/* Publications Section */}
                                <div className="border-t-2 border-border pt-4">
                                    <h3 className="font-semibold mb-3 text-foreground">Add Publication</h3>
                                    <div className="mb-4">
                                        <div className="relative">
                                            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                                            <input
                                                type="text"
                                                value={searchQuery}
                                                onChange={(e) => setSearchQuery(e.target.value)}
                                                placeholder="Search publications..."
                                                className="w-full pl-10 pr-4 py-2 bg-input-background border-2 border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary text-sm"
                                            />
                                        </div>
                                    </div>

                                    <div className="max-h-64 overflow-y-auto space-y-2">
                                        {filteredPublications.map(pub => {
                                            const alreadyInList = editingListId ? isPublicationInList(editingListId, pub.id) : false;
                                            return (
                                                <label
                                                    key={pub.id}
                                                    className={`flex items-center gap-3 p-3 border-2 rounded-lg transition-all ${
                                                        alreadyInList
                                                            ? "border-primary bg-primary/5 cursor-not-allowed opacity-60"
                                                            : "border-border hover:border-primary hover:bg-primary/5 cursor-pointer"
                                                    }`}
                                                >
                                                    <input
                                                        type="checkbox"
                                                        checked={alreadyInList}
                                                        onChange={() => addPublicationToEditingList(pub.id)}
                                                        disabled={alreadyInList}
                                                        className="w-4 h-4 text-primary rounded focus:ring-2 focus:ring-primary cursor-pointer"
                                                    />
                                                    <div className="flex-1">
                                                        <p className="font-medium text-sm">{pub.title}</p>
                                                        <p className="text-xs text-muted-foreground">{pub.author}</p>
                                                    </div>
                                                </label>
                                            );
                                        })}
                                    </div>
                                </div>

                                <div className="flex gap-3 pt-4">
                                    <button
                                        type="submit"
                                        className="flex-1 flex items-center justify-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                                    >
                                        <Save className="w-5 h-5" />
                                        Save Changes
                                    </button>
                                    <button
                                        type="button"
                                        onClick={() => {
                                            setShowEditModal(false);
                                            setEditingListId(null);
                                        }}
                                        className="flex-1 px-6 py-3 bg-muted rounded-xl hover:bg-muted/80 transition-colors font-semibold"
                                    >
                                        Cancel
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}