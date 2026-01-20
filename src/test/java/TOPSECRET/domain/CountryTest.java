package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;
import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

class CountryTest {

    //Empty constructor Test
    @Test
    void EmptyConstructorTest(){
        new Country();
    }

    //Test when a country is "null"
    @Test
    void NullCountryTest(){

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new Country(null)); // esta linha é usada para dar "trigger" ao "if (_countryName == null)"

        assertEquals("Country name is null",exception.getMessage()); //chama o "exception" e compara as mensagens
    }

    @Test
    void CountryDoesntExist(){

        //Assert and Act
        IllegalArgumentException exception=
                assertThrows(IllegalArgumentException.class,
                    () -> new Country("Mordor")); //Inserido um "Country" que não existe
        assertEquals("Country name is not valid", exception.getMessage());
    }

    @Test
    void CountryExists(){

        //Act
        Country country = new Country("Portugal");

        //Assert
        assertNotNull(country); //checks the line "Countries.valuesOf(_countryName.toUpperCase());" and runs the constructor

    }

    @Test
    void MisspelledCountry(){

        IllegalArgumentException exception=
                assertThrows(IllegalArgumentException.class,
                        () -> new Country("Portug"));

        assertEquals("Country name is not valid", exception.getMessage());
    }
}
