package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

/**
 * Authorization policy responsible for determining whether a user is allowed
 * to perform collection of publications-related operations.
 *
 * <p>This policy encapsulates the business rule that only users with the
 * {@link UserRole#NORMAL_USER} role are permitted to
 * manage collections of publications.</p>
 */
@Component
public class CollectionOfPublicationsAuthorizationPolicy {

    /**
     * Determines whether the given user is authorized to create a collection of publications.
     *
     * @param user the user requesting the operation; must not be {@code null}
     * @return {@code true} if the user holds the {@link UserRole#NORMAL_USER} role,
     *         {@code false} otherwise
     * @throws NullPointerException if {@code user} is {@code null}
     */
    public boolean canCreateCollectionOfPublications(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

    /**
     * Determines whether the given user is authorized to share a collection of publications.
     *
     * @param user the user requesting the operation; must not be {@code null}
     * @return {@code true} if the user holds the {@link UserRole#NORMAL_USER} role,
     *         {@code false} otherwise
     * @throws NullPointerException if {@code user} is {@code null}
     */
    public boolean canShareCollectionOfPublications(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

    /**
     * Determines whether the given user is authorized to make a collection of publications
     * public until a specified date and time.
     *
     * @param user the user requesting the operation; must not be {@code null}
     * @return {@code true} if the user holds the {@link UserRole#NORMAL_USER} role,
     *         {@code false} otherwise
     * @throws NullPointerException if {@code user} is {@code null}
     */
    public boolean canMakeCollectionPublicUntil(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }

    /**
     * Determines whether the given user is authorized to view a collection of publications.
     *
     * @param user the user requesting the operation; must not be {@code null}
     * @return {@code true} if the user holds the {@link UserRole#NORMAL_USER} role,
     *         {@code false} otherwise
     * @throws NullPointerException if {@code user} is {@code null}
     */
    public boolean canViewCollectionOfPublications(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }
}
