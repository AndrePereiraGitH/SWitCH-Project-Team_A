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
        US022GenreMapper mapper = new US022GenreMapper();

        List<Genre> genres = List.of();

        // Act
        List<US022GenreDTO> result =
                mapper.toDTOList(genres);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertSingleGenre() {

        // Arrange
        US022GenreMapper mapper = new US022GenreMapper();

        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreDouble.id()).thenReturn(genreIdDouble);
        when(genreDouble.getGenreType()).thenReturn("Comedy");

        List<Genre> genres = List.of(genreDouble);

        // Act
        List<US022GenreDTO> result =
                mapper.toDTOList(genres);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Comedy", result.get(0).genreType());
    }

    @Test
    void shouldConvertMultipleGenres() {

        // Arrange
        US022GenreMapper mapper = new US022GenreMapper();

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
                mapper.toDTOList(genres);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).genreType());
        assertEquals("Romance", result.get(1).genreType());
    }

    @Test
    void shouldPreserveOrderOfGenres() {

        // Arrange
        US022GenreMapper mapper = new US022GenreMapper();

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
                mapper.toDTOList(genres);

        // Assert
        assertEquals("A", result.get(0).genreType());
        assertEquals("B", result.get(1).genreType());
    }

    @Test
    void shouldMapGenreTypeCorrectly() {

        // Arrange
        US022GenreMapper mapper = new US022GenreMapper();

        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreDouble.id()).thenReturn(genreIdDouble);
        when(genreDouble.getGenreType()).thenReturn("Sci-Fi");


        List<Genre> genres = List.of(genreDouble);

        // Act
        List<US022GenreDTO> result =
                mapper.toDTOList(genres);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Sci-Fi", result.get(0).genreType());
    }

    @Test
    void shouldThrowExceptionWhenInputIsNull() {

        // Arrange
        US022GenreMapper mapper = new US022GenreMapper();

        List<Genre> genres = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                mapper.toDTOList(genres)
        );

    }
}

