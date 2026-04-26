package TOPSECRET.controller;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.LibraryService;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US009ToCreateLibraryController}.
 * <p>
 * This test class verifies the controller behavior for the Create Library use case,
 * including construction validation, user existence checks, and library creation delegation.
 * </p>
 */
class US009ToCreateLibraryControllerTest {

    // ===========================================================
    // ========== Constructor Tests ==============================
    // ===========================================================

    @Test
    void givenValidDependencies_whenConstructing_thenCreatesSuccessfully() {
        // Arrange
        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        LibraryService libraryServiceDouble = mock(LibraryService.class);

        // Act + Assert
        assertDoesNotThrow(() ->
                new US009ToCreateLibraryController(userRepositoryDouble, libraryServiceDouble));
    }

    @Test
    void givenNullUserRepository_whenConstructing_thenThrowsNullPointerException() {
        // Arrange
        LibraryService libraryServiceDouble = mock(LibraryService.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new US009ToCreateLibraryController(null, libraryServiceDouble));
    }

    @Test
    void givenNullLibraryService_whenConstructing_thenThrowsNullPointerException() {
        // Arrange
        IUserRepository userRepositoryDouble = mock(IUserRepository.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new US009ToCreateLibraryController(userRepositoryDouble, null));
    }

    // ===========================================================
    // ========== createAndAddLibrary() Tests ====================
    // ===========================================================

    @Test
    void givenNullLibraryName_whenCreateAndAddLibrary_thenThrowsIllegalArgumentException() {
        // Arrange
        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        LibraryService libraryServiceDouble = mock(LibraryService.class);

        US009ToCreateLibraryController controller =
                new US009ToCreateLibraryController(userRepositoryDouble, libraryServiceDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createAndAddLibrary(null, "username"));
    }

    @Test
    void givenBlankLibraryName_whenCreateAndAddLibrary_thenThrowsIllegalArgumentException() {
        // Arrange
        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        LibraryService libraryServiceDouble = mock(LibraryService.class);

        US009ToCreateLibraryController controller =
                new US009ToCreateLibraryController(userRepositoryDouble, libraryServiceDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createAndAddLibrary("   ", "username"));
    }

    @Test
    void givenNullUsername_whenCreateAndAddLibrary_thenThrowsIllegalArgumentException() {
        // Arrange
        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        LibraryService libraryServiceDouble = mock(LibraryService.class);

        US009ToCreateLibraryController controller =
                new US009ToCreateLibraryController(userRepositoryDouble, libraryServiceDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createAndAddLibrary("My Library", null));
    }

    @Test
    void givenBlankUsername_whenCreateAndAddLibrary_thenThrowsIllegalArgumentException() {
        // Arrange
        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        LibraryService libraryServiceDouble = mock(LibraryService.class);

        US009ToCreateLibraryController controller =
                new US009ToCreateLibraryController(userRepositoryDouble, libraryServiceDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createAndAddLibrary("My Library", "   "));
    }

    @Test
    void givenNonExistentUser_whenCreateAndAddLibrary_thenThrowsIllegalArgumentException() {
        // Arrange
        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        LibraryService libraryServiceDouble = mock(LibraryService.class);

        when(userRepositoryDouble.findUserIdByUsername("unknown"))
                .thenReturn(Optional.empty());

        US009ToCreateLibraryController controller =
                new US009ToCreateLibraryController(userRepositoryDouble, libraryServiceDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createAndAddLibrary("My Library", "unknown"));
    }

    @Test
    void givenValidInput_whenCreateAndAddLibrary_thenReturnsTrue() throws Exception {
        // Arrange
        String libraryName = "My Library";
        String username = "user-123";
        UserId userIdDouble = mock(UserId.class);
        Library libraryDouble = mock(Library.class);

        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        when(userRepositoryDouble.findUserIdByUsername(username))
                .thenReturn(Optional.of(userIdDouble));

        LibraryService libraryServiceDouble = mock(LibraryService.class);
        when(libraryServiceDouble.createAndSave(libraryName, userIdDouble))
                .thenReturn(libraryDouble);

        US009ToCreateLibraryController controller =
                new US009ToCreateLibraryController(userRepositoryDouble, libraryServiceDouble);

        // Act
        boolean result = controller.createAndAddLibrary(libraryName, username);

        // Assert
        assertTrue(result);
    }

    @Test
    void givenUserWithExistingLibrary_whenCreateAndAddLibrary_thenThrowsInstanceAlreadyExistsException()
            throws Exception {
        // Arrange
        String libraryName = "My Library";
        String username = "user-123";
        UserId userIdDouble = mock(UserId.class);

        IUserRepository userRepositoryDouble = mock(IUserRepository.class);
        when(userRepositoryDouble.findUserIdByUsername(username))
                .thenReturn(Optional.of(userIdDouble));

        LibraryService libraryServiceDouble = mock(LibraryService.class);
        when(libraryServiceDouble.createAndSave(libraryName, userIdDouble))
                .thenThrow(new InstanceAlreadyExistsException("User already has a library"));

        US009ToCreateLibraryController controller =
                new US009ToCreateLibraryController(userRepositoryDouble, libraryServiceDouble);

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> controller.createAndAddLibrary(libraryName, username));
    }
}