package TOPSECRET.controller;

import TOPSECRET.domain.Auction.Auction;
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
public class ToGetListOfItemsOnAuctionOfGivenGenreController {

    private final IGenreRepository genreRepository;
    private final IAuctionRepository auctionRepository;
    private final IListingRepository listingRepository;
    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final IPublicationSampleRepository publicationSampleRepository;

    /**
     * Constructor injection of dependencies.
     *
     * @param auctionRepository repository that stores auctions
     */
    public ToGetListOfItemsOnAuctionOfGivenGenreController(
            IAuctionRepository auctionRepository, IGenreRepository genreRepository,
            IListingRepository listingRepository, IBibliographicalDataRepository bibliographicalDataRepository,
            IPublicationSampleRepository publicationSampleRepository) {

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

        this.auctionRepository = auctionRepository;
        this.genreRepository = genreRepository;
        this.listingRepository = listingRepository;
        this.bibliographicalDataRepository = bibliographicalDataRepository;
        this.publicationSampleRepository = publicationSampleRepository;

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

        Genre genre = genreRepository.findGenreById(selectedGenreDTO.genreId())
                .orElseThrow(() ->
                    new IllegalArgumentException("Genre is not valid or not found"));;


        List<BibliographicalDataID> bdId = bibliographicalDataRepository.findAllByGenreId(genre.id());

        List<PublicationSampleId> itemsId = publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdId);

        List<ListingId> listingId = listingRepository.findIdsByPublicationSampleIdIn(itemsId);

        List<Auction> auctions = auctionRepository.getListOfItemsOnAuctionGivenGenre(listingId);

        return US022AuctionMapper.toDTOList(auctions);
    }
}