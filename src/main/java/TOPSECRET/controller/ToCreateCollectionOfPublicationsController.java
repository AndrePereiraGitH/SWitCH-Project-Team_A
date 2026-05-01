package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US017GenreDTO;
import TOPSECRET.dto.US017GenreMapper;

import java.util.List;

/**
 * Controller for US017 - Create a private collection of publications.
 *
 * <p>Connects the UI to the domain layer. Provides the list of available genres
 * and delegates collection creation to the repository.
 * Does not contain business rules — those live in the repository and domain.
 */
public class ToCreateCollectionOfPublicationsController {

    private final IGenreRepository genreRepository;
    private final ICollectionOfPublicationsRepository collectionRepository;

    /**
     * @param genreRepository      repository that holds the available genres
     * @param collectionRepository repository that creates and stores collections
     * @throws IllegalArgumentException if any dependency is null
     */
    public ToCreateCollectionOfPublicationsController(
            IGenreRepository genreRepository,
            ICollectionOfPublicationsRepository collectionRepository
    ) {
        if (genreRepository == null || collectionRepository == null) {
            throw new IllegalArgumentException("Dependencies cannot be null.");
        }

        this.genreRepository = genreRepository;
        this.collectionRepository = collectionRepository;
    }

    /**
     * Returns all available genres as DTOs.
     *
     * @return list of genres ready to display in the UI
     */
    public List<US017GenreDTO> getAllGenres() {
        List<Genre> genres = genreRepository.getAllGenres();
        return US017GenreMapper.toDTOList(genres);
    }

    /**
     * Creates a new private collection of publications for the given user.
     *
     * @param name    name chosen by the user for the collection
     * @param genreId identifier of the genre chosen by the user
     * @param userId  identifier of the user who owns the collection
     * @return the created {@link CollectionOfPublications}, always private
     * @throws IllegalArgumentException if userId or genreId is null, or if a duplicate exists
     */
    public CollectionOfPublications createCollection(
            String name,
            GenreID genreId,
            UserId userId
    ) {
        if (userId == null || genreId == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return collectionRepository.createAndAdd(userId, name, genreId);
    }
}
