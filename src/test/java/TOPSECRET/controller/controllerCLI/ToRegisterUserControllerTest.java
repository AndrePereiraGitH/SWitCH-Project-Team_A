package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.UserService;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US001 - To register a User in the system.
 * SUT: ToRegisterUserController
 *
 * Repository dependency is isolated using test doubles.
 */
class ToRegisterUserControllerTest {

    @Test
    void shouldRegisterUserSuccessfully() throws InstanceAlreadyExistsException, AccessDeniedException {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Profile profileDouble = mock(Profile.class);
        UserId requesterIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);
        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        when(userServiceDouble.createAndSave("TestUser1", profileDouble, roles, requesterIdDouble))
                .thenReturn(userDouble);

        ToRegisterUserController sut = new ToRegisterUserController(userServiceDouble);

        // Act
        User result = sut.registerUser("TestUser1", profileDouble, roles, requesterIdDouble);

        // Assert
        assertSame(userDouble, result);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Profile profileDouble = mock(Profile.class);
        UserId requesterIdDouble = mock(UserId.class);
        ToRegisterUserController sut = new ToRegisterUserController(userServiceDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(null, profileDouble, Set.of(UserRole.ADMIN), requesterIdDouble)
        );
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        UserId requesterIdDouble = mock(UserId.class);
        ToRegisterUserController sut = new ToRegisterUserController(userServiceDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", null, Set.of(UserRole.ADMIN), requesterIdDouble)
        );
    }

    @Test
    void shouldThrowExceptionWhenRolesIsNull() {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Profile profileDouble = mock(Profile.class);
        UserId requesterIdDouble = mock(UserId.class);
        ToRegisterUserController sut = new ToRegisterUserController(userServiceDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", profileDouble, null, requesterIdDouble)
        );
    }

    @Test
    void shouldThrowExceptionWhenRequesterIdIsNull() {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Profile profileDouble = mock(Profile.class);
        ToRegisterUserController sut = new ToRegisterUserController(userServiceDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", profileDouble, Set.of(UserRole.ADMIN), null)
        );
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() throws InstanceAlreadyExistsException, AccessDeniedException {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Profile profileDouble = mock(Profile.class);
        UserId requesterIdDouble = mock(UserId.class);
        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        when(userServiceDouble.createAndSave("TestUser1", profileDouble, roles, requesterIdDouble))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        ToRegisterUserController sut = new ToRegisterUserController(userServiceDouble);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("TestUser1", profileDouble, roles, requesterIdDouble)
        );
    }

    @Test
    void constructor_shouldThrowException_whenUserServiceIsNull() {
        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new ToRegisterUserController(null)
        );
    }
}