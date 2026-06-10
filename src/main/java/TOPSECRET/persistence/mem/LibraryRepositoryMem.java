package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;

import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceAlreadyExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory implementation of {@link ILibraryRepository}.
 * <p>
 * Stores libraries in a {@link HashMap} keyed by the owner's username.
 * </p>
 */

public class LibraryRepositoryMem implements ILibraryRepository {

    private final Map<String, Library> _libraries;

    /**
     * Constructs a new {@code LibraryRepositoryMem} with the provided factory.
     * @throws IllegalArgumentException if {@code libraryFactory} is null
     */
    public LibraryRepositoryMem() {
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
    public boolean existsByUserId(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserID can not be null");
        }
        return _libraries.containsKey(userId.getUserId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Library> findAll() {
        return List.copyOf(_libraries.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Library> findById(LibraryId id) {
        if (id == null) {
            throw new IllegalArgumentException("LibraryId cannot be null");
        }
        return _libraries.values().stream()
                .filter(library -> library.id().equals(id))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Library> findByUserId(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return _libraries.values().stream()
                .filter(library -> library.getUserId().equals(userId))
                .findFirst();
    }
}