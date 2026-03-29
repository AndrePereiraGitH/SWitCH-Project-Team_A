package TOPSECRET.repository;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract tests for the {@link ICountryRepository} interface.
 *
 * <p>These tests verify the expected behavior defined by the
 * {@link ICountryRepository} contract, using {@link CountryRepositoryMem}
 * as the concrete implementation under test.</p>
 *
 * <p>Any future implementation of {@link ICountryRepository} should
 * satisfy these same tests.</p>
 */
class ICountryRepositoryTest {

    /** The repository under test, referenced via the interface. */
    private ICountryRepository repository;

    /**
     * Initializes the repository with a real {@link CountryFactory}
     * before each test, since the goal here is to test the contract
     * with real collaborators.
     */
    @BeforeEach
    void preparation() {
        repository = new CountryRepositoryMem(new CountryFactory());
    }

    // -------------------------------------------------------------------------
    // createAndAddNewCountry() contract tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a valid country is created and added successfully.
     */
    @Test
    void createAndAddNewCountry_validCountry() {
        Country country = repository.createAndAddNewCountry("Portugal");

        assertNotNull(country);
        assertEquals(1, repository.getAllCountries().size());
    }

    /**
     * Tests that the returned {@link Country} has the correct name.
     */
    @Test
    void createAndAddNewCountry_returnsCorrectCountryName() {
        Country country = repository.createAndAddNewCountry("Portugal");

        assertEquals("Portugal", country.getCountryName());
    }

    /**
     * Tests that the returned {@link Country} has a {@link CountryID}
     * derived from the country name in uppercase.
     */
    @Test
    void createAndAddNewCountry_returnsCountryWithDeterministicID() {
        Country country = repository.createAndAddNewCountry("Portugal");

        assertEquals("PORTUGAL", country.getCountryID().getCountryID());
    }

    /**
     * Tests that an invalid country name throws {@link IllegalArgumentException}.
     */
    @Test
    void createAndAddNewCountry_invalidCountry() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry("Mordor"));
    }

    /**
     * Tests that a {@code null} country name throws {@link IllegalArgumentException}.
     */
    @Test
    void createAndAddNewCountry_nullCountry() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry(null));
    }

    /**
     * Tests that a duplicate country name throws {@link IllegalArgumentException}.
     */
    @Test
    void createAndAddNewCountry_duplicateCountry() {
        repository.createAndAddNewCountry("Portugal");

        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry("Portugal"));
    }

    // -------------------------------------------------------------------------
    // getAllCountries() contract tests
    // -------------------------------------------------------------------------

    /**
     * Tests that an empty repository returns an empty list.
     */
    @Test
    void getAllCountries_emptyRepository() {
        assertEquals(0, repository.getAllCountries().size());
    }

    /**
     * Tests that the list returned is immutable.
     */
    @Test
    void getAllCountries_returnsReadOnlyList() {
        repository.createAndAddNewCountry("Portugal");

        List<Country> list = repository.getAllCountries();

        assertThrows(UnsupportedOperationException.class,
                () -> list.add(null));
    }

    // -------------------------------------------------------------------------
    // findByID() contract tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a country can be found by its {@link CountryID}.
     */
    @Test
    void findByID_existingID() {
        Country country = repository.createAndAddNewCountry("Portugal");
        CountryID id = country.getCountryID();

        Optional<Country> result = repository.findByID(id);

        assertTrue(result.isPresent());
        assertEquals(country, result.get());
    }

    /**
     * Tests that a non-existing {@link CountryID} returns an empty {@link Optional}.
     */
    @Test
    void findByID_nonExistingID() {
        CountryID id = new CountryID("FRANCE");

        Optional<Country> result = repository.findByID(id);

        assertFalse(result.isPresent());
    }
}