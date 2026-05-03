package TOPSECRET.persistence.mem;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * In-memory implementation of {@link ICollectionOfPublicationsRepository}.
 *
 * <p>This repository stores {@link CollectionOfPublications} aggregates in a
 * simple in-memory {@link java.util.List}, primarily for testing and development
 * purposes. It does not involve any persistence framework or database.</p>
 *
 * <p>This class supports multiple user stories:
 * <ul>
 *   <li><b>US017</b> — store newly created collections</li>
 *   <li><b>US018</b> — manage collections and their publications</li>
 *   <li><b>US019</b> — update collection visibility (private/public)</li>
 *   <li><b>US020</b> — query public collections by genre</li>
 * </ul>
 *
 * <p>Object creation is not handled by this repository. Instead, it assumes that
 * fully constructed {@link CollectionOfPublications} instances are provided by
 * the application layer (typically via a service using
 * {@link CollectionOfPublicationsFactory}). This keeps the repository focused
 * solely on persistence concerns.</p>
 *
 * <p>This implementation is not thread-safe and is intended for non-concurrent
 * scenarios such as unit testing.</p>
 */
public class CollectionOfPublicationsRepositoryMem implements ICollectionOfPublicationsRepository {

    private List<CollectionOfPublications> _collectionOfPublicationsList;

    /**
     * Creates an empty in-memory repository.
     *
     * <p>Initialises the internal collection storage. No external dependencies
     * are required.</p>
     */
    public CollectionOfPublicationsRepositoryMem() {

        _collectionOfPublicationsList = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    public CollectionOfPublications save(CollectionOfPublications collectionOfPublications) {
        if (existsById(collectionOfPublications.id())) {
            throw new IllegalArgumentException("CollectionOfPublications already exists");
        }

        _collectionOfPublicationsList.add(collectionOfPublications);

        return collectionOfPublications;
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsById(CollectionOfPublicationsId collectionOfPublicationsId) {
        if (collectionOfPublicationsId == null) {
            throw new IllegalArgumentException("CollectionOfPublicationsID cannot be null.");
        }

        for (CollectionOfPublications collectionOfPublications : _collectionOfPublicationsList) {
            if (collectionOfPublications.id().equals(collectionOfPublicationsId)) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public List<CollectionOfPublications> getUserCollections(UserId ownerId) {
//
//        if (ownerId == null) {
//            throw new IllegalArgumentException("User cannot be null");
//        }
//
//        List<CollectionOfPublications> userCollections = new ArrayList<>();
//
//        _collectionOfPublicationsList.forEach(collection -> {
//            if (collection.getOwnerId().equals(ownerId)) {
//                userCollections.add(collection);
//            }
//        });
//
//        return userCollections;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public boolean makeCollectionPublic(CollectionOfPublicationsID id) {
//
//        if (id == null) {
//            throw new NullPointerException("Collection ID must not be null");
//        }
//
//        for (CollectionOfPublications collection : _collectionOfPublicationsList) {
//            if (collection.id().equals(id)) {
//                collection.set_isPrivate();
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public List<CollectionOfPublications> findPublicCollectionsByGenreId(GenreID genreId) {
//        if (genreId == null) {
//            throw new IllegalArgumentException("Genre cannot be null");
//        }
//
//        List<CollectionOfPublications> collections = new ArrayList<>();
//
//        for (CollectionOfPublications c : _collectionOfPublicationsList) {
//            if (c.getCollectionGenre().equals(genreId) && !c.getIsPrivate()) {
//                collections.add(c);
//            }
//        }
//        return collections;
//    }
}