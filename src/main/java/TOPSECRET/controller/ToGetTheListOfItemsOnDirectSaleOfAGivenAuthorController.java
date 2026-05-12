package TOPSECRET.controller;

import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US025AuthorDTO;
import TOPSECRET.dto.US025DirectSaleDTO;
import TOPSECRET.dto.US025AuthorMapper;
import TOPSECRET.dto.US025DirectSaleMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    private final IListingRepository listingRepository;
    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final IPublicationSampleRepository publicationSampleRepository;
    private final US025AuthorMapper authorMapper;
    private final US025DirectSaleMapper directSaleMapper;

    /**
     * Constructs a new controller with the required repositories and mappers.
     *
     * @param authorRepository              repository for {@link TOPSECRET.domain.Author.Author} aggregates
     * @param listingRepository             repository for {@link Listing} aggregates
     * @param bibliographicalDataRepository repository for {@link BibliographicalData} aggregates
     * @param publicationSampleRepository   repository for {@link PublicationSample} aggregates
     * @param authorMapper                  mapper for converting authors to DTOs
     * @param directSaleMapper              mapper for converting listings to DTOs
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
            IAuthorRepository authorRepository,
            IListingRepository listingRepository,
            IBibliographicalDataRepository bibliographicalDataRepository,
            IPublicationSampleRepository publicationSampleRepository,
            US025AuthorMapper authorMapper,
            US025DirectSaleMapper directSaleMapper) {

        if (authorRepository == null)
            throw new IllegalArgumentException("AuthorRepository cannot be null");
        if (listingRepository == null)
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
        this.listingRepository = listingRepository;
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

        List<BibliographicalDataID> bdIds =
                bibliographicalDataRepository.findIdByAuthorId(authorId);

        List<PublicationSampleId> pubIds =
                publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdIds);

        List<Listing> listings =
                listingRepository.findAllByDirectSaleAndPublicationSampleIdIn(pubIds);

        List<US025DirectSaleDTO> dtos = new ArrayList<>();
        for (Listing listing : listings) {
            PublicationSampleId firstPubId = listing.getItemsId().get(0);
            PublicationSample pub = publicationSampleRepository.findById(firstPubId).orElseThrow();
            BibliographicalData bd = bibliographicalDataRepository.findById(pub.getBibliographicalDataID()).orElseThrow();
            List<String> authorNames = new ArrayList<>();
            for (AuthorId aId : bd.getAuthors()) {
                authorRepository.findById(aId).ifPresent(a -> authorNames.add(a.getName()));
            }
            dtos.add(directSaleMapper.toDTO(listing, bd, authorNames));
        }

        dtos.sort(Comparator.comparing(US025DirectSaleDTO::getPublicationTitle));

        return dtos;
    }
}