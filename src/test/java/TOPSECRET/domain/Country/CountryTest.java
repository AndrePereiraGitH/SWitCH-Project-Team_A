package TOPSECRET.domain.Country;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    @Test
    void constructor_validCountry() {
        //Act
        Country country = new Country("Portugal");
        //Assert
        assertNotNull(country);
    }

    @Test
    void constructor_nullCountryName() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new Country(null));

        assertEquals("Country name cannot be null or blank", exception.getMessage());
    }

    @Test
    void constructor_blankCountryName() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new Country("   "));

        assertEquals("Country name cannot be null or blank", exception.getMessage());
    }

    @Test
    void constructor_invalidCountryName() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new Country("Mordor"));

        assertEquals("Country name is not valid", exception.getMessage());
    }

    @Test
    void constructor_misspelledCountryName() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new Country("Portug"));

        assertEquals("Country name is not valid", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Getter tests
    // -------------------------------------------------------------------------

    @Test
    void getCountryName_returnsCorrectName() {
        //Arrange
        Country country = new Country("Portugal");
        //Act
        String result = country.getCountryName();
        //Assert
        assertEquals("Portugal", result);
    }

    @Test
    void getCountryID_returnsCorrectID() {
        //Arrange
        Country country = new Country("Portugal");
        //Act
        String result = country.getCountryID().getCountryID();
        //Assert
        assertEquals("PORTUGAL", result);
    }

    // -------------------------------------------------------------------------
    // Equals tests
    // -------------------------------------------------------------------------

    @Test
    void equals_sameObject() {
        //Arrange
        Country country = new Country("Portugal");
        //Act + Assert
        assertTrue(country.equals(country));
    }

    @Test
    void equals_sameCountryID() {
        //Arrange
        Country country1 = new Country("Portugal");
        Country country2 = new Country("Portugal");
        //Act + Assert
        assertTrue(country1.equals(country2));
    }

    @Test
    void equals_differentCountryID() {
        //Arrange
        Country country1 = new Country("Portugal");
        Country country2 = new Country("France");
        //Act + Assert
        assertFalse(country1.equals(country2));
    }

    @Test
    void equals_nullObject() {
        //Arrange
        Country country = new Country("Portugal");
        //Act + Assert
        assertFalse(country.equals(null));
    }

    @Test
    void equals_differentClass() {
        //Arrange
        Country country = new Country("Portugal");
        //Act + Assert
        assertFalse(country.equals("Portugal"));
    }
}