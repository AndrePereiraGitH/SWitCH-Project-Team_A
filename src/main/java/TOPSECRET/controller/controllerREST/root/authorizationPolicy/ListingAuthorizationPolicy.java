package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy for operations related to listings.
 *
 * <p>This component centralizes the authorization rules that determine whether
 * a user is allowed to perform listing-related actions.</p>
 */
@Component
public class ListingAuthorizationPolicy {

    /**
     * Determines whether the given user can create a listing.
     *
     * <p>A user can create a listing when they have the
     * {@link UserRole#NORMAL_USER} role.</p>
     *
     * @param user the user whose permissions should be checked
     * @return {@code true} if the user has permission to create a listing;
     *         {@code false} otherwise
     */
    public boolean canCreateListing(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

    /**
     * Determines whether the given user can buy a listing.
     *
     * <p>A user can buy a listing when they have the
     * {@link UserRole#NORMAL_USER} role.</p>
     *
     * @param user the user whose permissions should be checked
     * @return {@code true} if the user has permission to buy a listing;
     *         {@code false} otherwise
     */
    public boolean canBuyListing(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

}
