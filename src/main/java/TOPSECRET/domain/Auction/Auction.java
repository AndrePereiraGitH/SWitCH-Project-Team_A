package TOPSECRET.domain.Auction;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.ValueObjects.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate root representing an Auction in the domain.
 *
 * <p>An Auction is associated with a {@link ListingId} and defines a time‑bounded
 * bidding process with an initial price, a buyout price, and a chronological
 * history of {@link Bid} entities. The Auction manages its own lifecycle,
 * transitioning between scheduled, active, and terminal states based on time
 * and bidding activity.</p>
 *
 * <p>All business rules related to bidding, auction status transitions, and
 * closing logic are encapsulated within this aggregate. External validations
 * involving other aggregates (e.g., Listing sale type, seller restrictions)
 * must be enforced in the application layer.</p>
 */

public class Auction implements AggregateRoot<AuctionId> {

    private AuctionId _auctionId;
    private ListingId _listingId;
    private LocalDateTime _auctionStartDateTime;
    private LocalDateTime _auctionEndDateTime;
    private Price _initialPrice;
    private Price _buyoutPrice;                                    // We are allowing it to be optional
    private List<Bid> _bidHistory;                                 // Bid is an internal entity of the Auction aggregate
    private AuctionStatus _auctionStatus;
    private Clock _clock;


    private Bid _winningBid;                                       // Bid is an internal entity of the Auction aggregate
    private Price _finalPrice;
    private LocalDateTime _closedAt;


    // ============================================================
    // ============== CONSTRUCTORS ================================
    // ============================================================

    /**
     * Creates a new auction using the system default clock.
     *
     * @param listingId the identifier of the listing associated with the auction
     * @param auctionStartDateTime the date and time when the auction starts
     * @param finalDate the date and time when the auction ends
     * @param initialPrice the initial price from which bidding starts
     * @param buyOutPrice the price that immediately closes the auction if matched
     *
     * @throws IllegalArgumentException if any validation rule fails
     */
    protected Auction(ListingId listingId, LocalDateTime auctionStartDateTime, LocalDateTime finalDate, Price initialPrice, Price buyOutPrice) {
        this(listingId, auctionStartDateTime, finalDate, initialPrice, buyOutPrice, Clock.systemDefaultZone());
    }

    /**
     * Creates a new auction with an explicit clock.
     * <p>
     *
     * This constructor validates all temporal and price‑related invariants,
     * initializes the auction status (SCHEDULED or ACTIVE), and prepares the
     * internal bid history.</p>
     *
     * @param listingId the identifier of the listing associated with the auction
     * @param auctionStartDateTime the date and time when the auction starts
     * @param auctionEndDateTime the date and time when the auction ends
     * @param initialPrice the initial price from which bidding starts
     * @param buyOutPrice the buyout price of the auction
     * @param clock the clock used for time-based validations and status changes
     *
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>{@code listingId} is null</li>
     *     <li>{@code auctionStartDateTime} is null or before the current time</li>
     *     <li>{@code auctionEndDateTime} is null, before the start date, or equal to it</li>
     *     <li>{@code initialPrice} is null</li>
     *     <li>{@code buyOutPrice} is not greater than {@code initialPrice}</li>
     *     <li>{@code clock} is null</li>
     * </ul>
     */
    protected Auction(ListingId listingId, LocalDateTime auctionStartDateTime, LocalDateTime auctionEndDateTime,
                      Price initialPrice, Price buyOutPrice, Clock clock) {

        if (clock == null)
            throw new IllegalArgumentException("Clock cannot be null");

        this._clock = clock;
        LocalDateTime now = LocalDateTime.now(clock);

        if (listingId == null)
            throw new IllegalArgumentException("Listing ID cannot be null.");
//        if (!listingId.getSaleType().equals(SaleType.AUCTION))                    // TODO this validation should be on the application layer
//            throw new IllegalArgumentException("This listing is not an auction. SaleType must be AUCTION.");
        if (auctionStartDateTime == null || auctionStartDateTime.isBefore(now))
            throw new IllegalArgumentException("Initial date must not be before the current date.");
        if (auctionEndDateTime == null || auctionEndDateTime.isBefore(auctionStartDateTime) || auctionEndDateTime.isEqual(auctionStartDateTime))
            throw new IllegalArgumentException("Final date must not be before the initial date.");
        if (initialPrice == null)
            throw new IllegalArgumentException("Initial price cannot be null");

        this._auctionId = new AuctionId(UUID.randomUUID().toString());
        this._listingId = listingId;
        this._auctionStartDateTime = auctionStartDateTime;
        this._auctionEndDateTime = auctionEndDateTime;
//        this._initialPrice = listing.getPrice();
        this._initialPrice = initialPrice;
        this._buyoutPrice = buyOutPrice;
        this._bidHistory = new ArrayList<>(); // starts empty history

        _auctionStatus = auctionStartDateTime.isAfter(now) ? AuctionStatus.SCHEDULED : AuctionStatus.ACTIVE;

        if (buyOutPrice.getPrice().compareTo(_initialPrice.getPrice()) <= 0) {
            throw new IllegalArgumentException("Buyout price must be greater than Initial price.");
        }

    }

    // ============================================================
    // ================= BUSINESS METHODS =========================
    // ============================================================

