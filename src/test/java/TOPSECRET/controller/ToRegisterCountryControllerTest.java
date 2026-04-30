package TOPSECRET.controller;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToRegisterCountryControllerTest {

    private ToRegisterCountryController controller;

    private CountryService countryServiceDouble;
    private Country countryDouble;

    @BeforeEach
    void preparation() {
        countryServiceDouble = mock(CountryService.class);
        countryDouble = mock(Country.class);

        controller = new ToRegisterCountryController(countryServiceDouble);
    }

    @Test
    void registerNewCountry_validCountry() {
        // Arrange
        when(countryServiceDouble.createAndSaveCountry("Portugal"))
                .thenReturn(countryDouble);

        when(countryDouble.getCountryName()).thenReturn("Portugal");

        // Act
        Country result = controller.registerNewCountry("Portugal");

        // Assert
        assertNotNull(result);
        assertEquals("Portugal", result.getCountryName());

        verify(countryServiceDouble).createAndSaveCountry("Portugal");
    }

    @Test
    void registerNewCountry_invalidCountry() {
        // Arrange
        when(countryServiceDouble.createAndSaveCountry("Mordor"))
                .thenThrow(new IllegalArgumentException("Country name is not valid"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerNewCountry("Mordor"));
    }

    @Test
    void registerNewCountry_nullCountry() {
        // Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> controller.registerNewCountry(null));

        assertEquals("Country name cannot be null or blank", exception.getMessage());

        verifyNoInteractions(countryServiceDouble);
    }

    @Test
    void registerNewCountry_blankCountry() {
        // Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> controller.registerNewCountry("   "));

        assertEquals("Country name cannot be null or blank", exception.getMessage());

        verifyNoInteractions(countryServiceDouble);
    }

    @Test
    void registerNewCountry_duplicateCountry() {
        // Arrange
        when(countryServiceDouble.createAndSaveCountry("Portugal"))
                .thenThrow(new IllegalArgumentException("Country is already registered"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerNewCountry("Portugal"));
    }
}