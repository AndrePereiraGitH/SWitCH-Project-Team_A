package TOPSECRET.domain;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    private final CountryFactory countryFactory = new CountryFactory();
    private final Country anyCountry = countryFactory.create("Portugal");

    private final String anyCityName = "Braga";

    @Test
    void shouldCreateValidCity() {

        //Arrange
        City city = anyFactory.create(anyCountry, anyCityName);

        //Assert
        assertNotNull(city);
        assertEquals(anyCityName, city.getCityName());
        assertEquals(anyCountry, city.getCityCountry());
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
                () -> anyFactory.create(anyCountry, null));
    }

    @Test
    void shouldThrowWhenCityNameIsBlank() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> anyFactory.create(anyCountry, ""));
    }
}