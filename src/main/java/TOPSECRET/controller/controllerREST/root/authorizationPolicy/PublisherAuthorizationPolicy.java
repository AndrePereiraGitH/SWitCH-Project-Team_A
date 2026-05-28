package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy responsible for determining whether a user is allowed
 * to perform publisher-related operations.
 *
 * <p>This policy encapsulates the business rule that only users with the
 * {@link TOPSECRET.domain.Enum.UserRole#NORMAL_USER} role are permitted to
 * create publishers.</p>
 */

@Component
public class PublisherAuthorizationPolicy {

    /**
     * Determines whether the given user is authorized to create a publisher.
     *
     * @param user the user requesting the operation; must not be {@code null}
     * @return {@code true} if the user holds the {@link UserRole#NORMAL_USER} role,
     *         {@code false} otherwise
     * @throws NullPointerException if {@code user} is {@code null}
     */
    public boolean canCreatPublisher(User user) {
        return user.getUserRoles().contains(UserRole.ADMIN);
    }
}