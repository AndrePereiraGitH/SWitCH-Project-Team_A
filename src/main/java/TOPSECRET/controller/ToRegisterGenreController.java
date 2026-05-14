package TOPSECRET.controller;

import TOPSECRET.services.GenreService;

/**
 * Controller responsible for handling the registration of a new genre (US003).
 *
 * <p>This controller receives a genre name from the UI layer and delegates
 * the creation and storage to the {@link GenreService}.</p>
 */
public class ToRegisterGenreController {

    private final GenreService genreService;

    /**
     * Creates a new instance of the controller.
     *
     * @param genreService the service used to create and persist genres;
     *                     must not be {@code null}
     * @throws IllegalArgumentException if {@code genreService} is {@code null}
     */
    public ToRegisterGenreController(GenreService genreService) {

        if (genreService == null) {
            throw new IllegalArgumentException("GenreService must not be null");
        }

        this.genreService = genreService;
    }


    /**
     * Registers a new genre in the system.
     *
     * <p>Delegates to the service layer which is responsible for creating the
     * Genre object and ensuring no duplicates exist.</p>
     *
     * @param genreType the name of the genre to register (e.g. "Drama")
     * @return {@code true} if the genre was successfully created and persisted
     * @throws IllegalArgumentException if the genre already exists or the name is invalid
     */
    public boolean registerGenre(String genreType) {
        genreService.createAndSave(genreType);
        return true;
    }
}
