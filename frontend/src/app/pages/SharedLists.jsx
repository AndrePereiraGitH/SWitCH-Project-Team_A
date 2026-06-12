import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, BookOpen } from "lucide-react";
import { api } from "../../api/client";

const API_BASE_URL = "http://localhost:8081";

/**
 * SharedLists page component.
 *
 * Displays all publicly shared publication collections retrieved from the backend.
 * Allows the user to filter collections by genre.
 * Navigates to {@link SharedListDetail} when a collection card is clicked.
 *
 * Endpoints used:
 * - GET /collection-of-publications/public/all
 * - GET /collection-of-publications/public/genre/{genreId}
 *
 * @component
 * @returns {JSX.Element} The shared lists discovery page.
 */

export function SharedLists() {
    const navigate = useNavigate();

    const [lists, setLists] = useState([]);
    const [genres, setGenres] = useState([]);
    const [selectedGenreId, setSelectedGenreId] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch(`${API_BASE_URL}/genres`)
            .then(r => r.json())
            .then(setGenres)
            .catch(console.error);
    }, []);

    useEffect(() => {
        async function load() {
            try {
                setLoading(true);
                const url = selectedGenreId
                    ? `/collection-of-publications/public/genre/${selectedGenreId}`
                    : `/collection-of-publications/public/all`;
                const data = await api.get(url);
                setLists(data ?? []);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        }
        load();
    }, [selectedGenreId]);

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
                        <h1 className="font-serif text-4xl mb-2 text-foreground">Shared Lists</h1>
                        <p className="text-muted-foreground">Browse publication lists shared by the community</p>
                    </div>

                    <select
                        value={selectedGenreId}
                        onChange={e => setSelectedGenreId(e.target.value)}
                        className="px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary text-sm"
                    >
                        <option value="">All Genres</option>
                        {genres.map(g => (
                            <option key={g.id} value={g.id}>{g.genreName}</option>
                        ))}
                    </select>
                </div>

                {error && (
                    <div className="bg-destructive/10 border-2 border-destructive text-destructive rounded-2xl p-4 mb-6">
                        Could not load lists: {error}
                    </div>
                )}

                {loading ? (
                    <div className="text-center text-muted-foreground py-12">Loading shared lists…</div>
                ) : lists.length === 0 ? (
                    <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                        <p className="text-muted-foreground">No public lists found.</p>
                    </div>
                ) : (
                    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {lists.map(list => (
                            <div
                                key={list.collectionId}
                                onClick={() => navigate(`/shared-lists/${list.collectionId}`)}
                                className="bg-white border-2 border-border rounded-2xl p-6 hover:shadow-xl hover:border-primary/50 transition-all cursor-pointer"
                            >
                                <h3 className="font-serif text-xl mb-2 text-foreground">{list.collectionName}</h3>
                                <p className="text-sm text-muted-foreground mb-4">by {list.ownerName}</p>
                                {list.publicUntil && (
                                    <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                        <BookOpen className="w-4 h-4" />
                                        <span>Shared until {list.publicUntil}</span>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
