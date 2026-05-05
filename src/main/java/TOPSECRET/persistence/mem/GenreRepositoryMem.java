package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Genre.Genre;
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
    public Optional<Genre> findByTitle(String title) {
        if (title == null || title.isBlank()) {
            return Optional.empty();
        }
        return _genres.stream()
                .filter(genre -> genre.getGenreType().equalsIgnoreCase(title))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genre> findAll() {
        return List.copyOf(_genres);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Genre> findById(GenreId genreId) {
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