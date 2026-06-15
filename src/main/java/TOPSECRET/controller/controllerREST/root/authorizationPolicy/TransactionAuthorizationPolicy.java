package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy for operations related to transactions.
 *
 * <p>This component centralizes the authorization rules that determine whether
 * a user is allowed to perform transaction-related actions.</p>
 */
@Component
public class TransactionAuthorizationPolicy {

    /**
     * Determines whether the given user can create a transaction.
     *
     * <p>A user can create a transaction when they have the
     * {@link UserRole#NORMAL_USER} role.</p>
     *
     * @param user the user whose permissions should be checked
     * @return {@code true} if the user has permission to create a transaction;
     *         {@code false} otherwise
     */
    public boolean canCreateTransaction(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

    /**
     * Determines whether the given user can view a transaction by its identifier.
     *
     * <p>Both {@link UserRole#NORMAL_USER} and {@link UserRole#ADMIN} roles
     * are permitted to retrieve transactions.</p>
     *
     * @param user the user whose permissions should be checked
     * @return {@code true} if the user has permission to view a transaction;
     *         {@code false} otherwise
     */
    public boolean canViewTransaction(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER)
                || user.getUserRoles().contains(UserRole.ADMIN);
    }

    /**
     * Determines whether the given user can retrieve all transactions for a buyer.
     *
     * <p>Both {@link UserRole#NORMAL_USER} and {@link UserRole#ADMIN} roles
     * are permitted to retrieve transaction lists.</p>
     *
     * @param user the user whose permissions should be checked
     * @return {@code true} if the user has permission to view buyer transactions;
     *         {@code false} otherwise
     */
    public boolean canViewBuyerTransactions(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER)
                || user.getUserRoles().contains(UserRole.ADMIN);
    }
}