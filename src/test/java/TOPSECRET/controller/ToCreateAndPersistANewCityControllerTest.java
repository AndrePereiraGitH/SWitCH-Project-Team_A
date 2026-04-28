package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.ValueObjects.CountryID;
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
        controller = new ToCreateAndPersistANewCityController(doubleCityService);
    }

    @Test
    void shouldCreateAndPersistCity() {

        // Arrange
        City doubleCity = mock(City.class);
        when(doubleCityService.createAndSave(any(CountryID.class), eq("Porto")))
                .thenReturn(doubleCity);

        // Act
        City result = controller.createAndPersistCity("PORTUGAL_ID", "Porto");

        // Assert
        assertEquals(doubleCity, result);
        verify(doubleCityService).createAndSave(any(CountryID.class), eq("Porto"));
    }

    @Test
    void shouldPropagateExceptionWhenServiceThrows() {

        // Arrange
        when(doubleCityService.createAndSave(any(CountryID.class), eq("Porto")))
                .thenThrow(new IllegalArgumentException("City already exists"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> controller.createAndPersistCity("PORTUGAL_ID", "Porto"));
        assertEquals("City already exists", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCountryIDIsBlank() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createAndPersistCity("", "Porto"));
    }

    @Test
    void shouldThrowWhenCountryIDIsNull() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createAndPersistCity(null, "Porto"));
    }
}