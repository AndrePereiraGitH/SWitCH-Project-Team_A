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
 * This repository stores {@link Country} aggregate instances in memory,
 * making it suitable for testing, prototyping, or scenarios where persistence
 * is not required.
 * </p>
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Store {@link Country} instances in an internal collection</li>
 *     <li>Prevent duplicate countries based on their name</li>
 *     <li>Provide read-only access to stored countries</li>
 *     <li>Allow lookup by {@link CountryId}</li>
 *     <li>Support flexible lookup by country name</li>
 * </ul>
 * </p>
 * <p>
 * Note: This implementation does not persist data beyond application runtime.
 * </p>
 */
public class CountryRepositoryMem implements ICountryRepository {

    private final List<Country> registeredCountries;


    /**
     * Constructs an empty in-memory repository.
     */
    public CountryRepositoryMem() {
        this.registeredCountries = new ArrayList<>();
    }

    /**
     * Saves a {@link Country} into the repository.
     * <p>
     * Ensures that the provided country is not {@code null} and that no other
     * country with the same name (case-insensitive) already exists.
     * </p>
     *
     * @param countryName the {@link Country} to save;
     *                    must not be {@code null}
     * @return the saved {@link Country} instance
     * @throws IllegalArgumentException if {@code countryName} is {@code null}
     *                                  or if a duplicate country exists
     */
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
     * Checks whether a {@link Country} is not already registered.
     * <p>
     * Duplicate detection is based on the country name using
     * case-insensitive comparison.
     * </p>
     *
     * @param country the {@link Country} to check
     * @return {@code true} if the country is not yet registered;
     *         {@code false} otherwise
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
     * Normalizes a country name into a standardized format.
     * <p>
     * This transformation is used to support flexible comparisons
     * by applying the following rules:
     * <ul>
     *     <li>Trim leading and trailing whitespace</li>
     *     <li>Replace spaces with underscores</li>
     *     <li>Replace hyphens with underscores</li>
     * </ul>
     * </p>
     *
     * @param countryName the raw country name provided by the user
     * @return a normalized representation of the country name
     */
    private String normalizeCountryName(String countryName) {
        return countryName
                .trim()
                .replace(" ", "_")
                .replace("-", "_");
    }
}