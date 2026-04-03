package TOPSECRET.controller;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceAlreadyExistsException;
import java.util.Optional;


/**
 * Controller class for US009 - Create Library use case.
 * <p>
 * This controller manages the creation of user libraries by coordinating
 * between the LibraryRepository and UserRepository. It ensures that libraries
 * are only created for existing users.
 * </p>
 */

public class US009ToCreateLibraryController {

    private final ILibraryRepository _libraryRepository;
    private final IUserRepository _userRepository;

    /**
     * Constructs a new US009ToCreateLibraryController with the specified repositories.
     * <p>
     * Both repositories are required and cannot be null.
     * </p>
     *
     * @param libraryRepository the repository for managing libraries
     * @param userRepository the repository for managing users
     * @throws NullPointerException if either repository is null
     */

    public US009ToCreateLibraryController(ILibraryRepository libraryRepository, IUserRepository userRepository){
        if (libraryRepository == null) {
            throw new NullPointerException("libraryRepository can not be null");
        }

        if(userRepository == null){
            throw new NullPointerException("User repository can not be null");
        }

        _libraryRepository = libraryRepository;
        _userRepository = userRepository;
    }

    /**
     * Creates and adds a new library for the specified user.
     * <p>
     * Looks up the user by username and delegates library creation to the
     * LibraryRepository. If the user does not exist, an exception is thrown.
     * </p>
     *
     * @param libraryName the name of the library to create; must not be null or blank
     * @param username the username of the user who will own the library; must not be null or blank
     * @return {@code true} if the library was successfully created and added
     * @throws IllegalArgumentException if no user is found with the given username
     * @throws InstanceAlreadyExistsException if the user already has a library
     */

    public boolean createAndAddLibrary(String libraryName, String username) throws InstanceAlreadyExistsException {

        Optional<UserId> userId = _userRepository.findIdByUsername(username);

        if(userId.isEmpty()){
            throw new IllegalArgumentException("This user does not exist");
        }

        Library library = _libraryRepository.createAndAdd(libraryName,userId.get());

        return true;
    }

}
