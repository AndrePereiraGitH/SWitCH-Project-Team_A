package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.Enum.UserRole;

import TOPSECRET.services.UserService;

import javax.management.InstanceAlreadyExistsException;
import java.util.Set;

/**
 * Controller responsible for handling the registration of a new {@link User} (US001).
 *
 * <p>This controller validates input data and delegates the user creation
 * process to the {@link UserService}, ensuring separation of concerns between
 * the presentation layer and business logic.</p>
 */
public class ToRegisterUserController {

    private final UserService userService;

    public ToRegisterUserController(UserService userService) {

        if(userService == null){
            throw new IllegalArgumentException("UserService cannot be null");
        }
        this.userService = userService;
    }

    /**
     * Registers a new user in the system.
     *
     * <p>Validates all input parameters and delegates the creation and persistence
     * of the user to the {@link UserService}.</p>
     *
     * @param username the desired username for the new user; must not be {@code null}
     * @param profile  the profile information of the user; must not be {@code null}
     * @param userRole the role assigned to the user; must not be {@code null}
     * @return the created {@link User} instance
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public User registerUser(String username, Profile profile, Set<UserRole> userRole) throws InstanceAlreadyExistsException {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }
        if (userRole == null) {
            throw new IllegalArgumentException("UserRole cannot be null");
        }

        return userService.createAndSave(username, profile, userRole);
    }
}