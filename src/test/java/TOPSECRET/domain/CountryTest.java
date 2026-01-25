package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*; //import para nos permitir fazer os assertEquals, assertThrows, etc
import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test; //import para indicar ao JUnit que é para testes

class CountryTest { //Classe para testar Country

    //Empty constructor Test
    @Test
    void EmptyConstructorTest(){ //teste para criar o construtor vazio, só foi necessário no início do desenvolvimento
        new Country();
    }

    //Test when a country is "null"
    @Test
    void NullCountryTest(){ //vai testar o código de domínio em Country, linha 13 e 14

        IllegalArgumentException exception = //Cria uma variável para guardar a exceção lançada
                assertThrows(IllegalArgumentException.class,
                        () -> new Country(null)); // esta linha é usada para dar "trigger" ao "if (_countryName == null)"

        assertEquals("Country name is null",exception.getMessage()); //chama o "exception" e compara as mensagens
    } //Este teste, para passar, tem de ser lançada exceção pelo código a ser testado em Country, linha 13 e 14

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
    void CountryExists(){ //Vai servir para testar o que acontece quando se introduz um país que está no ENUM. Ou seja, quando é um país válido

        //Act
        Country country = new Country("Portugal");

        //Assert
        assertNotNull(country); //checks the line "Countries.valuesOf(_countryName.toUpperCase());" and runs the constructor
    } //Teste para garantir que o objeto foi criado (com o assertNotNull)

    //Test when provided country is misspelled
    @Test
    void MisspelledCountry(){

        IllegalArgumentException exception=
                assertThrows(IllegalArgumentException.class,
                        () -> new Country("Portug"));

        assertEquals("Country name is not valid", exception.getMessage());
    } //Este teste corre o ENUM e como não vai existir um país mal-escrito, não será válido, é considerado um país não existente, tal como "Mordor"
}
