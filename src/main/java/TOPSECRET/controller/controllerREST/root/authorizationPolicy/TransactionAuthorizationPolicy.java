package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.Transaction.Transaction;
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
     * Any registered user (NORMAL_USER) can create a transaction.
     */
    public boolean canCreateTransaction(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

    /**
     * Only the buyer (NORMAL_USER acting as buyer) or ADMIN can view
     * a transaction by ID or a list of transactions.
     * The seller check is handled at the controller/service level
     * since the link provider doesn't have transaction context here.
     */
    public boolean canViewTransaction(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER)
                || user.getUserRoles().contains(UserRole.ADMIN);
    }

    /**
     * A user can view a specific transaction if they are an admin
     * or if they are the buyer of that transaction.
     */
    public boolean canViewTransaction(User user, Transaction transaction) {
        boolean isAdmin = user.getUserRoles().contains(UserRole.ADMIN);
        boolean isBuyer = transaction.getBuyerId().equals(user.id());
        return isAdmin || isBuyer;
    }

    /**
     * Determines whether the given user can retrieve all transactions in the system.
     *
     * <p>Only users with the {@link UserRole#ADMIN} role are permitted
     * to retrieve the full list of transactions.</p>
     *
     * @param user the user whose permissions should be checked
     * @return {@code true} if the user has permission to view all transactions;
     *         {@code false} otherwise
     */
    public boolean canViewAllTransactions(User user) {
        return user.getUserRoles().contains(UserRole.ADMIN);
    }
}