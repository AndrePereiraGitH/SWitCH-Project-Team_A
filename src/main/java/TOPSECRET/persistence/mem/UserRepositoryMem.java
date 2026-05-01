package TOPSECRET.persistence.mem;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.IUserRepository;

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

    private final List<User> _userList;

    //================================================================================
    // Constructor

    /**
     * Constructs a new {@code UserRepositoryMem} with the given user factory.
     */
    public UserRepositoryMem() {
        this._userList = new ArrayList<>();
    }

    //================================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public User save(User user) {
        _userList.add(user);
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByUsername(String username) {
        for (User user : _userList) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByID(UserId userID) {

        if (userID == null) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }

        for (User user : _userList) {
            if (user.id().equals(userID)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
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