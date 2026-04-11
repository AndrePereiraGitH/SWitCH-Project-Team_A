package TOPSECRET.controller;

import TOPSECRET.domain.Genre.IGenreRepository;

/**
 * Controller responsible for handling the registration of a new genre (US003).
 *
 * <p>This controller receives a genre name from the UI layer and delegates
 * the creation and storage to the {@link IGenreRepository}.</p>
 */
public class US003ToRegisterGenreController {

    private final IGenreRepository _genreRepository;

    /**
     * Creates a new instance of the controller.
     *
     * @param genreRepository the repository used to create and store genres;
     *                        must not be {@code null}
     * @throws IllegalArgumentException if {@code genreRepository} is {@code null}
     */
    public US003ToRegisterGenreController(IGenreRepository genreRepository) {

        if (genreRepository == null) {
            throw new IllegalArgumentException("Genre repository must not be null");
        }

        _genreRepository = genreRepository;
    }

    /**
     * Registers a new genre in the system.
     *
     * <p>Delegates to the repository which is responsible for creating the
     * Genre object and ensuring no duplicates exist.</p>
     *
     * @param genreType the name of the genre to register (e.g. "Drama")
     * @return {@code true} if the genre was successfully created and added
     * @throws IllegalArgumentException if the genre already exists or the name is invalid
     */
    public boolean registerGenre(String genreType) {
        _genreRepository.createAndAdd(genreType);
        return true;
    }
}
