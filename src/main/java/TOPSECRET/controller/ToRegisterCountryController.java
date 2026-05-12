package TOPSECRET.controller;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.services.CountryService;

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

    /**
     * Constructs a new {@code ToRegisterCountryController}.
     *
     * @param countryService the service responsible for country operations;
     *                       must not be {@code null}
     * @throws IllegalArgumentException if {@code countryService} is {@code null}
     */
    public ToRegisterCountryController(CountryService countryService) {
        if (countryService == null) {
            throw new IllegalArgumentException("CountryService cannot be null");
        }
        this.countryService = countryService;
    }

    /**
     * Registers a new {@link Country}.
     *
     * <p>
     * Delegates the creation and persistence of the country to the
     * {@link CountryService}.
     * </p>
     *
     * @param countryName the name of the country
     * @return the created {@link Country}
     */
    public Country register(String countryName) {
        return countryService.createAndSave(countryName);
    }
}