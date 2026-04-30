package TOPSECRET.controller;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.services.UserService;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US001ToRegisterUserControllerTest {

    @Test
    void shouldRegisterUserSuccessfully() throws InstanceAlreadyExistsException {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);
        User userDouble = mock(User.class);

        when(userServiceDouble.createAndSave("TestUser1", contactDouble, profileDouble, UserRole.ADMIN))
                .thenReturn(userDouble);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(userServiceDouble);

        // Act
        User result = sut.registerUser("TestUser1", contactDouble, profileDouble, UserRole.ADMIN);

        // Assert
        assertSame(userDouble, result);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(userServiceDouble);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(null, contactDouble, profileDouble, UserRole.ADMIN)
        );
    }

    @Test
    void shouldThrowExceptionWhenContactIsNull() {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Profile profileDouble = mock(Profile.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(userServiceDouble);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", null, profileDouble, UserRole.ADMIN)
        );
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Contact contactDouble = mock(Contact.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(userServiceDouble);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", contactDouble, null, UserRole.ADMIN)
        );
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(userServiceDouble);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", contactDouble, profileDouble, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() throws InstanceAlreadyExistsException {
        // Arrange
        UserService userServiceDouble = mock(UserService.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        when(userServiceDouble.createAndSave("TestUser1", contactDouble, profileDouble, UserRole.ADMIN))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(userServiceDouble);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("TestUser1", contactDouble, profileDouble, UserRole.ADMIN)
        );
    }
}