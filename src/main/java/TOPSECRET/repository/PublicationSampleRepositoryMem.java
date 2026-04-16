package TOPSECRET.repository;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * In-memory implementation of {@link IPublicationSampleRepository}.
 *
 * <p>This class stores {@link PublicationSample} objects in an internal
 * collection and uses a {@link PublicationSampleFactory} to create
 * new instances.</p>
 *
 * <p>It provides basic operations to manage publication samples such as creation,
 * addition, retrieval, and existence verification, without any persistence mechanism.</p>
 */
public class PublicationSampleRepositoryMem implements IPublicationSampleRepository {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private final PublicationSampleFactory _publicationSampleFactory;
    private List<PublicationSample> allPublicationsInSystem;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a new {@code PublicationSampleRepositoryMem} with the given factory.
     * Initializes an empty internal list of publication samples.
     *
     * @param publicationSampleFactory the factory used to create {@link PublicationSample} instances
     */
    public PublicationSampleRepositoryMem(PublicationSampleFactory publicationSampleFactory) {
        if (publicationSampleFactory == null) {
            throw new NullPointerException("PublicationSampleFactory can not be null");
        }
        allPublicationsInSystem = new ArrayList<>();
        _publicationSampleFactory = publicationSampleFactory;
    }

    // -------------------------------------------------------------------------
    // Construction (createAndAdd)
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public PublicationSample createAndAdd(BibliographicalDataID bibliographicalDataID, UserId userID,
                                          PublicationStatus status, EditionID editionID,
                                          Condition condition) throws InstanceAlreadyExistsException {
        if (bibliographicalDataID == null) {
            throw new IllegalArgumentException("BibliographicalDataID cannot be null");
        }
        if (userID == null) {
            throw new IllegalArgumentException("UserID cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Publication Status cannot be null");
        }
        if (editionID == null) {
            throw new IllegalArgumentException("EditionID cannot be null");
        }
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }

        PublicationSample newPublicationSample = _publicationSampleFactory.create(
                bibliographicalDataID, userID, status, editionID, condition);

