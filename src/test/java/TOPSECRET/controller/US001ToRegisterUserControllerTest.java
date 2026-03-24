package TOPSECRET.controller;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Profile;
import TOPSECRET.repository.UserRepository;
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

    /**
     * Ensures successful user registration delegates to repository.
     */
    @Test
    void shouldRegisterUserSuccessfully() {

        // ARRANGE
        UserRepository repository = mock(UserRepository.class);

        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(repository);

        Contact contact = mock(Contact.class);
        Profile profile = mock(Profile.class);

        when(repository.exists("TestUser1")).thenReturn(false);

        // ACT
        sut.registerUser(
                "TestUser1",
                contact,
                profile,
                UserRole.ADMIN
        );

        // ASSERT
        verify(repository).createAndAdd(
                "TestUser1",
                contact,
                profile,
                UserRole.ADMIN
        );
    }

    /**
     * Ensures exception is thrown when username is null.
     */
    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {

        // ARRANGE
        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(
                        mock(UserRepository.class)
                );

        // ACT + ASSERT
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

    /**
     * Ensures exception is thrown when contact is null.
     */
    @Test
    void shouldThrowExceptionWhenContactIsNull() {

        // ARRANGE
        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(
                        mock(UserRepository.class)
                );

        // ACT + ASSERT
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

    /**
     * Ensures exception is thrown when profile is null.
     */
    @Test
    void shouldThrowExceptionWhenProfileIsNull() {

        // ARRANGE
        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(
                        mock(UserRepository.class)
                );

        // ACT + ASSERT
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

    /**
     * Ensures exception is thrown when role is null.
     */
    @Test
    void shouldThrowExceptionWhenRoleIsNull() {

        // ARRANGE
        US001ToRegisterUserController sut =
                new US001ToRegisterUserController(
                        mock(UserRepository.class)
                );

        // ACT + ASSERT
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
}