package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    //Arrange
    Country anyCountry = new Country("Finland");
    String anyCity = "Helsinki";

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


    //TODO test override methods
    @Test
    void should() {


    }

}