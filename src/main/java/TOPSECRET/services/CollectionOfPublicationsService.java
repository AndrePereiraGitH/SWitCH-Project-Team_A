package TOPSECRET.services;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
            String collectionName, GenreID collectionGenreId, UserId userId) {


        Objects.requireNonNull(collectionName, "collectionName cannot be null");
        Objects.requireNonNull(collectionGenreId, "collectionGenreId cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");

        CollectionOfPublications createdCollection = factory.create(
                collectionName, collectionGenreId, userId);

        CollectionOfPublications savedCollection =
                collectionOfPublicationsRepository.save(createdCollection);

        return savedCollection;
    }
}
