package TOPSECRET.services;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserService}.
 * <p>
 * This test class verifies that {@code UserService} correctly creates
 * and persists {@link User} instances by delegating to {@link UserFactory}
 * and {@link IUserRepository}.
 * </p>
 */
class UserServiceTest {

    @Test
    void constructor_shouldCreateService_whenValidDependencies() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);
        IUserRepository repoDouble = mock(IUserRepository.class);

        // Act
        UserService service = new UserService(factoryDouble, repoDouble);

        // Assert
        assertNotNull(service);
    }

    @Test
    void constructor_shouldThrowException_whenFactoryIsNull() {
        // Arrange
        IUserRepository repoDouble = mock(IUserRepository.class);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new UserService(null, repoDouble)
        );
    }

    @Test
    void constructor_shouldThrowException_whenRepositoryIsNull() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new UserService(factoryDouble, null)
        );
    }

    @Test
    void createAndSave_shouldReturnPersistedUser_whenValidArguments() throws Exception {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);
        IUserRepository repoDouble = mock(IUserRepository.class);

        UserService service = new UserService(factoryDouble, repoDouble);

        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        User createdUser = mock(User.class);
        User savedUser = mock(User.class);

        when(factoryDouble.create("Rodolph", contactDouble, profileDouble, UserRole.NORMAL_USER))
                .thenReturn(createdUser);

        when(repoDouble.save(createdUser)).thenReturn(savedUser);

        // Act
        User result = service.createAndSave(
                "Rodolph",
                contactDouble,
                profileDouble,
                UserRole.NORMAL_USER
        );

        // Assert
        assertNotNull(result);
        assertEquals(savedUser, result);
    }

    @Test
    void createAndSave_shouldThrowException_whenFactoryThrowsException() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);
        IUserRepository repoDouble = mock(IUserRepository.class);

        UserService service = new UserService(factoryDouble, repoDouble);

        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        when(factoryDouble.create(anyString(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> service.createAndSave(
                        "Rodolph",
                        contactDouble,
                        profileDouble,
                        UserRole.NORMAL_USER
                )
        );
    }

    @Test
    void createAndSave_shouldThrowException_whenRepositoryThrowsException()  {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);
        IUserRepository repoDouble = mock(IUserRepository.class);

        UserService service = new UserService(factoryDouble, repoDouble);

        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        User createdUser = mock(User.class);

        when(factoryDouble.create(anyString(), any(), any(), any()))
                .thenReturn(createdUser);

        when(repoDouble.save(createdUser))
                .thenThrow(new IllegalArgumentException());
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> service.createAndSave(
                        "Rodolph",
                        contactDouble,
                        profileDouble,
                        UserRole.NORMAL_USER
                )
        );
    }
}