package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class US007ToRegisterCountryControllerTest {

    private US007ToRegisterCountryController controller;

    private ICountryRepository countryRepoDouble;

    private Country countryDouble;

    @BeforeEach
    void preparation(){

        countryRepoDouble = mock(ICountryRepository.class); //Creates the mock
        countryDouble = mock(Country.class);
        controller = new US007ToRegisterCountryController(countryRepoDouble);
    }

    @Test
    void registerNewCountry_validCountry_double(){
        //Arrange
        when(countryRepoDouble.createAndAdd("Portugal")).thenReturn(countryDouble);

        when(countryDouble.getCountryName()).thenReturn("Portugal");

        //Act
        Country result = controller.registerNewCountry("Portugal");

        //assert
        assertEquals("Portugal", result.getCountryName());
        assertNotNull(result);

    }

    @Test
    void registerNewCountry_InvalidCountry_double(){

        when(countryRepoDouble.createAndAdd("Mordor")).thenThrow(new IllegalArgumentException("Country name is not valid"));

        //assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerNewCountry("Mordor"));

    }

    @Test
    void registerNewCountry_nullCountry_double(){

        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> controller.registerNewCountry(null));

        assertEquals("Country name cannot be null or blank", exception.getMessage());
    }

    @Test
    void registerNewCountry_blankCountry_double(){

        // Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> controller.registerNewCountry("   "));

        assertEquals("Country name cannot be null or blank", exception.getMessage());
    }

    @Test
    void registerNewCountry_duplicateCountry() {
        // Arrange
        when(countryRepoDouble.createAndAdd("Portugal"))
                .thenThrow(new IllegalArgumentException("Country is already registered"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerNewCountry("Portugal"));
    }

}