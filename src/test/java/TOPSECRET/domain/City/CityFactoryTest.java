package TOPSECRET.domain.City;

import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
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
    private final CountryId anyCountryId = mock(CountryId.class);
    private final String anyCityName = "Braga";

    @Test
    void shouldCreateValidCity() {

        //Arrange
        City city = anyFactory.create(anyCountryId, anyCityName);

        //Assert
        assertNotNull(city);
        assertEquals(anyCityName, city.getCityName());
        assertEquals(anyCountryId, city.getCityCountryID());
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
                () -> anyFactory.create(anyCountryId, null));
    }

    @Test
    void shouldThrowWhenCityNameIsBlank() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> anyFactory.create(anyCountryId, ""));
    }

    @Test
    void shouldCreateValidCityWithExistingID() {

        // Arrange
        CityId cityID = mock(CityId.class);

        // Act
        City city = anyFactory.create(cityID, anyCountryId, anyCityName);

        // Assert
        assertNotNull(city);
        assertEquals(anyCityName, city.getCityName());
        assertEquals(anyCountryId, city.getCityCountryID());
        assertEquals(cityID, city.id());
    }

    @Test
    void shouldThrowWhenCityIDIsNull() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> anyFactory.create(null, anyCountryId, anyCityName));
    }
}