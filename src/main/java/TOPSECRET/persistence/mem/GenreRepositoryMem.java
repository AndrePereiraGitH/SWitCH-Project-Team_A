package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * In-memory implementation of {@link IGenreRepository}.
 * Stores genres in a local list for use in testing or non-persistent contexts.
 */
public class GenreRepositoryMem implements IGenreRepository {

    private final List<Genre> _genres = new ArrayList<>();
    private final GenreFactory _genreFactory;

    /**
     * Constructs a new {@code GenreRepositoryMem} with the provided {@link GenreFactory}.
     *
     * @param genreFactory the factory responsible for creating {@link Genre} objects;
     *                     must not be {@code null}
     * @throws IllegalArgumentException if {@code genreFactory} is {@code null}
     */
    public GenreRepositoryMem(GenreFactory genreFactory) {
        if (genreFactory == null) {
            throw new IllegalArgumentException("GenreFactory cannot be null");
        }
        this._genreFactory = genreFactory;
    }

    /**
     * Default no-args constructor.
     */
    public GenreRepositoryMem() {
        this._genreFactory = new GenreFactory();
    }

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public Genre save(Genre genre) {
        Objects.requireNonNull(genre, "Genre cannot be null");
        if (exists(genre)) {
            throw new IllegalArgumentException("Genre already exists");
        }
        _genres.add(genre);
        return genre;
    }



    // =========================================================================
    // Lookup
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genre> getAllGenres() {
        return List.copyOf(_genres);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Genre> findGenreById(GenreId genreId) {
        if (genreId == null) {
            throw new IllegalArgumentException("GenreId cannot be null");
        }
        return _genres.stream()
                .filter(genre -> genre.id().equals(genreId))
                .findFirst();
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /**
     * Checks whether a genre already exists in the collection.
     *
     * @param genre the genre to check; must not be {@code null}
     * @return {@code true} if the genre already exists; {@code false} otherwise
     */
    private boolean exists(Genre genre) {
        for (Genre existingGenre : _genres) {
            if (genre.equals(existingGenre))
                return true;
        }
        return false;
    }
}