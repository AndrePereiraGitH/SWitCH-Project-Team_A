package TOPSECRET.domain.City;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.CityID;
import TOPSECRET.domain.ValueObjects.CountryID;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain aggregate representing a City within a specific country.
 *
 * <p>A City is identified by a {@link CityID} and belongs to a country
 * referenced by {@link CountryID}. Identity is based solely on {@link CityID}.</p>
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>City name must not be null or blank</li>
 *     <li>CountryID must not be null</li>
 * </ul>
 */
public class City implements AggregateRoot<CityID> {

    private CityID cityID;
    private CountryID _countryID;
    private String _cityName;

    /**
     * Required by JPA. Not for direct use.
     */
    protected City() {}

    /**
     * Constructs a new City, generating a random {@link CityID}.
     *
     * @param countryID the country this city belongs to; must not be {@code null}
     * @param cityName  the name of the city; must not be {@code null} or blank
     * @throws IllegalArgumentException if any constraint is violated
     */
    protected City(CountryID countryID, String cityName) {
        if (cityName == null || cityName.isBlank())
            throw new IllegalArgumentException("A city name cannot be left blank");
        if (countryID == null)
            throw new IllegalArgumentException("CountryID cannot be null");

        _countryID = countryID;
        _cityName = cityName;
        this.cityID = new CityID(UUID.randomUUID().toString());
    }

    /**
     * Reconstitutes an existing City from persistence with a known {@link CityID}.
     *
     * @param cityID    the existing domain identifier; must not be {@code null}
     * @param countryID the country this city belongs to; must not be {@code null}
     * @param cityName  the name of the city; must not be {@code null} or blank
     * @throws IllegalArgumentException if any constraint is violated
     */
    protected City(CityID cityID, CountryID countryID, String cityName) {
        if (cityName == null || cityName.isBlank())
            throw new IllegalArgumentException("A city name cannot be left blank");
        if (countryID == null)
            throw new IllegalArgumentException("CountryID cannot be null");
        if (cityID == null)
            throw new IllegalArgumentException("CityID cannot be null");

        this.cityID = cityID;
        this._countryID = countryID;
        this._cityName = cityName;
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