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
 * Unit tests for {@link PublicationSampleAuthorizationPolicy}.
 *
 * <p>Verifies the access control logic for operations related to publication samples,
 * ensuring that only users with the appropriate roles are granted authorization.</p>
 *
 * <p>Tests are organized into the following sections:</p>
 * <ul>
 * <li><b>canCreatePublicationSample</b> — creation authorization.</li>
 * <li><b>canFindByCollectionOfPublicationsId</b> — collection lookup authorization.</li>
 * <li><b>canFindPublicationsOwnedByUserId</b> — owner lookup authorization.</li>
 * <li><b>canFindPublicationsInLibrary</b> — library-assigned lookup authorization.</li>
 * <li><b>canFindPublicationsNotInLibrary</b> — non-library lookup authorization.</li>
 * <li><b>canFindByLibraryIdAndPublicationStatusNotIn</b> — status-filtered lookup authorization.</li>
 * <li><b>canFindAvailableForCollection</b> — collection eligibility lookup authorization.</li>
 * <li><b>canAddPublicationToLibrary</b> — library assignment authorization.</li>
 * <li><b>canAssignPublicationToCollection</b> — collection assignment authorization.</li>
 * <li><b>canFindPublicationsInPublicCollection</b> — public collection lookup authorization.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class PublicationSampleAuthorizationPolicyTest {

    @Mock
    private User authorizedUser;

    @Mock
    private User unauthorizedUser;

    @InjectMocks
    private PublicationSampleAuthorizationPolicy policy;

    // =========================================================================
    // canCreatePublicationSample
    // =========================================================================

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

    // =========================================================================
    // canFindByCollectionOfPublicationsId
    // =========================================================================

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

    // =========================================================================
    // canFindPublicationsOwnedByUserId
    // =========================================================================

    @Test
    void canFindPublicationsOwnedByUserId_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canFindPublicationsOwnedByUserId(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindPublicationsOwnedByUserId_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canFindPublicationsOwnedByUserId(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    // =========================================================================
    // canFindPublicationsInLibrary
    // =========================================================================

    @Test
    void canFindPublicationsInLibrary_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canFindPublicationsInLibrary(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindPublicationsInLibrary_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canFindPublicationsInLibrary(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    // =========================================================================
    // canFindPublicationsNotInLibrary
    // =========================================================================

    @Test
    void canFindPublicationsNotInLibrary_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canFindPublicationsNotInLibrary(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindPublicationsNotInLibrary_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canFindPublicationsNotInLibrary(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    // =========================================================================
    // canFindByLibraryIdAndPublicationStatusNotIn
    // =========================================================================

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

    // =========================================================================
    // canFindAvailableForCollection
    // =========================================================================

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

    // =========================================================================
    // canAddPublicationToLibrary
    // =========================================================================

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

    // =========================================================================
    // canAssignPublicationToCollection
    // =========================================================================

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

    // =========================================================================
    // canFindPublicationsInPublicCollection
    // =========================================================================

    @Test
    void canFindPublicationsInPublicCollection_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canFindPublicationsInPublicCollection(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindPublicationsInPublicCollection_shouldReturnTrue_whenUserIsAdmin() {
        // Arrange
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        boolean result = policy.canFindPublicationsInPublicCollection(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindPublicationsInPublicCollection_shouldReturnFalse_whenUserIsNotNormalUserOrAdmin() {
        // Arrange
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canFindPublicationsInPublicCollection(unauthorizedUser);

        // Assert
        assertFalse(result);
    }
}