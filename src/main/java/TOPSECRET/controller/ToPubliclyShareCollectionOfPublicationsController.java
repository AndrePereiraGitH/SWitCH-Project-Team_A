package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US019CollectionOfPublicationsDTO;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.dto.US019CollectionOfPublicationsMapper;
import TOPSECRET.services.CollectionOfPublicationsService;

import javax.management.InstanceNotFoundException;
import java.util.List;

/**
 * Controller for US019: Publicly share a user's collection of publications.
 * <p>
 * This controller orchestrates the retrieval of a user's {@link CollectionOfPublications} from the
 * {@link ICollectionOfPublicationsRepository}, converts them into {@link US019CollectionOfPublicationsDTO}
 * for presentation, and handles the request to make a selected collection public.
 * </p>
 */
public class ToPubliclyShareCollectionOfPublicationsController {

    ICollectionOfPublicationsRepository _collectionRepo;
    CollectionOfPublicationsService collectionOfPublicationsService;

    public ToPubliclyShareCollectionOfPublicationsController(ICollectionOfPublicationsRepository collectionRepo, CollectionOfPublicationsService collectionService) {
        if (collectionRepo == null) {
            throw new NullPointerException("CollectionOfPublicationsRepository cannot be null");
        }
        if (collectionService == null) {
            throw new IllegalArgumentException("CollectionOfPublicationService cannot be null");
        }
        this._collectionRepo = collectionRepo;
        this.collectionOfPublicationsService = collectionService;
    }

    /**
     * US019 - Retrieves all collections of publications owned by a given user and converts them to DTOs.
     *
     * <p>
     * The repository already filters by owner, so no additional ownership
     * check is needed here.
     * </p>
     *
     * @param userId the ID of the user whose collections are requested; must not be null
     * @return a list of {@link US019CollectionOfPublicationsDTO}; never null, may be empty
     * @throws IllegalArgumentException if {@code userId} is null
     */
    public List<US019CollectionOfPublicationsDTO> getUserCollections(UserId userId) {

        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        List<CollectionOfPublications> collections =
                _collectionRepo.findAllByOwnerId(userId);

        return US019CollectionOfPublicationsMapper.toDTOs(collections);
    }


    public boolean makeCollectionPublic(US019CollectionOfPublicationsDTO selectedCollectionDTO) throws InstanceNotFoundException {

        if (selectedCollectionDTO == null) {
            throw new IllegalArgumentException("CollectionId cannot be null");
        }

        return collectionOfPublicationsService.makeCollectionPublic(selectedCollectionDTO.id());
    }
}

