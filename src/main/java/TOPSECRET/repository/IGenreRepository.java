package TOPSECRET.repository;

import TOPSECRET.domain.Genre;

import java.util.List;

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
}

