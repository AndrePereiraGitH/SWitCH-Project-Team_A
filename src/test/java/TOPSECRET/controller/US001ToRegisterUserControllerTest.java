package TOPSECRET.controller;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US001 - To register a User in the system.
 * SUT: US001ToRegisterUserController
 *
 * Repository dependency is isolated using test doubles.
 */
class US001ToRegisterUserControllerTest {

    @Test
    void shouldRegisterUserSuccessfully() {
        // ARRANGE
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);
        UserId userIdDouble = mock(UserId.class);

        when(repositoryDouble.exists("TestUser1")).thenReturn(false);
        when(repositoryDouble.create("TestUser1", contactDouble, profileDouble, UserRole.ADMIN))
                .thenReturn(userIdDouble);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble);

        // ACT
        UserId result = sut.registerUser("TestUser1", contactDouble, profileDouble, UserRole.ADMIN);

        // ASSERT
        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        // ARRANGE
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble);

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

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble);

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

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble);

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

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble);

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

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("TestUser1", contactDouble, profileDouble, UserRole.ADMIN)
        );
    }
}