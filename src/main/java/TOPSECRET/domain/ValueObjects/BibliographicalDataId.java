package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Value Object representing the unique identifier of a {@code BibliographicalData} aggregate.
 *
 * <p>Implements the {@link DomainID} marker interface as required by the DDD-layered structure.
 * Identity is based solely on the underlying {@code String} value, making two instances with
 * the same value equal regardless of reference.</p>
 *
 * <p>Instances of this class are immutable. Once created, the ID value cannot be changed.</p>
 */

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BibliographicalDataId implements DomainID {

    private String bibliographicalDataID;

    /**
     * Constructs a new {@code PublicationSampleID} with the given value.
     *
     * @param bibliographicalDataID the string value for this ID; must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code pubSampleID} is {@code null} or blank
     */
    public BibliographicalDataId(String bibliographicalDataID) {
        if (bibliographicalDataID == null || bibliographicalDataID.isBlank()) {
            throw new IllegalArgumentException("ID value should not be null or blank");
        }
        this.bibliographicalDataID = bibliographicalDataID;
    }

    /**
     * Compares this {@code BibliographicalDataID} to another object for equality.
     *
     * <p>Two instances are considered equal if and only if they are of the same type
     * and hold the same ID value.</p>
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is a {@code BibliographicalDataID}
     *         with the same value; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BibliographicalDataId that = (BibliographicalDataId) o;
        return Objects.equals(bibliographicalDataID, that.bibliographicalDataID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bibliographicalDataID);
    }

    /**
     * Returns the underlying string value of this ID.
     *
     * @return the ID value; never {@code null} or blank
     */
    public String getID() {
        return bibliographicalDataID;
    }

}
