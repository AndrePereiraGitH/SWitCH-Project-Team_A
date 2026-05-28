package TOPSECRET.domain.City;

import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;

import java.util.List;
import java.util.Optional;

public interface ICityRepository {

    /**
     * Persists a {@link City} in the repository.
     *
     * @param city the city to persist; must not be {@code null}
     * @return the persisted {@link City}
     * @throws IllegalArgumentException if the city already exists or any constraint is violated
     */
    City save(City city);

    /**
     * Returns all cities persisted in the system.
     *
     * @return a list of all {@link City} domain objects; never {@code null}
     */
    List<City> findAll();

    /**
     * Returns the city matching the given identifier, if it exists.
     *
     * @param cityId the identifier to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching {@link City},
     *         or {@link Optional#empty()} if not found
     */
    Optional<City> findById(CityId cityId);

    /**
     * Checks whether a city already exists for the given country and name.
     *
     * @param countryID the identifier of the country
     * @param cityName  the name of the city
     * @return {@code true} if a matching city exists, {@code false} otherwise
     */
    boolean existsByCountryIdAndCityName(CountryId countryID, String cityName);
}