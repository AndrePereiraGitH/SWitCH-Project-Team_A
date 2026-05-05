package TOPSECRET.domain.ValueObjects;

import jakarta.persistence.Embeddable;
import TOPSECRET.ddd.DomainID;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class CountryId implements DomainID {

    private String countryId;

    /**
     * Constructs a new {@code CountryID} with the given identifier.
     *
     * @param id the string to be used as the country identifier;
     *           must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code id} is {@code null} or blank
     */
    public CountryId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("CountryID can't be null or empty.");
        }
        this.countryId = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object instanceof CountryId) {
            CountryId oCountryId = (CountryId) object;

            if (this.countryId.equals(oCountryId.countryId))
                return true;
        }
        return false;
    }

    public String getCountryID() {
        return countryId;
    }
}