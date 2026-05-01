package TOPSECRET.controller;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.services.CountryService;

/**
 * Controller responsible for handling the registration of new {@link Country} entities.
 * <p>
 * This class acts as an entry point between the application layer and the domain/service layer,
 * delegating the creation and persistence of a country to the {@link CountryService}.
 * </p>
 */
public class ToRegisterCountryController {

    private final CountryService countryService;

    /**
     * Constructs a new {@code ToRegisterCountryController}.
     *
     * @param countryService the {@link CountryService} used to create and persist countries;
     *                       must not be {@code null}
     */
    public ToRegisterCountryController(CountryService countryService) {
        if(countryService == null){
            throw new IllegalArgumentException("CountryService cannot be null!");
        }
        this.countryService = countryService;
    }

    /**
     * Registers a new {@link Country} with the given name.
     * <p>
     * This method validates that the provided country name is not {@code null} or blank,
     * and then delegates the creation and persistence process to the {@link CountryService}.
     * </p>
     *
     * @param countryName the name of the country to register;
     *                    must not be {@code null} or blank
     * @return the newly created and persisted {@link Country}
     * @throws IllegalArgumentException if {@code countryName} is {@code null} or blank
     */
    public Country registerNewCountry(String countryName) {

        if (countryName == null || countryName.isBlank()) {
            throw new IllegalArgumentException("Country name cannot be null or blank");
        }

        return countryService.createAndSave(countryName);
    }
}