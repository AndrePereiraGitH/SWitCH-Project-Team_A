package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.ValueObjects.CountryID;
import TOPSECRET.services.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToRegisterACityController}.
 *
 * <p>This test suite verifies the behavior of the controller responsible for
 * registering new cities in the system. The controller depends on
 * {@link CityService}, which is mocked in order to isolate the controller logic.</p>
 *
 * <p>The {@link City} domain object is mocked because its constructor is protected
 * and the controller only needs to forward the object returned by the service.</p>
 */
class ToRegisterACityControllerTest {

    private CityService doubleCityService;
    private ToRegisterACityController controller;

    @BeforeEach
    void setUp() {
        doubleCityService = mock(CityService.class);
        controller = new ToRegisterACityController(doubleCityService);
    }

    @Test
    void throwsConstructorWhenNullCityService() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ToRegisterACityController(null)
        );
        assertEquals("CityService must not be left null", ex.getMessage());
    }

    @Test
    void registerNewCitySuccess() {
        // Arrange
        CountryID countryID = new CountryID("PORTUGAL_ID");
        City doubleCity = mock(City.class);
        when(doubleCityService.createAndSave(countryID, "Lisbon")).thenReturn(doubleCity);

        // Act
        City result = controller.registerNewCity(countryID, "Lisbon");

        // Assert
        assertEquals(doubleCity, result);
        verify(doubleCityService).createAndSave(countryID, "Lisbon");
    }

    @Test
    void registerNewCitySuccess2() {
        // Arrange
        CountryID countryID = new CountryID("CZECH_REPUBLIC_ID");
        City doubleCity = mock(City.class);
        when(doubleCityService.createAndSave(countryID, "Prague")).thenReturn(doubleCity);

        // Act
        City result = controller.registerNewCity(countryID, "Prague");

        // Assert
        assertEquals(doubleCity, result);
        verify(doubleCityService).createAndSave(countryID, "Prague");
    }

    @Test
    void registerNewCityWhenServiceThrows() {
        // Arrange
        CountryID countryID = new CountryID("INVALID_ID");
        when(doubleCityService.createAndSave(countryID, "anyCity"))
                .thenThrow(new IllegalArgumentException("Country not found"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                controller.registerNewCity(countryID, "anyCity")
        );
        assertEquals("Country not found", ex.getMessage());
    }
}