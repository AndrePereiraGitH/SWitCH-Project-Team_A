package TOPSECRET.repository;

import TOPSECRET.domain.City;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CityRepositoryTest {

    @Test
    void shouldCreateNewCity() {
        //Arrange
        CityRepository cityRepository1 = new CityRepository();
        Country country1 = new Country("Poland");
        String cityName1 = "Warsaw";
        City expectedCity1 = new City(country1, cityName1);

        User _user = new User();

        //Act
        City result1 = cityRepository1.createAndAddNewCity(country1, cityName1);

        //Assert
        assertEquals(expectedCity1, result1); //in City class the @Override methods were created to address the issue here
    }

    @Test
    void shouldThrowIfCityAlreadyExists() {
        CityRepository cityRepository2 = new CityRepository();

        Country country2 = new Country("Belgium");
        String cityName2 = "Brussels";

        cityRepository2.createAndAddNewCity(country2, cityName2);

        assertThrows(IllegalArgumentException.class, () -> {
            cityRepository2.createAndAddNewCity(country2, cityName2);
        });
    }


}