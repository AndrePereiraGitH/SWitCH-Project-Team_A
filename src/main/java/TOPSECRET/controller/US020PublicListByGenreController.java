
package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US020_CollectionOfPublicationsDTOMapper;
import TOPSECRET.dto.US020_CollectionOfPublicationsDTO;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepositoryMem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller responsible for the use case of retrieving
 * public publication lists filtered by genre.
 * <p>
 * This controller coordinates access to:
 * <ul>
 *     <li>{@link GenreRepositoryMem} – to retrieve available genres</li>
 *     <li>{@link ICollectionOfPublicationsRepository} – to retrieve public collections by genre</li>
 * </ul>
 */

public class US020PublicListByGenreController {

    private final IGenreRepository _genreRepository;
    private final ICollectionOfPublicationsRepository _collectionRepo;
    private final IUserRepository _userRepo;
    private final US020_CollectionOfPublicationsDTOMapper _mapper;


    /**
     * Constructs the controller with the required repositories.
     *
     * @param genreRepository repository used to retrieve genres
     * @param collectionRepo  repository used to retrieve collections filtered by genre
     */
    public US020PublicListByGenreController(
            IGenreRepository genreRepository,
            ICollectionOfPublicationsRepository collectionRepo,
            IUserRepository userRepo,
            US020_CollectionOfPublicationsDTOMapper mapper) {

        this._genreRepository = genreRepository;
        this._collectionRepo = collectionRepo;
        this._userRepo = userRepo;
        this._mapper = mapper;
    }


    /**
     * Retrieves all available genres.
     *
     * @return a list containing all {@link Genre} instances
     */
    public List<Genre> getGenres() {
        return _genreRepository.getAllGenres();
    }


    /**
     * Retrieves all public publication lists associated with a given genre.
     *
     * @param genreId the genre used to filter public publication lists
     * @return a list of {@link US020_CollectionOfPublicationsDTO} matching the specified genre
     * @throws IllegalArgumentException if the provided genre is {@code null}
     */
    public List<US020_CollectionOfPublicationsDTO> getPublicListsByGenre(GenreID genreId) throws IllegalArgumentException {

        if (genreId == null) {
            throw new IllegalArgumentException("Genre can't be null.");
        }

        List<CollectionOfPublications> collections = _collectionRepo.findPublicCollectionsByGenre(genreId);

        Map<UserId, String> ownerNames = new HashMap<>();

        for (CollectionOfPublications c : collections) {
            UserId ownerId = c.getOwnerId();
            String ownerName = _userRepo.findUserByID(ownerId)
                    .map(User::getUserName)
                    .orElseThrow(() -> new IllegalStateException("User not found: " + ownerId));

            ownerNames.put(ownerId, ownerName);
        }

        return _mapper.toDTO(collections, ownerNames);
    }
}
