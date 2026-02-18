package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link LibraryRepository} class.
 * <p>
 * This test class verifies the behavior of library creation, validation,
 * and user library ownership checks in the LibraryRepository.
 * </p>
 */

class LibraryRepositoryTest {


    Profile profile = new Profile("profile name", new Country("Portugal"), LocalDate.now());
    User user = new User("user name", new Contact(), profile, UserRole.NORMAL_USER);

    Map<String, Library> libraries = new HashMap<>();
    LibraryRepository libraryRepository = new LibraryRepository(libraries);


    /**
     * Tests that a library is successfully created and added to the repository
     * when provided with valid parameters.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The returned library is not null</li>
     *   <li>The library name matches the provided name</li>
     *   <li>The owner username is correctly set</li>
     * </ul>
     * </p>
     *
     * @throws InstanceAlreadyExistsException if the user already has a library
     */

    @Test
    void shouldCreateAndAddLibrarySuccessfully() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";

        // Act
        Library newLibrary = libraryRepository.createAndAdd(libraryName,user);

        // Assert
        assertNotNull(newLibrary);
        assertEquals(libraryName, newLibrary.getLibraryName());
        assertEquals("user name", user.getUserName());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when attempting
     * to create a library with an empty name.
     * <p>
     * Validates that library names must contain at least one character.
     * </p>
     */

    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsEmpty(){
        //Arrange
        String libraryName = "";


        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> libraryRepository.createAndAdd(libraryName,user));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when attempting
     * to create a library with a null user.
     * <p>
     * Validates that a valid user must be provided as the library owner.
     * </p>
     */

    @Test
    void shouldReturnAnExceptionWhenTheUserIsNull(){
        //Arrange
        String libraryName = "mylib";


        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> libraryRepository.createAndAdd(libraryName,null));
    }

    /**
     * Tests that an {@link InstanceAlreadyExistsException} is thrown when attempting
     * to create a second library for a user who already has one.
     * <p>
     * Validates the business rule that users can only have one library.
     * </p>
     */

    @Test
    void shouldReturnAnExceptionWhenTheUserAlreadyHasALibrary() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";

        //Act
        libraryRepository.createAndAdd(libraryName,user);

        // Act & Assert
        assertThrows(InstanceAlreadyExistsException.class, () -> libraryRepository.createAndAdd(libraryName,user));
    }

    /**
     * Tests that the verification method returns {@code true} when checking
     * a user who already has a library.
     * <p>
     * Verifies the correct detection of existing library ownership.
     * </p>
     *
     * @throws InstanceAlreadyExistsException if library creation fails
     */

    @Test
    void returnTrueWhenUserAlreadyHasALibrary() throws InstanceAlreadyExistsException{
        // Arrange
        String libraryName = "My Library";
        libraryRepository.createAndAdd(libraryName,user);

        // Act
        boolean result = libraryRepository.verifyIfUserHasALibrary(user);

        //Assert
        assertTrue(result);

    }

    /**
     * Tests that the verification method returns {@code false} when checking
     * a user who does not have a library.
     * <p>
     * Verifies the correct detection when a user has no library ownership.
     * </p>
     */

    @Test
    void returnFalseWhenUserDoesNotHaveALibrary() {
        // Arrange

        // Act
        boolean result = libraryRepository.verifyIfUserHasALibrary(user);

        //Assert
        assertFalse(result);

    }




}