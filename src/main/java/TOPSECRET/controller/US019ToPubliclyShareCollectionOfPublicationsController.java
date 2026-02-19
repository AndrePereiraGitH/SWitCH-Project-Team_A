package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.User;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
import TOPSECRET.repository.CollectionOfPublicationsRepository;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for US019: Publicly share a user's collection of publications.
 * <p>
 * This controller orchestrates the retrieval of a user's {@link CollectionOfPublications} from the
 * {@link CollectionOfPublicationsRepository}, converts them into {@link CollectionOfPublicationsDTO_US019}
 * for presentation, and handles the request to share a selected collection.
 * </p>
 */
public class US019ToPubliclyShareCollectionOfPublicationsController {

    CollectionOfPublicationsRepository collectionOfPublicationsRepository;

    public US019ToPubliclyShareCollectionOfPublicationsController(CollectionOfPublicationsRepository collectionOfPublicationsRepository) {
        this.collectionOfPublicationsRepository = collectionOfPublicationsRepository;
    }

    /**
     * US019 - Retrieves all collections of publications owned by a given user and converts them to DTOs.
     *
     * @param user the user whose collections are being requested.
     * @return a list of {@link CollectionOfPublicationsDTO_US019} representing the user's collections.
     * @throws IllegalArgumentException if {@code user} is {@code null}.
     */
    public List<CollectionOfPublicationsDTO_US019> requestAllCollectionsOfPublicationsByUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<CollectionOfPublications> collections = collectionOfPublicationsRepository.getAllCollectionOfPublicationsByUser(user);

        List<CollectionOfPublicationsDTO_US019> collectionsDTO = new ArrayList<>();

        collections.forEach( collection -> {
            collectionsDTO.add(CollectionOfPublicationsDTO_US019.toDTO(collection));
        });

        return collectionsDTO;
    }

    /**
     * US019 - Shares a collection of publications publicly, identified by the provided DTO.
     * <p>
     * Validates the input DTO and attempts to locate the corresponding {@link CollectionOfPublications}
     * in the repository.
     * </p>
     *
     * @param collectionDTO the DTO that identifies the collection to be shared.
     * @return {@code true} if the request is accepted and the collection is found.
     * @throws IllegalArgumentException if {@code collectionDTO} is {@code null}.
     * @throws InstanceNotFoundException if the DTO does not match any existing collection.
     */
    public boolean shareCollectionOfPublications(CollectionOfPublicationsDTO_US019 collectionDTO) throws InstanceNotFoundException {

        if (collectionDTO == null) {
            throw new IllegalArgumentException("you must send a collection to share");
        }

        CollectionOfPublications collectionToShare = collectionOfPublicationsRepository.getCollectionByDTO(collectionDTO);

        return true;
    }

    }

