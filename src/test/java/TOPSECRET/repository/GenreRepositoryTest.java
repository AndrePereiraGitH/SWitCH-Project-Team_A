package TOPSECRET.repository;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreRepositoryTest {

    @Test
    void createAndAddGenre_shouldThrowWhenGenreAlreadyExists_caseInsensitive() {
        GenreRepository repo = new GenreRepository(new ArrayList<>());
        repo.createAndAddGenre("Terror");

        assertThrows(IllegalArgumentException.class, () -> repo.createAndAddGenre("Terror"));
    }

    @Test
    void getAllGenres_shouldReturnUnmodifiableCopy() {
        GenreRepository repo = new GenreRepository(new ArrayList<>());
        repo.createAndAddGenre("Fantasy");

        List<Genre> list = repo.getAllGenres();

        assertThrows(UnsupportedOperationException.class, () -> list.add(new Genre("Horror")));
    }
}
