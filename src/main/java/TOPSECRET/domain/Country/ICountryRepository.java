package TOPSECRET.domain.Country;

import TOPSECRET.domain.ValueObjects.CountryId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Country} aggregate instances.
 *
 * <p>Provides operations grouped into three categories:</p>
 * <ul>
 *   <li><b>Persistence</b> — save operations</li>
 *   <li><b>Lookup — by identity</b> — queries using {@link CountryId}</li>
 *   <li><b>Lookup — by attributes</b> — queries using domain attributes such as name</li>
 * </ul>
 */
public interface ICountryRepository {

    // =========================================================================
    // Persistence
    // =========================================================================

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


    // =========================================================================
    // Lookup — by identity
    // =========================================================================

    /**
     * Searches for a {@link Country} by its unique {@link CountryId}.
     *
     * @param countryID the identifier of the country to search for;
     *                  must not be {@code null}
     * @return an {@link Optional} containing the matching {@link Country},
     *         or an empty {@link Optional} if no country is found
     */
    Optional<Country> findByID(CountryId countryID);


    // =========================================================================
    // Lookup — bulk
    // =========================================================================

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
    List<Country> findAll();


    // =========================================================================
    // Lookup — by attributes
    // =========================================================================

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
    Optional<CountryId> findIdByName(String countryName);
}