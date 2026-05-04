package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CountryRepositoryMem}, an in-memory implementation
 * of {@link TOPSECRET.domain.Country.ICountryRepository}.
 *
 * <p>This test suite validates the correct behavior of the repository
 * including:</p>
 * <ul>
 *     <li>Saving valid countries and preventing duplicates</li>
 *     <li>Handling null inputs appropriately</li>
 *     <li>Ensuring case-insensitive duplicate detection</li>
 *     <li>Returning an unmodifiable list of stored countries</li>
 *     <li>Finding countries by their {@link CountryId}</li>
 *     <li>Retrieving {@link CountryId} by country name with normalization rules</li>
 * </ul>
 *
 * <p>The {@link CountryFactory} is used to create valid domain objects
 * used throughout the tests.</p>
 */
class CountryRepositoryMemTest {

    private CountryFactory factory;

    @BeforeEach
    void setup() {
        factory = new CountryFactory();
    }

    // -------------------------------------------------------------------------
    // save()
    // -------------------------------------------------------------------------

    @Test
    void save_validCountry() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();
        Country country = factory.create("Portugal");

        // Act
        Country result = repository.save(country);

        // Assert
        assertNotNull(result);
        assertEquals(1, repository.getAllCountries().size());
        assertEquals(country, result);
    }

    @Test
    void save_nullCountry() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void save_duplicateCountry_sameName() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();
        Country c1 = factory.create("Portugal");
        Country c2 = factory.create("Portugal");

        repository.save(c1);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(c2));

        assertEquals(1, repository.getAllCountries().size());
    }

    @Test
    void save_duplicateCountry_caseInsensitive() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();
        Country c1 = factory.create("Portugal");
        Country c2 = factory.create("portugal");

        repository.save(c1);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(c2));
    }

    // -------------------------------------------------------------------------
    // getAllCountries()
    // -------------------------------------------------------------------------

    @Test
    void getAllCountries_returnsUnmodifiableList() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();
        Country country = factory.create("Portugal");
        repository.save(country);

        // Act
        List<Country> list = repository.getAllCountries();

        // Assert
        assertEquals(1, list.size());

        assertThrows(UnsupportedOperationException.class,
                () -> list.add(factory.create("France")));
    }

    @Test
    void getAllCountries_empty() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();

        // Act + Assert
        assertTrue(repository.getAllCountries().isEmpty());
    }

    // -------------------------------------------------------------------------
    // findByID()
    // -------------------------------------------------------------------------

    @Test
    void findByID_existing() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();
        Country country = factory.create("Portugal");
        repository.save(country);

        // Act
        Optional<Country> result =
                repository.findByID(country.id());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(country, result.get());
    }

    @Test
    void findByID_nonExisting() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();
        CountryId id = new CountryId("FRANCE");

        // Act
        Optional<Country> result = repository.findByID(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByID_null() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();

        // Act + Assert
        assertTrue(repository.findByID(null).isEmpty());
    }

    // -------------------------------------------------------------------------
    // findCountryIdByCountryName()
    // -------------------------------------------------------------------------

    @Test
    void findCountryIdByCountryName_valid() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();
        Country country = factory.create("Portugal");
        repository.save(country);

        // Act
        Optional<CountryId> result =
                repository.findCountryIdByCountryName("Portugal");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(country.id(), result.get());
    }

    @Test
    void findCountryIdByCountryName_null() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findCountryIdByCountryName(null));
    }

    @Test
    void findCountryIdByCountryName_blank() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findCountryIdByCountryName(" "));
    }

    @Test
    void findCountryIdByCountryName_notFound() {
        // Arrange
        CountryRepositoryMem repository = new CountryRepositoryMem();

        // Act + Assert
        assertTrue(
                repository.findCountryIdByCountryName("Gondor").isEmpty()
        );
    }
}