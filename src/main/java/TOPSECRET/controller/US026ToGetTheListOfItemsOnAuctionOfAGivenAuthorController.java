package TOPSECRET.controller;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US026AuctionDTO;
import TOPSECRET.dto.US026AuctionMapper;
import TOPSECRET.dto.US026AuthorDTO;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.dto.US026AuthorMapper;

import javax.management.InstanceNotFoundException;
import java.util.List;

/**
 * Controller responsible for retrieving all authors and the list of auction items
 * associated with a given author.
 * <p>
 * This controller coordinates the retrieval of bibliographical data, publication samples,
 * listings, and auctions in order to return the corresponding DTO representations.
 */
public class US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController {

    private final IAuthorRepository authorRepository;
    private final IBibliographicalDataRepository biblioRepository;
    private final IPublicationSampleRepository pubRepo;
    private final IListingRepository listingRepo;
    private final IAuctionRepository auctionRepository;
    private final US026AuctionMapper mapper;

    /**
     * Creates a controller with all required dependencies.
     *
     * @param authorRepository the repository used to retrieve authors
     * @param biblioRepository the repository used to retrieve bibliographical data
     * @param pubRepo the repository used to retrieve publication samples
     * @param listingRepo the repository used to retrieve listings
     * @param auctionRepository the repository used to retrieve auctions
     * @param mapper the mapper used to convert auction domain objects into DTOs
     * @throws IllegalArgumentException if any dependency is null
     */
    public US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController(IAuthorRepository authorRepository, IBibliographicalDataRepository biblioRepository, IPublicationSampleRepository pubRepo, IListingRepository listingRepo, IAuctionRepository auctionRepository, US026AuctionMapper mapper) {

        if (authorRepository == null || biblioRepository == null || pubRepo == null || listingRepo == null || auctionRepository == null || mapper == null) {
            throw new IllegalArgumentException("Controller dependencies cannot be null");
        }

        this.authorRepository = authorRepository;
        this.biblioRepository = biblioRepository;
        this.pubRepo = pubRepo;
        this.listingRepo = listingRepo;
        this.auctionRepository = auctionRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all authors in the system.
     *
     * @return a list of {@link US026AuthorDTO} representing all authors
     */
    public List<US026AuthorDTO> getAllAuthors() {

        List<Author> authorsList = authorRepository.getAllAuthors();
        List<US026AuthorDTO> authorsListDTO = US026AuthorMapper.fromDomainList(authorsList);

        return authorsListDTO;
    }

    /**
     * Retrieves the list of auction items associated with a given author.
     * <p>
     * The method obtains the bibliographical data identifiers of the author,
     * retrieves the related publication sample identifiers, then the associated
     * listing identifiers, and finally the auctions corresponding to those listings.
     *
     * @param authorId the identifier of the author whose auction items are to be retrieved
     * @return a list of {@link US026AuctionDTO} representing the auction items of the given author
     * @throws IllegalArgumentException if {@code authorId} is null
     * @throws InstanceNotFoundException if a required instance cannot be found during the mapping process
     */
    public List<US026AuctionDTO> getAuctionsByAuthorId(AuthorId authorId) throws InstanceNotFoundException {

        if (authorId == null) {
            throw new IllegalArgumentException("authorId cannot be null.");
        }

       List<BibliographicalDataID> bdsOfAuthorID = biblioRepository.findBibliographicalDataIdByAuthorId(authorId);

       List<PublicationSampleId> samplesOfAuthorID = pubRepo.findPublicationSamplesByBiblioDataIds(bdsOfAuthorID);

       List<ListingId> authorListings = listingRepo.findListingIdsByPublicationSampleIds(samplesOfAuthorID);

       List<Auction> auctionsOfAuthor = auctionRepository.findAuctionsByListingId(authorListings);

       return mapper.fromDomainList(auctionsOfAuthor);
    }
}
