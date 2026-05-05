package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        ICountryRepository repository = new CountryRepositoryMem();

        Country country = mock(Country.class);
        when(country.getCountryName()).thenReturn("Portugal");
        when(country.id()).thenReturn(new CountryId("PORTUGAL"));

        // Act
        Country result = repository.save(country);

        // Assert
        assertNotNull(result);
        assertEquals(1, repository.findAll().size());
        assertEquals(country, result);
    }

    @Test
    void save_nullCountry() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void save_duplicateCountry_sameName() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();
        Country c1 = factory.create("Portugal");
        Country c2 = factory.create("Portugal");

        repository.save(c1);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(c2));

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void save_duplicateCountry_caseInsensitive() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();
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
    void findAll_returnsUnmodifiableList() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();
        Country country = factory.create("Portugal");
        repository.save(country);

        // Act
        List<Country> list = repository.findAll();

        // Assert
        assertEquals(1, list.size());

        assertThrows(UnsupportedOperationException.class,
                () -> list.add(factory.create("France")));
    }

    @Test
    void findAll_empty() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();

        // Act + Assert
        assertTrue(repository.findAll().isEmpty());
    }

    // -------------------------------------------------------------------------
    // findByID()
    // -------------------------------------------------------------------------

    @Test
    void findByID_existing() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();
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
        ICountryRepository repository = new CountryRepositoryMem();
        CountryId id = new CountryId("FRANCE");

        // Act
        Optional<Country> result = repository.findByID(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByID_null() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();

        // Act + Assert
        assertTrue(repository.findByID(null).isEmpty());
    }

    // -------------------------------------------------------------------------
    // findCountryIdByCountryName()
    // -------------------------------------------------------------------------

    @Test
    void findIdByName_valid() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();
        Country country = factory.create("Portugal");
        repository.save(country);

        // Act
        Optional<CountryId> result =
                repository.findIdByName("Portugal");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(country.id(), result.get());
    }

    @Test
    void findIdByName_null() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findIdByName(null));
    }

    @Test
    void findIdByName_blank() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findIdByName(" "));
    }

    @Test
    void findIdByName_notFound() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();

        // Act + Assert
        assertTrue(
                repository.findIdByName("Gondor").isEmpty()
        );
    }

    @Test
    void findByID_multipleCountries_wrongId_shouldNotReturnFirst() {
        // Arrange
        ICountryRepository repository = new CountryRepositoryMem();

        Country portugal = factory.create("Portugal");
        Country france = factory.create("France");

        repository.save(portugal);
        repository.save(france);

        CountryId germanyId = new CountryId("GERMANY");

        // Act
        Optional<Country> result = repository.findByID(germanyId);

        // Assert
        assertTrue(result.isEmpty());
    }
}