package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
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
    void givenNullFactory_whenConstructing_thenThrowsIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new LibraryRepositoryMem(null)
        );
    }

    @Test
    void givenValidFactory_whenConstructing_thenCreatesSuccessfully() {
        LibraryFactory factoryDouble = mock(LibraryFactory.class);

        assertDoesNotThrow(() -> new LibraryRepositoryMem(factoryDouble));
    }

    // ============ save Tests ============

    @Test
    void shouldSaveLibrarySuccessfully() throws InstanceAlreadyExistsException {
        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

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

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        Library result = repo.save(libraryDouble);

        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNullLibrary_whenSave_thenThrowsIllegalArgumentException() {
        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> repo.save(null)
        );
    }

    // ============ verifyIfUserHasALibrary Tests ============

    @Test
    void givenUserWithLibrary_whenVerifyIfUserHasALibrary_thenReturnsTrue()
            throws InstanceAlreadyExistsException {

        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        repo.save(libraryDouble);

        assertTrue(repo.verifyIfUserHasALibrary(userDouble));
    }

    @Test
    void givenUserWithoutLibrary_whenVerifyIfUserHasALibrary_thenReturnsFalse() {
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        assertFalse(repo.verifyIfUserHasALibrary(userDouble));
    }

    @Test
    void givenNullUserId_whenVerifyIfUserHasALibrary_thenThrowsIllegalArgumentException() {
        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> repo.verifyIfUserHasALibrary(null)
        );
    }

    @Test
    void givenOneUserWithLibrary_whenVerifyIfUserHasALibrary_thenReturnsFalseForOtherUser()
            throws InstanceAlreadyExistsException {

        UserId userDouble1 = mock(UserId.class);
        when(userDouble1.getUsername()).thenReturn("user-123");

        UserId userDouble2 = mock(UserId.class);
        when(userDouble2.getUsername()).thenReturn("user-456");

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.toString()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        repo.save(libraryDouble);

        assertTrue(repo.verifyIfUserHasALibrary(userDouble1));
        assertFalse(repo.verifyIfUserHasALibrary(userDouble2));
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

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        repo.save(libraryDouble);

        Library result = repo.getLibraryByUserId(userDouble);

        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNullUserId_whenGetLibraryOfUser_thenThrowsNullPointerException() {
        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        assertThrows(
                NullPointerException.class,
                () -> repo.getLibraryByUserId(null)
        );
    }

    @Test
    void givenNonExistingUser_whenGetLibraryOfUser_thenThrowsInstanceNotFoundException() {
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-999");

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        assertThrows(
                InstanceNotFoundException.class,
                () -> repo.getLibraryByUserId(userDouble)
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

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        repo.save(libraryDouble1);
        repo.save(libraryDouble2);

        assertEquals(libraryDouble1, repo.getLibraryByUserId(userDouble1));
        assertEquals(libraryDouble2, repo.getLibraryByUserId(userDouble2));
    }
}