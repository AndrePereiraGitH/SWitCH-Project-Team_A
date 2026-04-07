package TOPSECRET.repository;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.PublicationSampleDTO_US016;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
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

    // ---------------------------------------- Fields -------------------------------------------

    private final PublicationSampleFactory _publicationSampleFactory;
    private List<PublicationSample> allPublicationsInSystem;

    // -------------------------------------- Constructor ----------------------------------------

    /**
     * Constructs a new {@code PublicationSampleRepositoryMem} with the given factory.
     * Initializes an empty internal list of publication samples.
     *
     * @param publicationSampleFactory the factory used to create {@link PublicationSample} instances
     */
    public PublicationSampleRepositoryMem(PublicationSampleFactory publicationSampleFactory) {
        if(publicationSampleFactory == null){
            throw new NullPointerException("PublicationSampleFactory can not be null");
        }
        allPublicationsInSystem = new ArrayList<>();
        _publicationSampleFactory = publicationSampleFactory;
    }

    // ----------------------------------- Core CRUD Methods -------------------------------------

    /**
     * {@inheritDoc}
     */
    public PublicationSample createAndAdd(BibliographicalDataID bibliographicalDataID, UserId userID, PublicationStatus status, EditionID editionID, Condition condition) throws InstanceAlreadyExistsException {

        if(bibliographicalDataID == null){
            throw new IllegalArgumentException("BibliographicalDataID cannot be null");
        }
        if(userID == null){
            throw new IllegalArgumentException("UserID cannot be null");
        }
        if(status == null){
            throw new IllegalArgumentException("Publication Status cannot be null");
        }
        if(editionID == null){
            throw new IllegalArgumentException("EditionID cannot be null");
        }
        if(condition == null){
            throw new IllegalArgumentException("Condition cannot be null");
        }

        PublicationSample newPublicationSample = _publicationSampleFactory.create(bibliographicalDataID, userID, status, editionID, condition);

        return save(newPublicationSample);
    }

    public PublicationSample save(PublicationSample publicationSample){

        if(isExistent(publicationSample)){
            throw new IllegalArgumentException("PublicationSample already exists in the repository");
        }
        allPublicationsInSystem.add(publicationSample);
        return publicationSample;
    }

    // ----------------------------------- Query Methods -----------------------------------------

    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> getAllPublications() {
        return List.copyOf(allPublicationsInSystem);
    }

    /**
     * {@inheritDoc}
     */
    public Optional<PublicationSample> findById(PublicationSampleID id) {
        if (id == null) {
            throw new NullPointerException("PublicationSampleID cannot be null");
        }
        return allPublicationsInSystem.stream()
                .filter(ps -> id.equals(ps.id()))
                .findFirst();
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

    /**
     * {@inheritDoc}
     */
//    public List<PublicationSample> getListOfDifferentPublications(List<PublicationSample> listOfPublicationsInLibrary){
//        List<PublicationSample> listOfDifferentPublications = new ArrayList<>();
//
//        for (PublicationSample publication : allPublicationsInSystem) {
//            //If the user library does not contain the publication
//            if (!listOfPublicationsInLibrary.contains(publication)){
//                listOfDifferentPublications.add(publication);
//            }
//        }
//        return List.copyOf(listOfDifferentPublications);
//    }

    /**
     * {@inheritDoc}
     */
//    public List<PublicationSample> getPublicationsByDTO(List<PublicationSampleDTO_US016> pubsDto) {
//
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

    /**
     * {@inheritDoc}
     */
//    public List<PublicationSample> findItemsByListingAndPublication(List<Listing> listings, BibliographicalData publication){
//
//        List<PublicationSample> pubSamplesList = new ArrayList<>();
//
//        if (listings == null) {
//            throw new IllegalArgumentException("List of listings cannot be null");
//        }
//        if (publication == null){
//            throw new IllegalArgumentException("Publication cannot be null");
//        }
//
//        for (Listing listing : listings) {
//            for  (PublicationSample pubSample : listing.getItems()) {
//                if(pubSample.getBibliographicalDataID().equals(publication)){
//                    pubSamplesList.add(pubSample);
//                }
//            }
//        }
//        return List.copyOf(pubSamplesList);
//    }

    // --------------------------------------- US018 Methods -------------------------------------

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
//    public List<PublicationSample> findItemsByListingAndPublisher(List<Listing> listings, Publisher publisher){
//
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
//            for  (PublicationSample pubSample : listing.getItems()) {
//
//                if (pubSample == null) {
//                    throw new IllegalArgumentException("Listings must not contain null PublicationSample items");
//                }
//
//                if(pubSample.getEdition().getPublisher().equals(publisher)){
//                    pubSamplesList.add(pubSample);
//                }
//            }
//        }
//        return List.copyOf(pubSamplesList);
//    }

    /**
     * {@inheritDoc}
     */
    public List<PublicationSample> findByLibraryId(LibraryId libraryId) {

        List<PublicationSample> libraryPublicationSamples = new ArrayList<>();

        if (libraryId == null){
            throw new IllegalArgumentException("LibraryId cannot be null");
        }

        for (PublicationSample publicationSample : allPublicationsInSystem){
            if (libraryId.equals(publicationSample.getLibraryID())) {
                libraryPublicationSamples.add(publicationSample);
            }
        }
        return List.copyOf(libraryPublicationSamples);
    }
}