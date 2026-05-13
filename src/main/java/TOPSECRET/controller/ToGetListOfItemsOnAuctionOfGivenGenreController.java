package TOPSECRET.controller;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.Genre;

import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US022AuctionDTO;
import TOPSECRET.dto.US022AuctionMapper;
import TOPSECRET.dto.US022GenreDTO;
import TOPSECRET.dto.US022GenreMapper;
import TOPSECRET.domain.Auction.IAuctionRepository;

import java.util.List;

/**
 * Retrieves all auctions associated with the given genre.
 * <p>
 * The process consists of:
 * <ul>
 *     <li>Finding bibliographical data associated with the genre</li>
 *     <li>Finding publication samples associated with those bibliographical records</li>
 *     <li>Finding listings associated with those publication samples</li>
 *     <li>Finding auctions associated with those listings</li>
 * </ul>
 *
 * @return list of auction DTOs associated with the given genre
 * @throws IllegalArgumentException if genreDTO is null
 * @throws IllegalArgumentException if genreDTO.genreId() is null
 */
public class ToGetListOfItemsOnAuctionOfGivenGenreController {

    private final IGenreRepository genreRepository;
    private final IAuctionRepository auctionRepository;
    private final IListingRepository listingRepository;
    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final IPublicationSampleRepository publicationSampleRepository;
    private final US022GenreMapper genreMapper;
    private final US022AuctionMapper auctionMapper;

    /**
     * Constructs the controller with all required dependencies.
     *
     * @param auctionRepository repository of auctions
     * @param genreRepository repository of genres
     * @param listingRepository repository of listings
     * @param bibliographicalDataRepository repository of bibliographical data
     * @param publicationSampleRepository repository of publication samples
     * @param genreMapper mapper for genre DTO conversion
     * @param auctionMapper mapper for auction DTO conversion
     */
    public ToGetListOfItemsOnAuctionOfGivenGenreController(
            IAuctionRepository auctionRepository, IGenreRepository genreRepository,
            IListingRepository listingRepository, IBibliographicalDataRepository bibliographicalDataRepository,
            IPublicationSampleRepository publicationSampleRepository, US022GenreMapper genreMapper, US022AuctionMapper auctionMapper) {

        if (auctionRepository == null) {
            throw new IllegalArgumentException("AuctionRepository cannot be null");
        }
        if (genreRepository == null) {
            throw new IllegalArgumentException("GenreRepository cannot be null");
        }
        if (listingRepository == null) {
            throw new IllegalArgumentException("ListingRepository cannot be null");
        }
        if (bibliographicalDataRepository == null) {
            throw new IllegalArgumentException("BibliographicalDataRepository cannot be null");
        }
        if (publicationSampleRepository == null) {
            throw new IllegalArgumentException("PublicationSampleRepository cannot be null");
        }
        if (genreMapper == null) {
            throw new IllegalArgumentException("GenreMapper cannot be null");
        }
        if (auctionMapper == null) {
            throw new IllegalArgumentException("AuctionMapper cannot be null");
        }

        this.auctionRepository = auctionRepository;
        this.genreRepository = genreRepository;
        this.listingRepository = listingRepository;
        this.bibliographicalDataRepository = bibliographicalDataRepository;
        this.publicationSampleRepository = publicationSampleRepository;
        this.genreMapper = genreMapper;
        this.auctionMapper = auctionMapper;

    }

    /**
     * Retrieves all available genres.
     *
     * @return list of genre DTOs
     */
    public List<US022GenreDTO> getAllGenres() {

        List<Genre> genres = genreRepository.findAll();

        return genreMapper.toDTOList(genres);
    }

    /**
     * Retrieves the list of items currently on auction
     * filtered by a given genre.
     *
     * @param genreDTO the genre used as filter
     * @return list of US022AuctionDTO matching the genre
     */
    public List<US022AuctionDTO> findListOfItemsOnAuctionByGenreId(US022GenreDTO genreDTO) {

        if (genreDTO == null) {
            throw new IllegalArgumentException("genre cannot be null");
        }

        if (genreDTO.genreId() == null) {
            throw new IllegalArgumentException("genreId cannot be null");
        }

        List<BibliographicalDataID> bdId = bibliographicalDataRepository.findAllByGenreId(genreDTO.genreId());

        List<PublicationSampleId> itemsId = publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdId);

        List<ListingId> listingId = listingRepository.findIdsByPublicationSampleIdIn(itemsId);

        List<Auction> auctions = auctionRepository.findByListingIdIn(listingId);

        return auctionMapper.toDTOList(auctions);
    }

    /**
     * Retrieves the list of items currently on auction
     * filtered by a given genre and sorted ascending
     * by auction deadline.
     *
     * @param genreDTO the genre used as filter
     * @return list of US022AuctionDTO sorted by auction deadline
     */
    public List<US022AuctionDTO> findListOfItemsOnAuctionByGenreIdSortedByDeadlineAsc(US022GenreDTO genreDTO) {

        if (genreDTO == null) {
            throw new IllegalArgumentException("genre cannot be null");

        }
        if (genreDTO.genreId() == null) {
            throw new IllegalArgumentException("genreId cannot be null");
        }

        List<BibliographicalDataID> bdId = bibliographicalDataRepository.findAllByGenreId(genreDTO.genreId());

        List<PublicationSampleId> itemsId = publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdId);

        List<ListingId> listingId = listingRepository.findIdsByPublicationSampleIdIn(itemsId);

        List<Auction> auctions = auctionRepository.findByListingIdInOrderByAuctionDeadlineAsc(listingId);

        return auctionMapper.toDTOList(auctions);

    }

}