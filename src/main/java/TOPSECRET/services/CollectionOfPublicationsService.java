package TOPSECRET.services;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;


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
}
