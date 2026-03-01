package TOPSECRET.repository;

import TOPSECRET.domain.City;
import TOPSECRET.domain.CityFactory;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CityRepositoryTest {

    //Reusing Repo and Factory here
    CityFactory cityFactory = new CityFactory();
    CityRepository cityRepository1 = new CityRepository(cityFactory);

    @Test
    void shouldCreateNewCity() {
        //Arrange
        Country country1 = new Country("Poland");
        String cityName1 = "Warsaw";
        City expectedCity1 = new City(country1, cityName1);

        User _user = new User();

        //Act
        City result1 = cityRepository1.createAndAdd(country1, cityName1);

        //Assert
        assertEquals(expectedCity1, result1); //in City class the @Override methods were created to address the issue here
    }

    @Test
    void shouldThrowIfCityAlreadyExists() {

        Country country2 = new Country("Belgium");
        String cityName2 = "Brussels";

        cityRepository1.createAndAdd(country2, cityName2);

        assertThrows(IllegalArgumentException.class, () -> {
            cityRepository1.createAndAdd(country2, cityName2);
        });
    }


}