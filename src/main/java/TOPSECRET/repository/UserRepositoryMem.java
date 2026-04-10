package TOPSECRET.repository;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link IUserRepository}.
 *
 * <p>Stores users in a {@link List} held in memory. Intended for testing and development purposes.</p>
 */
public class UserRepositoryMem implements IUserRepository {

    private final List<User> _userList = new ArrayList<>();
    private final UserFactory _userFactory;

    //================================================================================
    // Constructor

    /**
     * Constructs a new {@code UserRepositoryMem} with the given user factory.
     *
     * @param userFactory the factory used to create new {@link User} instances; must not be null
     */
    public UserRepositoryMem(UserFactory userFactory) {
        this._userFactory = userFactory;
    }

    //================================================================================

    private void save(User user) {
        _userList.add(user);
    }

    /** {@inheritDoc} */
    @Override
    public UserId create(String username, Contact contact, Profile profile, UserRole userRole) {
        User user = _userFactory.create(username, contact, profile, userRole);
        save(user);
        return user.id();
    }

    /** {@inheritDoc} */
    @Override
    public boolean exists(String username) {
        for (User user : _userList) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<UserId> findUserIdByUsername(String username) {
        if (username == null) {
            throw new NullPointerException("Username must not be null");
        }
        if (username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        for (User user : _userList) {
            if (user.getUserName().equals(username)) {
                return Optional.of(user.id());
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the total number of users currently stored in memory.
     *
     * @return the number of users in the repository
     */
    public int getUserCount() {
        return _userList.size();
    }
}