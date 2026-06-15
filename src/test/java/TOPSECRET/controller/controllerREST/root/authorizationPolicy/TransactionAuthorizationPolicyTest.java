package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TransactionAuthorizationPolicy}.
 *
 * <p>Verifies the authorization rules for transaction-related operations:
 * creation, viewing by ID, and viewing by buyer.</p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionAuthorizationPolicyTest {

    @InjectMocks
    private TransactionAuthorizationPolicy sut;

    // =========================================================================
    // canCreateTransaction
    // =========================================================================

    @Test
    void canCreateTransaction_shouldReturnTrue_whenUserIsNormalUser() {

        // Arrange
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = sut.canCreateTransaction(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void canCreateTransaction_shouldReturnFalse_whenUserIsAdmin() {

        // Arrange
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        boolean result = sut.canCreateTransaction(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void canCreateTransaction_shouldReturnFalse_whenUserHasNoRoles() {

        // Arrange
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = sut.canCreateTransaction(user);

        // Assert
        assertFalse(result);
    }

    // =========================================================================
    // canViewTransaction
    // =========================================================================

    @Test
    void canViewTransaction_shouldReturnTrue_whenUserIsNormalUser() {

        // Arrange
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = sut.canViewTransaction(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void canViewTransaction_shouldReturnTrue_whenUserIsAdmin() {

        // Arrange
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        boolean result = sut.canViewTransaction(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void canViewTransaction_shouldReturnFalse_whenUserHasNoRoles() {

        // Arrange
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = sut.canViewTransaction(user);

        // Assert
        assertFalse(result);
    }

    // =========================================================================
    // canViewBuyerTransactions
    // =========================================================================

    @Test
    void canViewBuyerTransactions_shouldReturnTrue_whenUserIsNormalUser() {

        // Arrange
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = sut.canViewBuyerTransactions(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void canViewBuyerTransactions_shouldReturnTrue_whenUserIsAdmin() {

        // Arrange
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        boolean result = sut.canViewBuyerTransactions(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void canViewBuyerTransactions_shouldReturnFalse_whenUserHasNoRoles() {

        // Arrange
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = sut.canViewBuyerTransactions(user);

        // Assert
        assertFalse(result);
    }
}