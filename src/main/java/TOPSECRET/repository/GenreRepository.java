package TOPSECRET.repository;

import TOPSECRET.domain.*;

import java.util.List;

/**
 * Repository responsible for creating and storing Genre objects.
 * Delegates the creation of Genre instances to {@link GenreFactory}.
 * NOTE: Returns an unmodifiable copy of the internal list.
 */
public class GenreRepository {
    private final List<Genre> _genres;
    private final GenreFactory _genreFactory;

    public GenreRepository(List<Genre> genres, GenreFactory genreFactory) {
        if (genres == null) {
            throw new IllegalArgumentException("Genre List cannot be null");
        }
        if (genreFactory == null) {
            throw new IllegalArgumentException("GenreFactory cannot be null");
        }
        this._genres = genres;
        this._genreFactory = genreFactory;
    }

    /**
     * Creates a new Genre using the factory and adds it to the repository.
     *
     * @param genreType the type of genre to create
     * @return the created Genre
     * @throws IllegalArgumentException if the genre already exists
     */
    public Genre createAndAdd(String genreType) {
        Genre genre = _genreFactory.create(genreType);
        if (!exists(genre)) {
            this._genres.add(genre);  // Add the genre that was passed in
            return genre;
        }
        throw new IllegalArgumentException("Genre already exists");
    }

    // ----------------------------- US004 starts -------------------------------
    /**
     * Adds a Genre to the repository.
     * @param genre Genre to add
     */
    public void addGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        if (exists(genre)) {
            throw new IllegalArgumentException("Genre already exists");
        }

        _genres.add(genre);
    }

    /**
     * US004
     * Finds a Genre by title.
     * @param title Genre title to search.
     * @return Existing Genre domain object.
     * @throws IllegalArgumentException if title is null, blank or not found.
     */
    public Genre findByTitle(String title) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Genre title can't be null or blank.");
        }

        for (Genre genre : _genres) {
            if (genre.getGenreType().equalsIgnoreCase(title)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Genre not found.");
    }
    // ----------------------------- US004 ends -------------------------------
    // ----------------------------- US003 starts -------------------------------

    public List<Genre> getAllGenres() {
        return List.copyOf(_genres);            // Defensive copy + unmodifiable list: protects repository state from external changes
    }

    private boolean exists(Genre genre) {
        for (Genre existingGenre : this._genres) {   
            if (genre.equals(existingGenre)) {
                return true;
            }
        }
        return false;
    }
    // ----------------------------- US003 ends -------------------------------



}

