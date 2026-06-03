import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, Euro, Clock, Tag, Upload, X } from "lucide-react";
import { usePublications } from "../components/PublicationsContext";

export function SellPublication() {
    const navigate = useNavigate();
    const { bookId } = useParams();
    const { getAllPublications, setPublicationForSale } = usePublications();
    const [saleType, setSaleType] = useState("direct");
    const [images, setImages] = useState([]);
    const [formData, setFormData] = useState({
        price: "",
        startingPrice: "",
        buyoutPrice: "",
        auctionStartDate: "",
        auctionStartTime: "",
        auctionEndDate: "",
        auctionEndTime: ""
    });

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

    const handleSubmit = (e) => {
        e.preventDefault();

        const price = saleType === "direct"
            ? parseFloat(formData.price)
            : parseFloat(formData.startingPrice);

        setPublicationForSale(bookId, price);
        navigate("/my-publications");
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
                if (images.length < 5) {
                    const reader = new FileReader();
                    reader.onloadend = () => {
                        setImages(prev => [...prev, reader.result]);
                    };
                    reader.readAsDataURL(file);
                }
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
                            <p className="text-muted-foreground">US044: Direct sale of publication</p>
                        </div>
                    </div>

                    {/* Book Info */}
                    <div className="bg-muted p-4 rounded-lg mb-6">
                        <h3 className="font-serif text-lg mb-1">{book.title}</h3>
                        <p className="text-sm text-muted-foreground mb-1">{book.author}</p>
                        <span className="text-xs bg-card px-2 py-1 rounded">Condição: {book.condition}</span>
                    </div>

                    {/* Sale Type Selection */}
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
                        {/* Image Upload Section */}
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
                                <label className="block mb-2 font-semibold text-foreground">Sale Price *</label>
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
                                    <label className="block mb-2 font-semibold text-foreground">Starting Price *</label>
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
                                    <label className="block mb-2 font-semibold text-foreground">Buy Out Price (optional)</label>
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
                                    <p className="text-xs text-muted-foreground mt-2">
                                        Price for immediate purchase without auction
                                    </p>
                                </div>

                                <div className="grid md:grid-cols-2 gap-6">
                                    <div>
                                        <label className="block mb-2 font-semibold text-foreground">Start Date *</label>
                                        <input
                                            type="date"
                                            name="auctionStartDate"
                                            value={formData.auctionStartDate}
                                            onChange={handleChange}
                                            className="w-full px-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                            required
                                        />
                                    </div>
                                    <div>
                                        <label className="block mb-2 font-semibold text-foreground">Start Time *</label>
                                        <input
                                            type="time"
                                            name="auctionStartTime"
                                            value={formData.auctionStartTime}
                                            onChange={handleChange}
                                            className="w-full px-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                            required
                                        />
                                    </div>
                                </div>

                                <div className="grid md:grid-cols-2 gap-6">
                                    <div>
                                        <label className="block mb-2 font-semibold text-foreground">End Date *</label>
                                        <input
                                            type="date"
                                            name="auctionEndDate"
                                            value={formData.auctionEndDate}
                                            onChange={handleChange}
                                            className="w-full px-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                            required
                                        />
                                    </div>
                                    <div>
                                        <label className="block mb-2 font-semibold text-foreground">End Time *</label>
                                        <input
                                            type="time"
                                            name="auctionEndTime"
                                            value={formData.auctionEndTime}
                                            onChange={handleChange}
                                            className="w-full px-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                                            required
                                        />
                                    </div>
                                </div>

                                <div className="bg-gradient-to-br from-blue-50 to-cyan-50 border-2 border-blue-200 rounded-xl p-6">
                                    <p className="text-sm text-foreground leading-relaxed">
                                        <strong className="text-primary">ℹ️ Nota:</strong> Buy Out Price allows buyers to purchase immediately without auction. If not set, only bids will be accepted.
                                    </p>
                                </div>
                            </>
                        )}

                        {/* Actions */}
                        <div className="flex gap-4 pt-6">
                            <button
                                type="submit"
                                className="flex items-center gap-2 px-8 py-4 bg-gradient-to-r from-primary to-accent text-white rounded-xl hover:shadow-2xl hover:scale-105 transition-all font-semibold text-lg"
                            >
                                <Tag className="w-5 h-5" />
                                Publish Sale
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
