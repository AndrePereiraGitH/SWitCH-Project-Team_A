import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Save, BookOpen, X } from "lucide-react";
import { SuccessModal } from "../components/SuccessModal";
import { usePublications } from "../components/PublicationsContext";
import { API_BASE_URL } from "../../api/constants";

const CURRENT_USER_ID = "user-001";

const PUBLICATION_STATUSES = [
    { value: "CREATED", label: "Created" },
    { value: "READING", label: "Reading" },
    { value: "READ", label: "Read" },
];

const CONDITION_MAP = {
    New:        "New",
    likeNew:    "Like New",
    veryGood:   "Very Good",
    good:       "Good",
    acceptable: "Acceptable",
    poor:       "Poor",
};

const BINDING_MAP = {
    hardcover: "HARDBACK",
    paperback: "PAPERBACK",
};

const LANGUAGE_MAP = {
    English:    "en",
    Portuguese: "pt",
    Spanish:    "es",
    French:     "fr",
    German:     "de",
};

// TODO: fetch from /publication-types instead of hardcoding
const PUBLICATION_TYPE_IDS = {
    book:     "pub-type-001",
    magazine: "pub-type-002",
};

const MEDIA_FORMAT_MAP = {
    book:     "Book",
    magazine: "Magazine",
};

export function AddPublication() {
    const navigate = useNavigate();
    const { addPublication } = usePublications();

    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [publishers, setPublishers] = useState([]);
    const [genres, setGenres] = useState([]);
    const [authorsList, setAuthorsList] = useState([]);
    const [loadingOptions, setLoadingOptions] = useState(true);

    const [authorsDropdownOpen, setAuthorsDropdownOpen] = useState(false);
    const authorsDropdownRef = useRef(null);

    const [formData, setFormData] = useState({
        isbn: "",
        title: "",
        authors: [],
        genres: [],
        publicationType: "book",
        publisher: "",
        publishedDate: "",
        edition: "",
        language: "English",
        pages: "",
        binding: "",
        condition: "good",
        status: "CREATED",
        description: "",
        dimensions: "",
        weight: "",
    });

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (authorsDropdownRef.current && !authorsDropdownRef.current.contains(e.target)) {
                setAuthorsDropdownOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    useEffect(() => {
        async function fetchOptions() {
            try {
                const [publishersRes, genresRes, authorsRes] = await Promise.all([
                    fetch(`${API_BASE_URL}/publishers`),
                    fetch(`${API_BASE_URL}/genres`),
                    fetch(`${API_BASE_URL}/authors?userId=${CURRENT_USER_ID}`),
                ]);
                if (!publishersRes.ok || !genresRes.ok || !authorsRes.ok) {
                    throw new Error("Failed to fetch options");
                }
                const publishersData = await publishersRes.json();
                const genresData     = await genresRes.json();
                const authorsData    = await authorsRes.json();
                setPublishers(publishersData);
                setGenres(genresData);
                setAuthorsList(authorsData);
            } catch (error) {
                console.error("Error loading options:", error);
            } finally {
                setLoadingOptions(false);
            }
        }
        fetchOptions();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        // ── Validation ───────────────────────────────────────────────
        const pubYear = formData.publishedDate
            ? new Date(formData.publishedDate).getFullYear()
            : null;
        if (pubYear && pubYear > 1976 && !formData.isbn) {
            alert("ISBN/ISSN is required for publications after 1976");
            return;
        }

        if (formData.authors.length === 0) {
            alert("Please select at least one author");
            return;
        }

        if (formData.genres.length === 0) {
            alert("Please select at least one genre");
            return;
        }

        const cleanIsbn = formData.isbn ? formData.isbn.replace(/[-\s]/g, "") : "";
        let isbn10 = null, isbn13 = null, issn = null;
        if (cleanIsbn) {
            if (cleanIsbn.length === 8)       issn   = cleanIsbn;
            else if (cleanIsbn.length === 10) isbn10 = cleanIsbn;
            else if (cleanIsbn.length === 13) isbn13 = cleanIsbn;
            else { alert("ISBN must be 10 or 13 chars, or ISSN 8 chars"); return; }
        }

        if (!CONDITION_MAP[formData.condition]) {
            alert(`Unknown condition: ${formData.condition}`); return;
        }
        if (!BINDING_MAP[formData.binding]) {
            alert("Please select a binding type"); return;
        }

        // Parse dimensions "20 x 15 x 3" → width / height / depth
        const dims = formData.dimensions
            .split(/\s*x\s*/i)
            .map((s) => parseFloat(s));
        if (dims.length !== 3 || dims.some(isNaN) || dims.some((d) => d <= 0)) {
            alert('Dimensions must be in format "W x H x D" with positive numbers, e.g. "20 x 15 x 3"');
            return;
        }
        const [width, height, depth] = dims;

        const numberOfPages = parseInt(formData.pages, 10);
        if (!numberOfPages || numberOfPages <= 0) {
            alert("Number of pages is required and must be positive"); return;
        }
        const weight = parseFloat(formData.weight);
        if (!weight || weight <= 0) {
            alert("Weight is required and must be positive"); return;
        }

        // ── Resolve display labels ───────────────────────────────────
        const selectedPublisher = publishers.find(p => p.publisherId === formData.publisher);
        const selectedAuthors   = authorsList.filter(a => formData.authors.includes(a.id));
        const selectedGenres    = genres.filter(g => formData.genres.includes(g.id));
        const editionLanguage   = LANGUAGE_MAP[formData.language] || "en";

        // ── Build payload ────────────────────────────────────────────
        const payload = {
            title: formData.title,
            authorNames: formData.authors,
            genreNames:  formData.genres,
            originalLanguage: editionLanguage,
            publishedDate: formData.publishedDate,

            // Edition
            editionNumber: formData.edition,
            isbn10, isbn13, issn,
            height, width, depth, weight, numberOfPages,
            publicationTypeId: PUBLICATION_TYPE_IDS[formData.publicationType],
            publisherId: formData.publisher,
            mediaFormat: MEDIA_FORMAT_MAP[formData.publicationType],
            editionLanguage,
            typeOfBinding: BINDING_MAP[formData.binding],

            // Sample
            condition: CONDITION_MAP[formData.condition],

            // Display-only (used by MyPublications)
            publisherName: selectedPublisher?.name ?? "",
            authorDisplayNames: selectedAuthors.map(a => a.name),
            genreDisplayNames:  selectedGenres.map(g => g.genreName),
        };

        try {
            await addPublication(payload);
            setShowSuccessModal(true);
        } catch (err) {
            console.error("[submit] failed:", err);
            alert("Failed to create publication: " + err.message);
        }
    };

    const handleAuthorToggle = (authorId) => {
        setFormData(prev => ({
            ...prev,
            authors: prev.authors.includes(authorId)
                ? prev.authors.filter(a => a !== authorId)
                : [...prev.authors, authorId],
        }));
    };

    const handleGenreToggle = (genreId) => {
        setFormData(prev => ({
            ...prev,
            genres: prev.genres.includes(genreId)
                ? prev.genres.filter(g => g !== genreId)
                : [...prev.genres, genreId],
        }));
    };

    const handleChange = (e) => {
        setFormData(prev => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
    };

    return (
        <div className="min-h-screen bg-background pt-24 pb-12">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                <button
                    onClick={() => navigate(-1)}
                    className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
                >
                    <ArrowLeft className="w-4 h-4" />
                    Back
                </button>

                <div className="bg-white border-2 border-border rounded-2xl p-8 shadow-lg">
                    <div className="flex items-center gap-4 mb-8">
                        <div className="p-4 bg-primary/10 rounded-xl">
                            <BookOpen className="w-8 h-8 text-primary" />
                        </div>
                        <div>
                            <h1 className="font-serif text-4xl text-foreground">Add Publication</h1>
                            <p className="text-muted-foreground">US039: Register new publication in library</p>
                        </div>
                    </div>

                    <form onSubmit={handleSubmit} className="space-y-8">
                        {/* Identification */}
                        <div>
                            <h2 className="font-serif text-2xl mb-6 pb-3 border-b-2 border-border text-foreground">
                                Identification
                            </h2>
                            <div className="grid md:grid-cols-2 gap-6">
                                <div>
                                    <label htmlFor="isbn" className="block mb-2 font-semibold text-foreground">
                                        ISBN / ISSN
                                        {formData.publishedDate && new Date(formData.publishedDate).getFullYear() > 1976 && " *"}
                                    </label>
                                    <input
                                        id="isbn"
                                        type="text"
                                        name="isbn"
                                        value={formData.isbn}
                                        onChange={handleChange}
                                        placeholder="978-0-123456-78-9"
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                    />
                                    <p className="text-xs text-muted-foreground mt-2">
                                        ISBN-10 (books since 1970), ISBN-13 (since 2007) or ISSN (magazines since 1976). Optional for older publications.
                                    </p>
                                </div>

                                <div>
                                    <label htmlFor="publicationType" className="block mb-2 font-semibold text-foreground">Publication Type *</label>
                                    <select
                                        id="publicationType"
                                        name="publicationType"
                                        value={formData.publicationType}
                                        onChange={handleChange}
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        required
                                    >
                                        <option value="book">Book</option>
                                        <option value="magazine">Magazine</option>
                                    </select>
                                </div>

                                <div>
                                    <label className="block mb-2 font-semibold text-foreground">Genre *</label>
                                    <div className="grid grid-cols-2 gap-2 p-4 bg-input-background border-2 border-border rounded-xl">
                                        {genres.map(genre => (
                                            <label key={genre.id} className="flex items-center gap-2 cursor-pointer">
                                                <input
                                                    type="checkbox"
                                                    checked={formData.genres.includes(genre.id)}
                                                    onChange={() => handleGenreToggle(genre.id)}
                                                    className="w-4 h-4 text-primary rounded focus:ring-2 focus:ring-primary"
                                                />
                                                <span className="text-sm">{genre.genreName}</span>
                                            </label>
                                        ))}
                                    </div>
                                    {formData.genres.length === 0 && (
                                        <p className="text-xs text-destructive mt-1">Please select at least one genre</p>
                                    )}
                                </div>

                                <div>
                                    <label htmlFor="status" className="block mb-2 font-semibold text-foreground">Publication Status *</label>
                                    <select
                                        id="status"
                                        name="status"
                                        value={formData.status}
                                        onChange={handleChange}
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        required
                                    >
                                        {PUBLICATION_STATUSES.map(s => (
                                            <option key={s.value} value={s.value}>{s.label}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                        </div>

                        {/* Basic Info */}
                        <div>
                            <h2 className="font-serif text-2xl mb-6 pb-3 border-b-2 border-border text-foreground">
                                Basic Information
                            </h2>
                            <div className="space-y-6">
                                <div>
                                    <label htmlFor="title" className="block mb-2 font-semibold text-foreground">Title *</label>
                                    <input
                                        id="title"
                                        type="text"
                                        name="title"
                                        value={formData.title}
                                        onChange={handleChange}
                                        placeholder="Publication title"
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        required
                                    />
                                </div>

                                <div>
                                    <label className="block mb-2 font-semibold text-foreground">Author(s) *</label>
                                    <div className="relative" ref={authorsDropdownRef}>
                                        {/* Trigger */}
                                        <button
                                            type="button"
                                            onClick={() => setAuthorsDropdownOpen(prev => !prev)}
                                            className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary text-left flex justify-between items-center transition-all"
                                        >
                                            <span className={formData.authors.length === 0 ? "text-muted-foreground" : "text-foreground"}>
                                                {formData.authors.length === 0
                                                    ? "Select author(s)"
                                                    : authorsList
                                                        .filter(a => formData.authors.includes(a.id))
                                                        .map(a => a.name)
                                                        .join(", ")
                                                }
                                            </span>
                                            <svg
                                                className={`w-4 h-4 text-muted-foreground transition-transform ${authorsDropdownOpen ? "rotate-180" : ""}`}
                                                fill="none" stroke="currentColor" viewBox="0 0 24 24"
                                            >
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                                            </svg>
                                        </button>

                                        {/* Dropdown */}
                                        {authorsDropdownOpen && (
                                            <div className="absolute z-10 w-full mt-1 bg-white border-2 border-border rounded-xl shadow-lg max-h-64 overflow-y-auto">
                                                {authorsList.map(a => (
                                                    <label
                                                        key={a.id}
                                                        className={`flex items-center gap-3 px-4 py-3 hover:bg-primary/5 cursor-pointer transition-colors ${
                                                            formData.authors.includes(a.id) ? "bg-primary/10" : ""
                                                        }`}
                                                    >
                                                        <input
                                                            type="checkbox"
                                                            checked={formData.authors.includes(a.id)}
                                                            onChange={() => handleAuthorToggle(a.id)}
                                                            className="w-4 h-4 text-primary rounded focus:ring-2 focus:ring-primary"
                                                        />
                                                        <div>
                                                            <p className="text-sm font-medium text-foreground">{a.name}</p>
                                                            {a.dateOfBirth && (
                                                                <p className="text-xs text-muted-foreground">{a.dateOfBirth.slice(0, 4)}</p>
                                                            )}
                                                        </div>
                                                    </label>
                                                ))}
                                            </div>
                                        )}
                                    </div>

                                    {/* Selected tags */}
                                    {formData.authors.length > 0 && (
                                        <div className="flex flex-wrap gap-2 mt-2">
                                            {authorsList
                                                .filter(a => formData.authors.includes(a.id))
                                                .map(a => (
                                                    <span
                                                        key={a.id}
                                                        className="flex items-center gap-1 px-3 py-1 bg-primary/10 text-primary rounded-full text-sm font-medium"
                                                    >
                                                        {a.name}
                                                        <button
                                                            type="button"
                                                            onClick={() => handleAuthorToggle(a.id)}
                                                            className="hover:text-destructive transition-colors ml-1"
                                                        >
                                                            <X className="w-3 h-3" />
                                                        </button>
                                                    </span>
                                                ))
                                            }
                                        </div>
                                    )}

                                    {formData.authors.length === 0 && (
                                        <p className="text-xs text-destructive mt-1">Please select at least one author</p>
                                    )}
                                </div>

                                <div className="grid md:grid-cols-2 gap-6">
                                    <div>
                                        <label htmlFor="publisher" className="block mb-2 font-semibold text-foreground">Publisher *</label>
                                        <select
                                            id="publisher"
                                            name="publisher"
                                            value={formData.publisher}
                                            onChange={handleChange}
                                            className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                            required
                                        >
                                            <option value="">Select publisher</option>
                                            {publishers.map(pub => (
                                                <option key={pub.publisherId} value={pub.publisherId}>{pub.name}</option>
                                            ))}
                                        </select>
                                    </div>
                                </div>

                                <div className="grid md:grid-cols-3 gap-6">
                                    <div>
                                        <label htmlFor="publishedDate" className="block mb-2 font-semibold text-foreground">Published Date *</label>
                                        <input
                                            id="publishedDate"
                                            type="date"
                                            name="publishedDate"
                                            value={formData.publishedDate}
                                            onChange={handleChange}
                                            className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                            required
                                        />
                                    </div>

                                    <div>
                                        <label htmlFor="edition" className="block mb-2 font-semibold text-foreground">Edition *</label>
                                        <input
                                            id="edition"
                                            type="text"
                                            name="edition"
                                            value={formData.edition}
                                            onChange={handleChange}
                                            placeholder="1st edition"
                                            className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                            required
                                        />
                                    </div>

                                    <div>
                                        <label htmlFor="language" className="block mb-2 font-semibold text-foreground">Language *</label>
                                        <input
                                            id="language"
                                            type="text"
                                            name="language"
                                            value={formData.language}
                                            onChange={handleChange}
                                            className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                            required
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* Physical Details */}
                        <div>
                            <h2 className="font-serif text-2xl mb-6 pb-3 border-b-2 border-border text-foreground">
                                Physical Details
                            </h2>
                            <div className="grid md:grid-cols-3 gap-6 mb-6">
                                <div>
                                    <label htmlFor="pages" className="block mb-2 font-semibold text-foreground">Number of Pages *</label>
                                    <input
                                        id="pages"
                                        type="number"
                                        name="pages"
                                        value={formData.pages}
                                        onChange={handleChange}
                                        placeholder="350"
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        required
                                    />
                                </div>

                                <div>
                                    <label htmlFor="binding" className="block mb-2 font-semibold text-foreground">Binding Type *</label>
                                    <select
                                        id="binding"
                                        name="binding"
                                        value={formData.binding}
                                        onChange={handleChange}
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        required
                                    >
                                        <option value="">Select binding type</option>
                                        <option value="hardcover">Hardcover</option>
                                        <option value="paperback">Paperback</option>
                                    </select>
                                </div>

                                <div>
                                    <label htmlFor="condition" className="block mb-2 font-semibold text-foreground">Condition *</label>
                                    <select
                                        id="condition"
                                        name="condition"
                                        value={formData.condition}
                                        onChange={handleChange}
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        required
                                    >
                                        <option value="New">New</option>
                                        <option value="likeNew">Like New</option>
                                        <option value="veryGood">Very Good</option>
                                        <option value="good">Good</option>
                                        <option value="acceptable">Acceptable</option>
                                        <option value="poor">Poor</option>
                                    </select>
                                </div>
                            </div>

                            <div className="grid md:grid-cols-2 gap-6">
                                <div>
                                    <label htmlFor="dimensions" className="block mb-2 font-semibold text-foreground">Dimensions (W x H x D) cm *</label>
                                    <input
                                        id="dimensions"
                                        type="text"
                                        name="dimensions"
                                        value={formData.dimensions}
                                        onChange={handleChange}
                                        placeholder="20 x 15 x 3"
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        required
                                    />
                                    <p className="text-xs text-muted-foreground mt-1">Width x Height x Depth</p>
                                </div>

                                <div>
                                    <label htmlFor="weight" className="block mb-2 font-semibold text-foreground">Weight (g) *</label>
                                    <input
                                        id="weight"
                                        type="number"
                                        name="weight"
                                        value={formData.weight}
                                        onChange={handleChange}
                                        placeholder="350"
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        required
                                    />
                                </div>
                            </div>
                        </div>

                        {/* Description */}
                        <div>
                            <label htmlFor="description" className="block mb-2 font-semibold text-foreground">Description</label>
                            <textarea
                                id="description"
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                placeholder="Add additional details about condition, notes, etc."
                                rows={4}
                                className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent resize-none transition-all"
                            />
                        </div>

                        {/* Actions */}
                        <div className="flex gap-4 pt-6">
                            <button
                                type="submit"
                                disabled={loadingOptions}
                                className="flex items-center gap-2 px-8 py-4 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold shadow-lg disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                <Save className="w-5 h-5" />
                                Create Publication
                            </button>
                            <button
                                type="button"
                                onClick={() => navigate(-1)}
                                className="px-8 py-4 bg-white border-2 border-border rounded-xl hover:bg-muted transition-colors font-semibold"
                            >
                                Cancel
                            </button>
                        </div>
                    </form>
                </div>

                <SuccessModal
                    isOpen={showSuccessModal}
                    onClose={() => {
                        setShowSuccessModal(false);
                        navigate("/my-publications");
                    }}
                    message="Publication registered successfully!"
                />
            </div>
        </div>
    );
}