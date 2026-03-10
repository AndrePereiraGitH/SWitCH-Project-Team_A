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

/**
 * Unit tests for the {@link LibraryRepository} class.
 * <p>
 * Verifies library creation, retrieval, validation,
 * and user library ownership checks.
 * </p>
 */
class LibraryRepositoryTest {

    private LibraryFactory libraryFactoryDouble;

    @BeforeEach
    void setup() {
        libraryFactoryDouble = mock(LibraryFactory.class);
    }


    // ============ Constructor Tests ============


    @Test
    void shouldThrowAnExceptionWhenFactoryIsNull() {
        // Arrange


        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryRepository( null));
    }


    // ============ getLibraryOfUser Tests ============

    @Test
    void ensureGetLibraryOfUserSuccessfully() throws InstanceNotFoundException {
        // Arrange
        String username = "john_doe";
        String libraryName = "lib1";

        User userDouble = createMockUser(username);

        Library libraryDouble = createMockLibrary(libraryName, userDouble);

        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);
        libraryRepository.add(libraryDouble);

        // Act
        Library result = libraryRepository.getLibraryOfUser(username);

        // Assert
        assertEquals(libraryName, result.getLibraryName());
    }


    @Test
    void throwExceptionWhenUsernameIsNull() {
        // Arrange
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> libraryRepository.getLibraryOfUser(null));
    }


    @Test
    void throwExceptionWhenUsernameIsEmpty() {
        // Arrange
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.getLibraryOfUser(""));
    }


    @Test
    void throwExceptionWhenLibraryIsNotFound() {
        // Arrange
        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        // act & assert
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

        Library library = createMockLibrary(libraryName,userDouble);

        when(libraryFactoryDouble.create(libraryName, userDouble)).thenReturn(library);

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
        String username = "User1";

        User userDouble = createMockUser(username);

        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.createAndAdd(null, userDouble));

    }

    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsEmpty() {
        // Arrange
        String libraryName = "";
        String username = "User1";

        User userDouble = createMockUser(username);

        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> libraryRepository.createAndAdd(libraryName, userDouble));
    }


    @Test
    void shouldReturnAnExceptionWhenTheUserIsNull() {
        // Arrange
        String libraryName = "mylib";

        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);

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

        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);
        libraryRepository.add(libraryDouble);


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

        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);
        libraryRepository.add(libraryDouble);

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

        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);


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

        LibraryRepository libraryRepository = new LibraryRepository(libraryFactoryDouble);
        libraryRepository.add(libraryDouble);

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
}