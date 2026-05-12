package TOPSECRET.domain.City;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain aggregate representing a City within a specific country.
 *
 * <p>A City is identified by a {@link CityId} and belongs to a country
 * referenced by {@link CountryId}. Identity is based solely on {@link CityId}.</p>
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>City name must not be null or blank</li>
 *     <li>CountryID must not be null</li>
 * </ul>
 */
public class City implements AggregateRoot<CityId> {

    private CityId cityID;
    private CountryId _countryId;
    private String _cityName;

    /**
     * Constructs a new City, generating a random {@link CityId}.
     *
     * @param countryID the country this city belongs to; must not be {@code null}
     * @param cityName  the name of the city; must not be {@code null} or blank
     * @throws IllegalArgumentException if any constraint is violated
     */

    protected City(CountryId countryID, String cityName) {
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("A city name cannot be left blank");
        }
        if (countryID == null) {
            throw new IllegalArgumentException("CountryID cannot be null");
        }

        _countryId = countryID;
        _cityName = cityName;
        this.cityID = new CityId(UUID.randomUUID().toString());
    }

    /**
     * Reconstitutes an existing City from persistence with a known {@link CityId}.
     *
     * @param cityID    the existing domain identifier; must not be {@code null}
     * @param countryID the country this city belongs to; must not be {@code null}
     * @param cityName  the name of the city; must not be {@code null} or blank
     * @throws IllegalArgumentException if any constraint is violated
     */
    protected City(CityId cityID, CountryId countryID, String cityName) {
        if (cityName == null || cityName.isBlank())
            throw new IllegalArgumentException("A city name cannot be left blank");
        if (countryID == null)
            throw new IllegalArgumentException("CountryID cannot be null");
        if (cityID == null)
            throw new IllegalArgumentException("CityID cannot be null");

        this.cityID = cityID;
        this._countryId = countryID;
        this._cityName = cityName;
    }

    public CountryId getCityCountryID() {
        return _countryId;
    }

    public String getCityName() {
        return _cityName;
    }

    @Override
    public CityId id() { return cityID; }

    @Override
    public String toString() {
        return "City{" +
                "cityID=" + cityID +
                ", country=" + _countryId +
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
            return this._countryId.equals(other._countryId) &&
                    this._cityName.equals(other._cityName);
        }
        return false;
    }
}