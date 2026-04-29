package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryRepositoryMemTest {

    // ============ Constructor Tests ============

    @Test
    void whenConstructing_thenCreatesSuccessfully() {
        assertDoesNotThrow(LibraryRepositoryMem::new);
    }

    // ============ save Tests ============

    @Test
    void shouldSaveLibrarySuccessfully() throws InstanceAlreadyExistsException {
         ILibraryRepository repo = new LibraryRepositoryMem(  );

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        Library result = repo.save(libraryDouble);

        assertNotNull(result);
    }

    @Test
    void givenValidLibrary_whenSave_thenReturnsSameLibrary()
            throws InstanceAlreadyExistsException {

        ILibraryRepository repo = new LibraryRepositoryMem(  );

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        Library result = repo.save(libraryDouble);

        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNullLibrary_whenSave_thenThrowsIllegalArgumentException() {
        ILibraryRepository repo = new LibraryRepositoryMem(  );

        assertThrows(
                IllegalArgumentException.class,
                () -> repo.save(null)
        );
    }

    // ============ verifyIfUserHasALibrary Tests ============

    @Test
    void givenUserWithLibrary_whenExistsByUserId_thenReturnsTrue()
            throws InstanceAlreadyExistsException {

        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        ILibraryRepository repo = new LibraryRepositoryMem(  );

        repo.save(libraryDouble);

        assertTrue(repo.existsByUserId(userDouble));
    }

    @Test
    void givenUserWithoutLibrary_whenExistsByUserId_thenReturnsFalse() {
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        ILibraryRepository repo = new LibraryRepositoryMem(  );

        assertFalse(repo.existsByUserId(userDouble));
    }

    @Test
    void givenNullUserId_whenExistsByUserId_thenThrowsIllegalArgumentException() {
        ILibraryRepository repo = new LibraryRepositoryMem(  );

        assertThrows(
                IllegalArgumentException.class,
                () -> repo.existsByUserId(null)
        );
    }

    @Test
    void givenOneUserWithLibrary_whenExistsByUserHasALibrary_thenReturnsFalseForOtherUser()
            throws InstanceAlreadyExistsException {

        UserId userDouble1 = mock(UserId.class);
        when(userDouble1.getUsername()).thenReturn("user-123");

        UserId userDouble2 = mock(UserId.class);
        when(userDouble2.getUsername()).thenReturn("user-456");

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        ILibraryRepository repo = new LibraryRepositoryMem(  );

        repo.save(libraryDouble);

        assertTrue(repo.existsByUserId(userDouble1));
        assertFalse(repo.existsByUserId(userDouble2));
    }

    // ============ getLibraryByUserId Tests ============

    @Test
    void givenExistingUser_whenGetLibraryOfUser_thenReturnsCorrectLibrary()
            throws InstanceAlreadyExistsException, InstanceNotFoundException {

        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        ILibraryRepository repo = new LibraryRepositoryMem(  );

        repo.save(libraryDouble);

        Library result = repo.getByUserId(userDouble);

        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNullUserId_whenGetLibraryOfUser_thenThrowsNullPointerException() {
        ILibraryRepository repo = new LibraryRepositoryMem(  );

        assertThrows(
                NullPointerException.class,
                () -> repo.getByUserId(null)
        );
    }

    @Test
    void givenNonExistingUser_whenGetLibraryOfUser_thenThrowsInstanceNotFoundException() {
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-999");

        ILibraryRepository repo = new LibraryRepositoryMem(  );

        assertThrows(
                InstanceNotFoundException.class,
                () -> repo.getByUserId(userDouble)
        );
    }

    @Test
    void givenTwoDifferentUsers_whenGetLibraryOfUser_thenReturnsCorrectLibraryPerUser()
            throws InstanceAlreadyExistsException, InstanceNotFoundException {

        UserId userDouble1 = mock(UserId.class);
        when(userDouble1.getUsername()).thenReturn("user-123");

        UserId userDouble2 = mock(UserId.class);
        when(userDouble2.getUsername()).thenReturn("user-456");

        LibraryId libraryIdDouble1 = mock(LibraryId.class);
        when(libraryIdDouble1.toString()).thenReturn("user-123");

        LibraryId libraryIdDouble2 = mock(LibraryId.class);
        when(libraryIdDouble2.toString()).thenReturn("user-456");

        Library libraryDouble1 = mock(Library.class);
        when(libraryDouble1.id()).thenReturn(libraryIdDouble1);

        Library libraryDouble2 = mock(Library.class);
        when(libraryDouble2.id()).thenReturn(libraryIdDouble2);

        ILibraryRepository repo = new LibraryRepositoryMem(  );

        repo.save(libraryDouble1);
        repo.save(libraryDouble2);

        assertEquals(libraryDouble1, repo.getByUserId(userDouble1));
        assertEquals(libraryDouble2, repo.getByUserId(userDouble2));
    }
}