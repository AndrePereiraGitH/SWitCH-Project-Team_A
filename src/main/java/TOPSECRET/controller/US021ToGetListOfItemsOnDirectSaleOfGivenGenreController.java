package TOPSECRET.controller;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Item;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.GenreRepository;

import java.util.List;

/**
 * Controller for US021 - get the list of items on direct sale of a given genre.
 */
public class US021ToGetListOfItemsOnDirectSaleOfGivenGenreController {
    private GenreRepository genreRepository;
    private DirectSaleRepository directSaleRepository;

    /**
     * Constructor.
     *
     * @param genreRepository genre repository
     * @param directSaleRepository direct sale repository
     */
    public US021ToGetListOfItemsOnDirectSaleOfGivenGenreController (GenreRepository genreRepository,  DirectSaleRepository directSaleRepository) {
        if (genreRepository == null || directSaleRepository == null) {
            throw new IllegalArgumentException("GenreRepository and directSaleRepository cannot be null");
        }

        this.genreRepository = genreRepository;
        this.directSaleRepository = directSaleRepository;
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
    public List<Item> getListOfItemsOnDirectSaleOfAGivenGenre(Genre selectedGenre) {
        if (selectedGenre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        return directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(selectedGenre);
    }
  }
