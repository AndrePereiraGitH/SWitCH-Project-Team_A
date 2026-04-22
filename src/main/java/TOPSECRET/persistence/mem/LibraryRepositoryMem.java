package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.domain.Library.Library;

import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.HashMap;
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
    public Library createAndAdd(String libraryName, UserId userId) throws InstanceAlreadyExistsException {
        if (libraryName == null) {
            throw new IllegalArgumentException("Library name can not be null");
        }
        if (libraryName.isEmpty()) {
            throw new IllegalArgumentException("Library name must have one or more characters");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId can not be null");
        }
        if (verifyIfUserHasALibrary(userId)) {
            throw new InstanceAlreadyExistsException("The user already has a library");
        }
        Library newLibrary = _libraryFactory.create(libraryName, userId);
        _libraries.put(userId.getUsername(), newLibrary);
        return newLibrary;
    }

    /**
     * {@inheritDoc}
     */

    public Library getLibraryByUserId(UserId userId) throws InstanceNotFoundException {
        if (userId == null) {
            throw new NullPointerException("Username cannot be null");
        }
        Library library = _libraries.get(userId.getUsername());
        if (library == null) {
            throw new InstanceNotFoundException("This user does not have a library");
        }
        return library;
    }

    /**
     * {@inheritDoc}
     */
    public boolean verifyIfUserHasALibrary(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserID can not be null");
        }
        return _libraries.containsKey(userId.getUsername());
    }
}