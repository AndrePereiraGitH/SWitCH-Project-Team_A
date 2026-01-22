package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToGetListOfGenreControllerTest {
    @Test
    void constructor_shouldThrow_whenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ToGetListOfGenreController(null));
    }

    @Test
    void getGenresList_shouldReturnGenresFromRepository() {
        // Arrange
        GenreRepository repo = GenreRepository.getINSTANCE();
        repo.createAndAddGenre("Romance");

        ToGetListOfGenreController controller =
                new ToGetListOfGenreController(repo);

        // Act
        List<Genre> result = controller.getGenresList();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Romance", result.get(0).getGenreType());
    }
}
