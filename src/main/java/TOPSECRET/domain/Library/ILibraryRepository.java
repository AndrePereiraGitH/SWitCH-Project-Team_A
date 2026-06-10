package TOPSECRET.domain.Library;

import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

/**
 * Contract for managing {@link Library} objects.
 * <p>
 * Defines operations for creating, retrieving, and verifying user libraries.
 * </p>
 */
public interface ILibraryRepository {


    /**
     * Adds a {@link Library} to the repository.
     *
     * @throws InstanceAlreadyExistsException if the Library already exists.
     */
    Library save(Library library) throws InstanceAlreadyExistsException;

    /**
     * Verifies whether the specified user already has a library.
     *
     * @param userId the user to check for library ownership; must not be null
     * @return {@code true} if the user has a library, {@code false} otherwise
     * @throws IllegalArgumentException if {@code user} is null
     */
    boolean existsByUserId(UserId userId);

    /**
     * Returns all {@link Library} instances currently stored in the repository.
     *
     * @return a list of all Libraries; never {@code null}, but may be empty
     */
    List<Library> findAll();

    /**
     * Finds and returns the {@link Library} with the given ID.
     *
     * @param id the {@link LibraryId} to search for
     * @return an {@link Optional} containing the Library if found; empty otherwise
     */
    Optional<Library> findById(LibraryId id);

    /**
     * Finds and returns the {@link Library} associated with the given user ID.
     *
     * @param userId the {@link UserId} to search for
     * @return an {@link Optional} containing the Library if found; empty otherwise
     */
    Optional<Library> findByUserId(UserId userId);


}
