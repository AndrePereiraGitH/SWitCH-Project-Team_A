package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToRegisterNewCityController}.
 *
 * <p>This test suite verifies the behavior of the controller responsible for
 * coordinating the creation and persistence of a new {@link City}.
 * The controller depends on {@link CityService}, which is doubled to isolate
 * the controller logic.</p>
 */
class ToRegisterNewCityControllerTest {

    private CityService doubleCityService;

    @BeforeEach
    void setUp() {
        doubleCityService = mock(CityService.class);
    }

    @Test
    void shouldReturnTrueWhenCityIsRegistered() throws Exception {
        // Arrange
        ToRegisterNewCityController sut = new ToRegisterNewCityController(doubleCityService);
        UserId doubleUserId = mock(UserId.class);
        City doubleCity = mock(City.class);
        when(doubleCityService.createAndSave(eq("Portugal"), eq("Porto"), eq(doubleUserId)))
                .thenReturn(doubleCity);

        // Act
        boolean result = sut.registerCity("Portugal", "Porto", doubleUserId);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldPropagateExceptionWhenServiceThrows() throws Exception {
        // Arrange
        ToRegisterNewCityController sut = new ToRegisterNewCityController(doubleCityService);
        UserId doubleUserId = mock(UserId.class);
        when(doubleCityService.createAndSave(eq("Portugal"), eq("Porto"), eq(doubleUserId)))
                .thenThrow(new IllegalArgumentException("City already exists"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sut.registerCity("Portugal", "Porto", doubleUserId));
        assertEquals("City already exists", ex.getMessage());
    }

    @Test
    void shouldThrowWhenServiceIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterNewCityController(null));
    }
}