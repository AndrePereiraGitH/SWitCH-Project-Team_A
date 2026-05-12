package TOPSECRET.controller;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToRegisterCountryController}.
 *
 * <p>
 * This test suite validates the behavior of the controller as a thin layer
 * responsible for delegating requests to the {@link CountryService}.
 * </p>
 *
 * <p>
 * The controller does not implement business logic or validation.
 * Therefore, tests focus on:
 * <ul>
 *     <li>Correct delegation of requests to the service layer</li>
 *     <li>Proper propagation of results returned by the service</li>
 *     <li>Proper propagation of exceptions thrown by the service</li>
 * </ul>
 * </p>
 *
 * <p>
 * Mockito is used to simulate the behavior of the service layer.
 * </p>
 */
class ToRegisterCountryControllerTest {

    private CountryService countryService;
    private ToRegisterCountryController controller;
    private Country country;

    @BeforeEach
    void setup() {
        countryService = mock(CountryService.class);
        controller = new ToRegisterCountryController(countryService);
        country = mock(Country.class);
    }

    // -------------------------------------------------------------------------
    // registerNewCountry()
    // -------------------------------------------------------------------------

    @Test
    void register_validInput_delegatesToServiceAndReturnsResult() {

        // Arrange
        when(countryService.createAndSave("Portugal")).thenReturn(country);

        // Act
        Country result = controller.register("Portugal");

        // Assert
        assertNotNull(result);
        assertEquals(country, result);
    }

    @Test
    void registerNewCountry_propagatesException() {

        // Arrange
        when(countryService.createAndSave("Mordor"))
                .thenThrow(new IllegalArgumentException("Country name is not valid"));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.register("Mordor")
        );

        assertEquals("Country name is not valid", exception.getMessage());
    }

    @Test
    void register_propagatesException() {

        // Arrange
        when(countryService.createAndSave("Portugal"))
                .thenThrow(new IllegalArgumentException("Country is already registered"));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.register("Portugal")
        );

        assertEquals("Country is already registered", exception.getMessage());
    }

    @Test
    void register_nullInput_delegatesToService() {

        // Arrange
        when(countryService.createAndSave(null))
                .thenThrow(new IllegalArgumentException("Country name cannot be null or blank"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.register(null));
    }

    @Test
    void register_blankInput_delegatesToService() {

        // Arrange
        String input = "   ";

        when(countryService.createAndSave(input))
                .thenThrow(new IllegalArgumentException("Country name cannot be null or blank"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.register(input));
    }
}