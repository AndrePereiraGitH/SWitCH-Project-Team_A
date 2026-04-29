package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
import TOPSECRET.services.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToCreateAndPersistANewCityController}.
 *
 * <p>This test suite verifies the behavior of the controller responsible for
 * coordinating the creation and persistence of a new {@link City}.
 * The controller depends on {@link CityService}, which is mocked to isolate
 * the controller logic.</p>
 */
class ToCreateAndPersistANewCityControllerTest {

    private CityService doubleCityService;
    private ToCreateAndPersistANewCityController controller;

    @BeforeEach
    void setUp() {
        doubleCityService = mock(CityService.class);
    }

    @Test
    void shouldCreateAndPersistCity() {

        // Arrange
        ToCreateAndPersistANewCityController controller = new ToCreateAndPersistANewCityController(doubleCityService);
        City doubleCity = mock(City.class);
        when(doubleCityService.createAndSave(eq("PORTUGAL_ID"), eq("Porto")))
                .thenReturn(doubleCity);

        // Act
        City result = controller.createAndPersistCity("PORTUGAL_ID", "Porto");

        // Assert
        assertEquals(doubleCity, result);
        verify(doubleCityService).createAndSave(eq("PORTUGAL_ID"), eq("Porto"));
    }

    @Test
    void shouldPropagateExceptionWhenServiceThrows() {
        // Arrange
        ToCreateAndPersistANewCityController controller = new ToCreateAndPersistANewCityController(doubleCityService);
        when(doubleCityService.createAndSave(eq("PORTUGAL_ID"), eq("Porto")))
                .thenThrow(new IllegalArgumentException("City already exists"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> controller.createAndPersistCity("PORTUGAL_ID", "Porto"));
        assertEquals("City already exists", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCountryIDIsBlank() {
        // Arrange
        ToCreateAndPersistANewCityController controller = new ToCreateAndPersistANewCityController(doubleCityService);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createAndPersistCity("", "Porto"));
    }

    @Test
    void shouldThrowWhenCountryIDIsNull() {
        // Arrange
        ToCreateAndPersistANewCityController controller = new ToCreateAndPersistANewCityController(doubleCityService);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createAndPersistCity(null, "Porto"));
    }
}