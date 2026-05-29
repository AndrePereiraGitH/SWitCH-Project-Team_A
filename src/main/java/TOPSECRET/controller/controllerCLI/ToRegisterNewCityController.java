package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.CityService;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;

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
     *<p>Delegates to the service layer which is responsible for creating the
     * City object and ensuring no duplicates exist.</p>
     *
     * @param countryName the name of the country; must not be null or blank
     * @param cityName the name of the city; must not be null or blank
     * @return {@code true} if the city was successfully created and persisted
     * @throws IllegalArgumentException if the city cannot be created or persisted
     */
    public boolean registerCity(String countryName, String cityName, UserId userId) throws AccessDeniedException, InstanceAlreadyExistsException {

        cityService.createAndSave(countryName, cityName, userId);
        return true;
    }
}