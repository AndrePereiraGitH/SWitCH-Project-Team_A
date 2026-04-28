package TOPSECRET.domain.City;

import TOPSECRET.domain.ValueObjects.CountryID;

public interface ICityRepository {

    /**
     * Creates a new City and stores it in the repository.
     *
     * @return the newly created {@link City}
     * @throws IllegalArgumentException if the city already exists or any constraint is violated
     */

        City save (City city);
}