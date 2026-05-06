package TOPSECRET.persistence.mem;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CountryId;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of {@link ICityRepository}.
 *
 * <p>
 * Stores {@link City} instances in a local collection.
 * Responsible for delegating creation to the factory
 * and enforcing business rules such as duplicate prevention.
 * </p>
 *
 * <p>
 * Ensures:
 * <ul>
 *     <li>No duplicate cities exist with the same name and country</li>
 * </ul>
 * </p>
 */
public class CityRepositoryMem implements ICityRepository {

    /**
     * In-memory collection of registered cities.
     */
    private List<City> _cities;

    /**
     * Creates a new {@code CityRepositoryMem}.
     *
     * @throws IllegalArgumentException if cityFactory is null
     */
    public CityRepositoryMem() {
        _cities = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public City save(City city) {
        if (existsByCountryIdAndCityName(
                city.getCityCountryID(),
                city.getCityName())) {

            throw new IllegalArgumentException("This city already exists in the system.");
        }

        _cities.add(city);
        return city;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByCountryIdAndCityName(CountryId countryId, String cityName) {
        for (City existing : _cities) {
            if (existing.getCityCountryID().equals(countryId) &&
                    existing.getCityName().equals(cityName)) {
                return true;
            }
        }
        return false;
    }
}