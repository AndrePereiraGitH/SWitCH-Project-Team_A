package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.GenreAuthorizationPolicy;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import org.springframework.security.access.AccessDeniedException;
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

    private final IGenreRepository repository;
    private final GenreFactory factory;
    private final GenreAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    /**
     * Constructs a {@code GenreService} with the required collaborators.
     *
     * @param repository the repository used to persist genres; must not be {@code null}
     * @param factory    the factory used to create {@link Genre} domain objects; must not be {@code null}
     */
    public GenreService(IGenreRepository repository, GenreFactory factory,
                        GenreAuthorizationPolicy policy, IUserRepository userRepository) {

        this.repository = Objects.requireNonNull(repository, "Genre Repository must not be null");
        this.factory = Objects.requireNonNull(factory, "Genre Factory must not be null");
        this.policy = Objects.requireNonNull(policy, "Genre Authorization Policy must not be null");
        this.userRepository = Objects.requireNonNull(userRepository , "User Repository must not be null");
    }

    /**
     * Creates a new {@link Genre} and persists it in the repository.
     *
     * @param genreType the type of genre to create
     * @return the persisted {@link Genre}
     * @throws IllegalArgumentException if a genre with the same type already exists
     */
    public Genre createAndSave(String genreType, UserId userId) throws InstanceAlreadyExistsException, AccessDeniedException {

        User user = userRepository.findByID(userId).get();

        if(!policy.canCreateGenre(user)) {
            throw new AccessDeniedException("You do not have permission to create Genre");
        }

        if (repository.findByTitle(genreType).isPresent()) {
            throw new IllegalArgumentException(
                    "Genre with type '" + genreType + "' already exists");
        }

        Genre genre = factory.create(genreType);
        return repository.save(genre);
    }

    /**
     * Returns the genre matching the given title, if it exists.
     */
    public Optional<Genre> findByTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        return repository.findByTitle(title);
    }

    /**
     * Returns the complete list of official genres available in the system.
     */
    public List<Genre> findAll() {
        return repository.findAll();
    }

    /**
     * Returns the genre matching the ID.
     */
    public Optional<Genre> findById(GenreId genreId) {
        if (genreId == null) {
            throw new IllegalArgumentException("GenreId cannot be null");
        }
        return repository.findById(genreId);
    }
}