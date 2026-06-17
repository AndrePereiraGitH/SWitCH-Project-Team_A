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

    private final AuctionId _auctionId;
    private final ListingId _listingId;
    private final LocalDateTime _auctionStartDateTime;
    private final LocalDateTime _auctionEndDateTime;
    private final Price _initialPrice;
    private final Price _buyoutPrice;                              // We are allowing it to be optional

    // State attributes
    private AuctionStatus _auctionStatus;
    private LocalDateTime _closedAt;
    private List<Bid> _bidHistory;

    // Final state attributes
    private Bid _winningBid;
    private Price _finalPrice;

    private Clock _clock;

    // ============================================================
    // ============== CONSTRUCTORS ================================
    // ============================================================

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
        this._initialPrice = initialPrice; // we are allow the auction initial price to be different from the listing price
        this._buyoutPrice = buyOutPrice;
        this._bidHistory = new ArrayList<>(); // starts empty history

        _auctionStatus = auctionStartDateTime.isAfter(now) ? AuctionStatus.SCHEDULED : AuctionStatus.ACTIVE;

        if (buyOutPrice != null && buyOutPrice.getPrice().compareTo(_initialPrice.getPrice()) <= 0) {
            throw new IllegalArgumentException("Buyout price must be greater than Initial price.");
        }
    }

    /**
     * Creates a new auction using the system default clock.
     *
     * @param listingId the identifier of the listing associated with the auction
     * @param auctionStartDateTime the date and time when the auction starts
     * @param auctionEndDateTime the date and time when the auction ends
     * @param initialPrice the initial price from which bidding starts
     * @param buyOutPrice the price that immediately closes the auction if matched
     *
     * @throws IllegalArgumentException if any validation rule fails
     */
    protected Auction(ListingId listingId, LocalDateTime auctionStartDateTime, LocalDateTime auctionEndDateTime,
                      Price initialPrice, Price buyOutPrice) {
        this(listingId, auctionStartDateTime, auctionEndDateTime, initialPrice, buyOutPrice, Clock.systemDefaultZone());
    }

    /**
     * Reconstructs an {@link Auction} aggregate with a predefined identifier and explicit clock.
     * <p>
     * This constructor is intended for rehydration scenarios (e.g., loading from persistence),
     * where the auction identity and core attributes are already known.
     * </p>
     *
     * @param auctionId the unique identifier of the auction (must not be {@code null})
     * @param listingId the identifier of the associated listing (must not be {@code null})
     * @param auctionStartDateTime the start date and time of the auction
     * @param auctionEndDateTime the end date and time of the auction
     * @param initialPrice the initial price of the auction
     * @param buyOutPrice the buyout price of the auction
     * @param clock the clock used for time-based operations (must not be {@code null})
     *
     * @throws IllegalArgumentException if {@code clock} is {@code null}
     */
    protected Auction(AuctionId auctionId, ListingId listingId, LocalDateTime auctionStartDateTime,
                      LocalDateTime auctionEndDateTime, Price initialPrice, Price buyOutPrice, Clock clock) {
        if (clock == null)
            throw new IllegalArgumentException("Clock cannot be null");

        this._clock = clock;
        this._auctionId = auctionId;
        this._listingId = listingId;
        this._auctionStartDateTime = auctionStartDateTime;
        this._auctionEndDateTime = auctionEndDateTime;
        this._initialPrice = initialPrice;
        this._buyoutPrice = buyOutPrice;
        this._bidHistory = new ArrayList<>();           // to prevent bidHistory to be null when calling rehydrateStatus
    }

    /**
     * Reconstructs an {@link Auction} aggregate with a predefined identifier.
     * <p>
     * This constructor is used in persistence scenarios where the
     * auction is recreated without immediately requiring time-based behavior.
     * </p>
     *
     * @param auctionId the unique identifier of the auction (must not be {@code null})
     * @param listingId the identifier of the associated listing (must not be {@code null})
     * @param auctionStartDateTime the start date and time of the auction
     * @param auctionEndDateTime the end date and time of the auction
     * @param initialPrice the initial price of the auction
     * @param buyOutPrice the buyout price of the auction
     */
    protected Auction(AuctionId auctionId, ListingId listingId, LocalDateTime auctionStartDateTime,
                      LocalDateTime auctionEndDateTime, Price initialPrice, Price buyOutPrice) {

        this._auctionId = auctionId;
        this._listingId = listingId;
        this._auctionStartDateTime = auctionStartDateTime;
        this._auctionEndDateTime = auctionEndDateTime;
        this._initialPrice = initialPrice;
        this._buyoutPrice = buyOutPrice;
        this._bidHistory = new ArrayList<>();           // to prevent bidHistory to be null when calling rehydrateStatus
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

        Bid bid = new Bid(bidderId, value);

        _bidHistory.add(bid);

        if (_buyoutPrice != null && value.getPrice().compareTo(_buyoutPrice.getPrice()) == 0) {
            _winningBid = bid;
            _auctionStatus = AuctionStatus.BOUGHT_OUT;
            _finalPrice = _buyoutPrice;
            _closedAt = LocalDateTime.now(_clock);
        }

        return bid;
    }

    /**
     * Returns the current highest bid in the auction.
     *
     * <p>The highest bid is defined as the last bid in the bid history,
     * assuming that all bids are stored in chronological order of submission.</p>
     *
     * <p>If no bids have been placed yet, this method returns {@code null}.</p>
     *
     * @return the most recent (highest) {@link Bid}, or {@code null} if no bids exist
     */
    public Bid getCurrentHighestBid() {
        if (_bidHistory.isEmpty()) return null;
        return _bidHistory.get(_bidHistory.size() - 1);
    }

    /**
     * Reconstructs a {@link Bid} instance associated with this auction.
     *
     * <p>This method is intended exclusively for persistence scenarios,
     * where a {@link Bid} is recreated from previously stored data. It bypasses
     * all business rule validations and should not be used to place new bids.</p>
     *
     * @param bidderId the identifier of the user who placed the bid (must not be {@code null})
     * @param value the monetary value of the bid (must not be {@code null})
     * @param timeOfBid the timestamp when the bid was originally placed (must not be {@code null})
     *
     * @return the reconstructed {@link Bid}
     *
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public Bid rehydrateBid(UserId bidderId, Price value, LocalDateTime timeOfBid) {
        return new Bid(bidderId, value, timeOfBid);
    }

    /**
     * Reconstructs the internal state of the auction aggregate.
     *
     * <p>This method is intended exclusively for persistence rehydration scenarios,
     * where the auction is rebuilt from stored data without triggering business rules
     * or lifecycle transitions.</p>
     *
     * <p>It restores the bid history, auction status, and closing timestamp. Based on
     * the restored state, it also derives the {@code winningBid} and {@code finalPrice}
     * if the auction is in a terminal state.</p>
     *
     * <p>Business invariants are not enforced here; it is assumed that the persisted
     * state is already valid and consistent.</p>
     *
     * @param bidHistory the historical list of bids associated with the auction;
     *                   if {@code null}, an empty list is initialized
     * @param status the persisted auction status
     * @param closedAt the timestamp when the auction was closed (may be {@code null})
     */
    public void rehydrateState(List<Bid> bidHistory, AuctionStatus status, LocalDateTime closedAt) {

        this._bidHistory = bidHistory != null
                ? new ArrayList<>(bidHistory)       // if not null creates a copy
                : new ArrayList<>();                // if null creates an empty list

        this._auctionStatus = status;
        this._closedAt = closedAt;

        if (isTerminalStatus(status)) {

            this._winningBid = getCurrentHighestBid();

            if (this._winningBid != null) {
                this._finalPrice = this._winningBid.getValue();
            } else {
                this._finalPrice = null;
            }

        } else {
            this._winningBid = null;
            this._finalPrice = null;
        }
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
            _winningBid = getCurrentHighestBid();
            _finalPrice = _winningBid.getValue();
            _auctionStatus = AuctionStatus.ENDED_WITH_WINNER;

        }

        _closedAt = now;
        return true;
    }


    // ============================================================
    // ============== GETTERS =====================================
    // ============================================================

    @Override
    public AuctionId id() {
        return _auctionId;
    }

    public ListingId getListingId() {
        return _listingId;
    }

    public LocalDateTime getAuctionStartDateTime() {
        return _auctionStartDateTime;
    }

    public LocalDateTime getAuctionEndDateTime() {
        return _auctionEndDateTime;
    }

    public Price getInitialPrice() {
        return _initialPrice;
    }

    public Price getBuyoutPrice() {
        return _buyoutPrice;
    }

    public List<Bid> getBidHistory() {
        return List.copyOf(_bidHistory);
    }

    public AuctionStatus getAuctionStatus() {
        return _auctionStatus;
    }

    public Bid getWinningBid() {
        if (!isTerminalStatus(_auctionStatus)) {
            return getCurrentHighestBid();
        }
        return _winningBid;     // returns null
    }

    public Price getFinalPrice() {
        return _finalPrice;
    }

    public LocalDateTime getClosedAt() {
        return _closedAt;
    }


    // ============================================================
    // ========== EQUALS, SAME AS AND HASHCODE ======================
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
