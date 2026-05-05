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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CountryService}.
 *
 * <p>
 * This test class verifies the behavior of the service layer responsible for
 * orchestrating country creation, persistence, and retrieval operations.
 * The {@link CountryService} is instantiated using {@link org.mockito.InjectMocks},
 * with its dependencies provided as Mockito mocks.
 * </p>
 *
 * <p>
 * The test suite isolates the service logic by using doubles of its collaborators:
 * <ul>
 *     <li>{@link CountryFactory} for domain object creation</li>
 *     <li>{@link TOPSECRET.domain.Country.ICountryRepository} for persistence operations</li>
 * </ul>
 * </p>
 *
 * <p>
 * The tests cover:
 * <ul>
 *     <li>Creation and saving of countries</li>
 * </ul>
 * </p>
 */
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
    void createAndSaveCountry_valid() {

        // Arrange
        when(countryFactory.create("Portugal")).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(country);

        // Act
        Country result = countryService.createAndSave("Portugal");

        // Assert
        assertNotNull(result);
        assertEquals(country, result);
    }
}