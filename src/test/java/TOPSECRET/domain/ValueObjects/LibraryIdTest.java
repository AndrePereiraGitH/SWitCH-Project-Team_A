package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link LibraryId} value object.
 * <p>
 * This test class verifies construction validation, equality contract,
 * hash code consistency, and string representation.
 * </p>
 */
class LibraryIdTest {

    // ----------- Constructor Tests -------------

    @Test
    void givenValidId_whenConstructing_thenCreatesSuccessfully() {
        // Arrange + Act + Assert
        assertDoesNotThrow(() -> new LibraryId("lib-123"));
    }

    @Test
    void givenNullId_whenConstructing_thenThrowsIllegalArgumentException() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryId(null));
    }

    @Test
    void givenBlankId_whenConstructing_thenThrowsIllegalArgumentException() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryId("   "));
    }

    @Test
    void givenEmptyId_whenConstructing_thenThrowsIllegalArgumentException() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryId(""));
    }

    //  ------------- getId() Tests  -------------

    @Test
    void givenValidId_whenGetId_thenReturnsId() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-123");

        // Act
        String result = libraryId.getId();

        // Assert
        assertEquals("lib-123", result);
    }

    //  ------------- equals() Tests  -------------

    @Test
    void givenSameInstance_whenEquals_thenReturnsTrue() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-123");

        // Act + Assert
        assertEquals(libraryId, libraryId);
    }

    @Test
    void givenEqualIds_whenEquals_thenReturnsTrue() {
        // Arrange
        LibraryId libraryId1 = new LibraryId("lib-123");
        LibraryId libraryId2 = new LibraryId("lib-123");

        // Act + Assert
        assertEquals(libraryId1, libraryId2);
    }

    @Test
    void givenDifferentIds_whenEquals_thenReturnsFalse() {
        // Arrange
        LibraryId libraryId1 = new LibraryId("lib-123");
        LibraryId libraryId2 = new LibraryId("lib-456");

        // Act + Assert
        assertNotEquals(libraryId1, libraryId2);
    }

    @Test
    void givenNull_whenEquals_thenReturnsFalse() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-123");

        // Act + Assert
        assertNotEquals(null, libraryId);
    }

    @Test
    void givenDifferentType_whenEquals_thenReturnsFalse() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-123");

        // Act + Assert
        assertNotEquals("lib-123", libraryId);
    }

    //  ------------- hashCode() Tests  -------------

    @Test
    void givenEqualIds_whenHashCode_thenReturnsSameHashCode() {
        // Arrange
        LibraryId libraryId1 = new LibraryId("lib-123");
        LibraryId libraryId2 = new LibraryId("lib-123");

        // Act + Assert
        assertEquals(libraryId1.hashCode(), libraryId2.hashCode());
    }

    @Test
    void givenDifferentIds_whenHashCode_thenReturnsDifferentHashCode() {
        // Arrange
        LibraryId libraryId1 = new LibraryId("lib-123");
        LibraryId libraryId2 = new LibraryId("lib-456");

        // Act + Assert
        assertNotEquals(libraryId1.hashCode(), libraryId2.hashCode());
    }

    //  ------------- toString() Tests  -------------

    @Test
    void givenValidId_whenToString_thenReturnsId() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-123");

        // Act
        String result = libraryId.toString();

        // Assert
        assertEquals("lib-123", result);
    }
}