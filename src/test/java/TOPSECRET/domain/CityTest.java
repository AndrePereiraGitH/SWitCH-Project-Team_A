package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link City} domain class.
 * <p>
 * These tests verify the following aspects of the {@code City} entity:
 * <ul>
 *     <li>Correct construction with valid parameters</li>
 *     <li>Enforcement of domain invariants (country and city name cannot be null or blank)</li>
 *     <li>Correct behavior of accessor methods</li>
 *     <li>Correct implementation of {@link City#equals(Object)} and {@link City#hashCode()}</li>
 * </ul>
 * <p>
 * The {@code City} class represents a value defined by a combination of a
 * {@link Country} and a city name.
 */

class CityTest {


    @Test
    void shouldCreateCityWithValidData() {

        // Arrange
        Country country = mock(Country.class);
        String cityName = "Porto";

        // Act
        City city = new City(country, cityName);

        // Assert
        assertNotNull(city);
    }

    @Test
    void shouldThrowExceptionWhenCountryIsNull() {

        // Arrange
        String cityName = "Lisbon";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(null, cityName));
    }

    @Test
    void shouldThrowExceptionWhenCityNameIsNull() {

        // Arrange
        Country country = mock(Country.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(country, null));
    }

    @Test
    void shouldThrowExceptionWhenCityNameIsEmpty() {

        // Arrange
        Country country = mock(Country.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(country, ""));
    }

    @Test
    void shouldReturnCityName() {

        // Arrange
        Country country = mock(Country.class);
        City city = new City(country, "Florence");

        // Act
        String result = city.getCityName();

        // Assert
        assertEquals("Florence", result);
    }

    @Test
    void shouldReturnCityCountry() {

        // Arrange
        Country country = mock(Country.class);
        City city = new City(country, "Oslo");

        // Act
        Country result = city.getCityCountry();

        // Assert
        assertEquals(country, result);
    }

    @Test
    void equals_shouldReturnTrueForEqualCities() {

        Country country = mock(Country.class);

        City cityA = new City(country, "Helsinki");
        City cityB = new City(country, "Helsinki");

        assertEquals(cityA, cityB);
    }

    @Test
    void equals_shouldReturnFalseWhenCityNamesDiffer() {

        Country country = mock(Country.class);

        City cityA = new City(country, "Lisbon");
        City cityB = new City(country, "Porto");

        assertNotEquals(cityA, cityB);
    }

    @Test
    void hashCode_shouldBeEqualForEqualCities() {

        Country country = mock(Country.class);

        City cityA = new City(country, "Portland");
        City cityB = new City(country, "Portland");

        assertEquals(cityA.hashCode(), cityB.hashCode());
    }
}
