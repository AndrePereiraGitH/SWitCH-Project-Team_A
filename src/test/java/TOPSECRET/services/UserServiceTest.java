package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.UserAuthorizationPolicy;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserService}.
 *
 * <p>Verifies authorization, creation and persistence logic.</p>
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserFactory userFactory;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserAuthorizationPolicy policy;

    @InjectMocks
    private UserService service;

    // ================================================================
    // createAndSave — success
    // ================================================================

    @Test
    void createAndSave_shouldReturnSavedUser_whenRequesterIsAuthorized()
            throws InstanceAlreadyExistsException, AccessDeniedException {

        // Arrange
        String username = "newuser";
        Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.NORMAL_USER);
        UserId requesterIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);
        User createdUserDouble = mock(User.class);
        User savedUserDouble = mock(User.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.of(requesterDouble));
        when(policy.canRegisterUser(requesterDouble)).thenReturn(true);
        when(userFactory.create(username, profileDouble, roles)).thenReturn(createdUserDouble);
        when(userRepository.save(createdUserDouble)).thenReturn(savedUserDouble);

        // Act
        User result = service.createAndSave(username, profileDouble, roles, requesterIdDouble);

        // Assert
        assertSame(savedUserDouble, result);
    }

    // ================================================================
    // createAndSave — requester not found
    // ================================================================

    @Test
    void createAndSave_shouldThrowIllegalArgumentException_whenRequesterNotFound() {

        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave("user", profileDouble, Set.of(UserRole.NORMAL_USER), requesterIdDouble));
    }

    // ================================================================
    // createAndSave — access denied
    // ================================================================

    @Test
    void createAndSave_shouldThrowAccessDeniedException_whenRequesterIsNotAuthorized() {

        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);
        Profile profileDouble = mock(Profile.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.of(requesterDouble));
        when(policy.canRegisterUser(requesterDouble)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> service.createAndSave("user", profileDouble, Set.of(UserRole.NORMAL_USER), requesterIdDouble));
    }

    // ================================================================
    // constructor — null checks
    // ================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenUserFactoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new UserService(null, userRepository, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenUserRepositoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new UserService(userFactory, null, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenPolicyIsNull() {
        assertThrows(NullPointerException.class,
                () -> new UserService(userFactory, userRepository, null));
    }

    // ================================================================
    // findByID — found
    // ================================================================

    @Test
    void findByID_shouldReturnUser_whenUserExists() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);
        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));

        // Act
        User result = service.findByID(userIdDouble);

        // Assert
        assertSame(userDouble, result);
    }

    // ================================================================
// findByID — success
// ================================================================

    @Test
    void findByID_shouldReturnUser_whenFound() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // ACT
        User result = service.findByID(userId);

        // ASSERT
        assertSame(user, result);
    }

// ================================================================
// findByID — not found
// ================================================================

    @Test
    void findByID_shouldThrowIllegalArgument_whenUserNotFound() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> service.findByID(userId));
    }

    // ================================================================
    // findByID — null check
    // ================================================================

    @Test
    void findByID_shouldThrowNullPointerException_whenUserIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> service.findByID(null));
    }

    // ================================================================
    // existsByUsername — true
    // ================================================================

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameExists() {
        // Arrange
        String username = "existinguser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean result = service.existsByUsername(username);

        // Assert
        assertTrue(result);
    }

    // ================================================================
    // existsByUsername — false
    // ================================================================

    @Test
    void existsByUsername_shouldReturnFalse_whenUsernameDoesNotExist() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean result = service.existsByUsername(username);

        // Assert
        assertFalse(result);
    }

    // ================================================================
    // existsByUsername — null check
    // ================================================================

    @Test
    void existsByUsername_shouldThrowNullPointerException_whenUsernameIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.existsByUsername(null));
    }

    // ================================================================
    // findIdByUsername — found
    // ================================================================

    @Test
    void findIdByUsername_shouldReturnUserId_whenUsernameExists() {
        // Arrange
        String username = "existinguser";
        UserId userIdDouble = mock(UserId.class);
        when(userRepository.findIdByUsername(username)).thenReturn(Optional.of(userIdDouble));

        // Act
        UserId result = service.findIdByUsername(username);

        // Assert
        assertSame(userIdDouble, result);
    }

    // ================================================================
    // findIdByUsername — not found
    // ================================================================

    @Test
    void findIdByUsername_shouldThrowIllegalArgumentException_whenUsernameDoesNotExist() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.findIdByUsername(username)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.findIdByUsername(username));
    }

    // ================================================================
    // findIdByUsername — null check
    // ================================================================

    @Test
    void findIdByUsername_shouldThrowNullPointerException_whenUsernameIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.findIdByUsername(null));
    }
}