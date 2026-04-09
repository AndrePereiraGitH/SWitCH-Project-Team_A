package TOPSECRET.repository;

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
    public void save(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (exists(user.getUserName())) {
            throw new IllegalArgumentException("User already exists");
        }

        _userList.add(user);
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

    /**
     * {@inheritDoc}
     */
    public Optional<UserId> findUserIdByUsername(String username){

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