package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryID;

import java.util.Optional;

/**
 * Controller responsible for handling the use case of registering
 * a new {@link City} in the system.
 *
 * <p>This controller belongs to the application layer and acts as an
 * intermediary between the user interface and the domain/repository layer.
 * It coordinates the creation of a city by delegating the operation to
 * {@link ICityRepository}.</p>
 *
 * <p>The controller itself does not enforce domain rules. All validation
 * and integrity constraints are handled by the domain model and the
 * repository/factory infrastructure.</p>
 *
 * <p>Use Case: <b>US008 – Register a new city</b></p>
 */

public class US008ToRegisterACityCtrl {

    private final ICityRepository _cityRepository;
    private final ICountryRepository _countryRepository;

    //Again, how do we validate it's a user admin here? Should the method receive a User as input and not allow the role
    // to be anything else other than ADMIN?

    public US008ToRegisterACityCtrl(ICityRepository cityRepository, ICountryRepository countryRepository) {
        if (cityRepository == null) {
            throw new IllegalArgumentException("ICityRepository must not be left null");
        }

        if (countryRepository == null) {
            throw new IllegalArgumentException("ICountryRepository must not be left null");
        }

        this._cityRepository = cityRepository;
        this._countryRepository = countryRepository;
    }

    public boolean registerNewCity(String countryName, String cityName1)
            throws IllegalArgumentException {

        Optional<CountryID> countryId = _countryRepository.findCountryIdByCountryName(countryName);

        if (countryId.isEmpty()) {
            throw new IllegalArgumentException("Country with name " + countryName + " not found");
        }

        City newCity = _cityRepository.createAndAdd(countryId.get(), cityName1);

        return true;
    }
}