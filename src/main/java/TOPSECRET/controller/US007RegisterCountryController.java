package TOPSECRET.controller;

import TOPSECRET.domain.Country;
import TOPSECRET.repository.CountryRepository;

public class US007RegisterCountryController {

    private final CountryRepository _countryRepository;

    public US007RegisterCountryController(CountryRepository countryRepository) {
        this._countryRepository = countryRepository;
    }

    //Being called by the UI - "registerNewCountry (countryName)"

    public Country registerNewCountry (String _countryName) {
        return _countryRepository.createAndAddNewCountry(_countryName);
    }
}
