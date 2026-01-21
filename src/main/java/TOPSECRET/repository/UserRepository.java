package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final List<User> users = new ArrayList<>();

    private boolean validateUser(User user) {
        if (user == null) {
            return false;
        }

        for (User u : users) {
            if (u.getUserName().equals(user.getUserName())) {
                return false;
            }
        }

        return true;
    }

    public void createAndAddUser(String _userName, Contact _contact, Profile _profile, UserRole _userRole) {
        User newUser = new User(_userName, _contact, _profile, _userRole);

        if (!validateUser(newUser)) {
            throw new IllegalArgumentException("User already exists");
        }

        addUserToUserList(newUser);
    }

    private void addUserToUserList(User user) {
        users.add(user);
    }

    public boolean exists(String userName) {

        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }
}