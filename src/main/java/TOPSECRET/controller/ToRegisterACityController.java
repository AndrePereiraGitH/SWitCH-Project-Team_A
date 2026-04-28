package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CountryID;
import TOPSECRET.services.CityService;

/**
 * Controller responsible for handling the use case of registering
 * a new {@link City} in the system.
 *
 * <p>This controller belongs to the application layer and acts as an
 * intermediary between the user interface and the domain/repository layer.
 * It coordinates the creation of a city by delegating the operation to
 * {@link ICityRepository}.</p>
 *
 * <p>The controller itself does not enforce domain rules. All validation
 * and integrity constraints are handled by the domain model and the
 * repository/factory infrastructure.</p>
 *
 * <p>Use Case: <b>US008 – Register a new city</b></p>
 */

public class ToRegisterACityController {

    private final CityService cityService;

    //Again, how do we validate it's a user admin here? Should the method receive a User as input and not allow the role
    // to be anything else other than ADMIN?

    public ToRegisterACityController(CityService cityService) {
        if (cityService == null) {
            throw new IllegalArgumentException("CityService must not be left null");
        }

        this.cityService = cityService;
    }

    public City registerNewCity(CountryID countryID, String cityName) {
        return cityService.createAndSave(countryID, cityName);
    }
}