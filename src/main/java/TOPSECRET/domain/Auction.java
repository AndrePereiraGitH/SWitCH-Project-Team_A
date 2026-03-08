package TOPSECRET.domain;

import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.Enum.SaleType;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an auction associated with a listing.
 * <p>
 * An auction has a start and end date/time, a buyout price, a bid history,
 * and a status that changes over time according to the current clock and bids received.
 * </p>
 */
public class Auction {

    private Listing _listing;
    private LocalDateTime _auctionStartDateTime;
    private LocalDateTime _auctionEndDateTime;
    private Price _initialPrice;
    private Price _buyoutPrice;
    private List<Bid> _bidHistory;
    private AuctionStatus _auctionStatus;
    private Clock _clock;


    private Bid _winningBid;
    private Price _finalPrice;
    private LocalDateTime _closedAt;


    /**
     * Creates a new auction using the system default clock.
     *
     * @param listing the listing associated with the auction
     * @param auctionStartDateTime the date and time when the auction starts
     * @param finalDate the date and time when the auction ends
     * @param buyOutPrice the buyout price of the auction
     */
    protected Auction(Listing listing, LocalDateTime auctionStartDateTime, LocalDateTime finalDate, Price buyOutPrice) {
        this(listing, auctionStartDateTime, finalDate, buyOutPrice, Clock.systemDefaultZone());
    }

    /**
     * Creates a new auction with an explicit clock.
     * <p>
     * The clock is used to evaluate current time-dependent rules, such as validating
     * auction start dates and updating auction status.
     * </p>
     *
     * @param listing the listing associated with the auction
     * @param auctionStartDateTime the date and time when the auction starts
     * @param auctionEndDateTime the date and time when the auction ends
     * @param buyOutPrice the buyout price of the auction
     * @param clock the clock used for time-based validations and status changes
     * @throws IllegalArgumentException if:
     * <ul>
     *   <li>{@code clock} is {@code null}</li>
     *   <li>{@code listing} is {@code null}</li>
     *   <li>{@code auctionStartDateTime} is {@code null} or before the current time</li>
     *   <li>{@code auctionEndDateTime} is {@code null}, before the start date, or equal to the start date</li>
     *   <li>{@code buyOutPrice} is less than or equal to the listing initial price</li>
     * </ul>
     */
    protected Auction(Listing listing, LocalDateTime auctionStartDateTime, LocalDateTime auctionEndDateTime, Price buyOutPrice, Clock clock) {

        if (clock == null)
            throw new IllegalArgumentException("Clock cannot be null");

        this._clock = clock;
        LocalDateTime now = LocalDateTime.now(clock);

        if (listing == null)
            throw new IllegalArgumentException("Listing cannot be null.");
        if (!listing.getSaleType().equals(SaleType.AUCTION))
            throw new IllegalArgumentException("This listing is not an auction. SaleType must be AUCTION.");
        if (auctionStartDateTime == null || auctionStartDateTime.isBefore(now))
            throw new IllegalArgumentException("Initial date must not be before the current date.");
        if (auctionEndDateTime == null || auctionEndDateTime.isBefore(auctionStartDateTime) || auctionEndDateTime.isEqual(auctionStartDateTime))
            throw new IllegalArgumentException("Final date must not be before the initial date.");


        this._listing = listing;
        this._auctionStartDateTime = auctionStartDateTime;
        this._auctionEndDateTime = auctionEndDateTime;
        this._initialPrice = listing.getPrice();
        this._buyoutPrice = buyOutPrice;
        this._bidHistory = new ArrayList<>(); // starts empty history

        _auctionStatus = auctionStartDateTime.isAfter(now) ? AuctionStatus.SCHEDULED : AuctionStatus.ACTIVE;

        if (buyOutPrice.getPrice().compareTo(_initialPrice.getPrice()) <= 0) {
            throw new IllegalArgumentException("Buy out price must be greater than Initial price.");
        }

    }

    /**
     * Attempts to accept a bid for this auction.
     * <p>
     * Before accepting the bid, the method refreshes the auction status and closes the auction
     * if it has already expired. A bid is accepted only if the auction is active and the bid
     * value is greater than both the initial price and the latest accepted bid (if any).
     * </p>
     * <p>
     * If the bid equals the buyout price, the auction is immediately marked as bought out.
     * </p>
     *
     * @param bid the bid to be accepted
     * @return {@code true} if the bid is accepted and added to the bid history
     * @throws IllegalArgumentException if {@code bid} is {@code null}, or if its value is not valid
     * @throws IllegalStateException if the auction is not active
     */
    public boolean acceptBid(Bid bid) {

        boolean added = false;
        if (bid == null)
            throw new IllegalArgumentException("Bid cannot be null");

        refreshStatus();
        closeIfExpired();

        if (_auctionStatus != AuctionStatus.ACTIVE)
            throw new IllegalStateException("Auction is not active");

        if (_bidHistory == null)
            _bidHistory = new ArrayList<>();

        if (!_bidHistory.isEmpty()) {

            Bid lastBid = _bidHistory.get(_bidHistory.size() - 1);

            if (lastBid.getValue().getPrice().compareTo(bid.getValue().getPrice()) >= 0)
                throw new IllegalArgumentException("Bid value must be greater than last bid value: " + lastBid.getValue());
        }

        if (bid.getValue().getPrice().compareTo(_initialPrice.getPrice()) <= 0)
            throw new IllegalArgumentException("Bid value must be greater than initial Price.");

        _bidHistory.add(bid);
        _winningBid = bid;
        _finalPrice = bid.getValue();
        added = true;

        if (bid.getValue().getPrice().equals(_buyoutPrice.getPrice())) {
            _auctionStatus = AuctionStatus.BOUGHT_OUT;
            _closedAt = LocalDateTime.now(_clock);
            _finalPrice = _buyoutPrice;
        }

        return added;

    }


    /**
     * Refreshes the auction status based on the current time.
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

        if (_bidHistory == null || _bidHistory.isEmpty()) {
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

    public List<Bid> get_bidHistory() {
        return _bidHistory;
    }

    public AuctionStatus getAuctionStatus() {
        return _auctionStatus;
    }

    public Listing getListing() {
        return _listing;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Objects.equals(_listing, auction._listing)
                && Objects.equals(_auctionStartDateTime, auction._auctionStartDateTime)
                && Objects.equals(_auctionEndDateTime, auction._auctionEndDateTime)
                && Objects.equals(_buyoutPrice, auction._buyoutPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_listing, _auctionStartDateTime, _auctionEndDateTime, _buyoutPrice);
    }


}
