package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;


/**
 * Value Object representing the unique identity of a Bid entity inside the Auction aggregate.
 * <p>
 * A {@code BidId} encapsulates a UUID string and provides identity semantics for Bid entities.
 * It is immutable, final, and equality is based solely on its underlying UUID string value.
 * </p>
 */
public final class BidId implements DomainID {

    private final String bid_id;

    // ============================================================
    // ============== CONSTRUCTOR =================================
    // ============================================================

    /**
     * Creates a new {@code BidId} with the given UUID string value.
     * <p>
     * The provided UUID string must not be null, blank or empty. This constructor enforces
     * the immutability and validity of the identifier, ensuring that every
     * {@code BidId} instance represents a valid identity for a Bid entity.
     * </p>
     *
     * @param id the UUID string representing the bid's identity
     * @throws IllegalArgumentException if {@code id} is null, blank or empty
     */
    public BidId(String id) {
        if (id != null  && !id.isBlank() && !id.isEmpty())
            this.bid_id = id;
        else
            throw new IllegalArgumentException("Bid ID cannot be null or empty.");
    }


    // ============================================================
    // ============== GETTERS =====================================
    // ============================================================

    public String getId() {
        return bid_id;
    }


    // ============================================================
    // ============== EQUALS ======================================
    // ============================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BidId other)) return false;
        return bid_id.equals(other.bid_id);
    }
}
