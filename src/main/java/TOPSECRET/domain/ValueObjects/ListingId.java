package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing the unique identity of a Listing.
 * Immutable and equality-based on its underlying UUID string.
 */

public final class ListingId implements DomainID {

    private final String _listingId;

    // --------------------------------------------------------------------------------
    // ----------------------- CONSTRUCTOR --------------------------------------------
    // --------------------------------------------------------------------------------
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
            throw new IllegalArgumentException("Valor de parâmetro 'id' não pode ser nulo ou vazio.");
        else
            this._listingId = listingId;
    }

    // --------------------------------------------------------------------------------

    /**
     * Generates a new {@code ListingId} with a randomly created UUID string.
     * <p>
     * This factory method is the preferred way to create new identifiers
     * for Listing aggregate roots.
     * </p>
     *
     * @return a new {@code ListingId} instance with a unique UUID string
     */
    public static ListingId newId() {
        return new ListingId(UUID.randomUUID().toString());
    }

    /**
     * Returns the underlying UUID string of this identifier.
     *
     * @return the UUID string representing this {@code ListingId}
     */
    public String getId() {
        return _listingId;
    }

    /**
     * Compares this identifier with another object for equality.
     * <p>
     * Two {@code ListingId} instances are considered equal if and only if
     * their underlying UUID strings are equal.
     * </p>
     *
     * @param o the object to compare with this identifier
     * @return {@code true} if the given object is a {@code ListingId} with the same UUID string;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListingId other)) return false;
        return Objects.equals(_listingId, other._listingId);
    }
}
