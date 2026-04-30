package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * JPA Data Model representing a User entity persisted in the database.
 *
 * <p>This class is responsible for mapping the domain concept of a User
 * into a relational database structure using JPA annotations.</p>
 *
 * <p>It stores user identity, username, contact information, profile data,
 * and assigned role.</p>
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
public class UserDM {

    /**
     * Unique identifier of the User.
     */
    @EmbeddedId
    @AttributeOverride(name = "_id", column = @Column(name = "user_id"))
    private UserId id;

    /**
     * Username chosen by the user.
     * Must be unique and not null.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    /**
     * Contact information of the user.
     */
    @Embedded
    private Contact contact;

    /**
     * Profile information of the user.
     */
    @Embedded
    private Profile profile;

    /**
     * Role assigned to the user.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    /**
     * Constructs a fully initialized UserDM instance.
     *
     * @param id        unique identifier of the user; must not be null
     * @param userName  username; must not be null
     * @param contact   contact information; must not be null
     * @param profile   profile information; must not be null
     * @param userRole  assigned role; must not be null
     */
    public UserDM(UserId id, String userName, Contact contact, Profile profile, UserRole userRole) {
        this.id = Objects.requireNonNull(id, "UserId cannot be null");
        this.userName = Objects.requireNonNull(userName, "Username cannot be null");
        this.contact = contact; // nullable
        this.profile = Objects.requireNonNull(profile, "Profile cannot be null");
        this.userRole = Objects.requireNonNull(userRole, "UserRole cannot be null");
    }
}
