package TOPSECRET.domain.Genre;

import TOPSECRET.domain.ValueObjects.GenreId;

import java.util.List;
import java.util.Optional;

/**
 * Contract for managing {@link Genre} objects.
 * <p>
 * Defines operations for storing, retrieving, and searching genres
 * according to their type.
 * </p>
 */
public interface IGenreRepository {

    /**
     * Persists the given {@link Genre} in the repository.
     *
     * @param genre the genre to persist; must not be {@code null}
     * @return the persisted genre
     */
    Genre save(Genre genre);

    /**
     * Returns the genre matching the given title, if it exists.
     *
     * @param title the title of the genre to search for
     * @return an {@link Optional} containing the matching genre, or empty if not found
     */
    Optional<Genre> findByTitle(String title);

    /**
     * Returns the complete list of official genres available in the system.
     *
     * @return an unmodifiable list of all official genres; may be empty
     */
    List<Genre> findAll();

    /**
     * Returns the genre matching the ID.
     *
     * @param genreId the ID of the genre to search for
     * @return an {@link Optional} containing the matching genre, or empty if not found
     */
    Optional<Genre> findById(GenreId genreId);
}