package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
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
 * The controller depends on {@link CityService}, which is mocked to isolate
 * the controller logic.</p>
 */
class ToRegisterNewCityControllerTest {

    private CityService doubleCityService;
    private ToRegisterNewCityController controller;

    @BeforeEach
    void setUp() {
        doubleCityService = mock(CityService.class);
    }

    @Test
    void shouldRegister() {

        // Arrange
        ToRegisterNewCityController controller = new ToRegisterNewCityController(doubleCityService);
        City doubleCity = mock(City.class);
        when(doubleCityService.createAndSave(eq("PORTUGAL_ID"), eq("Porto")))
                .thenReturn(doubleCity);

        // Act
        City result = controller.register("PORTUGAL_ID", "Porto");

        // Assert
        assertEquals(doubleCity, result);
    }

    @Test
    void shouldPropagateExceptionWhenServiceThrows() {
        // Arrange
        ToRegisterNewCityController controller = new ToRegisterNewCityController(doubleCityService);
        when(doubleCityService.createAndSave(eq("PORTUGAL_ID"), eq("Porto")))
                .thenThrow(new IllegalArgumentException("City already exists"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> controller.register("PORTUGAL_ID", "Porto"));
        assertEquals("City already exists", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCountryIDIsBlank() {
        // Arrange
        ToRegisterNewCityController controller = new ToRegisterNewCityController(doubleCityService);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.register("", "Porto"));
    }

    @Test
    void shouldThrowWhenCountryIDIsNull() {
        // Arrange
        ToRegisterNewCityController controller = new ToRegisterNewCityController(doubleCityService);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.register(null, "Porto"));
    }
}