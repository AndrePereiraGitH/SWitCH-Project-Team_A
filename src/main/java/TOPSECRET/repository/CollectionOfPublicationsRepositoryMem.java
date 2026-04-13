package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Repository that stores and manages all collections of publications.
 *
 * <p>This class is shared across multiple user stories, each adding its own methods:
 * <ul>
 *   <li><b>US017</b> — create a new private collection for a user</li>
 *   <li><b>US018</b> — add a publication to an existing collection</li>
 *   <li><b>US019</b> — make a collection public</li>
 *   <li><b>US020</b> — find public collections by genre</li>
 * </ul>
 *
 * <p>The repository holds the internal list of all collections and uses a
 * {@link CollectionOfPublicationsFactory} to create new ones. The factory is injected
 * via the constructor so it can be replaced with a double in unit tests.
 */
public class CollectionOfPublicationsRepositoryMem implements ICollectionOfPublicationsRepository {

    private List<CollectionOfPublications> _collectionOfPublicationsList;
    private final CollectionOfPublicationsFactory _factory;

    /**
     * Creates the repository with the factory it needs to build new collections — US017.
     *
     * <p>The factory is injected here instead of being created inside the class.
     * This is called Dependency Injection — it means tests can pass a fake (double) factory
     * and control what gets created, without touching the real domain class.
     *
     * @param factory the factory used to create {@link CollectionOfPublications} instances
     * @throws IllegalArgumentException if factory is null
     */

    public CollectionOfPublicationsRepositoryMem(CollectionOfPublicationsFactory factory) {

        if (factory == null) {
            throw new IllegalArgumentException("CollectionOfPublicationsFactory cannot be null");
        }

        _collectionOfPublicationsList = new ArrayList<>();
        _factory = factory;
    }

    /**
     * {@inheritDoc}
     */

    public CollectionOfPublications createAndAdd(
            UserId userId,
            String name,
            GenreID genreId) {

        if (userId == null || name == null || genreId == null) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        for (int i = 0; i < _collectionOfPublicationsList.size(); i++) {
            CollectionOfPublications c = _collectionOfPublicationsList.get(i);
            if (userId.equals(c.getOwnerId())
                    && c.getCollectionName().equalsIgnoreCase(name) // case-insensitive: "Favorites" == "favorites"
                    && c.getCollectionGenre().equals(genreId)) {

                throw new IllegalArgumentException(
                        "Collection with same name and genreId already exists for this userId."
                );
            }
        }

        CollectionOfPublications newCollection =
                _factory.create(name, genreId, userId);

        _collectionOfPublicationsList.add(newCollection);
        return newCollection;
    }

    /**
     * {@inheritDoc}
     */

    public List<CollectionOfPublications> getUserCollections(UserId userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return getAllCollectionOfPublicationsByUser(userId);
    }

    /**
     * Returns all collections owned by a given user — US019.
     *
     * @param ownerId the user to search for
     * @return list of collections owned by the user
     * @throws IllegalArgumentException if ownerId is null
     */

    private List<CollectionOfPublications> getAllCollectionOfPublicationsByUser(UserId ownerId) {

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
    public List<CollectionOfPublications> findPublicCollectionsByGenreId(GenreID genreId) {
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
}