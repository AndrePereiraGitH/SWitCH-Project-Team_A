package TOPSECRET.controller;

import TOPSECRET.domain.City;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.CityRepository;

public class US008_ToRegisterACityCtrl {
    private final CityRepository _cityRepository;


    //Again, how do we validate it's a user admin here? Should the method receive a User as input and not allow the role
    // to be anything else other than ADMIN?

    public US008_ToRegisterACityCtrl(CityRepository cityRepository) {
        if (cityRepository == null) {
            throw new IllegalArgumentException("CityRepository must not be left blank");
        }

        this._cityRepository = cityRepository;
    }

    public City registerNewCity(Country country1, String cityName1) {


        return _cityRepository.createAndAddNewCity(country1, cityName1);

    }
}
