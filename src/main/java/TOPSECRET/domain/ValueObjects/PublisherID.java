package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;

/**
 * Value Object representing the unique identifier of a {@link TOPSECRET.domain.Publisher} aggregate.
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
     * Compares this {@code PublisherID} to another object for equality.
     *
     * <p>Two instances are considered equal if and only if they are of the same type
     * and hold the same ID value.</p>
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is a {@code PublisherID}
     *         with the same value; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublisherID that = (PublisherID) o;
        return Objects.equals(_publisherID, that._publisherID);
    }

    @Override
    public String toString() {
        return _publisherID;
    }
}
