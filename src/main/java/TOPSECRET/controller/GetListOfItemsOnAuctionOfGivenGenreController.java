package TOPSECRET.controller;

import TOPSECRET.domain.Auction;
import TOPSECRET.domain.Genre;

import TOPSECRET.dto.AuctionDTO_US022;
import TOPSECRET.dto.AuctionMapper_US022;
import TOPSECRET.dto.GenreDTO_US022;
import TOPSECRET.dto.GenreMapper_US022;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.GenreRepository;

import java.util.List;

public class GetListOfItemsOnAuctionOfGivenGenreController {

    private final AuctionRepository auctionRepository;
    private final GenreRepository genreRepository;
    private final AuctionMapper_US022 auctionMapper;
    private final GenreMapper_US022 genreMapper;

    public GetListOfItemsOnAuctionOfGivenGenreController(AuctionRepository auctionRepository, GenreRepository genreRepository, AuctionMapper_US022 auctionMapper, GenreMapper_US022 genreMapper) {
        this.auctionRepository = auctionRepository;
        this.genreRepository = genreRepository;
        this.auctionMapper = auctionMapper;
        this.genreMapper = genreMapper;
    }

    public List<GenreDTO_US022> getAllGenres() {
        List<Genre> genres = genreRepository.getAllGenres();
        return genreMapper.toDTO(genres);
    }

    public List<AuctionDTO_US022> getListOfItemsOnAuctionGivenGenre(Genre genre) {

        if (genre == null) {
            throw new IllegalArgumentException("genre cannot be null");
        }

        List<Auction> auctions = auctionRepository.getListOfItemsOnAuctionGivenGenre(genre);

        return auctionMapper.toDTO(auctions);
    }
}