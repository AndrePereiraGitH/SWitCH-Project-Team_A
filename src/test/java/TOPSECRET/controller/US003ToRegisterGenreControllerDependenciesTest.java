package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreFactory;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link US003ToRegisterGenreController}.
 * Uses real objects (no mocks) to verify the full chain works together:
 * Controller -> Repository -> Factory -> Domain.
 */
class US003ToRegisterGenreControllerDependenciesTest {

    // Real objects — register a valid genre and check the result
    @Test
    void shouldRegisterGenreSuccessfully() {

        // Arrange
        GenreRepository genreRepository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        US003ToRegisterGenreController controller =
                new US003ToRegisterGenreController(genreRepository);

        // Act
        Genre genre = controller.registerGenre("Drama");

        // Assert
        assertNotNull(genre);
        assertEquals("Drama", genre.getGenreType());
    }

    // Real objects — registering the same genre twice must throw an exception
    @Test
    void shouldThrowExceptionWhenGenreAlreadyExists() {

        // Arrange
        GenreRepository genreRepository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        US003ToRegisterGenreController controller =
                new US003ToRegisterGenreController(genreRepository);

        // Register once — succeeds
        controller.registerGenre("Science");

        // Act + Assert — second register must fail
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerGenre("Science"));
    }
}