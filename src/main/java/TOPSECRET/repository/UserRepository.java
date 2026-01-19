package TOPSECRET.repository;

import TOPSECRET.domain.User;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public void register(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (users.containsKey(user.getUserName())) {
            throw new IllegalArgumentException("User already exists");
        }

        users.put(user.getUserName(), user);
    }

    public boolean exists(String userName) {
        return users.containsKey(userName);
    }
}