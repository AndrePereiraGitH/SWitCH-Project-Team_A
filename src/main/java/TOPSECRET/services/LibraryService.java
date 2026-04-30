package TOPSECRET.services;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.domain.ValueObjects.UserId;

import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;

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

    /**
     * Constructs a new {@code LibraryService} with the required dependencies.
     *
     * @param libraryRepository the repository used to persist libraries; must not be null
     * @param libraryFactory    the factory used to create library instances; must not be null
     * @throws IllegalArgumentException if either argument is null
     */

    public LibraryService(ILibraryRepository libraryRepository, LibraryFactory libraryFactory) {
        if (libraryRepository == null)
            throw new IllegalArgumentException("LibraryRepository cannot be null");
        if (libraryFactory == null)
            throw new IllegalArgumentException("LibraryFactory cannot be null");
        this._libraryRepository = libraryRepository;
        this._libraryFactory = libraryFactory;
    }

    /**
     * Creates a new {@link Library} and persists it.
     *
     * <p>Delegates creation to the {@link LibraryFactory} and persistence
     * to the {@link ILibraryRepository}.</p>
     *
     * @param libraryName the name of the library; must not be null or blank
     * @param userId      the identifier of the user who owns the library; must not be null
     * @return the persisted {@link Library} instance
     * @throws IllegalArgumentException      if {@code libraryName} is null or blank,
     *                                       or if {@code userId} is null
     * @throws InstanceAlreadyExistsException if the user already has a library
     */


    public Library createAndSave(String libraryName, UserId userId)
            throws InstanceAlreadyExistsException {

        Library library = _libraryFactory.create(libraryName, userId);
        return _libraryRepository.save(library);
    }
}
