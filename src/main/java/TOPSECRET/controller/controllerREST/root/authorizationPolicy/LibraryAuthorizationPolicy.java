package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Component;
/**
 * Authorization policy component responsible for validating access rights
 * for operations related to {@link TOPSECRET.domain.Library.Library}.
 *
 * <p>Ensures that only users with the appropriate roles are permitted to perform
 * specific actions such as creating or retrieving libraries.</p>
 *
 * <p>Policies are grouped into two categories:</p>
 * <ul>
 *   <li><b>Persistence</b> — creation of libraries.</li>
 *   <li><b>Lookup</b> — retrieval of libraries, scoped by ownership or admin role.</li>
 * </ul>
 */
@Component
public class LibraryAuthorizationPolicy {


    /**
     * Checks if the user is authorized to create a new library.
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code NORMAL_USER} role, {@code false} otherwise
     */
    public boolean canCreateLibrary(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

    /**
     * Checks if the user is authorized to retrieve all libraries in the system.
     *
     * <p>This operation is restricted to administrators only.</p>
     *
     * @param user the user attempting the operation; must not be {@code null}
     * @return {@code true} if the user has the {@code ADMIN} role, {@code false} otherwise
     */
    public boolean canGetAllLibrary(User user) {
        return user.getUserRoles().contains(UserRole.ADMIN);
    }

    /**
     * Checks if the user is authorized to retrieve a specific library.
     *
     * <p>Access is granted if the requesting user is the owner of the library
     * or has the {@code ADMIN} role.</p>
     *
     * @param requestingUser the user attempting the operation; must not be {@code null}
     * @param ownerId        the identifier of the library owner; must not be {@code null}
     * @return {@code true} if the requesting user is the owner or has the {@code ADMIN} role,
     *         {@code false} otherwise
     */
    public boolean canGetLibrary(User requestingUser, UserId ownerId) {
        boolean isOwner = requestingUser.id().equals(ownerId);
        boolean isAdmin = requestingUser.getUserRoles().contains(UserRole.ADMIN);
        return isOwner || isAdmin;
    }
}
