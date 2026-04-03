package TOPSECRET.controller;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import java.util.Optional;
import java.util.UUID;

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

    // ===========================================================
    // ========== Constructor Tests ==============================
    // ===========================================================

    @Test
    void shouldCreateControllerWithValidRepositories() {
        //Arrange
        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);

        // Act & Assert
        assertDoesNotThrow(() ->
                new US009ToCreateLibraryController(libraryRepositoryDouble, userRepositoryDouble));
    }

    @Test
    void shouldThrowExceptionWhenLibraryRepositoryIsNull() {
        //Arrange
        IUserRepository userRepositoryDouble = mock(IUserRepository.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new US009ToCreateLibraryController(null, userRepositoryDouble));
    }

    @Test
    void shouldThrowExceptionWhenUserRepositoryIsNull() {
        //Arrange
        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new US009ToCreateLibraryController(libraryRepositoryDouble, null));
    }

    // ===========================================================
    // ========== createAndAddLibrary Tests ======================
    // ===========================================================

    @Test
    void createAndAdd_shouldReturnTrue_whenALibraryIsCreated() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "mylib";
        String username = "User1";

        UserId userId = mock(UserId.class);
        when(userId.getUsername()).thenReturn(UUID.randomUUID().toString());

        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        when(userRepositoryDouble.findUserIdByUsername(username)).thenReturn(Optional.of(userId));

        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);

        US009ToCreateLibraryController ctrl = new US009ToCreateLibraryController(
                libraryRepositoryDouble, userRepositoryDouble);

        // Act
        boolean result = ctrl.createAndAddLibrary(libraryName, username);

        // Assert
        assertTrue(result);
    }

    @Test
    void createAndAdd_shouldThrowAnException_whenUserIsEmpty() {
        // Arrange
        String libraryName = "mylib";
        String username = "User1";

        UserId userId = mock(UserId.class);
        when(userId.getUsername()).thenReturn(UUID.randomUUID().toString());

        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        when(userRepositoryDouble.findUserIdByUsername(username)).thenReturn(Optional.empty());

        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);

        US009ToCreateLibraryController ctrl = new US009ToCreateLibraryController(
                libraryRepositoryDouble, userRepositoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                ctrl.createAndAddLibrary(libraryName, username));
    }
}