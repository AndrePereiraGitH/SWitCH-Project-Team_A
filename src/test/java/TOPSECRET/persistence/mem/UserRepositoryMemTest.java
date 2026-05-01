package TOPSECRET.persistence.mem;

import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserRepositoryMem}.
 *
 * <p>Verifies saving, querying, and validation behavior.
 */
@ExtendWith(MockitoExtension.class)
class UserRepositoryMemTest {

    private IUserRepository userRepository;

    @Mock
    private User user1;

    @Mock
    private User user2;

    @Mock
    private UserId userId1;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryMem();
    }

    @Test
    void shouldSaveUserSuccessfully() {
        // Arrange
        when(user1.getUserName()).thenReturn("UserOne");

        // Act
        User result = userRepository.save(user1);

        // Assert
        assertSame(user1, result);
        assertTrue(userRepository.existsByUsername("UserOne"));
    }

    @Test
    void shouldAllowSavingMultipleUsers() {
        // Arrange
        when(user1.getUserName()).thenReturn("UserOne");
        when(user2.getUserName()).thenReturn("UserTwo");

        // Act
        userRepository.save(user1);
        userRepository.save(user2);

        // Assert
        assertTrue(userRepository.existsByUsername("UserOne"));
        assertTrue(userRepository.existsByUsername("UserTwo"));

        if (userRepository instanceof UserRepositoryMem repoMem) {
            assertEquals(2, repoMem.getUserCount());
        }
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExist() {
        // Act
        boolean result = userRepository.existsByUsername("Ghost");

        // Assert
        assertFalse(result);
    }

    @Test
    void findUserIdByUsername_shouldReturnId_whenUserExistsByUsername() {
        // Arrange
        when(user1.getUserName()).thenReturn("UserOne");
        when(user1.id()).thenReturn(userId1);

        userRepository.save(user1);

        // Act
        Optional<UserId> result = userRepository.findUserIdByUsername("UserOne");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId1, result.get());
    }

    @Test
    void findUserIdByUsername_shouldReturnEmpty_whenUserNotFound() {
        // Act
        Optional<UserId> result = userRepository.findUserIdByUsername("Unknown");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findUserIdByUsername_shouldThrowException_whenUsernameIsNull() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> userRepository.findUserIdByUsername(null)
        );
    }

    @Test
    void findUserIdByUsername_shouldThrowException_whenUsernameIsBlank() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.findUserIdByUsername("   ")
        );
    }

    @Test
    void findByID_shouldReturnUser_whenExistsByUsername() {
        // Arrange
        when(user1.id()).thenReturn(userId1);

        userRepository.save(user1);

        // Act
        Optional<User> result = userRepository.findByID(userId1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
    }

    @Test
    void findByID_shouldReturnEmpty_whenNotFound() {
        // Act
        Optional<User> result = userRepository.findByID(userId1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByID_shouldThrowException_whenIdIsNull() {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.findByID(null)
        );
    }
}