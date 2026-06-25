package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.UserAuthorizationPolicy;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * Service responsible for user-related application operations.
 *
 * <p>Coordinates the creation of {@link User} domain objects through
 * {@link UserFactory}, authorization via {@link UserAuthorizationPolicy},
 * and persistence through {@link IUserRepository}.</p>
 */
@Service
public class UserService {

    private final UserFactory userFactory;
    private final IUserRepository userRepository;
    private final UserAuthorizationPolicy policy;

    /**
     * Creates an instance of {@code UserService}.
     *
     * @param userFactory    the factory used to create {@link User} domain objects; must not be {@code null}
     * @param userRepository the repository used to persist and retrieve users; must not be {@code null}
     * @param policy         the authorization policy for user operations; must not be {@code null}
     */
    public UserService(UserFactory userFactory,
                       IUserRepository userRepository,
                       UserAuthorizationPolicy policy) {
        this.userFactory = Objects.requireNonNull(userFactory, "UserFactory cannot be null.");
        this.userRepository = Objects.requireNonNull(userRepository, "UserRepository cannot be null.");
        this.policy = Objects.requireNonNull(policy, "UserAuthorizationPolicy cannot be null.");
    }

    /**
     * Creates a new {@link User} and persists it.
     *
     * <p>Validates that the requester exists and has permission to register users
     * before delegating creation to {@link UserFactory} and persistence to
     * {@link IUserRepository}.</p>
     *
     * @param username    the username of the new user
     * @param profile     the profile of the new user
     * @param userRoles   the roles assigned to the new user
     * @param requesterId the ID of the user performing the registration
     * @return the persisted {@link User}
     * @throws IllegalArgumentException       if the requester does not exist
     * @throws AccessDeniedException          if the requester lacks permission
     * @throws InstanceAlreadyExistsException if a user with the same identity already exists
     */
    @Transactional
    public User createAndSave(String username, Profile profile, Set<UserRole> userRoles, UserId requesterId)
            throws InstanceAlreadyExistsException {

        User requester = userRepository.findByID(requesterId)
                .orElseThrow(() -> new NoSuchElementException("Requester not found: " + requesterId));

        if (!policy.canRegisterUser(requester)) {
            throw new AccessDeniedException("You do not have permission to register users.");
        }

        User user = userFactory.create(username, profile, userRoles);
        return userRepository.save(user);
    }



    // =========================================================================
    // Lookup
    // =========================================================================

    /**
     *
     * ======= TO BE REMOVED ========================
     *
     */
    public User findByID(UserId targetUserId) {
        Objects.requireNonNull(targetUserId, "UserId cannot be null.");

        return userRepository.findByID(targetUserId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + targetUserId));
    }


    /**
     * Finds a user by their unique identifier.
     *
     * @param targetUserId the identifier to search for; must not be {@code null}
     * @return the {@link User} with the given identifier
     * @throws NullPointerException     if {@code userId} is {@code null}
     * @throws NoSuchElementException if no user exists with the given identifier
     */
    public User findByIDNew(UserId targetUserId, UserId requesterId) {
        Objects.requireNonNull(targetUserId, "UserId cannot be null.");
        Objects.requireNonNull(requesterId, "RequesterId cannot be null");

        User requester = userRepository.findByID(requesterId)
                .orElseThrow(() -> new NoSuchElementException("Requester not found: " + requesterId));

        if (!policy.canFindUserById(requester, targetUserId)) {
            throw new AccessDeniedException("You do not have permission to find this user.");
        }

        return userRepository.findByID(targetUserId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + targetUserId));
    }

    /**
     * Returns all users in the system.
     *
     * @return a list of all {@link User} instances; never {@code null}, may be empty
     */
    public List<User> findAll(UserId requesterId) {
        Objects.requireNonNull(requesterId, "Requester UserId cannot be null.");

        User requester = userRepository.findByID(requesterId)
                .orElseThrow(() -> new NoSuchElementException("Requester not found: " + requesterId));

        if (!policy.canFindAllUsers(requester)) {
            throw new AccessDeniedException("You do not have permission to list all users.");
        }

        return userRepository.findAll();
    }

    /**
     * Checks whether a user with the given username already exists.
     *
     * @param username the username to check; must not be {@code null}
     * @return {@code true} if a user with the given username exists, {@code false} otherwise
     * @throws NullPointerException if {@code username} is {@code null}
     */
    public boolean existsByUsername(String username) {
        Objects.requireNonNull(username, "Username cannot be null.");
        return userRepository.existsByUsername(username);
    }

    /**
     * Finds the unique identifier of a user by their username.
     *
     * @param username the username to search for; must not be {@code null}
     * @return the {@link UserId} of the user with the given username
     * @throws NullPointerException     if {@code username} is {@code null}
     * @throws IllegalArgumentException if no user exists with the given username
     */
    public UserId findIdByUsername(String username) {
        Objects.requireNonNull(username, "Username cannot be null.");
        return userRepository.findIdByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }

    /**
     * Checks whether the user with the given ID is allowed to register a new user.
     *
     * @param requesterId the identifier of the requesting user; must not be {@code null}
     * @return {@code true} if the user has permission to register users
     * @throws NoSuchElementException if no user exists with the given identifier
     */
    public boolean canRegisterUser(UserId requesterId) {
        Objects.requireNonNull(requesterId, "requesterId cannot be null");
        User user = userRepository.findByID(requesterId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + requesterId));
        return policy.canRegisterUser(user);
    }

    /**
     * Checks whether the user with the given ID is allowed to list all users.
     *
     * @param requesterId the identifier of the requesting user; must not be {@code null}
     * @return {@code true} if the user has permission to list all users
     * @throws NoSuchElementException if no user exists with the given identifier
     */
    public boolean canFindAllUsers(UserId requesterId) {
        Objects.requireNonNull(requesterId, "requesterId cannot be null");
        User user = userRepository.findByID(requesterId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + requesterId));
        return policy.canFindAllUsers(user);
    }

    /**
     * Checks whether the requesting user is allowed to view the target user's data.
     *
     * @param requesterId  the identifier of the requesting user; must not be {@code null}
     * @param targetUserId the identifier of the target user; must not be {@code null}
     * @return {@code true} if the requester is the target user or has admin permission
     * @throws NoSuchElementException if no user exists with the given requester identifier
     */
    public boolean canFindUserById(UserId requesterId, UserId targetUserId) {
        Objects.requireNonNull(requesterId, "requesterId cannot be null");
        Objects.requireNonNull(targetUserId, "targetUserId cannot be null");
        User user = userRepository.findByID(requesterId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + requesterId));
        return policy.canFindUserById(user, targetUserId);
    }

}