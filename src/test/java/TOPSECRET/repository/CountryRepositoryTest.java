package TOPSECRET.repository;

import static org.junit.jupiter.api.Assertions.*;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class CountryRepositoryTest {
    private CountryRepository repository; //declara que este repositório vai ser testado ("repository" do tipo CountryRepository)

    @BeforeEach //Faz antes de cada teste
    void preparation() {
        repository = new CountryRepository(); //O "repository" torna-se uma List<Country> vazia - ver linha 12 do CountryRepository
    }
    //Este teste vai testar a criação e adicionar um novo country
    @Test
    void createAndAddNewCountry_ValidCountry(){

        //act
        repository.createAndAddNewCountry("Portugal"); //No repositório que criamos, usamos o méto.do createAndAddNewCountry, para adicionar "Portugal"

        //assert
        assertEquals(1, repository.getAllCountries().size()); //Verificamos se o repositório aumentou de tamanho (se adicionou, deve aumentar de 0 para 1)

        //assert (another valid country)
        repository.createAndAddNewCountry("France"); //Cria outro país com o createAndAddNewCountry no "repository"
        assertEquals(2, repository.getAllCountries().size()); //Volta a verificar se o repositório aumentou de trabalho
    }
    //Este teste vai testar a resposta em caso de ser introduzido um input inválido
    @Test
    void createAndAddNewCountry_InvalidInput(){

        //act (add invalid country)
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry("Mordor")); //"Quando tentar criar o país Mordor, o sistema deve falhar"

        //Repository should remain empty, since the input was invalid
        assertEquals(0,repository.getAllCountries().size()); //O input inválido não deve levar à mudança de tamanho do repositório
    }
    //Este teste vai testar o que acontece quando se faz createAndAddNewCountry com um input null
    @Test
    void createAndAddNewCountry_Null(){
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry(null));
        assertEquals(0, repository.getAllCountries().size());
    }
    //Este teste vai testar o que acontece quando se faz createAndAddNewCountry com input de um country já registado
    @Test
    void createAndAddNewCountry_Duplicate(){
        repository.createAndAddNewCountry("Portugal"); //A lista agora tem 1 country, "Portugal"

        //Now we add the same country again, expecting it to "throw"
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry("Portugal")); //Vai-se tentar adicionar o mesmo país novamente e dizer que se for feito isto, para dar throw ao "IllegalArgumentException"

        //Only 1 should have been added
        assertEquals(1, repository.getAllCountries().size()); //Verifica se o repositório só ficou com 1 país
    }
    //Este teste vai testar o que acontece quando fazemos createAndAddNewCountry e depois tentamos modificar a lista com os countries (não deve ser possível)
    @Test
    void createAndAddNewCountry_ReturnReadOnlyList(){
        repository.createAndAddNewCountry("Portugal"); //Cria e adiciona um novo país - "Portugal"
        List<Country> registeredCountries = repository.getAllCountries(); //Cria uma List<Country> chamada registeredCountries e vai buscar o méto.do getAllCountries para por os países dentro da lista

        //try to modify the list externally (not allowed)
        assertThrows(UnsupportedOperationException.class, //exception expected, implied in the name "UnsupportedOperation"
                () -> registeredCountries.add(new Country("France"))); //tenta-se modificar a lista para conter "France". Não pode ser possível
    }
}