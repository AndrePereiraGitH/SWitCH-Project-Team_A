package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy for {@link Auction}-related operations.
 *
 * <p>Defines which {@link UserRole}s are permitted to perform each auction operation.
 *
 * <p>If new roles are added in the future, each method must be reviewed and updated
 * explicitly to grant or deny access.</p>
 */
@Component
public class AuctionAuthorizationPolicy {

    /**
     * Determines whether the given user is allowed to create an auction.
     *
     * <p>Permitted roles: {@link UserRole#NORMAL_USER}, {@link UserRole#ADMIN}.</p>
     *
     * @param user the user requesting the operation
     * @return {@code true} if the user has permission to create an auction;
     *         {@code false} otherwise
     */
    public boolean canCreateAuction(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

    /**
     * Determines whether the given user is allowed to place a bid on an auction.
     *
     * <p>Permitted roles: {@link UserRole#NORMAL_USER} only.
     * Administrators are not permitted to place bids.</p>
     *
     * @param user the user requesting the operation
     * @return {@code true} if the user has permission to place a bid;
     *         {@code false} otherwise
     */
    public boolean canPlaceBid(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }
}
