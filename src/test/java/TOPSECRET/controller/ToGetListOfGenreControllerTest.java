package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToGetListOfGenreControllerTest {
    @Test
    void constructor_shouldThrow_whenRepositoryIsNull() {
        // If null is passed, an IllegalArgumentException is expected
        assertThrows(IllegalArgumentException.class, () -> new ToGetListOfGenreController(null));
    }

    @Test
    void getGenresList_shouldReturnGenresFromRepository() {
        // Arrange
        // Get the repository instance and add a genre to it
        GenreRepository repo = GenreRepository.getINSTANCE();
        repo.createAndAddGenre("Romance");

        // Create the controller using the repository
        ToGetListOfGenreController controller =
                new ToGetListOfGenreController(repo);

        // Act
        List<Genre> result = controller.getGenresList();

        // Assert
        // Verify that the list contains the expected genre
        assertEquals(1, result.size());
        assertEquals("Romance", result.get(0).getGenreType());
    }
}
