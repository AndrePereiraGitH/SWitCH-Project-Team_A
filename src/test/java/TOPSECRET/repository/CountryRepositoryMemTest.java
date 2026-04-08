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

class CountryRepositoryMemTest {

    private ICountryRepository repository;

    private CountryFactory factoryMock;

    private Country countryMock1;

    private Country countryMock2;

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

    @Test
    void constructor_nullFactory() {

        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CountryRepositoryMem(null));
    }

    // -------------------------------------------------------------------------
    // createAndAddNewCountry() tests
    // -------------------------------------------------------------------------

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

    @Test
    void findByID_nonExistingID() {

        //Arrange
        CountryID id = new CountryID("FRANCE");

        //Act
        Optional<Country> result = repository.findByID(id);

        //Assert
        assertFalse(result.isPresent());
    }

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
    void shouldCreateAndReturnCountry() {
        CountryFactory factory = new CountryFactory();
        CountryRepositoryMem repo = new CountryRepositoryMem(factory);

        Country result = repo.createAndAddNewCountry("Portugal");

        assertNotNull(result);
        assertEquals("Portugal", result.getCountryName());

        // Optional but strong check:
        assertTrue(repo.getAllCountries().contains(result));
        assertEquals("PORTUGAL", result.getCountryID().getCountryID());
    }
}