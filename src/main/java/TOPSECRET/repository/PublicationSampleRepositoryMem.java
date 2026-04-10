package TOPSECRET.repository;


import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
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
    public List<PublicationSample> getPublicationsByLibraryID(LibraryId libraryID) {

        List<PublicationSample> allPublications = allPublicationsInSystem.stream()
                .filter(p -> Objects.equals(p.getLibraryID(), libraryID))
                .toList();

        return allPublications;
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

    /**
     * {@inheritDoc}
     */
//    public List<PublicationSample> getPublicationsByDTO(List<PublicationSampleDTO_US016> pubsDto) {
//        if (pubsDto == null || pubsDto.isEmpty()) {
//            throw new IllegalArgumentException("list of PublicationSampleDTO cannot be null or empty");
//        }
//
//        List<PublicationSample> matched = allPublicationsInSystem.stream()
//                .filter(ps -> pubsDto.stream().anyMatch(dto -> matches(ps, dto)))
//                .toList();
//
//        return matched;
//    }

    /**
     * {@inheritDoc}
     */
//    public List<PublicationSample> findItemsByListingAndPublication(List<Listing> listings, BibliographicalData publication) {
//        List<PublicationSample> pubSamplesList = new ArrayList<>();
//
//        if (listings == null) {
//            throw new IllegalArgumentException("List of listings cannot be null");
//        }
//        if (publication == null) {
//            throw new IllegalArgumentException("Publication cannot be null");
//        }
//
//        for (Listing listing : listings) {
//            for (PublicationSample pubSample : listing.getItems()) {
//                if (pubSample.getBibliographicalDataID().equals(publication)) {
//                    pubSamplesList.add(pubSample);
//                }
//            }
//        }
//        return List.copyOf(pubSamplesList);
//    }




    // --------------------------------------- US018 starts ----------------------------------------

    /**
     * {@inheritDoc}
     */
//    public PublicationSample PublicationSampleDtoToDomain(
//            Library userLibrary,
//            PublicationSampleDTO_US018 dto) {
//
//        if (userLibrary == null)
//            throw new IllegalArgumentException("Library cannot be null");
//
//        if (dto == null)
//            throw new IllegalArgumentException("DTO cannot be null");
//
//        for (PublicationSample publication : userLibrary.getAllPublications()) {
//
//            if (publication.getBibliographicalData().getTitle().equals(dto.getTitle())
//                    && publication.getEdition().getEditionNumber().equals(dto.getEditionNumber())
//                    && publication.getOwnerId().getUserName().equals(dto.getOwnerUsername())) {
//
//                return publication;
//            }
//        }
//
//        throw new IllegalArgumentException("Publication not found in user's library");
//    }

    /**
     * {@inheritDoc}
     */
//    public List<PublicationSample> findItemsByListingAndPublisher(List<Listing> listings, Publisher publisher) {
//        List<PublicationSample> pubSamplesList = new ArrayList<>();
//
//        if (listings == null) {
//            throw new IllegalArgumentException("List of listings cannot be null");
//        }
//        if (publisher == null) {
//            throw new IllegalArgumentException("Publisher cannot be null");
//        }
//        for (Listing listing : listings) {
//            if (listing == null)
//                throw new IllegalArgumentException("Listings cannot contain null elements");
//        }
//
//        for (Listing listing : listings) {
//            for (PublicationSample pubSample : listing.getItems()) {
//                if (pubSample == null) {
//                    throw new IllegalArgumentException("Listings must not contain null PublicationSample items");
//                }
//                if (pubSample.getEdition().getPublisher().equals(publisher)) {
//                    pubSamplesList.add(pubSample);
//                }
//            }
//        }
//        return List.copyOf(pubSamplesList);
//    }

    /**
     * {@inheritDoc}
     */
//    public PublicationSample PublicationSampleDtoToDomain(Library userLibrary, PublicationSampleDTO_US018 dto) {
//        if (userLibrary == null)
//            throw new IllegalArgumentException("Library cannot be null");
//        if (dto == null)
//            throw new IllegalArgumentException("DTO cannot be null");
//
//        for (PublicationSample publication : userLibrary.getAllPublications()) {
//            if (publication.getBibliographicalData().getTitle().equals(dto.getTitle())
//                    && publication.getEdition().getEditionNumber().equals(dto.getEditionNumber())
//                    && publication.getOwnerId().getUserName().equals(dto.getOwnerUsername())) {
//                return publication;
//            }
//        }
//        throw new IllegalArgumentException("Publication not found in user's library");
//    }

    // -------------------------------------------------------------------------
    // Private Helper Methods
    // -------------------------------------------------------------------------

    /**
     * Determines whether a {@link PublicationSample} matches the criteria defined in a
     * {@link PublicationSampleDTO_US016}.
     * <p>
     * Equality is checked across: title, author names (order-sensitive), original language,
     * genre types (order-sensitive), owner username, and condition description.
     * </p>
     *
     * @param ps  the publication sample to evaluate; must not be {@code null}
     * @param dto the DTO containing the filter criteria; must not be {@code null}
     * @return {@code true} if all fields match; {@code false} otherwise
     */
//    private static boolean matches(PublicationSample ps, PublicationSampleDTO_US016 dto) {
//        BibliographicalData bd = ps.getBibliographicalData();
//
//        List<String> authorNames = bd.getAuthors().stream()
//                .map(a -> a.getName())
//                .toList();
//
//        List<String> genreTypes = bd.getGenres().stream()
//                .map(Genre::getGenreType)
//                .toList();
//
//        return Objects.equals(bd.getTitle(), dto.getTitle())
//                && Objects.equals(authorNames, dto.getAuthorName())
//                && Objects.equals(bd.getOriginalLanguage(), dto.getLanguage())
//                && Objects.equals(genreTypes, dto.getGenreType())
//                && Objects.equals(ps.getOwnerId().getUserName(), dto.getOwnerName())
//                && Objects.equals(ps.getCondition().getDescription(), dto.getCondition());
//    }
}