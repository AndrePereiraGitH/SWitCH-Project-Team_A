package TOPSECRET.services;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.CollectionOfPublicationsDM;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;


/**
 * Application service responsible for creating and persisting
 * {@link CollectionOfPublications} aggregates.
 *
 * <p>This service coordinates the interaction between the domain factory
 * and the repository, ensuring that new collections are created according
 * to domain rules and then persisted correctly.</p>
 *
 * <p>The creation logic itself is delegated to
 * {@link CollectionOfPublicationsFactory}, while persistence is handled by
 * {@link ICollectionOfPublicationsRepository}. This keeps the service focused
 * on orchestration rather than domain construction or infrastructure concerns.</p>
 *
 * <p>All mandatory input parameters are validated before delegation.</p>
 */
@Service
public class CollectionOfPublicationsService {


    /**
     * Repository responsible for persisting
     * {@link CollectionOfPublications} aggregates.
     */
    private final ICollectionOfPublicationsRepository collectionOfPublicationsRepository;

    /**
     * Factory responsible for creating new
     * {@link CollectionOfPublications} domain objects.
     */
    private final CollectionOfPublicationsFactory factory;

    /**
     * Creates a new service with the required dependencies.
     *
     * @param collectionOfPublicationsRepository repository used to persist collections
     * @param factory                            factory used to create new domain objects
     */
    public CollectionOfPublicationsService(
            ICollectionOfPublicationsRepository collectionOfPublicationsRepository,
            CollectionOfPublicationsFactory factory) {

        if (collectionOfPublicationsRepository == null) {
            throw new IllegalArgumentException("collectionOfPublicationsRepository cannot be null");
        }

        if (factory == null) {
            throw new IllegalArgumentException("factory cannot be null");
        }

        this.collectionOfPublicationsRepository = collectionOfPublicationsRepository;
        this.factory = factory;
    }

    /**
     * Creates and persists a new {@link CollectionOfPublications}.
     *
     * <p>This method validates the input parameters, delegates the domain
     * object creation to the {@link CollectionOfPublicationsFactory}, and
     * then saves the created aggregate using the
     * {@link ICollectionOfPublicationsRepository}.</p>
     *
     * @param collectionName    the name of the collection; must not be null
     * @param collectionGenreId the genre associated with the collection;
     *                          must not be null
     * @param userId            the owner of the collection; must not be null
     * @return the persisted {@link CollectionOfPublications} instance
     * @throws NullPointerException if any argument is null
     */
    public CollectionOfPublications createAndSave(
            String collectionName, GenreId collectionGenreId, UserId userId) {

        CollectionOfPublications createdCollection = factory.create(
                collectionName, collectionGenreId, userId);

        CollectionOfPublications savedCollection =
                collectionOfPublicationsRepository.save(createdCollection);

        return savedCollection;
    }

    /**
     * Makes the collection of publications with the given identifier public.
     *
     * @param collectionId the identifier of the collection to make public; must not be null
     * @return {@code true} if the collection was changed to public,
     *         {@code false} if it was already public
     * @throws NullPointerException if {@code collectionId} is null
     * @throws IllegalArgumentException if no collection is found with the given identifier
     */
    public boolean makeCollectionPublic(CollectionOfPublicationsId collectionId) throws InstanceNotFoundException {

        Objects.requireNonNull(collectionId, "CollectionOfPublications Id cannot be null");

        CollectionOfPublications collection = collectionOfPublicationsRepository.findById(collectionId);

        boolean changed = collection.makePublic();

        if (changed) {
            collectionOfPublicationsRepository.update(collection);
        }

        return changed;
    }

    public boolean makeCollectionPublicUntil(CollectionOfPublicationsId collectionId, LocalDateTime publicUntil) throws InstanceNotFoundException {

        if (publicUntil == null) {
            throw new IllegalArgumentException("Public until date time cannot be null");
        }

        Objects.requireNonNull(collectionId, "CollectionOfPublications Id cannot be null");
        Objects.requireNonNull(publicUntil, "Public until date cannot be null");

        CollectionOfPublications collection =
                collectionOfPublicationsRepository.findById(collectionId);

        boolean changed = collection.makePublicUntil(publicUntil);

        if (changed) {
            collectionOfPublicationsRepository.update(collection);
        }

        return changed;

    }
}
