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
        LibraryId libraryId = new LibraryId("lib-001");
        assertEquals("lib-001", libraryId.getId());
    }

    // -------------------------------------------------------------------------
    // Construction — invalid input
    // -------------------------------------------------------------------------

    @Test
    void givenNullId_whenConstructing_thenThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new LibraryId(null)
        );
        assertEquals("ID must be a valid String.", ex.getMessage());
    }

    @Test
    void givenBlankId_whenConstructing_thenThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new LibraryId("   ")
        );
        assertEquals("ID must be a valid String.", ex.getMessage());
    }

    @Test
    void givenEmptyId_whenConstructing_thenThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> new LibraryId("")
        );
        assertEquals("ID must be a valid String.", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Equality contract
    // -------------------------------------------------------------------------

    @Test
    void givenSameInstance_whenEquals_thenReturnsTrue() {
        LibraryId libraryId = new LibraryId("lib-001");
        assertEquals(libraryId, libraryId);
    }

    @Test
    void givenTwoIdsWithSameValue_whenEquals_thenReturnsTrue() {
        LibraryId first = new LibraryId("lib-001");
        LibraryId second = new LibraryId("lib-001");
        assertEquals(first, second);
    }

    @Test
    void givenTwoIdsWithDifferentValues_whenEquals_thenReturnsFalse() {
        LibraryId first = new LibraryId("lib-001");
        LibraryId second = new LibraryId("lib-002");
        assertNotEquals(first, second);
    }

    @Test
    void givenNull_whenEquals_thenReturnsFalse() {
        LibraryId libraryId = new LibraryId("lib-001");
        assertNotEquals(null, libraryId);
    }

    @Test
    void givenDifferentType_whenEquals_thenReturnsFalse() {
        LibraryId libraryId = new LibraryId("lib-001");
        assertNotEquals("lib-001", libraryId);
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Test
    void givenValidId_whenToString_thenReturnsRawId() {
        LibraryId libraryId = new LibraryId("lib-001");
        assertEquals("lib-001", libraryId.toString());
    }

    @Test
    void givenValidId_whenToStringAndGetId_thenBothMatch() {
        LibraryId libraryId = new LibraryId("lib-001");
        assertEquals(libraryId.getId(), libraryId.toString());
    }

    // -------------------------------------------------------------------------
    // Immutability
    // -------------------------------------------------------------------------

    @Test
    void givenValidId_whenGetIdCalledMultipleTimes_thenAlwaysReturnsSameValue() {
        LibraryId libraryId = new LibraryId("lib-001");
        assertEquals(libraryId.getId(), libraryId.getId());
    }
}
