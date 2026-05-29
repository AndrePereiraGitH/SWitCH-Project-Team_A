package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.CountryService;

import javax.management.InstanceAlreadyExistsException;
import org.springframework.security.access.AccessDeniedException;

/**
 * Controller responsible for handling the registration of new {@link Country} entities.
 *
 * <p>
 * Acts as the entry point for the "Register Country" use case, delegating all business
 * logic to the {@link CountryService}.
 * </p>
 *
 * <p>
 * This controller does not contain domain logic. It simply forwards requests to the
 * service layer and returns the result.
 * </p>
 */
public class ToRegisterCountryController {
    private final CountryService countryService;

    public ToRegisterCountryController(CountryService countryService) {

        if (countryService == null)
            throw new IllegalArgumentException("CountryService cannot be null");

        this.countryService = countryService;
    }

    /**
     * Registers a new {@link Country} in the system.
     *
     * <p>
     * This method delegates the country creation and persistence logic
     * to the {@link CountryService}.
     * </p>
     *
     * @param countryName name of the country to be registered
     * @param userId identifier of the user performing the operation
     * @return the created and persisted {@link Country}
     * @throws InstanceAlreadyExistsException if a country with the same
     *         identifier already exists
     * @throws AccessDeniedException if the user does not have permission
     *         to register a country
     */
    public Country register(String countryName, UserId userId)
            throws InstanceAlreadyExistsException, AccessDeniedException {

        return countryService.createAndSave(countryName, userId);
    }
}