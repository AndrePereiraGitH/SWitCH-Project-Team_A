package TOPSECRET.domain;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CityID;
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
        CityID cityID = mock(CityID.class);

        // Act
        City city = new City(country, cityName, cityID);

        // Assert
        assertNotNull(city);
    }

    @Test
    void shouldThrowExceptionWhenCountryIsNull() {

        // Arrange
        String cityName = "Lisbon";
        CityID cityID = mock(CityID.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(null, cityName, cityID));
    }

    @Test
    void shouldThrowExceptionWhenCityNameIsNull() {

        // Arrange
        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(country, null, cityID));
    }

    @Test
    void shouldThrowExceptionWhenCityNameIsEmpty() {

        // Arrange
        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(country, "", cityID));
    }

    @Test
    void shouldReturnCityName() {

        // Arrange
        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);
        City city = new City(country, "Florence", cityID);

        // Act
        String result = city.getCityName();

        // Assert
        assertEquals("Florence", result);
    }

    @Test
    void shouldReturnCityCountry() {

        // Arrange
        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);
        City city = new City(country, "Oslo", cityID);

        // Act
        Country result = city.getCityCountry();

        // Assert
        assertEquals(country, result);
    }

    @Test
    void equals_shouldReturnTrueForEqualCities() {

        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);

        City cityA = new City(country, "Helsinki", cityID);
        City cityB = new City(country, "Helsinki", cityID);

        assertEquals(cityA, cityB);
    }

    @Test
    void equals_shouldReturnFalseWhenCityNamesDiffer() {

        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);

        City cityA = new City(country, "Lisbon", cityID);
        City cityB = new City(country, "Porto", cityID);

        assertNotEquals(cityA, cityB);
    }

    @Test
    void hashCode_shouldBeEqualForEqualCities() {

        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);

        City cityA = new City(country, "Portland", cityID);
        City cityB = new City(country, "Portland", cityID);

        assertEquals(cityA.hashCode(), cityB.hashCode());
    }

    @Test
    void differentCitiesShouldHaveDifferentHashCodes() {
        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);

        City city1 = new City(country, "Lisbon", cityID);
        City city2 = new City(country, "Porto",  cityID);

        assertNotEquals(city1.hashCode(), city2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithNull() {

        //Arrange
        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);

        City city = new City(country, "Lisbon", cityID);

        //Assert
        assertFalse(city.equals(null));
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithDifferentType() {

        //Arrange
        Country country = mock(Country.class);
        CityID cityID = mock(CityID.class);

        City city = new City(country, "Lisbon", cityID);

        //Assert
        assertFalse(city.equals("NotACity"));
    }

}
