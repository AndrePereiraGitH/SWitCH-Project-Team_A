package TOPSECRET.domain.Auction;

import TOPSECRET.ddd.DomainEntity;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.BidId;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a bid placed within an {@link Auction}.
 * <p>
 * A bid is an immutable domain entity that records:
 * <ul>
 *     <li>the unique identifier of the bid,</li>
 *     <li>the auction to which it belongs,</li>
 *     <li>the identifier of the user who placed it,</li>
 *     <li>the monetary value offered,</li>
 *     <li>the exact timestamp at which the bid was made.</li>
 * </ul>
 * <p>
 * A {@code Bid} is always created and managed by the {@code Auction} aggregate root,
 * ensuring that all auction invariants (such as minimum bid value or auction state)
 * are enforced at the aggregate level.
 */
public class Bid implements DomainEntity<BidId> {

    private final BidId _bidId;
    private final AuctionId _auctionId;
    private final UserId _bidderId;
    private final Price _value;
    private final LocalDateTime _timeOfBid;

    // ============================================================
    // ============== CONSTRUCTOR =================================
    // ============================================================

    /**
     * Creates a new immutable bid instance.
     * This constructor is invoked by the {@code Auction} aggregate root.
     *
     * @param bidId      the unique identifier of the bid (must not be null)
     * @param auctionId  the identifier of the auction to which this bid belongs (must not be null)
     * @param bidderId   the identifier of the user who placed the bid (must not be null)
     * @param value      the monetary value offered in the bid (must not be null)
     * @param timeOfBid  the timestamp at which the bid was placed (must not be null)
     *
     * @throws IllegalArgumentException if bidId is null
     * @throws IllegalArgumentException if auctionId is null
     * @throws IllegalArgumentException if bidder is null
     * @throws IllegalArgumentException if value is null
     * @throws IllegalArgumentException if timeOfBid is null
     */
    protected Bid (BidId bidId, AuctionId auctionId, UserId bidderId, Price value, LocalDateTime timeOfBid) {

        if (bidId == null) {
            throw new IllegalArgumentException("Bid ID can't be null.");
        }
        if (auctionId == null) {
            throw new IllegalArgumentException("Auction ID can't be null.");
        }
        if (bidderId == null) {
            throw new IllegalArgumentException("Bidder ID can't be null.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Bid value can't be null.");
        }
        if (timeOfBid == null) {
            throw new IllegalArgumentException("Time of bid can't be null.");
        }

        this._bidId = bidId;
        this._auctionId = auctionId;
        this._bidderId = bidderId;
        this._value = value;
        this._timeOfBid = timeOfBid;
    }

    // ============================================================
    // ============== GETTERS =====================================
    // ============================================================

    @Override
    public BidId id() {
        return _bidId;
    }

    public AuctionId getAuctionId() {
        return _auctionId;
    }

    public UserId getBidderId(){
        return _bidderId;
    }

    public Price getValue(){
        return _value;
    }

    public LocalDateTime getTimeOfBid(){
        return _timeOfBid;
    }


    // ============================================================
    // =========== EQUALS AND SAMEAS ==============================
    // ============================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bid)) return false;
        Bid other = (Bid) o;
        return this._bidId.equals(other._bidId);
    }

    @Override
    public boolean sameAs(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(_auctionId, bid._auctionId)
                && Objects.equals(_bidderId, bid._bidderId)
                && Objects.equals(_value, bid._value)
                && Objects.equals(_timeOfBid, bid._timeOfBid);
    }
}
