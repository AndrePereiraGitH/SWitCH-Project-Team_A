package TOPSECRET.services;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Application service responsible for managing {@link Genre} creation and persistence.
 *
 * <p>Coordinates the interaction between the domain layer and the persistence layer.
 * Delegates creation of {@link Genre} aggregates to {@link GenreFactory} and ensures
 * their persistence through {@link IGenreRepository}.</p>
 */
@Service
public class GenreService {

    private final IGenreRepository genreRepository;
    private final GenreFactory genreFactory;

    /**
     * Constructs a {@code GenreService} with the required collaborators.
     *
     * @param genreRepository the repository used to persist genres; must not be {@code null}
     * @param genreFactory    the factory used to create {@link Genre} domain objects; must not be {@code null}
     */
    public GenreService(IGenreRepository genreRepository, GenreFactory genreFactory) {
        this.genreRepository = Objects.requireNonNull(genreRepository, "genreRepository must not be null");
        this.genreFactory = Objects.requireNonNull(genreFactory, "genreFactory must not be null");
    }

    /**
     * Creates a new {@link Genre} and persists it in the repository.
     *
     * <p>The factory is responsible for creating the domain object,
     * and the repository is responsible for persisting it.</p>
     *
     * @param genreType the type of genre to create
     * @return the persisted {@link Genre}
     * @throws IllegalArgumentException if a genre with the same type already exists
     */
    public Genre createAndSave(String genreType) {
        try {
            genreRepository.findByTitle(genreType);
            throw new IllegalArgumentException(
                    "Genre with type '" + genreType + "' already exists");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("already exists")) {
                throw e;
            }
        }
        Genre genre = genreFactory.create(genreType);
        return genreRepository.save(genre);
    }

    /**
     * Returns all genres available in the system.
     *
     * @return list of all genres; may be empty
     */
    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    /**
     * Returns the genre matching the given title.
     *
     * @param title the genre name to search for
     * @return the matching genre
     * @throws IllegalArgumentException if the genre is not found
     */
    public Genre findByTitle(String title) {
        Objects.requireNonNull(title, "Title cannot be null");
        return genreRepository.findByTitle(title);
    }

    /**
     * Returns the genre matching the given ID.
     *
     * @param genreId the ID to search for
     * @return an {@link Optional} containing the matching genre, or empty if not found
     */
    public Optional<Genre> findGenreById(GenreId genreId) {
        Objects.requireNonNull(genreId, "GenreId cannot be null");
        return genreRepository.findGenreById(genreId);
    }
}