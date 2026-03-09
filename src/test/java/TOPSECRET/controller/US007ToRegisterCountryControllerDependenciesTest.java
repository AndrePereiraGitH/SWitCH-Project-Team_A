package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*; //importante para os assertEquals, assertThrows, etc

import TOPSECRET.domain.Country;
import TOPSECRET.repository.CountryRepository;
import TOPSECRET.domain.CountryFactory;
import org.junit.jupiter.api.BeforeEach; //BeforeEach é um méto.do executado sempre antes de cada teste. Import para poder ser usado
import org.junit.jupiter.api.Test; //Para marcar métodos de teste

class US007ToRegisterCountryControllerDependenciesTest {

    private US007ToRegisterCountryController controller; //declaração de Objetos para ser usados no teste
    private CountryRepository repository; //declaração de Objetos para ser usados no teste
    private CountryFactory countryFactory;


    @BeforeEach //BeforeEach é um méto.do executado sempre antes de cada teste. Garante isolamento entre testes
    void preparation(){ //Méto.do para preparação
        countryFactory = new CountryFactory();
        repository = new CountryRepository(countryFactory); //Cria um novo repositório do tipo "CountryRepository" e injeta a factory
        controller = new US007ToRegisterCountryController(repository);//Cria um novo controller e insere-o no repositório ("repository") criado acima
    }

    //This Test is for testing when a valid Country is introduced
    @Test
    void registerNewCountry_ValidCountry(){
        Country country = controller.registerNewCountry("Portugal"); //Objeto do tipo Country = controller criado na linha 18, usa o méto.do "registerNewCountry", linha 16 do Controller

        assertNotNull(country); //Dar assert que não é null
        assertEquals(1, repository.getAllCountries().size()); //Dar assert que o repositório criado na linha 17 fica com 1 país
        assertEquals("Portugal", repository.getAllCountries().get(0).getCountryName()); //Dar assert que o país no repositório criado foi o mesmo
    }

    //This test checks the response in case of inputing a "wrong" country
    @Test
    void registerNewCountry_InvalidCountryThrowException(){
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerNewCountry("Mordor"));
        assertEquals(0, repository.getAllCountries().size()); //verifica que o repositório não mudou, não ficou com mais um país, ou seja, o país introduzido foi null e não adicionado
    }

    @Test //This test checks the response in case of inputing a "null" name
    void registerNewCountry_nullName(){

        assertThrows(IllegalArgumentException.class, () -> controller.registerNewCountry(null));
    }

    @Test //This test checks the response in case of inputing a "blank" name
    void registerNewCountry_blankName(){

        assertThrows(IllegalArgumentException.class, () -> controller.registerNewCountry(" "));
    }
}