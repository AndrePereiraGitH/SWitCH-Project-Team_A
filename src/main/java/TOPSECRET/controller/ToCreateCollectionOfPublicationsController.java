package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US017GenreDTO;
import TOPSECRET.dto.US017GenreMapper;
import TOPSECRET.services.CollectionOfPublicationsService;

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
     * @param genreRepository   repository that holds the available genres
     * @param collectionService service that handles collection creation and persistence
     * @throws IllegalArgumentException if any dependency is null
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
     * @return list of genres ready to display in the UI
     */
    public List<US017GenreDTO> getAllGenres() {
        return US017GenreMapper.toDTOList(genreRepository.findAll());
    }

    /**
     * Creates a new private collection of publications for the given user.
     *
     * @param name    name chosen by the user for the collection
     * @param genreId identifier of the genre chosen by the user
     * @param userId  identifier of the user who owns the collection
     * @return the created {@link CollectionOfPublications}, always private
     * @throws IllegalArgumentException if name is null/blank, or userId/genreId is null
     */
    public CollectionOfPublications createCollection(
            String name,
            GenreId genreId,
            UserId userId
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        if (userId == null || genreId == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return collectionService.createAndSave(name, genreId, userId);
    }
}