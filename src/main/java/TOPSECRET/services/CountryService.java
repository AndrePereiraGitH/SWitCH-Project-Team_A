package TOPSECRET.services;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for handling business operations related to {@link TOPSECRET.domain.Country.Country}.
 *
 * <p>
 * This service acts as an intermediary between the controller layer and the domain/repository layer,
 * coordinating the creation and retrieval of Country aggregates using {@link TOPSECRET.domain.Country.CountryFactory}
 * and {@link TOPSECRET.domain.Country.ICountryRepository}.
 * </p>
 *
 * <p>
 * It ensures that:
 * <ul>
 *     <li>Country creation is delegated to the factory</li>
 *     <li>Persistence is handled through the repository abstraction</li>
 *     <li>Retrieval operations are centralized in one service</li>
 * </ul>
 * </p>
 */
@Service
public class CountryService {

    private final CountryFactory countryFactory;
    private final ICountryRepository countryRepository;

    /**
     * Constructs a new {@code CountryService} with the required dependencies.
     *
     * @param countryFactory    the factory responsible for creating {@link Country} instances
     * @param countryRepository the repository responsible for persisting and retrieving countries
     */
    public CountryService(CountryFactory countryFactory, ICountryRepository countryRepository) {
        this.countryFactory = countryFactory;
        this.countryRepository = countryRepository;
    }

    /**
     * Creates a new {@link Country} using the factory and persists it in the repository.
     *
     * @param countryName the name of the country to create; must be valid according to domain rules
     * @return the created and saved {@link Country}
     */
    public Country createAndSave(String countryName) {
        Country country = countryFactory.create(countryName);
        return countryRepository.save(country);
    }
}