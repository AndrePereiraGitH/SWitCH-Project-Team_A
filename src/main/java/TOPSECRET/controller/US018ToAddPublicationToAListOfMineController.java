package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US018CollectionOfPublicationsDTO;
import TOPSECRET.dto.US018PublicationSampleDTO;
import TOPSECRET.dto.US018CollectionOfPublicationsMapper;
import TOPSECRET.dto.US018PublicationSampleMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for the use case:
 * <b>US018 - Add Publication to Collection</b>.
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
public class US018ToAddPublicationToAListOfMineController {

    private final ICollectionOfPublicationsRepository _collectionRepo;
    private final IPublicationSampleRepository _publicationSampleRepo;
    private final IBibliographicalDataRepository _bibliographicalRepo;
    private final UserId _userId;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param userId identifier of the current user
     * @param collectionRepo repository for collections
     * @param publicationRepo repository for publications
     * @param bibliographicalRepo repository for bibliographical data
     *
     * @throws IllegalArgumentException if any argument is null
     */
    public US018ToAddPublicationToAListOfMineController(
            UserId userId,
            ICollectionOfPublicationsRepository collectionRepo,
            IPublicationSampleRepository publicationRepo,
            IBibliographicalDataRepository bibliographicalRepo) {

        if (userId == null || collectionRepo == null ||
                publicationRepo == null || bibliographicalRepo == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        this._userId = userId;
        this._collectionRepo = collectionRepo;
        this._publicationSampleRepo = publicationRepo;
        this._bibliographicalRepo = bibliographicalRepo;
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
     * The method delegates the retrieval of available publications to the repository,
     * obtains their titles from the bibliographical repository, and converts the result
     * into DTOs.
     * </p>
     *
     * @param collectionId identifier of the selected collection
     * @return list of available publication DTOs
     *
     * @throws IllegalArgumentException if collectionId is null
     */
    public List<US018PublicationSampleDTO> getPublicationsForCollection(
            CollectionOfPublicationsID collectionId) {

        if (collectionId == null) {
            throw new IllegalArgumentException("collectionId cannot be null");
        }

        List<PublicationSample> available =
                _publicationSampleRepo.getAvailablePublicationsForCollection(_userId, collectionId);

        List<String> titles = getTitles(available);

        return US018PublicationSampleMapper.toDTOs(available, titles);
    }

    /**
     * Retrieves the titles associated with a list of publications.
     *
     * <p>
     * For each publication, the method obtains the corresponding
     * bibliographical data and extracts its title.
     * </p>
     *
     * @param publications list of publication samples
     * @return list of titles in the same order as the publications
     */
    private List<String> getTitles(List<PublicationSample> publications) {

        List<String> titles = new ArrayList<>();

        for (PublicationSample publication : publications) {

            BibliographicalDataID bibliographicalDataId =
                    publication.getBibliographicalDataID();

            String title = _bibliographicalRepo
                    .findBibliographicalDataById(bibliographicalDataId)
                    .get()
                    .getTitle();

            titles.add(title);
        }

        return titles;
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
            CollectionOfPublicationsID collectionId,
            List<PublicationSampleId> selectedPublicationId) {

        if (collectionId == null ||
                selectedPublicationId == null ||
                selectedPublicationId.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return _publicationSampleRepo.assignPublicationsToCollection(collectionId, selectedPublicationId);
    }

    /**
     * Computes the difference between two lists of publications.
     *
     * @param all all publications
     * @param inCollection publications already in collection
     * @return publications not yet in collection
     */
    private List<PublicationSample> diff(
            List<PublicationSample> all,
            List<PublicationSample> inCollection) {

        List<PublicationSample> result = new ArrayList<>();

        for (PublicationSample p : all) {
            if (!inCollection.contains(p)) {
                result.add(p);
            }
        }

        return result;
    }

}