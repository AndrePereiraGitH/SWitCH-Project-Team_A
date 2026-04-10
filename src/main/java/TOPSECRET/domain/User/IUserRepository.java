package TOPSECRET.domain.User;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.Optional;

public interface IUserRepository {

    boolean exists(String username);
    UserId create(String username, Contact contact, Profile profile, UserRole userRole);

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