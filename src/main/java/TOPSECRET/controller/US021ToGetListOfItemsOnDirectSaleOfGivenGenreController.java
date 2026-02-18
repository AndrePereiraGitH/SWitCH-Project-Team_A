package TOPSECRET.controller;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Item;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.GenreRepository;

import java.util.List;

public class US021ToGetListOfItemsOnDirectSaleOfGivenGenreController {
    private GenreRepository genreRepository;
    private DirectSaleRepository directSaleRepository;

    public US021ToGetListOfItemsOnDirectSaleOfGivenGenreController (GenreRepository genreRepository,  DirectSaleRepository directSaleRepository) {
        if (genreRepository == null || directSaleRepository == null) {
            throw new IllegalArgumentException("GenreRepository and directSaleRepository cannot be null");
        }

        this.genreRepository = genreRepository;
        this.directSaleRepository = directSaleRepository;
    }


    // Vai buscar todos os géneros para o utilizador escolher
    public List<Genre> getAllGenres(){
        return genreRepository.getAllGenres();
    }


    // US021 Start ---------------------------------------------------------
    public List<Item> getListOfItemsOnDirectSaleOfAGivenGenre(Genre selectedGenre) {
        if (selectedGenre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        return directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(selectedGenre);
    }
    // US021 End -----------------------------------------------------------

}
