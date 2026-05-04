package TOPSECRET.domain.User;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Factory responsible for creating valid {@link User} aggregate instances.
 * <p>
 * This factory centralizes the creation logic for {@code User} objects,
 * ensuring that all instances are created consistently and in a valid state
 * according to the domain business rules.
 * </p>
 */
@Component
public class UserFactory {
    /**
     * Creates a new {@link User} with a generated {@link UserId}.
     *
     * @param username username (min 5 non-space chars)
     * @param profile required profile
     * @param userRoles non-empty set of roles
     * @return new valid user
     * @throws IllegalArgumentException if validation fails
     */
    public User create(String username, Profile profile, Set<UserRole> userRoles) {
        return new User(username, profile, userRoles);
    }

    /**
     * Rebuilds an existing {@link User}.
     *
     * @param userId existing id
     * @param username username (min 5 non-space chars)
     * @param profile required profile
     * @param userRoles non-empty set of roles
     * @return new valid user
     * @throws IllegalArgumentException if validation fails
     */
    public User create(UserId userId, String username, Profile profile, Set<UserRole> userRoles) {
        return new User(userId, username, profile, userRoles);
    }
}