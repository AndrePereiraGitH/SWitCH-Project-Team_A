package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US019CollectionOfPublicationsDTO;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.dto.US019CollectionOfPublicationsMapper;
import TOPSECRET.services.CollectionOfPublicationsService;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Controller for US019: Publicly share a user's collection of publications.
 * <p>
 * This controller orchestrates the retrieval of a user's {@link CollectionOfPublications} from the
 * {@link ICollectionOfPublicationsRepository}, converts them into {@link US019CollectionOfPublicationsDTO}
 * for presentation, and handles the request to make a selected collection public.
 * </p>
 */
public class ToPubliclyShareCollectionOfPublicationsController {

    private final ICollectionOfPublicationsRepository _collectionRepo;
    private final CollectionOfPublicationsService collectionOfPublicationsService;

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
     * Retrieves all collections of publications owned by a given user and converts them to DTOs.
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

    /**
     * Makes the selected collection of publications public.
     *
     * <p>This method validates the selected collection DTO and delegates the operation
     * to the collection of publications service using the collection identifier
     * contained in the DTO.</p>
     *
     * @param selectedCollectionDTO the DTO representing the collection to be made public;
     *                              must not be null
     * @return {@code true} if the collection was changed to public;
     *         {@code false} if it was already public
     * @throws IllegalArgumentException if {@code selectedCollectionDTO} is null
     * @throws InstanceNotFoundException if no collection is found with the given identifier
     */
//    public boolean makeCollectionPublic(US019CollectionOfPublicationsDTO selectedCollectionDTO)
//            throws InstanceNotFoundException {
//
//        if (selectedCollectionDTO == null) {
//            throw new IllegalArgumentException("CollectionId cannot be null");
//        }
//
//        return collectionOfPublicationsService.makeCollectionPublic(selectedCollectionDTO.id());
//    }
//
//    /**
//     * Shares a collection of publications publicly until the given date and time.
//     *
//     * <p>This method validates the selected collection identifier and the expiration
//     * date, then delegates the operation to the collection of publications service.</p>
//     *
//     * @param selectedCollectionDTO the identifier of the collection to be shared;
//     *                             must not be null
//     * @param publicUntil the date and time until which the collection will remain public;
//     *                    must not be null
//     * @return {@code true} if the collection was changed;
//     *         {@code false} if it was already public until the same date and time
//     * @throws NullPointerException if {@code selectedCollectionId} or {@code publicUntil} is null
//     * @throws InstanceNotFoundException if no collection is found with the given identifier
//     */
//    public boolean shareCollectionUntil(US019CollectionOfPublicationsDTO selectedCollectionDTO, LocalDateTime publicUntil) throws InstanceNotFoundException {
//
//        Objects.requireNonNull(selectedCollectionDTO, "selected collection Id cannot be null");
//        Objects.requireNonNull(publicUntil, "public until date time cannot be null");
//
//
//        return collectionOfPublicationsService.makeCollectionPublicUntil(selectedCollectionDTO.id(), publicUntil);
//    }
}

