package TOPSECRET.controller;

import TOPSECRET.domain.City;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.CityRepository;

public class US008_toRegisterACityCtrl {
    private  CityRepository _cityRepository;

    public US008_toRegisterACityCtrl(CityRepository cityRepository) { this._cityRepository = cityRepository; }

    public City addCityToSystem(Country country, City cityName) {
        return _cityRepository
    }
}
