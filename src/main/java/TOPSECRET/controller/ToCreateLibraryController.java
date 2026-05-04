package TOPSECRET.controller;

import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.LibraryService;

import javax.management.InstanceAlreadyExistsException;
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
     * <p>
     * Both dependencies are mandatory.
     * </p>
     *
     * @param userRepository the repository used to retrieve user information
     * @param libraryService the service responsible for creating and saving libraries
     * @throws NullPointerException if {@code userRepository} or {@code libraryService} is null
     */

    public ToCreateLibraryController(IUserRepository userRepository, LibraryService libraryService) {
        if (userRepository == null) {
            throw new NullPointerException("User repository can not be null");
        }
        if (libraryService == null) {
            throw new NullPointerException("libraryService can not be null");
        }

        _userRepository = userRepository;
        _libraryService = libraryService;
    }

    /**
     * Creates a new library associated with a given user.
     * <p>
     * The method first retrieves the {@link UserId} associated with the provided username.
     * If the user does not exist, an exception is thrown. Otherwise, the request is
     * delegated to the {@link LibraryService}.
     * </p>
     *
     * @param libraryName the name of the library to create; must not be null or blank
     * @param username    the username of the library owner; must not be null or blank
     * @return {@code true} if the library is successfully created
     * @throws IllegalArgumentException       if no user exists with the given username
     * @throws InstanceAlreadyExistsException if a library already exists for the user
     */

    public boolean createAndAddLibrary(String libraryName, String username) throws InstanceAlreadyExistsException {

        Optional<UserId> userId = _userRepository.findIdByUsername(username);

        if (userId.isEmpty()) {
            throw new IllegalArgumentException("This user does not exist");
        }

        _libraryService.createAndSave(libraryName, userId.get());

        return true;
    }

}
