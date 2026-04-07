package TOPSECRET.domain.Auction;

import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Factory responsible for creating {@link Auction} aggregate instances.
 *
 * <p>This factory does not perform validation. All business rules and
 * invariant checks are enforced inside the {@link Auction} constructor,
 * ensuring that the aggregate remains the single source of truth for
 * domain consistency.</p>
 */
public class AuctionFactory {

    /**
     * Creates a new {@link Auction} using the system default clock.
     *
     * @param listingId the identifier of the listing associated with the auction (must not be {@code null})
     * @param auctionStartDateTime the date and time when the auction starts (must not be {@code null})
     * @param finalDate the date and time when the auction ends (must not be {@code null})
     * @param initialPrice the initial price of the auction (must not be {@code null})
     * @param buyOutPrice the buyout price that immediately closes the auction if matched (can be {@code null})
     *
     * @return a new {@link Auction} instance
     *
     * @throws IllegalArgumentException if any invariant is violated by the {@link Auction} constructor
     */
    public Auction create(ListingId listingId, LocalDateTime auctionStartDateTime, LocalDateTime finalDate, Price initialPrice, Price buyOutPrice) {
        return new Auction(listingId, auctionStartDateTime, finalDate, initialPrice, buyOutPrice);
    }

    // --- only for tests ----
    /**
     * Creates a new {@link Auction} using an explicit {@link Clock}.
     *
     * <p>This method exists primarily for testing time‑dependent behaviour,
     * allowing deterministic control over the current time.</p>
     *
     * @param listingId the identifier of the listing associated with the auction
     * @param start the auction start date and time
     * @param end the auction end date and time
     * @param initialPrice the initial price of the auction
     * @param buyOutPrice the buyout price of the auction
     * @param clock the clock used for time‑based evaluations
     *
     * @return a new {@link Auction} instance
     */
    protected Auction create(ListingId listingId, LocalDateTime start, LocalDateTime end, Price initialPrice, Price buyOutPrice, Clock clock) {
        return new Auction(listingId, start, end, initialPrice, buyOutPrice, clock);
    }

}
