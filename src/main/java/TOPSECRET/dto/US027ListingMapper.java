package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mapper responsible for converting {@link Listing} domain objects into
 * {@link US027ListingDTO} instances for user story US027.
 * <p>
 * Since the service layer is not yet part of the current sprint, this mapper
 * temporarily orchestrates the retrieval of related aggregates required to
 * build a complete DTO. It interacts with repositories for
 * {@link PublicationSample}, {@link BibliographicalData}, and {@link Author}
 * to gather all necessary information associated with a Listing.
 * </p>
 *
 * <p>
 * All dependencies are validated upon construction, and appropriate exceptions
 * are thrown when referenced entities cannot be found.
 * </p>
 *
 */
public class US027ListingMapper {

    private final IPublicationSampleRepository pubSampleRepo;
    private final IBibliographicalDataRepository bibDataRepo;
    private final IAuthorRepository authorRepo;

    /**
     * Creates a new mapper with the required repositories.
     *
     * @param pubSampleRepo the repository used to retrieve PublicationSample entities
     * @param bibDataRepo   the repository used to retrieve BibliographicalData entities
     * @param authorRepo    the repository used to retrieve Author entities
     *
     * @throws IllegalArgumentException if any repository is null
     */
    public US027ListingMapper(IPublicationSampleRepository pubSampleRepo, IBibliographicalDataRepository bibDataRepo,
                              IAuthorRepository authorRepo) {

        if(pubSampleRepo==null) throw new IllegalArgumentException("PublicationSampleRepository cannot be null");
        if(bibDataRepo==null) throw new IllegalArgumentException("BibliographicalDataRepository cannot be null");
        if(authorRepo==null) throw new IllegalArgumentException("AuthorRepository cannot be null");

        this.pubSampleRepo = pubSampleRepo;
        this.bibDataRepo = bibDataRepo;
        this.authorRepo = authorRepo;
    }



    /**
     * Converts a {@link Listing} into a {@link US027ListingDTO}, retrieving all
     * associated publication samples, bibliographical data, titles, and authors.
     *
     * @param listing the Listing to convert; must not be null
     * @return a fully populated {@link US027ListingDTO}
     *
     * @throws NullPointerException       if the listing is null
     * @throws InstanceNotFoundException if any referenced entity (PublicationSample,
     *                                   BibliographicalData, or Author) cannot be found
     */
    public US027ListingDTO toDTO(Listing listing) throws InstanceNotFoundException {

        if  (listing == null) {
            throw new NullPointerException("Listing cannot be null");
        }

        String listingId = listing.id().toString();
        String listingTitle = listing.getListingTitle();
        String price = listing.getPrice().toString();
        List<String> pubTitle = getTitle(listing);
        List<String> authorsName = getAuthors(listing);

        US027ListingDTO listingDTO = new US027ListingDTO(listingId, listingTitle, price, pubTitle, authorsName);

        return listingDTO;
    }

    /**
     * Converts a list of {@link Listing} objects into an immutable list of
     * {@link US027ListingDTO} instances.
     *
     * @param listOfListings the list of Listing objects to convert; must not be null
     * @return an immutable list of DTOs
     *
     * @throws NullPointerException       if the list itself is null
     * @throws IllegalArgumentException   if the list contains null elements
     * @throws InstanceNotFoundException if any referenced entity required for DTO
     *                                   construction cannot be found
     */
    public List<US027ListingDTO> toDTOs(List<Listing> listOfListings) throws InstanceNotFoundException {

        if  (listOfListings == null) {
            throw new NullPointerException("List of listings can not be null");
        }

        List<US027ListingDTO> listOfListingDTO = new ArrayList<>();

        for (Listing listing : listOfListings) {
            listOfListingDTO.add(toDTO(listing));
        }

        return listOfListingDTO;
    }

    // =================================================================================================================
    // ================ Private Helpers ================================================================================
    // =================================================================================================================

    /**
     * Retrieves all {@link PublicationSample} entities referenced by a Listing.
     *
     * @param listing the listing whose publication samples should be loaded
     * @return a list of PublicationSample entities
     *
     * @throws InstanceNotFoundException if any PublicationSample ID does not correspond
     *                                   to a stored entity
     */
    private List<PublicationSample> getPublicationSample(Listing listing) throws InstanceNotFoundException {

        List<PublicationSampleId> pubSampleIds = listing.getItemsId();

        List<PublicationSample> pubSamples = new ArrayList<>();

        for (PublicationSampleId pubSampleId : pubSampleIds) {
            Optional<PublicationSample> pubSample = pubSampleRepo.findById(pubSampleId);
            if (pubSample.isPresent()) {
                pubSamples.add(pubSample.get());
            }
            else throw new InstanceNotFoundException("PublicationSample with id " + pubSampleId + " was not found");
        }

        return pubSamples;
    }

    /**
     * Retrieves the {@link BibliographicalData} associated with a given
     * {@link PublicationSample}.
     *
     * @param pubSample the publication sample whose bibliographical data is required
     * @return the corresponding BibliographicalData entity
     *
     * @throws InstanceNotFoundException if the bibliographical data cannot be found
     */
    private BibliographicalData getBibliographicalData(PublicationSample pubSample) throws InstanceNotFoundException {

        BibliographicalDataID bibDataId = pubSample.getBibliographicalDataID();

        BibliographicalData bibData = bibDataRepo.findById(bibDataId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("BibliographicalData with id " + bibDataId + " was not found" ));

        return bibData;
    }

    /**
     * Extracts all titles associated with the publication samples of a Listing.
     *
     * @param listing the listing whose titles should be retrieved
     * @return a list of titles extracted from bibliographical data
     *
     * @throws InstanceNotFoundException if any referenced entity cannot be found
     */
    private List<String> getTitle(Listing listing) throws InstanceNotFoundException {

        List<PublicationSample> pubSamples = getPublicationSample(listing);

        List<BibliographicalData> bdDatas = new ArrayList<>();
        for (PublicationSample pubSample : pubSamples) {
            BibliographicalData bdData = getBibliographicalData(pubSample);
            bdDatas.add(bdData);
        }

        List<String> titles = new ArrayList<>();
        for (BibliographicalData bdData : bdDatas) {
            titles.add(bdData.getTitle());
        }

        return titles;
    }

    /**
     * Retrieves the names of all authors associated with the bibliographical data
     * of a Listing's publication samples.
     *
     * @param listing the listing whose authors should be retrieved
     * @return a list of author names
     *
     * @throws InstanceNotFoundException if any Author ID does not correspond to a stored entity
     */
    private List<String> getAuthors(Listing listing) throws InstanceNotFoundException {

        // finds all publicationSamples for a given listing
        List<PublicationSample> pubSamples = getPublicationSample(listing);

        // finds the bibliographicalData for each publicationSample
        List<BibliographicalData> bdDatas = new ArrayList<>();
        for (PublicationSample pubSample : pubSamples) {
            BibliographicalData bdData = getBibliographicalData(pubSample);
            bdDatas.add(bdData);
        }

        // finds authors to each bibliographicalData
        List<String> authorsName = new ArrayList<>();
        for (BibliographicalData bdData : bdDatas) {
            List<AuthorId> authorIds = bdData.getAuthors();
            for (AuthorId authorId : authorIds) {
                Author author = authorRepo.findById(authorId).orElseThrow(() ->
                        new InstanceNotFoundException("Author with id " + authorId + " not found"));
                String authorName = author.getName();
                authorsName.add(authorName);
            }
        }
        return authorsName;
    }
}
