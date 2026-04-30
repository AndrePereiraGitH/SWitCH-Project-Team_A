package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.CountryDM;
import org.springframework.stereotype.Service;

@Service
public class CountryAssembler {

    private final CountryFactory countryFactory;

    public CountryAssembler(CountryFactory countryFactory) {
        this.countryFactory = countryFactory;
    }

    /**
     * Converts a Country domain object to CountryDM for persistence.
     */
    public CountryDM DomainToDM(Country country) {

        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }

        return new CountryDM(
                country.getCountryID(),
                country.getCountryName()
        );
    }

    /**
     * Converts a CountryDM to a Country domain object.
     */
    public Country DMToDomain(CountryDM countryDM) {

        if (countryDM == null) {
            throw new IllegalArgumentException("CountryDM cannot be null");
        }

        return countryFactory.create(
                countryDM.getCountryID(),
                countryDM.getCountryName()
        );
    }
}