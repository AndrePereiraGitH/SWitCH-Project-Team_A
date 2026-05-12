package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US018CollectionOfPublicationsDTO;
import TOPSECRET.dto.US018CollectionOfPublicationsMapper;
import TOPSECRET.dto.US018PublicationSampleDTO;
import TOPSECRET.dto.US018PublicationSampleMapper;

import java.util.List;

/**
 * Controller responsible for the use case:
 * <b>Add Publication to Collection</b>.
 *
 * <p>
 * This controller acts as an orchestrator between the UI and domain layers.
 * It retrieves data from repositories, delegates transformations to mappers,
 * and invokes domain operations through repositories.
 * </p>
 *
 * <p>
 * No business logic is implemented here. All state changes are delegated
 * to the appropriate domain entities.
 * </p>
 */
public class ToAddPublicationToAListOfMineController {

    private final ICollectionOfPublicationsRepository _collectionRepo;
    private final IPublicationSampleRepository _publicationSampleRepo;
    private final US018PublicationSampleMapper _publicationSampleMapper;
    private final UserId _userId;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param userId identifier of the current user
     * @param collectionRepo repository for collections
     * @param publicationRepo repository for publication samples
     * @param publicationSampleMapper mapper for publication samples
     *
     * @throws IllegalArgumentException if any argument is null
     */
    public ToAddPublicationToAListOfMineController(
            UserId userId,
            ICollectionOfPublicationsRepository collectionRepo,
            IPublicationSampleRepository publicationRepo,
            US018PublicationSampleMapper publicationSampleMapper) {

        if (userId == null || collectionRepo == null ||
                publicationRepo == null || publicationSampleMapper == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }


        this._userId = userId;
        this._collectionRepo = collectionRepo;
        this._publicationSampleRepo = publicationRepo;
        this._publicationSampleMapper = publicationSampleMapper;
    }

    /**
     * Retrieves all collections belonging to the current user.
     *
     * <p>
     * The method fetches domain objects from the repository and converts them
     * into DTOs using the collection mapper.
     * </p>
     *
     * @return list of collection DTOs
     */
    public List<US018CollectionOfPublicationsDTO> getUserCollections() {

        List<CollectionOfPublications> userCollections =
                _collectionRepo.getUserCollections(_userId);

        return US018CollectionOfPublicationsMapper.toDTOs(userCollections);
    }

    /**
     * Retrieves the list of publications that can be added to a collection.
     *
     * <p>
     * The method delegates:
     * <ul>
     *     <li>Filtering logic to the repository</li>
     *     <li>DTO transformation to the mapper</li>
     * </ul>
     * </p>
     *
     * @param collectionId identifier of the selected collection
     * @return list of available publication DTOs
     *
     * @throws IllegalArgumentException if collectionId is null
     */
    public List<US018PublicationSampleDTO> getPublicationsForCollection(
            CollectionOfPublicationsId collectionId) {

        if (collectionId == null) {
            throw new IllegalArgumentException("collectionId cannot be null");
        }

        List<PublicationSample> available =
                _publicationSampleRepo.findAvailableForCollection(_userId, collectionId);

        return _publicationSampleMapper.toDTOs(available);
    }

    /**
     * Assigns selected publications to a collection.
     *
     * <p>
     * This method:
     * <ul>
     *     <li>Receives the collection identifier</li>
     *     <li>Receives publication identifiers</li>
     *     <li>Delegates the operation to the PublicationSample repository</li>
     * </ul>
     * </p>
     *
     * <p>
     * The actual assignment logic is handled by the domain entity
     * {@link PublicationSample}.
     * </p>
     *
     * @param collectionId identifier of the selected collection
     * @param selectedPublicationId identifiers of selected publications
     * @return true if operation succeeds
     *
     * @throws IllegalArgumentException if input is invalid
     */
    public boolean addPublicationToCollection(
            CollectionOfPublicationsId collectionId,
            List<PublicationSampleId> selectedPublicationId) {

        if (collectionId == null ||
                selectedPublicationId == null ||
                selectedPublicationId.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return _publicationSampleRepo.assignPublicationsToCollection(collectionId, selectedPublicationId);
    }
}