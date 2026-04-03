package TOPSECRET.repository;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryMem implements IUserRepository {

    private List<User> _userList;
    private UserFactory _userFactory;

    //================================================================================
    // Constructor

    public UserRepositoryMem(UserFactory userFactory) {
        this._userFactory = userFactory;
        this._userList = new ArrayList<>();
    }

    //================================================================================

    @Override
    public User createAndAdd(String username, Contact contact, Profile profile, UserRole userRole) {

        if (exists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = _userFactory.create(username, contact, profile, userRole);
        add(user);
        return user;
    }

    @Override
    public boolean exists(String username) {
        for (User user : _userList) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (exists(user.getUserName())) {
            throw new IllegalArgumentException("User already exists");
        }

        _userList.add(user);
    }

    /**
     * {@inheritDoc}
     */
    public Optional<UserId> findIdByUsername(String username){

        if(username == null){
            throw new NullPointerException("Username must not be null");
        }

        if(username.isBlank()){
            throw new IllegalArgumentException("Username must not be blank");
        }

        for(User user: _userList){
            if(user.getUserName().equals(username)){
                return Optional.of(user.id());
            }
        }
        return Optional.empty();
    }

    public int getUserCount() {
        return _userList.size();
    }

}