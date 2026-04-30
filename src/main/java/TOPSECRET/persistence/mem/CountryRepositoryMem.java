package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.springframework.stereotype.Repository;

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
 *     <li>Allow lookup of a {@link Country} by its {@link CountryId}</li>
 * </ul>
 * </p>
 */
@Repository
public class CountryRepositoryMem implements ICountryRepository {

    private final List<Country> registeredCountries;

    public CountryRepositoryMem() {
        this.registeredCountries = new ArrayList<>();
    }

    @Override
    public Country save(Country countryName){
        if (countryName == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }

        if (!isANewCountry(countryName)) {
            throw new IllegalArgumentException("Country is already registered");
        }

        registeredCountries.add(countryName);
        return countryName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Country> getAllCountries() {
        return List.copyOf(registeredCountries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Country> findByID(CountryId countryID) {
        return registeredCountries.stream()
                .filter(c -> c.getCountryID().equals(countryID))
                .findFirst();
    }

    /**
     * Checks whether the given {@link Country} is not already registered.
     * <p>
     * Duplicate detection is performed by country name (case-insensitive),
     * since two entries representing the same real-world country should not
     * coexist, even if they carry different {@link CountryId} values.
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

    /**
     * {@inheritDoc}
     */
    public Optional<CountryId> findCountryIdByCountryName(String countryName) {

        if (countryName == null) {
            throw new NullPointerException("A country's name cannot be null");
        }

        if (countryName.isBlank()) {
            throw new IllegalArgumentException("A country's name cannot be blank");
        }

        String normalizedInput = normalizeCountryName(countryName);

        for (Country country : registeredCountries) {
            String normalizedStored = normalizeCountryName(country.getCountryName());

            if (normalizedStored.equals(normalizedInput)) {
                return Optional.of(country.getCountryID());
            }
        }

        return Optional.empty();
    }


    /**
     * Normalizes a country name into a standard format matching enum constants.
     * <p>
     * The transformation includes trimming whitespace, converting to uppercase,
     * and replacing spaces or hyphens with underscores.
     *
     * @param countryName the raw country name provided by the user
     * @return the normalized country name (e.g., "Czech republic" → "CZECH_REPUBLIC")
     */
    private String normalizeCountryName(String countryName) {
        return countryName
                .trim()
                .replace(" ", "_")
                .replace("-", "_");
    }
}