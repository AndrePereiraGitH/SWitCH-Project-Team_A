package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US020;

import javax.management.InstanceNotFoundException;
import java.util.List;

public interface CollectionOfPublicationsRepository {

    // US017
    CollectionOfPublications createIfNotExists(User user, String name, Genre genre);

    // US018
    boolean addPublicationToCollection(User user, Library userLibrary,
                                       CollectionOfPublications collection,
                                       PublicationSample publication);

    boolean addCollectionToRepo(CollectionOfPublications collection);

    List<CollectionOfPublications> getUserCollections(User user);

    CollectionOfPublications collectionDTOToDomain(CollectionOfPublicationsDTO_US018 dto)
            throws InstanceNotFoundException;

    // US019
    List<CollectionOfPublications> getAllCollectionOfPublicationsByUser(User owner);

    CollectionOfPublications getCollectionByDTO(CollectionOfPublicationsDTO_US019 collectionDTO)
            throws InstanceNotFoundException;

    boolean makeCollectionOfPublicationsPublic(CollectionOfPublicationsDTO_US019 collectionDTO)
            throws InstanceNotFoundException;

    // US020
    List<CollectionOfPublicationsDTO_US020> findPublicByGenre(Genre genreName);
}