package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy responsible for validating permissions
 * related to bibliographical data operations.
 *
 * <p>This component centralizes access control rules for actions
 * involving bibliographical data resources.</p>
 */
@Component
public class BibliographicalDataAuthorizationPolicy {

    /**
     * Determines whether the specified user is allowed to create
     * bibliographical data.
     *
     * <p>Only users with the {@link UserRole#NORMAL_USER} role
     * are authorized to perform this operation.</p>
     *
     * @param user the user whose permissions are being evaluated
     * @return {@code true} if the user can create bibliographical data;
     *         {@code false} otherwise
     */
    public boolean canCreateBibliographicalData(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }
}
