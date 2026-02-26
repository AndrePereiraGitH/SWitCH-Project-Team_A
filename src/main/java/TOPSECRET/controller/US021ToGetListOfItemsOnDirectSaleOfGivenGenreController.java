package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.repository.GenreRepository;
import TOPSECRET.repository.ListingRepository;
import java.util.List;

/**
 * Controller for US021 - get the list of items on direct sale of a given genre.
 */
public class US021ToGetListOfItemsOnDirectSaleOfGivenGenreController {
    private GenreRepository genreRepository;
    private ListingRepository listingRepository;

    /**
     * Constructor.
     *
     * @param genreRepository genre repository
     * @param listingRepository direct sale repository
     */
    public US021ToGetListOfItemsOnDirectSaleOfGivenGenreController (GenreRepository genreRepository,  ListingRepository listingRepository) {
        if (genreRepository == null || listingRepository == null) {
            throw new IllegalArgumentException("GenreRepository and listingRepository cannot be null");
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
