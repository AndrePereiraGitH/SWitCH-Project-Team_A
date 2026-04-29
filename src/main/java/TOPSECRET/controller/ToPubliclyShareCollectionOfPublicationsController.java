package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US019CollectionOfPublicationsDTO;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.dto.US019CollectionOfPublicationsMapper;
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

    public ToPubliclyShareCollectionOfPublicationsController(ICollectionOfPublicationsRepository collectionRepo) {
        if (collectionRepo == null) {
            throw new NullPointerException("CollectionOfPublicationsRepository cannot be null");
        }
        this._collectionRepo = collectionRepo;
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
//    public List<US019CollectionOfPublicationsDTO> getUserCollections(UserId userId) {
//
//        if (userId == null) {
//            throw new IllegalArgumentException("UserId cannot be null");
//        }
//
//        List<CollectionOfPublications> collections =
//                _collectionRepo.getUserCollections(userId);
//
//        return US019CollectionOfPublicationsMapper.toDTOs(collections);
//    }
//
//    /**
//     * US019 - Makes the collection identified by {@code collectionId} public,
//     * after verifying it belongs to {@code userId}.
//     *
//     * @param collectionId the ID of the collection to make public; must not be null
//     * @throws IllegalArgumentException if either parameter is null,
//     *                                  if the collection is not found,
//     *                                  or if the collection does not belong to the user
//     */
//    public boolean makeCollectionPublic(CollectionOfPublicationsID collectionId) {
//
//        if (collectionId == null) {
//            throw new IllegalArgumentException("CollectionId cannot be null");
//        }
//
//        return _collectionRepo.makeCollectionPublic(collectionId);
//    }
}

