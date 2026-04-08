package TOPSECRET.repository;


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

    // ============ Constructor============

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

    // ============ createAndAdd Tests ============

    @Test
    void shouldCreateAndAddLibrarySuccessfully() throws InstanceAlreadyExistsException {

        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.getLibraryName()).thenReturn("My Library");

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        when(factoryDouble.create("My Library", userDouble)).thenReturn(libraryDouble);

        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);
        Library result = repo.createAndAdd("My Library", userDouble);

        assertNotNull(result);
    }

    @Test
    void givenValidArgumentsWhenCreateAndAddThenReturnsLibraryWithCorrectName() throws InstanceAlreadyExistsException {
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.getLibraryName()).thenReturn("My Library");

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        when(factoryDouble.create("My Library", userDouble)).thenReturn(libraryDouble);

        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);
        Library result = repo.createAndAdd("My Library", userDouble);

        assertEquals("My Library", result.getLibraryName());
    }

    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsNull() {
        // Arrange
        UserId userDouble = mock(UserId.class);
        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        assertThrows(IllegalArgumentException.class,
                () -> repo.createAndAdd(null, userDouble));
    }

    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsEmpty() {
        // Arrange
        UserId userDouble = mock(UserId.class);
        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        assertThrows(
                IllegalArgumentException.class, () -> repo.createAndAdd("", userDouble));
    }

    @Test
    void shouldReturnAnExceptionWhenTheUserIsNull() {
        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> repo.createAndAdd("My Library", null));
    }

    @Test
    void shouldReturnAnExceptionWhenTheUserAlreadyHasALibrary() throws InstanceAlreadyExistsException {
        // Arrange
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        when(factoryDouble.create("My Library", userDouble)).thenReturn(libraryDouble);

        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);
        repo.createAndAdd("My Library", userDouble);

        assertThrows(
                InstanceAlreadyExistsException.class, () -> repo.createAndAdd("My Library", userDouble));
    }


    // ============ verifyIfUserHasALibrary Tests ============

    @Test
    void givenUserWithLibrary_whenVerifyIfUserHasALibrary_thenReturnsTrue()
            throws InstanceAlreadyExistsException {
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        when(factoryDouble.create("My Library", userDouble)).thenReturn(libraryDouble);

        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);
        repo.createAndAdd("My Library", userDouble);

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

        Library libraryDouble = mock(Library.class);

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        when(factoryDouble.create("My Library", userDouble1)).thenReturn(libraryDouble);

        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);
        repo.createAndAdd("My Library", userDouble1);

        assertTrue(repo.verifyIfUserHasALibrary(userDouble1));
        assertFalse(repo.verifyIfUserHasALibrary(userDouble2));
    }


    // ============ getAllPublicationsFromUserLibrary Tests ============
    @Test
    void givenExistingUser_whenGetLibraryOfUser_thenReturnsCorrectLibrary()
            throws InstanceAlreadyExistsException, InstanceNotFoundException {
        UserId userDouble = mock(UserId.class);
        when(userDouble.getUsername()).thenReturn("user-123");

        Library libraryDouble = mock(Library.class);

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        when(factoryDouble.create("My Library", userDouble)).thenReturn(libraryDouble);

        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);
        repo.createAndAdd("My Library", userDouble);

        Library result = repo.getLibraryOfUser(userDouble);
        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNullUserId_whenGetLibraryOfUser_thenThrowsNullPointerException() {
        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);

        assertThrows(
                NullPointerException.class,
                () -> repo.getLibraryOfUser(null)
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
                () -> repo.getLibraryOfUser(userDouble)
        );
    }

    @Test
    void givenTwoDifferentUsers_whenGetLibraryOfUser_thenReturnsCorrectLibraryPerUser()
            throws InstanceAlreadyExistsException, InstanceNotFoundException {
        UserId userDouble1 = mock(UserId.class);
        when(userDouble1.getUsername()).thenReturn("user-123");

        UserId userDouble2 = mock(UserId.class);
        when(userDouble2.getUsername()).thenReturn("user-456");

        Library libraryDouble1 = mock(Library.class);
        Library libraryDouble2 = mock(Library.class);

        LibraryFactory factoryDouble = mock(LibraryFactory.class);
        when(factoryDouble.create("Library A", userDouble1)).thenReturn(libraryDouble1);
        when(factoryDouble.create("Library B", userDouble2)).thenReturn(libraryDouble2);

        LibraryRepositoryMem repo = new LibraryRepositoryMem(factoryDouble);
        repo.createAndAdd("Library A", userDouble1);
        repo.createAndAdd("Library B", userDouble2);

        assertEquals(libraryDouble1, repo.getLibraryOfUser(userDouble1));
        assertEquals(libraryDouble2, repo.getLibraryOfUser(userDouble2));
    }


    // ============ Helper Methods ============

    private Library createMockLibrary(String libraryName, LibraryId libraryId) {
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.getLibraryName()).thenReturn(libraryName);
        when(libraryDouble.id()).thenReturn(libraryId);
        return libraryDouble;
    }
}