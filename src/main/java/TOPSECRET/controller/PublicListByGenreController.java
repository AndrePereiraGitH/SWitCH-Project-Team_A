package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.dto.US013GenreDTO;
import TOPSECRET.dto.US013GenreMapper;
import TOPSECRET.dto.US020CollectionOfPublicationsDTO;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.dto.US020CollectionOfPublicationsDTOMapper;
import TOPSECRET.persistence.mem.GenreRepositoryMem;

import java.util.List;

/**
 * Controller responsible for the use case of retrieving
 * public publication lists filtered by genre.
 * <p>
 * This controller coordinates access to:
 * <ul>
 *     <li>{@link GenreRepositoryMem} – to retrieve available genres</li>
 *     <li>{@link ICollectionOfPublicationsRepository} – to retrieve public collections by genre</li>
 *     <li>{@link IUserRepository} – to retrieve usernames of collections</li>
 * </ul>
 */

public class PublicListByGenreController {

    private final IGenreRepository _genreRepository;
    private final ICollectionOfPublicationsRepository _collectionRepo;
    private final IUserRepository _userRepo;


    /**
     * Constructs the controller with the required repositories.
     *
     * @param genreRepository repository used to retrieve genres
     * @param collectionRepo  repository used to retrieve collections filtered by genre
     * @param userRepo repository used to retrieve usernames
     */
    public PublicListByGenreController(
            IGenreRepository genreRepository,
            ICollectionOfPublicationsRepository collectionRepo,
            IUserRepository userRepo
            ) {
        this._genreRepository = genreRepository;
        this._collectionRepo = collectionRepo;
        this._userRepo = userRepo;
    }


    /**
     * Retrieves all available genres.
     *
     * @return a list containing all {@link Genre} instances
     */
    public List<US013GenreDTO> getGenres() {
        List<Genre> genreList = _genreRepository.getAllGenres();
        US013GenreMapper genreMapper = new US013GenreMapper();

        return genreMapper.toDTO(genreList);
    }


    /**
     * Retrieves all public publication lists associated with a given genre.
     *
     * @param genreId the genre used to filter public publication lists
     * @return a list of {@link US020CollectionOfPublicationsDTO} matching the specified genre
     * @throws IllegalArgumentException if the provided genre is {@code null}
     */
    public List<US020CollectionOfPublicationsDTO> getPublicListsByGenre(GenreId genreId) throws IllegalArgumentException {

        if (genreId == null) {
            throw new IllegalArgumentException("Genre can't be null.");
        }

        List<CollectionOfPublications> collections = _collectionRepo.findPublicCollectionsByGenreId(genreId);

        US020CollectionOfPublicationsDTOMapper mapper = new US020CollectionOfPublicationsDTOMapper(_userRepo);

        return mapper.toDTO(collections);
    }
}
