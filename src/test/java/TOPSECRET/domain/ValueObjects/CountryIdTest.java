package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CountryId} Value Object.
 *
 * <p>Validates construction rules and equality behavior.</p>
 */
class CountryIdTest {

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a valid identifier string produces a
     * non-null {@link CountryId} instance (happy path).
     */
    @Test
    void constructor_validID() {
        //Assert + Act
        CountryId id = new CountryId("PORTUGAL");

        //Assert
        assertNotNull(id);
    }

    /**
     * Tests that a {@code null} identifier throws
     * {@link IllegalArgumentException} with the expected message.
     */
    @Test
    void constructor_nullID() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new CountryId(null));

        assertEquals("CountryID can't be null or empty.", exception.getMessage());
    }

    /**
     * Tests that a blank identifier throws
     * {@link IllegalArgumentException} with the expected message.
     */
    @Test
    void constructor_blankID() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new CountryId("   "));

        assertEquals("CountryID can't be null or empty.", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Getter tests
    // -------------------------------------------------------------------------

    /**
     * Tests that {@link CountryId#getCountryID()} returns the correct identifier string.
     */
    @Test
    void getCountryID_returnsCorrectID() {
        //Arrange
        CountryId id = new CountryId("PORTUGAL");
        //Act
        String result = id.getCountryID();
        //Assert
        assertEquals("PORTUGAL", result);
    }

    // -------------------------------------------------------------------------
    // Equals tests
    // -------------------------------------------------------------------------

    /**
     * Tests that a {@link CountryId} is equal to itself (reflexivity).
     */
    @Test
    void equals_sameObject() {
        //Arrange
        CountryId id = new CountryId("PORTUGAL");
        //Act + Assert
        assertTrue(id.equals(id));
    }

    /**
     * Tests that two {@link CountryId} instances with the same
     * identifier string are considered equal.
     */
    @Test
    void equals_sameID() {
        //Arrange
        CountryId id1 = new CountryId("PORTUGAL");
        CountryId id2 = new CountryId("PORTUGAL");
        //Act + Assert
        assertTrue(id1.equals(id2));
    }

    /**
     * Tests that two {@link CountryId} instances with different
     * identifier strings are not considered equal.
     */
    @Test
    void equals_differentID() {
        //Arrange
        CountryId id1 = new CountryId("PORTUGAL");
        CountryId id2 = new CountryId("FRANCE");
        //Act + Assert
        assertFalse(id1.equals(id2));
    }

    /**
     * Tests that a {@link CountryId} is not equal to {@code null}.
     */
    @Test
    void equals_nullObject() {
        //Arrange
        CountryId id = new CountryId("PORTUGAL");
        //Act + Assert
        assertFalse(id.equals(null));
    }

    /**
     * Tests that a {@link CountryId} is not equal to an object of a different class.
     */
    @Test
    void equals_differentClass() {
        //Arrange
        CountryId id = new CountryId("PORTUGAL");
        //Act + Assert
        assertFalse(id.equals("PORTUGAL"));
    }
}