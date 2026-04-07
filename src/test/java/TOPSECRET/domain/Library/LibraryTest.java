package TOPSECRET.domain.Library;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    @NullAndEmptySource
    void shouldThrowExceptionWhenLibraryNameIsInvalid(String libraryName) {
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
    void shouldBeEqual_whenComparedToItself() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertEquals(library, library);
    }

    @Test
    void shouldNotBeEqual_whenComparedToNull() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertNotEquals(null, library);
    }

    @Test
    void shouldNotBeEqual_whenComparedToDifferentClass() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertNotEquals("not a library", library);
    }

    @Test
    void shouldNotBeEquals_whenComparedDifferentId() {
        UserId userDouble = createMockUser("Nome1");
        Library library1 = new Library("My Library", userDouble);
        Library library2 = new Library("My Library", userDouble);
        assertNotEquals(library1.id(), library2.id());
    }
    /**
     * Uses reflection to assign the same {@link LibraryId} to two distinct instances,
     * verifying that {@link Library#equals(Object)} returns {@code true} when IDs match.
     * Necessary since IDs are randomly generated and cannot be shared through normal construction.
     *
     * @throws Exception if reflection access to {@code _id} fails
     */
    @Test
    void shouldBeEqual_whenComparedToDifferentInstanceWithSameId() throws Exception {
        UserId userDouble = createMockUser("Nome1");
        Library l1 = new Library("Library A", userDouble);
        Library l2 = new Library("Library B", userDouble);

        java.lang.reflect.Field idField = Library.class.getDeclaredField("_id");
        idField.setAccessible(true);
        idField.set(l2, l1.id());

        assertEquals(l1, l2);
    }

    // -------------------------------------------------------------------------
    // sameAs()
    // -------------------------------------------------------------------------

    @Test
    void givenSameInstanceWhenSameAsThenReturnsTrue() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertTrue(library.sameAs(library));
    }

    @Test
    void givenTwoLibrariesWithSameAttributesWhenSameAsThenReturnsTrue() {
        UserId userDouble = createMockUser("Nome1");

        Library first  = new Library("Library A", userDouble);
        Library second = new Library("Library A", userDouble);

        assertTrue(first.sameAs(second));
    }

    @Test
    void givenDifferentNamesWhenSameAsThenReturnsFalse() {
        UserId userDouble = createMockUser("Nome1");

        Library first  = new Library("Library A", userDouble);
        Library second = new Library("Library B", userDouble);

        assertFalse(first.sameAs(second));
    }

    @Test
    void givenNullWhenSameAsThenReturnsFalse() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertFalse(library.sameAs(null));
    }

    @Test
    void givenDifferentTypeWhenSameAsThenReturnsFalse() {
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        assertFalse(library.sameAs(new Object()));
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