
package TOPSECRET.controller;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.dto.CollectionOfPublicationsDTOMapper_US020;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US020;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepositoryMem;

import java.util.ArrayList;
import java.util.List;

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

    private final GenreRepositoryMem _genreRepository;
    private final ICollectionOfPublicationsRepository _collectionRepo;
    private final IUserRepository _userRepo;


    /**
     * Constructs the controller with the required repositories.
     *
     * @param genreRepository repository used to retrieve genres
     * @param collectionRepo  repository used to retrieve collections filtered by genre
     */
    public US020PublicListByGenreController(GenreRepositoryMem genreRepository, ICollectionOfPublicationsRepository collectionRepo, IUserRepository userRepo)
    {
        this._genreRepository = genreRepository;
        this._collectionRepo = collectionRepo;
        this._userRepo = userRepo;
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
     * @param genreID the genre used to filter public publication lists
     * @return a list of {@link CollectionOfPublicationsDTO_US020} matching the specified genre
     * @throws IllegalArgumentException if the provided genre is {@code null}
     */
    public List<CollectionOfPublicationsDTO_US020> getPublicListsByGenre(GenreID genreID) throws IllegalArgumentException {

        if (genreID == null) {
            throw new IllegalArgumentException("Genre can't be null.");
        }

        CollectionOfPublicationsDTOMapper_US020 mapper = new CollectionOfPublicationsDTOMapper_US020(_collectionRepo, _userRepo);

        return mapper.toDTO(genreID);
    }
}
