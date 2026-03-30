package TOPSECRET.domain.Country;

import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CountryFactory} class.
 *
 * <p>Validates that the factory correctly creates {@link Country} instances
 * with deterministic {@link CountryID}s derived from the country name.</p>
 */
class CountryFactoryTest {

    /** System Under Test: the factory responsible for creating countries. */
    private CountryFactory factory;

    /**
     * Initializes a new {@link CountryFactory} before each test.
     */
    @BeforeEach
    void preparation() {
        factory = new CountryFactory();
    }

    // -------------------------------------------------------------------------
    // create() tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a valid country name produces a non-null
     * {@link Country} instance (happy path).
     */
    @Test
    void create_validCountry() {

        //Act
        Country country = factory.create("Portugal");

        //Assert
        assertNotNull(country);
    }

    /**
     * Tests that the created {@link Country} holds the correct name.
     */
    @Test
    void create_returnsCorrectCountryName() {

        //Act
        Country country = factory.create("Portugal");

        //Assert
        assertEquals("Portugal", country.getCountryName());
    }

    /**
     * Tests that the {@link CountryID} is derived from the country name
     * in uppercase (e.g., "Portugal" → {@code CountryID("PORTUGAL")}).
     */
    @Test
    void create_generatesDeterministicID() {

        //Act
        Country country = factory.create("Portugal");

        //Assert
        assertEquals("PORTUGAL", country.getCountryID().getCountryID());
    }

    /**
     * Tests that two calls with the same country name produce
     * the same {@link CountryID}, guaranteeing consistency across the system.
     */
    @Test
    void create_sameNameGeneratesSameID() {

        //Act
        Country country1 = factory.create("Portugal");
        Country country2 = factory.create("Portugal");

        //Assert
        assertEquals(country1.getCountryID(), country2.getCountryID());
    }

    /**
     * Tests that two calls with different country names produce
     * different {@link CountryID}s.
     */
    @Test
    void create_differentNameGeneratesDifferentID() {

        //Act
        Country country1 = factory.create("Portugal");
        Country country2 = factory.create("France");

        //Assert
        assertNotEquals(country1.getCountryID(), country2.getCountryID());
    }

    /**
     * Tests that a {@code null} country name throws
     * {@link IllegalArgumentException}.
     */
    @Test
    void create_nullCountryName() {

        //Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null));
    }

    /**
     * Tests that a blank country name throws
     * {@link IllegalArgumentException}.
     */
    @Test
    void create_blankCountryName() {

        //Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("   "));
    }

    /**
     * Tests that a country name not present in the {@code Countries} enum
     * throws {@link IllegalArgumentException}.
     */
    @Test
    void create_invalidCountryName() {

        //Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("Mordor"));
    }
}