import {useState, useEffect, useRef} from "react";
import {ArrowLeft, Search, Filter, ShoppingCart, Tag} from "lucide-react";
import {useNavigate, useSearchParams} from "react-router-dom";
import {BookCard} from "../components/BookCard";
import {BidModal} from "../components/BidModal";
import {useCart} from "../components/CartContext";
import {usePurchases} from "../components/PurchasesContext";
import {API_BASE_URL, CURRENT_USER_ID} from "../../api/constants";

export function Explore() {
    const navigate = useNavigate();
    const {addToCart} = useCart();
    const {soldItemIds} = usePurchases();
    const [searchParams] = useSearchParams();

    const [searchQuery, setSearchQuery] = useState("");
    const [priceRange, setPriceRange] = useState([0, 100]);
    const [filterGenre, setFilterGenre] = useState("all");
    const [filterType, setFilterType] = useState("all");
    const [saleType, setSaleType] = useState("all");
    const [filterAuthor, setFilterAuthor] = useState("all");
    const [filterPublication, setFilterPublication] = useState("all");

    const [genres, setGenres] = useState([]);
    const [publications, setPublications] = useState([]);
    const [sales, setSales] = useState([]);
    const [allSales, setAllSales] = useState([]);
    const auctionsByListingIdRef = useRef({});
    const [loadingFilter, setLoadingFilter] = useState(false);

    const [bidModalOpen, setBidModalOpen] = useState(false);
    const [selectedItem, setSelectedItem] = useState(null);

    useEffect(() => {
        const searchFromUrl = searchParams.get("search");
        if (searchFromUrl) setSearchQuery(searchFromUrl);
    }, [searchParams]);

    useEffect(() => {
        async function fetchData() {
            try {
                const [genresRes, listingsRes, bdRes, auctionRes] = await Promise.all([
                    fetch(`${API_BASE_URL}/genres`),
                    fetch(`${API_BASE_URL}/listings`),
                    fetch(`${API_BASE_URL}/bibliographicalDatas`),
                    fetch(`${API_BASE_URL}/auctions`),
                ]);

                if (!genresRes.ok || !listingsRes.ok || !bdRes.ok || !auctionRes.ok) {
                    throw new Error("Failed to fetch explore data");
                }

                const genresData = await genresRes.json();
                const listingsData = await listingsRes.json();
                const bdData = await bdRes.json();

                const auctionsData = await auctionRes.json();
                const terminalStatuses = ["ENDED_WITH_WINNER", "ENDED_WITHOUT_BIDS", "BOUGHT_OUT", "CANCELLED"];
                const auctionsByListingId = Object.fromEntries(
                    auctionsData
                        .filter(a => ["ACTIVE", "DRAFT", "SCHEDULED"].includes(a.auctionStatus))
                        .map(a => [a.listingId, a])
                );

                const mapped = listingsData
                    .filter(listing => {
                        if (listing.saleType !== "AUCTION") return true;
                        return auctionsByListingId[listing.id] != null;
                    })
                    .map(listing => mapListingToSale(listing, auctionsByListingId));

                setGenres(genresData);
                setAllSales(mapped);
                setSales(mapped);
                auctionsByListingIdRef.current = auctionsByListingId;

                const bdItems = bdData._embedded?.responseBibliographicalDataDTOList ?? [];
                setPublications(bdItems.map(bd => ({
                    id: bd.bdId,
                    title: bd.pubTitle,
                })));
            } catch (error) {
                console.error("Error loading explore data:", error);
            }
        }

        fetchData();
    }, []);

    useEffect(() => {
        if (filterPublication !== "all") {
            fetchByPublication(filterPublication);
        } else if (filterGenre !== "all") {
            fetchByGenre(filterGenre);
        } else {
            setSales(allSales);
        }
    }, [filterGenre, filterPublication, allSales]);

    async function fetchByPublication(bdId) {
        setLoadingFilter(true);
        try {
            const res = await fetch(`${API_BASE_URL}/listings/by-publication/${bdId}`);
            if (!res.ok) throw new Error(`Failed: ${res.status}`);
            const data = await res.json();
            setSales(data.map(listing => mapListingToSale(listing, auctionsByListingIdRef.current)));
        } catch (error) {
            console.error("Error fetching listings by publication:", error);
            setSales([]);
        } finally {
            setLoadingFilter(false);
        }
    }

    async function fetchByGenre(genreId) {
        setLoadingFilter(true);
        try {
            const res = await fetch(`${API_BASE_URL}/listings/genre/${genreId}`);
            if (!res.ok) throw new Error(`Failed: ${res.status}`);
            const data = await res.json();
            setSales(data.map(listing => mapListingToSale(listing, auctionsByListingIdRef.current)));
        } catch (error) {
            console.error("Error fetching listings by genre:", error);
            setSales([]);
        } finally {
            setLoadingFilter(false);
        }
    }

    const mapListingToSale = (listing, auctionsByListingId = {}) => {
        const auction = auctionsByListingId[listing.id];
        return{
            id: listing.id,
            title: listing.listingTile ?? listing.listingTitle,
            author: listing.authors?.join(", ") ?? "Unknown",
            genre: listing.genre ?? [],
            price: listing.saleType === "AUCTION" && auction
                ? Number(auction.currentHighestBid ?? auction.initialPrice)
                : Number(listing.price),
            condition: listing.condition,
            isAuction: listing.saleType === "AUCTION",
            auctionStatus: auction?.auctionStatus ?? null,
            type: "book",
        }
    };

    const getGenreName = (genreId) =>
        genres.find(g => g.id === genreId)?.genreName ?? genreId;

    const getSaleGenreNames = (sale) =>
        sale.genre?.map(getGenreName).join(", ") ?? "";

    const authors = [...new Set(
        allSales.map(s => s.author).filter(a => a && a !== "Unknown")
    )];

    const handlePublicationChange = (e) => {
        setFilterPublication(e.target.value);
        setFilterGenre("all");
        setFilterAuthor("all");
        setSaleType("all");
    };

    const handleGenreChange = (e) => {
        setFilterGenre(e.target.value);
        setFilterPublication("all");
    };

    const filteredSales = sales.filter(sale => {
        const matchesSearch =
            sale.title?.toLowerCase().includes(searchQuery.toLowerCase()) ||
            sale.author?.toLowerCase().includes(searchQuery.toLowerCase());

        const matchesType =
            filterType === "all" || sale.type === filterType;

        const matchesSaleType =
            saleType === "all" ||
            (saleType === "direct" && !sale.isAuction) ||
            (saleType === "auction" && sale.isAuction);

        const matchesPrice =
            sale.price >= priceRange[0] && sale.price <= priceRange[1];

        const matchesAuthor =
            filterAuthor === "all" || sale.author === filterAuthor;

        return matchesSearch && matchesType && matchesSaleType && matchesPrice && matchesAuthor;
    });

    const activeFilterLabel =
        filterPublication !== "all" ? "publication" :
            filterGenre !== "all" ? "genre" :
                null;

    const clearActiveFilter = () => {
        setFilterPublication("all");
        setFilterGenre("all");
    };

    return (
        <div className="min-h-screen bg-background pt-24 pb-12">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <button
                    onClick={() => navigate("/")}
                    className="flex items-center gap-2 text-muted-foreground hover:text-primary mb-6 transition-colors font-medium"
                >
                    <ArrowLeft className="w-4 h-4"/>
                    Back
                </button>

                <div className="mb-8">
                    <h1 className="font-serif text-4xl md:text-5xl mb-2 text-foreground">
                        Explore Sales
                    </h1>
                    <p className="text-muted-foreground">
                        Browse items for sale by genre, author or publication
                    </p>
                </div>

                <div className="bg-white border-2 border-border rounded-2xl p-6 mb-8 shadow-sm">
                    <div className="relative mb-6">
                        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground"/>
                        <input
                            type="text"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            placeholder="Search by title or author..."
                            className="w-full pl-12 pr-4 py-4 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                        />
                    </div>

                    <div className="flex items-center gap-3 mb-4">
                        <Filter className="w-5 h-5 text-primary"/>
                        <h3 className="font-semibold text-lg">Filters</h3>
                    </div>

                    <div className="grid sm:grid-cols-2 md:grid-cols-3 gap-4 mb-4">
                        <div>
                            <label className="block mb-2 text-sm font-semibold text-foreground">Genre</label>
                            <select
                                value={filterGenre}
                                onChange={handleGenreChange}
                                disabled={filterPublication !== "all"}
                                className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                <option value="all">All Genres</option>
                                {genres.map(genre => (
                                    <option key={genre.id} value={genre.id}>{genre.genreName}</option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block mb-2 text-sm font-semibold text-foreground">Publication</label>
                            <select
                                value={filterPublication}
                                onChange={handlePublicationChange}
                                disabled={filterGenre !== "all"}
                                className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                <option value="all">All Publications</option>
                                {publications.map(pub => (
                                    <option key={pub.id} value={pub.id}>{pub.title}</option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block mb-2 text-sm font-semibold text-foreground">Publication Type</label>
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
                            <label className="block mb-2 text-sm font-semibold text-foreground">Sale Type</label>
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

                        <div>
                            <label className="block mb-2 text-sm font-semibold text-foreground">Author</label>
                            <select
                                value={filterAuthor}
                                onChange={(e) => setFilterAuthor(e.target.value)}
                                disabled={filterPublication !== "all" || filterGenre !== "all"}
                                className="w-full px-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                <option value="all">All Authors</option>
                                {authors.map(author => (
                                    <option key={author} value={author}>{author}</option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <div className="bg-blue-50 border border-blue-200 rounded-xl p-6 mt-6">
                        <label className="block mb-4 font-semibold text-foreground flex items-center gap-2">
                            <span>Price Range</span>
                            <span className="text-sm font-normal text-muted-foreground">
                                (€{priceRange[0]} - €{priceRange[1]})
                            </span>
                        </label>
                        <div className="space-y-3">
                            <div className="flex items-center gap-4">
                                <span className="text-sm font-medium w-12">Min</span>
                                <input
                                    type="range"
                                    min="0"
                                    max="100"
                                    value={priceRange[0]}
                                    onChange={(e) => setPriceRange([parseInt(e.target.value), priceRange[1]])}
                                    className="flex-1 accent-primary"
                                />
                                <span className="text-sm font-bold w-16 text-right">€{priceRange[0]}</span>
                            </div>
                            <div className="flex items-center gap-4">
                                <span className="text-sm font-medium w-12">Max</span>
                                <input
                                    type="range"
                                    min="0"
                                    max="100"
                                    value={priceRange[1]}
                                    onChange={(e) => setPriceRange([priceRange[0], parseInt(e.target.value)])}
                                    className="flex-1 accent-primary"
                                />
                                <span className="text-sm font-bold w-16 text-right">€{priceRange[1]}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="mb-4 flex justify-between items-center">
                    <p className="text-muted-foreground">
                        {loadingFilter ? "Loading…" : (
                            <>
                                {filteredSales.length}{" "}
                                {filteredSales.length === 1 ? "result" : "results"} found
                                {activeFilterLabel && (
                                    <span className="ml-2 text-primary font-medium">
                                        — filtered by {activeFilterLabel}
                                    </span>
                                )}
                            </>
                        )}
                    </p>
                    {activeFilterLabel && (
                        <button
                            onClick={clearActiveFilter}
                            className="text-sm text-muted-foreground hover:text-primary transition-colors underline"
                        >
                            Clear {activeFilterLabel} filter
                        </button>
                    )}
                </div>

                {filteredSales.length === 0 ? (
                    <div className="bg-white border-2 border-border rounded-2xl p-12 text-center shadow-sm">
                        <p className="text-muted-foreground">
                            No items found with the selected filters
                        </p>
                    </div>
                ) : (
                    <div className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {filteredSales.map(sale => (
                            <div key={sale.id} className="relative group flex flex-col">
                                <div className="absolute top-3 right-3 z-10 flex flex-col gap-2">
                                    <span className={`px-3 py-1 rounded-full text-xs font-semibold text-center ${
                                        sale.isAuction ? "bg-accent text-white" : "bg-primary text-white"
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
                                        genre={getSaleGenreNames(sale)}
                                        price={sale.price}
                                        condition={sale.condition}
                                        isAuction={sale.isAuction}
                                        auctionEndDate={sale.auctionEndDate}
                                        isSold={soldItemIds.has(sale.id)}
                                    />
                                </div>

                                <div className="mt-3">
                                    {soldItemIds.has(sale.id) ? (
                                        <button
                                            disabled
                                            className="w-full flex items-center justify-center gap-2 px-4 py-3 bg-gray-200 text-gray-400 rounded-xl font-semibold cursor-not-allowed"
                                        >
                                            Sold
                                        </button>
                                    ) : sale.isAuction ? (
                                        sale.auctionStatus === "ACTIVE" ? (
                                            <button
                                                onClick={async () => {
                                                    const auctionRes = await fetch(
                                                        `${API_BASE_URL}/auctions?listingId=${sale.id}`
                                                    );
                                                    if (auctionRes.ok) {
                                                        const auctions = await auctionRes.json();
                                                        const auction = auctions[0];
                                                        if (auction) {
                                                            setSelectedItem({
                                                                ...sale,
                                                                auctionId: auction.auctionId,
                                                                price: Number(auction.currentHighestBid ?? auction.initialPrice),
                                                                buyoutPrice: auction.buyoutPrice != null ? Number(auction.buyoutPrice) : null,
                                                            });
                                                            setBidModalOpen(true);
                                                        }
                                                    }
                                                }}
                                                className="w-full flex items-center justify-center gap-2 px-4 py-3 bg-accent text-white rounded-xl hover:bg-accent/90 transition-colors font-semibold"
                                            >
                                                <Tag className="w-5 h-5" />
                                                Make Offer
                                            </button>
                                        ) : (
                                            <button
                                                disabled
                                                className="w-full flex items-center justify-center gap-2 px-4 py-3 bg-muted text-muted-foreground rounded-xl font-semibold cursor-not-allowed"
                                            >
                                                <Tag className="w-5 h-5" />
                                                {sale.auctionStatus === "SCHEDULED" ? "Starts Soon" : "Not Available"}
                                            </button>
                                        )
                                    ) : (
                                        <button
                                            onClick={() => addToCart({
                                                id: sale.id,
                                                title: sale.title,
                                                price: sale.price,
                                            })}
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


      {selectedItem && (
        <BidModal
          isOpen={bidModalOpen}
          onClose={() => {
            setBidModalOpen(false);
            setSelectedItem(null);
          }}
          itemTitle={selectedItem.title}
          currentBid={selectedItem.price}
          buyoutPrice={selectedItem.buyoutPrice}
          onSubmitBid={async (amount, onError) => {
              const res = await fetch(
                  `${API_BASE_URL}/auctions/${selectedItem.auctionId}/bids?bidderId=${CURRENT_USER_ID}`,
                  {
                      method: "POST",
                      headers: { "Content-Type": "application/json" },
                      body: JSON.stringify({ value: amount })
                  }
              );
              if (res.status === 204) {
                  setBidModalOpen(false);
                  // renders the marketplace page
                  setSales(prev => prev.map(s =>
                      s.id === selectedItem.id
                          ? { ...s, price: amount }
                          : s
                  ));
                  setSelectedItem(null);
              } else if (res.status === 400) {
                  onError?.("Invalid bid. Please try a higher amount.");
              } else {
                  onError?.("Something went wrong. Please try again.");
              }
          }}
          onBuyNow={async () => {
              const res = await fetch(
                  `${API_BASE_URL}/auctions/${selectedItem.auctionId}/bids?bidderId=${CURRENT_USER_ID}`,
                  {
                      method: "POST",
                      headers: { "Content-Type": "application/json" },
                      body: JSON.stringify({ value: selectedItem.buyoutPrice })
                  }
              );
              if (res.status === 204) {
                  setBidModalOpen(false);
                  setSales(prev => prev.map(s =>
                      s.id === selectedItem.id
                          ? {...s, price: selectedItem.buyoutPrice}
                          : s
                  ));
                  addToCart({
                      id: selectedItem.id,
                      title: selectedItem.title,
                      price: selectedItem.buyoutPrice
                  });
                  setSelectedItem(null);
              }
          }}
        />
      )}
    </div>
  );
}