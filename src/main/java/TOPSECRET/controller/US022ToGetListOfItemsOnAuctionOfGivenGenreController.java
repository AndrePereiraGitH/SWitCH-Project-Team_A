package TOPSECRET.controller;

import TOPSECRET.domain.Auction;
import TOPSECRET.domain.Genre;

import TOPSECRET.dto.AuctionDTO_US022;
import TOPSECRET.dto.GenreDTO_US022;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.GenreRepository;

import java.util.List;
/**
 * Controller responsible for coordinating the use case:
 * "Get the list of items on auction of a given genre" (US022).
 *
 * Responsibilities:
 * - Receives the genre selected by the user.
 * - Requests matching auctions from the repository.
 * - Converts domain objects into DTOs.
 * - Returns a list of AuctionDTO_US022 to the UI.
 */
public class GetListOfItemsOnAuctionOfGivenGenreController {

    // Repository responsible for accessing Auction domain objects
    private final AuctionRepository auctionRepository;
    private final GenreRepository genreRepository;

    /**
     * Constructor injection of dependencies.
     *
     * @param auctionRepository repository that stores auctions
     */
    public GetListOfItemsOnAuctionOfGivenGenreController(
            AuctionRepository auctionRepository, GenreRepository genreRepository) {

        if (auctionRepository == null) {
            throw new IllegalArgumentException("AuctionRepository cannot be null");
        }

        this.auctionRepository = auctionRepository;
        this.genreRepository = genreRepository;
    }

    public List<GenreDTO_US022> getAllGenres() {

        List<Genre> genres = genreRepository.getAllGenres();

        return GenreDTO_US022.fromDomainList(genres);
    }
    /**
     * Retrieves the list of items currently on auction
     * filtered by a given genre.
     *
     * @param genre the genre used as filter
     * @return list of AuctionDTO_US022 matching the genre
     */
    public List<AuctionDTO_US022> getListOfItemsOnAuctionGivenGenre(Genre genre) {

        if (genre == null) {
            throw new IllegalArgumentException("genre cannot be null");
        }

        List<Auction> auctions =
                auctionRepository.getListOfItemsOnAuctionGivenGenre(genre);

        return AuctionDTO_US022.fromDomainList(auctions);
    }
}