package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US017GenreDTO;
import TOPSECRET.dto.US017GenreMapper;

import java.util.List;

/**
 * Controller for US017 - Create a private collection of publications.
 *
 * <p>Connects the UI to the domain layer. Provides the list of available genres
 * and delegates collection creation to the factory and repository.
 * Does not contain business rules — those live in the domain layer.
 */
public class ToCreateCollectionOfPublicationsController {

    private final IGenreRepository genreRepository;
    private final ICollectionOfPublicationsRepository collectionRepository;
    private final CollectionOfPublicationsFactory factory;

    /**
     * @param genreRepository      repository that holds the available genres
     * @param collectionRepository repository that stores collections
     * @param factory              factory that creates CollectionOfPublications instances
     * @throws IllegalArgumentException if any dependency is null
     */
    public ToCreateCollectionOfPublicationsController(
            IGenreRepository genreRepository,
            ICollectionOfPublicationsRepository collectionRepository,
            CollectionOfPublicationsFactory factory
    ) {
        if (genreRepository == null) {
            throw new IllegalArgumentException("Genre repository cannot be null.");
        }
        if (collectionRepository == null) {
            throw new IllegalArgumentException("Collection repository cannot be null.");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null.");
        }

        this.genreRepository = genreRepository;
        this.collectionRepository = collectionRepository;
        this.factory = factory;
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
     * @throws IllegalArgumentException if userId or genreId is null
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

        CollectionOfPublications collection = factory.create(name, genreId, userId);
        return collectionRepository.save(collection);
    }
}