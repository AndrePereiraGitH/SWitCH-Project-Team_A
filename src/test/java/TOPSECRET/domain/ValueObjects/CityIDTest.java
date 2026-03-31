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

    @Test
    void shouldCreateValidID() {
        // Arrange
        CityID id = new CityID("C1");

        // Assert
        assertEquals("C1", id.getCityID());
    }

    @Test
    void shouldThrowExceptionWhenIDIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new CityID(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenIDIsBlank() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new CityID("   ");
        });
    }

    @Test
    void shouldBeEqualWhenIDsAreSame() {
        // Arrange
        CityID id1 = new CityID("C1");
        CityID id2 = new CityID("C1");

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualWhenIDsAreDifferent() {
        // Arrange
        CityID id1 = new CityID("C1");
        CityID id2 = new CityID("C2");

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        // Arrange
        CityID id = new CityID("C1");

        // Assert
        assertNotEquals(null, id);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        // Arrange
        CityID id = new CityID("C1");
        String other = "C1";

        // Assert
        assertNotEquals(other, id);
    }

    @Test
    void getterShouldReturnCorrectID() {
        // Arrange
        CityID id = new CityID("C1");

        // Assert
        assertEquals("C1", id.getCityID());
    }

    @Test
    void hashCodeShouldBeEqualForSameID() {
        // Arrange
        CityID id1 = new CityID("C1");
        CityID id2 = new CityID("C1");

        // Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }
}