package TOPSECRET.domain.Country;

import TOPSECRET.domain.ValueObjects.CountryID;

/**
 * Aggregate Root representing a Country in the system.
 * <p>
 * A {@code Country} is uniquely identified by its {@link CountryID}, which is
 * derived from the country name in uppercase. This ensures that the same
 * country always produces the same {@link CountryID}, regardless of when
 * or by whom it is created.
 * </p>
 * <p>
 * Two {@code Country} instances are considered equal if and only if
 * they share the same {@link CountryID}.
 * </p>
 */
public class Country {

    private final CountryID _countryID;
    private final String _countryName;

    /**
     * Constructs a new {@code Country} with the given identifier and name.
     * <p>
     * Validates that neither argument is {@code null} or blank, and that
     * the name corresponds to a valid entry in the {@code Countries} enum.
     * </p>
     *
     * @param countryName the name of the country;
     *                    must not be {@code null} or blank,
     *                    and must exist in the {@code Countries} enum
     * @throws IllegalArgumentException if {@code countryID} is {@code null},
     *                                  or if {@code countryName} is {@code null},
     *                                  blank, or not a valid country in the enum
     */
    protected Country(String countryName) {

        if (countryName == null || countryName.isBlank()) {
            throw new IllegalArgumentException("Country name cannot be null or blank");
        }

        try {
            TOPSECRET.domain.Enum.Countries.valueOf(countryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Country name is not valid");
        }

        this._countryID = new CountryID(countryName.toUpperCase());;
        this._countryName = countryName;
    }

    /**
     * Returns the unique identifier of this {@code Country}.
     *
     * @return the {@link CountryID} of this country
     */
    public CountryID getCountryID() {
        return _countryID;
    }

    /**
     * Returns the name of this {@code Country}.
     *
     * @return the country name string
     */
    public String getCountryName() {
        return _countryName;
    }

    /**
     * Indicates whether some other object is equal to this {@code Country}.
     * <p>
     * Two {@code Country} instances are considered equal if and only if
     * they share the same {@link CountryID}, as the identity of an Aggregate Root
     * is defined by its identifier.
     * </p>
     *
     * @param object the object to compare with
     * @return {@code true} if the given object is a {@code Country}
     *         with the same {@link CountryID}; {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object instanceof Country) {
            Country oCountry = (Country) object;

            if (this._countryID.equals(oCountry._countryID))
                return true;
        }
        return false;
    }
}