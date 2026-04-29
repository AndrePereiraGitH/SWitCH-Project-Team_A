package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import java.util.Objects;

/**
 * Value Object representing the unique identifier of a {@code Genre} aggregate.
 *
 * <p>Implements the {@link DomainID} marker interface as required by the DDD-layered structure.
 * Identity is based solely on the underlying {@code String} value, making two instances with
 * the same value equal regardless of reference.</p>
 *
 * <p>Instances of this class are immutable. Once created, the ID value cannot be changed.</p>
 */
@Embeddable
public class GenreID implements DomainID {

    private String genreID;

    /**
     * No-args constructor required by JPA/Hibernate for embeddable instantiation.
     * Not intended for use outside the persistence framework.
     */
    protected GenreID() {}
    /**
     * Constructs a new {@code GenreID} with the given value.
     *
     * @param genreID the string value for this ID; must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code genreID} is {@code null} or blank
     */
    public GenreID(String genreID) {
        if (genreID == null || genreID.isBlank()) {
            throw new IllegalArgumentException("ID value should not be null or blank");
        }
        this.genreID = genreID;
    }

    /**
     * Compares this {@code GenreID} to another object for equality.
     *
     *
     * @return {@code true} if the given object is a {@code GenreID}
     *         with the same value; {@code false} otherwise
     */
    public String getID() {
        return genreID;
    }

    /**
     * Compares this {@code GenreID} to another object for equality.
     *
     * <p>Two {@code GenreID} instances are equal if and only if their
     * underlying string values are equal.</p>
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is a {@code GenreID}
     *         with the same value; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GenreID that = (GenreID) o;
        return Objects.equals(genreID, that.genreID);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return hash code based on the underlying string value
     */
    @Override
    public int hashCode() {
        return Objects.hash(genreID);
    }
}
