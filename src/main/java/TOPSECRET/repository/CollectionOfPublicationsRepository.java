package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.dto.CollectionOfPublicationsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// This repository is responsible for managing PrivateList operations.
// Even though PrivateLists are conceptually associated with a user's Library,
// they represent a distinct domain concept with their own business rules.
// Creating a dedicated repository avoids overloading the LibraryRepository,
// improves separation of concerns, and makes the system easier to maintain,
// test, and extend in the future.

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

    // METHOD: Adds a publication to a user's collection.
    // Returns true if the publication was added successfully,
    // or false if any validation fails.

    /**
     * US018 - Adds a publication to a user's collection.
     * Validates ownership and library membership before adding.
     *
     * @param user Owner of the collection
     * @param collection Collection to add publication to
     * @param publication Publication to add
     * @return true if added successfully, false if validation fails
     */
    // public boolean addPublicationToCollection(User user, CollectionOfPublications collection, PublicationSample publication) {
    public boolean addPublicationToCollection(User user,
                                              Library userLibrary,
                                              CollectionOfPublications collection,
                                              Publication publication) {

        if (user == null || userLibrary == null || collection == null || publication == null) {
            return false;
        }

        // ownership
        if (!user.equals(collection.getOwner())) {
            return false;
        }

        // library must belong to user
        if (!user.equals(userLibrary.getUser())) {
            return false;
        }

        // publication must exist in user's library
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
            Genre genre,
            boolean isPrivate
    ) {
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
                new CollectionOfPublications(name, genre, isPrivate, user);

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

}




