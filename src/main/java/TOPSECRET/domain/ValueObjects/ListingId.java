package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;

/**
 * Value Object representing the unique identity of a Listing.
 * Immutable and equality-based on its underlying UUID string.
 */

public final class ListingId implements DomainID {

    private final String _listingId;

    // ============================================================
    // ============== CONSTRUCTOR =================================
    // ============================================================

    /**
     * Creates a new {@code ListingId} with the given UUID string value.
     * <p>
     * This constructor enforces the immutability and validity of the identifier.
     * A {@code ListingId} cannot be a null, empty or blank.
     * </p>
     *
     * @param listingId the UUID string representing the unique identity of a Listing
     * @throws IllegalArgumentException if {@code listingId} is null, empty or blank
     */
    public ListingId(String listingId) {
        if (listingId == null || listingId.isBlank() || listingId.isEmpty())
            throw new IllegalArgumentException("Listing ID cannot be null or empty.");
        else
            this._listingId = listingId;
    }



    // ============================================================
    // ============== GETTERS =====================================
    // ============================================================

    public String getId() {
        return _listingId;
    }


    // ============================================================
    // ============== EQUALS ======================================
    // ============================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListingId other)) return false;
        return Objects.equals(_listingId, other._listingId);
    }

    /**
     * Returns a hash code value for this {@code ListingId}.
     * <p>
     * The hash code is derived solely from the underlying identifier string,
     * consistent with the {@link #equals(Object)} implementation.
     * </p>
     *
     * @return the hash code value for this {@code ListingId}
     */
    @Override
    public int hashCode() {
        return Objects.hash(_listingId);
    }
}
