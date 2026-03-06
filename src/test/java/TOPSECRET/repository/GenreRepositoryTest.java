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
    private final GenreRepository genreRepository = new GenreRepository(genres, genreFactory);


    @Test
    void getAllGenres_ReturnsCorrectList() {
        // Arrange
        Genre genre2 = mock(Genre.class);
        when(genre.getGenreType()).thenReturn("Fantasy");
        when(genre2.getGenreType()).thenReturn("Science Fiction");
        genres.add(genre);
        genres.add(genre2);

        // Act
        List<Genre> result = genreRepository.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(genre));
        assertTrue(result.contains(genre2));
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Genre.class)));
    }

    @Test
    void testCreateAndAddGenre_AddsNewGenre() {
        // Arrange
        String genreType = "Fantasy";
        when(genre.getGenreType()).thenReturn(genreType);
        when(genreFactory.create(genreType)).thenReturn(genre);

        // Act
        Genre result = genreRepository.createAndAddGenre(genreType);

        // Assert
        assertEquals(1, genres.size());
        assertEquals(genre, result);
        assertTrue(genres.contains(genre));
    }

    @Test
    void testCreateAndAddGenre_ThrowsExceptionWhenGenreExists() {
        // Arrange
        String genreType = "Fantasy";
        when(genre.getGenreType()).thenReturn(genreType);
        genres.add(genre);
        when(genreFactory.create(genreType)).thenReturn(genre);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> genreRepository.createAndAddGenre(genreType));
        assertEquals("Genre already exists", exception.getMessage());
    }
}