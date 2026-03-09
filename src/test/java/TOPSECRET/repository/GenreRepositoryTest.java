package TOPSECRET.repository;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreRepositoryTest {

    private final GenreFactory genreFactory = mock(GenreFactory.class);
    private final List<Genre> genres = new ArrayList<>();
    private final Genre genre = mock(Genre.class);

    @Test
    void testCreateAndAddGenre_AddsNewGenre() {
        // Arrange
        GenreRepository genreRepository = new GenreRepository(genreFactory);

        String genreType = "Fantasy";
        when(genre.getGenreType()).thenReturn(genreType);
        when(genreFactory.create(genreType)).thenReturn(genre);


        // Act
        Genre result = genreRepository.createAndAdd(genreType);

        // Assert
        assertEquals(1, genreRepository.getAllGenres().size());
        assertEquals(genre, result);
    }

    @Test
    void testCreateAndAddGenre_ThrowsExceptionWhenGenreExists() {
        // Arrange
        String genreType = "Fantasy";
        when(genre.getGenreType()).thenReturn(genreType);
        when(genreFactory.create(genreType)).thenReturn(genre);

        GenreRepository genreRepository = new GenreRepository(genreFactory);
        genreRepository.createAndAdd(genreType);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> genreRepository.createAndAdd(genreType));
    }

    @Test
    void getAllGenres_ReturnsCorrectList() {
        // Arrange
        Genre genre2 = mock(Genre.class);
        when(genre.getGenreType()).thenReturn("Fantasy");
        when(genre2.getGenreType()).thenReturn("Science Fiction");
        when(genreFactory.create("Fantasy")).thenReturn(genre);
        when(genreFactory.create("Science Fiction")).thenReturn(genre2);


        GenreRepository genreRepository = new GenreRepository(genreFactory);
        genreRepository.createAndAdd("Fantasy");       // adiciona ao repo
        genreRepository.createAndAdd("Science Fiction");

        // Act
        List<Genre> result = genreRepository.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(genre));
        assertTrue(result.contains(genre2));
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Genre.class)));
    }

    @Test
    void getAllGenresShouldReturnUnmodifiableList() {

        // Arrange
        GenreRepository repo = new GenreRepository(genreFactory);

        // Act
        List<Genre> result = repo.getAllGenres();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Genre.class)));
    }

    @Test
    void getAllGenresShouldReturnEmptyListWhenRepositoryIsEmpty() {

        // Arrange
        GenreRepository repo = new GenreRepository(genreFactory);

        // Act
        List<Genre> result = repo.getAllGenres();

        // Assert
        assertTrue(result.isEmpty());
    }
}