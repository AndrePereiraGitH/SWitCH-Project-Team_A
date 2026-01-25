package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToGetListOfGenreControllerTest {
    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================

    @Test
    void shouldCreateControllerWithValidRepository() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>());

        // Act
        ToGetListOfGenreController controller = new ToGetListOfGenreController(repository);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new ToGetListOfGenreController(null)
        );
        assertEquals("Genre Repository can't be null", exception.getMessage());
    }

    // ========================================
    // GET GENRES LIST TESTS
    // ========================================

    @Test
    void shouldReturnEmptyListWhenNoGenres() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>());
        ToGetListOfGenreController controller = new ToGetListOfGenreController(repository);

        // Act
        List<Genre> result = controller.getGenresList();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnGenresFromRepository() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>());
        repository.createAndAddGenre("Romance");
        repository.createAndAddGenre("Horror");

        ToGetListOfGenreController controller = new ToGetListOfGenreController(repository);

        // Act
        List<Genre> result = controller.getGenresList();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(new Genre("Romance")));
        assertTrue(result.contains(new Genre("Horror")));
    }

    @Test
    void shouldReturnSameListAsRepository() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>());
        repository.createAndAddGenre("Mystery");
        repository.createAndAddGenre("Thriller");

        ToGetListOfGenreController controller = new ToGetListOfGenreController(repository);

        // Act
        List<Genre> controllerResult = controller.getGenresList();
        List<Genre> repositoryResult = repository.getAllGenres();

        // Assert - same content
        assertEquals(repositoryResult, controllerResult);
    }
}
