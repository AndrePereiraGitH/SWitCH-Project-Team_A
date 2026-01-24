package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.repository.GenreRepository;

public class US003ToRegisterGenreController {

    private GenreRepository _genreRepository;

    public US003ToRegisterGenreController(GenreRepository genreRepository) {

        _genreRepository = genreRepository;
    }

    public Genre registerGenre(String genreType) {
        return _genreRepository.createAndAddGenre(genreType);
    }
}
