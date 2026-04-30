package TOPSECRET.services;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private final CountryFactory countryFactory;
    private final ICountryRepository countryRepository;

    public CountryService(CountryFactory countryFactory, ICountryRepository countryRepository) {
        this.countryFactory = countryFactory;
        this.countryRepository = countryRepository;
    }

        public Country createAndSaveCountry(String countryName ) {
            Country country = countryFactory.create(countryName);
            return countryRepository.save(country);
        }

        public Optional<Country> getCountryById(CountryId countryID ) {

            Optional<Country> opCountry = countryRepository.findByID(countryID);

            return opCountry;
        }

        public List<Country> getAll() {

            List<Country> lstCountries = countryRepository.getAllCountries();

            return lstCountries;
        }

    }
