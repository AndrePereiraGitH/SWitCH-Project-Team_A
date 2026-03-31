package TOPSECRET.domain.User;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.Objects;
import java.util.UUID;
/**
 * Aggregate Root representing a registered User in the system.
 * <p>
 * A {@code User} is uniquely identified by a {@link UserId}, generated automatically
 * upon creation. It encapsulates the user's identity, role, contact information,
 * and profile, ensuring that all business rules are enforced at construction time.
 * </p>
 *
 * <p>Business rules enforced:</p>
 * <ul>
 *     <li>Username must not be {@code null}</li>
 *     <li>Username must contain at least 5 non-space characters</li>
 *     <li>UserRole must not be {@code null}</li>
 *     <li>Profile must not be {@code null}</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 *     User user = UserFactory.create("david123", contact, profile, UserRole.BUYER);
 * </pre>
 */
public class User implements AggregateRoot<UserId> {

    private String _userName;
    private Contact _contact;
    private Profile _profile;
    private UserRole _userRole;
    private UserId _id;

    /**
     * Constructs a new {@code User} with the given attributes.
     * <p>
     * A unique {@link UserId} is automatically generated upon creation.
     * </p>
     *
     * @param userName the username of the user; must not be {@code null}
     *                 and must contain at least 5 non-space characters
     * @param contact  the contact information of the user; may be {@code null}
     * @param profile  the profile of the user; must not be {@code null}
     * @param userRole the role assigned to the user; must not be {@code null}
     * @throws IllegalArgumentException if {@code userName} is {@code null},
     *                                  has fewer than 5 non-space characters,
     *                                  {@code userRole} is {@code null},
     *                                  or {@code profile} is {@code null}
     */
    //Constructor
    protected User(String userName, Contact contact, Profile profile, UserRole userRole) {

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

        if (userRole == null) {
            throw new IllegalArgumentException("UserRole can't be null.");
        }

        if(profile == null) {
            throw new IllegalArgumentException("Profile can't be null.");
        }

        this._userName = userName;
        this._contact = contact;
        this._profile = profile;
        this._userRole = userRole;
        this._id = new UserId(UUID.randomUUID().toString());
    }

    /**
     * Returns the unique identifier of this {@code User}.
     *
     * @return the {@link UserId} of this user
     */
    public UserId id() {
        return _id;
    }

    // Empty Constructor
    public User() {
    }

    // Getters
    public UserId getId(){
        return _id;
    }

    public String getUserName() {
        return _userName;
    }

    public Contact getContact() {
        return _contact;
    }

    public Profile getProfile() {
        return _profile;
    }

    public UserRole getUserRole() {
        return _userRole;
    }

    // Method: full user information
    public String getUserInfo() {
        return "User: " + _userName + ", Role: " + _userRole + ", Contact: " + _contact + ", Profile: " + _profile;
    }


    /**
     * Indicates whether some other object is equal to this {@code User}.
     * <p>
     * Two {@code User} instances are considered equal if they share the same username.
     * </p>
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is a {@code User} with the same username;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
                if (this == obj) return true;
    
                if (obj instanceof User) {
                    User other = (User) obj;
                    if (this._id.equals(other._id))
                        return true;
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
        if (!(obj instanceof User)) {
        return false;
        }

    User other = (User) obj;

    return this._userName.equals(other._userName) &&
            this._contact.equals(other._contact) &&
            this._profile.equals(other._profile) &&
            this._userRole.equals(other._userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

}

