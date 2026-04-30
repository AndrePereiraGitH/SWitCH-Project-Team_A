package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.UserDM;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Assembler responsible for converting between
 * Domain User objects and JPA Data Model UserDM.
 *
 * <p>This class isolates persistence concerns from the domain layer,
 * ensuring a clean separation of responsibilities.</p>
 */
@Component
public class UserAssembler {

    private final UserFactory userFactory;

    public UserAssembler(UserFactory userFactory) {
        this.userFactory = Objects.requireNonNull(userFactory, "UserFactory cannot be null");
    }

    /**
     * Converts a domain {@link User} into a JPA {@link UserDM}.
     *
     * @param user the domain user; must not be null
     * @return corresponding data model representation
     */
    public UserDM toDM(User user) {

        Objects.requireNonNull(user, "User cannot be null");

        UserDM userDM = new UserDM(
                user.id(),
                user.getUserName(),
                user.getContact(),
                user.getProfile(),
                user.getUserRole()
        );

        return userDM;
    }

    /**
     * Converts a JPA {@link UserDM} into a domain {@link User}.
     *
     * @param dm the data model; must not be null
     * @return corresponding domain user
     */
    public User toDomain(UserDM dm) {

        Objects.requireNonNull(dm, "UserDM cannot be null");

        User user = userFactory.create(
                dm.getId(),
                dm.getUserName(),
                dm.getContact(),
                dm.getProfile(),
                dm.getUserRole()
        );

        return user;
    }
}

