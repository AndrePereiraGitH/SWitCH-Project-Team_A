import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, X } from "lucide-react";
import { api } from "../../api/client";
import { BookCard } from "../components/BookCard";

const CURRENT_USER_ID = "user-001";

const LANGUAGE_DISPLAY = {
    en: "English", pt: "Portuguese", es: "Spanish",
    fr: "French", de: "German", it: "Italian",
};

/**
 * SharedListDetail page component.
 *
 * Displays the publications inside a specific publicly shared collection.
 * Reads the collection ID from the URL parameter and fetches the publications
 * from the backend. Shows an error banner if the collection is not public or
 * does not exist. Opens a detail modal when a publication card is clicked.
 *
 * Endpoint used:
 * - GET /publicationSamples/{collectionId}/publication-sample?userId={userId}
 *
 * @component
 * @returns {JSX.Element} The shared list detail page.
 */


export function SharedListDetail() {
    const navigate = useNavigate();
    const { listId } = useParams();

    const [publications, setPublications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [detailPub, setDetailPub] = useState(null);

    useEffect(() => {
        async function load() {
            try {
                setLoading(true);
                const data = await api.get(
                    `/publicationSamples/${listId}/publication-sample?userId=${CURRENT_USER_ID}`
                );
                setPublications(data ?? []);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        }
        load();
    }, [listId]);

    return (
        <div className="min-h-screen bg-background pt-24 pb-12">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <button
                    onClick={() => navigate("/shared-lists")}
                    className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-4 transition-colors font-medium"
                >
                    <ArrowLeft className="w-4 h-4" />
                    Shared Lists
                </button>
                <p className="text-muted-foreground text-sm mb-6">US066: List items in a shared list</p>

                {error && (
                    <div className="bg-destructive/10 border-2 border-destructive text-destructive rounded-2xl p-4 mb-6">
                        Could not load publications: {error}
                    </div>
                )}

                <div className="mb-8">
                    <div className="flex items-center gap-4 text-muted-foreground">
                        <span>
                            {publications.length}{" "}
                            {publications.length === 1 ? "publication" : "publications"}
                        </span>
                    </div>
                </div>

                {loading ? (
                    <div className="text-center text-muted-foreground py-12">Loading publications…</div>
                ) : publications.length === 0 ? (
                    <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                        <p className="text-muted-foreground">No publications in this list.</p>
                    </div>
                ) : (
                    <div className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {publications.map(pub => (
                            <div key={pub.id} onClick={() => setDetailPub(pub)} className="cursor-pointer">
                                <BookCard
                                    title={pub.title}
                                    author={pub.authors?.join(", ")}
                                    genre={pub.genres?.join(", ")}
                                    condition={pub.condition}
                                />
                            </div>
                        ))}
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
                                    ["Author", detailPub.authors?.join(", ")],
                                    ["Genre", detailPub.genres?.join(", ")],
                                    ["Condition", detailPub.condition],
                                    ["Language", LANGUAGE_DISPLAY[detailPub.language] ?? detailPub.language],
                                    ["Code", detailPub.code],
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