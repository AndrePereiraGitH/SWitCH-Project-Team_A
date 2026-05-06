package TOPSECRET.domain.City;

import TOPSECRET.domain.ValueObjects.CountryId;

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
     * Checks whether a city already exists for the given country and name.
     *
     * @param countryID the identifier of the country
     * @param cityName  the name of the city
     * @return {@code true} if a matching city exists, {@code false} otherwise
     */
    boolean existsByCountryIdAndCityName(CountryId countryID, String cityName);
}