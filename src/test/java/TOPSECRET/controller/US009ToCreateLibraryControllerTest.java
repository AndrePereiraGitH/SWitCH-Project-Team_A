package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.repository.LibraryRepository;
import TOPSECRET.repository.UserRepositoryMem;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link US009ToCreateLibraryController} class.
 * <p>
 * This test class verifies the behavior of the library creation controller,
 * including controller instantiation, library creation for existing users,
 * and user existence validation.
 * </p>
 */
class US009ToCreateLibraryControllerTest {



    /**
     * Tests that the controller can be successfully instantiated with valid repositories.
     * <p>
     * Verifies that no exception is thrown when both repositories are non-null.
     * </p>
     */
    @Test
    void shouldCreateControllerWithValidRepositories() {
        //Arrange

        UserRepositoryMem userRepositoryMemDouble = mock(UserRepositoryMem.class);
        LibraryRepository libraryRepositoryDouble = mock(LibraryRepository.class);

        // Act & Assert
        assertDoesNotThrow(() -> new US009ToCreateLibraryController(libraryRepositoryDouble, userRepositoryMemDouble));
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
        UserRepositoryMem userRepositoryMemDouble = mock(UserRepositoryMem.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new US009ToCreateLibraryController(null, userRepositoryMemDouble));
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
        LibraryRepository libraryRepositoryDouble = mock(LibraryRepository.class);


        // Act & Assert
        assertThrows(NullPointerException.class, () -> new US009ToCreateLibraryController(libraryRepositoryDouble,null));
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
        // Arrange
        String libraryName = "mylib";
        String username = "User1";

        User userDouble = mock(User.class);
        when(userDouble.getUserName()).thenReturn(username);

        UserRepositoryMem userRepositoryMemDouble = mock(UserRepositoryMem.class);
        when(userRepositoryMemDouble.exists(username)).thenReturn(true);

        LibraryRepository libraryRepositoryDouble = mock(LibraryRepository.class);

        US009ToCreateLibraryController ctrl = new US009ToCreateLibraryController(
                libraryRepositoryDouble, userRepositoryMemDouble);

        // Act
        boolean result = ctrl.createAndAddLibrary(libraryName, userDouble);

        // Assert
        assertTrue(result);
        verify(libraryRepositoryDouble).createAndAdd(libraryName, userDouble);
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

        User userDouble = mock(User.class);

        UserRepositoryMem userRepositoryMemDouble = mock(UserRepositoryMem.class);

        LibraryRepository libraryRepositoryDouble = mock(LibraryRepository.class);

        US009ToCreateLibraryController ctrl = new US009ToCreateLibraryController(
                libraryRepositoryDouble, userRepositoryMemDouble);

        // Act
        boolean result = ctrl.createAndAddLibrary(libraryName,userDouble);

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
        String username = "User1";

        UserRepositoryMem userRepositoryMemDouble = mock(UserRepositoryMem.class);
        when(userRepositoryMemDouble.exists(username)).thenReturn(true);

        User userDouble = mock(User.class);
        when(userDouble.getUserName()).thenReturn(username);

        LibraryRepository libraryRepositoryDouble = mock(LibraryRepository.class);

        US009ToCreateLibraryController ctrl = new US009ToCreateLibraryController(
                libraryRepositoryDouble, userRepositoryMemDouble);

        // Act
        boolean result = ctrl.isUserExistent(userDouble);

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
        String username = "User1";

        UserRepositoryMem userRepositoryMemDouble = mock(UserRepositoryMem.class);
        when(userRepositoryMemDouble.exists(username)).thenReturn(false);

        User userDouble = mock(User.class);
        when(userDouble.getUserName()).thenReturn(username);

        LibraryRepository libraryRepositoryDouble = mock(LibraryRepository.class);

        US009ToCreateLibraryController ctrl = new US009ToCreateLibraryController(
                libraryRepositoryDouble, userRepositoryMemDouble);

        // Act
        boolean result = ctrl.isUserExistent(userDouble);

        //Assert
        assertFalse(result);
    }


}
