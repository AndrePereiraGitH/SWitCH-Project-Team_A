package TOPSECRET.controller;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.repository.UserRepository;

public class RegisterUserController {

    private final UserRepository userRepository;

    public RegisterUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String username, Contact contact, Profile profile, UserRole userRole) {
        if (username == null || contact == null || profile == null || userRole == null) {
            throw new IllegalArgumentException("All parameters must be provided");
        }

        userRepository.createAndAddUser(username, contact, profile, userRole);
    }
}