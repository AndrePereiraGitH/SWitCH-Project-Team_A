package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link PublicationSampleAuthorizationPolicy} class.
 * <p>
 * This test suite verifies the access control logic for operations related to
 * publication samples. It ensures that only users possessing the
 * {@link UserRole#NORMAL_USER} role are granted authorization to perform
 * specific actions, such as creating, retrieving, and assigning publication samples.
 * </p>
 * <p>
 * The tests utilize Mockito to simulate both authorized and unauthorized users
 * by stubbing {@link User#getUserRoles()} directly inside each test's Arrange
 * block. This keeps stubs local to the tests that need them, satisfying
 * {@link MockitoExtension}'s strict stubbing rules and avoiding
 * {@code UnnecessaryStubbingException}.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class PublicationSampleAuthorizationPolicyTest {

    @Mock
    private User authorizedUser;

    @Mock
    private User unauthorizedUser;

    @InjectMocks
    private PublicationSampleAuthorizationPolicy policy;

    @Test
    void canCreatePublicationSample_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canCreatePublicationSample(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canCreatePublicationSample_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canCreatePublicationSample(unauthorizedUser);

        // Assert
        assertFalse(result);
    }


    @Test
    void canFindByCollectionOfPublicationsId_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canFindByCollectionOfPublicationsId(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindByCollectionOfPublicationsId_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canFindByCollectionOfPublicationsId(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindByUserIdAndLibraryIdIsNotNull_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canFindByUserIdAndLibraryIdIsNotNull(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindByUserIdAndLibraryIdIsNotNull_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canFindByUserIdAndLibraryIdIsNotNull(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindByUserIdAndLibraryIdIsNull_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canFindByUserIdAndLibraryIdIsNull(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindByUserIdAndLibraryIdIsNull_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canFindByUserIdAndLibraryIdIsNull(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindByLibraryIdAndPublicationStatusNotIn_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canFindByLibraryIdAndPublicationStatusNotIn(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindByLibraryIdAndPublicationStatusNotIn_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canFindByLibraryIdAndPublicationStatusNotIn(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindAvailableForCollection_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canFindAvailableForCollection(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindAvailableForCollection_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canFindAvailableForCollection(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canAddPublicationToLibrary_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canAddPublicationToLibrary(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canAddPublicationToLibrary_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canAddPublicationToLibrary(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canAssignPublicationToCollection_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canAssignPublicationToCollection(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canAssignPublicationToCollection_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canAssignPublicationToCollection(unauthorizedUser);

        // Assert
        assertFalse(result);
    }
}