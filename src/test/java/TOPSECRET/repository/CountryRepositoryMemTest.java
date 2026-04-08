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
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CountryRepositoryMem} class.
 *
 * <p>Uses Mockito to isolate the SUT ({@link CountryRepositoryMem})
 * from its collaborators ({@link CountryFactory} and {@link Country}).</p>
 *
 * <p>Validates the following responsibilities:</p>
 * <ul>
 *     <li>Adding valid countries</li>
 *     <li>Rejecting invalid or null country inputs</li>
 *     <li>Preventing duplicate countries</li>
 *     <li>Returning an immutable list of registered countries</li>
 *     <li>Finding a country by its {@link CountryID}</li>
 * </ul>
 */
class CountryRepositoryMemTest {

    /**
     * System Under Test: the repository responsible for storing countries.
     */
    private ICountryRepository repository;

    /**
     * Mocked collaborator responsible for creating Country objects.
     */
    private CountryFactory factoryMock;

    /**
     * Mocked Country object used in tests.
     */
    private Country countryMock1;

    /**
     * Another mocked Country object used in tests.
     */
    private Country countryMock2;

    /**
     * Initializes mocks and the repository before each test.
     */
    @BeforeEach
    void preparation() {
        factoryMock = mock(CountryFactory.class);
        countryMock1 = mock(Country.class);
        countryMock2 = mock(Country.class);
        repository = new CountryRepositoryMem(factoryMock);
    }

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a {@code null} factory throws {@link IllegalArgumentException}.
     */
    @Test
    void constructor_nullFactory() {

        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CountryRepositoryMem(null));
    }

    // -------------------------------------------------------------------------
    // createAndAddNewCountry() tests
    // -------------------------------------------------------------------------

    /**
     * Tests the successful creation and addition of valid countries.
     */
    @Test
    void createAndAddNewCountry_validCountry() {

        //Arrange
        when(factoryMock.create("Portugal")).thenReturn(countryMock1);
        when(factoryMock.create("France")).thenReturn(countryMock2);
        when(countryMock1.getCountryName()).thenReturn("Portugal");
        when(countryMock2.getCountryName()).thenReturn("France");

        //Act
        repository.createAndAddNewCountry("Portugal");
        repository.createAndAddNewCountry("France");

        //Assert
        assertEquals(2, repository.getAllCountries().size());
    }

    /**
     * Tests that an invalid country name causes the factory exception
     * to propagate and leaves the repository unchanged.
     */
    @Test
    void createAndAddNewCountry_invalidCountry() {

        //Arrange
        when(factoryMock.create("Mordor"))
                .thenThrow(new IllegalArgumentException());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry("Mordor"));
        assertEquals(0, repository.getAllCountries().size());
    }

    /**
     * Tests that a {@code null} country name causes the factory exception
     * to propagate and leaves the repository unchanged.
     */
    @Test
    void createAndAddNewCountry_nullCountry() {

        //Arrange
        when(factoryMock.create(null))
                .thenThrow(new IllegalArgumentException());

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry(null));
        assertEquals(0, repository.getAllCountries().size());
    }

    /**
     * Tests that a duplicate country (same name) is rejected.
     */
    @Test
    void createAndAddNewCountry_duplicateCountry() {

        //Arrange
        when(factoryMock.create("Portugal")).thenReturn(countryMock1);
        when(countryMock1.getCountryName()).thenReturn("Portugal");

        repository.createAndAddNewCountry("Portugal");

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry("Portugal"));
        assertEquals(1, repository.getAllCountries().size());
    }

    // -------------------------------------------------------------------------
    // getAllCountries() tests
    // -------------------------------------------------------------------------

    /**
     * Tests that the list returned by {@link CountryRepositoryMem#getAllCountries()}
     * is immutable.
     */
    @Test
    void getAllCountries_returnsReadOnlyList() {

        //Arrange
        when(factoryMock.create("Portugal")).thenReturn(countryMock1);
        when(countryMock1.getCountryName()).thenReturn("Portugal");

        repository.createAndAddNewCountry("Portugal");

        //Act
        List<Country> countryList = repository.getAllCountries();

        //Assert
        assertThrows(UnsupportedOperationException.class,
                () -> countryList.add(countryMock2));
    }

    /**
     * Tests that {@link CountryRepositoryMem#getAllCountries()}
     * returns an empty list when no countries have been added.
     */
    @Test
    void getAllCountries_emptyRepository() {

        //Arrange + act
        List<Country> result = repository.getAllCountries();

        //Assert
        assertEquals(0, result.size());
    }

    // -------------------------------------------------------------------------
    // findByID() tests
    // -------------------------------------------------------------------------

    /**
     * Tests that {@link CountryRepositoryMem#findByID(CountryID)}
     * returns the correct country when a matching ID exists.
     */
    @Test
    void findByID_existingID() {

        //Arrange
        CountryID id = new CountryID("PORTUGAL");

        when(factoryMock.create("Portugal")).thenReturn(countryMock1);
        when(countryMock1.getCountryName()).thenReturn("Portugal");
        when(countryMock1.getCountryID()).thenReturn(id);

        repository.createAndAddNewCountry("Portugal");

        //Act
        Optional<Country> result = repository.findByID(id);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(countryMock1, result.get());
    }

    /**
     * Tests that {@link CountryRepositoryMem#findByID(CountryID)}
     * returns an empty {@link Optional} when no matching ID exists.
     */
    @Test
    void findByID_nonExistingID() {

        //Arrange
        CountryID id = new CountryID("FRANCE");

        //Act
        Optional<Country> result = repository.findByID(id);

        //Assert
        assertFalse(result.isPresent());
    }

    /**
     * Tests that {@link CountryRepositoryMem#findByID(CountryID)}
     * returns the correct country when multiple countries exist in the repository,
     * ensuring the filter correctly matches by ID and not just by position.
     */
    @Test
    void findByID_returnsCorrectCountryAmongMultiple() {

        //Arrange
        CountryID idPortugal = new CountryID("PORTUGAL");
        CountryID idFrance = new CountryID("FRANCE");

        when(factoryMock.create("Portugal")).thenReturn(countryMock1);
        when(factoryMock.create("France")).thenReturn(countryMock2);
        when(countryMock1.getCountryName()).thenReturn("Portugal");
        when(countryMock2.getCountryName()).thenReturn("France");
        when(countryMock1.getCountryID()).thenReturn(idPortugal);
        when(countryMock2.getCountryID()).thenReturn(idFrance);

        repository.createAndAddNewCountry("Portugal");
        repository.createAndAddNewCountry("France");

        //Act
        Optional<Country> result = repository.findByID(idFrance);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(countryMock2, result.get());
    }

    @Test
    void findIdByCountryNameShouldReturnAValidCountryIdWhenCountryNameIsValid() {

        //Arrange
        when(factoryMock.create("Portugal")).thenReturn(countryMock1);

        Country createdCountry = repository.createAndAddNewCountry(
                "Portugal");

        CountryID countryId = mock(CountryID.class);


        when(createdCountry.getCountryName()).thenReturn("Portugal");
        when(createdCountry.getCountryID()).thenReturn(countryId);

        //Act
        Optional<CountryID> result = repository.findCountryIdByCountryName("Portugal");


        //Assert
        assertTrue(result.isPresent());
        assertEquals(countryId, result.get());
    }

    @Test
    void findIdByCountryNameShouldThrowWhenCountryNameIsNull() {

        //Assert
        assertThrows(NullPointerException.class,
                () -> repository.findCountryIdByCountryName(null));
    }

    @Test
    void findIdByCountryNameShouldThrowWhenCountryNameIsBlank() {

        //Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findCountryIdByCountryName(""));
    }

    @Test
    void findIdByCountryNameShouldReturnEmptyWhenCountryIsNotFound() {
        // Arrange
        // Just make sure the repository doesn't contain the country "Gondor"

        // Act
        Optional<CountryID> result = repository.findCountryIdByCountryName("Gondor");

        // Assert
        assertTrue(result.isEmpty());
    }
}