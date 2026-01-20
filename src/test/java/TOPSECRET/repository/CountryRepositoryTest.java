package TOPSECRET.repository;

import static org.junit.jupiter.api.Assertions.*;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class CountryRepositoryTest {
    private CountryRepository repository;

    @BeforeEach
    void preparation() {
        repository = new CountryRepository();
    }

    @Test
    void createAndAddNewCountry_ValidCountry(){

        //act
        repository.createAndAddNewCountry("Portugal");

        //assert
        assertEquals(1, repository.getAllCountries().size());

        //assert (another valid country)
        repository.createAndAddNewCountry("France");
        assertEquals(2, repository.getAllCountries().size());
    }

    @Test
    void createAndAddNewCountry_InvalidInput(){

        //act (add invalid country)
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry("Mordor"));

        //Repository should remain empty, since the input was invalid
        assertEquals(0,repository.getAllCountries().size());
    }

    @Test
    void createAndAddNewCountry_Null(){
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry(null));
        assertEquals(0, repository.getAllCountries().size());
    }
/*
    @Test
    void createAndAddNewCountry_Duplicate(){
        repository.createAndAddNewCountry("Portugal");

        //Now we add the same country again, expecting it to "throw"
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddNewCountry("Portugal"));

        //Only 1 should have been added

        assertEquals(1, repository.getAllCountries().size());
    }

 */

    @Test
    void createAndAddNewCountry_ReturnReadOnlyList(){
        repository.createAndAddNewCountry("Portugal");
        List<Country> registeredCountries = repository.getAllCountries();

        //try to modify the list externally (not allowed)
        assertThrows(UnsupportedOperationException.class, //exception expected, implied in the name "UnsupportedOperation"
                () -> registeredCountries.add(new Country("France")));
    }
}