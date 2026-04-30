package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
import TOPSECRET.services.CityService;

/**
 * Controller responsible for handling the use case of creating and
 * persisting a new {@link City}.
 *
 * <p>Belongs to the application layer and delegates all business logic
 * to {@link CityService}. Does not enforce domain rules directly.</p>
 */
public class ToCreateAndPersistANewCityController {

    private final CityService cityService;

    /**
     * Constructs the controller with the required service dependency.
     *
     * @param cityService the service to delegate city creation; must not be {@code null}
     */
    public ToCreateAndPersistANewCityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Creates and persists a new city for the given country and name.
     *
     * @param countryName the name of the country; must not be {@code null} or blank
     * @param cityName  the name of the city; must not be {@code null} or blank
     * @return the newly created and persisted {@link City}
     * @throws IllegalArgumentException if countryName or cityName are null or blank,
     *                                  or if any domain constraint is violated
     */
    public City createAndPersistCity(String countryName, String cityName) {
        if (countryName == null || countryName.isBlank())
            throw new IllegalArgumentException("Country name cannot be null or blank");
        return cityService.createAndSave(countryName, cityName);
    }
}