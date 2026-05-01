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

/**
 * Unit tests for {@link CountryService}.
 *
 * <p>
 * This test class verifies the behavior of the service layer responsible for
 * orchestrating country creation, persistence, and retrieval operations.
 * It uses Mockito to double dependencies such as {@link CountryFactory}
 * and {@link TOPSECRET.domain.Country.ICountryRepository}.
 * </p>
 *
 * <p>
 * The tests cover:
 * <ul>
 *     <li>Creation and saving of countries</li>
 *     <li>Verification that factory and repository interactions occur</li>
 *     <li>Retrieval of countries by ID</li>
 *     <li>Retrieval of all countries</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryFactory countryFactory;

    @Mock
    private ICountryRepository countryRepository;

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
    void createAndSaveCountry_valid() {

        // Arrange
        CountryService countryService =
                new CountryService(countryFactory, countryRepository);

        when(countryFactory.create("Portugal")).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(country);

        // Act
        Country result = countryService.createAndSave("Portugal");

        // Assert
        assertNotNull(result);
        assertEquals(country, result);
    }

    // -------------------------------------------------------------------------
    // getCountryById
    // -------------------------------------------------------------------------

    @Test
    void getCountryById_found() {

        // Arrange
        CountryService countryService =
                new CountryService(countryFactory, countryRepository);

        when(countryRepository.findByID(countryId))
                .thenReturn(Optional.of(country));

        // Act
        Optional<Country> result = countryService.getCountryById(countryId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(country, result.get());
    }

    @Test
    void getCountryById_notFound() {

        // Arrange
        CountryService countryService =
                new CountryService(countryFactory, countryRepository);

        when(countryRepository.findByID(countryId))
                .thenReturn(Optional.empty());

        // Act
        Optional<Country> result = countryService.getCountryById(countryId);

        // Assert
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // getAll
    // -------------------------------------------------------------------------

    @Test
    void getAll_returnsList() {

        // Arrange
        CountryService countryService =
                new CountryService(countryFactory, countryRepository);

        List<Country> countries = List.of(country);

        when(countryRepository.getAllCountries())
                .thenReturn(countries);

        // Act
        List<Country> result = countryService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(country, result.get(0));
    }

    @Test
    void getAll_emptyList() {

        // Arrange
        CountryService countryService =
                new CountryService(countryFactory, countryRepository);

        when(countryRepository.getAllCountries())
                .thenReturn(List.of());

        // Act
        List<Country> result = countryService.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}