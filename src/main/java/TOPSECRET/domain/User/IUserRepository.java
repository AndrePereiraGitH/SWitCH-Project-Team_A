package TOPSECRET.domain.User;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.Optional;

public interface IUserRepository {

    User createAndAdd(String username, Contact contact, Profile profile, UserRole userRole);
    boolean exists(String username);
    void add(User user);

    /**
     * Finds the unique identifier of a user by their username.
     *
     * @param username the username to search for; must not be null or blank
     * @return an {@link Optional} containing the {@link UserId} if a user with
     *         the given username exists, or {@link Optional#empty()} if not found
     * @throws IllegalArgumentException if {@code username} is null or blank
     */
    Optional<UserId> findUserIdByUsername(String username);


    /**
     * Finds the unique user by their ID.
     *
     * @param userID the userID to search for; must not be null
     * @return an {@link Optional} containing the {@link User} if a user with
     *         the given ID exists, or {@link Optional#empty()} if not found
     * @throws IllegalArgumentException if {@code userID} is null or blank
     */
    Optional<User> findUserByID(UserId userID);

}