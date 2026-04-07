package TOPSECRET.domain.City;

import TOPSECRET.domain.ValueObjects.CountryID;
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
 * {@link CountryID} and a city name.
 */

class CityTest {

    @Test
    void shouldCreateCityWithValidData() {

        // Arrange
        CountryID countryID = mock(CountryID.class);
        String cityName = "Porto";

        // Act
        City city = new City(countryID, cityName);

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
        CountryID countryID = mock(CountryID.class);


        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(countryID, null));
    }

    @Test
    void shouldThrowExceptionWhenCityNameIsEmpty() {

        // Arrange
        CountryID countryID = mock(CountryID.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(countryID, ""));
    }

    @Test
    void shouldReturnCityName() {

        // Arrange
        CountryID countryID = mock(CountryID.class);
        City city = new City(countryID, "Florence");

        // Act
        String result = city.getCityName();

        // Assert
        assertEquals("Florence", result);
    }

    @Test
    void shouldReturnCityCountry() {

        // Arrange
        CountryID countryID = mock(CountryID.class);
        City city = new City(countryID, "Oslo");

        // Act
        CountryID result = city.getCityCountryID();

        // Assert
        assertEquals(countryID, result);
    }

    @Test
    void equals_shouldReturnTrueForEqualCities() {

        CountryID countryID = mock(CountryID.class);
        City cityA = new City(countryID, "Helsinki");
        City cityB = cityA;

        assertEquals(cityA, cityB);
    }

    @Test
    void equals_shouldReturnFalseWhenCityNamesDiffer() {

        CountryID countryID = mock(CountryID.class);
        City cityA = new City(countryID, "Lisbon");
        City cityB = new City(countryID, "Porto");

        assertNotEquals(cityA, cityB);
    }

    @Test
    void hashCode_shouldBeEqualForSameCity() {
        CountryID countryID = mock(CountryID.class);
        City city = new City(countryID, "Portland");

        assertEquals(city.hashCode(), city.hashCode());
    }

    @Test
    void differentCitiesShouldHaveDifferentHashCodes() {
        CountryID countryID = mock(CountryID.class);
        City city1 = new City(countryID, "Lisbon");
        City city2 = new City(countryID, "Porto");

        assertNotEquals(city1.hashCode(), city2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithNull() {

        //Arrange
        CountryID countryID = mock(CountryID.class);
        City city = new City(countryID, "Lisbon");

        //Assert
        assertFalse(city.equals(null));
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithDifferentType() {

        //Arrange
        CountryID countryID = mock(CountryID.class);
        City city = new City(countryID, "Lisbon");

        //Assert
        assertFalse(city.equals("NotACity"));
    }
}