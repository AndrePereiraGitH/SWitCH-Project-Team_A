package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US017GenreDTO;
import TOPSECRET.dto.US017GenreMapper;
import TOPSECRET.services.CollectionOfPublicationsService;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Controller for US017 - Create a private collection of publications.
 *
 * <p>Connects the UI to the domain layer. Provides the list of available genres
 * and delegates collection creation to the service layer.
 * Does not contain business rules — those live in the domain layer.
 */
public class ToCreateCollectionOfPublicationsController {

    private final IGenreRepository genreRepository;
    private final CollectionOfPublicationsService collectionService;

    /**
     * Constructs a new {@code ToCreateCollectionOfPublicationsController} with the required dependencies.
     *
     * @param genreRepository   repository that holds the available genres; must not be {@code null}
     * @param collectionService service that handles collection creation and persistence;
     *                          must not be {@code null}
     * @throws IllegalArgumentException if any dependency is {@code null}
     */
    public ToCreateCollectionOfPublicationsController(
            IGenreRepository genreRepository,
            CollectionOfPublicationsService collectionService
    ) {
        if (genreRepository == null) {
            throw new IllegalArgumentException("Genre repository cannot be null.");
        }
        if (collectionService == null) {
            throw new IllegalArgumentException("Collection service cannot be null.");
        }

        this.genreRepository = genreRepository;
        this.collectionService = collectionService;
    }

    /**
     * Returns all available genres as DTOs.
     *
     * @return list of genres ready to display in the UI; may be empty if no genres exist
     */
    public List<US017GenreDTO> getAllGenres() {
        return US017GenreMapper.toDTOList(genreRepository.findAll());
    }

    /**
     * Creates a new private collection of publications for the given user.
     *
     * <p>Validates that the name is not null or blank, and that both {@code userId}
     * and {@code genreId} are not null before delegating to the service layer.
     * The collection is always created as private — this is a domain rule enforced
     * by {@link CollectionOfPublications}.</p>
     *
     * @param name    name chosen by the user for the collection; must not be {@code null} or blank
     * @param genreId identifier of the genre chosen by the user; must not be {@code null}
     * @param userId  identifier of the user who owns the collection; must not be {@code null}
     * @return the created {@link CollectionOfPublications}, always private
     * @throws IllegalArgumentException       if {@code name} is null or blank, or if
     *                                        {@code userId} or {@code genreId} is null
     * @throws AccessDeniedException          if the user does not have permission to create a collection
     * @throws InstanceAlreadyExistsException if a collection with the same identity already exists
     */
    public CollectionOfPublications createCollection(
            String name,
            GenreId genreId,
            UserId userId
    ) throws AccessDeniedException, InstanceAlreadyExistsException {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        if (userId == null || genreId == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return collectionService.createAndSave(name, genreId, userId);
    }
}