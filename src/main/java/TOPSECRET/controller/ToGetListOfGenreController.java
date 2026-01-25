package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.repository.GenreRepository;

import java.util.List;

/**
 * Controller that retrieves the list of genres stored in the {@link GenreRepository}.
 * NOTE: This controller only delegates the request to the repository (it does not create or change genres).
 */
public class ToGetListOfGenreController {
    private final GenreRepository _genreRepository;

    public ToGetListOfGenreController(GenreRepository genreRepository) {
        if (genreRepository == null) {
            throw new IllegalArgumentException("Genre Repository can't be null");
        }
        this._genreRepository = genreRepository;
    }

    public List<Genre> getGenresList() {
        return _genreRepository.getAllGenres();
    }
}
