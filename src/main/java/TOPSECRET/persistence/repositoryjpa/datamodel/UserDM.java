package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

/**
 * JPA Data Model representing a User entity persisted in the database.
 *
 * <p>This class is responsible for mapping the domain concept of a User
 * into a relational database structure using JPA annotations.</p>
 *
 * <p>It stores user identity, username, contact information, profile data,
 * and assigned roles.</p>
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
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    /**
     * Profile information of the user.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "_name", column = @Column(name = "profile_name")),
            @AttributeOverride(name = "_birthday", column = @Column(name = "profile_birthday")),
            @AttributeOverride(name = "_contact._phoneNum", column = @Column(name = "profile_phone_num")),
            @AttributeOverride(name = "_contact._extension", column = @Column(name = "profile_extension")),
            @AttributeOverride(name = "_contact._email.userName", column = @Column(name = "profile_email_user_name")),
            @AttributeOverride(name = "_contact._email.domain", column = @Column(name = "profile_email_domain"))
    })
    private Profile profile;

    /**
     * Role assigned to the user.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> userRoles;

    /**
     * Constructs a fully initialized UserDM instance.
     *
     * @param id        unique identifier of the user; must not be null
     * @param userName  username; must not be null
     * @param profile   profile information; must not be null
     * @param userRoles  assigned role; must not be null
     */
    public UserDM(UserId id, String userName, Profile profile, Set<UserRole> userRoles) {
        this.id = Objects.requireNonNull(id, "UserId cannot be null");
        this.userName = Objects.requireNonNull(userName, "Username cannot be null");
        this.profile = Objects.requireNonNull(profile, "Profile cannot be null");

        if (userRoles == null || userRoles.isEmpty()) {
            throw new IllegalArgumentException("UserRoles cannot be null or empty");
        }

        this.userRoles = Set.copyOf(userRoles);
    }
}
