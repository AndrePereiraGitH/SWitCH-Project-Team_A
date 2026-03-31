package TOPSECRET.domain.Country;

import TOPSECRET.domain.ValueObjects.CountryID;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Country} aggregate instances.
 * <p>
 * Defines the contract for creating, retrieving, and looking up countries
 * within the system.
 * </p>
 */
public interface ICountryRepository {

    /**
     * Creates a new {@link Country} and adds it to the repository.
     *
     * @param countryName the name of the country to be created;
     *                    must not be {@code null}, blank, or already registered
     * @return the newly created {@link Country} instance
     * @throws IllegalArgumentException if the country name is invalid or already registered
     */
    Country createAndAddNewCountry(String countryName);

    /**
     * Returns an unmodifiable list of all registered countries.
     *
     * @return an unmodifiable {@link List} of all registered {@link Country} instances
     */
    List<Country> getAllCountries();

    /**
     * Searches for a {@link Country} by its unique {@link CountryID}.
     *
     * @param countryID the {@link CountryID} to search for;
     *                  must not be {@code null}
     * @return an {@link Optional} containing the matching {@link Country},
     *         or an empty {@link Optional} if no match is found
     */
    Optional<Country> findByID(CountryID countryID);
}