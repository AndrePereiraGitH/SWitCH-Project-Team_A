package TOPSECRET.controller;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToRegisterCountryController}.
 * <p>
 * This test class verifies the behavior of the controller responsible for
 * registering new {@link Country} instances.
 * </p>
 * <p>
 * The controller is tested in isolation using a mocked {@link CountryService},
 * ensuring that:
 * <ul>
 *     <li>Valid inputs result in successful country registration</li>
 *     <li>Invalid inputs (null, blank, or invalid country names) throw appropriate exceptions</li>
 *     <li>Duplicate country scenarios are properly handled</li>
 * </ul>
 * </p>
 * <p>
 * Mockito is used to simulate the behavior of the service layer and to
 * verify interactions where applicable.
 * </p>
 */
class ToRegisterCountryControllerTest {

    private CountryService countryServiceDouble;
    private Country countryDouble;

    @BeforeEach
    void preparation() {
        countryServiceDouble = mock(CountryService.class);
        countryDouble = mock(Country.class);
    }

    @Test
    void registerNewCountry_validCountry() {
        // Arrange
        ToRegisterCountryController controller =
                new ToRegisterCountryController(countryServiceDouble);

        when(countryServiceDouble.createAndSave("Portugal"))
                .thenReturn(countryDouble);

        when(countryDouble.getCountryName()).thenReturn("Portugal");

        // Act
        Country result = controller.registerNewCountry("Portugal");

        // Assert
        assertNotNull(result);
        assertEquals("Portugal", result.getCountryName());

    }

    @Test
    void registerNewCountry_invalidCountry() {
        // Arrange
        ToRegisterCountryController controller =
                new ToRegisterCountryController(countryServiceDouble);

        when(countryServiceDouble.createAndSave("Mordor"))
                .thenThrow(new IllegalArgumentException("Country name is not valid"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerNewCountry("Mordor"));
    }

    @Test
    void registerNewCountry_nullCountry() {
        // Arrange
        ToRegisterCountryController controller =
                new ToRegisterCountryController(countryServiceDouble);

        // Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> controller.registerNewCountry(null));

        assertEquals("Country name cannot be null or blank", exception.getMessage());

    }

    @Test
    void registerNewCountry_blankCountry() {
        // Arrange
        ToRegisterCountryController controller =
                new ToRegisterCountryController(countryServiceDouble);

        // Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> controller.registerNewCountry("   "));

        assertEquals("Country name cannot be null or blank", exception.getMessage());

    }

    @Test
    void registerNewCountry_duplicateCountry() {
        // Arrange
        ToRegisterCountryController controller =
                new ToRegisterCountryController(countryServiceDouble);

        when(countryServiceDouble.createAndSave("Portugal"))
                .thenThrow(new IllegalArgumentException("Country is already registered"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerNewCountry("Portugal"));
    }
}