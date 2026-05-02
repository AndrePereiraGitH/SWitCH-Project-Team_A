package TOPSECRET.persistence.mem;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryMemTest {

    @InjectMocks
    private UserRepositoryMem sut;

    @Mock
    private User user;

    @Mock
    private UserId userId;


    @Test
    void shouldSaveUser() {
        // Act
        User result = sut.save(user);

        // Assert
        assertEquals(user, result);
        assertEquals(1, sut.getUserCount());
    }

    @Test
    void shouldReturnTrueWhenUsernameExists() {
        // Arrange
        when(user.getUserName()).thenReturn("john");
        sut.save(user);

        // Act
        boolean result = sut.existsByUsername("john");

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldFindUserById() {
        // Arrange
        when(user.id()).thenReturn(userId);
        sut.save(user);

        // Act
        Optional<User> result = sut.findByID(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundById() {
        // Act
        Optional<User> result = sut.findByID(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> sut.findByID(null));
    }

    @Test
    void shouldFindIdByUsername() {
        // Arrange
        when(user.getUserName()).thenReturn("john");
        when(user.id()).thenReturn(userId);
        sut.save(user);

        // Act
        Optional<UserId> result = sut.findIdByUsername("john");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get());
    }

    @Test
    void shouldThrowWhenUsernameIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> sut.findIdByUsername(null));
    }

    @Test
    void shouldThrowWhenUsernameIsBlank() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> sut.findIdByUsername(" "));
    }
}