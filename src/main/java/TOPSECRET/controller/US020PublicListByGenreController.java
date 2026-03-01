
package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.dto.CollectionOfPublicationsDTO;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepository;

import java.util.List;

/**
 * Controller responsible for the use case of retrieving
 * public publication lists filtered by genre.
 * <p>
 * This controller coordinates access to:
 * <ul>
 *     <li>{@link GenreRepository} – to retrieve available genres</li>
 *     <li>{@link CollectionOfPublicationsRepository} – to retrieve public collections by genre</li>
 * </ul>
 */

public class US020PublicListByGenreController {

    private final GenreRepository _genreRepository;
    private final CollectionOfPublicationsRepository _collectionRepo;

    /**
     * Constructs the controller with the required repositories.
     *
     * @param genreRepository repository used to retrieve genres
     * @param collectionRepo  repository used to retrieve collections filtered by genre
     */
    public US020PublicListByGenreController(GenreRepository genreRepository, CollectionOfPublicationsRepository collectionRepo)
    {
        this._genreRepository = genreRepository;
        this._collectionRepo = collectionRepo;
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
     * @param genre the genre used to filter public publication lists
     * @return a list of {@link CollectionOfPublicationsDTO} matching the specified genre
     * @throws IllegalArgumentException if the provided genre is {@code null}
     */
    public List<CollectionOfPublicationsDTO> getPublicListsByGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre can't be null.");
        }
        return _collectionRepo.findPublicByGenre(genre);
    }
}
