package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public void createAndAddUser(String _userName, Contact _contact, Profile _profile, UserRole _userRole) {
        User newUser = new User(_userName, _contact, _profile, _userRole);

        if (!validateUser(newUser)) {
            throw new IllegalArgumentException("User validation failed or user already exists");
        }

        addUserToUserList(newUser);
    }

    private boolean validateUser(User user) {
        if (user == null) {
            return false;
        }
        return !exists(user.getUserName());
    }

    public boolean exists(String userName) {
        for (User u : users) {
            if (u.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    private void addUserToUserList(User user) {
        users.add(user);
    }
}