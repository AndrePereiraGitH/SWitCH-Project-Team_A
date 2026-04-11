package TOPSECRET.controller;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;

/**
 * Controller responsible for handling the registration of a new user in the system (US001).
 *
 * <p>Validates the input parameters and delegates the creation of the user to the repository.</p>
 */
public class US001ToRegisterUserController {

    private final IUserRepository userRepository;

    /**
     * Constructs a new {@code US001ToRegisterUserController} with the given user repository.
     *
     * @param userRepository the repository responsible for user persistence; must not be null
     */
    public US001ToRegisterUserController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user in the system.
     *
     * <p>Validates all input parameters and ensures the username is not already taken
     * before delegating the creation to the repository.</p>
     *
     * @param username the desired username for the new user; must not be null
     * @param contact  the contact information of the new user; must not be null
     * @param profile  the profile information of the new user; must not be null
     * @param userRole the role assigned to the new user; must not be null
     * @return the {@link UserId} of the newly registered user
     * @throws IllegalArgumentException if any parameter is null or if the username already exists
     */
    public UserId registerUser(String username, Contact contact, Profile profile, UserRole userRole) {
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

        return userRepository.createAndAdd(username, contact, profile, userRole);
    }
}