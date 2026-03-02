package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.dto.CollectionOfPublicationsDTO;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Repository for managing collections of publications.
 *
 * Supports multiple user stories:
 * - US017: Creating private collections with name and genre
 * - US018: Adding publications to existing collections
 * - US020: Finding public collections by genre
 */
public class CollectionOfPublicationsRepository {

    private List<CollectionOfPublications> _collectionOfPublicationsList = new ArrayList<>();;

    public CollectionOfPublicationsRepository() {}

    /**
     * US018 - Adds a publication to a user's collection.
     * Validates ownership and library membership before adding.
     *
     * @param user Owner of the collection
     * @param collection Collection to add publication to
     * @param publication PublicationSample to add
     * @return true if added successfully, false if validation fails
     */
 public boolean addPublicationToCollection(User user,
                                           Library userLibrary,
                                           CollectionOfPublications collection,
                                           PublicationSample publication) {


        if (user == null || userLibrary == null || collection == null || publication == null) {
            return false;
        }
        if (!user.equals(collection.getOwner())) {
            return false;
        }
        if (!user.equals(userLibrary.getUser())) {
            return false;
        }
        if (!userLibrary.containsPublication(publication)) {
            return false;
        }

        return collection.addPublication(publication);
    }


    /**
     * Adds a collection to the repository.
     *
     * @param collection Collection to add
     * @return true if added, false if already exists
     */
    public boolean addCollectionToRepo(CollectionOfPublications collection)
    {
        if (_collectionOfPublicationsList.contains(collection)) {
            return false;
        }

        return _collectionOfPublicationsList.add(collection);
    }

    // This method will search for public collections of publications (lists) of a certain genre.
    /**
     * US020 - Finds all public collections of a given genre.
     *
     * @param genreName Genre to search for
     * @return List of public collections with the given genre
     * @throws IllegalArgumentException if genre is null
     */
    public List<CollectionOfPublicationsDTO> findPublicByGenre(Genre genreName)
    {
        if (genreName == null) {
            throw new IllegalArgumentException("Genre can't be null.");
        }

        List<CollectionOfPublicationsDTO> result = new ArrayList<>();

        for (CollectionOfPublications c : _collectionOfPublicationsList)
        {
            if (!c.getIsPrivate() && genreName.equals(c.getCollectionGenre()))
            {
                result.add(new CollectionOfPublicationsDTO(c.getCollectionName(), c.getOwner().getUserName()));

            }
        }

        return List.copyOf(result);
    }

    /**
     * US017 - Creates a new private collection for a user if no duplicate exists.
     * Collections are always created as private.
     * A user cannot have two collections with the same name and genre.
     *
     * @param user Owner of the collection
     * @param name Collection name
     * @param genre Collection genre
     * @return Created collection
     * @throws IllegalArgumentException if parameters are invalid or duplicate exists
     */

    /**
     * Creates a new private collection for a user if no duplicate exists.
     * US017 - A user cannot have two private collections with the same name and genre.
    */

    public CollectionOfPublications createIfNotExists(
            User user,
            String name,
            Genre genre)
    {
        if (user == null || name == null || genre == null) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        // Check for duplicates
        for (CollectionOfPublications c : _collectionOfPublicationsList) {
            if (user.equals(c.getOwner())
                    && c.getCollectionName().equalsIgnoreCase(name)
                    && c.getCollectionGenre().equals(genre)) {

                throw new IllegalArgumentException(
                        "Collection with same name and genre already exists for this user."
                );
            }
        }

        // Create new collection (always private)
        CollectionOfPublications newCollection =
                new CollectionOfPublications(name, genre, user);

        _collectionOfPublicationsList.add(newCollection);
        return newCollection;
    }

    /**
     * US019 - Gets all collections owned by a user.
     *
     * @param owner User to search collections for
     * @return List of collections owned by the user
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
     * US019 - Retrieves a {@link CollectionOfPublications} using a
     * {@link CollectionOfPublicationsDTO_US019}.
     * <p>
     * The search is performed over the internal {@code _collectionOfPublicationsList}. A collection is
     * considered a match when all of the following attributes are equal (using
     * {@link java.util.Objects#equals(Object, Object)} for null-safe comparison):
     * </p>
     * <ul>
     *   <li>Collection name: {@code c.getCollectionName()} equals {@code collectionDTO.get_name()}</li>
     *   <li>Genre type: {@code c.getCollectionGenre().getGenreType()} equals {@code collectionDTO.get_genreName()}</li>
     *   <li>Owner username: {@code c.getOwner().getUserName()} equals {@code collectionDTO.getOwnerName().getUserName()}</li>
     * </ul>
     *
     * @param collectionDTO the DTO containing the identifying data of the collection to be retrieved.
     * @return the {@link CollectionOfPublications} instance that matches the provided DTO.
     * @throws InstanceNotFoundException if no collection in {@code _collectionOfPublicationsList} matches the DTO fields.
     */
    public CollectionOfPublications getCollectionByDTO(CollectionOfPublicationsDTO_US019 collectionDTO) throws InstanceNotFoundException {

        Optional<CollectionOfPublications> collectionFound = _collectionOfPublicationsList.stream()
                .filter(c ->
                        Objects.equals(c.getCollectionName(), collectionDTO.get_name()) &&
                        Objects.equals(c.getCollectionGenre().getTitle(), collectionDTO.get_genreName()) &&
                        Objects.equals(c.getOwner().getUserName(), collectionDTO.getOwnerName().getUserName())
                )
                .findFirst();

        if (collectionFound.isEmpty()) {
            throw new InstanceNotFoundException("selected collection, does not match any CollectionOfPublications");
        }

        CollectionOfPublications collection = collectionFound.get();

        return collection;

    }

    /**
     * US019 - Makes a collection of publications public.
     * <p>
     * This method validates the received DTO, locates the corresponding collection through
     * {@link #getCollectionByDTO(CollectionOfPublicationsDTO_US019)}, and updates its privacy state by
     * setting {@code isPrivate} to {@code false}.
     * </p>
     *
     * @param collectionDTO the DTO that identifies the collection to be made public.
     * @return {@code true} if the operation completes successfully.
     * @throws IllegalArgumentException if {@code collectionDTO} is {@code null}.
     * @throws InstanceNotFoundException if no collection matches the provided DTO.
     */
    public boolean makeCollectionOfPublicationsPublic(CollectionOfPublicationsDTO_US019 collectionDTO) throws InstanceNotFoundException {
        if (collectionDTO == null) {
            throw new IllegalArgumentException("collection dto cannot be null");
        }

        CollectionOfPublications coll = getCollectionByDTO(collectionDTO);

        coll.set_isPrivate(false);
        return true;
    }

    // ------------------------------------- US018 starts ------------------------------------------
    /**
     * US018 - As a User, I want to add one of my publications to a list of mine
     * Retrieves all collections that belong to a given user.
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
     * Converts a CollectionOfPublicationsDTO into its corresponding domain object.
     *
     * @param dto the selected collection DTO
     * @return the matching CollectionOfPublications domain object
     * @throws IllegalArgumentException if dto is null
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

    // ------------------------------------- ends ------------------------------------------
}




