package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy responsible for PublicationType-related permissions.
 *
 * <p>
 * Encapsulates authorization rules associated with PublicationType use cases,
 * such as creation permissions.
 * </p>
 */
@Component
public class PublicationTypeAuthorizationPolicy {

    /**
     * Determines whether the given user is allowed to create a PublicationType.
     *
     * <p>
     * Access is granted only to users with the {@link UserRole#ADMIN} role.
     * </p>
     *
     * @param user the user requesting the operation
     * @return {@code true} if the user has the ADMIN role;
     *         {@code false} otherwise
     */
    public boolean canCreatePublicationType(User user){
        return user.getUserRoles().contains(UserRole.ADMIN);
    }
}