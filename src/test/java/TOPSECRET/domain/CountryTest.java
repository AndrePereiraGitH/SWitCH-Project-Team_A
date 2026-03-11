package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*; //import para nos permitir fazer os assertEquals, assertThrows, etc

import org.junit.jupiter.api.Test;

class CountryTest { //Classe para testar Country

    //Empty constructor Test
    @Test
    void EmptyConstructorTest(){ //teste para criar o construtor vazio, só foi necessário no início do desenvolvimento
        new Country();
    }

    //Test when a country is "null"
    @Test
    void NullCountryTest(){

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new Country(null));

        assertEquals("Country name is null",exception.getMessage());
    }

    //Test when a country doesn't exist (in the ENUM)
    @Test
    void CountryDoesntExist(){ //vai testar o código de domínio em Country, linha 18 a 21
        //Assert and Act
        IllegalArgumentException exception=
                assertThrows(IllegalArgumentException.class,
                    () -> new Country("Mordor")); //Inserido um "Country" que não existe, neste caso, "Mordor"
        assertEquals("Country name is not valid", exception.getMessage()); //Vai comparar as mensagens, sendo que o país inserido não foi válido, devem ser iguais
    }

    //Test when a country does exist. It's the "happy path"
    @Test
    void CountryExists(){
        //Act
        Country country = new Country("Portugal");

        //Assert
        assertNotNull(country);
    }

    //Test when provided country is misspelled
    @Test
    void MisspelledCountry(){

        IllegalArgumentException exception=
                assertThrows(IllegalArgumentException.class,
                        () -> new Country("Portug"));

        assertEquals("Country name is not valid", exception.getMessage());
    }
    @Test
    void equalsSameObject(){

        CountryFactory factory = new CountryFactory();

        Country country = factory.create("Portugal");

        assertTrue(country.equals(country));
    }

    @Test
    void equalsNullObject(){

        CountryFactory factory = new CountryFactory();

        Country country = factory.create("Portugal");

        assertFalse(country.equals(null));
    }
    @Test

    void equalsDifferentClass(){

        CountryFactory factory = new CountryFactory();

        Country country = factory.create("Portugal");

        assertFalse(country.equals("Portugal"));
    }

    @Test
    void hashCodeSameCountryName(){

        CountryFactory factory = new CountryFactory();

        Country country1 = factory.create("Portugal");
        Country country2 = factory.create("Portugal");

        assertEquals(country1.hashCode(), country2.hashCode());
    }

    @Test
    void hashCodeDifferentCountryName(){

        CountryFactory factory = new CountryFactory();

        Country country1 = factory.create("Portugal");
        Country country2 = factory.create("France");

        assertNotEquals(country1.hashCode(), country2.hashCode());
    }
}
