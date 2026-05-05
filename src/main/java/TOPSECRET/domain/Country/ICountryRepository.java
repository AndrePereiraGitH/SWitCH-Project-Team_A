package TOPSECRET.domain.Country;

import TOPSECRET.domain.ValueObjects.CountryId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Country} aggregate instances.
 * <p>
 * This interface defines the contract for accessing and persisting
 * {@link Country} aggregates, abstracting the underlying data storage mechanism.
 * </p>
 * <p>
 * Implementations of this repository are responsible for ensuring
 * consistency, uniqueness, and proper lifecycle management of {@code Country}
 * entities.
 * </p>
 */
public interface ICountryRepository {

    /**
     * Retrieves all registered {@link Country} instances.
     * <p>
     * The returned list must be unmodifiable to prevent external mutation
     * of the repository's internal state.
     * </p>
     *
     * @return an unmodifiable {@link List} containing all registered countries;
     *         never {@code null}, but may be empty
     */
    List<Country> getAllCountries();

    /**
     * Searches for a {@link Country} by its unique {@link CountryId}.
     *
     * @param countryID the identifier of the country to search for;
     *                  must not be {@code null}
     * @return an {@link Optional} containing the matching {@link Country},
     *         or an empty {@link Optional} if no country is found
     */
    Optional<Country> findByID(CountryId countryID);

    /**
     * Finds the {@link CountryId} associated with a given country name.
     * <p>
     * Implementations are expected to normalize the input in order to allow
     * flexible matching (e.g., case-insensitive comparison, handling of spaces
     * and hyphens).
     * </p>
     *
     * @param countryName the name of the country to search for;
     *                    must not be {@code null} or blank
     * @return an {@link Optional} containing the corresponding {@link CountryId}
     *         if a match is found; otherwise, an empty {@link Optional}
     * @throws NullPointerException if {@code countryName} is {@code null}
     * @throws IllegalArgumentException if {@code countryName} is blank
     */
    Optional<CountryId> findCountryIdByCountryName(String countryName);

    /**
     * Persists a {@link Country} in the repository.
     * <p>
     * Implementations may enforce constraints such as preventing duplicates
     * or ensuring consistency based on business rules.
     * </p>
     *
     * @param countryName the {@link Country} to be saved;
     *                    must not be {@code null}
     * @return the saved {@link Country} instance
     * @throws IllegalArgumentException if {@code countryName} is {@code null}
     */
    Country save(Country countryName);
}