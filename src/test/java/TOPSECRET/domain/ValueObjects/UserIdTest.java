package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    // Constructor tests
    @Test
    void constructor_ValidId_CreatesUserId() {
        new UserId("david123");
    }

    @Test
    void constructor_NullId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserId(null)
        );
        assertEquals("UserId can't be null or empty.", exception.getMessage());
    }

    @Test
    void constructor_BlankId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserId("   ")
        );
        assertEquals("UserId can't be null or empty.", exception.getMessage());
    }

    @Test
    void constructor_EmptyId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserId("")
        );
        assertEquals("UserId can't be null or empty.", exception.getMessage());
    }

    // equals() tests
    @Test
    void equals_EqualIds_ReturnsTrue() {
        UserId userId1 = new UserId("david123");
        UserId userId2 = new UserId("david123");
        assertEquals(userId1, userId2);
    }

    @Test
    void equals_DifferentIds_ReturnsFalse() {
        UserId userId1 = new UserId("david123");
        UserId userId2 = new UserId("helena456");
        assertNotEquals(userId1, userId2);
    }

    @Test
    void equals_NullObject_ReturnsFalse() {
        UserId userId = new UserId("david123");
        assertNotEquals(null, userId);
    }

    @Test
    void equals_DifferentClass_ReturnsFalse() {
        UserId userId = new UserId("david123");
        String notAUserId = "david123";
        assertNotEquals(userId, notAUserId);
    }

    // getUsername() tests
    @Test
    void getUsername() {
        UserId userId = new UserId("david123");
        assertEquals("david123", userId.getUsername());
    }
}