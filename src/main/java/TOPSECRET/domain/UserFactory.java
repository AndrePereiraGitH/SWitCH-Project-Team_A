package TOPSECRET.domain;

import TOPSECRET.domain.Enum.UserRole;

public class UserFactory {

    public User create(String username, Contact contact, Profile profile, UserRole userRole) {
        return new User(username, contact, profile, userRole);
    }
}