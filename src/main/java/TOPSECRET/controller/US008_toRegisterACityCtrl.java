package TOPSECRET.controller;

import TOPSECRET.domain.City;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.CityRepository;

public class US008_toRegisterACityCtrl {
    private final CityRepository _cityRepository;

    public US008_toRegisterACityCtrl(CityRepository cityRepository) {
        this._cityRepository = cityRepository;
    }

    public City addCityToSystem(Country _country, String _cityName) {

        return _cityRepository.createAndAddNewCity(_country, _cityName);
    }
}
