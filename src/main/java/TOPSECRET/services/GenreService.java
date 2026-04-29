package TOPSECRET.services;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import org.springframework.stereotype.Service;
import java.util.Objects;

/**
 * Application service responsible for managing {@link Genre} creation and persistence.
 *
 * <p>
 * This service coordinates the interaction between the domain layer and the
 * persistence layer. It delegates the creation of {@link Genre} aggregates
 * to the {@link GenreFactory} and ensures their persistence through the
 * {@link IGenreRepository}.
 * </p>
 *
 * <p>
 * This class does not contain business rules related to the internal consistency
 * of the {@link Genre} aggregate. Such rules are enforced within the domain layer.
 * Its responsibility is to orchestrate operations and validate input parameters.
 * </p>
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
     * @param genreType the type of genre to create; must not be {@code null} or blank
     * @return the persisted {@link Genre}
     * @throws NullPointerException if {@code genreType} is {@code null}
     */
    public Genre createAndSave(String genreType) {
        Objects.requireNonNull(genreType, "GenreType cannot be null");

        Genre genreSaved = genreRepository.createAndAdd(genreType);

        return genreSaved;
    }
}