package TOPSECRET.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Auction {

    private Listing listing;
    private LocalDateTime initialDate;
    private LocalDateTime finalDate;
    private double initialPrice;
    private double buyOutPrice;
    private List<Bid> bidHistory;

    public Auction() {}

    public Auction(Listing listing, LocalDateTime initialDate, LocalDateTime finalDate, double initialPrice, double buyOutPrice) {

        if (listing == null)
            throw new IllegalArgumentException("Listing cannot be null.");
        if (initialDate == null || initialDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Initial date must not be before the current date.");
        if (finalDate == null || finalDate.isBefore(initialDate) || finalDate.isEqual(initialDate))
            throw new IllegalArgumentException("Final date must not be before the initial date.");
        if (initialPrice <= 0)
            throw new IllegalArgumentException("Initial price must be greater than 0.");
        if (buyOutPrice <= initialPrice) {
            throw new IllegalArgumentException("Buy out price must be greater than Initial price.");
        }

        this.listing = listing;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.initialPrice = listing.get_price();
        this.buyOutPrice = buyOutPrice;
        this.initialPrice = initialPrice;
        this.bidHistory = new ArrayList<>(); // start empty history
    }

    public void acceptBid(Bid bid) {

        if (bid == null)
            throw new IllegalArgumentException("Bid cannot be null");

        if (bidHistory.isEmpty())
            bidHistory.add(bid);

        Bid lastBid = bidHistory.get(bidHistory.size() -1);

        if (lastBid.getValue() > bid.getValue())
            throw new IllegalArgumentException("Bid value must be greater than last bid value: " + lastBid.getValue());

        if (bid.getValue() < initialPrice)
            throw new IllegalArgumentException("Bid value must be greater than initial Price.");

        bidHistory.add(bid);
    }

    public List<Bid> getBidHistory() {
        return bidHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Double.compare(initialPrice, auction.initialPrice) == 0 && Double.compare(buyOutPrice, auction.buyOutPrice) == 0 && Objects.equals(listing, auction.listing) && Objects.equals(initialDate, auction.initialDate) && Objects.equals(finalDate, auction.finalDate) && Objects.equals(bidHistory, auction.bidHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listing, initialDate, finalDate, initialPrice, buyOutPrice, bidHistory);
    }
}
