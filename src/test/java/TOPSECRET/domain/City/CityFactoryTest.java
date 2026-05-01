package TOPSECRET.domain.City;

import TOPSECRET.domain.ValueObjects.CityID;
import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link CityFactory}.
 * <p>
 * These tests verify that:
 * <ul>
 *     <li>A valid {@link City} is created when correct parameters are provided</li>
 *     <li>{@link IllegalArgumentException} is propagated when domain constraints are violated</li>
 * </ul>
 * <p>
 * Since {@code CityFactory} is stateless, a single shared instance is reused
 * across all test methods.
 */
class CityFactoryTest {


    private final CityFactory anyFactory = new CityFactory();
    private final CountryID anyCountryID = mock(CountryID.class);
    private final String anyCityName = "Braga";

    @Test
    void shouldCreateValidCity() {

        //Arrange
        City city = anyFactory.create(anyCountryID, anyCityName);

        //Assert
        assertNotNull(city);
        assertEquals(anyCityName, city.getCityName());
        assertEquals(anyCountryID, city.getCityCountryID());
    }

    @Test
    void shouldThrowWhenCountryIsNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> anyFactory.create(null, anyCityName));
    }

    @Test
    void shouldThrowWhenCityNameIsNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> anyFactory.create(anyCountryID, null));
    }

    @Test
    void shouldThrowWhenCityNameIsBlank() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> anyFactory.create(anyCountryID, ""));
    }

    @Test
    void shouldCreateValidCityWithExistingID() {

        // Arrange
        CityID cityID = new CityID("existing-uuid");

        // Act
        City city = anyFactory.create(cityID, anyCountryID, anyCityName);

        // Assert
        assertNotNull(city);
        assertEquals(anyCityName, city.getCityName());
        assertEquals(anyCountryID, city.getCityCountryID());
        assertEquals(cityID, city.id());
    }

    @Test
    void shouldThrowWhenCityIDIsNull() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> anyFactory.create(null, anyCountryID, anyCityName));
    }
}