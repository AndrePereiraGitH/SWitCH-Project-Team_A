package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.GenreId;

import TOPSECRET.domain.Genre.Genre;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US022GenreMapperTest {

    @Test
    void shouldReturnEmptyListWhenInputIsEmpty() {

        // Arrange
        List<Genre> genres = List.of();

        // Act
        List<US022GenreDTO> result =
                US022GenreMapper.toDTOList(genres);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertSingleGenre() {

        // Arrange
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreDouble.id()).thenReturn(genreIdDouble);
        when(genreDouble.getGenreType()).thenReturn("Comedy");

        List<Genre> genres = List.of(genreDouble);

        // Act
        List<US022GenreDTO> result =
                US022GenreMapper.toDTOList(genres);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Comedy", result.get(0).name());
    }

    @Test
    void shouldConvertMultipleGenres() {

        // Arrange
        Genre genreDouble1 = mock(Genre.class);
        GenreId genreIdDouble1 = mock(GenreId.class);
        when(genreDouble1.id()).thenReturn(genreIdDouble1);
        Genre genreDouble2 = mock(Genre.class);
        GenreId genreIdDouble2 = mock(GenreId.class);
        when(genreDouble2.id()).thenReturn(genreIdDouble2);

        when(genreDouble1.getGenreType()).thenReturn("Fantasy");
        when(genreDouble2.getGenreType()).thenReturn("Romance");

        List<Genre> genres = List.of(genreDouble1, genreDouble2);

        // Act
        List<US022GenreDTO> result =
                US022GenreMapper.toDTOList(genres);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).name());
        assertEquals("Romance", result.get(1).name());
    }

    @Test
    void shouldPreserveOrderOfGenres() {

        // Arrange
        Genre genreDouble1 = mock(Genre.class);
        GenreId genreIdDouble1 = mock(GenreId.class);
        when(genreDouble1.id()).thenReturn(genreIdDouble1);
        Genre genreDouble2 = mock(Genre.class);
        GenreId genreIdDouble2 = mock(GenreId.class);
        when(genreDouble2.id()).thenReturn(genreIdDouble2);

        when(genreDouble1.getGenreType()).thenReturn("A");
        when(genreDouble2.getGenreType()).thenReturn("B");

        List<Genre> genres = List.of(genreDouble1, genreDouble2);

        // Act
        List<US022GenreDTO> result =
                US022GenreMapper.toDTOList(genres);

        // Assert
        assertEquals("A", result.get(0).name());
        assertEquals("B", result.get(1).name());
    }

    @Test
    void shouldMapGenreTypeCorrectly() {

        // Arrange
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreDouble.id()).thenReturn(genreIdDouble);
        when(genreDouble.getGenreType()).thenReturn("Sci-Fi");


        List<Genre> genres = List.of(genreDouble);

        // Act
        List<US022GenreDTO> result =
                US022GenreMapper.toDTOList(genres);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Sci-Fi", result.get(0).name());
    }

    @Test
    void shouldThrowExceptionWhenInputIsNull() {

        // Arrange
        List<Genre> genres = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                US022GenreMapper.toDTOList(genres)
        );
    }
}

