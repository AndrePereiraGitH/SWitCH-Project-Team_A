package TOPSECRET.repository;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link ICountryRepository}.
 * <p>
 * This class is responsible for storing {@link Country} aggregate instances
 * in memory and managing their lifecycle within the application.
 * </p>
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Create new {@link Country} instances using {@link CountryFactory}</li>
 *     <li>Prevent duplicate {@link Country} entries (by name)</li>
 *     <li>Store {@link Country} objects in an internal list</li>
 *     <li>Provide read-only access to stored countries</li>
 *     <li>Allow lookup of a {@link Country} by its {@link CountryID}</li>
 * </ul>
 * </p>
 */
public class CountryRepositoryMem implements ICountryRepository {

    private final List<Country> registeredCountries;
    private final CountryFactory countryFactory;

    /**
     * Constructs a {@code CountryRepositoryMem} with the given factory.
     *
     * @param countryFactory the factory responsible for creating {@link Country} instances;
     *                       must not be {@code null}
     * @throws IllegalArgumentException if {@code countryFactory} is {@code null}
     */
    public CountryRepositoryMem(CountryFactory countryFactory) {
        if (countryFactory == null) {
            throw new IllegalArgumentException("CountryFactory cannot be null");
        }
        this.countryFactory = countryFactory;
        this.registeredCountries = new ArrayList<>();
    }

    /**
     * Creates a new {@link Country} using the factory and adds it to the repository.
     * <p>
     * Ensures that no duplicate country (by name) is added to the repository.
     * </p>
     *
     * @param countryName the name of the country to be created;
     *                    must not be {@code null}, blank, or already registered
     * @return the newly created {@link Country} instance
     * @throws IllegalArgumentException if the country name is invalid or already registered
     */
    @Override
    public Country createAndAddNewCountry(String countryName) {
        Country country = countryFactory.create(countryName);
        if (!isANewCountry(country)) {
            throw new IllegalArgumentException("Country is already registered");
        }
        registeredCountries.add(country);
        return country;
    }

    /**
     * Returns an unmodifiable list of all registered countries.
     * <p>
     * The returned list is a snapshot of the current state of the repository,
     * preventing any external modification of the internal state.
     * </p>
     *
     * @return an unmodifiable {@link List} of all registered {@link Country} instances
     */
    @Override
    public List<Country> getAllCountries() {
        return List.copyOf(registeredCountries);
    }

    /**
     * Searches for a {@link Country} by its unique {@link CountryID}.
     *
     * @param countryID the {@link CountryID} to search for;
     *                  must not be {@code null}
     * @return an {@link Optional} containing the matching {@link Country},
     *         or an empty {@link Optional} if no match is found
     */
    @Override
    public Optional<Country> findByID(CountryID countryID) {
        return registeredCountries.stream()
                .filter(c -> c.getCountryID().equals(countryID))
                .findFirst();
    }

    /**
     * Checks whether the given {@link Country} is not already registered.
     * <p>
     * Duplicate detection is performed by country name (case-insensitive),
     * since two entries representing the same real-world country should not
     * coexist, even if they carry different {@link CountryID} values.
     * </p>
     *
     * @param country the {@link Country} to check
     * @return {@code true} if no country with the same name exists; {@code false} otherwise
     */
    private boolean isANewCountry(Country country) {
        for (Country registered : registeredCountries) {
            if (registered.getCountryName()
                    .equalsIgnoreCase(country.getCountryName())) {
                return false;
            }
        }
        return true;
    }
}