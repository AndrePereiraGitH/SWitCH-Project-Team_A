package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryDMTest {

    LibraryId libraryId = new LibraryId("library-1");
    UserId userId = new UserId("user-1");

    @Test
    void constructor_shouldCreateLibraryDMWithValidData() {
        // Act
        LibraryDM libraryDM = new LibraryDM(libraryId, "My Library", userId);

        // Assert
        assertNotNull(libraryDM);
        assertEquals(libraryId, libraryDM.getId());
        assertEquals("My Library", libraryDM.getLibraryName());
        assertEquals(userId, libraryDM.getUserId());
    }

    @Test
    void constructor_shouldThrowException_whenLibraryIdIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new LibraryDM(null, "My Library", userId)
        );

        // Assert
        assertEquals("LibraryId cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenLibraryNameIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new LibraryDM(libraryId, null, userId)
        );

        // Assert
        assertEquals("Library name cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenUserIdIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new LibraryDM(libraryId, "My Library", null)
        );

        // Assert
        assertEquals("UserId cannot be null", exception.getMessage());
    }

    @Test
    void protectedConstructor_shouldCreateEmptyInstance() {
        // Act
        LibraryDM libraryDM = new LibraryDM();

        // Assert
        assertNotNull(libraryDM);
        assertNull(libraryDM.getId());
        assertNull(libraryDM.getLibraryName());
        assertNull(libraryDM.getUserId());
    }

    @Test
    void getId_shouldReturnLibraryId() {
        // Arrange
        LibraryDM libraryDM = new LibraryDM(libraryId, "My Library", userId);

        // Act
        LibraryId result = libraryDM.getId();

        // Assert
        assertEquals(libraryId, result);
    }

    @Test
    void getLibraryName_shouldReturnLibraryName() {
        // Arrange
        LibraryDM libraryDM = new LibraryDM(libraryId, "My Library", userId);

        // Act
        String result = libraryDM.getLibraryName();

        // Assert
        assertEquals("My Library", result);
    }

    @Test
    void getUserId_shouldReturnUserId() {
        // Arrange
        LibraryDM libraryDM = new LibraryDM(libraryId, "My Library", userId);

        // Act
        UserId result = libraryDM.getUserId();

        // Assert
        assertEquals(userId, result);
    }
}