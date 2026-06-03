import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Save, BookOpen, Plus, X } from "lucide-react";
import { SuccessModal } from "../components/SuccessModal";
import { usePublications } from "../components/PublicationsContext";


const PUBLICATION_STATUSES = [
    { value: "CREATED", label: "Created" },
    { value: "READING", label: "Reading" },
    { value: "READ", label: "Read" },
];

export function AddPublication() {
    const navigate = useNavigate();
    const { addPublication } = usePublications();
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [authors, setAuthors] = useState([""]);
    const [formData, setFormData] = useState({
        isbn: "",
        title: "",
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
        weight: ""
    });
    
    const [publishers, setPublishers] = useState([]);
    const [genres, setGenres] = useState([]);
    const [loadingOptions, setLoadingOptions] = useState(true);


useEffect(() => {
    async function fetchOptions() {
        try {
            const [publishersRes, genresRes] = await Promise.all([
                fetch("http://localhost:8081/publishers"),
                fetch("http://localhost:8081/genres")
            ]);

            if (!publishersRes.ok || !genresRes.ok) {
                throw new Error("Failed to fetch options");
            }

            const publishersData = await publishersRes.json();
            const genresData = await genresRes.json();

            setPublishers(publishersData);
            setGenres(genresData);
        } catch (error) {
            console.error("Error loading publishers/genres:", error);
        } finally {
            setLoadingOptions(false);
        }
    }

    fetchOptions();
}, []);
    const handleSubmit = (e) => {
        e.preventDefault();

        const pubYear = formData.publishedDate ? new Date(formData.publishedDate).getFullYear() : null;
        if (pubYear && pubYear > 1976 && !formData.isbn) {
            alert("ISBN/ISSN is required for publications after 1976");
            return;
        }

        if (formData.isbn) {
            const cleanIsbn = formData.isbn.replace(/[-\s]/g, '');
            const isIsbn10 = cleanIsbn.length === 10;
            const isIsbn13 = cleanIsbn.length === 13;
            const isIssn = cleanIsbn.length === 8;

            if (!isIsbn10 && !isIsbn13 && !isIssn) {
                alert("ISBN must have 10 or 13 characters, or ISSN must have 8 characters");
                return;
            }
        }

        addPublication({
            title: formData.title,
            author: authors.filter(a => a.trim()).join(", "),
            genres: formData.genres,
            isbn: formData.isbn,
            condition: formData.condition,
            type: formData.publicationType,
            publisher: formData.publisher,
            status: formData.status,
        });

        setShowSuccessModal(true);
    };

    const handleGenreToggle = (genre) => {
        setFormData(prev => ({
            ...prev,
            genres: prev.genres.includes(genre)
                ? prev.genres.filter(g => g !== genre)
                : [...prev.genres, genre]
        }));
    };

    const handleChange = (e) => {
        setFormData(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const addAuthor = () => {
        setAuthors([...authors, ""]);
    };

    const removeAuthor = (index) => {
        if (authors.length > 1) {
            setAuthors(authors.filter((_, i) => i !== index));
        }
    };

    const handleAuthorChange = (index, value) => {
        const newAuthors = [...authors];
        newAuthors[index] = value;
        setAuthors(newAuthors);
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
                        {/* Identification Section */}
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
                                    <div className="space-y-3">
                                        {authors.map((author, index) => (
                                            <div key={index} className="flex gap-2">
                                                <input
                                                    type="text"
                                                    value={author}
                                                    onChange={(e) => handleAuthorChange(index, e.target.value)}
                                                    placeholder="Author name"
                                                    className="flex-1 px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                                    required
                                                />
                                                {authors.length > 1 && (
                                                    <button
                                                        type="button"
                                                        aria-label="Remove author"
                                                        onClick={() => removeAuthor(index)}
                                                        className="px-3 py-3 bg-destructive/10 text-destructive rounded-xl hover:bg-destructive/20 transition-colors"
                                                    >
                                                        <X className="w-5 h-5" />
                                                    </button>
                                                )}
                                            </div>
                                        ))}
                                        <button
                                            type="button"
                                            onClick={addAuthor}
                                            className="flex items-center gap-2 px-4 py-2 bg-primary/10 text-primary rounded-xl hover:bg-primary/20 transition-colors font-medium"
                                        >
                                            <Plus className="w-4 h-4" />
                                            Add Author
                                        </button>
                                    </div>
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
                                                <option key={pub.id} value={pub.id}>{pub.name}</option>
                                            ))}
                                        </select>
                                        <p className="text-xs text-muted-foreground mt-1">
                                        </p>
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
                                    <label htmlFor="pages" className="block mb-2 font-semibold text-foreground">Number of Pages</label>
                                    <input
                                        id="pages"
                                        type="number"
                                        name="pages"
                                        value={formData.pages}
                                        onChange={handleChange}
                                        placeholder="350"
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
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
                                        <option value="softcover">Softcover</option>
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
                                    <label htmlFor="weight" className="block mb-2 font-semibold text-foreground">Weight (g)</label>
                                    <input
                                        id="weight"
                                        type="number"
                                        name="weight"
                                        value={formData.weight}
                                        onChange={handleChange}
                                        placeholder="350"
                                        className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
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
                                className="flex items-center gap-2 px-8 py-4 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold shadow-lg"
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