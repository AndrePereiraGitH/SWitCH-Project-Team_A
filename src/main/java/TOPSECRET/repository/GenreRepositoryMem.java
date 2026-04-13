package TOPSECRET.repository;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsible for creating and storing Genre objects.
 * Delegates the creation of Genre instances to {@link GenreFactory}.
 * NOTE: Returns an unmodifiable copy of the internal list.
 */
public class GenreRepositoryMem implements IGenreRepository {
    private final List<Genre> _genres = new ArrayList<>();
    private final GenreFactory _genreFactory;

    /**
     * Constructs a new {@code GenreRepositoryMem} with the provided {@link GenreFactory}.
     *
     * <p>The factory is used to delegate the creation of {@link Genre} instances
     * within this repository.</p>
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


    // ----------------------------- US003 starts -------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Genre createAndAdd(String genreType) {
        Genre genre = _genreFactory.create(genreType);
        if (!exists(genre)) {
            this._genres.add(genre);  // Add the genre that was passed in
            return genre;
        }
        throw new IllegalArgumentException("Genre already exists");
    }
    // ----------------------------- US003 starts -------------------------------

    // ----------------------------- US004 starts -------------------------------

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
    // ----------------------------- US004 ends -------------------------------
    // ----------------------------- US013 starts -------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genre> getAllGenres() {
        return List.copyOf(_genres);            // Defensive copy + unmodifiable list: protects repository state from external changes
    }

    /**
     * Checks whether a genre already exists in the genre collection.
     *
     * <p>Iterates through the internal list of genres and compares each element
     * with the provided genre using the {@link Genre#equals(Object)} method.</p>
     *
     * @param genre the genre to check; must not be {@code null}
     * @return {@code true} if the genre already exists in the collection;
     * {@code false} otherwise
     */
    private boolean exists(Genre genre) {
        for (Genre existingGenre : this._genres) {
            if (genre.equals(existingGenre)) //uses sameAs() for duplicate test
                return true;
        }
        return false;
    }
    // ----------------------------- US013 ends -------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Genre> findGenreById(GenreID genreId){
        if (genreId == null) {
            throw new IllegalArgumentException("GenreId cannot be null");
        }

        return _genres.stream()
                .filter(genre -> genre.id().equals(genreId))
                .findFirst();
    }

}

