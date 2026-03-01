package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.User;
import TOPSECRET.domain.UserFactory;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final List<User> users = new ArrayList<>();
    private final UserFactory userFactory;

    public UserRepository(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    public boolean exists(String userName) {
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public User createAndAddUser(String username, Contact contact, Profile profile, UserRole userRole) {

        if (exists(username))
            throw new IllegalArgumentException("Username already exists");
        User newUser = userFactory.create(username, contact, profile, userRole);
        users.add(newUser);
        return newUser;
    }

    // To get the list's size
    public int getUserCount() {
        return users.size();
    }
}