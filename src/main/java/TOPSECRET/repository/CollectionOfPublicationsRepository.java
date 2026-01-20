package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Publication;
import TOPSECRET.domain.User;

// This repository is responsible for managing PrivateList operations.
// Even though PrivateLists are conceptually associated with a user's Library,
// they represent a distinct domain concept with their own business rules.
// Creating a dedicated repository avoids overloading the LibraryRepository,
// improves separation of concerns, and makes the system easier to maintain,
// test, and extend in the future.

public class CollectionOfPublicationsRepository {

    // private List<CollectionOfPublications> _collectionOfPublications;

    //  METHOD: Adds a publication to a user's private list.
    //  Validations:
    //   - The list must belong to the user.
    //   - The publication must belong to the user's library.
    //   - The publication must not already exist in the list.
    //  Returns true if the publication was added successfully, or false if any validation fails.
    public boolean addPublicationToPrivateList(User user, CollectionOfPublications collectionOfPublications, Publication publication) {

        if (user == null || collectionOfPublications == null || publication == null) {
            return false;
        }

        // Checks if the user owns the list
        if (!user.ownsPrivateList(collectionOfPublications)) {
            return false;
        }

        // Checks if the publication belongs to the user's library
        if (!user.hasPublicationInLibrary(publication)) {
            return false;
        }

        // Delegates duplicate validation to the domain object
        return collectionOfPublications.addPublication(publication);
    }
}

