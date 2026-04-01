package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;

/**
 * Value Object representing the unique identifier of a {@link Publisher} aggregate.
 *
 * <p>Implements the {@link DomainID} marker interface as required by the DDD-layered structure.
 * Identity is based solely on the underlying {@code String} value, making two instances with
 * the same value equal regardless of reference.</p>
 *
 * <p>Instances of this class are immutable. Once created, the ID value cannot be changed.</p>
 */
public class PublisherID implements DomainID {

    private final String _publisherID;

    /**
     * Constructs a new {@code PublisherID} with the given value.
     *
     * @param publisherID the string value for this ID; must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code publisherID} is {@code null} or blank
     */
    public PublisherID(String publisherID) {
        if (publisherID == null || publisherID.isBlank()) {
            throw new IllegalArgumentException("PublisherId must be a non empty String");
        }
        this._publisherID = publisherID;
    }

    /**
     * Returns the underlying string value of this ID.
     *
     * @return the ID value; never {@code null} or blank
     */
    public String getId() {
        return _publisherID;
    }

    /**
     * Indicates whether some other object is equal to this {@code PublisherID}.
     *
     * <p>Two {@code PublisherID} instances are equal if they share the same
     * underlying string identifier.</p>
     *
     * @param o the reference object with which to compare
     * @return {@code true} if {@code o} is a {@code PublisherID} with an equal identifier;
     *         {@code false} otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublisherID that = (PublisherID) o;
        return Objects.equals(_publisherID, that._publisherID);
    }

    /**
     * Returns a hash code value for this {@code PublisherID}, based on its underlying string value.
     *
     * @return the hash code of this identifier's value
     */

    @Override
    public int hashCode() {
        return Objects.hash(_publisherID);
    }

    /**
     * Returns the string representation of this {@code PublisherID}.
     *
     * @return the underlying string value of this identifier
     */

    @Override
    public String toString() {
        return _publisherID;
    }
}
