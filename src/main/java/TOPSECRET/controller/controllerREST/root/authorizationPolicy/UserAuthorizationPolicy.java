package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Component;

/**
 * Authorization policy responsible for User-related permissions.
 *
 * <p>Encapsulates the business rules that determine whether a given user
 * is allowed to perform operations on {@link User} resources.</p>
 */
@Component
public class UserAuthorizationPolicy {

    /**
     * Determines whether the given user has the administrator role.
     *
     * @param user the user to verify; must not be {@code null}
     * @return {@code true} if the user has the {@link UserRole#ADMIN} role;
     *         {@code false} otherwise
     */
    public boolean isAdmin(User user) {
        return user.getUserRoles().contains(UserRole.ADMIN);
    }

    /**
     * Determines whether the requester is allowed to register a new user.
     *
     * <p>Only users with the {@link UserRole#ADMIN} role are allowed
     * to register new users in the system.</p>
     *
     * @param requester the user requesting the operation; must not be {@code null}
     * @return {@code true} if the requester has the {@link UserRole#ADMIN} role;
     *         {@code false} otherwise
     */
    public boolean canRegisterUser(User requester){
        return isAdmin(requester);
    }

    /**
     * Determines whether the requester is allowed to retrieve all users.
     *
     * <p>Only users with the {@link UserRole#ADMIN} role are allowed
     * to access the full list of users.</p>
     *
     * @param requester the user requesting the operation; must not be {@code null}
     * @return {@code true} if the requester has the {@link UserRole#ADMIN} role;
     *         {@code false} otherwise
     */
    public boolean canFindAllUsers(User requester) {
        return isAdmin(requester);
    }

    /**
     * Determines whether the requester is allowed to retrieve a specific user by ID.
     *
     * <p>Administrators can retrieve any user. Non-administrator users can only
     * retrieve their own user resource.</p>
     *
     * @param requester the user requesting the operation; must not be {@code null}
     * @param targetUserId the ID of the user resource being requested; must not be {@code null}
     * @return {@code true} if the requester is an administrator or is requesting
     *         their own user resource; {@code false} otherwise
     */
    public boolean canFindUserById(User requester, UserId targetUserId) {
        return isAdmin(requester) || requester.id().equals(targetUserId);
    }
}
