package TOPSECRET.domain.Author;

import TOPSECRET.domain.ValueObjects.AuthorId;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface responsible for managing {@link Author} entities.
 *
 * <p>This interface defines operations for creating and storing authors,
 * as well as retrieving all authors available in the repository.</p>
 *
 * <p>Implementations of this interface encapsulate the persistence mechanism
 * used to manage {@code Author} instances.</p>
 */

public interface IAuthorRepository {

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------
    /**
     * Saves the given {@link Author} in the repository.
     *
     * <p>
     * The author is expected to have already been created according to the domain
     * invariants defined by {@link Author}.
     * </p>
     *
     * @param author the author to save
     * @return the saved {@link Author}
     * @throws IllegalArgumentException if {@code author} is {@code null} or invalid
     *                                  according to the repository implementation
     */
    Author save(Author author) throws InstanceAlreadyExistsException;

    // -------------------------------------------------------------------------
    // Identity Lookup
    // -------------------------------------------------------------------------
    /**
     * Checks whether an author with the given identifier exists in the repository.
     *
     * @param authorId the identifier of the author to search for
     * @return {@code true} if an author with the given identifier exists;
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code authorId} is {@code null}
     */
    boolean existsById(AuthorId authorId);

    /**
     * Retrieves all authors currently stored in the repository.
     *
     * @return a list containing all stored {@link Author} instances
     */
    List<Author> findAll();

    /**
     * Retrieves an {@link Author} instance by its identifier.
     * <p>
     * This method searches the in-memory list of stored authors and returns
     * the first element whose {@code id} matches the provided {@link AuthorId}.
     * If no matching author is found, an empty {@link Optional} is returned.
     * </p>
     *
     * @param id the identifier of the author to retrieve; must not be {@code null}
     * @return an {@link Optional} containing the matching author if found,
     *         or an empty {@link Optional} otherwise
     *
     * @throws IllegalArgumentException if {@code id} is {@code null}
     */
    Optional<Author> findById(AuthorId id);
}
