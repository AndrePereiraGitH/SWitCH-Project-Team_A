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
 * This test class validates the behavior of the service layer responsible for
 * orchestrating country creation and persistence operations.
 * </p>
 *
 * <p>
 * The {@link CountryService} is instantiated using {@link org.mockito.InjectMocks},
 * allowing Mockito to automatically inject mocked dependencies into the System Under Test (SUT).
 * </p>
 *
 * <p>
 * The test suite isolates the service logic by using test doubles (mocks) for all collaborators:
 * <ul>
 *     <li>{@link CountryFactory} — responsible for creating {@link Country} domain objects</li>
 *     <li>{@link ICountryRepository} — responsible for persisting and retrieving countries</li>
 *     <li>{@link CountryId} — mocked to maintain consistency with the use of doubles across dependencies</li>
 * </ul>
 * </p>
 *
 * <p>
 * The tests focus on verifying:
 * <ul>
 *     <li>Correct delegation of country creation to the factory</li>
 *     <li>Proper persistence of created countries via the repository</li>
 *     <li>Correct return of the created and saved {@link Country} instance</li>
 * </ul>
 * </p>
 *
 * <p>
 * By using doubles for all dependencies, the tests ensure that only the behavior of
 * {@link CountryService} is evaluated, without involving external layers.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryFactory countryFactory;

    @Mock
    private ICountryRepository countryRepository;

    @Mock
    private CountryId countryId;

    @InjectMocks
    private CountryService countryService;

    private Country country;

    @BeforeEach
    void setup() {
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