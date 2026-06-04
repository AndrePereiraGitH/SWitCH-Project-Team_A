import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, ShoppingCart, Tag, Package, Star, Calendar, User, BookOpen, Info } from "lucide-react";
import { BidModal } from "../components/BidModal";
import { useCart } from "../components/CartContext";
import { API_BASE_URL } from "../../api/constants";



export function ProductDetail() {
    const { productId } = useParams();
    const navigate = useNavigate();
    const { addToCart } = useCart();


    const [bidModalOpen, setBidModalOpen] = useState(false);
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
    async function fetchProduct() {
        try {
        const userId = "user-001"; // trocar pelo user real/autenticado

        const response = await fetch(
            `${API_BASE_URL}/listings/${productId}?userId=${userId}`
        );

        if (!response.ok) {
            setProduct(null);
            return;
        }

        const data = await response.json();

        setProduct({
            id: data.id,
            title: data.listingTitle,
            author: data.authors?.join(", ") ?? "Unknown",
            genre: data.genre ?? [],
            price: Number(data.price),
            condition: data.condition,
            isAuction: data.saleType === "AUCTION",
            picture: data.picture,
            description: data.shortDescription ?? "",
            type: "book",
            sellerName: data.sellerId ?? "Unknown seller",
            sellerRating: 0,
            location: "",
            isbn: data.isbn ?? "N/A",
            publisher: data.publisher ?? "N/A",
            year: data.year ?? "N/A",
            pages: data.pages ?? "N/A",
            language: data.language ?? "N/A",
        });
        } catch (error) {
        console.error("Error fetching listing details:", error);
        setProduct(null);
        } finally {
        setLoading(false);
        }
    }

    fetchProduct();
    }, [productId]);


    if (loading) {
    return (
        <div className="min-h-screen bg-background pt-24 pb-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <p className="text-muted-foreground">Loading product details...</p>
        </div>
        </div>
    );
    }


    if (!product) {
        return (
            <div className="min-h-screen bg-background pt-24 pb-12">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <button
                        onClick={() => navigate("/explore")}
                        className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
                    >
                        <ArrowLeft className="w-4 h-4" />
                        Back to Marketplace
                    </button>
                    <div className="bg-white border-2 border-border rounded-2xl p-12 text-center">
                        <h2 className="text-2xl font-serif mb-4">Product Not Found</h2>
                        <p className="text-muted-foreground">The product you're looking for doesn't exist.</p>
                    </div>
                </div>
            </div>
        );
    }

    const getConditionColor = (condition) => {
        switch (condition.toLowerCase()) {
            case "like new":
                return "bg-green-100 text-green-800";
            case "very good":
                return "bg-blue-100 text-blue-800";
            case "good":
                return "bg-yellow-100 text-yellow-800";
            default:
                return "bg-gray-100 text-gray-800";
        }
    };

    const formatAuctionTime = (date) => {
        const now = new Date();
        const diff = date.getTime() - now.getTime();
        const days = Math.floor(diff / (1000 * 60 * 60 * 24));
        const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        return `${days}d ${hours}h remaining`;
    };

    return (
        <div className="min-h-screen bg-background pt-24 pb-12">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <button
                    onClick={() => navigate("/explore")}
                    className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
                >
                    <ArrowLeft className="w-4 h-4" />
                    Back to Marketplace
                </button>

                <div className="grid lg:grid-cols-3 gap-8">
                    {/* Left Column - Product Image & Info */}
                    <div className="lg:col-span-1">
                        <div className="bg-white border-2 border-border rounded-2xl p-8 shadow-sm sticky top-24">
                            {/* Product Image Placeholder */}
                            <div className="aspect-[3/4] bg-gradient-to-br from-primary/10 to-accent/10 rounded-xl flex items-center justify-center mb-6">
                                <BookOpen className="w-24 h-24 text-primary/40" />
                            </div>

                            {/* Sale Type Badge */}
                            <div className="flex justify-center mb-4">
                <span className={`px-4 py-2 rounded-full text-sm font-semibold ${
                    product.isAuction
                        ? "bg-accent text-white"
                        : "bg-primary text-white"
                }`}>
                  {product.isAuction ? "Auction" : "Direct Sale"}
                </span>
                            </div>

                            {/* Auction Timer */}
                            {product.isAuction && product.auctionEndDate && (
                                <div className="bg-accent/10 border border-accent/20 rounded-xl p-4 mb-4">
                                    <div className="flex items-center gap-2 mb-2">
                                        <Calendar className="w-5 h-5 text-accent" />
                                        <span className="font-semibold text-accent">Auction Ends</span>
                                    </div>
                                    <p className="text-sm text-foreground font-bold">
                                        {formatAuctionTime(product.auctionEndDate)}
                                    </p>
                                </div>
                            )}

                            {/* Seller Info */}
                            <div className="bg-gray-50 rounded-xl p-4">
                                <div className="flex items-center gap-3 mb-3">
                                    <div className="w-10 h-10 bg-gradient-to-br from-primary to-accent rounded-full flex items-center justify-center text-white font-bold">
                                        {product.sellerName[0]}
                                    </div>
                                    <div className="flex-1">
                                        <p className="font-semibold text-foreground">{product.sellerName}</p>
                                        <div className="flex items-center gap-1">
                                            <Star className="w-4 h-4 text-yellow-500 fill-yellow-500" />
                                            <span className="text-sm font-medium">{product.sellerRating}</span>
                                        </div>
                                    </div>
                                </div>
                                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                    <Package className="w-4 h-4" />
                                    <span>{product.location}</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Right Column - Product Details */}
                    <div className="lg:col-span-2">
                        <div className="bg-white border-2 border-border rounded-2xl p-8 shadow-sm mb-6">
                            {/* Title & Author */}
                            <div className="mb-6">
                                <h1 className="font-serif text-4xl mb-2 text-foreground">{product.title}</h1>
                                <p className="text-xl text-muted-foreground mb-4">by {product.author}</p>
                                <div className="flex flex-wrap gap-2">
                  <span className="px-4 py-1.5 bg-primary/10 text-primary rounded-full text-sm font-medium">
                    {product.genre}
                  </span>
                                    <span className="px-4 py-1.5 bg-accent/10 text-accent rounded-full text-sm font-medium">
                    {product.type === "book" ? "Book" : "Magazine"}
                  </span>
                                    <span className={`px-4 py-1.5 rounded-full text-sm font-medium ${getConditionColor(product.condition)}`}>
                    {product.condition}
                  </span>
                                </div>
                            </div>

                            {/* Price */}
                            <div className="mb-6 pb-6 border-b border-border">
                                <div className="flex items-baseline gap-2">
                                    <span className="text-4xl font-bold text-primary">€{product.price.toFixed(2)}</span>
                                    {product.isAuction && (
                                        <span className="text-sm text-muted-foreground">(Current Offer)</span>
                                    )}
                                </div>
                                {product.isAuction && (
                                    <p className="text-sm text-muted-foreground mt-1">
                                        Buyout Price: €{(product.price * 1.5).toFixed(2)}
                                    </p>
                                )}
                            </div>

                            {/* Description */}
                            <div className="mb-6">
                                <div className="flex items-center gap-2 mb-3">
                                    <Info className="w-5 h-5 text-primary" />
                                    <h3 className="text-xl font-semibold">Description</h3>
                                </div>
                                <p className="text-muted-foreground leading-relaxed">{product.description}</p>
                            </div>

                            {/* Publication Details */}
                            <div className="grid md:grid-cols-2 gap-4 mb-6">
                                <div>
                                    <h4 className="font-semibold text-foreground mb-3">Publication Details</h4>
                                    <div className="space-y-2 text-sm">
                                        <div className="flex justify-between">
                                            <span className="text-muted-foreground">ISBN:</span>
                                            <span className="font-medium">{product.isbn}</span>
                                        </div>
                                        <div className="flex justify-between">
                                            <span className="text-muted-foreground">Publisher:</span>
                                            <span className="font-medium">{product.publisher}</span>
                                        </div>
                                        <div className="flex justify-between">
                                            <span className="text-muted-foreground">Year:</span>
                                            <span className="font-medium">{product.year}</span>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <h4 className="font-semibold text-foreground mb-3">Additional Info</h4>
                                    <div className="space-y-2 text-sm">
                                        <div className="flex justify-between">
                                            <span className="text-muted-foreground">Pages:</span>
                                            <span className="font-medium">{product.pages}</span>
                                        </div>
                                        <div className="flex justify-between">
                                            <span className="text-muted-foreground">Language:</span>
                                            <span className="font-medium">{product.language}</span>
                                        </div>
                                        <div className="flex justify-between">
                                            <span className="text-muted-foreground">Condition:</span>
                                            <span className="font-medium">{product.condition}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            {/* Action Buttons */}
                            <div className="flex gap-4">
                                {product.isAuction ? (
                                    <button
                                        onClick={() => setBidModalOpen(true)}
                                        className="flex-1 flex items-center justify-center gap-2 px-6 py-4 bg-accent text-white rounded-xl hover:bg-accent/90 transition-colors font-semibold text-lg shadow-lg"
                                    >
                                        <Tag className="w-6 h-6" />
                                        Make Offer
                                    </button>
                                ) : (
                                    <button
                                        onClick={() => {
                                            addToCart({
                                                id: product.id,
                                                title: product.title,
                                                price: product.price
                                            });
                                        }}
                                        className="flex-1 flex items-center justify-center gap-2 px-6 py-4 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold text-lg shadow-lg"
                                    >
                                        <ShoppingCart className="w-6 h-6" />
                                        Add to Cart
                                    </button>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Bid Modal */}
            {product.isAuction && (
                <BidModal
                    isOpen={bidModalOpen}
                    onClose={() => setBidModalOpen(false)}
                    itemTitle={product.title}
                    currentBid={product.price}
                    buyoutPrice={product.price * 1.5}
                    onSubmitBid={(amount) => {
                        console.log("Bid submitted:", amount);
                    }}
                    onBuyNow={() => {
                        addToCart({
                            id: product.id,
                            title: product.title,
                            price: product.price * 1.5
                        });
                    }}
                />
            )}
        </div>
    );
}
