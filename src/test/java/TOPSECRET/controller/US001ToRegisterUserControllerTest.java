package TOPSECRET.controller;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US001 - To register a User in the system
 * SUT: US001ToRegisterUserController
 *
 * Repository dependency is mocked to isolate controller logic.
 */
class US001ToRegisterUserControllerTest {

    @Test
    void shouldRegisterUserSuccessfully() {
        // ARRANGE
        IUserRepository repository = mock(IUserRepository.class);
        UserFactory factory = mock(UserFactory.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repository, factory);

        Contact contact = mock(Contact.class);
        Profile profile = mock(Profile.class);
        User mockUser = mock(User.class);

        when(repository.exists("TestUser1")).thenReturn(false);
        when(factory.create("TestUser1", contact, profile, UserRole.ADMIN))
                .thenReturn(mockUser);

        // ACT
        sut.registerUser("TestUser1", contact, profile, UserRole.ADMIN);

        // ASSERT
        verify(factory).create("TestUser1", contact, profile, UserRole.ADMIN);
        verify(repository).save(mockUser);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        IUserRepository repository = mock(IUserRepository.class);
        UserFactory factory = mock(UserFactory.class);
        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repository, factory);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(
                        null,
                        mock(Contact.class),
                        mock(Profile.class),
                        UserRole.ADMIN
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenContactIsNull() {
        IUserRepository repository = mock(IUserRepository.class);
        UserFactory factory = mock(UserFactory.class);
        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repository, factory);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(
                        "User",
                        null,
                        mock(Profile.class),
                        UserRole.ADMIN
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        IUserRepository repository = mock(IUserRepository.class);
        UserFactory factory = mock(UserFactory.class);
        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repository, factory);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(
                        "User",
                        mock(Contact.class),
                        null,
                        UserRole.ADMIN
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        IUserRepository repository = mock(IUserRepository.class);
        UserFactory factory = mock(UserFactory.class);
        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repository, factory);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(
                        "User",
                        mock(Contact.class),
                        mock(Profile.class),
                        null
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        IUserRepository repository = mock(IUserRepository.class);
        UserFactory factory = mock(UserFactory.class);

        when(repository.exists("TestUser1")).thenReturn(true);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repository, factory);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.registerUser(
                        "TestUser1",
                        mock(Contact.class),
                        mock(Profile.class),
                        UserRole.ADMIN
                )
        );

        verify(factory, never()).create(anyString(), any(), any(), any());
        verify(repository, never()).save(any());
    }
}