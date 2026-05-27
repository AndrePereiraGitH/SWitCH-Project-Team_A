package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceNotFoundException;
import java.util.List;

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
     * Updates an existing collection of publications.
     *
     * @param collection the collection of publications to update; must not be null
     * @return the updated collection of publications
     * @throws NullPointerException if {@code collection} is null
     */
    CollectionOfPublications update(CollectionOfPublications collection);
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
    CollectionOfPublications findById(CollectionOfPublicationsId collectionId) throws InstanceNotFoundException;

    /**
     * Returns all collections owned by a given user.
     *
     * @param ownerId the user to search for
     * @return list of collections owned by the user
     * @throws IllegalArgumentException if ownerId is null
     */
    List<CollectionOfPublications> findAllByOwnerId(UserId ownerId);

    /**
     * Returns all public collections that belong to a given genre — US020.
     *
     * @param genreId the genre of the collections
     * @return list of collections of the given genre that are public
     * @throws IllegalArgumentException if genreId is null
     */
    List<CollectionOfPublications> findPublicCollectionsByGenreId(GenreId genreId);
}