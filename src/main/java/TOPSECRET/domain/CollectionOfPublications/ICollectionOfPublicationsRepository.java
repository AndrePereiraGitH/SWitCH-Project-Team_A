package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link CollectionOfPublications} aggregates.
 *
 * <p>This interface defines the persistence contract between the domain layer
 * and the infrastructure layer for operations related to collections of
 * publications.</p>
 *
 * <p>Implementations are responsible for storing, retrieving, and updating
 * {@link CollectionOfPublications} objects while preserving domain integrity.</p>
 */
public interface ICollectionOfPublicationsRepository {

    /**
     * Retrieves all {@link CollectionOfPublications} stored in the repository.
     *
     * <p>This method returns all collections regardless of owner, genre, or
     * visibility status. Filtering (e.g., public-only constraints) is
     * handled at the service layer.</p>
     *
     * @return a list of all collections; may be empty but never {@code null}
     */
    List<CollectionOfPublications> findAll();

    /**
     * Saves a new {@link CollectionOfPublications} aggregate.
     *
     * <p>If a collection with the same identity already exists, the
     * implementation should reject the operation.</p>
     *
     * @param collectionOfPublications the collection to persist;
     *                                 must not be null
     * @return the persisted {@link CollectionOfPublications} instance
     * @throws NullPointerException     if {@code collectionOfPublications} is null
     * @throws IllegalArgumentException if the collection already exists
     */
    CollectionOfPublications save(CollectionOfPublications collectionOfPublications);

    /**
     * Verifies whether a collection with the given identifier exists.
     *
     * @param collectionOfPublicationsId the identifier of the collection
     *                                   to search for; must not be null
     * @return {@code true} if a collection with the given identifier exists;
     * {@code false} otherwise
     * @throws NullPointerException if {@code collectionOfPublicationsID} is null
     */

    boolean existsById(CollectionOfPublicationsId collectionOfPublicationsId);

    /**
     * Finds a collection of publications by its unique identifier.
     *
     * @param collectionId the unique identifier of the collection to find; must not be null
     * @return the collection of publications with the given identifier
     * @throws NullPointerException if {@code collectionId} is null
     */
    Optional<CollectionOfPublications> findById(CollectionOfPublicationsId collectionId);

    /**
     * Returns all collections owned by a given user.
     *
     * @param ownerId the user to search for
     * @return list of collections owned by the user
     * @throws IllegalArgumentException if ownerId is null
     */
    List<CollectionOfPublications> findAllByOwnerId(UserId ownerId);

    /**
     * Returns all public collections that belong to a given genre.
     *
     * @param genreId the genre of the collections
     * @return list of collections of the given genre that are public
     * @throws IllegalArgumentException if genreId is null
     */
    List<CollectionOfPublications> findPublicCollectionsByGenreId(GenreId genreId);

    /**
     * Returns all currently public collections.
     *
     * @return a list of public collections; may be empty if none exist
     */
    List<CollectionOfPublications> findAllPublicCollections();
}