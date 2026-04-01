package TOPSECRET.domain.City;

import TOPSECRET.domain.Country.Country;

public interface ICityRepository {

    /**
     * Creates a new City and stores it in the repository.
     *
     * @param country  the country the city belongs to; must not be null
     * @param cityName the name of the city; must not be null or blank
     * @return the newly created {@link City}
     * @throws IllegalArgumentException if the city already exists or any constraint is violated
     */
    City createAndAdd(Country country, String cityName);
}