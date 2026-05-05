package TOPSECRET.services;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
     * @param genreType the type of genre to create
     * @return the persisted {@link Genre}
     * @throws IllegalArgumentException if a genre with the same type already exists
     */
    public Genre createAndSave(String genreType) {
        if (genreRepository.findByTitle(genreType).isPresent()) {
            throw new IllegalArgumentException(
                    "Genre with type '" + genreType + "' already exists");
        }
        Genre genre = genreFactory.create(genreType);
        return genreRepository.save(genre);
    }
}