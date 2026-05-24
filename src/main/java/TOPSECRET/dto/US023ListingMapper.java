package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mapper for US023: converts {@link Listing} domain objects
 * into {@link US023ListingDTO} data transfer objects.
 *
 * <p>Because {@link Listing} holds only IDs rather than resolved references,
 * this mapper queries three repositories to assemble each DTO:</p>
 * <ul>
 *   <li>{@link IBibliographicalDataRepository} – resolves the title and author IDs</li>
 *   <li>{@link IAuthorRepository} – resolves each author ID to an author name</li>
 *   <li>{@link IEditionRepository} – resolves the publication type and code</li>
 * </ul>
 */
public class US023ListingMapper {

    private final IListingRepository _listingRepo;
    private final IPublicationSampleRepository _pubSampleRepo;
    private final IBibliographicalDataRepository _bdRepo;
    private final IAuthorRepository _authorRepo;

    
    // =================================================================================================================
    // ============ CONSTRUCTOR ========================================================================================
    // =================================================================================================================
    
    /**
     * Constructs a new mapper with the required repositories.
     *
     * @param listingRepo       the repository used to resolve listing; must not be {@code null}
     * @param pubSampleRepo     the repository used to resolve publication sample; must not be {@code null}
     * @param bdRepo            the repository used to resolve bibliographical data; must not be {@code null}
     * @param authorRepo        the repository used to resolve authors; must not be {@code null}
     * @throws IllegalArgumentException if any of the provided repositories is {@code null}
     */
    public US023ListingMapper(IListingRepository listingRepo,IPublicationSampleRepository pubSampleRepo ,
                              IBibliographicalDataRepository bdRepo, IAuthorRepository authorRepo) {
        if(listingRepo==null) {
            throw new IllegalArgumentException("ListingRepository is null");
        }
        if(pubSampleRepo==null) {
            throw new IllegalArgumentException("PublicationSampleRepository cannot be null");
        }
        if (bdRepo == null) {
            throw new IllegalArgumentException("BibliographicalDataRepository can not be null");
        }
        if (authorRepo == null) {
            throw new IllegalArgumentException("AuthorRepository can not be null");
        }

        _listingRepo = listingRepo;
        _pubSampleRepo = pubSampleRepo;
        _bdRepo = bdRepo;
        _authorRepo = authorRepo;
    }

    
    // =================================================================================================================
    // ============ CONVERSION METHODS =================================================================================
    // =================================================================================================================

    /**
     * Converts a list of {@link Listing} objects into a list of DTOs.
     *
     * @param listings the list of direct sale listings to convert; must not be {@code null}
     * @return a list of {@link US023ListingDTO}; never {@code null}, may be empty
     * @throws InstanceNotFoundException if any referenced bibliographical data, edition, or author
     *                                   cannot be found in their respective repositories
     */
    public List<US023ListingDTO> toDTOs(List<Listing> listings) throws InstanceNotFoundException {
        List<US023ListingDTO> listOfListingDTO = new ArrayList<>();

        if  (listings == null) {
            throw new IllegalArgumentException("List of listings cannot be null");
        }

        for (Listing listing : listings) {
            US023ListingDTO listingDTO = toDTO(listing);
            listOfListingDTO.add(listingDTO);
        }

        return List.copyOf(listOfListingDTO);
    }

    /**
     * Converts a single {@link Listing} into a {@link US023ListingDTO}.
     *
     * @param listing the direct sale listing to convert; must not be {@code null}
     * @return a fully populated {@link US023ListingDTO}
     * @throws InstanceNotFoundException if any referenced bibliographical data, edition, or author
     *                                   cannot be found in their respective repositories
     */
    public US023ListingDTO toDTO(Listing listing) throws InstanceNotFoundException {

        if  (listing == null) {
            throw new IllegalArgumentException("Listing cannot be null");
        }

        String listingTitle = listing.getListingTitle();
        String price = listing.getPrice().toString();
        List<String> pubTitle = getTitle(listing);
        List<String> authorsName = getAuthors(listing);

        US023ListingDTO listingDTO = new US023ListingDTO(listing.id().toString(), listingTitle, price, pubTitle, authorsName);

        return listingDTO;
    }


    // =================================================================================================================
    // ============ PRIVATE HELPERS ====================================================================================
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
            Optional<PublicationSample> pubSample = _pubSampleRepo.findById(pubSampleId);
            if (pubSample.isPresent()) {
                pubSamples.add(pubSample.get());
            }
            else throw new InstanceNotFoundException("PublicationSample with id " + pubSampleId + " was not found");
        }

        return pubSamples;
    }

    /**
     * Resolves the {@link BibliographicalData} associated with the given publication.
     *
     * @param publicationSample the publication whose bibliographical data is to be resolved
     * @return the resolved {@link BibliographicalData}
     * @throws InstanceNotFoundException if no bibliographical data is found for the publication's ID
     */
    private BibliographicalData getBibliographicalData(PublicationSample publicationSample) throws InstanceNotFoundException {
        BibliographicalDataId bibliographicalDataId = publicationSample.getBibliographicalDataID();
        BibliographicalData bibliographicalData = _bdRepo.findById(bibliographicalDataId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("BibliographicalData id was not found:" + bibliographicalDataId));

        return bibliographicalData;
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
                Author author = _authorRepo.findById(authorId).orElseThrow(() ->
                        new InstanceNotFoundException("Author with id " + authorId + " not found"));
                String authorName = author.getName();
                authorsName.add(authorName);
            }
        }
        return authorsName;
    }
}
