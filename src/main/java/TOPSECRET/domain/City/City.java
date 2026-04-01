package TOPSECRET.domain.City;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CityID;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a City within a specific country.
 *
 * <p>
 * A City contains a name and a reference to its country.
 * </p>
 *
 * <p>
 * Validation rules:
 * <ul>
 *     <li>City name must not be null or blank</li>
 *     <li>Country must not be null</li>
 * </ul>
 * </p>
 *
 * <p>
 * Identity is defined by {@link CityID}.
 * </p>
 */
public class City implements AggregateRoot<CityID> {

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

    /**
     * Returns the unique identifier of this city (AggregateRoot contract).
     *
     * @return the {@link CityID} of this city
     */
    @Override
    public CityID id() { return cityID; }

    /**
     * Returns a string representation of the city.
     *
     * @return formatted string with city data
     */
    @Override
    public String toString() {
        return "City{" +
                "cityID=" + cityID +
                ", country=" + _country +
                ", cityName='" + _cityName + '\'' +
                '}';
    }

    /**
     * Compares this city with another based on identifier.
     *
     * @param o object to compare
     * @return true if both cities have the same id, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(cityID, city.cityID);
    }

    /**
     * Returns the hash code based on the identifier.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {return Objects.hash(cityID);}

    /**
     * Compares cities based on business identity.
     *
     * @param object object to compare
     * @return true if country and name are equal, false otherwise
     */
    @Override
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