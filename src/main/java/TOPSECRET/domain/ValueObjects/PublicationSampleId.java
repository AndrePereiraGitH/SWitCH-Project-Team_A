package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;

/**
 * Value Object representing the unique identifier of a {@code PublicationSample} aggregate.
 *
 * <p>Implements the {@link DomainID} marker interface as required by the DDD-layered structure.
 * Identity is based solely on the underlying {@code String} value, making two instances with
 * the same value equal regardless of reference.</p>
 *
 * <p>Instances of this class are immutable. Once created, the ID value cannot be changed.</p>
 */
public class PublicationSampleId implements DomainID {

    private final String pubSampleID;

    /**
     * Constructs a new {@code PublicationSampleID} with the given value.
     *
     * @param pubSampleID the string value for this ID; must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code pubSampleID} is {@code null} or blank
     */
    public PublicationSampleId(String pubSampleID) {
        if (pubSampleID == null || pubSampleID.isBlank()) {
            throw new IllegalArgumentException("ID value should not be null or blank");
        }
        this.pubSampleID = pubSampleID;
    }

    /**
     * Compares this {@code PublicationSampleID} to another object for equality.
     *
     * <p>Two instances are considered equal if and only if they are of the same type
     * and hold the same ID value.</p>
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is a {@code PublicationSampleID}
     *         with the same value; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicationSampleId that = (PublicationSampleId) o;
        return Objects.equals(pubSampleID, that.pubSampleID);
    }

    /**
     * Returns the underlying string value of this ID.
     *
     * @return the ID value; never {@code null} or blank
     */
    public String getID() {
        return pubSampleID;
    }
}