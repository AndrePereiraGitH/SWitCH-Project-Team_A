package TOPSECRET.controller;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.services.CountryService;

/**
 * Controller responsible for registering a new country.
 */
public class ToRegisterCountryController {

    private final CountryService countryService;

    public ToRegisterCountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    public Country registerNewCountry(String countryName) {

        if (countryName == null || countryName.isBlank()) {
            throw new IllegalArgumentException("Country name cannot be null or blank");
        }

        return countryService.createAndSaveCountry(countryName);
    }
}