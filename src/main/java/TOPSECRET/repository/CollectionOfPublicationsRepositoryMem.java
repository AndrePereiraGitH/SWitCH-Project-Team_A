package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US020;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
import TOPSECRET.domain.CollectionOfPublicationsFactory;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


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
 * via the constructor so it can be replaced with a mock in unit tests.
 */
public class CollectionOfPublicationsRepositoryMem implements ICollectionOfPublicationsRepository {

    // the internal list that holds all collections — shared across all user stories
    private List<CollectionOfPublications> _collectionOfPublicationsList = new ArrayList<>();

    // factory used to create new collections — injected so tests can mock it (US017)
    private final CollectionOfPublicationsFactory _factory;


    // ============================== US017 ==============================

    /**
     * Creates the repository with the factory it needs to build new collections — US017.
     *
     * <p>The factory is injected here instead of being created inside the class.
     * This is called Dependency Injection — it means tests can pass a fake (mock) factory
     * and control what gets created, without touching the real domain class.
     *
     * @param factory the factory used to create {@link CollectionOfPublications} instances
     * @throws IllegalArgumentException if factory is null
     */
    // factory injected via constructor — so unit tests can replace it with a mock
    public CollectionOfPublicationsRepositoryMem(CollectionOfPublicationsFactory factory) {
        // factory is required — without it the repository cannot create any collection
        if (factory == null) {
            throw new IllegalArgumentException("CollectionOfPublicationsFactory cannot be null");
        }
        this._factory = factory;
    }

    /**
     * Creates a new private collection for a user, but only if no duplicate exists — US017.
     *
     * <p>US017 rule: a user cannot have two collections with the same name and the same genre.
     * This method checks for that duplicate before creating anything.
     *
     * <p>The name comparison uses {@code equalsIgnoreCase} because the name is typed by the user —
     * "Favorites" and "favorites" should be treated as the same name.
     *
     * <p>Note: we do NOT use {@code CollectionOfPublications.equals()} for the duplicate check
     * because that method also compares {@code isPrivate} and the publications list.
     * Here we only care about owner + name + genre — that is what defines a duplicate for US017.
     *
     * <p>If no duplicate is found, the factory creates the collection and it is added to the list.
     * The new collection is always private — that is a US017 rule enforced by the domain constructor.
     *
     * @param user  the user who wants to create the collection
     * @param name  the name chosen by the user
     * @param genre the genre chosen by the user
     * @return the newly created private collection
     * @throws IllegalArgumentException if any parameter is null, or if a duplicate already exists
     */
    public CollectionOfPublications createIfNotExists(
            User user,
            String name,
            Genre genre) {

        // all three are required — a collection must have an owner, a name, and a genre
        if (user == null || name == null || genre == null) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        // check every existing collection for a duplicate
        // duplicate = same owner + same name (ignoring case) + same genre
        for (int i = 0; i < _collectionOfPublicationsList.size(); i++) {
            CollectionOfPublications c = _collectionOfPublicationsList.get(i);
            if (user.equals(c.getOwner())
                    && c.getCollectionName().equalsIgnoreCase(name) // case-insensitive: "Favorites" == "favorites"
                    && c.getCollectionGenre().equals(genre)) {

                // duplicate found — US017 does not allow this
                throw new IllegalArgumentException(
                        "Collection with same name and genre already exists for this user."
                );
            }
        }

        // no duplicate found — ask the factory to create the collection (always private by default)
        CollectionOfPublications newCollection =
                _factory.create(name, genre, user);

        _collectionOfPublicationsList.add(newCollection);
        return newCollection;
    }

    // ============================== end US017 ==============================


    // ============================== US018 ==============================

    /**
     * Adds a publication to a user's collection — US018.
     *
     * @param user        owner of the collection
     * @param userLibrary the user's library (publication must be in it)
     * @param collection  the collection to add the publication to
     * @param publication the publication to add
     * @return true if added successfully, false if any validation fails
     */
//    public boolean addPublicationToCollection(User user,
//                                              Library userLibrary,
//                                              CollectionOfPublications collection,
//                                              PublicationSample publication) {
//
//        if (user == null || userLibrary == null || collection == null || publication == null) {
//            return false;
//        }
//        if (!user.equals(collection.getOwner())) {
//            return false;
//        }
//        if (!user.equals(userLibrary.getUser())) {
//            return false;
//        }
//        if (!userLibrary.containsPublication(publication)) {
//            return false;
//        }
//
//        return collection.addPublication(publication);
//    }

    /**
     * Adds a collection directly to the repository — US018.
     *
     * @param collection collection to add
     * @return true if added, false if already exists
     */
    public boolean addCollectionToRepo(CollectionOfPublications collection) {
        if (_collectionOfPublicationsList.contains(collection)) {
            return false;
        }

        return _collectionOfPublicationsList.add(collection);
    }

