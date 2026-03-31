package TOPSECRET.controller;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;

public class US001ToRegisterUserController {

    private final IUserRepository userRepository;

    // Constructor
    public US001ToRegisterUserController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Validates parameters and delegates user registration to the repository
    public void registerUser(String username, Contact contact, Profile profile, UserRole userRole) {
        if (username == null || contact == null || profile == null || userRole == null) {
            throw new IllegalArgumentException("All parameters must be provided");
        }

        userRepository.createAndAdd(username, contact, profile, userRole);
    }
}