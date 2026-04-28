package TOPSECRET.domain.ValueObjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing a unique identifier for a {@link TOPSECRET.domain.Country.Country}.
 * <p>
 * A {@code CountryID} is immutable and is considered equal to another
 * {@code CountryID} if both share the same underlying identifier string.
 * </p>
 * <p>
 * The identifier is derived from the country name in uppercase,
 * ensuring that the same country always produces the same {@code CountryID}.
 * </p>
 */
@Embeddable
public class CountryID {

    private String countryId;

    protected CountryID() {}

    /**
     * Constructs a new {@code CountryID} with the given identifier.
     *
     * @param id the string to be used as the country identifier;
     *           must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code id} is {@code null} or blank
     */
    public CountryID(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("CountryID can't be null or empty.");
        }
        this.countryId = id;
    }

    /**
     * Indicates whether some other object is equal to this {@code CountryID}.
     * <p>
     * Two {@code CountryID} instances are considered equal if and only if
     * their underlying identifier strings are equal.
     * </p>
     *
     * @param object the object to compare with
     * @return {@code true} if the given object is a {@code CountryID}
     *         with the same identifier; {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object instanceof CountryID) {
            CountryID oCountryID = (CountryID) object;

            if (this.countryId.equals(oCountryID.countryId))
                return true;
        }
        return false;
    }

    /**
     * Returns the string identifier of this {@code CountryID}.
     *
     * @return the country identifier string
     */
    public String getCountryID() {
        return countryId;
    }
}