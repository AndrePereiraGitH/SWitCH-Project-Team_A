//package TOPSECRET.controller;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import TOPSECRET.domain.Country.Country;
//import TOPSECRET.repository.CountryRepositoryMem;
//import TOPSECRET.domain.Country.CountryFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//class US007ToRegisterCountryControllerDependenciesTest {
//
//    private US007ToRegisterCountryController controller;
//    private CountryRepositoryMem repository;
//    private CountryFactory countryFactory;
//
//
//    @BeforeEach
//    void preparation(){
//        countryFactory = new CountryFactory();
//        repository = new CountryRepositoryMem(countryFactory);
//        controller = new US007ToRegisterCountryController(repository);
//    }
//
//
//    @Test
//    void registerNewCountry_ValidCountry(){
//        Country country = controller.registerNewCountry("Portugal");
//
//        assertNotNull(country);
//        assertEquals(1, repository.getAllCountries().size());
//        assertEquals("Portugal", repository.getAllCountries().get(0).getCountryName());
//    }
//
//
//    @Test
//    void registerNewCountry_InvalidCountryThrowException(){
//        assertThrows(IllegalArgumentException.class,
//                () -> controller.registerNewCountry("Mordor"));
//        assertEquals(0, repository.getAllCountries().size());
//    }
//
//    @Test
//    void registerNewCountry_nullName(){
//
//        assertThrows(IllegalArgumentException.class, () -> controller.registerNewCountry(null));
//    }
//
//    @Test
//    void registerNewCountry_blankName(){
//
//        assertThrows(IllegalArgumentException.class, () -> controller.registerNewCountry(" "));
//    }
//}