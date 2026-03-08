package TOPSECRET.domain;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Factory responsible for creating {@link Auction} instances.
 */
public class AuctionFactory {

    /**
     * Creates a new {@link Auction} for the given listing.
     *
     * @param listing the listing that will be auctioned (must not be {@code null})
     * @param auctionStartDateTime the date and time when the auction starts (must not be {@code null})
     * @param finalDate the date and time when the auction ends (must not be {@code null})
     * @param buyOutPrice the optional buyout price that allows immediate purchase (may be {@code null} if not supported)
     * @return a new {@link Auction} instance
     * @throws NullPointerException if {@code listing}, {@code auctionStartDateTime}, or {@code finalDate} is {@code null}
     */
    public Auction create(Listing listing, LocalDateTime auctionStartDateTime, LocalDateTime finalDate, Price buyOutPrice) {
        return new Auction(listing, auctionStartDateTime, finalDate, buyOutPrice);
    }

    // --- only for tests ----
    protected Auction create(Listing listing, LocalDateTime start, LocalDateTime end, Price buyOutPrice, Clock clock) {
        return new Auction(listing, start, end, buyOutPrice, clock);
    }

}
