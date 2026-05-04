package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CountryId}.
 * <p>
 * This test class validates the behavior of the {@link CountryId} value object,
 * ensuring its correctness, immutability, and equality semantics.
 * </p>
 * <p>
 * The tests verify that:
 * <ul>
 *     <li>Valid identifiers are correctly created</li>
 *     <li>Null and blank inputs are rejected with appropriate exceptions</li>
 *     <li>The getter returns the correct value</li>
 *     <li>Equality is based on the identifier value</li>
 *     <li>Different objects behave correctly in equality comparisons</li>
 * </ul>
 * </p>
 * <p>
 * This class ensures that {@link CountryId} behaves as a proper value object,
 * where equality is based on state rather than reference.
 * </p>
 */
class CountryIdTest {

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    @Test
    void constructor_validID() {
        //Assert + Act
        CountryId id = new CountryId("PORTUGAL");

        //Assert
        assertNotNull(id);
    }

    @Test
    void constructor_nullID() {
        //Arrange + Act + Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> new CountryId(null));

        assertEquals("CountryID can't be null or empty.", exception.getMessage());
    }

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

    @Test
    void equals_sameObject() {
        //Arrange
        CountryId id = new CountryId("PORTUGAL");
        //Act + Assert
        assertTrue(id.equals(id));
    }

    @Test
    void equals_sameID() {
        //Arrange
        CountryId id1 = new CountryId("PORTUGAL");
        CountryId id2 = new CountryId("PORTUGAL");
        //Act + Assert
        assertTrue(id1.equals(id2));
    }

    @Test
    void equals_differentID() {
        //Arrange
        CountryId id1 = new CountryId("PORTUGAL");
        CountryId id2 = new CountryId("FRANCE");
        //Act + Assert
        assertFalse(id1.equals(id2));
    }

    @Test
    void equals_nullObject() {
        //Arrange
        CountryId id = new CountryId("PORTUGAL");
        //Act + Assert
        assertFalse(id.equals(null));
    }

    @Test
    void equals_differentClass() {
        //Arrange
        CountryId id = new CountryId("PORTUGAL");
        //Act + Assert
        assertFalse(id.equals("PORTUGAL"));
    }
}