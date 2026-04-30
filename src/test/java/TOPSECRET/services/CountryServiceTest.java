package TOPSECRET.services;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryFactory countryFactory;

    @Mock
    private ICountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private Country country;
    private CountryId countryId;

    @BeforeEach
    void setup() {
        countryId = new CountryId("PORTUGAL");
        country = mock(Country.class);
    }

    // -------------------------------------------------------------------------
    // createAndSaveCountry
    // -------------------------------------------------------------------------

    @Test
    void createAndSaveCountry_validCountry() {

        // Arrange
        when(countryFactory.create("Portugal")).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(country);

        // Act
        Country result = countryService.createAndSaveCountry("Portugal");

        // Assert
        assertNotNull(result);
        assertEquals(country, result);

    }

    @Test
    void createAndSaveCountry_factoryIsCalled() {

        // Arrange
        when(countryFactory.create("Portugal")).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(country);

        // Act
        countryService.createAndSaveCountry("Portugal");

        // Assert
        verify(countryFactory, times(1)).create("Portugal");
    }

    @Test
    void createAndSaveCountry_repositoryIsCalled() {

        // Arrange
        when(countryFactory.create("Portugal")).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(country);

        // Act
        Country result = countryService.createAndSaveCountry("Portugal");

        // Assert
        assertNotNull(result);
        assertEquals(country, result);
    }

    // -------------------------------------------------------------------------
    // getCountryById
    // -------------------------------------------------------------------------

    @Test
    void getCountryById_found() {

        //Arrange + Act
        when(countryRepository.findByID(countryId))
                .thenReturn(Optional.of(country));

        Optional<Country> result = countryService.getCountryById(countryId);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(country, result.get());
    }

    @Test
    void getCountryById_notFound() {

        //Arrange + Act
        when(countryRepository.findByID(countryId))
                .thenReturn(Optional.empty());

        Optional<Country> result = countryService.getCountryById(countryId);

        //Assert
        assertTrue(result.isEmpty());

    }

    // -------------------------------------------------------------------------
    // getAll
    // -------------------------------------------------------------------------

    @Test
    void getAll_returnsList() {

        //Arrange +Act
        List<Country> countries = List.of(country);

        when(countryRepository.getAllCountries())
                .thenReturn(countries);

        List<Country> result = countryService.getAll();

        //Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(country, result.get(0));

    }

    @Test
    void getAll_emptyList() {

        //Arrange + Act
        when(countryRepository.getAllCountries())
                .thenReturn(List.of());

        List<Country> result = countryService.getAll();

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}