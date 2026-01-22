package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.repository.GenreRepository;
import java.util.List;

public class US013_ToGetListOfGenreController {
    private final GenreRepository _genreRepository;

    public US013_ToGetListOfGenreController(GenreRepository genreRepository) {
        if (genreRepository == null) throw new IllegalArgumentException("Genre Repository can't be null");
        this._genreRepository = genreRepository;
    }

    public List<Genre> getGenresList() {
        return _genreRepository.getAllGenres();
    }
}
