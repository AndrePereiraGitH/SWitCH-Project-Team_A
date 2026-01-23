package TOPSECRET.controller;

import TOPSECRET.domain.Country;
import TOPSECRET.repository.CountryRepository;

public class US007ToRegisterCountryController {

    private final CountryRepository _countryRepository;

    public US007ToRegisterCountryController(CountryRepository countryRepository) {
        this._countryRepository = countryRepository;
    }

    //Being called by the UI - "registerNewCountry (countryName)"

    public Country registerNewCountry (String _countryName) {
        return _countryRepository.createAndAddNewCountry(_countryName);
    }
}
