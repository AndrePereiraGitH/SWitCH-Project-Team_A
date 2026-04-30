package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Value Object representing the unique identifier of a {@code City} entity.
 *
 * <p>Implements the {@link DomainID} marker interface as required by the DDD-layered structure.
 * Identity is based solely on the underlying {@code String} value, meaning that two instances
 * with the same value are considered equal regardless of their reference.</p>
 *
 * <p>Instances of this class are immutable. Once created, the ID value cannot be changed.</p>
 */

@Embeddable
@NoArgsConstructor
public class CityID implements DomainID {

    private  String cityID;

    /**
     * Constructs a new {@code CityID} with the given value.
     *
     * @param cityID the string value for this ID; must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code cityID} is {@code null} or blank
     */
    public CityID(String cityID) {
        if (cityID == null || cityID.isBlank()) {
            throw new IllegalArgumentException("ID value can't be null or empty.");
        }
        this.cityID = cityID;
    }

    /**
     * Compares this {@code CityID} to another object for equality.
     *
     * <p>Two instances are considered equal if and only if they are of the same type
     * and contain the same ID value.</p>
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is a {@code CityID}
     *         with the same value; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CityID that = (CityID) o;
        return Objects.equals(cityID, that.cityID);
    }

    /**
     * Returns a hash code value for this ID.
     *
     * @return the hash code based on the underlying ID value
     */
    @Override
    public int hashCode() {
        return Objects.hash(cityID);
    }

    /**
     * Returns the underlying string value of this ID.
     *
     * @return the ID value; never {@code null} or blank
     */
    public String getCityID() {
        return cityID;
    }
}