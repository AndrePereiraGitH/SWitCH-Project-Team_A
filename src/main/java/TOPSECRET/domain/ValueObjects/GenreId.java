package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenreId implements DomainID {

    private String genreID;

    /**
     * Constructs a new {@code GenreID} with the given value.
     *
     * @param genreID the string value for this ID; must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code genreID} is {@code null} or blank
     */
    public GenreId(String genreID) {
        if (genreID == null || genreID.isBlank()) {
            throw new IllegalArgumentException("ID value should not be null or blank");
        }
        this.genreID = genreID;
    }

    public String getID() {
        return genreID;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GenreId that = (GenreId) o;
        return Objects.equals(genreID, that.genreID);
    }


    @Override
    public int hashCode() {
        return Objects.hash(genreID);
    }
}
