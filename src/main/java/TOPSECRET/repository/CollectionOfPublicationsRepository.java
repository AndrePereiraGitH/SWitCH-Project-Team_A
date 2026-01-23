package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Publication;
import TOPSECRET.domain.User;

import java.util.ArrayList;
import java.util.List;

// This repository is responsible for managing PrivateList operations.
// Even though PrivateLists are conceptually associated with a user's Library,
// they represent a distinct domain concept with their own business rules.
// Creating a dedicated repository avoids overloading the LibraryRepository,
// improves separation of concerns, and makes the system easier to maintain,
// test, and extend in the future.

public class CollectionOfPublicationsRepository {

    private List<CollectionOfPublications> _collectionOfPublicationsList;

    public CollectionOfPublicationsRepository() {
        this._collectionOfPublicationsList = new ArrayList<>();
    }

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
}


