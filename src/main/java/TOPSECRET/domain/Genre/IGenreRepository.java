package TOPSECRET.domain.Genre;

import TOPSECRET.domain.ValueObjects.GenreID;

import java.util.List;
import java.util.Optional;

/**
 * Contract for managing {@link Genre} objects.
 * <p>
 * Defines operations for creating, storing, retrieving, and searching genres
 * according to their type.
 * </p>
 */

public interface IGenreRepository {

    /**
     * Creates a new genre and adds it to the repository if it does not already exist.
     *
     * @param genreType the type of the genre to create
     * @return the created and stored genre
     * @throws IllegalArgumentException if a genre with the same type already exists
     */
       Genre createAndAdd(String genreType);

    /**
     * Returns the genre matching the given title.
     *
     * <p>This method searches the repository's internal collection and returns
     * the first genre whose type matches the given title, ignoring case.
     * The search is case-insensitive.</p>
     *
     * @param title the title of the genre to search for
     * @return the genre matching the given title; never {@code null}
     * @throws IllegalArgumentException if {@code title} is {@code null}, blank, or not found
     */
        Genre findByTitle(String title);


    /**
     * Returns the complete list of official genres available in the system.
     * <p>
     * The returned list is an unmodifiable defensive copy of the repository's
     * internal collection, protecting the repository's internal state from
     * external modifications.
     * </p>
     *
     * @return an unmodifiable list of all official genres; may be empty if no genres have been registered
     */
        List<Genre> getAllGenres();


    /**
     * Retrieves a {@link Genre} by its unique identifier.
     *
     * <p>This method searches the repository's internal collection and returns
     * the genre whose {@link GenreID} matches the provided identifier.</p>
     *
     * @param id the identifier of the genre to retrieve; must not be {@code null}
     * @return the {@link Genre} associated with the given identifier
     *
     * @throws IllegalArgumentException if {@code id} is {@code null}
     * @throws IllegalArgumentException if no genre with the given identifier is found
     */
        Genre findById(GenreID id);


    /**
     * Returns the genre matching the ID.
     *
     * <p>This method searches the repository's internal collection and returns
     * the genre corresponding to the genreID.
     *
     * @param genreId the ID of the genre to search for
     * @return the genre matching the ID; never {@code null}
     * @throws IllegalArgumentException if {@code genreId} is {@code null}, blank, or not found
     */
    Optional<Genre> findGenreById(GenreID genreId);

}

