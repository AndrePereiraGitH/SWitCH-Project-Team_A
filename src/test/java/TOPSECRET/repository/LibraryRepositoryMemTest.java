package TOPSECRET.repository;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryRepositoryMemTest {

    private LibraryFactory libraryFactoryDouble;

    @BeforeEach
    void setup() {
        libraryFactoryDouble = mock(LibraryFactory.class);
    }


    // ============ Constructor Tests ============

    @Test
    void shouldThrowAnExceptionWhenFactoryIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryRepositoryMem(null));
    }


    // ============ getLibraryOfUser Tests ============

    @Test
    void ensureGetLibraryOfUserSuccessfully() throws InstanceNotFoundException, InstanceAlreadyExistsException {
        // Arrange
        String username = "john_doe";
        String libraryName = "lib1";

        User userDouble = createMockUser(username);
        LibraryFactory factoryDouble = createLibraryFactory(libraryName, userDouble);
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(factoryDouble);

        libraryRepo.createAndAdd(libraryName, userDouble);

        // Act
        Library result = libraryRepo.getLibraryOfUser(username);

        // Assert

        assertEquals(libraryName, result.getLibraryName());
    }

    @Test
    void throwExceptionWhenUsernameIsNull() {
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(libraryFactoryDouble);

        assertThrows(NullPointerException.class,
                () -> libraryRepo.getLibraryOfUser(null));
    }

    @Test
    void throwExceptionWhenUsernameIsEmpty() {
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(libraryFactoryDouble);

        assertThrows(IllegalArgumentException.class,
                () -> libraryRepo.getLibraryOfUser(""));
    }

    @Test
    void throwExceptionWhenLibraryIsNotFound() {
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(libraryFactoryDouble);

        assertThrows(InstanceNotFoundException.class,
                () -> libraryRepo.getLibraryOfUser("ghost_user"));
    }


    // ============ createAndAdd Tests ============

    @Test
    void shouldCreateAndAddLibrarySuccessfully() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        String username = "username1";

        User userDouble = createMockUser(username);
        Library libraryDouble = createMockLibrary(libraryName, userDouble);

        // ← stub the factory so it returns the mock library
        when(libraryFactoryDouble.create(libraryName, userDouble)).thenReturn(libraryDouble);

        ILibraryRepository libraryRepo = new LibraryRepositoryMem(libraryFactoryDouble);

        // Act
        Library result = libraryRepo.createAndAdd(libraryName, userDouble);

        // Assert
        assertNotNull(result);
        assertEquals(libraryName, result.getLibraryName());
        assertEquals(username, result.getUser().getUserName());
    }

    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsNull() {
        // Arrange
        User userDouble = createMockUser("User1");
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(libraryFactoryDouble);

        assertThrows(IllegalArgumentException.class,
                () -> libraryRepo.createAndAdd(null, userDouble));
    }

    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsEmpty() {
        // Arrange
        User userDouble = createMockUser("User1");
        ILibraryRepository libraryRepositoryMem = new LibraryRepositoryMem(libraryFactoryDouble);

        assertThrows(IllegalArgumentException.class,
                () -> libraryRepositoryMem.createAndAdd("", userDouble));
    }

    @Test
    void shouldReturnAnExceptionWhenTheUserIsNull() {
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(libraryFactoryDouble);

        assertThrows(IllegalArgumentException.class,
                () -> libraryRepo.createAndAdd("mylib", null));
    }

    @Test
    void shouldReturnAnExceptionWhenTheUserAlreadyHasALibrary() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        String username = "User1";

        User userDouble = createMockUser(username);
        LibraryFactory factoryDouble = createLibraryFactory(libraryName, userDouble);
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(factoryDouble);

        // ← first call populates the repo
        libraryRepo.createAndAdd(libraryName, userDouble);

        // Act & Assert — second call should throw because user already has a library
        assertThrows(InstanceAlreadyExistsException.class,
                () -> libraryRepo.createAndAdd(libraryName, userDouble));
    }


    // ============ verifyIfUserHasALibrary Tests ============

    @Test
    void returnTrueWhenUserAlreadyHasALibrary() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        String username = "User1";

        User userDouble = createMockUser(username);
        LibraryFactory factoryDouble = createLibraryFactory(libraryName, userDouble);
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(factoryDouble);

        // ← actually add the library first
        libraryRepo.createAndAdd(libraryName, userDouble);

        // Act
        boolean result = libraryRepo.verifyIfUserHasALibrary(userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void returnFalseWhenUserDoesNotHaveALibrary() {
        // Arrange
        User userDouble = createMockUser("User1");
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(libraryFactoryDouble);

        // Act
        boolean result = libraryRepo.verifyIfUserHasALibrary(userDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void verifyIfUserHasALibrary_shouldThrowException_whenUserIsNull() {
        //Arrange
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(libraryFactoryDouble);

        //Act & Assert
        assertThrows(NullPointerException.class,
                () -> libraryRepo.getLibraryOfUser(null));
    }


    // ============ getAllPublicationsFromUserLibrary Tests ============

    @Test
    void shouldReturnAllPublicationsFromUserLibrary() throws InstanceNotFoundException, InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "MyLib1";
        String username = "User1";

        PublicationSample pub1 = mock(PublicationSample.class);
        PublicationSample pub2 = mock(PublicationSample.class);
        PublicationSample pub3 = mock(PublicationSample.class);
        List<PublicationSample> expectedPublications = List.of(pub1, pub2, pub3);

        User userDouble = createMockUser(username);
        Library libraryDouble = createMockLibrary(libraryName, userDouble);
        when(libraryDouble.getAllPublications()).thenReturn(expectedPublications);

        // ← stub factory to return our library mock
        when(libraryFactoryDouble.create(libraryName, userDouble)).thenReturn(libraryDouble);
        ILibraryRepository libraryRepo = new LibraryRepositoryMem(libraryFactoryDouble);

        // ← actually populate the repo
        libraryRepo.createAndAdd(libraryName, userDouble);

        // Act
        List<PublicationSample> realPublications =
                libraryRepo.getAllPublicationsFromUserLibrary(username);

        // Assert
        assertEquals(expectedPublications, realPublications);
    }


    // ============ Helper Methods ============

    private Library createMockLibrary(String libraryName, User owner) {
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.getLibraryName()).thenReturn(libraryName);
        when(libraryDouble.getUser()).thenReturn(owner);
        return libraryDouble;
    }

    private User createMockUser(String name) {
        User userDouble = mock(User.class);
        when(userDouble.getUserName()).thenReturn(name);
        return userDouble;
    }

    private LibraryFactory createLibraryFactory(String libraryName, User owner) {
        Library libraryDouble = createMockLibrary(libraryName, owner);
        LibraryFactory factory = mock(LibraryFactory.class);
        when(factory.create(libraryName, owner)).thenReturn(libraryDouble);
        return factory;
    }
}