package TOPSECRET.domain.User;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
/**
 * Aggregate Root representing a User.
 *
 * <p>A {@code User} has a unique {@link UserId}, username, contact, profile,
 * and one or more {@link UserRole roles}. All invariants are validated on creation.</p>
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *   <li>Username must have at least 5 non-space characters</li>
 *   <li>Profile must not be null</li>
 *   <li>User must have at least one role</li>
 * </ul>
 */
public class User implements AggregateRoot<UserId> {

    private UserId _id;
    private String _userName;
    private Profile _profile;
    private Set<UserRole> _userRoles;

    /**
     * Creates a new User with a generated {@link UserId}.
     *
     * @param userName username (min 5 non-space chars)
     * @param profile required profile
     * @param userRoles non-empty set of roles
     * @throws IllegalArgumentException if validation fails
     */
    protected User(String userName, Profile profile, Set<UserRole> userRoles) {

        //Considered that the username can't be null
        //nor can it be composed of fewer than 5 digits
        if (userName == null) {
            throw new IllegalArgumentException("Username can't be null.");
        }

        //trim() - Java method to remove spaces
        //length() - Java method to count the length of the provided argument
        if (userName.replace(" ", "").length() < 5) {
            throw new IllegalArgumentException("Username must contain at least 5 characters.");
        }

        if (userRoles == null || userRoles.isEmpty())
            throw new IllegalArgumentException("UserRoles can't be null or empty.");

        if(profile == null) {
            throw new IllegalArgumentException("Profile can't be null.");
        }

        this._userName = userName;
        this._profile = profile;
        this._userRoles = Set.copyOf(userRoles); // immutable defensive copy
        this._id = new UserId(UUID.randomUUID().toString());
    }

    /**
     * Rebuilds an existing User.
     *
     * @param userId existing id
     * @param username username (min 5 non-space chars)
     * @param profile required profile
     * @param userRoles non-empty set of roles
     * @throws IllegalArgumentException if validation fails
     */
    public User(UserId userId, String username, Profile profile, Set<UserRole> userRoles) {

        if (userId == null) {
            throw new IllegalArgumentException("UserId can't be null.");
        }

        if (username == null) {
            throw new IllegalArgumentException("Username can't be null.");
        }

        if (username.replace(" ", "").length() < 5) {
            throw new IllegalArgumentException("Username must contain at least 5 characters.");
        }

        if (userRoles == null || userRoles.isEmpty()) {
            throw new IllegalArgumentException("UserRoles can't be null or empty.");
        }

        if (profile == null) {
            throw new IllegalArgumentException("Profile can't be null.");
        }

        this._id = userId;
        this._userName = username;
        this._profile = profile;
        this._userRoles = Set.copyOf(userRoles); // defensive immutable copy
    }

    // Empty Constructor
    public User() {
    }

    // Getters

    public UserId id() {
        return _id;
    }
    public String getUserName() {
        return _userName;
    }

    public Profile getProfile() {
        return _profile;
    }

    public Set<UserRole> getUserRoles() {
        return Collections.unmodifiableSet(_userRoles);
    }
    public String getUserInfo() {
        return "User: " + _userName + ", Role: " + _userRoles +  ", Profile: " + _profile;
    }


    /**
     * Indicates whether some other object is equal to this {@code User}.
     * <p>
     * Two {@code User} instances are considered equal if they share the same unique identifier.
     * </p>
     *
     * @param obj the object to compare with
     * @return {@code true} if the given object is a {@code User} with the same ID;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
                if (this == obj) return true;
    
                if (obj instanceof User) {
                    User other = (User) obj;
                    return this._id.equals(other._id);
        }
        return false;
    }

  /**
 * Indicates whether this {@code User} is the same entity as another object,
 * based on their business attributes.
 *
 * @param obj the object to compare with
 * @return {@code true} if the given object is a {@code User} with the same
 *         business attributes; {@code false} otherwise
 */
  @Override
  public boolean sameAs(Object obj) {
      if (!(obj instanceof User other)) return false;
      return this._userName.equals(other._userName) &&
              this._profile.equals(other._profile) &&
              this._userRoles.equals(other._userRoles);
  }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }
}