package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

/**
 * Represents a unique identifier for a Collection of Publications.
 * <p>
 * This value object encapsulates the collection identifier as a non-null,
 * non-blank {@link String}. It is immutable and defines equality
 * based solely on the encapsulated identifier value.
 * <p>
 * Two {@code CollectionOfPublicationsID} instances are considered equal
 * if their underlying ID values are equal.
 */

public class CollectionOfPublicationsID implements DomainID {

    private final String collectionOfPub_id;

    /**
     * Constructs a {@code CollectionOfPublicationsID} with the specified identifier.
     *
     * @param id the unique identifier for the collection of publications
     * @throws IllegalArgumentException if {@code auctionId} is null or blank
     */

    public CollectionOfPublicationsID(String id) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank");
        }

        this.collectionOfPub_id = id;
    }

    /**
     * Compares this {@code CollectionOfPublicationsID} to another object for equality.
     * <p>
     * Two {@code CollectionOfPublicationsID} instances are equal if:
     * <ul>
     *     <li>They are the same instance, or</li>
     *     <li>The other object is a {@code CollectionOfPublicationsID}
     *         with the same ID value</li>
     * </ul>
     *
     * @param object the object to compare with
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */

    @Override
    public boolean equals(Object object) {

        if (this == object)
            return true;

        if (object instanceof CollectionOfPublicationsID) {
            CollectionOfPublicationsID collectionOfPubId = (CollectionOfPublicationsID) object;

            if (this.collectionOfPub_id.equals(collectionOfPubId.collectionOfPub_id))
                return true;
        }
        return false;
    }

    /**
     * Returns the string representation of this collection identifier.
     *
     * @return the collection ID as a {@link String}
     */

    public String getId() {

        return collectionOfPub_id;
    }
}