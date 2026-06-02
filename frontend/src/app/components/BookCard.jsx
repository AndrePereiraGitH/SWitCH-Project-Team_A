import { Book, Clock, Euro } from "lucide-react";

export function BookCard({ title, author, genre, price, condition, isAuction, auctionEndDate }) {
  return (
    <div className="bg-white border-2 border-border rounded-2xl overflow-hidden hover:shadow-2xl hover:border-primary transition-all duration-300 hover:-translate-y-2 group h-full flex flex-col">
      <div className="aspect-square bg-gradient-to-br from-blue-100 via-blue-50 to-cyan-50 flex items-center justify-center relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-primary/5 to-accent/5"></div>
        <Book className="w-20 h-20 text-primary/40 relative z-10" />
      </div>
      <div className="p-5 flex flex-col flex-1">
        <h3 className="font-serif font-semibold text-lg line-clamp-2 mb-1 text-foreground group-hover:text-primary transition-colors min-h-[3.5rem]">{title}</h3>
        <p className="text-sm text-muted-foreground mb-3 line-clamp-1">{author}</p>
        <div className="flex items-center justify-between mb-3">
          <span className="text-xs bg-gradient-to-r from-primary/10 to-accent/10 text-primary px-3 py-1 rounded-full font-medium">{genre}</span>
          <span className="text-xs text-muted-foreground">{condition}</span>
        </div>
        {price && (
          <div className="flex items-center justify-between pt-3 border-t-2 border-border mt-auto">
            <div className="flex items-center gap-1">
              {isAuction && <Clock className="w-4 h-4 text-accent" />}
              <span className="font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent flex items-center gap-1 text-lg">
                <Euro className="w-5 h-5 text-primary" />
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
