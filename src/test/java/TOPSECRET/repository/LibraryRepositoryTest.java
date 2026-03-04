package TOPSECRET.repository;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link LibraryRepository} class.
 * <p>
 * Verifies library creation, retrieval, validation,
 * and user library ownership checks.
 * </p>
 */
class LibraryRepositoryTest {

    // ============ Constructor Tests ============


    @Test
    void shouldThrowAnExceptionWhenFactoryIsNull() {
        // Arrange
        Map<String, Library> libraries = new HashMap<>();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryRepository(libraries, null));
    }


    // ============ getLibraryOfUser Tests ============


    @Test
    void ensureGetLibraryOfUserSuccessfully() throws InstanceNotFoundException {
        // Arrange
        String username = "john_doe";
        String libraryName = "lib1";

        User userDouble = createMockUser(username);
        Library libraryDouble = createMockLibrary(libraryName, userDouble);

        Map<String, Library> libraries = new HashMap<>();
        libraries.put(username, libraryDouble);

        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act
        Library result = libraryRepository.getLibraryOfUser(username);

        // Assert
        assertEquals(libraryName, result.getLibraryName());
    }


    @Test
    void throwExceptionWhenUsernameIsNull() {
        // Arrange
        Map<String, Library> libraries = new HashMap<>();
        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> libraryRepository.getLibraryOfUser(null));
    }


    @Test
    void throwExceptionWhenUsernameIsEmpty() {
        // Arrange
        Map<String, Library> libraries = new HashMap<>();
        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.getLibraryOfUser(""));
    }


    @Test
    void throwExceptionWhenLibraryIsNull() {
        // Arrange
        String username = "User123";

        Map<String, Library> libraries = new HashMap<>();
        libraries.put(username, null);

        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act & Assert
        assertThrows(InstanceNotFoundException.class,
                () -> libraryRepository.getLibraryOfUser(username));
    }


    // ============ createAndAdd Tests ============


    @Test
    void shouldCreateAndAddLibrarySuccessfully() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        String username = "username1";

        User userDouble = createMockUser(username);

        Map<String, Library> libraries = new HashMap<>();

        LibraryFactory libraryFactoryDouble = createMockLibraryFactory(userDouble, libraryName);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act
        Library result = libraryRepository.createAndAdd(libraryName, userDouble);

        // Assert
        assertNotNull(result);
        assertEquals(libraryName, result.getLibraryName());
        assertEquals(username, result.getUser().getUserName());
    }


    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsEmpty() {
        // Arrange
        String libraryName = "";
        String username = "User1";

        User userDouble = createMockUser(username);

        Map<String, Library> libraries = new HashMap<>();

        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.createAndAdd(libraryName, userDouble));
    }


    @Test
    void shouldReturnAnExceptionWhenTheUserIsNull() {
        // Arrange
        String libraryName = "mylib";

        Map<String, Library> libraries = new HashMap<>();

        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.createAndAdd(libraryName, null));
    }


    @Test
    void shouldReturnAnExceptionWhenTheUserAlreadyHasALibrary() {
        // Arrange
        String libraryName = "My Library";
        String username = "User1";

        User userDouble = createMockUser(username);
        Library libraryDouble = createMockLibrary(libraryName, userDouble);

        Map<String, Library> libraries = new HashMap<>();
        libraries.put(username, libraryDouble);


        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act & Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> libraryRepository.createAndAdd(libraryName, userDouble));
    }


    // ============ verifyIfUserHasALibrary Tests ============


    @Test
    void returnTrueWhenUserAlreadyHasALibrary() {
        // Arrange
        String libraryName = "My Library";
        String username = "User1";

        User userDouble = createMockUser(username);
        Library libraryDouble = createMockLibrary(libraryName, userDouble);

        Map<String, Library> libraries = new HashMap<>();
        libraries.put(username, libraryDouble);

        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act
        boolean result = libraryRepository.verifyIfUserHasALibrary(userDouble);

        // Assert
        assertTrue(result);
    }


    @Test
    void returnFalseWhenUserDoesNotHaveALibrary() {
        // Arrange
        String username = "User1";

        User userDouble = createMockUser(username);

        Map<String, Library> libraries = new HashMap<>();
        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act
        boolean result = libraryRepository.verifyIfUserHasALibrary(userDouble);

        // Assert
        assertFalse(result);
    }


    // ============ getAllPublicationsFromUserLibrary Tests ============


    @Test
    void shouldReturnAllPublicationsFromUserLibrary() throws InstanceNotFoundException {
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

        Map<String, Library> libraries = new HashMap<>();
        libraries.put(username, libraryDouble);

        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        LibraryRepository libraryRepository = new LibraryRepository(libraries, libraryFactoryDouble);

        // Act
        List<PublicationSample> realPublications =
                libraryRepository.getAllPublicationsFromUserLibrary(username);

        // Assert
        assertEquals(expectedPublications, realPublications);
    }


    // ============ Helper Methods ============


    private LibraryFactory createMockLibraryFactory(User userDouble, String libraryName) {
        Library library = createMockLibrary(libraryName, userDouble);
        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        when(libraryFactoryDouble.create(libraryName, userDouble)).thenReturn(library);
        return libraryFactoryDouble;
    }


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
}