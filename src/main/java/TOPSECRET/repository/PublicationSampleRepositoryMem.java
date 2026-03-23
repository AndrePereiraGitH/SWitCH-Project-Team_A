package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.dto.PublicationSampleDTO_US016;
import TOPSECRET.dto.PublicationSampleDTO_US018;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private final PublicationSampleFactory _publicationSampleFactory;
    private List<PublicationSample> allPublicationsInSystem;

    /**
     * Constructs a new {@code PublicationSampleRepositoryMem} with the given factory.
     * Initializes an empty internal list of publication samples.
     *
     * @param publicationSampleFactory the factory used to create {@link PublicationSample} instances
     */
    public PublicationSampleRepositoryMem(PublicationSampleFactory publicationSampleFactory) {
        allPublicationsInSystem = new ArrayList<>();
        _publicationSampleFactory = publicationSampleFactory;
    }

    /**
     * Returns an immutable copy of all {@link PublicationSample} instances currently held in the repository.
     *
     * @return an unmodifiable list of all publication samples; never {@code null}
     */
    public List<PublicationSample> getAllPublications() { //getter to obtain list of all publications in the system
        return List.copyOf(allPublicationsInSystem);
    }


    /**
     * Creates a new PublicationSample and adds it to the repository.
     *
     * @param bibliographicalData the bibliographical data of the publication (must not be null)
     * @param userName the owner of the publication (must not be null)
     * @param status the publication status (must not be null)
     * @param edition the edition of the publication (must not be null)
     * @param condition the condition of the publication (must not be null)
     * @return the created PublicationSample
     * @throws IllegalArgumentException if any required argument is null
     */
    public PublicationSample createAndAdd(BibliographicalData bibliographicalData, User userName, PublicationStatus status, Edition edition, Condition condition) throws InstanceAlreadyExistsException {

        if(bibliographicalData == null){
            throw new IllegalArgumentException("Bibliographical Data cannot be null");
        }
        if(userName == null){
            throw new IllegalArgumentException("User Name cannot be null");
        }
        if(status == null){
            throw new IllegalArgumentException("Publication Status cannot be null");
        }
        if(edition == null){
            throw new IllegalArgumentException("Edition cannot be null");
        }
        if(condition == null){
            throw new IllegalArgumentException("Condition cannot be null");
        }

        PublicationSample newPublicationSample = _publicationSampleFactory.create(bibliographicalData, userName, status, edition, condition);

        allPublicationsInSystem.add(newPublicationSample);

        return newPublicationSample;
    }

    /**
     * Adds a {@link PublicationSample} to the repository.
     * <p>
     * <b>Note:</b> This method is intended for testing purposes only
     * and should not be used in production code.
     * </p>
     *
     * @param publicationSample the publication sample to add; should not be {@code null}
     */
    public void add(PublicationSample publicationSample){
        allPublicationsInSystem.add(publicationSample);
    }

    /**
     * Checks whether the given {@link PublicationSample} already exists in the repository.
     *
     * @param pub the publication sample to look up; must not be {@code null}
     * @return {@code true} if the repository contains an equal publication sample; {@code false} otherwise
     */
    public boolean isExistent(PublicationSample pub){
        for(PublicationSample publication: allPublicationsInSystem){
            if(pub.equals(publication)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all publication samples in the repository that are <em>not</em> present
     * in the given library list.
     *
     * @param listOfPublicationsInLibrary the publications already owned by the user;
     *                                    must not be {@code null}
     * @return an immutable list of publication samples absent from the provided library;
     *         never {@code null}, but may be empty
     */
    public List<PublicationSample> getListOfDifferentPublications(List<PublicationSample> listOfPublicationsInLibrary){
        List<PublicationSample> listOfDifferentPublications = new ArrayList<>();

        for (PublicationSample publication : allPublicationsInSystem) {
            //If the user library does not contain the publication
            if (!listOfPublicationsInLibrary.contains(publication)){
                listOfDifferentPublications.add(publication);
            }
        }
        return List.copyOf(listOfDifferentPublications);

    }


    /**
     * Retrieves all {@link PublicationSample} instances that match at least one of the
     * provided {@link PublicationSampleDTO_US016} criteria.
     * <p>
     * Matching is performed across title, authors, language, genres, owner username,
     * and physical condition. See {@link #matches(PublicationSample, PublicationSampleDTO_US016)}.
     * </p>
     *
     * @param pubsDto the list of DTOs to match against; must not be {@code null} or empty
     * @return a list of matching {@link PublicationSample} instances; never {@code null}, but may be empty
     * @throws IllegalArgumentException if {@code pubsDto} is {@code null} or empty
     */
    public List<PublicationSample> getPublicationsByDTO(List<PublicationSampleDTO_US016> pubsDto) {

        if (pubsDto == null || pubsDto.isEmpty()) {
            throw new IllegalArgumentException("list of PublicationSampleDTO cannot be null or empty");
        }

        List<PublicationSample> matched = allPublicationsInSystem.stream()
                .filter(ps -> pubsDto.stream().anyMatch(dto -> matches(ps, dto)))
                .toList();

        return matched;
    }

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
    private static boolean matches(PublicationSample ps, PublicationSampleDTO_US016 dto) {
        BibliographicalData bd = ps.getBibliographicalData();

        List<String> authorNames = bd.getAuthors().stream()
                .map(a -> a.getProfile().getName())
                .toList();

        List<String> genreTypes = bd.getGenres().stream()
                .map(Genre::getGenreType)
                .toList();

        return Objects.equals(bd.getTitle(), dto.getTitle())
                && Objects.equals(authorNames, dto.getAuthorName())
                && Objects.equals(bd.getOriginalLanguage(), dto.getLanguage())
                && Objects.equals(genreTypes, dto.getGenreType())
                && Objects.equals(ps.getOwner().getUserName(), dto.getOwnerName())
                && Objects.equals(ps.getCondition().getDescription(), dto.getCondition());
    }

    /**
     * Returns all publication samples associated with the given list of listings
     * that correspond to the specified publication.
     *
     * <p>This method iterates through each listing in the provided list and inspects
     * its associated {@link PublicationSample} objects. Only the samples whose
     * bibliographical data matches the given {@code publication} are included in
     * the result.</p>
     *
     * @param listings the listings whose publication samples should be inspected;
     *                 must not be {@code null}
     * @param publication the publication to match against; must not be {@code null}
     * @return a list of publication samples belonging to the given listings and
     *         matching the specified publication; never {@code null}, but may be empty
     */
    public List<PublicationSample> findItemsByListingAndPublication(List<Listing> listings, BibliographicalData publication){

        List<PublicationSample> pubSamplesList = new ArrayList<>();

        if (listings == null) {
            throw new IllegalArgumentException("List of listings cannot be null");
        }
        if (publication == null){
            throw new IllegalArgumentException("Publication cannot be null");
        }

        for (Listing listing : listings) {
            for  (PublicationSample pubSample : listing.getItems()) {
                if(pubSample.getBibliographicalData().equals(publication)){
                    pubSamplesList.add(pubSample);
                }
            }
        }
        return List.copyOf(pubSamplesList);
    }

    // --------------------------------------- US018 stars ----------------------------------------

    /**
     * Converts a PublicationSampleDTO_US018 into
     * its corresponding PublicationSample domain object.
     *
     * @param userLibrary the user's library
     * @param dto the selected publication DTO
     * @return matching PublicationSample
     * @throws IllegalArgumentException if not found
     */
    public PublicationSample PublicationSampleDtoToDomain(
            Library userLibrary,
            PublicationSampleDTO_US018 dto) {

        if (userLibrary == null)
            throw new IllegalArgumentException("Library cannot be null");

        if (dto == null)
            throw new IllegalArgumentException("DTO cannot be null");

        for (PublicationSample publication : userLibrary.getAllPublications()) {

            if (publication.getBibliographicalData().getTitle().equals(dto.getTitle())
                    && publication.getEdition().getEditionNumber().equals(dto.getEditionNumber())
                    && publication.getOwner().getUserName().equals(dto.getOwnerUsername())) {

                return publication;
            }
        }

        throw new IllegalArgumentException("Publication not found in user's library");
    }

    // ------------------------------------------ ends --------------------------------------------

    /**
     * Retrieves all {@link PublicationSample} items that belong to the given {@link Publisher}
     * across a collection of {@link Listing} objects.
     *
     * <p>This method iterates through each listing and inspects all publication samples
     * associated with it. A publication sample is included in the result if the publisher
     * of its edition matches the publisher provided as argument.</p>
     *
     * <p>The returned list is immutable, ensuring that callers cannot modify the internal
     * collection of results.</p>
     *
     * @param listings  the list of listings to search through; must not be {@code null}
     * @param publisher the publisher whose items should be selected; must not be {@code null}
     * @return an immutable list containing all publication samples whose edition publisher
     *         matches the specified publisher
     * @throws NullPointerException if {@code listings} or {@code publisher} is {@code null}
     */
    public List<PublicationSample> findItemsByListingAndPublisher(List<Listing> listings, Publisher publisher){

        List<PublicationSample> pubSamplesList = new ArrayList<>();

        if (listings == null) {
            throw new IllegalArgumentException("List of listings cannot be null");
        }
        if (publisher == null) {
            throw new IllegalArgumentException("Publisher cannot be null");
        }
        for (Listing listing : listings) {
            if (listing == null)
                throw new IllegalArgumentException("Listings cannot contain null elements");
        }

        for (Listing listing : listings) {
            for  (PublicationSample pubSample : listing.getItems()) {

                if (pubSample == null) {
                    throw new IllegalArgumentException("Listings must not contain null PublicationSample items");
                }

                if(pubSample.getEdition().getPublisher().equals(publisher)){
                    pubSamplesList.add(pubSample);
                }
            }
        }
        return List.copyOf(pubSamplesList);
    }
}
