package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.User;

public interface UserRepository {

    User createAndAdd(String username, Contact contact, Profile profile, UserRole userRole);
    boolean exists(String username);
    void add(User user);
}