    /**
     * Places a new bid in this auction.
     *
     * <p>This method enforces all bidding rules, including:</p>
     * <ul>
     *     <li>the auction must be ACTIVE</li>
     *     <li>the bid value must be strictly higher than the last bid (if any)</li>
     *     <li>the bid value must be strictly higher than the initial price</li>
     *     <li>the auction closes immediately if the buyout price is matched</li>
     * </ul>
     *
     * <p>The auction status is refreshed and the auction may be closed if expired
     * before accepting the bid.</p>
     *
     * @param bidderId the identifier of the user placing the bid
     * @param value the monetary value of the bid
     *
     * @return the newly created {@link Bid}
     *
     * @throws IllegalArgumentException if {@code bidderId} or {@code value} is null,
     *                                  or if the bid value violates bidding rules
     * @throws IllegalStateException if the auction is not ACTIVE
     */

    public Bid placeBid(UserId bidderId, Price value) {

        if (bidderId == null)
            throw new IllegalArgumentException("Bidder ID cannot be null");
        if (value == null)
            throw new IllegalArgumentException("Bid value cannot be null");

        closeIfExpired();
        refreshStatus();

        if (_auctionStatus != AuctionStatus.ACTIVE)
            throw new IllegalStateException("Auction is not active");

        if (_bidHistory.isEmpty()) {
            if (value.getPrice().compareTo(_initialPrice.getPrice()) <= 0)
                throw new IllegalArgumentException("Bid must be higher than initial price");
        } else {
            Price lastPrice = _bidHistory.get(_bidHistory.size() - 1).getValue();
            if (value.getPrice().compareTo(lastPrice.getPrice()) <= 0)
                throw new IllegalArgumentException("Bid must be higher than last bid");
        }


//        if (bidderId.equals(_listing.getSeller()))                        // TODO this validation should be on application layer
//            throw new IllegalArgumentException("Seller cannot bid on their own auction");

        // creates Bid
        LocalDateTime timestamp = LocalDateTime.now(_clock);

        Bid bid = new Bid(_auctionId, bidderId, value, timestamp);

        _bidHistory.add(bid);
        _winningBid = bid;
        _finalPrice = value;

        if (value.getPrice().equals(_buyoutPrice.getPrice())) {
            _auctionStatus = AuctionStatus.BOUGHT_OUT;
            _closedAt = timestamp;
            _finalPrice = _buyoutPrice;
        }

        return bid;
    }

    /**
     * Updates the auction status based on the current time.
     * <p>
     * This method does not close expired auctions. It only transitions between
     * scheduled and active states when applicable.
     * Terminal states are not modified.
     * </p>
     */
    public void refreshStatus() {
        if (_clock == null) _clock = Clock.systemDefaultZone();

        if (isTerminalStatus(_auctionStatus)) return;

        LocalDateTime now = LocalDateTime.now(_clock);

        if (now.isBefore(_auctionStartDateTime)) {
            _auctionStatus = AuctionStatus.SCHEDULED;
            return;
        }

        if (now.isBefore(_auctionEndDateTime)) {
            _auctionStatus = AuctionStatus.ACTIVE;
        }
    }

    /**
     * Checks whether the given auction status is terminal.
     * <p>
     * Terminal statuses are those that should no longer transition automatically.
     * </p>
     *
     * @param auctionStatus the status to evaluate
     * @return {@code true} if the status is terminal; {@code false} otherwise
     */
    private boolean isTerminalStatus(AuctionStatus auctionStatus) {

        if (auctionStatus == AuctionStatus.CANCELLED
                || auctionStatus == AuctionStatus.ENDED_WITH_WINNER
                || auctionStatus == AuctionStatus.ENDED_WITHOUT_BIDS
                || auctionStatus == AuctionStatus.BOUGHT_OUT) {
            return true;
        }
        return false;
    }

    /**
     * Closes the auction if it has expired and is not already in a terminal state.
     * <p>
     * If the auction has no bids, it is marked as ended without bids.
     * Otherwise, the last bid becomes the winning bid and final price.
     * </p>
     *
     * @return {@code true} if the auction was closed by this method; {@code false} otherwise
     */
    private boolean closeIfExpired() {

        if (_clock == null)
            _clock = Clock.systemDefaultZone();

        if (isTerminalStatus(_auctionStatus))
            return false;

        LocalDateTime now = LocalDateTime.now(_clock);

        if (now.isBefore(_auctionEndDateTime))
            return false;

        if (_bidHistory.isEmpty()) {
            _auctionStatus = AuctionStatus.ENDED_WITHOUT_BIDS;
            _winningBid = null;
            _finalPrice = null;

        } else {
            _winningBid = _bidHistory.get(_bidHistory.size() - 1);
            _finalPrice = _winningBid.getValue();
            _auctionStatus = AuctionStatus.ENDED_WITH_WINNER;

        }

        _closedAt = now;
        return true;
    }


    // ============================================================
    // ============== GETTERS =====================================
    // ============================================================

    public List<Bid> getBidHistory() {
        return List.copyOf(_bidHistory);
    }

    public AuctionStatus getAuctionStatus() {
        return _auctionStatus;
    }

    public ListingId getListingId() {
        return _listingId;
    }

    @Override
    public AuctionId id() {
        return _auctionId;
    }


    // ============================================================
    // ========== EQUALS, SAMEAS AND HASCODE ======================
    // ============================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Auction)) return false;
        Auction other = (Auction) o;
        return this._auctionId.equals(other._auctionId);
    }

    @Override
    public boolean sameAs(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Objects.equals(_listingId, auction._listingId)
                && Objects.equals(_auctionStartDateTime, auction._auctionStartDateTime)
                && Objects.equals(_auctionEndDateTime, auction._auctionEndDateTime)
                &&  Objects.equals(_initialPrice, auction._initialPrice)
                && Objects.equals(_buyoutPrice, auction._buyoutPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_auctionId);
    }
}
