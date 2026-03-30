package TOPSECRET.controller;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.dto.GenreDTO_US013;
import TOPSECRET.repository.GenreRepositoryMem;

import java.util.ArrayList;
import java.util.List;

/**
 * Application controller responsible for retrieving the list of genres.
 * <p>
 * This controller coordinates access to the {@link GenreRepositoryMem}
 * and transforms {@link Genre} domain objects into
 * {@link GenreDTO_US013} instances.
 * <p>
 * It does not contain business logic and does not modify the domain state.
 * Its responsibility is limited to delegation and data transformation,
 * preserving separation of concerns between the domain and presentation layers.
 */
public class US013ToGetListOfGenreController {
    private final GenreRepositoryMem _genreRepository;

    public US013ToGetListOfGenreController(GenreRepositoryMem genreRepository) {
        if (genreRepository == null) {
            throw new IllegalArgumentException("Genre Repository can't be null");
        }

        this._genreRepository = genreRepository;
    }

    public List<GenreDTO_US013> getGenresList() {

        List<GenreDTO_US013> genresDTO = new ArrayList<>();

        for (Genre genre : _genreRepository.getAllGenres()) {
            genresDTO.add(GenreDTO_US013.toDTO(genre));
        }

        return genresDTO;
    }
}
