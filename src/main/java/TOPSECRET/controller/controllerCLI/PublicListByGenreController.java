package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.dto.US013GenreDTO;
import TOPSECRET.dto.US013GenreMapper;
import TOPSECRET.dto.US020CollectionOfPublicationsDTO;
import TOPSECRET.dto.US020CollectionOfPublicationsDTOMapper;
import TOPSECRET.services.CollectionOfPublicationsService;

import java.util.List;

/**
 * Controller for US020: Browse public collections of publications filtered by genre.
 *
 * <p>This controller orchestrates the retrieval of available genres and public
 * collections of publications filtered by a selected genre. It delegates persistence
 * concerns to the relevant repositories and service, and converts domain objects
 * into DTOs for presentation.</p>
 *
 * <p>Does not contain business rules — those live in the domain layer.</p>
 */
public class PublicListByGenreController {

    private final IGenreRepository _genreRepository;
    private final CollectionOfPublicationsService _collectionService;
    private final IUserRepository _userRepo;

    /**
     * Constructs a new {@code PublicListByGenreController} with the required dependencies.
     *
     * @param genreRepository   repository used to retrieve available genres;
     *                          must not be {@code null}
     * @param collectionService service used to retrieve public collections filtered by genre;
     *                          must not be {@code null}
     * @param userRepo          repository used to retrieve user information;
     *                          must not be {@code null}
     */
    public PublicListByGenreController(
            IGenreRepository genreRepository,
            CollectionOfPublicationsService collectionService,
            IUserRepository userRepo
    ) {
        this._genreRepository = genreRepository;
        this._collectionService = collectionService;
        this._userRepo = userRepo;
    }

    /**
     * Retrieves all available genres as DTOs.
     *
     * @return a list of {@link US013GenreDTO} representing all genres in the system;
     * may be empty if no genres exist
     */
    public List<US013GenreDTO> getGenres() {
        List<Genre> genreList = _genreRepository.findAll();
        US013GenreMapper genreMapper = new US013GenreMapper();

        return genreMapper.toDTOList(genreList);
    }

    /**
     * Retrieves all public collections of publications associated with the given genre.
     *
     * @param genreId the identifier of the genre used to filter public collections;
     *                must not be {@code null}
     * @return a list of {@link US020CollectionOfPublicationsDTO} matching the specified genre;
     * may be empty if no public collections exist for the given genre
     * @throws IllegalArgumentException if {@code genreId} is {@code null}
     */
    public List<US020CollectionOfPublicationsDTO> getPublicListsByGenre(GenreId genreId) throws IllegalArgumentException {

        if (genreId == null) {
            throw new IllegalArgumentException("Genre can't be null.");
        }

        List<CollectionOfPublications> collections = _collectionService.findPublicCollectionsByGenreId(genreId);

        US020CollectionOfPublicationsDTOMapper mapper = new US020CollectionOfPublicationsDTOMapper(_userRepo);

        return mapper.toDTO(collections);
    }
}
