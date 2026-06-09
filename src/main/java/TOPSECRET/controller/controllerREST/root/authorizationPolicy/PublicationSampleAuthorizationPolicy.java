package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy component responsible for validating access rights
 * for operations related to {@link TOPSECRET.domain.PublicationSample.PublicationSample}.
 *
 * <p>Ensures that only users with the appropriate roles are permitted to perform
 * specific actions such as creating, retrieving, or assigning publication samples.</p>
 *
 * <p>Policies are grouped into three categories:</p>
 * <ul>
 *   <li><b>Persistence</b> — creation of publication samples.</li>
 *   <li><b>Lookup — by owner</b> — retrieval of publications scoped to a specific user.</li>
 *   <li><b>Domain Operations</b> — library and collection assignment operations.</li>
 * </ul>
 */
@Component
public class PublicationSampleAuthorizationPolicy {

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * Checks if the user is authorized to create a new publication sample.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canCreatePublicationSample(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    // =========================================================================
    // Lookup — by associated identifiers
    // =========================================================================

    /**
     * Checks if the user is authorized to find publication samples associated with a specific collection.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canFindByCollectionOfPublicationsId(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    // =========================================================================
    // Lookup — by owner
    // =========================================================================

    /**
     * Checks if the user is authorized to find all publication samples they own.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canFindPublicationsOwnedByUserId(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to find their own publication samples that are assigned to a library.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canFindPublicationsInLibrary(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to find their own publication samples that are not assigned to any library.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canFindPublicationsNotInLibrary(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to find publication samples in their library,
     * excluding those with specific publication statuses.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canFindByLibraryIdAndPublicationStatusNotIn(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to find publication samples that are eligible
     * to be added to a collection.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canFindAvailableForCollection(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    // =========================================================================
    // Domain Operations
    // =========================================================================

    /**
     * Checks if the user is authorized to add a publication sample to a library.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canAddPublicationToLibrary(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to assign a publication sample to a collection.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canAssignPublicationToCollection(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }
}