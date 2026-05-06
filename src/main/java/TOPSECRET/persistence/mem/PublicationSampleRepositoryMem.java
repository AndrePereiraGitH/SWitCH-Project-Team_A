package TOPSECRET.persistence.mem;


import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;


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

    private List<PublicationSample> allPublicationsInSystem;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a new {@code PublicationSampleRepositoryMem}
     * Initializes an empty internal list of publication samples.
     *
     */
    public PublicationSampleRepositoryMem() {
        allPublicationsInSystem = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public PublicationSample save(PublicationSample publicationSample) {
        if (existsById(publicationSample.id())) {
            throw new IllegalArgumentException("PublicationSample already exists in the repository");
        }
        allPublicationsInSystem.add(publicationSample);
        return publicationSample;
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsById(PublicationSampleId publicationSampleId) {
        if (publicationSampleId == null) {
            throw new IllegalArgumentException("PublicationSample cannot be null");
        }

        for(PublicationSample pbSample: allPublicationsInSystem){
            if(pbSample.id().equals(publicationSampleId)){
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // Identity Lookup
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Optional<PublicationSample> findById(PublicationSampleId pbSampleId) {
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
    public List<PublicationSampleId> findIdsByBibliographicalDataId(BibliographicalDataId bdId){
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
    public List<PublicationSampleId> findIdsByEditionIdIn(List<EditionId> listOfEditionIds) {
        if (listOfEditionIds == null) {
            throw new NullPointerException("listOfEditionIds cannot be null");
        }

        List<PublicationSampleId> listOfPubSampleIds = new ArrayList<>();

        for(EditionId editionId: listOfEditionIds) {
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
    public List<PublicationSampleId> findIdsByBibliographicalDataIdIn(List<BibliographicalDataId> bdIds) {
        if(bdIds == null) {
            throw new IllegalArgumentException("BibliographicalDataID List cannot be null");
        }

        List<PublicationSampleId> publicationSampleIds = new ArrayList<>();

        for (PublicationSample pbSample : allPublicationsInSystem){

            BibliographicalDataId bdId = pbSample.getBibliographicalDataID();

            for(BibliographicalDataId bdId2: bdIds){
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
    public List<PublicationSample> findAll() {
        return List.copyOf(allPublicationsInSystem);
    }


    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> findByUserIdAndLibraryIdIsNotNull(UserId userId){

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
    public List<PublicationSample> findByUserIdAndLibraryIdIsNull(UserId userId){

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
    public List<PublicationSampleId> findPublicationSamplesIdsByBiblioDataIds(List<BibliographicalDataId> bds) {

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
    public List<PublicationSample> findByLibraryId(LibraryId libraryID) {

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
    public List<PublicationSample> findAvailableForSaleByLibraryId(LibraryId libraryId) {

        List<PublicationSample> allPublications = findByLibraryId(libraryId);

        List<PublicationSample> listOfAvailablePublications = new ArrayList<>();

        for (PublicationSample pbSample : allPublications) {
            if(pbSample.isAvailableForSale()) {
                listOfAvailablePublications.add(pbSample);
            }
        }
        return listOfAvailablePublications;
    }

    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> findByCollectionOfPublicationsId(CollectionOfPublicationsId collectionID) {

        List<PublicationSample> allPublications = allPublicationsInSystem.stream()
                .filter(p -> Objects.equals(p.getCollecOfPubID(), collectionID))
                .toList();

        return allPublications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean assignPublicationsToCollection(
            CollectionOfPublicationsId collectionId,
            List<PublicationSampleId> publicationSampleIds) {

        if (collectionId == null) {
            throw new IllegalArgumentException("Collection ID cannot be null.");
        }

        if (publicationSampleIds == null || publicationSampleIds.isEmpty()) {
            throw new IllegalArgumentException("Publication IDs cannot be null or empty.");
        }

        for (PublicationSampleId id : publicationSampleIds) {

            Optional<PublicationSample> publicationOptional =
                    findById(id);

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
    public List<PublicationSample> findAvailableForCollection(
            UserId userId,
            CollectionOfPublicationsId collectionId) {

        if (userId == null || collectionId == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        List<PublicationSample> allUserPublications =
                findByUserIdAndLibraryIdIsNotNull(userId);

        List<PublicationSample> publicationsInCollection =
                findByCollectionOfPublicationsId(collectionId);

        List<PublicationSample> result = new ArrayList<>();

        for (PublicationSample p : allUserPublications) {
            if (!publicationsInCollection.contains(p)) {
                result.add(p);
            }
        }

        return result;
    }

}