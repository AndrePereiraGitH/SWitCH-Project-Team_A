package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy component responsible for validating access rights
 * for operations related to {@link TOPSECRET.domain.PublicationSample.PublicationSample}.
 * <p>
 * This class ensures that only users with the appropriate roles (currently {@code NORMAL_USER})
 * are permitted to perform specific actions such as creating, retrieving, or assigning publication samples.
 * </p>
 */
@Component
public class PublicationSampleAuthorizationPolicy {

    /**
     * Checks if the user is authorized to create a new publication sample.
     *
     * @param user The user attempting the operation.
     * @return {@code true} if the user has the NORMAL_USER role, {@code false} otherwise.
     */
    public boolean canCreatePublicationSample(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }


    /**
     * Checks if the user is authorized to find publication samples associated with a specific collection.
     *
     * @param user The user attempting the operation.
     * @return {@code true} if the user has the NORMAL_USER role, {@code false} otherwise.
     */
    public boolean canFindByCollectionOfPublicationsId(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to find their own publication samples that are currently assigned to a library.
     *
     * @param user The user attempting the operation.
     * @return {@code true} if the user has the NORMAL_USER role, {@code false} otherwise.
     */
    public boolean canFindByUserIdAndLibraryIdIsNotNull(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to find their own publication samples that are NOT assigned to any library.
     *
     * @param user The user attempting the operation.
     * @return {@code true} if the user has the NORMAL_USER role, {@code false} otherwise.
     */
    public boolean canFindByUserIdAndLibraryIdIsNull(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to find publication samples in a library, excluding specific statuses.
     *
     * @param user The user attempting the operation.
     * @return {@code true} if the user has the NORMAL_USER role, {@code false} otherwise.
     */
    public boolean canFindByLibraryIdAndPublicationStatusNotIn(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to find publication samples that are available to be added to a collection.
     *
     * @param user The user attempting the operation.
     * @return {@code true} if the user has the NORMAL_USER role, {@code false} otherwise.
     */
    public boolean canFindAvailableForCollection(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to add a publication sample to a library.
     *
     * @param user The user attempting the operation.
     * @return {@code true} if the user has the NORMAL_USER role, {@code false} otherwise.
     */
    public boolean canAddPublicationToLibrary(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }

    /**
     * Checks if the user is authorized to assign a publication sample to a collection.
     *
     * @param user The user attempting the operation.
     * @return {@code true} if the user has the NORMAL_USER role, {@code false} otherwise.
     */
    public boolean canAssignPublicationToCollection(User user) {
        boolean result = user.getUserRoles().contains(UserRole.NORMAL_USER);
        return result;
    }
}