package TOPSECRET.services;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserFactory factoryDouble;

    @Mock
    private IUserRepository repoDouble;

    @InjectMocks
    private UserService service;

    @Test
    void constructor_shouldCreateService_whenValidDependencies() {
        assertNotNull(service);
    }

    @Test
    void constructor_shouldThrowException_whenFactoryIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UserService(null, repoDouble)
        );
    }

    @Test
    void constructor_shouldThrowException_whenRepositoryIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UserService(factoryDouble, null)
        );
    }

    @Test
    void createAndSave_shouldReturnPersistedUser_whenValidArguments() throws Exception {
        Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.NORMAL_USER);

        User createdUser = mock(User.class);
        User savedUser = mock(User.class);

        when(factoryDouble.create("Rodolph", profileDouble, roles))
                .thenReturn(createdUser);
        when(repoDouble.save(createdUser)).thenReturn(savedUser);

        User result = service.createAndSave("Rodolph", profileDouble, roles);

        assertNotNull(result);
        assertEquals(savedUser, result);
    }

    @Test
    void createAndSave_shouldThrowException_whenFactoryThrowsException() {
        Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.NORMAL_USER);

        when(factoryDouble.create(any(), any(), anySet()))
                .thenThrow(new IllegalArgumentException());

        assertThrows(
                IllegalArgumentException.class,
                () -> service.createAndSave("Rodolph", profileDouble, roles)
        );
    }

    @Test
    void createAndSave_shouldThrowException_whenRepositoryThrowsException() {
        Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.NORMAL_USER);

        User createdUser = mock(User.class);

        when(factoryDouble.create(any(), any(), anySet()))
                .thenReturn(createdUser);
        when(repoDouble.save(createdUser))
                .thenThrow(new IllegalArgumentException());

        assertThrows(
                IllegalArgumentException.class,
                () -> service.createAndSave("Rodolph", profileDouble, roles)
        );
    }
}