    /**
     * Returns all collections that belong to a given user — US018 / US019.
     *
     * @param user the owner of the collections
     * @return list of collections owned by the user
     * @throws IllegalArgumentException if user is null
     */
    public List<CollectionOfPublications> getUserCollections(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return getAllCollectionOfPublicationsByUser(user);
    }

    /**
     * Converts a {@link CollectionOfPublicationsDTO_US018} DTO back into its domain object — US018.
     *
     * @param dto the selected collection DTO
     * @return the matching CollectionOfPublications domain object
     * @throws IllegalArgumentException  if dto is null
     * @throws InstanceNotFoundException if no matching collection exists
     */
    public CollectionOfPublications collectionDTOToDomain(
            CollectionOfPublicationsDTO_US018 dto)
            throws InstanceNotFoundException {

        if (dto == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }

        for (CollectionOfPublications collection : _collectionOfPublicationsList) {

            if (collection.getCollectionName().equalsIgnoreCase(dto.getName())
                    && collection.getOwner().getUserName().equals(dto.getOwnerName())) {

                return collection;
            }
        }
        throw new InstanceNotFoundException(
                "Collection not found for given DTO"
        );
    }

    // ============================== end US018 ==============================


    // ============================== US019 ==============================

    /**
     * Returns all collections owned by a given user — US019.
     *
     * @param owner the user to search for
     * @return list of collections owned by the user
     * @throws IllegalArgumentException if owner is null
     */
    public List<CollectionOfPublications> getAllCollectionOfPublicationsByUser(User owner) {

        if (owner == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<CollectionOfPublications> userCollections = new ArrayList<>();

        _collectionOfPublicationsList.forEach(collection -> {
            if (collection.getOwner().equals(owner)) {
                userCollections.add(collection);
            }
        });

        return userCollections;
    }

    /**
     * Finds a collection in the repository that matches the given DTO — US019.
     *
     * @param collectionDTO DTO containing the identifying data
     * @return the matching {@link CollectionOfPublications}
     * @throws InstanceNotFoundException if no collection matches
     */
    public CollectionOfPublications getCollectionByDTO(CollectionOfPublicationsDTO_US019 collectionDTO) throws InstanceNotFoundException {

        Optional<CollectionOfPublications> collectionFound = _collectionOfPublicationsList.stream()
                .filter(c ->
                        Objects.equals(c.getCollectionName(), collectionDTO.get_name()) &&
                                Objects.equals(c.getCollectionGenre().getGenreType(), collectionDTO.get_genreName()) &&
                                Objects.equals(c.getOwner().getUserName(), collectionDTO.getOwnerName())
                )
                .findFirst();

        if (collectionFound.isEmpty()) {
            throw new InstanceNotFoundException("selected collection, does not match any CollectionOfPublications");
        }

        CollectionOfPublications collection = collectionFound.get();

        return collection;
    }

    /**
     * Makes a collection public by setting its private flag to false — US019.
     *
     * @param collectionDTO the DTO that identifies the collection to make public
     * @return true if the operation completes successfully
     * @throws IllegalArgumentException  if collectionDTO is null
     * @throws InstanceNotFoundException if no collection matches the provided DTO
     */
    public boolean makeCollectionOfPublicationsPublic(CollectionOfPublicationsDTO_US019 collectionDTO) throws InstanceNotFoundException {
        if (collectionDTO == null) {
            throw new IllegalArgumentException("collection dto cannot be null");
        }

        CollectionOfPublications coll = getCollectionByDTO(collectionDTO);

        coll.set_isPrivate(false);
        return true;
    }

    // ============================== end US019 ==============================


    // ============================== US020 ==============================

    /**
     * Finds all public collections that match a given genre — US020.
     *
     * @param genreName the genre to search for
     * @return list of public collections with the given genre
     * @throws IllegalArgumentException if genre is null or blank
     */
    public List<CollectionOfPublicationsDTO_US020> findPublicByGenre(Genre genreName) {
        if (genreName == null || genreName.getGenreType() == null || genreName.getGenreType().isBlank()) {
            throw new IllegalArgumentException("Genre can't be null.");
        }

        List<CollectionOfPublicationsDTO_US020> result = new ArrayList<>();

        for (CollectionOfPublications c : _collectionOfPublicationsList) {
            if (!c.getIsPrivate() && genreName.equals(c.getCollectionGenre())) {
                result.add(new CollectionOfPublicationsDTO_US020(c.getCollectionName(), c.getOwner().getUserName()));
            }
        }

        return List.copyOf(result);
    }

    // ============================== end US020 ==============================
}