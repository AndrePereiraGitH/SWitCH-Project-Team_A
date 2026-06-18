package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy component responsible for validating access rights
 * for operations related to {@link TOPSECRET.domain.Author.Author}.
 *
 * <p>Ensures that only users with the appropriate roles are permitted to perform
 * specific actions such as creating an author.</p>
 *
 * <p>Currently, this class governs a single category:</p>
 * <ul>
 *   <li><b>Persistence</b> — creation of authors.</li>
 * </ul>
 *
 * <p>Retrieval of authors is public and does not require authorization.</p>
 */
@Component
public class AuthorAuthorizationPolicy {

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * Checks if the user is authorized to register a new author.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code ADMIN} or {@code NORMAL_USER} role,
     *         {@code false} otherwise
     */
    public boolean canCreateAuthor(User user) {
        return user.getUserRoles().contains(UserRole.ADMIN)
                || user.getUserRoles().contains(UserRole.NORMAL_USER);
    }
}