package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToRegisterCountryController}.
 *
 * <p>
 * This test suite verifies that the CLI controller correctly delegates
 * country registration to the service layer and properly propagates
 * exceptions without altering business behavior.
 * </p>
 *
 * <p>
 * The tests follow the AAA (Arrange–Act–Assert) pattern:
 * <ul>
 *     <li><b>Arrange</b>: set up mocks and input data</li>
 *     <li><b>Act</b>: execute controller method</li>
 *     <li><b>Assert</b>: verify expected results or exceptions</li>
 * </ul>
 * </p>
 */
class ToRegisterCountryControllerTest {

    private CountryService countryService;
    private ToRegisterCountryController controller;
    private Country country;

    @Mock
    private UserId userId;

    @BeforeEach
    void setup() {
        countryService = mock(CountryService.class);
        controller = new ToRegisterCountryController(countryService);
        country = mock(Country.class);
        userId = mock(UserId.class);
    }

    @Test
    void register_validInput_delegatesToServiceAndReturnsResult()
            throws InstanceAlreadyExistsException, AccessDeniedException {

        // Arrange
        when(countryService.createAndSave("Portugal", userId)).thenReturn(country);

        // Act
        Country result = controller.register("Portugal", userId);

        // Assert
        assertEquals(country, result);
    }

    @Test
    void register_propagatesIllegalArgumentException()
            throws InstanceAlreadyExistsException, AccessDeniedException {

        // Arrange
        when(countryService.createAndSave("Mordor", userId))
                .thenThrow(new IllegalArgumentException("Country name is not valid"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.register("Mordor", userId));
    }

    @Test
    void register_propagatesInstanceAlreadyExistsException()
            throws InstanceAlreadyExistsException, AccessDeniedException {

        // Arrange
        when(countryService.createAndSave("Portugal", userId))
                .thenThrow(new InstanceAlreadyExistsException("Country is already registered"));

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> controller.register("Portugal", userId));
    }

    @Test
    void register_propagatesAccessDeniedException()
            throws InstanceAlreadyExistsException, AccessDeniedException {

        // Arrange
        when(countryService.createAndSave("Portugal", userId))
                .thenThrow(new AccessDeniedException("You do not have permission"));

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> controller.register("Portugal", userId));
    }

    @Test
    void constructor_nullService_throwsException() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterCountryController(null));
    }
}