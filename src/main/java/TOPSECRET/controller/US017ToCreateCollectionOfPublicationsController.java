package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.User.User;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.repository.ICollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepositoryMem;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for US017 - Create a private collection of publications.
 *
 * <p>This controller connects the UI to the domain layer.
 * It does two things:
 * <ul>
 *   <li>Give the user a list of genres to pick from</li>
 *   <li>Create the collection using the data the user provided</li>
 * </ul>
 *
 * <p>It does NOT hold any business rules — validation and duplicate checks
 * are done by the repository and the domain class.
 */
public class US017ToCreateCollectionOfPublicationsController {

    // the controller needs these two to do its job — both are injected so they can be mocked in tests
    private final GenreRepositoryMem genreRepository;
    private final ICollectionOfPublicationsRepository collectionRepository;

    /**
     * Creates the controller with the required repositories.
     *
     * @param genreRepository      repository that holds the available genres
     * @param collectionRepository repository that creates and stores collections
     * @throws IllegalArgumentException if either repository is null
     */
    public US017ToCreateCollectionOfPublicationsController(
            GenreRepositoryMem genreRepository,
            ICollectionOfPublicationsRepository collectionRepository
    ) {
        // both are required — without them the controller can't do anything
        if (genreRepository == null || collectionRepository == null) {
            throw new IllegalArgumentException("Dependencies cannot be null.");
        }

        this.genreRepository = genreRepository;
        this.collectionRepository = collectionRepository;
    }

    /**
     * Returns all available genres as DTOs so the UI can show them to the user.
     *
     * <p>Each {@link Genre} is converted to a {@link GenreDTO_US017} before being returned.
     * This way the UI only gets the data it needs (just the name), not the full domain object.
     *
     * @return list of genres ready to display
     */
    public List<GenreDTO_US017> getAllGenres() {

        List<GenreDTO_US017> result = new ArrayList<>();

        List<Genre> genres = genreRepository.getAllGenres();

        // convert each Genre to a DTO — the UI should not work with domain objects directly
        for (int i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            result.add(GenreDTO_US017.toDTO(genre));
        }

        return result;
    }

    /**
     * Creates a new private collection of publications for the given user.
     *
     * <p>The controller checks that user and genre are not null, then delegates
     * the actual creation to the repository. If a duplicate exists, the repository
     * throws an exception and this method lets it go up — it does not catch it.
     *
     * @param name          name chosen by the user for the collection
     * @param selectedGenre genre chosen by the user
     * @param user          the user who owns the collection
     * @return the created {@link CollectionOfPublications}, always private
     * @throws IllegalArgumentException if user or genre is null, or if a duplicate exists
     */
    public CollectionOfPublications createCollection(
            String name,
            Genre selectedGenre,
            User user
    ) {
        // basic guard — the repository also validates, but we check here too before calling it
        if (user == null || selectedGenre == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        // delegate to the repository — it handles duplicate detection and creation
        return collectionRepository.createIfNotExists(user, name, selectedGenre);
    }

}

