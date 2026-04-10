package TOPSECRET.controller;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.Genre;

import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US022AuctionMapper;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.dto.US022AuctionDTO;
import TOPSECRET.dto.US022GenreDTO;
import TOPSECRET.dto.US022GenreMapper;
import TOPSECRET.domain.Auction.IAuctionRepository;

import java.util.List;
/**
 * Controller responsible for coordinating the use case:
 * "Get the list of items on auction of a given genre" (US022).
 * <p>
 * Responsibilities:
 * - Receives the genre selected by the user.
 * - Requests matching auctions from the repository.
 * - Converts domain objects into DTOs.
 * - Returns a list of US022AuctionMapper to the UI.
 */
public class US022ToGetListOfItemsOnAuctionOfGivenGenreController {

    private final IGenreRepository genreRepository;
    private final IAuctionRepository auctionRepository;
    private final IListingRepository listingRepository;
    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final IPublicationSampleRepository publicationSampleRepository;
    private final US022AuctionMapper auctionMapper;

    /**
     * Constructor injection of dependencies.
     *
     * @param auctionRepository repository that stores auctions
     */
    public US022ToGetListOfItemsOnAuctionOfGivenGenreController(
            IAuctionRepository auctionRepository, IGenreRepository genreRepository,
            IListingRepository listingRepository, IBibliographicalDataRepository bibliographicalDataRepository,
            IPublicationSampleRepository publicationSampleRepository,
            US022AuctionMapper auctionMapper) {

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
        if (auctionMapper == null) {
            throw new IllegalArgumentException("auctionMapper cannot be null");
        }

        this.auctionRepository = auctionRepository;
        this.genreRepository = genreRepository;
        this.listingRepository = listingRepository;
        this.bibliographicalDataRepository = bibliographicalDataRepository;
        this.publicationSampleRepository = publicationSampleRepository;
        this.auctionMapper = auctionMapper;
    }

    public List<US022GenreDTO> getAllGenres() {

        List<Genre> genres = genreRepository.getAllGenres();

        return US022GenreMapper.toDTOList(genres);
    }
    /**
     * Retrieves the list of items currently on auction
     * filtered by a given genre.
     *
     * @param selectedGenreDTO the genre used as filter
     * @return list of US022AuctionMapper matching the genre
     */
    public List<US022AuctionDTO> getListOfItemsOnAuctionGivenGenre(US022GenreDTO selectedGenreDTO) {

        if (selectedGenreDTO == null) {
            throw new IllegalArgumentException("genre cannot be null");
        }

        if (selectedGenreDTO.genreId() == null) {
            throw new IllegalArgumentException("genreId cannot be null");
        }
        Genre genre = genreRepository.findById(selectedGenreDTO.genreId());

        if (genre == null) {
            throw new IllegalArgumentException("genre not found");
        }

        List<BibliographicalDataID> bdId = bibliographicalDataRepository.findAllBibliographicalDataByGenreId(genre.id());

        if (bdId == null) {
            throw new IllegalArgumentException("bibliographical data cannot be null");
        }

        List<PublicationSampleId> itemsId = publicationSampleRepository.findAllItemsIdByBibliographicalDataId(bdId);

        if (itemsId == null) {
            throw new IllegalArgumentException("items cannot be null");
        }

        List<ListingId> listingId = listingRepository.findAllListingId(itemsId);

        if (listingId == null) {
            throw new IllegalArgumentException("listingIds cannot be null");
        }

        List<Auction> auctions = auctionRepository.getListOfItemsOnAuctionGivenGenre(listingId);

        if (auctions == null) {
            throw new IllegalArgumentException("auctions cannot be null");
        }

        return auctionMapper.toDTOList(auctions);
    }
}