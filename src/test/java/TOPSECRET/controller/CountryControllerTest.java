package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;
import TOPSECRET.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryControllerTest {

    private CountryController controller;
    private CountryRepository repository;

    @BeforeEach
    void preparation(){
        repository = new CountryRepository();
        controller = new CountryController(repository);
    }

    @Test
    void registerNewCountry_ValidCountry(){
        controller.registerNewCountry("Portugal");

        assertEquals(1, repository.getAllCountries().size());
        assertEquals("Portugal", repository.getAllCountries().get(0).getCountryName());
    }

    @Test
    void registerNewCountry_InvalidCountryThrowException(){
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerNewCountry("Mordor"));
        assertEquals(0, repository.getAllCountries().size());
    }
}