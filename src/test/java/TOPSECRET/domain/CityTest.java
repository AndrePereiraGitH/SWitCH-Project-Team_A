package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

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

    //Arrange
    Country anyCountry = new Country("Finland");
    String anyCityName = "Helsinki";
    City anyCity = new City(anyCountry, anyCityName);


    @Test
    void shouldCreateCityWithValidData() {
        //Arrange
        Country country = new Country("Portugal");
        String cityName = "Porto";

        //Act
        City city = new City(country, cityName);

        //Assert
        assertNotNull(city);
    }

    @Test
    void shouldThrowExceptionWhenCountryIsNull() {
        //Arrange
        Country country2 = null;
        String cityName2 = "Lisbon";

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new City(country2, cityName2));
    }

    @Test
    void shouldThrowExceptionWhenCityNameIsNull() {
        //Arrange
        Country country3 = new Country("Spain");
        String cityName3 = null;

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new City(country3, cityName3));
    }

    @Test
    void shouldThrowExceptionWhenCityNameIsEmpty() {

        //Arrange
        Country country3 = new Country("Australia");
        String cityName3 = "";

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new City(country3, cityName3));
    }

    @Test
    void shouldReturnCityName() {

        //Arrange
        Country country4 = new Country("Italy");
        String cityName4 = "Florence";

        City city4 = new City(country4, cityName4);

        //Act + assert
        String wantCityName = "Florence";

        assertEquals(wantCityName, city4.getCityName());
    }

    @Test
    void shouldReturnCityMultipleWords() {

        //Arrange
        Country country5 = new Country("Australia");
        String cityName5 = "Gold Coast";

        City city5 = new City(country5, cityName5);

        //Act + assert
        String wantCityMultipleWords = "Gold Coast";

        assertEquals(wantCityMultipleWords, city5.getCityName());
    }

    @Test
    void shouldReturnCityCountry() {
        //Arrange
        Country country6 = new Country("Norway");
        String cityName6 = "Oslo";

        City city6 = new City(country6, cityName6);

        //Act + assert
        Country wantCityCountry = new Country("Norway");

        assertEquals(wantCityCountry, city6.getCityCountry());
    }

    @Test
    void shouldReturnCountryMultipleWords() {

        //Arrange
        Country country7 = new Country("South_Korea"); //did not exist in enum?
        String cityName7 = "Seoul";

        City city7 = new City(country7, cityName7);

        //Act + assert
        Country wantCountryMultipleWords = new Country("South_Korea"); // do not forget " _ " in countries

        assertEquals(wantCountryMultipleWords, city7.getCityCountry());

    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        assertEquals(anyCity, anyCity);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedToNull() {

        //Arrange
        City nullCity = null;

        //Assert
        assertNotEquals(anyCity, nullCity);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedToNotCities() {

        //Arrange
        Country notACity = new Country("Czech_Republic");

        //Assert
        assertNotEquals(anyCity, notACity);
    }

    @Test
    void equals_shouldReturnTrueForEqualCities() {

        //Arrange
        City cityA = anyCity;
        City cityB = anyCity;

        //Assert
        assertEquals(cityA, cityB);

    }

    @Test
    void equals_shouldReturnFalseWhenSameCityDifferentCountry() {

        //Arrange
        Country countryA = new Country("Canada");
        String london = "London";

        Country countryB = new Country("United_Kingdom");

        City cityC = new City(countryA, london);
        City cityD = new City(countryB, london);

        //Assert
        assertNotEquals(cityC, cityD);

    }

    @Test
    void hashCode_SameIfEqualObjects() {

        City hashCity = anyCity;
        assertEquals(anyCity.hashCode(), anyCity.hashCode());
        assertEquals(hashCity.hashCode(), anyCity.hashCode());
    }

    @Test
    void hashCode_NotEqualIfDifferentAttributes() {

        //differing fields
        Country countryC = new Country("China");
        Country countryD = new Country("Djibouti");

        String beijing = "Beijing";

        City hashCity = new City(countryC, beijing);
        City hashCity2 = new City(countryD, beijing);

        //Assert
        assertNotEquals(hashCity.hashCode(), hashCity2.hashCode());

    }

}