package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy responsible for validating permissions related to
 * Edition operations.
 *
 * <p>This class defines the authorization rules used to determine whether
 * a user is allowed to perform specific actions over Edition objects.</p>
 */
@Component
public class EditionAuthorizationPolicy {

    /**
     * Verifies whether a user has permission to register a new Edition.
     *
     * <p>Any authenticated user (both {@link UserRole#ADMIN} and
     * {@link UserRole#NORMAL_USER}) may create an Edition, as it is
     * required for publication registration (US010).</p>
     *
     * @param user the user whose permissions are being validated
     * @return {@code true} if the user has any valid role;
     *         {@code false} otherwise
     */
    public boolean canCreateEdition(User user) {
        return user.getUserRoles().contains(UserRole.ADMIN)
                || user.getUserRoles().contains(UserRole.NORMAL_USER);
    }
}
