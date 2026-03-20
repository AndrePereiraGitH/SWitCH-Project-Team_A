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
 * In-memory implementation of {@link ILibraryRepository}.
 * <p>
 * Stores libraries in a {@link HashMap} keyed by the owner's username.
 * Uses a {@link LibraryFactory} to create new library instances.
 * </p>
 */
public class LibraryRepositoryMem implements ILibraryRepository {

    private final LibraryFactory _libraryFactory;
    private final Map<String, Library> _libraries;

    /**
     * Constructs a new {@code LibraryRepositoryMem} with the provided factory.
     *
     * @param libraryFactory the factory used to create new {@link Library} instances;
     *                       must not be null
     * @throws IllegalArgumentException if {@code libraryFactory} is null
     */
    public LibraryRepositoryMem(LibraryFactory libraryFactory) {
        if (libraryFactory == null) {
            throw new IllegalArgumentException("LibraryFactory cannot be null");
        }
        _libraries = new HashMap<>();
        _libraryFactory = libraryFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Library createAndAdd(String libraryName, User owner) throws InstanceAlreadyExistsException {
        if (libraryName == null) {
            throw new IllegalArgumentException("Library name can not be null");
        }
        if (libraryName.isEmpty()) {
            throw new IllegalArgumentException("Library name must have one or more characters");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Owner can not be null");
        }
        if (verifyIfUserHasALibrary(owner)) {
            throw new InstanceAlreadyExistsException("The user already has a library");
        }
        Library newLibrary = _libraryFactory.create(libraryName, owner);
        _libraries.put(owner.getUserName(), newLibrary);
        return newLibrary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> getAllPublicationsFromUserLibrary(String username) throws InstanceNotFoundException {
        Library userLibrary = getLibraryOfUser(username);
        return userLibrary.getAllPublications();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyIfUserHasALibrary(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User can not be null");
        }
        for (Library library : _libraries.values()) {
            if (library.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }
}