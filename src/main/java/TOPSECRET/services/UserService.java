package TOPSECRET.services;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.Enum.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;

/**
 * Service responsible for user-related application operations.
 *
 * <p>
 * This service coordinates the creation of {@link User} domain objects
 * through {@link UserFactory} and their persistence through
 * {@link IUserRepository}.
 * </p>
 */
@Service
public class UserService {

    private final UserFactory userFactory;
    private final IUserRepository userRepository;

    /**
     * Creates an instance of {@code UserService}.
     *
     * @param userFactory     the factory used to create {@link User} domain objects
     * @param userRepository  the repository used to persist and retrieve users
     */
    public UserService(UserFactory userFactory, IUserRepository userRepository) {
        if (userFactory == null) {
            throw new IllegalArgumentException("User factory cannot be null.");
        }

        if (userRepository == null) {
            throw new IllegalArgumentException("User repository cannot be null.");
        }

        this.userFactory = userFactory;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new {@link User} and persists it.
     *
     * <p>
     * The user is first created using the {@link UserFactory}. After that,
     * it is saved through the {@link IUserRepository}. The operation runs
     * within a transaction.
     * </p>
     *
     * @param username the username of the user
     * @param contact  the contact information of the user
     * @param profile  the profile of the user
     * @param userRole the role assigned to the user
     * @return the persisted {@link User}
     * @throws InstanceAlreadyExistsException if a user with the same identifier already exists
     * @throws IllegalArgumentException if any invalid argument is rejected by the domain factory or repository
     */
    @Transactional
    public User createAndSave(String username, Contact contact, Profile profile, UserRole userRole)
            throws InstanceAlreadyExistsException {

        User user = userFactory.create(username, contact, profile, userRole);

        return userRepository.save(user);
    }
}