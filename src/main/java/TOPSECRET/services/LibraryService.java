package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.LibraryAuthorizationPolicy;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.domain.User.IUserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;
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
    /**
     * Retrieves all registered {@link Library} entities.
     *
     * <p>Access is restricted to administrators only. The requesting user must have
     * the {@code ADMIN} role to perform this operation.</p>
     *
     * @param requestingUserId the identifier of the user requesting the operation; must not be null
     * @return a list of all libraries in the system
     * @throws InstanceNotFoundException if the requesting user does not exist
     * @throws AccessDeniedException     if the requesting user does not have the {@code ADMIN} role
     */
    public List<Library> findAll(UserId requestingUserId) throws InstanceNotFoundException {
        User user = userRepository.findByID(requestingUserId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found: " + requestingUserId));

        if (!policy.canGetAllLibrary(user)) {
            throw new AccessDeniedException("You do not have permission to retrieve all libraries");
        }

        return _libraryRepository.findAll();
    }

    /**
     * Retrieves a {@link Library} by its unique identifier.
     *
     * <p>Access is granted only to the owner of the library or to an administrator.
     * The library is first retrieved, then the requesting user's authorization is validated
     * against the library's owner before returning the result.</p>
     *
     * @param id               the unique identifier of the library to retrieve; must not be null
     * @param requestingUserId the identifier of the user requesting the operation; must not be null
     * @return the {@link Library} associated with the given identifier
     * @throws IllegalArgumentException  if {@code id} is null
     * @throws InstanceNotFoundException if no library exists for the given ID,
     *                                   or if the requesting user does not exist
     * @throws AccessDeniedException     if the requesting user is neither the owner
     *                                   nor an administrator
     */
    public Library findById(LibraryId id, UserId requestingUserId) throws InstanceNotFoundException {
        Objects.requireNonNull(id, "LibraryID cannot be null");

        Library library = _libraryRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Library not found with id: " + id));

        User user = userRepository.findByID(requestingUserId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found: " + requestingUserId));

        if (!policy.canGetLibrary(user, library.getUserId())) {
            throw new AccessDeniedException("You do not have permission to retrieve this library");
        }

        return library;
    }
    /**
     * Retrieves the {@link Library} associated with the given user ID.
     *
     * <p>Access is granted only to the owner of the library or to an administrator.
     * The requesting user's authorization is validated against the target user ID
     * before returning the result.</p>
     *
     * @param userId           the identifier of the user whose library is to be retrieved;
     *                         must not be null
     * @param requestingUserId the identifier of the user requesting the operation;
     *                         must not be null
     * @return the {@link Library} associated with the given user ID
     * @throws IllegalArgumentException  if {@code userId} is null
     * @throws InstanceNotFoundException if no library exists for the given user ID,
     *                                   or if the requesting user does not exist
     * @throws AccessDeniedException     if the requesting user is neither the owner
     *                                   nor an administrator
     */
    public Library findByUserId(UserId userId, UserId requestingUserId) throws InstanceNotFoundException {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        User requestingUser = userRepository.findByID(requestingUserId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found: " + requestingUserId));

        if (!policy.canGetLibrary(requestingUser, userId)) {
            throw new AccessDeniedException("You do not have permission to retrieve this library");
        }

        return _libraryRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("Library not found for user: " + userId));
    }
}