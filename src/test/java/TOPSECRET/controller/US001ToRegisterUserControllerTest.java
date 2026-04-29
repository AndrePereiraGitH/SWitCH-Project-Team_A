package TOPSECRET.controller;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US001ToRegisterUserControllerTest {

    @Test
    void shouldRegisterUserSuccessfully() {
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        UserFactory userFactoryDouble = mock(UserFactory.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);
        User userDouble = mock(User.class);

        when(repositoryDouble.exists("TestUser1")).thenReturn(false);
        when(userFactoryDouble.create("TestUser1", contactDouble, profileDouble, UserRole.ADMIN))
                .thenReturn(userDouble);
        when(repositoryDouble.save(userDouble)).thenReturn(userDouble);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble, userFactoryDouble);

        User result = sut.registerUser("TestUser1", contactDouble, profileDouble, UserRole.ADMIN);

        assertSame(userDouble, result);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        UserFactory userFactoryDouble = mock(UserFactory.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble, userFactoryDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(null, contactDouble, profileDouble, UserRole.ADMIN)
        );
    }

    @Test
    void shouldThrowExceptionWhenContactIsNull() {
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        UserFactory userFactoryDouble = mock(UserFactory.class);
        Profile profileDouble = mock(Profile.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble, userFactoryDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", null, profileDouble, UserRole.ADMIN)
        );
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        UserFactory userFactoryDouble = mock(UserFactory.class);
        Contact contactDouble = mock(Contact.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble, userFactoryDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", contactDouble, null, UserRole.ADMIN)
        );
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        UserFactory userFactoryDouble = mock(UserFactory.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble, userFactoryDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("User", contactDouble, profileDouble, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        IUserRepository repositoryDouble = mock(IUserRepository.class);
        UserFactory userFactoryDouble = mock(UserFactory.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        when(repositoryDouble.exists("TestUser1")).thenReturn(true);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repositoryDouble, userFactoryDouble);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser("TestUser1", contactDouble, profileDouble, UserRole.ADMIN)
        );
    }
}