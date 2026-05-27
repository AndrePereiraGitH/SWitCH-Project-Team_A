package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.LibraryService;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller for the US009 use case: Create Library.
 * <p>
 * This class is responsible for handling the application logic required to
 * create a new library for a given user. It validates the existence of the user
 * and delegates the creation and persistence of the library to the
 * {@link LibraryService}.
 * </p>
 */
public class ToCreateLibraryController {

    private final IUserRepository _userRepository;
    private final LibraryService _libraryService;

    /**
     * Creates a new controller instance.
     *
     * @param userRepository the repository used to retrieve user information; must not be null
     * @param libraryService the service responsible for creating and saving libraries; must not be null
     * @throws NullPointerException if {@code userRepository} or {@code libraryService} is null
     */
    public ToCreateLibraryController(IUserRepository userRepository, LibraryService libraryService) {
        Objects.requireNonNull(userRepository, "UserRepository cannot be null");
        Objects.requireNonNull(libraryService, "LibraryService cannot be null");
        _userRepository = userRepository;
        _libraryService = libraryService;
    }

    /**
     * Creates a new library associated with a given user.
     *
     * @param libraryName the name of the library to create; must not be null or blank
     * @param username    the username of the library owner; must not be null or blank
     * @return {@code true} if the library is successfully created
     * @throws IllegalArgumentException       if no user exists with the given username
     * @throws InstanceAlreadyExistsException if a library already exists for the user
     * @throws AccessDeniedException          if the user does not have permission to create a library
     */
    public boolean createAndAddLibrary(String libraryName, String username)
            throws InstanceAlreadyExistsException, AccessDeniedException {

        Optional<UserId> userId = _userRepository.findIdByUsername(username);

        if (userId.isEmpty()) {
            throw new IllegalArgumentException("This user does not exist");
        }

        _libraryService.createAndSave(libraryName, userId.get());

        return true;
    }
}