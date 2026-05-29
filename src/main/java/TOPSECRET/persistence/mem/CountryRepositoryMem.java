package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;

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
     * {@inheritDoc}
     */
    @Override
    public Country save(Country countryName){
        if (countryName == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }

        registeredCountries.add(countryName);
        return countryName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Country> findAll() {
        return List.copyOf(registeredCountries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Country> findByID(CountryId countryID) {
        return registeredCountries.stream()
                .filter(c -> c.id().equals(countryID))
                .findFirst();
    }


    /**
     * {@inheritDoc}
     */
    public Optional<CountryId> findIdByName(String countryName) {

        String normalizedInput = normalizeCountryName(countryName);

        for (Country country : registeredCountries) {
            String normalizedStored = normalizeCountryName(country.getCountryName());

            if (normalizedStored.equals(normalizedInput)) {
                return Optional.of(country.id());
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