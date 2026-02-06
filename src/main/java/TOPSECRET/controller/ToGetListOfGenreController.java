package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.dto.GenreDTO_US013;
import TOPSECRET.dto.GenreMapper_US013;
import TOPSECRET.repository.GenreRepository;

import java.util.List;

/**
 * Controller that retrieves the list of genres stored in the {@link GenreRepository}.
 * NOTE: This controller only delegates the request to the repository (it does not create or change genres).
 */
public class ToGetListOfGenreController {
    private final GenreRepository _genreRepository;
    private final GenreMapper_US013 _genreMapper;

    public ToGetListOfGenreController(GenreRepository genreRepository, GenreMapper_US013 genreMapper) {
        if (genreRepository == null) {
            throw new IllegalArgumentException("Genre Repository can't be null");
        }
        if (genreMapper == null) {
            throw new IllegalArgumentException("Genre Mapper can't be null");
        }
        this._genreRepository = genreRepository;
        this._genreMapper = genreMapper;
    }

    public List<GenreDTO_US013> getGenresList() {
        List<Genre> allGenre = _genreRepository.getAllGenres();
        return _genreMapper.toDTO(allGenre);
    }
}
