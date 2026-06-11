package TOPSECRET.persistence.mem;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CollectionOfPublications> findById(CollectionOfPublicationsId collectionId) {

        Objects.requireNonNull(collectionId, "CollectionId cannot be null");

        for (CollectionOfPublications collectionOfPublications : _collectionOfPublicationsList) {
            if (collectionOfPublications.id().equals(collectionId)) {
                return Optional.of(collectionOfPublications);
            }
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionOfPublications> findAllByOwnerId(UserId ownerId) {

        if (ownerId == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<CollectionOfPublications> userCollections = new ArrayList<>();

        _collectionOfPublicationsList.forEach(collection -> {
            if (collection.getOwnerId().equals(ownerId)) {
                userCollections.add(collection);
            }
        });

        return userCollections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionOfPublications> findPublicCollectionsByGenreId(GenreId genreId) {
        if (genreId == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        List<CollectionOfPublications> collections = new ArrayList<>();

        for (CollectionOfPublications c : _collectionOfPublicationsList) {
            if (c.getCollectionGenre().equals(genreId) && !c.getIsPrivate()) {
                collections.add(c);
            }
        }
        return collections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionOfPublications> findAllPublicCollections() {

        List<CollectionOfPublications> publicCollections = new ArrayList<>();

        for (CollectionOfPublications c : _collectionOfPublicationsList) {
            if (c.isCurrentlyPublic()) {
                publicCollections.add(c);
            }
        }

        return publicCollections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionOfPublications> findAll() {
        return new ArrayList<>(_collectionOfPublicationsList);
    }
}