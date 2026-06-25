package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
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
// canViewAllTransactions
// =========================================================================

    @Test
    void canViewAllTransactions_shouldReturnTrue_whenUserIsAdmin() {

        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = sut.canViewAllTransactions(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canViewAllTransactions_shouldReturnFalse_whenUserIsNormalUser() {

        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canViewAllTransactions(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canViewAllTransactions_shouldReturnFalse_whenUserHasNoRoles() {

        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of());

        // ACT
        boolean result = sut.canViewAllTransactions(user);

        // ASSERT
        assertFalse(result);
    }

    // =========================================================================
    // canViewTransaction(User, Transaction)
    // =========================================================================

    @Test
    void canViewTransaction_withTransaction_shouldReturnTrue_whenUserIsAdmin() {

        // ARRANGE
        User user = mock(User.class);
        TOPSECRET.domain.Transaction.Transaction transaction = mock(TOPSECRET.domain.Transaction.Transaction.class);
        UserId userId = new TOPSECRET.domain.ValueObjects.UserId("admin-1");
        UserId buyerId = new TOPSECRET.domain.ValueObjects.UserId("buyer-1");
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));
        when(user.id()).thenReturn(userId);
        when(transaction.getBuyerId()).thenReturn(buyerId);

        // ACT
        boolean result = sut.canViewTransaction(user, transaction);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canViewTransaction_withTransaction_shouldReturnTrue_whenUserIsBuyer() {

        // ARRANGE
        User user = mock(User.class);
        TOPSECRET.domain.Transaction.Transaction transaction = mock(TOPSECRET.domain.Transaction.Transaction.class);
        UserId buyerId = new TOPSECRET.domain.ValueObjects.UserId("buyer-1");
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));
        when(user.id()).thenReturn(buyerId);
        when(transaction.getBuyerId()).thenReturn(buyerId);

        // ACT
        boolean result = sut.canViewTransaction(user, transaction);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canViewTransaction_withTransaction_shouldReturnFalse_whenUserIsNeitherAdminNorBuyer() {

        // ARRANGE
        User user = mock(User.class);
        TOPSECRET.domain.Transaction.Transaction transaction = mock(TOPSECRET.domain.Transaction.Transaction.class);
        UserId userId = new TOPSECRET.domain.ValueObjects.UserId("other-user");
        UserId buyerId = new TOPSECRET.domain.ValueObjects.UserId("buyer-1");
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));
        when(user.id()).thenReturn(userId);
        when(transaction.getBuyerId()).thenReturn(buyerId);

        // ACT
        boolean result = sut.canViewTransaction(user, transaction);

        // ASSERT
        assertFalse(result);
    }
}