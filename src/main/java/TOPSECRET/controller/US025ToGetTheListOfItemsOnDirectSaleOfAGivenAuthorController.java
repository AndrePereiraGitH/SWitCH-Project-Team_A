package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US025AuthorDTO;
import TOPSECRET.dto.US025AuthorMapper;
import TOPSECRET.dto.US025DirectSaleDTO;
import TOPSECRET.dto.US025DirectSaleMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for providing:
 * 1. A list of all authors.
 * 2. A list of items on direct sale of a specific author.
 *
 * The controller orchestrates all repository calls and delegates
 * DTO conversion to the Mapper classes.
 */
public class US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController {

    private final IAuthorRepository authorRepository;
    private final IListingRepository listingRepository;
    private final IPublicationSampleRepository publicationSampleRepository;
    private final IBibliographicalDataRepository bibliographicalDataRepository;

    /**
     * Constructs a new {@code US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController}
     * with the required dependencies.
     *
     * @param authorRepository              the repository for authors; must not be {@code null}
     * @param listingRepository             the repository for listings; must not be {@code null}
     * @param publicationSampleRepository   the repository for publication samples; must not be {@code null}
     * @param bibliographicalDataRepository the repository for bibliographical data; must not be {@code null}
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
            IAuthorRepository authorRepository,
            IListingRepository listingRepository,
            IPublicationSampleRepository publicationSampleRepository,
            IBibliographicalDataRepository bibliographicalDataRepository) {

        if (authorRepository == null)
            throw new IllegalArgumentException("AuthorRepository cannot be null");
        if (listingRepository == null)
            throw new IllegalArgumentException("ListingRepository cannot be null");
        if (publicationSampleRepository == null)
            throw new IllegalArgumentException("PublicationSampleRepository cannot be null");
        if (bibliographicalDataRepository == null)
            throw new IllegalArgumentException("BibliographicalDataRepository cannot be null");

        this.authorRepository = authorRepository;
        this.listingRepository = listingRepository;
        this.publicationSampleRepository = publicationSampleRepository;
        this.bibliographicalDataRepository = bibliographicalDataRepository;
    }

    /**
     * Returns all authors in the system, converted into DTOs.
     *
     * @return list of AuthorDTO_US025 representing all authors
     */
    public List<US025AuthorDTO> getAllAuthors() {
        return US025AuthorMapper.toDTOList(authorRepository.getAllAuthors());
    }

    /**
     * Returns the list of items on direct sale for a specific author, converted into DTOs.
     *
     * @param authorId the string identifier of the author being queried; must not be {@code null}
     * @return list of DirectSaleDTO_US025 representing the author's direct sale items
     * @throws IllegalArgumentException if authorId is null or blank, or if the author is not found
     */
    public List<US025DirectSaleDTO> getListOfItemsOnDirectSaleOfAGivenAuthor(String authorId)
            throws IllegalArgumentException {

        if (authorId == null)
            throw new IllegalArgumentException("AuthorID cannot be null");

        AuthorId id = new AuthorId(authorId);

        Author author = authorRepository.findAuthorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        List<BibliographicalData> bds = bibliographicalDataRepository.findByAuthorId(id);
        List<PublicationSample> samples = publicationSampleRepository.findByBibliographicalData(bds);
        List<PublicationSampleId> sampleIds = extractIds(samples);
        List<Listing> listings = listingRepository.findDirectSaleListingsContaining(sampleIds);
        return US025DirectSaleMapper.toDTO(author, listings, samples, bds);
    }

    /**
     * Helper to extract the identifiers from a list of {@link PublicationSample} instances.
     *
     * @param samples the list of publication samples; must not be {@code null}
     * @return a list of {@link PublicationSampleId} in the same order as the input; never {@code null}
     */
    private List<PublicationSampleId> extractIds(List<PublicationSample> samples) {
        List<PublicationSampleId> ids = new ArrayList<>();
        for (PublicationSample ps : samples) {
            ids.add(ps.id());
        }
        return ids;
    }
}