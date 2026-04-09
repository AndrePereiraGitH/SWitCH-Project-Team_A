package TOPSECRET.controller;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;

public class US001ToRegisterUserController {

    private final IUserRepository userRepository;
    private final UserFactory userFactory;

    public US001ToRegisterUserController(IUserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    public void registerUser(String username, Contact contact, Profile profile, UserRole userRole) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }
        if (userRole == null) {
            throw new IllegalArgumentException("UserRole cannot be null");
        }

        if (userRepository.exists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = userFactory.create(username, contact, profile, userRole);

        userRepository.save(user);
    }
}