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
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act & Assert
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
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act & Assert
        new Library("My Library", userDouble);
    }

    @Test
    void givenValidArgumentsThenIdIsNotNull() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act & Assert
        Library library = new Library("My Library", userDouble);
        assertNotNull(library.id());
    }

    @Test
    void givenTwoLibraries_whenConstructing_thenEachHasUniqueId() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act
        Library first = new Library("Library A", userDouble);
        Library second = new Library("Library B", userDouble);
        // Assert
        assertNotEquals(first.id(), second.id());
    }

    // ============ Constructor with LibraryId ===========

    @Test
    void shouldCreateLibraryWithGivenId_whenValidArguments() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        LibraryId id = new LibraryId("fixed-id");

        // Act
        Library library = new Library(id, "My Library", userDouble);

        // Assert
        assertEquals(id, library.id());
        assertEquals("My Library", library.getLibraryName());
        assertEquals(userDouble, library.getUserId());
    }

    @Test
    void shouldThrowException_whenLibraryIdIsNull() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Library(null, "My Library", userDouble)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_whenLibraryNameIsInvalid_inSecondConstructor(String name) {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        LibraryId id = new LibraryId("fixed-id");

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Library(id, name, userDouble)
        );
    }

    @Test
    void shouldThrowException_whenUserIsNull_inSecondConstructor() {
        // Arrange
        LibraryId id = new LibraryId("fixed-id");

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Library(id, "My Library", null)
        );
    }

    // -------------------------------------------------------------------------
    // getLibraryName()
    // -------------------------------------------------------------------------

    @Test
    void givenValidNameThenReturnsCorrectName() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act
        Library library = new Library("My Library", userDouble);
        // Assert
        assertEquals("My Library", library.getLibraryName());
    }

    // -------------------------------------------------------------------------
    //  id()
    // -------------------------------------------------------------------------

    @Test
    void givenValidLibraryThenReturnsLibraryId() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act
        Library library = new Library("My Library", userDouble);
        // Assert
        assertInstanceOf(LibraryId.class, library.id());
    }


    // -------------------------------------------------------------------------
    // equals()
    // -------------------------------------------------------------------------

    @Test
    void shouldBeEqual_whenComparedToItself() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act
        Library library = new Library("My Library", userDouble);
        // Assert
        assertEquals(library, library);
    }

    @Test
    void shouldNotBeEqual_whenComparedToNull() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act
        Library library = new Library("My Library", userDouble);
        // Assert
        assertNotEquals(null, library);
    }

    @Test
    void shouldNotBeEqual_whenComparedToDifferentClass() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act
        Library library = new Library("My Library", userDouble);
        // Assert
        assertNotEquals("not a library", library);
    }

    @Test
    void shouldNotBeEquals_whenComparedDifferentId() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        // Act
        Library library1 = new Library("My Library", userDouble);
        Library library2 = new Library("My Library", userDouble);
        // Assert
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
        // Arrange
        UserId userDouble = createMockUser("Nome1");

        // Act
        Library l1 = new Library("Library A", userDouble);
        Library l2 = new Library("Library B", userDouble);

        java.lang.reflect.Field idField = Library.class.getDeclaredField("_id");
        idField.setAccessible(true);
        idField.set(l2, l1.id());

        // Assert
        assertEquals(l1, l2);
    }

    // -------------------------------------------------------------------------
    // sameAs()
    // -------------------------------------------------------------------------

    @Test
    void givenSameInstanceWhenSameAsThenReturnsTrue() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        // Act
        boolean result = library.sameAs(library);
        // Assert
        assertTrue(result);
    }

    @Test
    void givenTwoLibrariesWithSameAttributesWhenSameAsThenReturnsTrue() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        Library first = new Library("Library A", userDouble);
        Library second = new Library("Library A", userDouble);

        // Act
        boolean result = first.sameAs(second);

        // Assert
        assertTrue(result);
    }


    @Test
    void givenDifferentNamesWhenSameAsThenReturnsFalse() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        Library first = new Library("Library A", userDouble);
        Library second = new Library("Library B", userDouble);
        // Act
        boolean result = first.sameAs(second);
        // Assert
        assertFalse(result);
    }

    @Test
    void givenNullWhenSameAsThenReturnsFalse() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        // Act
        boolean result = library.sameAs(null);
        // Assert
        assertFalse(result);
    }

    @Test
    void givenDifferentTypeWhenSameAsThenReturnsFalse() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);
        // Act
        boolean result = library.sameAs(new Object());
        // Assert
        assertFalse(result);
    }

    @Test
    void shouldNotBeEqual_whenDifferentInstancesHaveDifferentIdsEvenWithSameNameAndUser() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");

        // Act
        Library first = new Library("My Library", userDouble);
        Library second = new Library("My Library", userDouble);

        // Assert
        assertNotEquals(first, second);
    }

    @Test
    void shouldReturnSameIdOnMultipleCalls() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        Library library = new Library("My Library", userDouble);

        // Act
        LibraryId firstId = library.id();
        LibraryId secondId = library.id();

        // Assert
        assertSame(firstId, secondId);
    }

    @Test
    void sameAsShouldReturnTrue_whenNamesAreEqualEvenIfIdsAreDifferent() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");
        Library first = new Library("Library A", userDouble);
        Library second = new Library("Library A", userDouble);

        // Act
        boolean result = first.sameAs(second);

        // Assert
        assertTrue(result);
        assertNotEquals(first.id(), second.id());
    }


    @Test
    void constructorShouldAcceptNonBlankNameWithLeadingAndTrailingSpaces() {
        // Arrange
        UserId userDouble = createMockUser("Nome1");

        // Act
        Library library = new Library("  My Library  ", userDouble);

        // Assert
        assertEquals("  My Library  ", library.getLibraryName());
    }


    // -------------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------------

    private UserId createMockUser(String name) {
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUserId()).thenReturn(name);
        return userDouble;
    }
}