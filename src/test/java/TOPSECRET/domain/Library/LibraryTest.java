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


    // --------------------------------------
    //              Constructor Tests
    //---------------------------------------

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenLibraryNameIsInvalid(String libraryName) {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(libraryName, userIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenGivenBlankLibraryName() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Library("   ", userIdDouble)
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
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act
        Library lib = new Library("My Library", userIdDouble);

        //Assert
        assertNotNull(lib);
    }

    @Test
    void givenValidArgumentsThenIdIsNotNull() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act
        Library library = new Library("My Library", userIdDouble);

        //Assert
        assertNotNull(library.id());
    }

    @Test
    void givenTwoLibraries_whenConstructing_thenEachHasUniqueId() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act
        Library first = new Library("Library A", userIdDouble);
        Library second = new Library("Library B", userIdDouble);

        // Assert
        assertNotEquals(first.id(), second.id());
    }

    // ------------------------------------------------
    //          Constructor with LibraryId
    // ------------------------------------------------

    @Test
    void shouldCreateLibraryWithGivenId_whenValidArguments() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");
        LibraryId id = new LibraryId("fixed-id");

        // Act
        Library library = new Library(id, "My Library", userIdDouble);

        // Assert
        assertEquals(id, library.id());
        assertEquals("My Library", library.getLibraryName());
        assertEquals(userIdDouble, library.getUserId());
    }

    @Test
    void shouldThrowException_whenLibraryIdIsNull() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Library(null, "My Library", userIdDouble)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowException_whenLibraryNameIsInvalid_inSecondConstructor(String name) {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");
        LibraryId id = new LibraryId("fixed-id");

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Library(id, name, userIdDouble)
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
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act
        Library library = new Library("My Library", userIdDouble);

        // Assert
        assertEquals("My Library", library.getLibraryName());
    }

    // -------------------------------------------------------------------------
    //  id()
    // -------------------------------------------------------------------------

    @Test
    void givenValidLibraryThenReturnsLibraryId() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act
        Library library = new Library("My Library", userIdDouble);

        // Assert
        assertInstanceOf(LibraryId.class, library.id());
    }


    // -------------------------------------------------------------------------
    // equals()
    // -------------------------------------------------------------------------

    @Test
    void shouldBeEqual_whenComparedToItself() {
        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");
        // Act
        Library library = new Library("My Library", userIdDouble);
        // Assert
        assertEquals(library, library);
    }

    @Test
    void shouldNotBeEqual_whenComparedToNull() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act
        Library library = new Library("My Library", userIdDouble);

        // Assert
        assertNotEquals(null, library);
    }

    @Test
    void shouldNotBeEqual_whenComparedToDifferentClass() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act
        Library library = new Library("My Library", userIdDouble);

        // Assert
        assertNotEquals("not a library", library);
    }

    @Test
    void shouldNotBeEquals_whenComparedDifferentId() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act
        Library library1 = new Library("My Library", userIdDouble);
        Library library2 = new Library("My Library", userIdDouble);

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
        UserId userIdDouble = createDoubleUserId("Nome1");

        // Act
        Library l1 = new Library("Library A", userIdDouble);
        Library l2 = new Library("Library B", userIdDouble);

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
        UserId userIdDouble = createDoubleUserId("Nome1");
        Library library = new Library("My Library", userIdDouble);

        // Act
        boolean result = library.sameAs(library);

        // Assert
        assertTrue(result);
    }

    @Test
    void givenTwoLibrariesWithSameNameAndSameUserWhenSameAsThenReturnsTrue() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");
        Library first = new Library("Library A", userIdDouble);
        Library second = new Library("Library A", userIdDouble);

        // Act
        boolean result = first.sameAs(second);

        // Assert
        assertTrue(result);
    }

    @Test
    void givenDifferentNamesWhenSameAsThenReturnsFalse() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");
        Library first = new Library("Library A", userIdDouble);
        Library second = new Library("Library B", userIdDouble);

        // Act
        boolean result = first.sameAs(second);

        // Assert
        assertFalse(result);
    }

    @Test
    void givenSameNameButDifferentUserWhenSameAsThenReturnsFalse() {

        // Arrange
        UserId userIdDouble1 = createDoubleUserId("Nome1");
        UserId userIdDouble2 = createDoubleUserId("Nome2");
        Library first = new Library("Library A", userIdDouble1);
        Library second = new Library("Library A", userIdDouble2);

        // Act
        boolean result = first.sameAs(second);

        // Assert
        assertFalse(result);
    }

    @Test
    void givenNullWhenSameAsThenReturnsFalse() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");
        Library library = new Library("My Library", userIdDouble);

        // Act
        boolean result = library.sameAs(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void givenDifferentTypeWhenSameAsThenReturnsFalse() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");
        Library library = new Library("My Library", userIdDouble);

        // Act
        boolean result = library.sameAs(new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnTrue_whenNameAndUserAreEqualEvenIfIdsAreDifferent() {

        // Arrange
        UserId userIdDouble = createDoubleUserId("Nome1");
        Library first = new Library("Library A", userIdDouble);
        Library second = new Library("Library A", userIdDouble);

        // Act
        boolean result = first.sameAs(second);

        // Assert
        assertTrue(result);
        assertNotEquals(first.id(), second.id());
    }


    // -------------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------------

    private UserId createDoubleUserId(String name) {
        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.getUserId()).thenReturn(name);
        return userIdDouble;
    }
}