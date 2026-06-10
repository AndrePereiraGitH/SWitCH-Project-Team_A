package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy component responsible for validating access rights
 * for operations related to {@link TOPSECRET.domain.Publisher.Publisher}.
 *
 * <p>Ensures that only users with the appropriate roles are permitted to perform
 * specific actions such as creating or retrieving publishers.</p>
 *
 * <p>Policies are grouped into two categories:</p>
 * <ul>
 *   <li><b>Persistence</b> — creation of publishers.</li>
 *   <li><b>Lookup</b> — retrieval of publishers.</li>
 * </ul>
 */
@Component
public class PublisherAuthorizationPolicy {

    /**
     * Checks if the user is authorized to create a new publisher.
     *
     * <p>This operation is restricted to administrators only.</p>
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code ADMIN} role, {@code false} otherwise
     */
    public boolean canCreatePublisher(User user) {
        return user.getUserRoles().contains(UserRole.ADMIN);
    }

}