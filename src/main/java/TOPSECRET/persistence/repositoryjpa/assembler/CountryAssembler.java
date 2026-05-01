package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.CountryDM;
import org.springframework.stereotype.Service;

/**
 * Assembler responsible for converting between {@link Country} domain objects
 * and {@link CountryDM} data models used for JPA persistence.
 * <p>
 * This class acts as a bridge between the domain layer and the persistence layer,
 * ensuring that domain objects are correctly transformed into a format suitable
 * for storage, and vice versa.
 * </p>
 * <p>
 * It also delegates object reconstruction to the {@link CountryFactory},
 * ensuring that domain invariants are preserved when rehydrating entities
 * from persistence.
 * </p>
 */
@Service
public class CountryAssembler {

    private final CountryFactory countryFactory;

    /**
     * Constructs a new {@code CountryAssembler}.
     *
     * @param countryFactory the factory used to reconstruct {@link Country} domain objects;
     *                       must not be {@code null}
     */
    public CountryAssembler(CountryFactory countryFactory) {
        this.countryFactory = countryFactory;
    }

    /**
     * Converts a {@link Country} domain object into a {@link CountryDM}
     * suitable for persistence.
     * <p>
     * This method extracts the relevant fields from the domain object and
     * maps them into the corresponding data model representation.
     * </p>
     *
     * @param country the {@link Country} domain object to convert;
     *                must not be {@code null}
     * @return a {@link CountryDM} representing the given country
     * @throws IllegalArgumentException if {@code country} is {@code null}
     */
    public CountryDM ToDM(Country country) {

        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }

        return new CountryDM(
                country.getCountryID(),
                country.getCountryName()
        );
    }

    /**
     * Converts a {@link CountryDM} data model into a {@link Country}
     * domain object.
     * <p>
     * This method delegates the creation of the domain object to the
     * {@link CountryFactory}, ensuring that all domain rules and invariants
     * are respected during reconstruction.
     * </p>
     *
     * @param countryDM the {@link CountryDM} to convert;
     *                  must not be {@code null}
     * @return a reconstructed {@link Country} domain object
     * @throws IllegalArgumentException if {@code countryDM} is {@code null}
     */
    public Country ToDomain(CountryDM countryDM) {

        if (countryDM == null) {
            throw new IllegalArgumentException("CountryDM cannot be null");
        }

        return countryFactory.create(
                countryDM.getCountryID(),
                countryDM.getCountryName()
        );
    }
}