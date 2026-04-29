package TOPSECRET.persistence.mem;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.ICityRepository;

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
        if (existsByAttributes(city)) {
            throw new IllegalArgumentException("This city already exists in the system.");
        }
        _cities.add(city);
        return city;
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