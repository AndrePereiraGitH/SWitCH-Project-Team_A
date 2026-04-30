package TOPSECRET.domain.User;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating valid {@link User} aggregate instances.
 * <p>
 * This factory centralizes the creation logic for {@code User} objects,
 * ensuring that all instances are created consistently and in a valid state
 * according to the domain business rules.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     User user = UserFactory.create("david123", contact, profile, UserRole.BUYER);
 * </pre>
 */
@Component
public class UserFactory {
    /**
     * Creates and returns a new {@link User} with the given attributes.
     * <p>
     * Delegates validation to the {@link User} constructor, which enforces
     * all business rules — such as non-null username with at least 5 characters,
     * non-null role, and non-null profile.
     * </p>
     *
     * @param username the username of the user; must not be {@code null}
     *                 and must contain at least 5 non-space characters
     * @param contact  the contact information of the user; may be {@code null}
     * @param profile  the profile of the user; must not be {@code null}
     * @param userRole the role assigned to the user; must not be {@code null}
     * @return a new valid {@link User} instance
     * @throws IllegalArgumentException if any business rule is violated
     */
    public User create(String username, Contact contact, Profile profile, UserRole userRole) {
        return new User(username, contact, profile, userRole);
    }

    /**
     * Creates and returns a new {@link User} with the given identifier and attributes.
     * <p>
     * Delegates validation to the {@link User} constructor, which enforces
     * all business rules — such as non-null userId, non-null username with at least
     * 5 characters, non-null role, and non-null profile.
     * </p>
     *
     * @param userId   the unique identifier of the user; must not be {@code null}
     * @param username the username of the user; must not be {@code null}
     *                 and must contain at least 5 non-space characters
     * @param contact  the contact information of the user; may be {@code null}
     * @param profile  the profile of the user; must not be {@code null}
     * @param userRole the role assigned to the user; must not be {@code null}
     * @return a new valid {@link User} instance
     * @throws IllegalArgumentException if any business rule is violated
     */
    public User create(UserId userId, String username, Contact contact, Profile profile, UserRole userRole) {
        return new User(userId, username, contact, profile, userRole);
    }
}