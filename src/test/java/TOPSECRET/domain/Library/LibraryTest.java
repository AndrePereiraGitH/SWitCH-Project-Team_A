package TOPSECRET.domain.Library;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.Enum.*;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

/**
 * Unit tests for the {@link Library} class.
 * <p>
 * This test class verifies the behavior of the Library class including construction,
 * publication management, and getter methods.
 * </p>
 */
class LibraryTest {


    // ============ Constructor Tests ============


    @ParameterizedTest
    @NullAndEmptySource    void shouldThrowExceptionWhenLibraryNameIsInvalid(String libraryName) {
        // Arrange
        UserId userDouble = createMockUser("Nome1");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(libraryName, userDouble));
    }

    @Test
    void shouldThrowExceptionWhenGivenBlankLibraryName() {
        UserId userDouble = createMockUser("Nome1");
        assertThrows(
                IllegalArgumentException.class,
                () -> new Library("   ", userDouble)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange
        String libraryName = "Some valid name";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(libraryName, null));
    }


    @Test
    void shouldInitializeInstanceWhenValidParameters() {
        UserId userDouble = createMockUser("Nome1");
        new Library("My Library", userDouble);
    }

    @Test
    void givenValidArgumentsThenIdIsNotNull() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertNotNull(library.id());
    }

    @Test
    void givenTwoLibraries_whenConstructing_thenEachHasUniqueId() {
        UserId userDouble = createMockUser("Nome1");
        Library first  = new Library("Library A", userDouble);
        Library second = new Library("Library B", userDouble);
        assertNotEquals(first.id(), second.id());
    }

    // -------------------------------------------------------------------------
    // getLibraryName()
    // -------------------------------------------------------------------------

    @Test
    void givenValidNameThenReturnsCorrectName() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertEquals("My Library", library.getLibraryName());
    }

    // -------------------------------------------------------------------------
    // id()
    // -------------------------------------------------------------------------

    @Test
    void givenValidLibraryThenReturnsLibraryId() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertInstanceOf(LibraryId.class, library.id());
    }


    // -------------------------------------------------------------------------
    // equals()
    // -------------------------------------------------------------------------

    @Test
    void givenSameInstanceWhenEqualsThenReturnsTrue() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertEquals(library, library);
    }

    @Test
    void givenTwoLibrariesWithDifferentIds_whenEquals_thenReturnsFalse() {
        UserId userDouble = createMockUser("Nome1");
        Library libraryA  = new Library("Library A", userDouble);
        Library libraryB = new Library("Library A", userDouble);
        assertNotEquals(libraryA, libraryB);
    }

    @Test
    void givenNullWhenEqualsThenReturnsFalse() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertNotEquals(null, library);
    }

    // -------------------------------------------------------------------------
    // sameIdentityAs()
    // -------------------------------------------------------------------------

    @Test
    void givenSameInstanceWhenSameIdentityAsThenReturnsTrue() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertTrue(library.sameIdentityAs(library));
    }

    @Test
    void givenTwoLibrariesWithDifferentIdsWhenSameIdentityAsThenReturnsFalse() {
        UserId userDouble = createMockUser("Nome1");
        Library first  = new Library("Library A", userDouble);
        Library second = new Library("Library B", userDouble);
        assertFalse(first.sameIdentityAs(second));
    }

    @Test
    void givenNullWhenSameIdentityAsThenReturnsFalse() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertFalse(library.sameIdentityAs(null));
    }

    @Test
    void givenDifferentTypeWhenSameIdentityAsThenReturnsFalse() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertFalse(library.sameIdentityAs(new Object()));
    }

    // -------------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------------

    private UserId createMockUser(String name){
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn(name);
        return userDouble;
    }
}