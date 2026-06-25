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
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.NoSuchElementException;
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
            throws InstanceAlreadyExistsException {

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
        assertThrows(NoSuchElementException.class,
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
    // findByID — not found
    // ================================================================

    @Test
    void findByID_shouldThrowNoSuchElementException_whenUserNotFound() {

        // Arrange
        UserId userId = mock(UserId.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> service.findByID(userId));
    }

    // ================================================================
    // findByID — null check
    // ================================================================

    @Test
    void findByID_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act + Assert
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

    // =======================================================================
    // canRegisterUser
    // =========================================================================

    @Test
    void canRegisterUser_shouldReturnTrue_whenUserIsAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(user));
        when(policy.canRegisterUser(user)).thenReturn(true);

        // ACT
        boolean result = service.canRegisterUser(requesterId);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canRegisterUser_shouldReturnFalse_whenUserIsNotAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(user));
        when(policy.canRegisterUser(user)).thenReturn(false);

        // ACT
        boolean result = service.canRegisterUser(requesterId);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canRegisterUser_shouldThrowNoSuchElementException_whenUserDoesNotExist() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(NoSuchElementException.class,
                () -> service.canRegisterUser(requesterId));
    }

    @Test
    void canRegisterUser_shouldThrowNullPointerException_whenRequesterIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> service.canRegisterUser(null));
    }

    // =========================================================================
    // canFindAllUsers
    // =========================================================================

    @Test
    void canFindAllUsers_shouldReturnTrue_whenUserIsAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(user));
        when(policy.canFindAllUsers(user)).thenReturn(true);

        // ACT
        boolean result = service.canFindAllUsers(requesterId);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canFindAllUsers_shouldReturnFalse_whenUserIsNotAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(user));
        when(policy.canFindAllUsers(user)).thenReturn(false);

        // ACT
        boolean result = service.canFindAllUsers(requesterId);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canFindAllUsers_shouldThrowNoSuchElementException_whenUserDoesNotExist() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(NoSuchElementException.class,
                () -> service.canFindAllUsers(requesterId));
    }

    @Test
    void canFindAllUsers_shouldThrowNullPointerException_whenRequesterIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> service.canFindAllUsers(null));
    }

    // =========================================================================
    // canFindUserById
    // =========================================================================

    @Test
    void canFindUserById_shouldReturnTrue_whenUserIsAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        UserId targetId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(user));
        when(policy.canFindUserById(user, targetId)).thenReturn(true);

        // ACT
        boolean result = service.canFindUserById(requesterId, targetId);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canFindUserById_shouldReturnFalse_whenUserIsNotAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        UserId targetId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(user));
        when(policy.canFindUserById(user, targetId)).thenReturn(false);

        // ACT
        boolean result = service.canFindUserById(requesterId, targetId);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canFindUserById_shouldThrowNoSuchElementException_whenRequesterDoesNotExist() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        UserId targetId = mock(UserId.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(NoSuchElementException.class,
                () -> service.canFindUserById(requesterId, targetId));
    }

    @Test
    void canFindUserById_shouldThrowNullPointerException_whenRequesterIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> service.canFindUserById(null, mock(UserId.class)));
    }

    @Test
    void canFindUserById_shouldThrowNullPointerException_whenTargetUserIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> service.canFindUserById(mock(UserId.class), null));
    }

    // =========================================================================
    // findByIDNew
    // =========================================================================

    @Test
    void findByIDNew_shouldReturnUser_whenRequesterIsAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        UserId targetId = mock(UserId.class);
        User requester = mock(User.class);
        User target = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(requester));
        when(policy.canFindUserById(requester, targetId)).thenReturn(true);
        when(userRepository.findByID(targetId)).thenReturn(Optional.of(target));

        // ACT
        User result = service.findByIDNew(targetId, requesterId);

        // ASSERT
        assertSame(target, result);
    }

    @Test
    void findByIDNew_shouldThrowAccessDeniedException_whenNotAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        UserId targetId = mock(UserId.class);
        User requester = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(requester));
        when(policy.canFindUserById(requester, targetId)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> service.findByIDNew(targetId, requesterId));
    }

    @Test
    void findByIDNew_shouldThrowNoSuchElementException_whenRequesterDoesNotExist() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        UserId targetId = mock(UserId.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(NoSuchElementException.class,
                () -> service.findByIDNew(targetId, requesterId));
    }

    @Test
    void findByIDNew_shouldThrowNoSuchElementException_whenTargetDoesNotExist() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        UserId targetId = mock(UserId.class);
        User requester = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(requester));
        when(policy.canFindUserById(requester, targetId)).thenReturn(true);
        when(userRepository.findByID(targetId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(NoSuchElementException.class,
                () -> service.findByIDNew(targetId, requesterId));
    }

    @Test
    void findByIDNew_shouldThrowNullPointerException_whenTargetUserIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> service.findByIDNew(null, mock(UserId.class)));
    }

    @Test
    void findByIDNew_shouldThrowNullPointerException_whenRequesterIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> service.findByIDNew(mock(UserId.class), null));
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnUsers_whenRequesterIsAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        User requester = mock(User.class);
        User user = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(requester));
        when(policy.canFindAllUsers(requester)).thenReturn(true);
        when(userRepository.findAll()).thenReturn(List.of(user));

        // ACT
        List<User> result = service.findAll(requesterId);

        // ASSERT
        assertEquals(1, result.size());
        assertSame(user, result.get(0));
    }

    @Test
    void findAll_shouldThrowAccessDeniedException_whenNotAuthorized() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        User requester = mock(User.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.of(requester));
        when(policy.canFindAllUsers(requester)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> service.findAll(requesterId));
    }

    @Test
    void findAll_shouldThrowNoSuchElementException_whenRequesterDoesNotExist() {

        // ARRANGE
        UserId requesterId = mock(UserId.class);
        when(userRepository.findByID(requesterId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(NoSuchElementException.class,
                () -> service.findAll(requesterId));
    }

    @Test
    void findAll_shouldThrowNullPointerException_whenRequesterIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> service.findAll(null));
    }
}