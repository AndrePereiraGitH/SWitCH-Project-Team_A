package TOPSECRET.domain.Auction;

import TOPSECRET.ddd.ValueObject;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a bid placed within an {@link Auction}.
 * <p>
 * A bid is a value object that records:
 * <ul>
 *     <li>the identifier of the user who placed it,</li>
 *     <li>the monetary value offered,</li>
 *     <li>the exact timestamp at which the bid was made.</li>
 * </ul>
 * <p>
 * A {@code Bid} is always created and managed by the {@code Auction} aggregate root,
 * ensuring that all auction invariants (such as minimum bid value or auction state)
 * are enforced at the aggregate level.
 */
public class Bid implements ValueObject {

    private final UserId _bidderId;
    private final Price _value;
    private final LocalDateTime _timeOfBid;

    // ============================================================
    // ============== CONSTRUCTOR =================================
    // ============================================================

    /**
     * Creates a new immutable bid instance.
     * This constructor is invoked by the {@code Auction} aggregate root.
     * The timestamp is automatically generated at creation time.
     *
     * @param bidderId   the identifier of the user who placed the bid (must not be null)
     * @param value      the monetary value offered in the bid (must not be null)
     *
     * @throws IllegalArgumentException if bidder is null
     * @throws IllegalArgumentException if value is null
     */
    protected Bid (UserId bidderId, Price value) {

        if (bidderId == null) {
            throw new IllegalArgumentException("Bidder ID can't be null.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Bid value can't be null.");
        }

        this._bidderId = bidderId;
        this._value = value;
        this._timeOfBid = LocalDateTime.now();
    }

    /**
     * Reconstructs an existing {@code Bid} instance from persisted state.
     *
     * <p>This constructor is intended to be used by the persistence layer or
     * during aggregate rehydration.
     *
     * <p>Unlike the primary constructor, this version requires all fields to be
     * explicitly provided, including the {@code timeOfBid}, ensuring that the original
     * state of the entity is preserved exactly as stored.</p>
     *
     * @param bidderId the identifier of the user who placed the bid (must not be null)
     * @param value the monetary value of the bid (must not be null)
     * @param timeOfBid the timestamp when the bid was originally placed (must not be null)
     *
     * @throws IllegalArgumentException if any parameter is null
     */
    protected Bid (UserId bidderId, Price value, LocalDateTime timeOfBid) {

        if (bidderId == null) {
            throw new IllegalArgumentException("Bidder ID can't be null.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Bid value can't be null.");
        }
        if (timeOfBid == null) {
            throw new IllegalArgumentException("TimeOfBid can't be null.");
        }
        this._bidderId = bidderId;
        this._value = value;
        this._timeOfBid = timeOfBid;
    }

    // ============================================================
    // ============== GETTERS =====================================
    // ============================================================

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
    // =========== EQUALS AND HASHCODE ============================
    // ============================================================

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(_bidderId, bid._bidderId)
                && Objects.equals(_value, bid._value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_bidderId, _value);
    }
}
