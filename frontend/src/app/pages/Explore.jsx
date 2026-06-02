import { useState, useEffect } from "react";
import { ArrowLeft, Search, Filter, ShoppingCart, Tag } from "lucide-react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { BookCard } from "../components/BookCard";
import { BidModal } from "../components/BidModal";
import { useCart } from "../components/CartContext";

export function Explore() {
  const navigate = useNavigate();
  const { addToCart } = useCart();
  const [searchParams] = useSearchParams();
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    const searchFromUrl = searchParams.get("search");
    if (searchFromUrl) {
      setSearchQuery(searchFromUrl);
    }
  }, [searchParams]);

  const [filterGenre, setFilterGenre] = useState("all");
  const [filterType, setFilterType] = useState("all");
  const [saleType, setSaleType] = useState("all");
  const [bidModalOpen, setBidModalOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);

  const genres = [
    "all", "Fiction", "Romance", "Mystery", "Thriller", "Fantasy",
    "Science Fiction", "Biography", "History", "Science", "Philosophy"
  ];

  const mockSales = [
    {
      id: "1",
      title: "The Da Vinci Code",
      author: "Dan Brown",
      genre: "Mystery",
      price: 12.5,
      condition: "Good",
      isAuction: false,
      type: "book"
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
      type: "book"
    },
    {
      id: "3",
      title: "National Geographic Magazine",
      author: "Various",
      genre: "Science",
      price: 8,
      condition: "Like New",
      isAuction: false,
      type: "magazine"
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
      type: "book"
    },
    {
      id: "5",
      title: "Game of Thrones",
      author: "George R.R. Martin",
      genre: "Fantasy",
      price: 22,
      condition: "Very Good",
      isAuction: false,
      type: "book"
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
      type: "magazine"
    }
  ];

  const filteredSales = mockSales.filter(sale => {
    const matchesSearch =
        sale.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        sale.author.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesGenre = filterGenre === "all" || sale.genre === filterGenre;
    const matchesType = filterType === "all" || sale.type === filterType;
    const matchesSaleType =
        saleType === "all" ||
        (saleType === "direct" && !sale.isAuction) ||
        (saleType === "auction" && sale.isAuction);
    return matchesSearch && matchesGenre && matchesType && matchesSaleType;
  });

  return (
      <div className="min-h-screen bg-background pt-24 pb-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <button
              onClick={() => navigate("/")}
              className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
          >
            <ArrowLeft className="w-4 h-4" />
            Back
          </button>

          <div className="mb-8">
            <h1 className="font-serif text-4xl md:text-5xl mb-2 text-foreground">
              Explore Sales
            </h1>
            <p className="text-muted-foreground">
              US045: Search items for sale by genre
            </p>
          </div>

          {/* Search and Filters */}
          <div className="bg-white border-2 border-border rounded-2xl p-6 mb-8 shadow-sm">

            {/* Search Bar */}
            <div className="relative mb-6">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
              <input
                  type="text"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  placeholder="Search by title or author..."
                  className="w-full pl-12 pr-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
              />
            </div>

            {/* Filters */}
            <div className="flex items-center gap-3 mb-4">
              <Filter className="w-5 h-5 text-primary" />
              <h3 className="font-semibold text-lg">Filters</h3>
            </div>

            <div className="grid sm:grid-cols-2 md:grid-cols-3 gap-4">
              <div>
                <label className="block mb-2 text-sm font-semibold text-foreground">
                  Genre
                </label>
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

              <div>
                <label className="block mb-2 text-sm font-semibold text-foreground">
                  Publication Type
                </label>
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

              <div>
                <label className="block mb-2 text-sm font-semibold text-foreground">
                  Sale Type
                </label>
                <select
                    value={saleType}
                    onChange={(e) => setSaleType(e.target.value)}
                    className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="all">All Types</option>
                  <option value="direct">Direct Sale</option>
                  <option value="auction">Auction</option>
                </select>
              </div>
            </div>
          </div>

          {/* Results */}
          <div className="mb-4 flex justify-between items-center">
            <p className="text-muted-foreground">
              {filteredSales.length}{" "}
              {filteredSales.length === 1 ? "result" : "results"} found
            </p>
          </div>

          {filteredSales.length === 0 ? (
              <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                <p className="text-muted-foreground">
                  No books found with the selected filters
                </p>
              </div>
          ) : (
              <div className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                {filteredSales.map(sale => (
                    <div key={sale.id} className="relative group flex flex-col">
                      <div className="absolute top-3 right-3 z-10 flex flex-col gap-2">
                  <span className={`px-3 py-1 rounded-full text-xs font-semibold text-center ${
                      sale.isAuction
                          ? "bg-accent text-white"
                          : "bg-primary text-white"
                  }`}>
                    {sale.isAuction ? "Auction" : "Direct Sale"}
                  </span>
                        <span className="px-3 py-1 rounded-full text-xs font-semibold text-center bg-gradient-to-r from-purple-500 to-indigo-500 text-white">
                    {sale.type === "book" ? "Book" : "Magazine"}
                  </span>
                      </div>
                      <div
                          onClick={() => navigate(`/product/${sale.id}`)}
                          className="cursor-pointer"
                      >
                        <BookCard
                            title={sale.title}
                            author={sale.author}
                            genre={sale.genre}
                            price={sale.price}
                            condition={sale.condition}
                            isAuction={sale.isAuction}
                            auctionEndDate={sale.auctionEndDate}
                        />
                      </div>
                      <div className="mt-3">
                        {sale.isAuction ? (
                            <button
                                onClick={() => {
                                  setSelectedItem(sale);
                                  setBidModalOpen(true);
                                }}
                                className="w-full flex items-center justify-center gap-2 px-4 py-3 bg-accent text-white rounded-xl hover:bg-accent/90 transition-colors font-semibold"
                            >
                              <Tag className="w-5 h-5" />
                              Make Offer
                            </button>
                        ) : (
                            <button
                                onClick={() => {
                                  addToCart({
                                    id: sale.id,
                                    title: sale.title,
                                    price: sale.price
                                  });
                                }}
                                className="w-full flex items-center justify-center gap-2 px-4 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold"
                            >
                              <ShoppingCart className="w-5 h-5" />
                              Add to Cart
                            </button>
                        )}
                      </div>
                    </div>
                ))}
              </div>
          )}
        </div>

        {/* Bid Modal */}
        {selectedItem && (
            <BidModal
                isOpen={bidModalOpen}
                onClose={() => {
                  setBidModalOpen(false);
                  setSelectedItem(null);
                }}
                itemTitle={selectedItem.title}
                currentBid={selectedItem.price}
                buyoutPrice={selectedItem.price * 1.5}
                onSubmitBid={(amount) => {
                  console.log("Bid submitted:", amount);
                }}
                onBuyNow={() => {
                  addToCart({
                    id: selectedItem.id,
                    title: selectedItem.title,
                    price: selectedItem.price * 1.5
                  });
                }}
            />
        )}
      </div>
  );
}