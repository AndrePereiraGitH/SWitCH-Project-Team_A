package TOPSECRET.controller;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.dto.US013GenreDTO;
import TOPSECRET.dto.US013GenreMapper;
import TOPSECRET.persistence.mem.GenreRepositoryMem;

import java.util.List;

/**
 * Application controller responsible for retrieving the list of genres.
 * <p>
 * This controller coordinates access to the {@link GenreRepositoryMem}
 * and transforms {@link Genre} domain objects into
 * {@link US013GenreDTO} instances.
 * <p>
 * It does not contain business logic and does not modify the domain state.
 * Its responsibility is limited to delegation and data transformation,
 * preserving separation of concerns between the domain and presentation layers.
 */
public class ToGetListOfGenreController {
    private IGenreRepository _genreRepository;
    private US013GenreMapper _mapper;

    public ToGetListOfGenreController(GenreRepositoryMem genreRepository, US013GenreMapper mapper) {
        if (genreRepository == null) {
            throw new IllegalArgumentException("Genre Repository can't be null");
        }
        if (mapper == null) {
            throw new IllegalArgumentException("GenreMapper can't be null");
        }


        this._genreRepository = genreRepository;
        this._mapper = mapper;
    }

    /**
     * Retrieves all available genres and converts them into DTOs.
     *
     * <p>This method delegates the retrieval of genres to the repository,
     * which returns a list of domain objects. The result is then transformed
     * into a list of {@link US013GenreDTO} objects for use in the presentation layer.</p>
     *
     * @return a list of genre DTOs representing all official genres
     */
    public List<US013GenreDTO> getGenresList() {
        List<Genre> allGenreList = _genreRepository.findAll();
        return _mapper.toDTOList(allGenreList);
    }
}
