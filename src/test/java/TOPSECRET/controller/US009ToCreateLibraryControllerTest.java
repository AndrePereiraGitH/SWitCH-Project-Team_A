package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.repository.LibraryRepository;
import TOPSECRET.repository.UserRepository;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link US009ToCreateLibraryController} class.
 * <p>
 * This test class verifies the behavior of the library creation controller,
 * including controller instantiation, library creation for existing users,
 * and user existence validation.
 * </p>
 */
class US009ToCreateLibraryControllerTest {

    // User instantiation
    Email email = new Email("test.user", "example.com");
    Address address = new Address(
            "Main Street",
            "10",
            "12345",
            new Country("PORTUGAL")
    );
    Profile profile = new Profile("profile name", new Country("Portugal"), LocalDate.now());
    Contact contact = new Contact("987654321", "351", email, address);


    // Repo & Ctrl instantiation
    UserRepository userRepository = new UserRepository();

    Map<String, Library> libraries = new HashMap<>();
    LibraryFactory factory = new LibraryFactory();
    LibraryRepository libraryRepository = new LibraryRepository(libraries,factory);

    US009ToCreateLibraryController ctrl = new US009ToCreateLibraryController(libraryRepository,userRepository);

    /**
     * Tests that the controller can be successfully instantiated with valid repositories.
     * <p>
     * Verifies that no exception is thrown when both repositories are non-null.
     * </p>
     */
    @Test
    void shouldCreateControllerWithValidRepositories() {
        //Arrange
        Map<String, Library> libraries = new HashMap<>();
        LibraryRepository libraryRepository = new LibraryRepository(libraries,factory);

        // Act & Assert
        assertDoesNotThrow(() -> new US009ToCreateLibraryController(libraryRepository,userRepository));

    }

    /**
     * Tests that a {@link NullPointerException} is thrown when attempting to
     * instantiate the controller with a null LibraryRepository.
     * <p>
     * Validates that the LibraryRepository is a required dependency.
     * </p>
     */
    @Test
    void shouldThrowExceptionWhenLibraryRepositoryIsNull() {
        //Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new US009ToCreateLibraryController(null,userRepository));
    }

    /**
     * Tests that a {@link NullPointerException} is thrown when attempting to
     * instantiate the controller with a null UserRepository.
     * <p>
     * Validates that the UserRepository is a required dependency.
     * </p>
     */
    @Test
    void shouldThrowExceptionWhenUserRepositoryIsNull() {
        //Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new US009ToCreateLibraryController(libraryRepository,null));
    }

    /**
     * Tests that a library is successfully created when the user exists in the repository.
     * <p>
     * Verifies that the createAndAddLibrary method returns {@code true} when
     * provided with a valid library name and an existing user.
     * </p>
     *
     * @throws InstanceAlreadyExistsException if the user already has a library
     */
    @Test
    void shouldCreateALibraryIfUserExists() throws InstanceAlreadyExistsException {
        //Arrange
        String libraryName = "mylib";
        User user1 = userRepository.createAndAddUser("John1", contact,profile,UserRole.NORMAL_USER);

        // Act
        boolean result = ctrl.createAndAddLibrary(libraryName,user1);

        //Assert
        assertTrue(result);
    }

    /**
     * Tests that a library is not created when the user does not exist in the repository.
     * <p>
     * Verifies that the createAndAddLibrary method returns {@code false} when
     * attempting to create a library for a user that hasn't been added to the repository.
     * </p>
     *
     * @throws InstanceAlreadyExistsException if the user already has a library
     */
    @Test
    void shouldNotCreateALibraryIfUserDoesNotExist() throws InstanceAlreadyExistsException {
        //Arrange
        String libraryName = "mylib";
        User user1 = new User("John1", contact,profile,UserRole.NORMAL_USER);

        // Act
        boolean result = ctrl.createAndAddLibrary(libraryName,user1);

        //Assert
        assertFalse(result);
    }

    /**
     * Tests that the isUserExistent method returns {@code true} for a user
     * that exists in the repository.
     * <p>
     * Verifies correct detection of existing users.
     * </p>
     */
    @Test
    void shouldReturnTrueIfUserExists(){
        //Arrange
        User user1 = userRepository.createAndAddUser("John1", contact,profile,UserRole.NORMAL_USER);

        // Act
        boolean result = ctrl.isUserExistent(user1);

        //Assert
        assertTrue(result);
    }

    /**
     * Tests that the isUserExistent method returns {@code false} for a user
     * that does not exist in the repository.
     * <p>
     * Verifies correct detection when a user has not been added to the repository.
     * </p>
     */
    @Test
    void shouldReturnFalseIfUserDoesNotExist(){
        //Arrange
        User user1 = new User("John1", contact,profile,UserRole.NORMAL_USER);

        // Act
        boolean result = ctrl.isUserExistent(user1);

        //Assert
        assertFalse(result);
    }

}
