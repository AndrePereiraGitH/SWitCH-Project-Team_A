package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryRepositoryMemTest {

    // ============ Constructor Tests ============

    @Test
    void whenConstructing_thenCreatesSuccessfully() {
        // Arrange, Act & Assert
        assertDoesNotThrow(LibraryRepositoryMem::new);
    }

    // ============ save Tests ============

    @Test
    void shouldSaveLibrarySuccessfully() throws InstanceAlreadyExistsException {
        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        // Act
        Library result = repo.save(libraryDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void givenValidLibrary_whenSave_thenReturnsSameLibrary()
            throws InstanceAlreadyExistsException {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        // Act
        Library result = repo.save(libraryDouble);

        // Assert
        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNullLibrary_whenSave_thenThrowsIllegalArgumentException() {
        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.save(null)
        );
    }

    // ============ verifyIfUserHasALibrary Tests ============

    @Test
    void givenUserWithLibrary_whenExistsByUserId_thenReturnsTrue()
            throws InstanceAlreadyExistsException {

        // Arrange
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUserId()).thenReturn("user-123");

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        ILibraryRepository repo = new LibraryRepositoryMem();

        repo.save(libraryDouble);

        // Act
        boolean result = repo.existsByUserId(userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void givenUserWithoutLibrary_whenExistsByUserId_thenReturnsFalse() {
        // Arrange
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUserId()).thenReturn("user-123");

        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act
        boolean result = repo.existsByUserId(userDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void givenNullUserId_whenExistsByUserId_thenThrowsIllegalArgumentException() {
        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.existsByUserId(null)
        );
    }

    @Test
    void givenOneUserWithLibrary_whenExistsByUserHasALibrary_thenReturnsFalseForOtherUser()
            throws InstanceAlreadyExistsException {

        // Arrange
        UserId userDouble1 = mock(UserId.class);
        when(userDouble1.getUserId()).thenReturn("user-123");

        UserId userDouble2 = mock(UserId.class);
        when(userDouble2.getUserId()).thenReturn("user-456");

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        ILibraryRepository repo = new LibraryRepositoryMem();

        repo.save(libraryDouble);

        // Act
        boolean resultForUser1 = repo.existsByUserId(userDouble1);
        boolean resultForUser2 = repo.existsByUserId(userDouble2);

        // Assert
        assertTrue(resultForUser1);
        assertFalse(resultForUser2);
    }

    // ============ getLibraryByUserId Tests ============

    @Test
    void givenExistingUser_whenGetLibraryOfUser_thenReturnsCorrectLibrary()
            throws InstanceAlreadyExistsException, InstanceNotFoundException {

        // Arrange
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUserId()).thenReturn("user-123");

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        ILibraryRepository repo = new LibraryRepositoryMem();

        repo.save(libraryDouble);

        // Act
        Library result = repo.getByUserId(userDouble);

        // Assert
        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNullUserId_whenGetLibraryOfUser_thenThrowsNullPointerException() {
        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> repo.getByUserId(null)
        );
    }

    @Test
    void givenNonExistingUser_whenGetLibraryOfUser_thenThrowsInstanceNotFoundException() {
        // Arrange
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUserId()).thenReturn("user-999");

        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act & Assert
        assertThrows(
                InstanceNotFoundException.class,
                () -> repo.getByUserId(userDouble)
        );
    }

    @Test
    void givenTwoDifferentUsers_whenGetLibraryOfUser_thenReturnsCorrectLibraryPerUser()
            throws InstanceAlreadyExistsException, InstanceNotFoundException {

        // Arrange
        UserId userDouble1 = mock(UserId.class);
        when(userDouble1.getUserId()).thenReturn("user-123");

        UserId userDouble2 = mock(UserId.class);
        when(userDouble2.getUserId()).thenReturn("user-456");

        LibraryId libraryIdDouble1 = mock(LibraryId.class);
        when(libraryIdDouble1.toString()).thenReturn("user-123");

        LibraryId libraryIdDouble2 = mock(LibraryId.class);
        when(libraryIdDouble2.toString()).thenReturn("user-456");

        Library libraryDouble1 = mock(Library.class);
        when(libraryDouble1.id()).thenReturn(libraryIdDouble1);

        Library libraryDouble2 = mock(Library.class);
        when(libraryDouble2.id()).thenReturn(libraryIdDouble2);

        ILibraryRepository repo = new LibraryRepositoryMem();

        repo.save(libraryDouble1);
        repo.save(libraryDouble2);

        // Act
        Library result1 = repo.getByUserId(userDouble1);
        Library result2 = repo.getByUserId(userDouble2);

        // Assert
        assertEquals(libraryDouble1, result1);
        assertEquals(libraryDouble2, result2);
    }
    // ============ findAll Tests ============

    @Test
    void givenEmptyRepository_whenFindAll_thenReturnsEmptyList() {
        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act & Assert
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    void givenOneLibrary_whenFindAll_thenReturnsOneLibrary()
            throws InstanceAlreadyExistsException {

        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("lib-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(libraryDouble);

        // Act & Assert
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void givenTwoLibraries_whenFindAll_thenReturnsBoth()
            throws InstanceAlreadyExistsException {

        // Arrange
        LibraryId libraryIdDouble1 = mock(LibraryId.class);
        when(libraryIdDouble1.toString()).thenReturn("lib-123");

        LibraryId libraryIdDouble2 = mock(LibraryId.class);
        when(libraryIdDouble2.toString()).thenReturn("lib-456");

        Library libraryDouble1 = mock(Library.class);
        when(libraryDouble1.id()).thenReturn(libraryIdDouble1);

        Library libraryDouble2 = mock(Library.class);
        when(libraryDouble2.id()).thenReturn(libraryIdDouble2);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(libraryDouble1);
        repo.save(libraryDouble2);

        // Act & Assert
        assertEquals(2, repo.findAll().size());
    }

// ============ findById Tests ============

    @Test
    void givenExistingLibrary_whenFindById_thenReturnsLibrary()
            throws InstanceAlreadyExistsException {

        // Arrange
        LibraryId libraryId = new LibraryId("lib-123"); // real object so equals() works

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryId);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(libraryDouble);

        // Act
        Optional<Library> result = repo.findById(libraryId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(libraryDouble, result.get());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnsEmpty() {

        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act
        Optional<Library> result = repo.findById(libraryIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNullId_whenFindById_thenThrowsIllegalArgumentException() {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> repo.findById(null)
        );
    }

    // ============ findByUserId Tests ============

    @Test
    void givenExistingUserId_whenFindByUserId_thenReturnsLibrary()
            throws InstanceAlreadyExistsException {

        // Arrange
        UserId userIdDouble = mock(UserId.class);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("lib-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.getUserId()).thenReturn(userIdDouble);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(libraryDouble);

        // Act
        Optional<Library> result = repo.findByUserId(userIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(libraryDouble, result.get());
    }

    @Test
    void givenNonExistingUserId_whenFindByUserId_thenReturnsEmpty()
            throws InstanceAlreadyExistsException {

        // Arrange
        UserId existingUserIdDouble = mock(UserId.class);
        UserId nonExistingUserIdDouble = mock(UserId.class);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("lib-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.getUserId()).thenReturn(existingUserIdDouble);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        ILibraryRepository repo = new LibraryRepositoryMem();
        repo.save(libraryDouble);

        // Act
        Optional<Library> result = repo.findByUserId(nonExistingUserIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNullUserId_whenFindByUserId_thenThrowsIllegalArgumentException() {

        // Arrange
        ILibraryRepository repo = new LibraryRepositoryMem();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repo.findByUserId(null)
        );

        assertEquals("UserId cannot be null", exception.getMessage());
    }
}