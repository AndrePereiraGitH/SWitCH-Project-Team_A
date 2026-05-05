package TOPSECRET.controller;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.dto.US013GenreDTO;
import TOPSECRET.dto.US013GenreMapper;

import java.util.List;

/**
 * Application controller responsible for retrieving the list of official genres.
 *
 * <p>This controller acts as an entry point from the presentation layer (UI)
 * into the application layer. It coordinates the retrieval of {@link Genre}
 * domain objects through the {@link IGenreRepository} abstraction and
 * converts them into {@link US013GenreDTO} objects using the
 * {@link US013GenreMapper}.</p>
 *
 * <p>The controller does not contain business logic. It delegates all
 * data access responsibilities to the repository and all transformation
 * responsibilities to the mapper, ensuring proper separation of concerns.</p>
 *
 * <p>In the underlying architecture, the repository implementation may rely
 * on persistence mechanisms such as JPA, including the use of data models (DM),
 * assemblers, and factories to reconstruct domain objects. These details are
 * hidden behind the {@link IGenreRepository} interface.</p>
 */
public class ToGetListOfGenreController {

    /**
     * Repository abstraction used to retrieve genre domain objects.
     */
    private final IGenreRepository genreRepository;

    /**
     * Mapper responsible for converting domain objects into DTOs.
     */
    private final US013GenreMapper mapper;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param genreRepository the repository used to access genre data
     * @param mapper the mapper used to convert domain objects to DTOs
     * @throws IllegalArgumentException if any argument is null
     */
    public ToGetListOfGenreController(IGenreRepository genreRepository,
                                      US013GenreMapper mapper) {
        if (genreRepository == null) {
            throw new IllegalArgumentException("Genre Repository can't be null");
        }
        if (mapper == null) {
            throw new IllegalArgumentException("Genre Mapper can't be null");
        }

        this.genreRepository = genreRepository;
        this.mapper = mapper;
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
        List<Genre> allGenres = genreRepository.getAllGenres();
        return mapper.toDTOList(allGenres);
    }
}