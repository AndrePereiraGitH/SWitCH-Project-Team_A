package TOPSECRET.domain;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CityID;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a city within a specific country.
 * <p>
 * Instances of this class can only be created by subclasses or classes
 * within the same package.
 */
public class City {

    private final CityID cityID;

    /**
     * The country this city belongs to.
     */
    private Country _country;

    /**
     * The name of the city.
     */
    private String _cityName;

    /**
     * Constructs a new City with the specified country and city name.
     * <p>
     * The constructor is protected, so it can only be called from
     * subclasses or classes in the same package.
     *
     * @param country  the country this city belongs to; must not be null
     * @param cityName the name of the city; must not be null or blank
     * @throws IllegalArgumentException if country is null or cityName is null/blank
     */

    protected City(Country country, String cityName) {
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("A city name cannot be left blank");
        }

        if (country == null) {
            throw new IllegalArgumentException("Country cannot be left blank");
        }

        _country = country;
        _cityName = cityName;
        this.cityID = new CityID(UUID.randomUUID().toString());
    }

    /**
     * Returns the country this city belongs to.
     *
     * @return the country of this city
     */
    public Country getCityCountry() {
        return _country;
    }

    /**
     * Returns the name of the city.
     *
     * @return the city name
     */
    public String getCityName() {
        return _cityName;
    }

    public CityID id() {
        return cityID;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof City city)) return false;
        return Objects.equals(_country, city._country) && Objects.equals(_cityName, city._cityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_country, _cityName);
    }

    public boolean sameAs(Object object) {

        if (object instanceof City) {
            City oCity = (City) object;

            if (
                    this._country.equals(oCity._country) &&
                            this._cityName.equals(oCity._cityName))
                return true;
        }
        return false;
    }
}