package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy responsible for verifying permissions for City operations.
 *
 * <p>This class determines the allowance for a user to execute specific actions over City objects.</p>
 */
@Component
public class CityAuthorizationPolicy {

    /**
     * Verifies if a user has permission to register a City.
     *
     * <p>Only users with the {@link UserRole#ADMIN} role are allowed to
     * create a City.</p>
     *
     * @param user the user whose permissions are being validated
     * @return {@code true} if the user has the ADMIN role;
     *         {@code false} otherwise
     */
    public boolean canCreateCity(User user){
        return user.getUserRoles().contains(UserRole.ADMIN);
    }
}