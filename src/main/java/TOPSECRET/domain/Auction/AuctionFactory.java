package TOPSECRET.domain.Auction;

import TOPSECRET.domain.ValueObjects.*;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Factory responsible for creating and reconstructing {@link Auction}
 * aggregate instances and related domain objects.
 * <p>
 * This factory centralizes the instantiation of {@link Auction} and {@link Bid}
 * objects, ensuring that object creation is consistent across the application.
 * </p>
 * <p>
 * Validation and business invariants are not handled here. Instead, they are
 * enforced within the domain model itself (e.g., {@link Auction} constructor),
 * preserving the aggregate as the single source of truth.
 * </p>
 * <p>
 * This factory also supports reconstruction scenarios, typically used when
 * rehydrating domain objects from persistence layers.
 * </p>
 */
@Service
public class AuctionFactory {

    /**
     * Creates a new {@link Auction} instance.
     * <p>
     * This method is intended for standard auction creation scenarios where
     * a new aggregate is instantiated without a predefined identifier.
     * </p>
     *
     * @param listingId the identifier of the listing associated with the auction (must not be {@code null})
     * @param auctionStartDateTime the date and time when the auction starts (must not be {@code null})
     * @param auctionEndDateTime the date and time when the auction ends (must not be {@code null})
     * @param initialPrice the initial price of the auction (must not be {@code null})
     * @param buyOutPrice the buyout price that immediately closes the auction if matched (may be {@code null})
     *
     * @return a new {@link Auction} instance
     *
     * @throws IllegalArgumentException if any invariant is violated by the {@link Auction} constructor
     */
    public Auction create(ListingId listingId, LocalDateTime auctionStartDateTime, LocalDateTime auctionEndDateTime,
                          Price initialPrice, Price buyOutPrice) {
        return new Auction(listingId, auctionStartDateTime, auctionEndDateTime, initialPrice, buyOutPrice);
    }

    /**
     * Reconstructs an existing {@link Auction} aggregate from persisted data.
     * <p>
     * This method is used when loading auctions from a persistence layer,
     * where the identifier and state are already defined.
     * </p>
     *
     * @param auctionId the unique identifier of the auction (must not be {@code null})
     * @param listingId the identifier of the associated listing (must not be {@code null})
     * @param auctionStartDateTime the start date and time of the auction (must not be {@code null})
     * @param auctionEndDateTime the end date and time of the auction (must not be {@code null})
     * @param initialPrice the initial price of the auction (must not be {@code null})
     * @param buyOutPrice the buyout price of the auction (may be {@code null})
     *
     * @return a reconstructed {@link Auction} instance
     *
     * @throws IllegalArgumentException if any invariant is violated by the {@link Auction} constructor
     */
    public Auction create(AuctionId auctionId, ListingId listingId, LocalDateTime auctionStartDateTime,
                          LocalDateTime auctionEndDateTime, Price initialPrice, Price buyOutPrice) {
        return new Auction(auctionId, listingId, auctionStartDateTime, auctionEndDateTime, initialPrice, buyOutPrice);
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
