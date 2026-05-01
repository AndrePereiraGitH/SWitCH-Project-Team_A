package TOPSECRET.domain.User;


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
     * Persists the given {@link User} to the in-memory list.
     *
     * @param user the user to save; must not be null
     * @return the saved {@link User} instance
     */
     User save(User user);
    /**
     * Checks whether a user with the given username already exists in the repository.
     *
     * @param username the username to check; must not be null or blank
     * @return {@code true} if a user with the given username exists, {@code false} otherwise
     * @throws IllegalArgumentException if {@code username} is null or blank
     */
    boolean existsByUsername(String username);

    /**
     * Finds the unique user by their ID.
     *
     * @param userID the userID to search for; must not be null
     * @return an {@link Optional} containing the {@link User} if a user with
     *         the given ID exists, or {@link Optional#empty()} if not found
     * @throws IllegalArgumentException if {@code userID} is null or blank
     */
    Optional<User> findByID(UserId userID);

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