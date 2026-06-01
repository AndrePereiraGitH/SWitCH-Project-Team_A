package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US025AuthorDTO;
import TOPSECRET.dto.US025DirectSaleDTO;
import TOPSECRET.dto.US025AuthorMapper;
import TOPSECRET.dto.US025DirectSaleMapper;
import TOPSECRET.services.ListingService;

import java.util.List;
import java.util.Comparator;

/**
 * Controller responsible for providing:
 * <ul>
 *   <li>A list of all authors.</li>
 *   <li>A list of items on direct sale of a specific author.</li>
 * </ul>
 *
 * <p>Orchestrates repository calls and delegates DTO conversion to mappers.
 */
public class ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController {

    private final IAuthorRepository authorRepository;
    private final ListingService listingService;
    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final IPublicationSampleRepository publicationSampleRepository;
    private final US025AuthorMapper authorMapper;
    private final US025DirectSaleMapper directSaleMapper;

    /**
     * Constructs a new controller with the required repositories and mappers.
     *
     * @param authorRepository              repository for {@link TOPSECRET.domain.Author.Author} aggregates
     * @param listingService             repository for {@link Listing} aggregates
     * @param bibliographicalDataRepository repository for {@link BibliographicalData} aggregates
     * @param publicationSampleRepository   repository for {@link PublicationSample} aggregates
     * @param authorMapper                  mapper for converting authors to DTOs
     * @param directSaleMapper              mapper for converting listings to DTOs
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
            IAuthorRepository authorRepository,
            ListingService listingService,
            IBibliographicalDataRepository bibliographicalDataRepository,
            IPublicationSampleRepository publicationSampleRepository,
            US025AuthorMapper authorMapper,
            US025DirectSaleMapper directSaleMapper) {

        if (authorRepository == null)
            throw new IllegalArgumentException("AuthorRepository cannot be null");
        if (listingService == null)
            throw new IllegalArgumentException("ListingRepository cannot be null");
        if (bibliographicalDataRepository == null)
            throw new IllegalArgumentException("BibliographicalDataRepository cannot be null");
        if (publicationSampleRepository == null)
            throw new IllegalArgumentException("PublicationSampleRepository cannot be null");
        if (authorMapper == null)
            throw new IllegalArgumentException("AuthorMapper cannot be null");
        if (directSaleMapper == null)
            throw new IllegalArgumentException("DirectSaleMapper cannot be null");

        this.authorRepository = authorRepository;
        this.listingService = listingService;
        this.bibliographicalDataRepository = bibliographicalDataRepository;
        this.publicationSampleRepository = publicationSampleRepository;
        this.authorMapper = authorMapper;
        this.directSaleMapper = directSaleMapper;
    }

    /**
     * Returns all authors in the system as DTOs.
     *
     * @return list of {@link US025AuthorDTO} representing all authors
     */
    public List<US025AuthorDTO> getAllAuthors() {
        return authorMapper.toDTOList(authorRepository.findAll());
    }

    /**
     * Returns the list of items on direct sale for a specific author,
     * sorted ascending by publication title.
     *
     * @param authorId the identifier of the author whose items are being queried
     * @return list of {@link US025DirectSaleDTO} sorted ascending by publication title
     * @throws IllegalArgumentException if {@code authorId} is {@code null}
     */
    public List<US025DirectSaleDTO> getDirectSalesByAuthorId(AuthorId authorId) {
        if (authorId == null)
            throw new IllegalArgumentException("AuthorId cannot be null");

        List<BibliographicalDataId> bdIds =
                bibliographicalDataRepository.findIdByAuthorId(authorId);

        List<PublicationSampleId> pubIds =
                publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdIds);

        List<Listing> listings =
                listingService.findAllDirectSaleAndPublicationSampleIdIn(pubIds);

        List<US025DirectSaleDTO> dtos = directSaleMapper.toDTOList(listings);
        dtos.sort(Comparator.comparing(US025DirectSaleDTO::publicationTitle));
        return dtos;
    }
}