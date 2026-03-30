package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CityID} value object.
 *
 * <p>This test class verifies the correct behavior of the CityID,
 * including object creation, validation rules, equality logic,
 * hash code consistency, and getter method functionality.</p>
 *
 * <p>The tests ensure that the value object follows the rules of a DDD Value Object:
 * immutability, equality based on value, and validation of construction parameters.</p>
 */
class CityIDTest {

    /**
     * Tests if a CityID is successfully created
     * when a valid non-null and non-blank string is provided.
     */
    @Test
    void shouldCreateValidID() {
        // Arrange
        CityID id = new CityID("C1");

        // Assert
        assertEquals("C1", id.getCityID());
    }

    /**
     * Tests if the constructor throws IllegalArgumentException
     * when the provided ID is null.
     */
    @Test
    void shouldThrowExceptionWhenIDIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new CityID(null);
        });
    }

    /**
     * Tests if the constructor throws IllegalArgumentException
     * when the provided ID is blank.
     */
    @Test
    void shouldThrowExceptionWhenIDIsBlank() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new CityID("   ");
        });
    }

    /**
     * Tests if two CityID objects are considered equal
     * when they contain the same ID value.
     */
    @Test
    void shouldBeEqualWhenIDsAreSame() {
        // Arrange
        CityID id1 = new CityID("C1");
        CityID id2 = new CityID("C1");

        // Assert
        assertEquals(id1, id2);
    }

    /**
     * Tests if two CityID objects are not equal
     * when they contain different ID values.
     */
    @Test
    void shouldNotBeEqualWhenIDsAreDifferent() {
        // Arrange
        CityID id1 = new CityID("C1");
        CityID id2 = new CityID("C2");

        // Assert
        assertNotEquals(id1, id2);
    }

    /**
     * Tests if a CityID object is not equal to null.
     */
    @Test
    void shouldNotBeEqualToNull() {
        // Arrange
        CityID id = new CityID("C1");

        // Assert
        assertNotEquals(null, id);
    }

    /**
     * Tests if a CityID object is not equal
     * to an object of a different class.
     */
    @Test
    void shouldNotBeEqualToDifferentClass() {
        // Arrange
        CityID id = new CityID("C1");
        String other = "C1";

        // Assert
        assertNotEquals(other, id);
    }

    /**
     * Tests if the getCityID() method returns the correct ID value.
     */
    @Test
    void getterShouldReturnCorrectID() {
        // Arrange
        CityID id = new CityID("C1");

        // Assert
        assertEquals("C1", id.getCityID());
    }

    /**
     * Tests if two equal CityID objects
     * produce the same hash code.
     */
    @Test
    void hashCodeShouldBeEqualForSameID() {
        // Arrange
        CityID id1 = new CityID("C1");
        CityID id2 = new CityID("C1");

        // Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }
}