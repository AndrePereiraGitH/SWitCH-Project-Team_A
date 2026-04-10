package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.dto.GenreMapper_US017;

import java.util.List;

/**
 * Controller for US017 - Create a private collection of publications.
 *
 * <p>Connects the UI to the domain layer. Provides the list of available genres
 * and delegates collection creation to the repository.
 * Does not contain business rules — those live in the repository and domain.
 */
public class US017ToCreateCollectionOfPublicationsController {

    private final IGenreRepository genreRepository;
    private final ICollectionOfPublicationsRepository collectionRepository;
    private final GenreMapper_US017 genreMapper;

    /**
     * @param genreRepository      repository that holds the available genres
     * @param collectionRepository repository that creates and stores collections
     * @param genreMapper          mapper that converts Genre objects to DTOs
     * @throws IllegalArgumentException if any dependency is null
     */
    public US017ToCreateCollectionOfPublicationsController(
            IGenreRepository genreRepository,
            ICollectionOfPublicationsRepository collectionRepository,
            GenreMapper_US017 genreMapper
    ) {
        if (genreRepository == null || collectionRepository == null || genreMapper == null) {
            throw new IllegalArgumentException("Dependencies cannot be null.");
        }

        this.genreRepository = genreRepository;
        this.collectionRepository = collectionRepository;
        this.genreMapper = genreMapper;
    }

    /**
     * Returns all available genres as DTOs.
     *
     * @return list of genres ready to display in the UI
     */
    public List<GenreDTO_US017> getAllGenres() {
        List<Genre> genres = genreRepository.getAllGenres();
        return genreMapper.toDTOList(genres);
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
