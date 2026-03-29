package TOPSECRET.controller;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.repository.GenreRepositoryMem;
import TOPSECRET.repository.IListingRepository;

import java.util.List;

/**
 * Controller for US021 - get the list of items on direct sale of a given genre.
 */
public class US021ToGetListOfItemsOnDirectSaleOfGivenGenreController {
    private IListingRepository listingRepository;
    private GenreRepositoryMem genreRepository;

    /**
     * Constructor.
     *
     * @param genreRepository genre repository
     * @param listingRepository direct sale repository
     */
    public US021ToGetListOfItemsOnDirectSaleOfGivenGenreController (GenreRepositoryMem genreRepository, IListingRepository listingRepository) {
        if (genreRepository == null || listingRepository == null) {
            throw new IllegalArgumentException("GenreRepository and ListingRepository cannot be null");
        }

        this.genreRepository = genreRepository;
        this.listingRepository = listingRepository;
    }

    /**
     * Returns all available genres.
     *
     * @return list of all genres
     */
    // Vai buscar todos os géneros para o utilizador escolher
    public List<Genre> getAllGenres(){
        return genreRepository.getAllGenres();
    }

    /**
     * Returns the list of items on direct sale of a given genre.
     *
     * @param selectedGenre genre selected by the buyer
     * @return list of items matching the given genre
     */
    public List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenGenre(Genre selectedGenre) {
        if (selectedGenre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        return listingRepository.getListOfItemsOnDirectSaleOfAGivenGenre(selectedGenre);
    }
  }
