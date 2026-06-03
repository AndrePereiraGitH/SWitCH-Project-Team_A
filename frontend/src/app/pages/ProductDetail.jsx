import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, ShoppingCart, Tag, Package, Star, Calendar, User, BookOpen, Info } from "lucide-react";
import { BidModal } from "../components/BidModal";
import { useCart } from "../components/CartContext";

// Mock data - in production, this would come from an API or context
const mockProducts = [
    {
        id: "1",
        title: "The Da Vinci Code",
        author: "Dan Brown",
        genre: "Mystery",
        price: 12.5,
        condition: "Good",
        isAuction: false,
        type: "book",
        isbn: "978-0-307-47427-7",
        publisher: "Doubleday",
        year: 2003,
        pages: 689,
        language: "English",
        description: "A gripping mystery-thriller that combines art, history, and conspiracy theories. Follow symbologist Robert Langdon as he uncovers a centuries-old secret that could shake the foundations of Christianity.",
        sellerName: "BookCollector123",
        sellerRating: 4.8,
        location: "Lisbon, Portugal"
    },
    {
        id: "2",
        title: "Harry Potter and the Philosopher's Stone",
        author: "J.K. Rowling",
        genre: "Fantasy",
        price: 15,
        condition: "Very Good",
        isAuction: true,
        auctionEndDate: new Date(Date.now() + 5 * 24 * 60 * 60 * 1000),
        type: "book",
        isbn: "978-0-439-70818-8",
        publisher: "Bloomsbury",
        year: 1997,
        pages: 223,
        language: "English",
        description: "The first book in the beloved Harry Potter series. Join young wizard Harry Potter as he discovers his magical heritage and begins his education at Hogwarts School of Witchcraft and Wizardry.",
        sellerName: "MagicBooks",
        sellerRating: 4.9,
        location: "Porto, Portugal"
    },
    {
        id: "3",
        title: "National Geographic Magazine",
        author: "Various",
        genre: "Science",
        price: 8,
        condition: "Like New",
        isAuction: false,
        type: "magazine",
        isbn: "N/A",
        publisher: "National Geographic Society",
        year: 2023,
        pages: 120,
        language: "English",
        description: "This issue explores the wonders of our planet with stunning photography and in-depth articles on wildlife, conservation, and environmental science.",
        sellerName: "MagazineHub",
        sellerRating: 4.7,
        location: "Coimbra, Portugal"
    },
    {
        id: "4",
        title: "One Hundred Years of Solitude",
        author: "Gabriel García Márquez",
        genre: "Fiction",
        price: 18,
        condition: "Good",
        isAuction: true,
        auctionEndDate: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000),
        type: "book",
        isbn: "978-0-06-088328-7",
        publisher: "Harper & Row",
        year: 1967,
        pages: 417,
        language: "English",
        description: "A landmark of magical realism, this novel tells the story of the Buendía family over seven generations in the fictional town of Macondo.",
        sellerName: "ClassicReads",
        sellerRating: 5.0,
        location: "Braga, Portugal"
    },
    {
        id: "5",
        title: "Game of Thrones",
        author: "George R.R. Martin",
        genre: "Fantasy",
        price: 22,
        condition: "Very Good",
        isAuction: false,
        type: "book",
        isbn: "978-0-553-10354-0",
        publisher: "Bantam Spectra",
        year: 1996,
        pages: 694,
        language: "English",
        description: "The first book in A Song of Ice and Fire series. Enter the epic world of Westeros where summers span decades and winters can last a lifetime.",
        sellerName: "FantasyWorld",
        sellerRating: 4.8,
        location: "Faro, Portugal"
    },
    {
        id: "6",
        title: "TIME Magazine Collection",
        author: "Various",
        genre: "History",
        price: 25,
        condition: "Like New",
        isAuction: true,
        auctionEndDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000),
        type: "magazine",
        isbn: "N/A",
        publisher: "Time Inc.",
        year: 2022,
        pages: 100,
        language: "English",
        description: "A curated collection of TIME magazines featuring pivotal moments in history, from world-changing events to cultural milestones.",
        sellerName: "VintagePress",
        sellerRating: 4.9,
        location: "Aveiro, Portugal"
    }
];

export function ProductDetail() {
    const { productId } = useParams();
    const navigate = useNavigate();
    const { addToCart } = useCart();
    const [bidModalOpen, setBidModalOpen] = useState(false);

    // Find the product
    const product = mockProducts.find(p => p.id === productId);

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
