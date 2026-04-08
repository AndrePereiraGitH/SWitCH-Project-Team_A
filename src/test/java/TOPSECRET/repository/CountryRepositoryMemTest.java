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

    private CountryFactory factoryDouble;

    private Country countryDouble1;

    private Country countryDouble2;

    @BeforeEach
    void preparation() {
        factoryDouble = mock(CountryFactory.class);
        countryDouble1 = mock(Country.class);
        countryDouble2 = mock(Country.class);
        repository = new CountryRepositoryMem(factoryDouble);
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
        when(factoryDouble.create("Portugal")).thenReturn(countryDouble1);
        when(factoryDouble.create("France")).thenReturn(countryDouble2);
        when(countryDouble1.getCountryName()).thenReturn("Portugal");
        when(countryDouble2.getCountryName()).thenReturn("France");

        //Act
        repository.createAndAddNewCountry("Portugal");
        repository.createAndAddNewCountry("France");

        //Assert
        assertEquals(2, repository.getAllCountries().size());
    }

    @Test
    void createAndAddNewCountry_invalidCountry() {

        //Arrange
        when(factoryDouble.create("Mordor"))
                .thenThrow(new IllegalArgumentException());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry("Mordor"));
        assertEquals(0, repository.getAllCountries().size());
    }

    @Test
    void createAndAddNewCountry_nullCountry() {

        //Arrange
        when(factoryDouble.create(null))
                .thenThrow(new IllegalArgumentException());

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry(null));
        assertEquals(0, repository.getAllCountries().size());
    }

    @Test
    void createAndAddNewCountry_duplicateCountry() {

        //Arrange
        when(factoryDouble.create("Portugal")).thenReturn(countryDouble1);
        when(countryDouble1.getCountryName()).thenReturn("Portugal");

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
        when(factoryDouble.create("Portugal")).thenReturn(countryDouble1);
        when(countryDouble1.getCountryName()).thenReturn("Portugal");

        repository.createAndAddNewCountry("Portugal");

        //Act
        List<Country> countryList = repository.getAllCountries();

        //Assert
        assertThrows(UnsupportedOperationException.class,
                () -> countryList.add(countryDouble2));
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

        when(factoryDouble.create("Portugal")).thenReturn(countryDouble1);
        when(countryDouble1.getCountryName()).thenReturn("Portugal");
        when(countryDouble1.getCountryID()).thenReturn(id);

        repository.createAndAddNewCountry("Portugal");

        //Act
        Optional<Country> result = repository.findByID(id);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(countryDouble1, result.get());
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

        when(factoryDouble.create("Portugal")).thenReturn(countryDouble1);
        when(factoryDouble.create("France")).thenReturn(countryDouble2);
        when(countryDouble1.getCountryName()).thenReturn("Portugal");
        when(countryDouble2.getCountryName()).thenReturn("France");
        when(countryDouble1.getCountryID()).thenReturn(idPortugal);
        when(countryDouble2.getCountryID()).thenReturn(idFrance);

        repository.createAndAddNewCountry("Portugal");
        repository.createAndAddNewCountry("France");

        //Act
        Optional<Country> result = repository.findByID(idFrance);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(countryDouble2, result.get());
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