package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.UUID;

/**
 * Value Object representing the unique identity of an Auction.
 * <p>
 * This identifier is immutable and equality is based solely on its
 * underlying UUID string value. Two {@code AuctionId} instances are
 * considered equal if they encapsulate the same UUID string identifier.
 * </p>
 */

public final class AuctionId implements DomainID {

    private final String auc_id;

    // --------------------------------------------------------------------------------
    // ----------------------- CONSTRUCTOR --------------------------------------------
    // --------------------------------------------------------------------------------
    /**
     * Creates a new {@code AuctionId} with the given UUID string value.
     *
     * @param id the UUID string representing the auction's identity; must not be null, blank or empty
     * @throws IllegalArgumentException if {@code id} is null, blank or empty
     */

    public AuctionId(String id) {
        if (id != null && !id.isBlank() && !id.isEmpty())
            this.auc_id = id;
        else
            throw new IllegalArgumentException("Auction ID cannot be null or empty.");
    }

    // --------------------------------------------------------------------------------

    /**
     * Returns the underlying UUID string value of this identifier.
     *
     * @return the UUID string representing this {@code AuctionId}
     */
    public String getId() {
        return auc_id;
    }

    /**
     * Compares this {@code AuctionId} with another object for equality.
     * <p>
     * Two {@code AuctionId} instances are equal if and only if their
     * underlying UUID string values are equal.
     * </p>
     *
     * @param o the object to compare with this identifier
     * @return {@code true} if the given object is an {@code AuctionId} with the same UUID string;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuctionId other)) return false;
        return auc_id.equals(other.auc_id);
    }
}
