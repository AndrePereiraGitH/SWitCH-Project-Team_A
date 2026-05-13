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
public class ToRegisterNewCityController {

    private final CityService cityService;

    /**
     * Constructs the controller with the required service dependency.
     *
     * @param cityService the service to delegate city creation; must not be {@code null}
     */
    public ToRegisterNewCityController(CityService cityService) {

        if (cityService == null) {
            throw new IllegalArgumentException("CityService cannot be null");
        }

        this.cityService = cityService;
    }


    /**
     * Registers a new city in the system.
     *
     * @param countryName the name of the country; must not be null or blank
     * @param cityName the name of the city; must not be null or blank
     * @return the created and persisted {@link City}
     * @throws IllegalArgumentException if countryName or cityName are null or blank,
     *                                  or if the city cannot be created or persisted
     */
    public City register(String countryName, String cityName) {
        if (countryName == null || countryName.isBlank())
            throw new IllegalArgumentException("Country name cannot be null or blank");

        if (cityName == null || cityName.isBlank())
            throw new IllegalArgumentException("City name cannot be null or blank");

        return cityService.createAndSave(countryName, cityName);
    }
}