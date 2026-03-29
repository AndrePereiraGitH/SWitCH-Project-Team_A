package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CountryID} Value Object.
 *
 * <p>Validates construction rules and equality behavior.</p>
 */
class CountryIDTest {

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a valid identifier string produces a
     * non-null {@link CountryID} instance (happy path).
     */
    @Test
    void constructor_validID() {
        CountryID id = new CountryID("PORTUGAL");
        assertNotNull(id);
    }

    /**
     * Tests that a {@code null} identifier throws
     * {@link IllegalArgumentException} with the expected message.
     */
    @Test
    void constructor_nullID() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new CountryID(null));

        assertEquals("CountryID can't be null or empty.", exception.getMessage());
    }

    /**
     * Tests that a blank identifier throws
     * {@link IllegalArgumentException} with the expected message.
     */
    @Test
    void constructor_blankID() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new CountryID("   "));

        assertEquals("CountryID can't be null or empty.", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Getter tests
    // -------------------------------------------------------------------------

    /**
     * Tests that {@link CountryID#getCountryID()} returns the correct identifier string.
     */
    @Test
    void getCountryID_returnsCorrectID() {
        CountryID id = new CountryID("PORTUGAL");
        assertEquals("PORTUGAL", id.getCountryID());
    }

    // -------------------------------------------------------------------------
    // Equals tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a {@link CountryID} is equal to itself (reflexivity).
     */
    @Test
    void equals_sameObject() {
        CountryID id = new CountryID("PORTUGAL");
        assertTrue(id.equals(id));
    }

    /**
     * Tests that two {@link CountryID} instances with the same
     * identifier string are considered equal.
     */
    @Test
    void equals_sameID() {
        CountryID id1 = new CountryID("PORTUGAL");
        CountryID id2 = new CountryID("PORTUGAL");
        assertTrue(id1.equals(id2));
    }

    /**
     * Tests that two {@link CountryID} instances with different
     * identifier strings are not considered equal.
     */
    @Test
    void equals_differentID() {
        CountryID id1 = new CountryID("PORTUGAL");
        CountryID id2 = new CountryID("FRANCE");
        assertFalse(id1.equals(id2));
    }

    /**
     * Tests that a {@link CountryID} is not equal to {@code null}.
     */
    @Test
    void equals_nullObject() {
        CountryID id = new CountryID("PORTUGAL");
        assertFalse(id.equals(null));
    }

    /**
     * Tests that a {@link CountryID} is not equal to an object of a different class.
     */
    @Test
    void equals_differentClass() {
        CountryID id = new CountryID("PORTUGAL");
        assertFalse(id.equals("PORTUGAL"));
    }
}