package TOPSECRET.controller;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Genre;
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


    // Vai buscar todos os géneros para o usuário escolher
    public List<Genre> getAllGenres(){
        return genreRepository.getAllGenres();
    }



    // Obtém lista de items em direct sale por género
    public List<DirectSale> getListOfItemsOnDirectSale(Genre selectedgenre){
        if (selectedgenre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        return directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(selectedgenre);

    }

}
