package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CityId} value object.
 *
 * <p>This test class verifies the correct behavior of the CityID,
 * including object creation, validation rules, equality logic,
 * hash code consistency, and getter method functionality.</p>
 *
 * <p>The tests ensure that the value object follows the rules of a DDD Value Object:
 * immutability, equality based on value, and validation of construction parameters.</p>
 */
class CityIdTest {

    @Test
    void shouldCreateValidID() {
        // Arrange
        CityId id = new CityId("C1");

        // Assert
        assertEquals("C1", id.getCityID());
    }

    @Test
    void shouldThrowExceptionWhenIDIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new CityId(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenIDIsBlank() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new CityId("   ");
        });
    }

    @Test
    void shouldBeEqualWhenIDsAreSame() {
        // Arrange
        CityId id1 = new CityId("C1");
        CityId id2 = new CityId("C1");

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualWhenIDsAreDifferent() {
        // Arrange
        CityId id1 = new CityId("C1");
        CityId id2 = new CityId("C2");

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        // Arrange
        CityId id = new CityId("C1");

        // Assert
        assertNotEquals(null, id);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        // Arrange
        CityId id = new CityId("C1");
        String other = "C1";

        // Assert
        assertNotEquals(other, id);
        assertFalse(id.equals(other));
    }

    @Test
    void getterShouldReturnCorrectID() {
        // Arrange
        CityId id = new CityId("C1");

        // Assert
        assertEquals("C1", id.getCityID());
    }

    @Test
    void hashCodeShouldBeEqualForSameID() {
        // Arrange
        CityId id1 = new CityId("C1");
        CityId id2 = new CityId("C1");

        // Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldBeEqualToItself() {
        // Arrange
        CityId id = new CityId("C1");

        // Assert
        assertEquals(id, id);
    }
}