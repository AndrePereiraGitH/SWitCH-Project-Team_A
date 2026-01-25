package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        GenreRepository repo = new GenreRepository(new ArrayList<>());
        repo.createAndAddGenre("Romance");

        ToGetListOfGenreController controller = new ToGetListOfGenreController(repo);

        List<Genre> result = controller.getGenresList();

        assertTrue(result.contains(new Genre("Romance")));
    }
}
