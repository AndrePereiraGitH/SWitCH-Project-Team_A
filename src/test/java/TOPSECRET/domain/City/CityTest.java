package TOPSECRET.domain.City;

import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
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
 * {@link CountryId} and a city name.
 */
class CityTest {

    @Test
    void shouldCreateCityWithValidData() {

        // Arrange
        CountryId countryID = mock(CountryId.class);
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
        CountryId countryID = mock(CountryId.class);


        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(countryID, null));
    }

    @Test
    void shouldThrowExceptionWhenCityNameIsEmpty() {

        // Arrange
        CountryId countryID = mock(CountryId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(countryID, ""));
    }

    @Test
    void shouldReturnCityName() {

        // Arrange
        CountryId countryID = mock(CountryId.class);
        City city = new City(countryID, "Florence");

        // Act
        String result = city.getCityName();

        // Assert
        assertEquals("Florence", result);
    }

    @Test
    void shouldReturnCityCountry() {

        // Arrange
        CountryId countryID = mock(CountryId.class);
        City city = new City(countryID, "Oslo");

        // Act
        CountryId result = city.getCityCountryID();

        // Assert
        assertEquals(countryID, result);
    }

    @Test
    void equals_shouldReturnTrueForEqualCities() {

        CountryId countryID = mock(CountryId.class);
        City cityA = new City(countryID, "Helsinki");
        City cityB = cityA;

        assertEquals(cityA, cityB);
    }

    @Test
    void equals_shouldReturnFalseWhenCityNamesDiffer() {

        CountryId countryID = mock(CountryId.class);
        City cityA = new City(countryID, "Lisbon");
        City cityB = new City(countryID, "Porto");

        assertNotEquals(cityA, cityB);
    }

    @Test
    void hashCode_shouldBeEqualForSameCity() {
        CountryId countryID = mock(CountryId.class);
        City city = new City(countryID, "Portland");

        assertEquals(city.hashCode(), city.hashCode());
    }

    @Test
    void differentCitiesShouldHaveDifferentHashCodes() {
        CountryId countryID = mock(CountryId.class);
        City city1 = new City(countryID, "Lisbon");
        City city2 = new City(countryID, "Porto");

        assertNotEquals(city1.hashCode(), city2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithNull() {

        //Arrange
        CountryId countryID = mock(CountryId.class);
        City city = new City(countryID, "Lisbon");

        //Assert
        assertFalse(city.equals(null));
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithDifferentType() {
        //Arrange
        CountryId countryID = mock(CountryId.class);
        City city = new City(countryID, "Lisbon");

        //Assert
        assertFalse(city.equals("NotACity"));
    }

    @Test
    void shouldReturnValidToString() {
        //Arrange
        CountryId countryID = mock(CountryId.class);
        City city = new City(countryID, "Porto");
        String result = city.toString();

        //Assert
        assertNotNull(result);
        assertTrue(result.contains("Porto"));
        assertTrue(result.contains("City"));
    }

    @Test
    void equals_shouldReturnTrueForDifferentInstancesWithSameID() {
        // Arrange
        CityId cityId = mock(CityId.class);
        CountryId countryId = mock(CountryId.class);


        City city1 = new City(cityId, countryId, "Lisboa");
        City city2 = new City(cityId, countryId, "Porto");

        // Assert
        assertEquals(city1, city2);
    }

    @Test
    void sameAs_shouldReturnTrueWhenBusinessDataIsSame() {
        // Arrange
        CountryId countryID = new CountryId("PT");
        City city1 = new City(countryID, "Porto");
        City city2 = new City(countryID, "Porto");

        // Assert
        assertTrue(city1.sameAs(city2));
    }

    @Test
    void sameAs_shouldReturnFalseWhenBusinessDataDiffers() {
        // Arrange
        CountryId countryID = new CountryId("PT");
        City city1 = new City(countryID, "Porto");
        City city2 = new City(countryID, "Lisboa");

        // Assert
        assertFalse(city1.sameAs(city2));
    }

    @Test
    void sameAs_shouldReturnFalseForDifferentTypeOrNull() {
        // Arrange
        CountryId countryID = new CountryId("PT");
        City city = new City(countryID, "Porto");

        // Assert
        assertFalse(city.sameAs(null));
        assertFalse(city.sameAs("NotACity"));
    }

    @Test
    void shouldThrowExceptionWhenCityNameIsBlankInFullConstructor() {
        // Arrange
        CityId cityID = mock(CityId.class);
        CountryId countryID = mock(CountryId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(cityID, countryID, "  "));
    }

    @Test
    void shouldThrowExceptionWhenCountryIsNullInFullConstructor() {
        // Arrange
        CityId cityID = mock(CityId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(cityID, null, "Porto"));
    }

    @Test
    void shouldThrowExceptionWhenCityIDIsNull() {
        // Arrange
        CountryId countryID = mock(CountryId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(null, countryID, "Porto"));
    }
}