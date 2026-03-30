package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link LibraryId}.
 *
 * <p>Covers construction validation, equality contract,
 * and string representation behaviour.</p>
 */
class LibraryIdTest {

    // -------------------------------------------------------------------------
    // Construction — happy path
    // -------------------------------------------------------------------------

    @Test
    void givenValidId_whenConstructing_thenCreatesSuccessfully() {
        new LibraryId("lib-001");
    }

    @Test
    void givenValidId_whenGetId_thenReturnsCorrectId() {
         // Arrange
        LibraryId libraryId = new LibraryId("lib-001");
        // Act
        String result = libraryId.getId();
        // Assert
        assertEquals("lib-001", libraryId.getId());
    }

    // -------------------------------------------------------------------------
    // Construction — invalid input
    // -------------------------------------------------------------------------

    @Test
    void givenNullId_whenConstructing_thenThrowsIllegalArgumentException() {
        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new LibraryId(null)
        );
        // Assert
        assertEquals("LibraryId must be a valid String.", ex.getMessage());
    }

    @Test
    void givenBlankId_whenConstructing_thenThrowsIllegalArgumentException() {
        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new LibraryId("   ")
        );
         // Assert
        assertEquals("LibraryId must be a valid String.", ex.getMessage());
    }

    @Test
    void givenEmptyId_whenConstructing_thenThrowsIllegalArgumentException() {
        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new LibraryId("")
        );
        // Assert
        assertEquals("LibraryId must be a valid String.", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Equality contract
    // -------------------------------------------------------------------------

    @Test
    void givenSameInstance_whenEquals_thenReturnsTrue() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-001");
        // Act + Assert
        assertEquals(libraryId, libraryId);
    }

    @Test
    void givenTwoIdsWithSameValue_whenEquals_thenReturnsTrue() {
        // Arrange
        LibraryId first = new LibraryId("lib-001");
        LibraryId second = new LibraryId("lib-001");
        // Assert
        assertEquals(first, second);
    }

    @Test
    void givenTwoIdsWithDifferentValues_whenEquals_thenReturnsFalse() {
        // Arrange
        LibraryId first = new LibraryId("lib-001");
        LibraryId second = new LibraryId("lib-002");
        // Assert
        assertNotEquals(first, second);
    }

    @Test
    void givenNull_whenEquals_thenReturnsFalse() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-001");
        // Assert
        assertNotEquals(null, libraryId);
    }

    @Test
    void givenDifferentType_whenEquals_thenReturnsFalse() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-001");
        // Assert
        assertNotEquals("lib-001", libraryId);
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Test
    void givenValidId_whenToString_thenReturnsRawId() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-001");
        // Assert
        assertEquals("lib-001", libraryId.toString());
    }

    @Test
    void givenValidId_whenToStringAndGetId_thenBothMatch() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-001");
       // Assert
        assertEquals(libraryId.getId(), libraryId.toString());
    }

    // -------------------------------------------------------------------------
    // Immutability
    // -------------------------------------------------------------------------

    @Test
    void givenValidId_whenGetIdCalledMultipleTimes_thenAlwaysReturnsSameValue() {
        // Arrange
        LibraryId libraryId = new LibraryId("lib-001");
        // Assert
        assertEquals(libraryId.getId(), libraryId.getId());
    }
}
