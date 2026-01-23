package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public boolean exists(String userName) {

        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void createAndAddUser(String username, Contact contact, Profile profile, UserRole userRole) {
        if (exists(username)) throw new IllegalArgumentException("Username already exists");
        users.add(new User(username, contact, profile, userRole));
    }

    public int getUserCount() {
        return users.size();
    }
}