        return save(newPublicationSample);
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public PublicationSample save(PublicationSample publicationSample) {
        if (isExistent(publicationSample)) {
            throw new IllegalArgumentException("PublicationSample already exists in the repository");
        }
        allPublicationsInSystem.add(publicationSample);
        return publicationSample;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isExistent(PublicationSample publicationSample) {
        if (publicationSample == null) {
            throw new IllegalArgumentException("PublicationSample cannot be null");
        }
        return allPublicationsInSystem.contains(publicationSample);
    }

    // -------------------------------------------------------------------------
    // Identity Lookup
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Optional<PublicationSample> findPublicationSampleById(PublicationSampleId pbSampleId) {
        if (pbSampleId == null) {
            throw new NullPointerException("PublicationSampleID cannot be null");
        }

        for(PublicationSample pbSample: allPublicationsInSystem){
            if(pbSample.id().equals(pbSampleId)){
                return Optional.of(pbSample);
            }
        }
        return Optional.empty();
    }


    /**
     * {@inheritDoc}
     */
    public List<PublicationSampleId> findPubSampleIdsByBibliographicalDataId(BibliographicalDataID bdId){
        if (bdId == null) {
            throw new NullPointerException("PublicationSampleID cannot be null");
        }

        List<PublicationSampleId> listOfPubSampleId = new ArrayList<>();

        for(PublicationSample pbSample:allPublicationsInSystem){
            if(pbSample.getBibliographicalDataID().equals(bdId)){
                listOfPubSampleId.add(pbSample.id());
            }
        }
        return listOfPubSampleId;

    }

    /**
     * {@inheritDoc}
     */
    public List<PublicationSampleId> findPublicationSampleIdsByEditionIds (List<EditionID> listOfEditionIds) {
        if (listOfEditionIds == null) {
            throw new NullPointerException("listOfEditionIds cannot be null");
        }

        List<PublicationSampleId> listOfPubSampleIds = new ArrayList<>();

        for(EditionID editionId: listOfEditionIds) {
            for (PublicationSample pbSample : allPublicationsInSystem) {
                if (pbSample.getEditionID().equals(editionId)) {
                    listOfPubSampleIds.add(pbSample.id());
                }
            }
        }
        return listOfPubSampleIds;
    }

    // -------------------------------------------------------------------------
    // Specific Queries
    // -------------------------------------------------------------------------

    @Override
    public List<PublicationSampleId> findAllItemsIdByBibliographicalDataId(List<BibliographicalDataID> bdIds) {
        if(bdIds == null) {
            throw new IllegalArgumentException("BibliographicalDataID List cannot be null");
        }

        List<PublicationSampleId> publicationSampleIds = new ArrayList<>();

        for (PublicationSample pbSample : allPublicationsInSystem){

            BibliographicalDataID bdId = pbSample.getBibliographicalDataID();

            for(BibliographicalDataID bdId2: bdIds){
                if(bdId.equals(bdId2)){
                    publicationSampleIds.add(pbSample.id());
                    break;
                }
            }
        }

        return publicationSampleIds;
    }

    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> getAllPublications() {
        return List.copyOf(allPublicationsInSystem);
    }


    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> getAllPublicationsInUserLibrary(UserId userId){

        if(userId == null){
            throw new NullPointerException("UserId must not be null");
        }

        List<PublicationSample> listOfPublications = new ArrayList<>();

        for(PublicationSample pubSample: allPublicationsInSystem) {
            if(pubSample.isOwnedByUser(userId) && pubSample.isInUserLibrary()) {
                listOfPublications.add(pubSample);
            }
        }

        return listOfPublications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> findByBibliographicalData(List<BibliographicalData> bibliographicalData) {

        if (bibliographicalData == null) {
            throw new IllegalArgumentException("bibliographicalData must not be null");
        }

        List<PublicationSample> result = new ArrayList<>();

        for (PublicationSample ps : allPublicationsInSystem) {

            for (BibliographicalData bd : bibliographicalData) {

                if (ps.getBibliographicalDataID().equals(bd.id())) {
                    result.add(ps);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
//    public List<PublicationSample> getListOfDifferentPublications(List<PublicationSample> listOfPublicationsInLibrary) {
//        List<PublicationSample> listOfDifferentPublications = new ArrayList<>();
//
//        for (PublicationSample publication : allPublicationsInSystem) {
//            if (!listOfPublicationsInLibrary.contains(publication)) {
//                listOfDifferentPublications.add(publication);
//            }
//        }
//        return List.copyOf(listOfDifferentPublications);
//    }
    public List<PublicationSample> getAllPublicationsFromUserWithoutLibrary(UserId userId){

        if(userId == null){
            throw new NullPointerException("UserId must not be null");
        }

        List<PublicationSample> listOfPublicationsNotInLibrary = new ArrayList<>();

        for(PublicationSample pubSample: allPublicationsInSystem){
            if(pubSample.isOwnedByUser(userId) && !pubSample.isInUserLibrary()){
                listOfPublicationsNotInLibrary.add(pubSample);
            }
        }

        return listOfPublicationsNotInLibrary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSampleId> findPublicationSamplesIdsByBiblioDataIds(List<BibliographicalDataID> bds) {

        if (bds == null) {
            throw new IllegalArgumentException("List of BibliographicalDataId cannot be null.");
        }

        List<PublicationSampleId> samplesOfAuthor = allPublicationsInSystem.stream()
                .filter(pub -> bds.contains(pub.getBibliographicalDataID()))
                .map(PublicationSample::id)
                .toList();

        return samplesOfAuthor;
    }

    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> getPublicationsByLibraryID(LibraryId libraryID) {

        List<PublicationSample> allPublications = allPublicationsInSystem.stream()
                .filter(p -> Objects.equals(p.getLibraryID(), libraryID))
                .toList();

        return allPublications;
    }

    /**
     * {@inheritDoc}
     */
    public boolean addPublicationToLibrary(PublicationSample publicationSample, LibraryId libraryId){

        if (publicationSample == null) {
            throw new IllegalArgumentException("PublicationSample cannot be null");
        }

        if (libraryId == null) {
            throw new IllegalArgumentException("LibraryId cannot be null");
        }

        publicationSample.addPublicationToLibrary(libraryId);

        save(publicationSample);

        return true;
    }


    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> findPublicationSamplesAvailableForSaleByLibraryId(LibraryId libraryId) {

        List<PublicationSample> allPublications = getPublicationsByLibraryID(libraryId);

        List<PublicationSample> listOfAvailablePublications = new ArrayList<>();

        for (PublicationSample pbSample : allPublications) {
            if(pbSample.isAvailableForSale()) {
                listOfAvailablePublications.add(pbSample);
            }
        }
        return listOfAvailablePublications;
    }

    // ------------------ US018 starts -----------------
    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> getPublicationsByCollectionID (CollectionOfPublicationsID collectionID) {

        List<PublicationSample> allPublications = allPublicationsInSystem.stream()
                .filter(p -> Objects.equals(p.getCollecOfPubID(), collectionID))
                .toList();

        return allPublications;
    }

    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> findItemsByIdsAndPublication(List<PublicationSampleId> pubSampleIds, BibliographicalDataID publicationId) {

        List<PublicationSample> pubSamplesList = new ArrayList<>();

        if (pubSampleIds == null) {
            throw new IllegalArgumentException("PublicationSampleIds cannot be null");
        }
        if (publicationId == null) {
            throw new IllegalArgumentException("PublicationId cannot be null");
        }

        for (PublicationSampleId psId : pubSampleIds) {

            Optional<PublicationSample> psOpt = findPublicationSampleById(psId);

            if (psOpt.isEmpty())
                continue;           // if it doesn't find a corresponding pubSample, jumps the rest of the cycle

            PublicationSample ps = psOpt.get();

            if (ps.getBibliographicalDataID().equals(publicationId)) {
                pubSamplesList.add(ps);
            }
        }
        return List.copyOf(pubSamplesList);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean assignPublicationsToCollection(
            CollectionOfPublicationsID collectionId,
            List<PublicationSampleId> publicationSampleIds) {

        if (collectionId == null) {
            throw new IllegalArgumentException("Collection ID cannot be null.");
        }

        if (publicationSampleIds == null || publicationSampleIds.isEmpty()) {
            throw new IllegalArgumentException("Publication IDs cannot be null or empty.");
        }

        for (PublicationSampleId id : publicationSampleIds) {

            Optional<PublicationSample> publicationOptional =
                    findPublicationSampleById(id);

            if (publicationOptional.isEmpty()) {
                throw new IllegalArgumentException("Publication not found.");
            }

            PublicationSample publication = publicationOptional.get();
            publication.addPublicationToCollection(collectionId);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> getAvailablePublicationsForCollection(
            UserId userId,
            CollectionOfPublicationsID collectionId) {

        if (userId == null || collectionId == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        List<PublicationSample> allUserPublications =
                getAllPublicationsInUserLibrary(userId);

        List<PublicationSample> publicationsInCollection =
                getPublicationsByCollectionID(collectionId);

        List<PublicationSample> result = new ArrayList<>();

        for (PublicationSample p : allUserPublications) {
            if (!publicationsInCollection.contains(p)) {
                result.add(p);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> findPublicationSampleByBibliographicalDataIds(List<BibliographicalDataID> bdIds) {
        if (bdIds == null) {
            throw new IllegalArgumentException("List of BibliographicalDataID cannot be null");
        }

        List<PublicationSample> result = new ArrayList<>();

        for (PublicationSample ps : allPublicationsInSystem) {
            for (BibliographicalDataID bdId : bdIds) {
                if (ps.getBibliographicalDataID().equals(bdId)) {
                    result.add(ps);
                    break;
                }
            }
        }

        return result;
    }

    // --------------------- US018 ends --------------------------
}