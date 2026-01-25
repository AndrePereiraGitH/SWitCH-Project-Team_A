package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Publication;
import TOPSECRET.domain.User;
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

/** This repository supports multiple user stories:
 - US017: Creation of private collections with name and genre, including duplicate validation per user.
 - US018: Adding publications to existing collections.
*/

public class CollectionOfPublicationsRepository {

    private List<CollectionOfPublications> _collectionOfPublicationsList = new ArrayList<>();;

    public CollectionOfPublicationsRepository() {}

    // METHOD: Adds a publication to a user's collection.
    // Returns true if the publication was added successfully,
    // or false if any validation fails.
    public boolean addPublicationToCollection(User user, CollectionOfPublications collection, Publication publication) {

        if (user == null || collection == null || publication == null) {
            return false;
        }

        // Checks if the user owns the collection
        if (!user.ownsCollection(collection)) {
            return false;
        }

        // Checks if the publication belongs to the user's library
        if (!user.hasPublicationInLibrary(publication)) {
            return false;
        }

        // Delegates duplicate validation to the domain object
        return collection.addPublication(publication);

    }

    public boolean addCollectionToRepo(CollectionOfPublications collection)
    {
        if (_collectionOfPublicationsList.contains(collection)) {
            return false;
        }

        return _collectionOfPublicationsList.add(collection);
    }

    // This method will search for public collections of publications (lists) of a certain genre.
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
                result.add(new CollectionOfPublicationsDTO(c.getCollectionName(), c.getOwner()));
            }
        }

        return List.copyOf(result);
    }

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

        for (CollectionOfPublications c : _collectionOfPublicationsList) {
            if (user.ownsCollection(c)
                    && c.getCollectionName().equalsIgnoreCase(name)
                    && c.getCollectionGenre().equals(genre)) {

                throw new IllegalArgumentException(
                        "Collection with same name and genre already exists for this user."
                );
            }
        }

        CollectionOfPublications newCollection =
                new CollectionOfPublications(name, genre, user.getLibrary(), isPrivate, user);

        _collectionOfPublicationsList.add(newCollection);
        user.addCollection(newCollection);

        return newCollection;
    }

}




