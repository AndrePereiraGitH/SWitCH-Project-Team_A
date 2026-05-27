package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.LibraryAuthorizationPolicy;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.domain.User.IUserRepository;

import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

/**
 * Service responsible for managing {@link Library} operations.
 *
 * <p>Acts as the application layer between the domain and the outside world,
 * delegating creation to {@link LibraryFactory} and persistence to
 * {@link ILibraryRepository}.</p>
 */
@Service
public class LibraryService {

    private final ILibraryRepository _libraryRepository;
    private final LibraryFactory _libraryFactory;
    private final LibraryAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    /**
     * Constructs a new {@code LibraryService} with the required dependencies.
     *
     * @param libraryRepository the repository used to persist libraries; must not be null
     * @param libraryFactory    the factory used to create library instances; must not be null
     * @param policy            the authorization policy for library operations; must not be null
     * @param userRepository    the repository used to fetch users; must not be null
     * @throws IllegalArgumentException if any argument is null
     */
    public LibraryService(ILibraryRepository libraryRepository,
                          LibraryFactory libraryFactory,
                          LibraryAuthorizationPolicy policy,
                          IUserRepository userRepository) {
        Objects.requireNonNull(libraryRepository, "LibraryRepository cannot be null");
        Objects.requireNonNull(libraryFactory, "LibraryFactory cannot be null");
        Objects.requireNonNull(policy, "LibraryAuthorizationPolicy cannot be null");
        Objects.requireNonNull(userRepository, "UserRepository cannot be null");
        this._libraryRepository = libraryRepository;
        this._libraryFactory = libraryFactory;
        this.policy = policy;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new {@link Library} and persists it.
     *
     * <p>Validates authorization before delegating creation to {@link LibraryFactory}
     * and persistence to {@link ILibraryRepository}.</p>
     *
     * @param libraryName the name of the library; must not be null or blank
     * @param userId      the identifier of the user who owns the library; must not be null
     * @return the persisted {@link Library} instance
     * @throws IllegalArgumentException       if {@code libraryName} is null or blank,
     *                                        or if {@code userId} is null
     * @throws InstanceAlreadyExistsException if the user already has a library
     * @throws AccessDeniedException          if the user does not have permission to create a library
     */
    public Library createAndSave(String libraryName, UserId userId)
            throws InstanceAlreadyExistsException, AccessDeniedException {

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        if (!policy.canCreateLibrary(user)) {
            throw new AccessDeniedException("You do not have permission to create a library");
        }

        Library library = _libraryFactory.create(libraryName, userId);
        return _libraryRepository.save(library);
    }
}