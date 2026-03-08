package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.repository.GenreRepository;

/**
 * Controller for US003 - register a new genre.
 */

public class US003ToRegisterGenreController {

    private final GenreRepository _genreRepository;

    /**
     * Constructor.
     *
     * @param genreRepository Genre repository
     */
    public US003ToRegisterGenreController(GenreRepository genreRepository) {

        _genreRepository = genreRepository;
    }

    /**
     * Registers a new genre in the system.
     *
     * @param genreType Genre name
     * @return Created genre
     */
    public Genre registerGenre(String genreType) {
        return _genreRepository.createAndAdd(genreType);
    }
}
