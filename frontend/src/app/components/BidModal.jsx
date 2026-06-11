import { useState } from "react";
import { X, Euro, Gavel } from "lucide-react";

export function BidModal({ isOpen, onClose, itemTitle, currentBid, buyoutPrice, onSubmitBid, onBuyNow }) {
  const [bidAmount, setBidAmount] = useState(currentBid + 1);
  const [bidError, setBidError] = useState(null);

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (bidAmount > currentBid) {
        setBidError(null);
        await onSubmitBid(bidAmount, (error) => setBidError(error));
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl p-8 max-w-md w-full shadow-2xl">
        <div className="flex justify-between items-start mb-6">
          <div className="flex items-center gap-3">
            <div className="p-3 bg-accent/10 rounded-xl">
              <Gavel className="w-6 h-6 text-accent" />
            </div>
            <div>
              <h2 className="font-serif text-2xl text-foreground">Place Bid</h2>
              <p className="text-sm text-muted-foreground line-clamp-1">{itemTitle}</p>
            </div>
          </div>
          <button onClick={onClose} className="p-2 hover:bg-muted rounded-lg transition-colors">
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="space-y-4 mb-6">
          <div className="p-4 bg-blue-50 rounded-xl border border-blue-200">
            <p className="text-sm text-muted-foreground mb-1">Current Bid</p>
            <p className="text-2xl font-bold text-primary flex items-center gap-1">
              <Euro className="w-6 h-6" />{currentBid.toFixed(2)}
            </p>
          </div>
          {buyoutPrice && (
            <div className="p-4 bg-accent/10 rounded-xl border border-accent/20">
              <p className="text-sm text-muted-foreground mb-1">Buyout Price</p>
              <p className="text-2xl font-bold text-accent flex items-center gap-1">
                <Euro className="w-6 h-6" />{buyoutPrice.toFixed(2)}
              </p>
            </div>
          )}
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block mb-2 font-semibold text-foreground">Your Bid</label>
            <div className="relative">
              <Euro className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
              <input
                type="number"
                value={bidAmount}
                onChange={(e) => setBidAmount(parseFloat(e.target.value))}
                min={currentBid + 0.01}
                step="0.01"
                className="w-full pl-12 pr-4 py-3 bg-input-background border-2 border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              />
            </div>
            <p className="text-xs text-muted-foreground mt-1">Minimum: €{(currentBid + 0.01).toFixed(2)}</p>
            {bidError && (
               <p className="text-sm text-red-500 mt-1">{bidError}</p>
            )}
          </div>
          <div className="flex gap-3">
            <button type="submit" className="flex-1 flex items-center justify-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-xl hover:bg-primary/90 transition-colors font-semibold">
              <Gavel className="w-5 h-5" />Submit Bid
            </button>
            {buyoutPrice && onBuyNow && (
              <button type="button" onClick={async () => { await onBuyNow() ;}} className="flex-1 px-6 py-3 bg-accent text-white rounded-xl hover:bg-accent/90 transition-colors font-semibold">
                Buy Now
              </button>
            )}
          </div>
        </form>
      </div>
    </div>
  );
}
