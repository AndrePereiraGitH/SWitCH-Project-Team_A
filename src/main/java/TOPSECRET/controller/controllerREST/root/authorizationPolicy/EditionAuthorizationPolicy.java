package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy component responsible for validating access rights
 * for operations related to {@link TOPSECRET.domain.Edition.Edition}.
 *
 * <p>Ensures that only users with the appropriate roles are permitted to perform
 * specific actions such as creating editions.</p>
 *
 * <p>Policies are grouped into two categories:</p>
 * <ul>
 *   <li><b>Persistence</b> — creation of editions.</li>
 *   <li><b>Lookup</b> — retrieval of editions (public; no policy required).</li>
 * </ul>
 */
@Component
public class EditionAuthorizationPolicy {

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * Checks if the user is authorized to register a new edition.
     *
     * <p>Any authenticated user (both {@link UserRole#ADMIN} and
     * {@link UserRole#NORMAL_USER}) may create an edition, as it is
     * required for publication registration (US010).</p>
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code ADMIN} or {@code NORMAL_USER} role,
     *         {@code false} otherwise
     */
    public boolean canCreateEdition(User user) {
        return user.getUserRoles().contains(UserRole.ADMIN)
                || user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

    /**
     * Determines whether the given user is authorized to retrieve editions
     * by publisher identifier.
     *
     * <p>Access is granted only to users with the
     * {@link UserRole#NORMAL_USER} role.</p>
     *
     * @param user the user requesting the operation
     * @return {@code true} if the user has permission to retrieve editions
     *         by publisher ID; {@code false} otherwise
     */
    public boolean canFindByPublisherId(User user) {
        boolean result = user.getUserRoles().contains(UserRole.ADMIN)
                || user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Determines whether the given user is authorized to retrieve editions
     * by publication type identifier.
     *
     * <p>Access is granted only to users with the
     * {@link UserRole#NORMAL_USER} role.</p>
     *
     * @param user the user requesting the operation
     * @return {@code true} if the user has permission to retrieve editions
     *         by publication type ID; {@code false} otherwise
     */
    public boolean canFindByPublicationTypeId(User user) {
        boolean result = user.getUserRoles().contains(UserRole.ADMIN)
                || user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }
}
