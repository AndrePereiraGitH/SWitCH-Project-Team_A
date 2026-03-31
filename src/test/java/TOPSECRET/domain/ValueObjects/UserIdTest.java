package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UserId} Value Object.
 * <p>
 * This test class verifies the behaviour of {@code UserId} regarding:
 * </p>
 * <ul>
 *     <li>Constructor validation — valid, null, blank, and empty inputs</li>
 *     <li>Equality — equal ids, different ids, null object, and different class</li>
 *     <li>Getter — correct return of the underlying identifier</li>
 * </ul>
 */

class UserIdTest {

    // Constructor tests
    @Test
    void constructor_ValidId_CreatesUserId() {

        // Arrange & Act
        UserId userId = new UserId("david123");

        // Assert
        assertNotNull(userId);
        assertEquals("david123", userId.getUsername());
    }

    @Test
    void constructor_NullId_ThrowsIllegalArgumentException() {

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserId(null)
        );
        assertEquals("UserId can't be null or empty.", exception.getMessage());
    }

    @Test
    void constructor_BlankId_ThrowsIllegalArgumentException() {

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserId("   ")
        );
        assertEquals("UserId can't be null or empty.", exception.getMessage());
    }

    @Test
    void constructor_EmptyId_ThrowsIllegalArgumentException() {

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserId("")
        );
        assertEquals("UserId can't be null or empty.", exception.getMessage());
    }

    // equals() tests
    @Test
    void equals_EqualIds_ReturnsTrue() {

        // Arrange
        UserId userId1 = new UserId("david123");
        UserId userId2 = new UserId("david123");

        // Assert
        assertEquals(userId1, userId2);
    }

    @Test
    void equals_DifferentIds_ReturnsFalse() {
        // Arrange
        UserId userId1 = new UserId("david123");
        UserId userId2 = new UserId("helena456");

        // Assert
        assertNotEquals(userId1, userId2);
    }

    @Test
    void equals_SameObject_ReturnsTrue() {

        // Arrange
        UserId userId = new UserId("david123");

        // Act & Assert
        assertEquals(userId, userId);
    }

    @Test
    void equals_NullObject_ReturnsFalse() {

        // Arrange
        UserId userId = new UserId("david123");

        // Act & Assert
        assertNotEquals(null, userId);
    }

    @Test
    void equals_DifferentClass_ReturnsFalse() {

        // Arrange
        UserId userId = new UserId("david123");
        String notAUserId = "david123";

        // Assert
        assertNotEquals(userId, notAUserId);
    }

    // getUsername() tests
    @Test
    void getUsername_ReturnsCorrectUsername() {
        // Arrange
        UserId userId = new UserId("david123");

        // Act
        String result = userId.getUsername();

        // Assert
        assertEquals("david123", result);
    }
}