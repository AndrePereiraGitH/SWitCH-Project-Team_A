package TOPSECRET.repository;

import TOPSECRET.domain.LibraryFactory;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.domain.User;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Repository for managing {@link Library} objects.
 * <p>
 * This class is responsible for storing, retrieving, and managing libraries.
 * It uses a {@link LibraryFactory} to create new library instances and maintains
 * them in an internal map.
 * </p>
 */
public class LibraryRepository {

    private final LibraryFactory _libraryFactory;

    private final Map<String, Library> _libraries;

    /**
     * Constructs a LibraryRepository with the provided libraries map and factory.
     * <p>
     * A defensive copy of the provided map is created to prevent external modifications.
     * </p>
     *
     * @param libraries a map of library names to {@link Library} objects; can be empty
     * @param libraryFactory the factory used to create new {@link Library} instances
     * @throws IllegalArgumentException if libraryFactory is null
     */
    public LibraryRepository(Map<String, Library> libraries, LibraryFactory libraryFactory) {

        if (libraryFactory == null) {
            throw new IllegalArgumentException("LibraryFactory cannot be null");
        }

        _libraries = new HashMap<>(libraries);
        _libraryFactory = libraryFactory;
    }

    /**
     * Retrieves the library associated with the specified username.
     * <p>
     * This method validates that the username is not null and not empty before
     * attempting to retrieve the library from the repository.
     * </p>
     *
     * @param username the username of the user whose library is to be retrieved;
     *                 must not be null or empty
     * @return the {@link Library} associated with the username
     * @throws NullPointerException if the username is null
     * @throws IllegalArgumentException if the username is empty
     * @throws InstanceNotFoundException if no library is found for the specified username
     */
    public Library getLibraryOfUser(String username) throws InstanceNotFoundException {
        if (username == null) {
            throw new NullPointerException("Username cannot be null");
        }

        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        Library library = _libraries.get(username);

        if (library == null) {
            throw new InstanceNotFoundException("This user does not have a library");
        }

        return library;
    }

    /**
     * Creates a new library for the specified user and adds it to the repository.
     * <p>
     * This method validates that the user does not already have an existing library
     * before creating and storing a new one. The library is created using the
     * {@link LibraryFactory} and stored in the repository with the user's username
     * as the key.
     * </p>
     *
     * @param libraryName the name of the library to create; validation is handled
     *                    by {@link LibraryFactory}
     * @param owner the user who will own the library; must not be null
     * @return the newly created {@link Library} instance
     * @throws InstanceAlreadyExistsException if the user already has a library
     * @throws IllegalArgumentException if the library name is empty or the owner is null
     *                                  (thrown by {@link LibraryFactory})
     */
    public Library createAndAdd(String libraryName, User owner) throws InstanceAlreadyExistsException {
        if (verifyIfUserHasALibrary(owner)) {
            throw new InstanceAlreadyExistsException("The user already has a library");
        }

        if(libraryName.isEmpty()){
            throw new IllegalArgumentException("Library name must have one or more characters");
        }

        if(owner==null){
            throw new IllegalArgumentException("Owner can not be null");
        }

        Library newLibrary = _libraryFactory.create(libraryName,owner);

        _libraries.put(owner.getUserName(), newLibrary);

        return newLibrary;
    }

    /**
     * Retrieves all publications from the library associated with the given username.
     *
     * @param username the username of the user whose library publications are to be retrieved
     * @return a list of {@link PublicationSample} objects representing all publications
     *         in the user's library
     * @throws InstanceNotFoundException if no library is found for the specified username
     */
    public List<PublicationSample> getAllPublicationsFromUserLibrary(String username) throws InstanceNotFoundException {
        Library userLibrary = getLibraryOfUser(username);
        return userLibrary.getAllPublications();
    }

    /**
     * Verifies whether the specified user already has a library.
     * <p>
     * This method iterates through all existing libraries to check if any
     * library is owned by the given user.
     * </p>
     *
     * @param user the user to check for library ownership
     * @return {@code true} if the user has a library, {@code false} otherwise
     */
    public boolean verifyIfUserHasALibrary(User user) {
        for (Library library : _libraries.values()) {
            if (library.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }
}