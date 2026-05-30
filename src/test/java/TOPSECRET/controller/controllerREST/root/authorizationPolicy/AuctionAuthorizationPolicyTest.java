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
 * Unit tests for {@link AuctionAuthorizationPolicy}.
 *
 * <p>Verifies that each authorization method correctly grants or denies access
 * based on the user's {@link UserRole}, including:</p>
 * <ul>
 *     <li>{@code canCreateAuction} — permitted for {@link UserRole#NORMAL_USER}
 *         and {@link UserRole#ADMIN};</li>
 *     <li>{@code canPlaceBid} — permitted exclusively for {@link UserRole#NORMAL_USER};
 *         denied for {@link UserRole#ADMIN}.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class AuctionAuthorizationPolicyTest { ;

    @Mock
    private User userDouble;

    @InjectMocks
    private AuctionAuthorizationPolicy policy;

    // =============================================
    // ========== canCreateAuction =================
    // =============================================

    @Test
    void canCreateAuction_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act + Assert
        assertTrue(policy.canCreateAuction(userDouble));
    }

    @Test
    void canCreateAuction_shouldReturnFalse_whenUserIsAdmin() {
        // Arrange
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act + Assert
        assertFalse(policy.canCreateAuction(userDouble));
    }

    @Test
    void canCreateAuction_shouldReturnFalse_whenUserHasUnknownRole() {
        // Arrange
        when(userDouble.getUserRoles()).thenReturn(Set.of());

        // Act + Assert
        assertFalse(policy.canCreateAuction(userDouble));
    }

    // =============================================
    // ========== canPlaceBid ======================
    // =============================================

    @Test
    void canPlaceBid_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act + Assert
        assertTrue(policy.canPlaceBid(userDouble));
    }

    @Test
    void canPlaceBid_shouldReturnFalse_whenUserIsAdmin() {
        // Arrange
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act + Assert
        assertFalse(policy.canPlaceBid(userDouble));
    }

    @Test
    void canPlaceBid_shouldReturnFalse_whenUserHasUnknownRole() {
        // Arrange
        when(userDouble.getUserRoles()).thenReturn(Set.of());

        // Act + Assert
        assertFalse(policy.canPlaceBid(userDouble));
    }

}