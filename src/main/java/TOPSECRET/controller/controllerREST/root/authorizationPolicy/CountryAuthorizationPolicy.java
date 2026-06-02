package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy responsible for validating permissions related to
 * Country operations.
 *
 * <p>This class defines the authorization rules used to determine whether
 * a user is allowed to perform specific actions over Country objects.</p>
 */
@Component
public class CountryAuthorizationPolicy {

    /**
     * Verifies whether a user has permission to register a Country.
     *
     * <p>Only users with the {@link UserRole#ADMIN} role are allowed to
     * register a Country.</p>
     *
     * @param user the user whose permissions are being validated
     * @return {@code true} if the user has the ADMIN role;
     *         {@code false} otherwise
     */
    public boolean canRegisterCountry(User user) {

        return user.getUserRoles().contains(UserRole.ADMIN);
    }
}