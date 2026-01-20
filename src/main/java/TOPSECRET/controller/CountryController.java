package TOPSECRET.controller;

import TOPSECRET.domain.Country;
import TOPSECRET.repository.CountryRepository;

public class CountryController {

    private final CountryRepository _countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this._countryRepository = countryRepository;
    }

    //Being called by the UI - "registerNewCountry (countryName)"

    public void registerNewCountry (String _countryName) {
        _countryRepository.createAndAddNewCountry(_countryName);
    }
}
