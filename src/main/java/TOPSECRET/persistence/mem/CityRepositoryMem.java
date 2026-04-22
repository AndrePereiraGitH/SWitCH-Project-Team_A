package TOPSECRET.persistence.mem;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CountryID;

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
     * Factory used to create valid {@link City} instances.
     */
    private final CityFactory _cityFactory;

    /**
     * Creates a new {@code CityRepositoryMem}.
     *
     * @param cityFactory the factory responsible for creating {@link City} instances;
     *                    must not be {@code null}
     * @throws IllegalArgumentException if cityFactory is null
     */
    public CityRepositoryMem(CityFactory cityFactory) {
        if (cityFactory == null) {
            throw new IllegalArgumentException("CityFactory cannot be null.");
        }
        _cities = new ArrayList<>();
        _cityFactory = cityFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public City createAndAdd(CountryID countryID, String cityName) {
        City newCity = _cityFactory.create(countryID, cityName);

        if (existsByAttributes(newCity)) {
            throw new IllegalArgumentException("This city already exists in the system.");
        }

        _cities.add(newCity);
        return newCity;
    }

    /**
     * Checks if a city already exists in the repository based on its attributes.
     * <p>
     * Uses {@link City#sameAs(Object)} to compare by country and name,
     * not by identity, since the city was just created and has a new ID.
     * </p>
     *
     * @param city the city to check for existence; must not be {@code null}
     * @return {@code true} if a matching city exists, {@code false} otherwise
     */
    private boolean existsByAttributes(City city) {
        for (City existing : _cities) {
            if (existing.sameAs(city)) {
                return true;
            }
        }
        return false;
    }
}