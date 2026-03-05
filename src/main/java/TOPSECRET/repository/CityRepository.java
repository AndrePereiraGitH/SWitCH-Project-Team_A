package TOPSECRET.repository;

import TOPSECRET.domain.City;
import TOPSECRET.domain.CityFactory;
import TOPSECRET.domain.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link City} entities in memory.
 * <p>
 * This repository encapsulates storage and retrieval logic for cities and
 * centralizes the creation of {@code City} instances through the {@link CityFactory},
 * ensuring that domain constraints are consistently enforced.
 * <p>
 * At this stage, the repository uses in-memory persistence. This abstraction
 * allows future replacement with a database-backed implementation without
 * impacting application or domain layers.
 */

public class CityRepository {

    /**
     * In-memory collection of registered cities.
     */
    private List<City> _cities;

    /**
     * Factory used to create valid {@link City} instances.
     */
    private final CityFactory _cityFactory;

    /**
     * Creates a new {@code CityRepository}.
     *
     * @param cityFactory the factory responsible for creating {@link City} instances;
     *                    must not be {@code null}
     */
    public CityRepository(CityFactory cityFactory) {

        _cities = new ArrayList<City>();
        _cityFactory = cityFactory;

    }


    /**
     * Creates a new {@link City} and adds it to the repository.
     * <p>
     * The city is created using {@link CityFactory} to ensure domain constraints
     * are respected. If a city with the same identity already exists in the system,
     * the operation fails.
     *
     * @param _country the country to which the city belongs; must not be {@code null}
     * @param _cityName the name of the city; must not be {@code null} or blank
     * @return the newly created {@link City}
     * @throws IllegalArgumentException if the city already exists or if any domain constraint is violated
     */
    public City createAndAdd(Country _country, String _cityName) {
        City newCity = _cityFactory.create(_country, _cityName);


        if (!existsByName(newCity)) {
            _cities.add(newCity);
            return newCity;
        }
        throw new IllegalArgumentException("This city already exists in the system");
    }


    /**
     * Checks whether a city already exists in the repository.
     * <p>
     * Equality is determined using {@link City#equals(Object)}, meaning the domain
     * identity of {@code City} defines uniqueness.
     *
     * @param city the city to check for existence; must not be {@code null}
     * @return {@code true} if the city already exists, {@code false} otherwise
     */
    private boolean existsByName(City city) {

        //Again should it be "isExistent" or some other name?

        for (City newCity : this._cities) {
            if (city.equals(newCity)) {
                return true;
            }
        }
        return false;
    }

}


