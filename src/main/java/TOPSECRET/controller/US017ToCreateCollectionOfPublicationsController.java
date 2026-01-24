package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.User;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.dto.GenreMapper_US017;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for US017 – Create a private collection of publications.

 * Responsibilities:
 * - Providing the list of available genres to the UI
 * - Delegating the creation of a private collection to the repository

 * All business rules (such as duplicate validation) are handled by the repository, not by this controller.
 */
public class US017ToCreateCollectionOfPublicationsController {

    private final GenreRepository genreRepository;
    private final CollectionOfPublicationsRepository collectionRepository;
    private final GenreMapper_US017 genreMapper;

    public US017ToCreateCollectionOfPublicationsController(
            GenreRepository genreRepository,
            CollectionOfPublicationsRepository collectionRepository,
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
     * Used by the UI to allow genre selection.
     */
    public List<GenreDTO_US017> getAllGenres() {

        List<GenreDTO_US017> result = new ArrayList<>();

        List<Genre> genres = genreRepository.getAllGenres();

        for (int i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            result.add(genreMapper.toDTO(genre));
        }

        return result;
    }


    /**
     * Creates a new private collection for a user.
     * - The collection is created with a name and a genre.
     * - The collection is always created as private.
     * Duplicate validation is handled by the repository.
     */
    public CollectionOfPublications createCollection(
            String name,
            Genre selectedGenre,
            User user
    ) {
        if (user == null || selectedGenre == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        return collectionRepository.createIfNotExists(user, name, selectedGenre, true);
    }

}

