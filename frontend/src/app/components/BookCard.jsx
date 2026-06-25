import {Clock, Euro} from "lucide-react";

const PLACEHOLDER_COVER = "https://placehold.co/400x400/e0e7ff/6366f1?text=No+Cover";

const coversByKeyword = [
    { keyword: "lord of the rings", isbn: "9780544003415" },
    { keyword: "foundation",        isbn: "9780553293357" },
    { keyword: "dom quixote",       isbn: "9780060934347" },
];

function getCoverUrl(title) {
    if (!title) return PLACEHOLDER_COVER;
    const lowerTitle = title.toLowerCase();
    const match = coversByKeyword.find(c => lowerTitle.includes(c.keyword));
    if (!match) return PLACEHOLDER_COVER;
    return `https://covers.openlibrary.org/b/isbn/${match.isbn}-M.jpg`;
}

export function BookCard({title, author, genre, price, condition, isAuction, auctionEndDate, isSold = false}) {
    return (
        <div
            className={`bg-white border-2 rounded-2xl overflow-hidden transition-all duration-300 group h-full flex flex-col relative ${isSold ? "border-gray-300 opacity-75" : "border-border hover:shadow-2xl hover:border-primary hover:-translate-y-2"}`}>
            <div
                className="aspect-square bg-gradient-to-br from-blue-100 via-blue-50 to-cyan-50 flex items-center justify-center relative overflow-hidden">
                <img
                    src={getCoverUrl(title)}
                    alt={title}
                    className="absolute inset-0 w-full h-full object-cover"
                    onError={(e) => { e.currentTarget.src = PLACEHOLDER_COVER; }}
                />
                {isSold && (
                    <div className="absolute inset-0 bg-black/40 flex items-center justify-center z-20">
    <span
        className="bg-red-600 text-white font-bold px-5 py-2 rounded-full text-sm uppercase tracking-widest shadow-lg rotate-[-10deg]">
      Sold
    </span>
                    </div>
                )}
            </div>
            <div className="p-5 flex flex-col flex-1">
                <h3 className="font-serif font-semibold text-lg line-clamp-2 mb-1 text-foreground group-hover:text-primary transition-colors min-h-[3.5rem]">{title}</h3>
                <p className="text-sm text-muted-foreground mb-3 line-clamp-1">{author}</p>
                <div className="flex items-center justify-between mb-3">
                    <span
                        className="text-xs bg-gradient-to-r from-primary/10 to-accent/10 text-primary px-3 py-1 rounded-full font-medium">{genre}</span>
                    <span className="text-xs text-muted-foreground">{condition}</span>
                </div>
                {price && (
                    <div className="flex items-center justify-between pt-3 border-t-2 border-border mt-auto">
                        <div className="flex items-center gap-1">
                            {isAuction && <Clock className="w-4 h-4 text-accent"/>}
                            <span
                                className="font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent flex items-center gap-1 text-lg">
                <Euro className="w-5 h-5 text-primary"/>
                                {price.toFixed(2)}
              </span>
                        </div>
                        {isAuction && auctionEndDate && (
                            <span className="text-xs bg-accent/10 text-accent font-semibold px-2 py-1 rounded-full">
                {Math.ceil((auctionEndDate.getTime() - Date.now()) / (1000 * 60 * 60 * 24))}d left
              </span>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}