package TOPSECRET.domain.User;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;

public interface IUserRepository {

    User createAndAdd(String username, Contact contact, Profile profile, UserRole userRole);
    boolean exists(String username);
    void add(User user);
}