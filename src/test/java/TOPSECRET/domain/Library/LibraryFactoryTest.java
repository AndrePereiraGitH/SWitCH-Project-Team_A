package TOPSECRET.domain.Library;

import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LibraryFactoryTest {

    // -------- create(String, UserId) Tests --------

    @Test
    void create_shouldCreateLibraryWithGeneratedId() {
        // Arrange
        LibraryFactory factory = new LibraryFactory();
        UserId userIdDouble = mock(UserId.class);

        // Act
        Library result = factory.create("lib1", userIdDouble);

        // Assert
        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals("lib1", result.getLibraryName());
        assertSame(userIdDouble, result.getUserId());
    }

    @Test
    void create_shouldThrowException_whenLibraryNameIsNull() {
        // Arrange
        LibraryFactory factory = new LibraryFactory();
        UserId userIdDouble = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, userIdDouble));
    }

    @Test
    void create_shouldThrowException_whenLibraryNameIsBlank() {
        // Arrange
        LibraryFactory factory = new LibraryFactory();
        UserId userIdDouble = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("   ", userIdDouble));
    }

    @Test
    void create_shouldThrowException_whenUserIdIsNull() {
        // Arrange
        LibraryFactory factory = new LibraryFactory();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("lib1", null));
    }

    // -------- create(LibraryId, String, UserId) Tests --------

    @Test
    void createWithId_shouldCreateLibraryWithGivenId() {
        // Arrange
        LibraryFactory factory = new LibraryFactory();
        LibraryId libraryIdDouble = mock(LibraryId.class);
        UserId userIdDouble = mock(UserId.class);

        // Act
        Library result = factory.create(libraryIdDouble, "lib1", userIdDouble);

        // Assert
        assertNotNull(result);
        assertSame(libraryIdDouble, result.id());
        assertEquals("lib1", result.getLibraryName());
        assertSame(userIdDouble, result.getUserId());
    }

    @Test
    void createWithId_shouldThrowException_whenLibraryIdIsNull() {
        // Arrange
        LibraryFactory factory = new LibraryFactory();
        UserId userIdDouble = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, "lib1", userIdDouble));
    }

    @Test
    void createWithId_shouldThrowException_whenLibraryNameIsNull() {
        // Arrange
        LibraryFactory factory = new LibraryFactory();
        LibraryId libraryIdDouble = mock(LibraryId.class);
        UserId userIdDouble = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(libraryIdDouble, null, userIdDouble));
    }

    @Test
    void createWithId_shouldThrowException_whenLibraryNameIsBlank() {
        // Arrange
        LibraryFactory factory = new LibraryFactory();
        LibraryId libraryIdDouble = mock(LibraryId.class);
        UserId userIdDouble = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(libraryIdDouble, "   ", userIdDouble));
    }

    @Test
    void createWithId_shouldThrowException_whenUserIdIsNull() {
        // Arrange
        LibraryFactory factory = new LibraryFactory();
        LibraryId libraryIdDouble = mock(LibraryId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(libraryIdDouble, "lib1", null));
    }
}