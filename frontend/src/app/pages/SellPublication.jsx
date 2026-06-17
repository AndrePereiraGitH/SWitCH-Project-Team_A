import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, Euro, Clock, Tag, Upload, X } from "lucide-react";
import { usePublications } from "../components/PublicationsContext";
import { API_BASE_URL , CURRENT_USER_ID } from "../../api/constants";

export function SellPublication() {
    const navigate = useNavigate();
    const { bookId } = useParams();
    const { getAllPublications } = usePublications();

    const [saleType, setSaleType] = useState("direct");
    const [images, setImages] = useState([]);
    const [submitting, setSubmitting] = useState(false);
    const [submitError, setSubmitError] = useState("");

    const [formData, setFormData] = useState({
        price: "",
        startingPrice: "",
        buyoutPrice: "",
        auctionStartDate: "",
        auctionStartTime: "",
        auctionEndDate: "",
        auctionEndTime: "",
        shortDescription: ""
    });

    const minDateTime = new Date();
    minDateTime.setMinutes(minDateTime.getMinutes() + 5);
    const minDate = minDateTime.toISOString().split("T")[0];
    const minTime = minDateTime.toTimeString().slice(0, 5);

    const book = getAllPublications().find(pub => pub.id === bookId);

    if (!book) {
        return (
            <div className="min-h-screen bg-background pt-20 pb-12">
                <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                    <button
                        onClick={() => navigate(-1)}
                        className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6"
                    >
                        <ArrowLeft className="w-4 h-4" />
                        Voltar
                    </button>
                    <div className="bg-white border-2 border-border rounded-2xl p-8 text-center">
                        <p className="text-muted-foreground">Publicação não encontrada</p>
                    </div>
                </div>
            </div>
        );
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        setSubmitError("");

        if (formData.shortDescription.length > 0 && formData.shortDescription.length < 10) {
            setSubmitError("Description must be between 10 and 100 characters.");
            setSubmitting(false);
            return;
        }

        if (saleType === "auction") {
            const start = new Date(`${formData.auctionStartDate}T${formData.auctionStartTime}`);
            const end = new Date(`${formData.auctionEndDate}T${formData.auctionEndTime}`);
            if (start >= end) {
                setSubmitError("Auction end date/time must be after start date/time.");
                setSubmitting(false);
                return;
            }
            if (start <= new Date()) {
                setSubmitError("Auction start date/time must be in the future.");
                setSubmitting(false);
                return;
            }
            if (formData.buyoutPrice && parseFloat(formData.buyoutPrice) <= parseFloat(formData.startingPrice)) {
                setSubmitError("Buyout price must be greater than the starting price.");
                setSubmitting(false);
                return;
            }
        }

        try {
            const price =
                saleType === "direct"
                    ? formData.price
                    : formData.startingPrice;
            const description = formData.shortDescription && formData.shortDescription.length >= 10
                ? formData.shortDescription
                : "No description provided.";
            const body = {
                listingTitle: book.title,
                publicationIds: [bookId],
                saleType: saleType === "direct" ? "Direct Sale" : "Auction",
                price: price.toString(),
                shortDescription: description,
                pictures: [],
                publicationDate: new Date().toISOString().split("T")[0]
            };
            const response = await fetch(
                `${API_BASE_URL}/listings?userId=${CURRENT_USER_ID}`,
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(body)
                }
            );
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || "Failed to create listing");
            }
            const listing = await response.json();
            if (saleType === "auction") {
                const startDateTime = `${formData.auctionStartDate}T${formData.auctionStartTime}:00`;
                const endDateTime = `${formData.auctionEndDate}T${formData.auctionEndTime}:00`;
                const buyoutPrice = formData.buyoutPrice
                    ? parseFloat(formData.buyoutPrice)
                    : null;
                const auctionBody = {
                    listingId: listing.id,
                    startDateTime: startDateTime,
                    endDateTime: endDateTime,
                    initialPrice: parseFloat(formData.startingPrice),
                    ...(buyoutPrice !== null && { buyoutPrice: buyoutPrice })
                };
                const auctionResponse = await fetch(
                    `${API_BASE_URL}/auctions?userId=${CURRENT_USER_ID}`,
                    {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify(auctionBody)
                    }
                );
                if (!auctionResponse.ok) {
                    const errorText = await auctionResponse.text();
                    throw new Error(errorText || "Failed to create auction");
                }
            }
            navigate("/explore");
        } catch (error) {
            setSubmitError(error.message);
        } finally {
            setSubmitting(false);
        }
    };

    const handleChange = (e) => {
        setFormData(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const handleImageUpload = (e) => {
        const files = e.target.files;

        if (files && images.length < 5) {
            Array.from(files).forEach(file => {
                const reader = new FileReader();

                reader.onloadend = () => {
                    setImages(prev => {
                        if (prev.length >= 5) return prev;
                        return [...prev, reader.result];
                    });
                };

                reader.readAsDataURL(file);
            });
        }
    };

    const removeImage = (index) => {
        setImages(prev => prev.filter((_, i) => i !== index));
    };

    return (
        <div className="min-h-screen bg-background pt-20 pb-12">
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
                            <Tag className="w-8 h-8 text-primary" />
                        </div>
                        <div>
                            <h1 className="font-serif text-4xl text-foreground">Put For Sale</h1>
                            <p className="text-muted-foreground">Create listing for this publication</p>
                        </div>
                    </div>

                    <div className="bg-muted p-4 rounded-lg mb-6">
                        <h3 className="font-serif text-lg mb-1">{book.title}</h3>
                        <p className="text-sm text-muted-foreground mb-1">{book.author}</p>
                        <span className="text-xs bg-card px-2 py-1 rounded">
                            Condition: {book.condition}
                        </span>
                    </div>

                    {submitError && (
                        <div className="bg-destructive/10 border-2 border-destructive text-destructive rounded-xl p-4 mb-6">
                            {submitError}
                        </div>
                    )}

                    <div className="mb-8">
                        <label className="block mb-4 font-semibold text-lg">Sale Type</label>
                        <div className="grid md:grid-cols-2 gap-4">
                            <button
                                type="button"
                                onClick={() => setSaleType("direct")}
                                className={`p-6 border-2 rounded-xl text-left transition-all ${
                                    saleType === "direct"
                                        ? "border-primary bg-gradient-to-br from-primary/10 to-primary/5 shadow-lg"
                                        : "border-border hover:border-primary/50 hover:bg-primary/5"
                                }`}
                            >
                                <div className="flex items-center gap-3 mb-3">
                                    <div className={`p-2 rounded-lg ${saleType === "direct" ? "bg-primary/20" : "bg-muted"}`}>
                                        <Euro className="w-6 h-6 text-primary" />
                                    </div>
                                    <span className="font-bold text-lg">Direct Sale</span>
                                </div>
                                <p className="text-sm text-muted-foreground">
                                    Set a fixed price for immediate sale
                                </p>
                            </button>

                            <button
                                type="button"
                                onClick={() => setSaleType("auction")}
                                className={`p-6 border-2 rounded-xl text-left transition-all ${
                                    saleType === "auction"
                                        ? "border-accent bg-gradient-to-br from-accent/10 to-accent/5 shadow-lg"
                                        : "border-border hover:border-accent/50 hover:bg-accent/5"
                                }`}
                            >
                                <div className="flex items-center gap-3 mb-3">
                                    <div className={`p-2 rounded-lg ${saleType === "auction" ? "bg-accent/20" : "bg-muted"}`}>
                                        <Clock className="w-6 h-6 text-accent" />
                                    </div>
                                    <span className="font-bold text-lg">Auction</span>
                                </div>
                                <p className="text-sm text-muted-foreground">
                                    Let buyers make bids
                                </p>
                            </button>
                        </div>
                    </div>

                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div>
                            <label className="block mb-2 font-semibold text-foreground">
                                Description
                            </label>
                            <textarea
                                name="shortDescription"
                                value={formData.shortDescription}
                                onChange={handleChange}
                                placeholder="Describe the condition, edition, or any useful details (min 10 characters)..."
                                rows={4}
                                minLength={10}
                                maxLength={100}
                                className="w-full px-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                            />
                        </div>

                        <div>
                            <label className="block mb-2 font-semibold text-foreground">Upload Images</label>
                            <div className="border-2 border-dashed border-border rounded-xl p-6 text-center hover:border-primary transition-colors">
                                <input
                                    type="file"
                                    accept="image/*"
                                    multiple
                                    onChange={handleImageUpload}
                                    className="hidden"
                                    id="sale-image-upload"
                                    disabled={images.length >= 5}
                                />
                                <label
                                    htmlFor="sale-image-upload"
                                    className="cursor-pointer flex flex-col items-center gap-3"
                                >
                                    <Upload className="w-10 h-10 text-muted-foreground" />
                                    <p className="text-sm text-muted-foreground">
                                        Add up to 5 images of the item ({images.length}/5)
                                    </p>
                                </label>
                            </div>

                            {images.length > 0 && (
                                <div className="grid grid-cols-3 md:grid-cols-5 gap-4 mt-4">
                                    {images.map((img, index) => (
                                        <div key={index} className="relative group">
                                            <img
                                                src={img}
                                                alt={`Upload ${index + 1}`}
                                                className="w-full aspect-square object-cover rounded-lg border-2 border-border"
                                            />
                                            <button
                                                type="button"
                                                onClick={() => removeImage(index)}
                                                className="absolute top-2 right-2 p-1 bg-destructive text-destructive-foreground rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
                                            >
                                                <X className="w-4 h-4" />
                                            </button>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>

                        {saleType === "direct" ? (
                            <div>
                                <label className="block mb-2 font-semibold text-foreground">
                                    Sale Price *
                                </label>
                                <div className="relative">
                                    <Euro className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
                                    <input
                                        type="number"
                                        name="price"
                                        value={formData.price}
                                        onChange={handleChange}
                                        placeholder="25.00"
                                        step="0.01"
                                        min="0"
                                        className="w-full pl-12 pr-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        required
                                    />
                                </div>
                            </div>
                        ) : (
                            <>
                                <div>
                                    <label className="block mb-2 font-semibold text-foreground">
                                        Starting Price *
                                    </label>
                                    <div className="relative">
                                        <Euro className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
                                        <input
                                            type="number"
                                            name="startingPrice"
                                            value={formData.startingPrice}
                                            onChange={handleChange}
                                            placeholder="10.00"
                                            step="0.01"
                                            min="0"
                                            className="w-full pl-12 pr-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                            required
                                        />
                                    </div>
                                </div>

                                <div>
                                    <label className="block mb-2 font-semibold text-foreground">
                                        Buy Out Price
                                    </label>
                                    <div className="relative">
                                        <Euro className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
                                        <input
                                            type="number"
                                            name="buyoutPrice"
                                            value={formData.buyoutPrice}
                                            onChange={handleChange}
                                            placeholder="20.00"
                                            step="0.01"
                                            min="0"
                                            className="w-full pl-12 pr-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                        />
                                    </div>
                                </div>

                                <div className="grid md:grid-cols-2 gap-6">
                                    <div>
                                        <label className="block mb-2 font-semibold text-foreground">
                                            Start Date *
                                        </label>
                                        <input
                                            type="date"
                                            name="auctionStartDate"
                                            value={formData.auctionStartDate}
                                            onChange={handleChange}
                                            min={minDate}
                                            className="w-full px-4 py-4 bg-input-background border-2 border-border rounded-xl"
                                            required
                                        />
                                    </div>
                                    <div>
                                        <label className="block mb-2 font-semibold text-foreground">
                                            Start Time *
                                        </label>
                                        <input
                                            type="time"
                                            name="auctionStartTime"
                                            value={formData.auctionStartTime}
                                            onChange={handleChange}
                                            min={formData.auctionStartDate === minDate ? minTime : undefined}
                                            className="w-full px-4 py-4 bg-input-background border-2 border-border rounded-xl"
                                            required
                                        />
                                    </div>
                                </div>

                                <div className="grid md:grid-cols-2 gap-6">
                                    <div>
                                        <label className="block mb-2 font-semibold text-foreground">
                                            End Date *
                                        </label>
                                        <input
                                            type="date"
                                            name="auctionEndDate"
                                            value={formData.auctionEndDate}
                                            onChange={handleChange}
                                            min={formData.auctionStartDate || minDate}
                                            className="w-full px-4 py-4 bg-input-background border-2 border-border rounded-xl"
                                            required
                                        />
                                    </div>
                                    <div>
                                        <label className="block mb-2 font-semibold text-foreground">
                                            End Time *
                                        </label>
                                        <input
                                            type="time"
                                            name="auctionEndTime"
                                            value={formData.auctionEndTime}
                                            onChange={handleChange}
                                            className="w-full px-4 py-4 bg-input-background border-2 border-border rounded-xl"
                                            required
                                        />
                                    </div>
                                </div>
                            </>
                        )}

                        <div className="flex gap-4 pt-6">
                            <button
                                type="submit"
                                disabled={submitting}
                                className="flex items-center gap-2 px-8 py-4 bg-gradient-to-r from-primary to-accent text-white rounded-xl hover:shadow-2xl hover:scale-105 transition-all font-semibold text-lg disabled:opacity-60 disabled:cursor-not-allowed"
                            >
                                <Tag className="w-5 h-5" />
                                {submitting ? "Publishing..." : "Publish Sale"}
                            </button>

                            <button
                                type="button"
                                onClick={() => navigate(-1)}
                                className="px-8 py-4 bg-white border-2 border-border rounded-xl hover:bg-muted transition-colors font-semibold text-lg"
                            >
                                Cancel
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}