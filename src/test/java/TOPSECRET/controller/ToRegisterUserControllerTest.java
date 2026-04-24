package TOPSECRET.controller;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.User.IUserRepository;
import org.junit.jupiter.api.Test;

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
    void shouldRegisterUserSuccessfully() {
        // ARRANGE
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);
        User userDouble = mock(User.class);

        when(repositoryDouble.exists("TestUser1")).thenReturn(false);

        when(repositoryDouble.createAndAdd("TestUser1", contactDouble, profileDouble, UserRole.ADMIN))
                .thenReturn(userDouble);

        ToRegisterUserController sut =
                new ToRegisterUserController(repositoryDouble);

        // ACT
        User result = sut.registerUser("TestUser1", contactDouble, profileDouble, UserRole.ADMIN);

        // ASSERT
        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        // ARRANGE
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        ToRegisterUserController sut =
                new ToRegisterUserController(repositoryDouble);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(null, contactDouble, profileDouble, UserRole.ADMIN)
        );
    }

    @Test
    void shouldThrowExceptionWhenContactIsNull() {
        // ARRANGE
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        Profile profileDouble = mock(Profile.class);

        ToRegisterUserController sut =
                new ToRegisterUserController(repositoryDouble);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", null, profileDouble, UserRole.ADMIN)
        );
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        // ARRANGE
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        Contact contactDouble = mock(Contact.class);

        ToRegisterUserController sut =
                new ToRegisterUserController(repositoryDouble);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", contactDouble, null, UserRole.ADMIN)
        );
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        // ARRANGE
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        ToRegisterUserController sut =
                new ToRegisterUserController(repositoryDouble);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", contactDouble, profileDouble, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // ARRANGE
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        when(repositoryDouble.exists("TestUser1")).thenReturn(true);

        ToRegisterUserController sut =
                new ToRegisterUserController(repositoryDouble);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("TestUser1", contactDouble, profileDouble, UserRole.ADMIN)
        );
    }
}