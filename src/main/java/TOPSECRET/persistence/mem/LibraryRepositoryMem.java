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

    private final Map<String, Library> _libraries;

    /**
     * Constructs a new {@code LibraryRepositoryMem} with the provided factory.
     *
     * @throws IllegalArgumentException if {@code libraryFactory} is null
     */
    public LibraryRepositoryMem(LibraryFactory libraryFactory) {
    
        _libraries = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    public Library save(Library library) throws InstanceAlreadyExistsException {
        if (library == null) {
            throw new IllegalArgumentException("Library name can not be null");
        }

        _libraries.put(library.id().toString(), library);
        return library;
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
