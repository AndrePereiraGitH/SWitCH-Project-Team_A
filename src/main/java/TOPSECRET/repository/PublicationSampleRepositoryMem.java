package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
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
     * {@inheritDoc}
     */
    public List<PublicationSample> getAllPublications() { //getter to obtain list of all publications in the system
        return List.copyOf(allPublicationsInSystem);
    }


    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void add(PublicationSample publicationSample){
        allPublicationsInSystem.add(publicationSample);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
                .map(a -> a.getName())
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
     * {@inheritDoc}
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

    // --------------------------------------- US018 starts ----------------------------------------

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
