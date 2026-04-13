package TOPSECRET.domain.User;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} aggregate persistence.
 * <p>
 * Provides operations for creating and querying users within the domain.
 * Implementations are responsible for ensuring uniqueness constraints on usernames.
 * </p>
 */
public interface IUserRepository {

    /**
     * Checks whether a user with the given username already exists in the repository.
     *
     * @param username the username to check; must not be null or blank
     * @return {@code true} if a user with the given username exists, {@code false} otherwise
     * @throws IllegalArgumentException if {@code username} is null or blank
     */
    boolean exists(String username);

    /**
     * Creates a new user in the repository with the provided details.
     *
     * @param username the desired username for the new user; must not be null, blank, or already taken
     * @param contact  the contact information associated with the user; must not be null
     * @param profile  the profile information associated with the user; must not be null
     * @param userRole the role to assign to the user; must not be null
     * @return the {@link UserId} assigned to the newly created user
     * @throws IllegalArgumentException if any parameter is null, or if {@code username} is blank
     * @throws IllegalStateException    if a user with the given username already exists
     */
    User createAndAdd(String username, Contact contact, Profile profile, UserRole userRole);

    /**
     * Finds the unique user by their ID.
     *
     * @param userID the userID to search for; must not be null
     * @return an {@link Optional} containing the {@link User} if a user with
     *         the given ID exists, or {@link Optional#empty()} if not found
     * @throws IllegalArgumentException if {@code userID} is null or blank
     */
    Optional<User> findUserByID(UserId userID);

    /**
     * Finds the unique identifier of a user by their username.
     *
     * @param username the username to search for; must not be null or blank
     * @return an {@link Optional} containing the {@link UserId} if a user with
     *         the given username exists, or {@link Optional#empty()} if not found
     * @throws IllegalArgumentException if {@code username} is null or blank
     */
    Optional<UserId> findUserIdByUsername(String username);

}