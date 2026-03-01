package TOPSECRET.controller;

import TOPSECRET.domain.City;
import TOPSECRET.domain.CityFactory;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.CityRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class US008ToRegisterACityCtrlTest {

    //TODO Reminder to learn how to validate that an ADMIN is adding the city and not just any regular User

    @Test
    void shouldAddACity() {

        CityFactory cityFactory1 = new CityFactory();
        CityRepository cityRepository1 = new CityRepository(cityFactory1);

        US008ToRegisterACityCtrl cityCtrl1 = new US008ToRegisterACityCtrl(cityRepository1);

        Country country1 = new Country("United_States"); //When countries have multiple words, write like so
        String cityName1 = "Jacksonville";
        City newCity = new City(country1, cityName1);

        //Act
        City addedCity = cityCtrl1.registerNewCity(country1, cityName1);

        //Assert
        assertEquals(addedCity, newCity);

    }

}