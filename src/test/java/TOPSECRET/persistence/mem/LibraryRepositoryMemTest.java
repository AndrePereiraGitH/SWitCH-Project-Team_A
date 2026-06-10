package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryRepositoryMemTest {

    // ============ Constructor Tests ============

    @Test
    void whenConstructing_thenCreatesSuccessfully() {
        // Arrange + Act + Assert
        assertDoesNotThrow(LibraryRepositoryMem::new);
    }

    // ============ save Tests ============

    @Test
    void shouldSaveLibrarySuccessfully() throws InstanceAlreadyExistsException {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        LibraryId libraryId = mock(LibraryId.class);
        when(libraryId.toString()).thenReturn("user-123");

        Library library = mock(Library.class);
        when(library.id()).thenReturn(libraryId);

        // Act
        Library result = repo.save(library);

        // Assert
        assertNotNull(result);
    }

    @Test
    void givenValidLibrary_whenSave_thenReturnsSameLibrary()
            throws InstanceAlreadyExistsException {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        LibraryId libraryId = mock(LibraryId.class);
        when(libraryId.toString()).thenReturn("user-123");

        Library library = mock(Library.class);
        when(library.id()).thenReturn(libraryId);

        // Act
        Library result = repo.save(library);

        // Assert
        assertEquals(library, result);
    }

    @Test
    void givenNullLibrary_whenSave_thenThrowsIllegalArgumentException() {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.save(null)
        );
    }

    // ============ existsByUserId Tests ============

    @Test
    void givenUserWithLibrary_whenExistsByUserId_thenReturnsTrue()
            throws InstanceAlreadyExistsException {

        // Arrange
        UserId user = mock(UserId.class);
        when(user.getUserId()).thenReturn("user-123");

        LibraryId libraryId = mock(LibraryId.class);
        when(libraryId.toString()).thenReturn("user-123");

        Library library = mock(Library.class);
        when(library.id()).thenReturn(libraryId);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(library);

        // Act
        boolean result = repo.existsByUserId(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void givenUserWithoutLibrary_whenExistsByUserId_thenReturnsFalse() {

        // Arrange
        UserId user = mock(UserId.class);
        when(user.getUserId()).thenReturn("user-123");

        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act
        boolean result = repo.existsByUserId(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void givenNullUserId_whenExistsByUserId_thenThrowsIllegalArgumentException() {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.existsByUserId(null)
        );
    }

    // ============ findByUserId Tests ============

    @Test
    void givenExistingUser_whenFindByUserId_thenReturnsLibrary()
            throws InstanceAlreadyExistsException {

        // Arrange
        UserId user = mock(UserId.class);
        when(user.getUserId()).thenReturn("user-123");

        LibraryId libraryId = mock(LibraryId.class);
        when(libraryId.toString()).thenReturn("user-123");

        Library library = mock(Library.class);
        when(library.getUserId()).thenReturn(user);
        when(library.id()).thenReturn(libraryId);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(library);

        // Act
        Optional<Library> result = repo.findByUserId(user);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(library, result.get());
    }

    @Test
    void givenNonExistingUser_whenFindByUserId_thenReturnsEmpty()
            throws InstanceAlreadyExistsException {

        // Arrange
        UserId existing = mock(UserId.class);
        UserId nonExisting = mock(UserId.class);

        LibraryId libraryId = mock(LibraryId.class);
        when(libraryId.toString()).thenReturn("user-123");

        Library library = mock(Library.class);
        when(library.getUserId()).thenReturn(existing);
        when(library.id()).thenReturn(libraryId);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(library);

        // Act
        Optional<Library> result = repo.findByUserId(nonExisting);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNullUserId_whenFindByUserId_thenThrowsIllegalArgumentException() {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repo.findByUserId(null)
        );

        assertEquals("UserId cannot be null", exception.getMessage());
    }

    // ============ findAll Tests ============

    @Test
    void givenEmptyRepository_whenFindAll_thenReturnsEmptyList() {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act + Assert
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    void givenOneLibrary_whenFindAll_thenReturnsOneLibrary()
            throws InstanceAlreadyExistsException {

        // Arrange
        LibraryId libraryId = mock(LibraryId.class);
        when(libraryId.toString()).thenReturn("lib-123");

        Library library = mock(Library.class);
        when(library.id()).thenReturn(libraryId);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(library);

        // Act
        int size = repo.findAll().size();

        // Assert
        assertEquals(1, size);
    }

    @Test
    void givenTwoLibraries_whenFindAll_thenReturnsBoth()
            throws InstanceAlreadyExistsException {

        // Arrange
        LibraryId libraryId1 = mock(LibraryId.class);
        when(libraryId1.toString()).thenReturn("lib-123");

        LibraryId libraryId2 = mock(LibraryId.class);
        when(libraryId2.toString()).thenReturn("lib-456");

        Library library1 = mock(Library.class);
        when(library1.id()).thenReturn(libraryId1);

        Library library2 = mock(Library.class);
        when(library2.id()).thenReturn(libraryId2);

        ILibraryRepository repo = new LibraryRepositoryMem();

        repo.save(library1);
        repo.save(library2);

        // Act
        int size = repo.findAll().size();

        // Assert
        assertEquals(2, size);
    }

    // ============ findById Tests ============

    @Test
    void givenExistingLibrary_whenFindById_thenReturnsLibrary()
            throws InstanceAlreadyExistsException {

        // Arrange
        LibraryId libraryId = new LibraryId("lib-123");

        Library library = mock(Library.class);
        when(library.id()).thenReturn(libraryId);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(library);

        // Act
        Optional<Library> result = repo.findById(libraryId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(library, result.get());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnsEmpty() {

        // Arrange
        LibraryId id = mock(LibraryId.class);

        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act
        Optional<Library> result = repo.findById(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNullId_whenFindById_thenThrowsIllegalArgumentException() {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.findById(null)
        );
    }
}