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
    public Country register(String countryName, UserId userId)
            throws InstanceAlreadyExistsException, AccessDeniedException {

        return countryService.createAndSave(countryName, userId);
    }
}