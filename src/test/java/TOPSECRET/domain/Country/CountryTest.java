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
        Country country = new Country("Portugal");
        assertNotNull(country);
    }

    /**
     * Tests that a {@code null} country name throws
     * {@link IllegalArgumentException} with the expected message.
     */
    @Test
    void constructor_nullCountryName() {

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
        Country country = new Country("Portugal");

        assertEquals("Portugal", country.getCountryName());
    }

    /**
     * Tests that {@link Country#getCountryID()} returns the correct {@link CountryID}.
     */
    @Test
    void getCountryID_returnsCorrectID() {

        Country country = new Country("Portugal");

        assertEquals("PORTUGAL", country.getCountryID().getCountryID());
    }

    // -------------------------------------------------------------------------
    // Equals tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a {@link Country} is equal to itself (reflexivity).
     */
    @Test
    void equals_sameObject() {
        Country country = new Country("Portugal");

        assertTrue(country.equals(country));
    }

    /**
     * Tests that two {@link Country} instances with the same {@link CountryID}
     * are considered equal.
     */
    @Test
    void equals_sameCountryID() {
        Country country1 = new Country("Portugal");
        Country country2 = new Country("Portugal");

        assertTrue(country1.equals(country2));
    }

    /**
     * Tests that two {@link Country} instances with different {@link CountryID}s
     * are not considered equal.
     */
    @Test
    void equals_differentCountryID() {
        Country country1 = new Country("Portugal");
        Country country2 = new Country("France");

        assertFalse(country1.equals(country2));
    }

    /**
     * Tests that a {@link Country} is not equal to {@code null}.
     */
    @Test
    void equals_nullObject() {
        Country country = new Country("Portugal");

        assertFalse(country.equals(null));
    }

    /**
     * Tests that a {@link Country} is not equal to an object of a different class.
     */
    @Test
    void equals_differentClass() {
        Country country = new Country("Portugal");

        assertFalse(country.equals("Portugal"));
    }
}