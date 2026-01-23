package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;
import TOPSECRET.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class US007ToRegisterCountryControllerTest {

    private US007ToRegisterCountryController controller;
    private CountryRepository repository;

    @BeforeEach
    void preparation(){
        repository = new CountryRepository();
        controller = new US007ToRegisterCountryController(repository);
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