package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.repository.GenreRepositoryMem;

/**
 * Controller responsible for handling the registration of a new genre (US003).
 *
 * <p>This controller receives a genre name from the UI layer and delegates
 * the creation and storage to the {@link GenreRepositoryMem}.</p>
 */
public class US003ToRegisterGenreController {

    private final GenreRepositoryMem _genreRepository;

    /**
     * Creates a new instance of the controller.
     *
     * @param genreRepository the repository used to create and store genres;
     *                        must not be {@code null}
     * @throws IllegalArgumentException if {@code genreRepository} is {@code null}
     */
    public US003ToRegisterGenreController(GenreRepositoryMem genreRepository) {

        if (genreRepository == null) {
            throw new IllegalArgumentException("Genre repository must not be null");
        }

        _genreRepository = genreRepository;
    }

    /**
     * Registers a new genre in the system.
     *
     * <p>Delegates to the repository which is responsible for creating the
     * {@link Genre} object and ensuring no duplicates exist.</p>
     *
     * @param genreType the name of the genre to register (e.g. "Drama")
     * @return the newly created {@link Genre}
     * @throws IllegalArgumentException if the genre already exists or the name is invalid
     */
    public Genre registerGenre(String genreType) {
        return _genreRepository.createAndAdd(genreType);
    }
}
