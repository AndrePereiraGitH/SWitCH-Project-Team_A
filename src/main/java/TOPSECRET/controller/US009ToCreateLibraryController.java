package TOPSECRET.controller;

import TOPSECRET.domain.User;
import TOPSECRET.repository.LibraryRepository;
import TOPSECRET.repository.UserRepositoryMem;

import javax.management.InstanceAlreadyExistsException;


/**
 * Controller class for US009 - Create Library use case.
 * <p>
 * This controller manages the creation of user libraries by coordinating
 * between the LibraryRepository and UserRepository. It ensures that libraries
 * are only created for existing users.
 * </p>
 */

public class US009ToCreateLibraryController {

    private final LibraryRepository _libraryRepository;
    private final UserRepositoryMem _userRepositoryMem;

    /**
     * Constructs a new US009ToCreateLibraryController with the specified repositories.
     * <p>
     * Both repositories are required and cannot be null.
     * </p>
     *
     * @param libraryRepository the repository for managing libraries
     * @param userRepositoryMem the repository for managing users
     * @throws NullPointerException if either repository is null
     */

    public US009ToCreateLibraryController(LibraryRepository libraryRepository, UserRepositoryMem userRepositoryMem){
        if (libraryRepository == null) {
            throw new NullPointerException("libraryRepository can not be null");
        }

        if(userRepositoryMem == null){
            throw new NullPointerException("User repository can not be null");
        }

        _libraryRepository = libraryRepository;
        _userRepositoryMem = userRepositoryMem;
    }

    /**
     * Creates and adds a new library for the specified user if the user exists in the system.
     * <p>
     * This method first verifies that the user exists before attempting to create the library.
     * If the user does not exist, the library is not created.
     * </p>
     *
     * @param libraryName the name of the library to create; must not be empty
     * @param user the user who will own the library; must exist in the system
     * @return {@code true} if the library was successfully created, {@code false} if the user does not exist
     * @throws InstanceAlreadyExistsException if the user already has a library
     * @throws IllegalArgumentException if the library name is empty or null
     */

    public boolean createAndAddLibrary(String libraryName, User user) throws InstanceAlreadyExistsException {
        if(isUserExistent(user)){
            _libraryRepository.createAndAdd(libraryName,user);
            return true;
        }
        return false;
    }

    /**
     * Checks whether the specified user exists in the user repository.
     *
     * @param user the user to check for existence
     * @return {@code true} if the user exists in the repository, {@code false} otherwise
     */

    public boolean isUserExistent(User user){
        String username = user.getUserName();
        boolean result = _userRepositoryMem.exists(username);
        return result;
    }
}
