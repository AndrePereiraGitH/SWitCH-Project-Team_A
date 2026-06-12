import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Plus, Filter, Grid3x3, List as ListIcon, Library as LibraryIcon, X } from "lucide-react";
import { BookCard } from "../components/BookCard";
import { usePublications } from "../components/PublicationsContext";
import { useLibrary } from "../components/LibraryContext";
import { useLists } from "../components/ListsContext";

const LANGUAGE_DISPLAY = {
  en: "English", pt: "Portuguese", es: "Spanish",
  fr: "French", de: "German", it: "Italian",
};

export function Library() {
  const navigate = useNavigate();
  const { publications, addToLibrary } = usePublications();
  const { library, loading, error } = useLibrary();
  const { lists } = useLists();

  const [viewMode, setViewMode] = useState("grid");
  const [filterGenre, setFilterGenre] = useState("all");
  const [filterType, setFilterType] = useState("all");
  const [showManageModal, setShowManageModal] = useState(false);
  const [detailPub, setDetailPub] = useState(null);

  const libraryPublications = publications.filter(p => p.libraryId != null);
  const notInLibrary = publications.filter(p => p.libraryId == null);

  const genres = ["all", ...Array.from(new Set(libraryPublications.map(b => b.genre).filter(Boolean)))];
  const filteredBooks = libraryPublications.filter(b => {
    const matchesGenre = filterGenre === "all" || b.genre === filterGenre;
    const matchesType = filterType === "all" || b.type === filterType;
    return matchesGenre && matchesType;
  });

  const getCollectionName = (collectionId) => {
    if (!collectionId) return null;
    return lists.find(l => l.collectionId === collectionId)?.collectionName ?? collectionId;
  };

  const getType = (type) => {
    if (type === "book") return "Book";
    if (type === "magazine") return "Magazine";
    return "Book"; // default
  };

  if (loading) {
    return (
        <div className="min-h-screen bg-background pt-24 pb-12 flex items-center justify-center">
          <p className="text-muted-foreground">Loading library…</p>
        </div>
    );
  }

  if (!library) {
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
            <div className="max-w-2xl mx-auto">
              <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-lg">
                <div className="w-20 h-20 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-6">
                  <LibraryIcon className="w-10 h-10 text-primary" />
                </div>
                <h1 className="font-serif text-3xl mb-3 text-foreground">No Library Found</h1>
                <p className="text-muted-foreground mb-8 text-lg">
                  You don't have a library yet. Add a publication to your library from My Publications.
                </p>
                <button
                    onClick={() => navigate("/my-publications")}
                    className="inline-flex items-center gap-2 px-8 py-4 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold shadow-lg text-lg"
                >
                  Go to My Publications
                </button>
              </div>
            </div>
          </div>
        </div>
    );
  }

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
            <div className="flex-1">
              <div className="flex items-center gap-3 mb-2">
                <LibraryIcon className="w-8 h-8 text-primary" />
                <h1 className="font-serif text-4xl text-foreground">{library.libraryName}</h1>
              </div>
              <p className="text-muted-foreground">
                {libraryPublications.length}{" "}
                {libraryPublications.length === 1 ? "publication" : "publications"}
              </p>
            </div>
            <button
                onClick={() => setShowManageModal(true)}
                className="flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold shadow-lg"
            >
              <Plus className="w-5 h-5" />
              Add Publications
            </button>
          </div>

          {error && (
              <div className="bg-destructive/10 border-2 border-destructive text-destructive rounded-2xl p-4 mb-6">
                {error}
              </div>
          )}

          {/* Filters */}
          {libraryPublications.length > 0 && (
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
                    >
                      <Grid3x3 className="w-5 h-5" />
                    </button>
                    <button
                        onClick={() => setViewMode("list")}
                        className={`p-2 rounded-lg transition-colors ${
                            viewMode === "list" ? "text-primary" : "text-muted-foreground hover:text-primary hover:bg-gray-100"
                        }`}
                    >
                      <ListIcon className="w-5 h-5" />
                    </button>
                  </div>
                </div>
              </div>
          )}

          {/* Books Display */}
          {filteredBooks.length === 0 ? (
              <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                <LibraryIcon className="w-16 h-16 mx-auto mb-4 text-muted-foreground opacity-50" />
                <p className="text-muted-foreground mb-4">
                  {libraryPublications.length === 0
                      ? "No publications in library yet"
                      : "No publications match these filters"}
                </p>
                {libraryPublications.length === 0 && (
                    <button
                        onClick={() => setShowManageModal(true)}
                        className="inline-flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                    >
                      <Plus className="w-5 h-5" />
                      Add Publications
                    </button>
                )}
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
                          <button
                              onClick={() => navigate(`/sell/${book.id}`)}
                              className="w-full px-4 py-2 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold mt-3"
                          >
                              Sell
                          </button>
                      </div>
                  ))}
              </div>
          ) : (
              <div className="space-y-4">
                  {filteredBooks.map(book => (
                      <div
                          key={book.id}
                          onClick={() => setDetailPub(book)}
                          className="bg-white border-2 border-border rounded-2xl p-6 hover:shadow-lg transition-shadow cursor-pointer"
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
                                  </div>
                              </div>
                              <button
                                  onClick={(e) => { e.stopPropagation(); navigate(`/sell/${book.id}`); }}
                                  className="ml-4 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                              >
                                  Sell
                              </button>
                          </div>
                      </div>
                  ))}
              </div>
          )}

          {/* Add Publications Modal */}
          {showManageModal && (
              <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                <div className="bg-white rounded-2xl max-w-4xl w-full p-8 shadow-2xl max-h-[80vh] overflow-y-auto">
                  <h2 className="font-serif text-2xl mb-4 text-foreground">Add Publications to Library</h2>
                  <p className="text-muted-foreground mb-6">Select publications to add to your library</p>
                  {notInLibrary.length === 0 ? (
                      <div className="text-center py-8">
                        <p className="text-muted-foreground">All your publications are already in the library.</p>
                      </div>
                  ) : (
                      <div className="space-y-3 mb-6">
                        {notInLibrary.map(pub => (
                            <div
                                key={pub.id}
                                className="flex justify-between items-center p-4 border-2 border-border rounded-xl hover:border-primary/50 transition-all"
                            >
                              <div>
                                <h3 className="font-semibold text-foreground">{pub.title}</h3>
                                <p className="text-sm text-muted-foreground">{pub.author}</p>
                                {pub.genre && (
                                    <span className="text-xs px-2 py-1 bg-primary/10 text-primary rounded-full">
                                                        {pub.genre}
                                                    </span>
                                )}
                              </div>
                              <button
                                  onClick={async () => {
                                    try { await addToLibrary(pub.id); }
                                    catch (e) { alert("Failed: " + e.message); }
                                  }}
                                  className="px-6 py-2 rounded-xl transition-colors font-semibold bg-primary text-primary-foreground hover:bg-primary/90"
                              >
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

          {/* Publication Detail Modal */}
            {detailPub && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-2xl max-w-lg w-full p-8 shadow-2xl">
                        <div className="flex justify-between items-start mb-6">
                            <h2 className="font-serif text-2xl text-foreground">{detailPub.title}</h2>
                            <button onClick={() => setDetailPub(null)} className="p-2 hover:bg-muted rounded-lg transition-colors">
                                <X className="w-5 h-5" />
                            </button>
                        </div>
                        <div className="space-y-1 text-sm">
                            {[
                                ["Author", detailPub.authors?.join(", ") ?? detailPub.author],
                                ["Genre", detailPub.genres?.join(", ") ?? detailPub.genre],
                                ["Condition", detailPub.condition],
                                ["Language", LANGUAGE_DISPLAY[detailPub.language] ?? detailPub.language],
                                ["Code", detailPub.isbn ?? detailPub.code ?? null],
                                ["Type", getType(detailPub.type)],
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
                            onClick={() => navigate(`/sell/${detailPub.id}`)}
                            className="w-full mt-6 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                        >
                            Sell
                        </button>
                        <button
                            onClick={() => setDetailPub(null)}
                            className="w-full mt-2 px-6 py-3 bg-muted rounded-xl hover:bg-muted/80 transition-colors font-semibold"
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