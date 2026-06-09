import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Plus, Filter, Grid3x3, List as ListIcon, ListPlus, X } from "lucide-react";
import { BookCard } from "../components/BookCard";
import { AddToListModal } from "../components/AddToListModal";
import { usePublications } from "../components/PublicationsContext";
import { useLists } from "../components/ListsContext";

const LANGUAGE_DISPLAY = {
    en: "English", pt: "Portuguese", es: "Spanish",
    fr: "French", de: "German", it: "Italian",
    nl: "Dutch", ru: "Russian", zh: "Chinese", ja: "Japanese",
};

const getType = (type) => {
    if (type === "book") return "Book";
    if (type === "magazine") return "Magazine";
    return "Book";
};

export function MyPublications() {
    const navigate = useNavigate();
    const { publications, loading, error, addToLibrary } = usePublications();
    const { lists } = useLists();
    const [viewMode, setViewMode] = useState("grid");
    const [filterGenre, setFilterGenre] = useState("all");
    const [filterType, setFilterType] = useState("all");
    const [addToListModalOpen, setAddToListModalOpen] = useState(false);
    const [selectedBook, setSelectedBook] = useState(null);
    const [detailPub, setDetailPub] = useState(null);

    const getCollectionName = (collectionId) => {
        if (!collectionId) return null;
        return lists.find(l => l.collectionId === collectionId)?.collectionName ?? collectionId;
    };

    const normalized = (publications ?? []).map(p => ({
        id: p.id,
        title: p.title && p.title !== "(untitled)" ? p.title : "(untitled)",
        author: p.authors?.join(", ") ?? "",
        genre:  p.genres?.join(", ")  ?? "",
        isbn: p.isbn ?? p.code ?? "",
        type: p.type ?? p.mediaFormat?.toLowerCase() ?? "book",
        condition: p.condition ?? "",
        language: LANGUAGE_DISPLAY[p.language] ?? p.language ?? "",
        price: p.price ?? null,
        libraryId: p.libraryId ?? null,
        collectionId: p.collectionId ?? null,
        inLibrary: p.libraryId != null || p.inLibrary === true,
    }));

    const genres = ["all", ...Array.from(new Set(normalized.map(b => b.genre).filter(Boolean)))];
    const filteredBooks = normalized.filter(b => {
        const matchesGenre = filterGenre === "all" || b.genre.includes(filterGenre);
        const matchesType = filterType === "all" || b.type === filterType;
        return matchesGenre && matchesType;
    });

    const handleAddToList = (book) => {
        setSelectedBook(book);
        setAddToListModalOpen(true);
    };

    return (
        <div className="min-h-screen bg-background pt-24 pb-12">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <button
                    onClick={() => navigate("/dashboard")}
                    className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
                >
                    <ArrowLeft className="w-4 h-4" />
                    My Dashboard
                </button>

                <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
                    <div>
                        <h1 className="font-serif text-4xl mb-2 text-foreground">My Publications</h1>
                        <p className="text-muted-foreground">Manage your registered publications</p>
                    </div>
                    <button
                        onClick={() => navigate("/add-publication")}
                        className="flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold shadow-lg"
                    >
                        <Plus className="w-5 h-5" />
                        Add Publication
                    </button>
                </div>

                {error && (
                    <div className="bg-destructive/10 border-2 border-destructive text-destructive rounded-2xl p-4 mb-6">
                        Could not load publications: {error}
                    </div>
                )}

                {/* Filters */}
                <div className="bg-white border-2 border-border rounded-2xl p-6 mb-8 shadow-sm">
                    <div className="flex items-center gap-3 mb-6">
                        <Filter className="w-5 h-5 text-primary" />
                        <h3 className="font-semibold text-lg">Filters</h3>
                    </div>
                    <div className="flex items-center gap-6">
                        <div className="flex-1">
                            <label className="block mb-2 font-semibold text-foreground">Filter by Genre</label>
                            <select
                                value={filterGenre}
                                onChange={(e) => setFilterGenre(e.target.value)}
                                className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary"
                            >
                                <option value="all">All Genres</option>
                                {genres.filter(g => g !== "all").map(genre => (
                                    <option key={genre} value={genre}>{genre}</option>
                                ))}
                            </select>
                        </div>
                        <div className="flex-1">
                            <label className="block mb-2 font-semibold text-foreground">Filter by Type</label>
                            <select
                                value={filterType}
                                onChange={(e) => setFilterType(e.target.value)}
                                className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary"
                            >
                                <option value="all">All Types</option>
                                <option value="book">Book</option>
                                <option value="magazine">Magazine</option>
                            </select>
                        </div>
                        <div className="flex items-center gap-2 pt-7">
                            <button
                                onClick={() => setViewMode("grid")}
                                className={`p-2 rounded-lg transition-colors ${
                                    viewMode === "grid" ? "text-primary" : "text-muted-foreground hover:text-primary hover:bg-gray-100"
                                }`}
                                title="Grid view"
                            >
                                <Grid3x3 className="w-5 h-5" />
                            </button>
                            <button
                                onClick={() => setViewMode("list")}
                                className={`p-2 rounded-lg transition-colors ${
                                    viewMode === "list" ? "text-primary" : "text-muted-foreground hover:text-primary hover:bg-gray-100"
                                }`}
                                title="List view"
                            >
                                <ListIcon className="w-5 h-5" />
                            </button>
                        </div>
                    </div>
                </div>

                {/* Books display */}
                {loading ? (
                    <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                        <p className="text-muted-foreground">Loading publications…</p>
                    </div>
                ) : filteredBooks.length === 0 ? (
                    <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                        <p className="text-muted-foreground mb-4">No publications found</p>
                        <button
                            onClick={() => navigate("/add-publication")}
                            className="inline-flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                        >
                            <Plus className="w-5 h-5" />
                            Add First Publication
                        </button>
                    </div>
                ) : viewMode === "grid" ? (
                    <div className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {filteredBooks.map(book => (
                            <div key={book.id}>
                                <div onClick={() => setDetailPub(book)} className="cursor-pointer">
                                    <BookCard
                                        title={book.title}
                                        author={book.author}
                                        genre={book.genre}
                                        condition={book.condition}
                                        price={book.price}
                                    />
                                </div>
                                <div className="flex gap-2 mt-3 flex-wrap items-center text-center">
                                    <button
                                        onClick={() => navigate(`/sell/${book.id}`)}
                                        className="flex-1 px-4 py-2 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                                    >
                                        Sell
                                    </button>
                                    <button
                                        onClick={() => handleAddToList(book)}
                                        className="px-4 py-2 border-2 border-primary text-primary rounded-xl hover:bg-primary/10 transition-colors font-semibold"
                                        title="Add to List"
                                    >
                                        <ListPlus className="w-5 h-5" />
                                    </button>
                                    {book.inLibrary ? (
                                        <span className="flex-1 px-4 py-2 bg-green-100 text-green-700 rounded-xl text-sm font-medium">
                                            ✓ In Library
                                        </span>
                                    ) : (
                                        <button
                                            onClick={async () => {
                                                try {
                                                    await addToLibrary(book.id);
                                                } catch (e) {
                                                    console.error("Add to library error:", e.message);
                                                    alert("Failed: " + e.message);
                                                }
                                            }}
                                            className="flex-1 px-4 py-2 bg-primary/10 text-primary rounded-xl hover:bg-primary/20 transition-colors font-medium"
                                        >
                                            Add to Library
                                        </button>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="space-y-4">
                        {filteredBooks.map(book => (
                            <div
                                key={book.id}
                                className="bg-white border-2 border-border rounded-2xl p-6 hover:shadow-lg transition-shadow cursor-pointer"
                                onClick={() => setDetailPub(book)}
                            >
                                <div className="flex justify-between items-start">
                                    <div className="flex-1">
                                        <h3 className="font-serif text-xl mb-1 text-foreground">{book.title}</h3>
                                        <p className="text-muted-foreground mb-2">{book.author}</p>
                                        <div className="flex gap-4 text-sm flex-wrap">
                                            {book.isbn && <span className="text-muted-foreground">ISBN: {book.isbn}</span>}
                                            {book.genre && (
                                                <span className="px-3 py-1 bg-primary/10 text-primary rounded-full text-xs font-medium">
                                                    {book.genre}
                                                </span>
                                            )}
                                            <span className="px-3 py-1 bg-accent/10 text-accent rounded-full text-xs font-medium">
                                                {getType(book.type)}
                                            </span>
                                            {book.price != null && <span className="text-muted-foreground">€{book.price}</span>}
                                        </div>
                                    </div>
                                    <div className="flex gap-2 flex-wrap items-center justify-end" onClick={e => e.stopPropagation()}>
                                        <button
                                            onClick={() => navigate(`/sell/${book.id}`)}
                                            className="px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                                        >
                                            Sell
                                        </button>
                                        <button
                                            onClick={() => handleAddToList(book)}
                                            className="px-4 py-3 border-2 border-primary text-primary rounded-xl hover:bg-primary/10 transition-colors font-semibold"
                                            title="Add to List"
                                        >
                                            <ListPlus className="w-5 h-5" />
                                        </button>
                                        {book.inLibrary ? (
                                            <span className="px-4 py-2 bg-green-100 text-green-700 rounded-xl text-sm font-medium">
                                                ✓ In Library
                                            </span>
                                        ) : (
                                            <button
                                                onClick={async () => {
                                                    try {
                                                        await addToLibrary(book.id);
                                                    } catch (e) {
                                                        console.error("Add to library error:", e.message);
                                                        alert("Failed: " + e.message);
                                                    }
                                                }}
                                                className="px-4 py-3 bg-primary/10 text-primary rounded-xl hover:bg-primary/20 transition-colors font-medium"
                                            >
                                                Add to Library
                                            </button>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}

                {/* Add to List Modal */}
                {selectedBook && (
                    <AddToListModal
                        isOpen={addToListModalOpen}
                        onClose={() => {
                            setAddToListModalOpen(false);
                            setSelectedBook(null);
                        }}
                        bookTitle={selectedBook.title}
                        publicationId={selectedBook.id}
                    />
                )}

                {/* Publication Detail Modal */}
                {detailPub && (
                    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                        <div className="bg-white rounded-2xl max-w-lg w-full p-8 shadow-2xl">
                            <div className="flex justify-between items-start mb-6">
                                <h2 className="font-serif text-2xl text-foreground">{detailPub.title}</h2>
                                <button
                                    onClick={() => setDetailPub(null)}
                                    className="p-2 hover:bg-muted rounded-lg transition-colors"
                                >
                                    <X className="w-5 h-5" />
                                </button>
                            </div>

                            <div className="space-y-1 text-sm">
                                {[
                                    ["Author", detailPub.author],
                                    ["Genre", detailPub.genre],
                                    ["Condition", detailPub.condition],
                                    ["Language", detailPub.language],
                                    ["ISBN", detailPub.isbn],
                                    ["Type", getType(detailPub.type)],
                                    ["Status", detailPub.inLibrary ? "In Library" : "Not in Library"],
                                    detailPub.collectionId && ["List", getCollectionName(detailPub.collectionId)],
                                    detailPub.price != null && ["Price", `€${detailPub.price}`],
                                ].filter(Boolean).map(([label, value]) => value ? (
                                    <div key={label} className="flex justify-between py-2 border-b border-border">
                                        <span className="text-muted-foreground font-medium">{label}</span>
                                        <span className="text-foreground">{value}</span>
                                    </div>
                                ) : null)}
                            </div>

                            <button
                                onClick={() => setDetailPub(null)}
                                className="w-full mt-6 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                            >
                                Close
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}