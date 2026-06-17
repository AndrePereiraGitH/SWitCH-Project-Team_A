import { Link, useNavigate } from "react-router-dom";
import { BookOpen, ShoppingBag, List, Users, ArrowRight, Star, Shield, Heart, Search, ShoppingCart, Tag } from "lucide-react";
import { useState, useEffect } from "react";
import { BookCard } from "../components/BookCard";
import { BidModal } from "../components/BidModal";
import { useCart } from "../components/CartContext";
import { usePurchases } from "../components/PurchasesContext";
import { API_BASE_URL, CURRENT_USER_ID } from "@/api/constants.js";

export function LandingPage() {
  const { addToCart } = useCart();
  const { soldItemIds } = usePurchases();
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState("");
  const [bidModalOpen, setBidModalOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);
  const [sales, setSales] = useState([]);
  const [genres, setGenres] = useState([]);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/explore?search=${encodeURIComponent(searchQuery.trim())}`);
    }
  };

  const mapListingToSale = (listing) => ({
    id: listing.id,
    title: listing.listingTile ?? listing.listingTitle,
    author: listing.authors?.join(", ") ?? "Unknown",
    genre: listing.genre ?? [],
    price: Number(listing.price),
    condition: listing.condition,
    isAuction: listing.saleType === "AUCTION",
    type: "book",
  });

  const getGenreName = (genreId) => {
    return genres.find(g => g.id === genreId)?.genreName ?? genreId;
  };

  const getSaleGenreNames = (sale) => {
    return sale.genre?.map(getGenreName).join(", ") ?? "";
  };

  useEffect(() => {
    async function fetchData() {
      try {
        const [genresRes, listingsRes] = await Promise.all([
          fetch(`${API_BASE_URL}/genres`),
          fetch(`${API_BASE_URL}/listings?userId=${CURRENT_USER_ID}`),
        ]);

        if (!genresRes.ok || !listingsRes.ok) {
          throw new Error("Failed to fetch landing data");
        }

        const genresData = await genresRes.json();
        const listingsData = await listingsRes.json();

        setGenres(genresData);
        setSales(listingsData.map(mapListingToSale));
      } catch (error) {
        console.error("Error loading listings:", error);
      }
    }

    fetchData();
  }, []);

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <section className="relative min-h-[700px] flex items-center overflow-hidden pt-20">
        <div className="absolute inset-0">
          <img
            src="https://images.unsplash.com/photo-1476081718509-d5d0b661a376?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&q=80&w=1920"
            alt="Books Library"
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-gradient-to-r from-blue-600/70 via-purple-600/60 to-pink-600/70"></div>
        </div>

        <div className="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 w-full">
          <div className="lg:w-2/3 mx-auto text-center">
            <h1 className="font-serif text-5xl md:text-7xl mb-6 text-white leading-tight drop-shadow-lg">
              Share the Passion
              <br />
              <span className="text-yellow-300">
                for Reading
              </span>
            </h1>

            <p className="text-xl text-white/95 mb-10 max-w-2xl mx-auto leading-relaxed drop-shadow">
              Used books marketplace where you can buy, sell and discover incredible stories.
            </p>

            <div className="max-w-2xl mx-auto mb-8">
              <form onSubmit={handleSearch} className="relative">
                <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-muted-foreground w-5 h-5" />
                <input
                  type="text"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  placeholder="Search by title, author, ISBN..."
                  className="w-full pl-12 pr-4 py-4 rounded-xl border-2 border-white/30 bg-white/95 backdrop-blur-sm focus:outline-none focus:ring-2 focus:ring-white focus:border-transparent transition-all shadow-2xl"
                />
              </form>
            </div>

            <div className="flex gap-4 justify-center flex-wrap">
              <Link
                to="/explore"
                className="inline-flex items-center gap-2 px-8 py-4 bg-white text-primary rounded-xl hover:shadow-2xl transition-all transform hover:-translate-y-1 font-semibold shadow-xl"
              >
                Buy Now
              </Link>
              <Link
                to="/dashboard"
                className="inline-flex items-center gap-2 px-8 py-4 bg-primary text-white rounded-xl hover:shadow-2xl transition-all transform hover:-translate-y-1 font-semibold shadow-xl"
              >
                Sell Now
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Explore by Category Section */}
      <section className="py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="font-serif text-4xl md:text-5xl mb-4 text-foreground">Explore by Category</h2>
            <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
              Browse our collection by category
            </p>
          </div>

          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {[
              { name: "Fiction", IconComponent: BookOpen, color: "from-blue-500 to-indigo-500", bg: "bg-blue-50" },
              { name: "Non-Fiction", IconComponent: BookOpen, color: "from-green-500 to-emerald-500", bg: "bg-green-50" },
              { name: "Science", IconComponent: BookOpen, color: "from-purple-500 to-pink-500", bg: "bg-purple-50" },
              { name: "History", IconComponent: BookOpen, color: "from-amber-500 to-orange-500", bg: "bg-amber-50" },
              { name: "Art", IconComponent: BookOpen, color: "from-rose-500 to-red-500", bg: "bg-rose-50" },
              { name: "Children", IconComponent: BookOpen, color: "from-cyan-500 to-teal-500", bg: "bg-cyan-50" }
            ].map((category) => {
              const Icon = category.IconComponent;
              return (
                <Link
                  key={category.name}
                  to={`/explore?genre=${category.name}`}
                  className={`${category.bg} border-2 border-border rounded-2xl p-8 hover:shadow-2xl hover:-translate-y-2 transition-all duration-300 group cursor-pointer`}
                >
                  <div className={`w-16 h-16 bg-gradient-to-br ${category.color} rounded-2xl flex items-center justify-center mb-4 group-hover:scale-110 transition-transform shadow-lg`}>
                    <Icon className="w-8 h-8 text-white" />
                  </div>
                  <h3 className="font-serif text-2xl mb-2 text-foreground group-hover:text-primary transition-colors">
                    {category.name}
                  </h3>
                  <p className="text-muted-foreground">
                    Explore {category.name.toLowerCase()} books
                  </p>
                </Link>
              );
            })}
          </div>
        </div>
      </section>

      {/* How It Works Section */}
      <section className="py-24 bg-gradient-to-b from-white to-blue-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="font-serif text-4xl md:text-5xl mb-4 text-foreground">How It Works</h2>
            <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
              Everything you need to buy and sell used books in one place
            </p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center p-8 rounded-2xl border-2 border-border hover:border-primary hover:shadow-xl transition-all">
              <div className="inline-flex items-center justify-center w-20 h-20 bg-primary/10 rounded-2xl mb-6">
                <ShoppingBag className="w-10 h-10 text-primary" />
              </div>
              <h3 className="font-serif text-2xl mb-3 text-foreground">Buy & Sell</h3>
              <p className="text-muted-foreground">
                Direct sale or auction. Set your prices and conditions easily.
              </p>
            </div>

            <div className="text-center p-8 rounded-2xl border-2 border-border hover:border-accent hover:shadow-xl transition-all">
              <div className="inline-flex items-center justify-center w-20 h-20 bg-accent/10 rounded-2xl mb-6">
                <List className="w-10 h-10 text-accent" />
              </div>
              <h3 className="font-serif text-2xl mb-3 text-foreground">Organize</h3>
              <p className="text-muted-foreground">
                Create your private library, organize in thematic lists and share your discoveries.
              </p>
            </div>

            <div className="text-center p-8 rounded-2xl border-2 border-border hover:border-primary hover:shadow-xl transition-all">
              <div className="inline-flex items-center justify-center w-20 h-20 bg-primary/10 rounded-2xl mb-6">
                <Users className="w-10 h-10 text-primary" />
              </div>
              <h3 className="font-serif text-2xl mb-3 text-foreground">Community</h3>
              <p className="text-muted-foreground">
                Connect with other readers, authors and publishers in a book-centered social network.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Items For Sale Section */}
      <section className="py-24 bg-gradient-to-b from-white to-blue-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center mb-12">
            <div>
              <h2 className="font-serif text-4xl md:text-5xl mb-2 text-foreground">Items For Sale</h2>
              <p className="text-muted-foreground">Discover items from our community</p>
            </div>
            <Link
              to="/explore"
              className="flex items-center gap-2 text-primary hover:text-primary/80 font-semibold transition-colors"
            >
              View All
              <ArrowRight className="w-5 h-5" />
            </Link>
          </div>

          <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {sales.map(sale => (
              <div key={sale.id} className="relative group flex flex-col">
                <div className="absolute top-3 right-3 z-10">
                  <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                    sale.isAuction
                      ? "bg-accent text-white"
                      : "bg-primary text-white"
                  }`}>
                    {sale.isAuction ? "Auction" : "Direct Sale"}
                  </span>
                </div>
                <BookCard
                  title={sale.title}
                  author={sale.author}
                  genre={getSaleGenreNames(sale)}
                  price={sale.price}
                  condition={sale.condition}
                  isAuction={sale.isAuction}
                  auctionEndDate={sale.auctionEndDate}
                  imageUrl={sale.image}
                  isSold={soldItemIds.has(sale.id)}
                />
                <div className="mt-3">
                  {soldItemIds.has(sale.id) ? (
                      <button
                          disabled
                          className="w-full flex items-center justify-center gap-2 px-4 py-3 bg-gray-200 text-gray-400 rounded-xl font-semibold cursor-not-allowed"
                      >
                        Sold
                      </button>
                  ) : sale.isAuction ? (
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
        </div>
      </section>

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

      {/* Why Us Section */}
      <section id="about" className="py-24 bg-white scroll-mt-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="font-serif text-4xl md:text-5xl mb-4 text-foreground">Why MiteLovers?</h2>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center p-8">
              <div className="inline-flex items-center justify-center w-16 h-16 bg-primary/10 rounded-2xl mb-6">
                <Heart className="w-8 h-8 text-primary" />
              </div>
              <h3 className="font-serif text-2xl mb-3 text-foreground">Sustainability</h3>
              <p className="text-muted-foreground">
                Give books a second life and promote the circular economy of literature.
              </p>
            </div>

            <div className="text-center p-8">
              <div className="inline-flex items-center justify-center w-16 h-16 bg-accent/10 rounded-2xl mb-6">
                <Shield className="w-8 h-8 text-accent" />
              </div>
              <h3 className="font-serif text-2xl mb-3 text-foreground">Security</h3>
              <p className="text-muted-foreground">
                Rating system and certified entities ensure secure transactions.
              </p>
            </div>

            <div className="text-center p-8">
              <div className="inline-flex items-center justify-center w-16 h-16 bg-primary/10 rounded-2xl mb-6">
                <Star className="w-8 h-8 text-primary" />
              </div>
              <h3 className="font-serif text-2xl mb-3 text-foreground">Affordable Access</h3>
              <p className="text-muted-foreground">
                Make literature accessible to everyone through fair and competitive prices.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-24 bg-gradient-to-br from-primary via-accent to-cyan-500 text-white relative overflow-hidden">
        <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImdyaWQiIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PGNpcmNsZSBjeD0iMzAiIGN5PSIzMCIgcj0iMiIgZmlsbD0icmdiYSgyNTUsMjU1LDI1NSwwLjEpIi8+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2dyaWQpIi8+PC9zdmc+')] opacity-30"></div>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10">
          <h2 className="font-serif text-4xl md:text-5xl mb-6">
            Ready to Start?
          </h2>
          <p className="text-xl mb-8 opacity-95 max-w-2xl mx-auto">
            Join thousands of readers who are already part of the MiteLovers community
          </p>
          <Link
            to="/dashboard"
            className="inline-flex items-center gap-2 px-8 py-4 bg-white text-primary rounded-xl hover:scale-105 hover:shadow-2xl transition-all text-lg font-semibold shadow-xl"
          >
            Create Free Account
            <ArrowRight className="w-5 h-5" />
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="py-12 bg-white border-t border-border">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row justify-between items-center gap-4">
            <div className="flex items-center gap-2">
              <BookOpen className="w-6 h-6 text-primary" />
              <span className="font-serif font-bold text-xl text-primary">MiteLovers</span>
            </div>
            <p className="text-muted-foreground text-sm">
              © 2026 MiteLovers. Promoting sustainability and access to literature.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
}
