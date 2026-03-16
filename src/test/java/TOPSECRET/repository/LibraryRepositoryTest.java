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

class LibraryRepositoryTest {

    private LibraryFactory libraryFactoryDouble;

    @BeforeEach
    void setup() {
        libraryFactoryDouble = mock(LibraryFactory.class);
    }


    // ============ Constructor Tests ============

    @Test
    void shouldThrowAnExceptionWhenFactoryIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryRepository(null));
    }


    // ============ getLibraryOfUser Tests ============

    @Test
    void ensureGetLibraryOfUserSuccessfully() throws InstanceNotFoundException, InstanceAlreadyExistsException {
        // Arrange
        String username = "john_doe";
        String libraryName = "lib1";

        User userDouble = createMockUser(username);
        LibraryFactory factoryDouble = createLibraryFactory(libraryName, userDouble);
        LibraryRepository libraryRepository = new LibraryRepository(factoryDouble);

        libraryRepository.createAndAdd(libraryName, userDouble);

        // Act
        Library result = libraryRepository.getLibraryOfUser(username);

        // Assert
        assertEquals(libraryName, result.getLibraryName());
    }

    @Test
    void throwExceptionWhenUsernameIsNull() {
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        assertThrows(NullPointerException.class,
                () -> libraryRepository.getLibraryOfUser(null));
    }

    @Test
    void throwExceptionWhenUsernameIsEmpty() {
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.getLibraryOfUser(""));
    }

    @Test
    void throwExceptionWhenLibraryIsNotFound() {
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        assertThrows(InstanceNotFoundException.class,
                () -> libraryRepository.getLibraryOfUser("ghost_user"));
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

        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        // Act
        Library result = libraryRepository.createAndAdd(libraryName, userDouble);

        // Assert
        assertNotNull(result);
        assertEquals(libraryName, result.getLibraryName());
        assertEquals(username, result.getUser().getUserName());
    }

    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsNull() {
        // Arrange
        User userDouble = createMockUser("User1");
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.createAndAdd(null, userDouble));
    }

    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsEmpty() {
        // Arrange
        User userDouble = createMockUser("User1");
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.createAndAdd("", userDouble));
    }

    @Test
    void shouldReturnAnExceptionWhenTheUserIsNull() {
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.createAndAdd("mylib", null));
    }

    @Test
    void shouldReturnAnExceptionWhenTheUserAlreadyHasALibrary() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        String username = "User1";

        User userDouble = createMockUser(username);
        LibraryFactory factoryDouble = createLibraryFactory(libraryName, userDouble);
        LibraryRepository libraryRepository = new LibraryRepository(factoryDouble);

        // ← first call populates the repo
        libraryRepository.createAndAdd(libraryName, userDouble);

        // Act & Assert — second call should throw because user already has a library
        assertThrows(InstanceAlreadyExistsException.class,
                () -> libraryRepository.createAndAdd(libraryName, userDouble));
    }


    // ============ verifyIfUserHasALibrary Tests ============

    @Test
    void returnTrueWhenUserAlreadyHasALibrary() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        String username = "User1";

        User userDouble = createMockUser(username);
        LibraryFactory factoryDouble = createLibraryFactory(libraryName, userDouble);
        LibraryRepository libraryRepository = new LibraryRepository(factoryDouble);

        // ← actually add the library first
        libraryRepository.createAndAdd(libraryName, userDouble);

        // Act
        boolean result = libraryRepository.verifyIfUserHasALibrary(userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void returnFalseWhenUserDoesNotHaveALibrary() {
        // Arrange
        User userDouble = createMockUser("User1");
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        // Act
        boolean result = libraryRepository.verifyIfUserHasALibrary(userDouble);

        // Assert
        assertFalse(result);
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
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        // ← actually populate the repo
        libraryRepository.createAndAdd(libraryName, userDouble);

        // Act
        List<PublicationSample> realPublications =
                libraryRepository.getAllPublicationsFromUserLibrary(username);

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