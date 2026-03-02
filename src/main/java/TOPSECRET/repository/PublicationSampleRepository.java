package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.dto.PublicationSampleDTO_US016;
import TOPSECRET.dto.PublicationSampleDTO_US018;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PublicationSampleRepository {

    private List<PublicationSample> allPublicationsInSystem = new ArrayList<>();

    public PublicationSampleRepository(List<PublicationSample> publicationList) {
        allPublicationsInSystem = new ArrayList<>(publicationList);
    }

    public PublicationSampleRepository(){
        this.allPublicationsInSystem = new ArrayList<>(); //no-argument constructor so that it can compile (this way it's not restrictive)
    }

    public List<PublicationSample> getAllPublications() { //getter to obtain list of all publications in the system
        return List.copyOf(allPublicationsInSystem);
    }


//    public boolean createAndAdd(BibliographicalData bibliographicalData, PublicationType publicationType, User username,
//                                PublicationStatus publicationStatus, String edition, Code code){
    public boolean createAndAdd(BibliographicalData bibliographicalData, User username,
                            PublicationStatus publicationStatus, Edition edition, Condition condition){

        PublicationSample publication = new PublicationSample(bibliographicalData, username, publicationStatus,edition, condition);

        if(isExistent(publication)){
            return false;
        }
        allPublicationsInSystem.add(publication);
        return true;
    }


    public boolean isExistent(PublicationSample pub){
        for(PublicationSample publication: allPublicationsInSystem){
            if(pub.equals(publication)){
                return true;
            }
        }
        return false;
    }

    public List<PublicationSample> getListOfDifferentPublications(List<Publication> listOfPublicationsInLibrary){
        List<PublicationSample> listOfDifferentPublications = new ArrayList<>();

        for (PublicationSample publication : allPublicationsInSystem) {
            //If the user library does not contain the publication
            if (!listOfPublicationsInLibrary.contains(publication)){
                listOfDifferentPublications.add(publication);
            }
        }
        return List.copyOf(listOfDifferentPublications);

    }
    //Method for testing purposes only (AddPublicationToLibraryControllerTest)
    public void addPublication(PublicationSample p) {
        if (!allPublicationsInSystem.contains(p)) {
            allPublicationsInSystem.add(p);
        }
    }

    /**
     * US016
     * @return
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
     *
     * @param ps
     * @param dto
     * @return
     */
    private static boolean matches(PublicationSample ps, PublicationSampleDTO_US016 dto) {
        BibliographicalData bd = ps.getBibliographicalData();

        List<String> authorNames = bd.getAuthors().stream()
                .map(a -> a.getProfile().getName())
                .toList();

        List<String> genreTypes = bd.getGenres().stream()
                .map(Genre::getTitle)
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

<<<<<<< Updated upstream
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
=======
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
>>>>>>> Stashed changes
}
