package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy responsible for User-related permissions.
 *
 * <p>Encapsulates the business rules that determine whether a given user
 * is allowed to perform operations on User resources.</p>
 */

@Component
public class UserAuthorizationPolicy {

    /**
     * Determines whether the given user is allowed to register a new user.
     *
     * <p>Only users with the {@link UserRole#ADMIN} role are permitted
     * to register new users in the system.</p>
     *
     * @param user the user requesting the operation; must not be {@code null}
     * @return {@code true} if the user has the ADMIN role; {@code false} otherwise
     */

    public boolean canRegisterUser(User user){
        return user.getUserRoles().contains(UserRole.ADMIN);
    }
}
