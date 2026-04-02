package TOPSECRET.domain.City;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.CityID;
import TOPSECRET.domain.ValueObjects.CountryID;

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
    private CountryID _countryID;
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
     * @param countryID  the country this city belongs to; must not be null
     * @param cityName the name of the city; must not be null or blank
     * @throws IllegalArgumentException if country is null or cityName is null/blank
     */

    protected City(CountryID countryID, String cityName) {
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("A city name cannot be left blank");
        }
        if (countryID == null) {
            throw new IllegalArgumentException("CountryID cannot be null");
        }

        _countryID = countryID;
        _cityName = cityName;
        this.cityID = new CityID(UUID.randomUUID().toString());
    }

    public CountryID getCityCountryID() {
        return _countryID;
    }

    public String getCityName() {
        return _cityName;
    }

    @Override
    public CityID id() { return cityID; }

    @Override
    public String toString() {
        return "City{" +
                "cityID=" + cityID +
                ", country=" + _countryID +
                ", cityName='" + _cityName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(cityID, city.cityID);
    }

    @Override
    public int hashCode() {return Objects.hash(cityID);}

    @Override
    public boolean sameAs(Object object) {
        if (object instanceof City other) {
            return this._countryID.equals(other._countryID) &&
                    this._cityName.equals(other._cityName);
        }
        return false;
    }
}