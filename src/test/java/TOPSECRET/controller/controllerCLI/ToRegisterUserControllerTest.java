package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.services.UserService;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US001 - To register a User in the system.
 * SUT: US001ToRegisterUserController
 *
 * Repository dependency is isolated using test doubles.
 */
class ToRegisterUserControllerTest {

    @Test
    void shouldRegisterUserSuccessfully() throws InstanceAlreadyExistsException {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
           Profile profileDouble = mock(Profile.class);
        User userDouble = mock(User.class);

        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        when(userServiceDouble.createAndSave("TestUser1",   profileDouble, roles))
                .thenReturn(userDouble);

        ToRegisterUserController sut =
                new ToRegisterUserController(userServiceDouble);

        // Act
        User result = sut.registerUser("TestUser1",   profileDouble, roles);

        // Assert
        assertSame(userDouble, result);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        UserService userServiceDouble = mock(UserService.class);
           Profile profileDouble = mock(Profile.class);

        ToRegisterUserController sut =
                new ToRegisterUserController(userServiceDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(null,   profileDouble, Set.of(UserRole.ADMIN))
        );
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        UserService userServiceDouble = mock(UserService.class);

        ToRegisterUserController sut =
                new ToRegisterUserController(userServiceDouble);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User",   null, Set.of(UserRole.ADMIN))
        );
    }

    @Test
    void shouldThrowExceptionWhenRolesIsNull() {
        UserService userServiceDouble = mock(UserService.class);
           Profile profileDouble = mock(Profile.class);

        ToRegisterUserController sut =
                new ToRegisterUserController(userServiceDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User",   profileDouble, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() throws InstanceAlreadyExistsException {
        UserService userServiceDouble = mock(UserService.class);
           Profile profileDouble = mock(Profile.class);

        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        when(userServiceDouble.createAndSave("TestUser1",   profileDouble, roles))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        ToRegisterUserController sut =
                new ToRegisterUserController(userServiceDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("TestUser1",   profileDouble, roles)
        );
    }
}