package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.User;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * US017 Controller - Create private collection.
 * Basically does two things:
 * - Provides a list of available genres
 * - Creates the collection by calling the repository
 */
public class US017ToCreateCollectionOfPublicationsController {
    private final GenreRepository genreRepository;
    private final CollectionOfPublicationsRepository collectionRepository;

    public US017ToCreateCollectionOfPublicationsController(
            GenreRepository genreRepository,
            CollectionOfPublicationsRepository collectionRepository
    ) {
        if (genreRepository == null || collectionRepository == null) {
            throw new IllegalArgumentException("Dependencies cannot be null.");
        }

        this.genreRepository = genreRepository;
        this.collectionRepository = collectionRepository;
    }

    /**
     * Retrieves all genres and converts them to DTOs.
     * The UI uses this to display the options to the user.
     */
    public List<GenreDTO_US017> getAllGenres() {

        List<GenreDTO_US017> result = new ArrayList<>();  //faz parte da lógica interna do controller

        List<Genre> genres = genreRepository.getAllGenres();

        // Converte cada Genre para DTO
        for (int i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            result.add(GenreDTO_US017.toDTO(genre));
        }

        return result;
    }

    /**
     * Creates a private collection for the user.
     * The repository checks if it already exists.
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

