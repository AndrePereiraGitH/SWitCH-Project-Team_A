package TOPSECRET.domain.Country;

import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Country} Aggregate Root.
 *
 * <p>Validates construction rules, getters, and equality behavior.</p>
 */
class CountryTest {

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a valid {@link CountryID} and country name produce
     * a non-null {@link Country} instance (happy path).
     */
    @Test
    void constructor_validCountry() {
        //Act
        Country country = new Country("Portugal");
        //Assert
        assertNotNull(country);
    }

    /**
     * Tests that a {@code null} country name throws
     * {@link IllegalArgumentException} with the expected message.
     */
    @Test
    void constructor_nullCountryName() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new Country(null));

        assertEquals("Country name cannot be null or blank", exception.getMessage());
    }

    /**
     * Tests that a blank country name throws
     * {@link IllegalArgumentException} with the expected message.
     */
    @Test
    void constructor_blankCountryName() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new Country("   "));

        assertEquals("Country name cannot be null or blank", exception.getMessage());
    }

    /**
     * Tests that a country name not present in the {@code Countries} enum
     * throws {@link IllegalArgumentException} with the expected message.
     */
    @Test
    void constructor_invalidCountryName() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new Country("Mordor"));

        assertEquals("Country name is not valid", exception.getMessage());
    }

    /**
     * Tests that a misspelled country name throws
     * {@link IllegalArgumentException} with the expected message.
     */
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

    /**
     * Tests that {@link Country#getCountryName()} returns the correct name.
     */
    @Test
    void getCountryName_returnsCorrectName() {
        //Arrange
        Country country = new Country("Portugal");
        //Act
        String result = country.getCountryName();
        //Assert
        assertEquals("Portugal", result);
    }

    /**
     * Tests that {@link Country#getCountryID()} returns the correct {@link CountryID}.
     */
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

    /**
     * Tests that a {@link Country} is equal to itself (reflexivity).
     */
    @Test
    void equals_sameObject() {
        //Arrange
        Country country = new Country("Portugal");
        //Act + Assert
        assertTrue(country.equals(country));
    }

    /**
     * Tests that two {@link Country} instances with the same {@link CountryID}
     * are considered equal.
     */
    @Test
    void equals_sameCountryID() {
        //Arrange
        Country country1 = new Country("Portugal");
        Country country2 = new Country("Portugal");
        //Act + Assert
        assertTrue(country1.equals(country2));
    }

    /**
     * Tests that two {@link Country} instances with different {@link CountryID}s
     * are not considered equal.
     */
    @Test
    void equals_differentCountryID() {
        //Arrange
        Country country1 = new Country("Portugal");
        Country country2 = new Country("France");
        //Act + Assert
        assertFalse(country1.equals(country2));
    }

    /**
     * Tests that a {@link Country} is not equal to {@code null}.
     */
    @Test
    void equals_nullObject() {
        //Arrange
        Country country = new Country("Portugal");
        //Act + Assert
        assertFalse(country.equals(null));
    }

    /**
     * Tests that a {@link Country} is not equal to an object of a different class.
     */
    @Test
    void equals_differentClass() {
        //Arrange
        Country country = new Country("Portugal");
        //Act + Assert
        assertFalse(country.equals("Portugal"));
    }
}