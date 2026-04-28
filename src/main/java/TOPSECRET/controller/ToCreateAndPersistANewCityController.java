package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.ValueObjects.CountryID;
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
     * @param countryID the string identifier of the country
     * @param cityName  the name of the city
     * @return the newly created and persisted {@link City}
     * @throws IllegalArgumentException if countryID or cityName violate domain constraints
     */
    public City createAndPersistCity(String countryID, String cityName) {
        CountryID countryId = new CountryID(countryID);
        City newCity = cityService.createAndSave(countryId, cityName);
        return newCity;
    }
}