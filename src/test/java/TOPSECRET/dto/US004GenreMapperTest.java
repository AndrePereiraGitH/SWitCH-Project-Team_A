package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreId;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US004GenreMapperTest {

    @Test
    void shouldMapGenreToDTO() {

        // ARRANGE
        Genre genre = mock(Genre.class);
        GenreId id = mock(GenreId.class);

        when(genre.id()).thenReturn(id);
        when(genre.getGenreType()).thenReturn("Fantasy");

        // ACT
        US004GenreDTO result = US004GenreMapper.toDTO(genre);

        // ASSERT
        assertEquals(id, result.id());
        assertEquals("Fantasy", result.title());
    }

    @Test
    void shouldConvertGenreListToDTO() {

        // ARRANGE
        Genre genre = mock(Genre.class);
        GenreId id = mock(GenreId.class);

        when(genre.id()).thenReturn(id);
        when(genre.getGenreType()).thenReturn("Fantasy");

        List<Genre> genres = List.of(genre);

        // ACT
        List<US004GenreDTO> result = US004GenreMapper.toDTOs(genres);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
        assertEquals("Fantasy", result.get(0).title());
    }

    @Test
    void shouldReturnEmptyListWhenInputIsEmpty() {

        // ARRANGE
        List<Genre> genres = List.of();

        // ACT
        List<US004GenreDTO> result = US004GenreMapper.toDTOs(genres);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertMultipleGenres() {

        // ARRANGE
        Genre genre1 = mock(Genre.class);
        GenreId id1 = mock(GenreId.class);
        when(genre1.id()).thenReturn(id1);
        when(genre1.getGenreType()).thenReturn("Fantasy");

        Genre genre2 = mock(Genre.class);
        GenreId id2 = mock(GenreId.class);
        when(genre2.id()).thenReturn(id2);
        when(genre2.getGenreType()).thenReturn("Horror");

        List<Genre> genres = List.of(genre1, genre2);

        // ACT
        List<US004GenreDTO> result = US004GenreMapper.toDTOs(genres);

        // ASSERT
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).title());
        assertEquals("Horror", result.get(1).title());
    }

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> US004GenreMapper.toDTO(null));
    }

    @Test
    void shouldReturnEmptyListWhenInputIsNull() {

        // ACT
        List<US004GenreDTO> result = US004GenreMapper.toDTOs(null);

        // ASSERT
        assertTrue(result.isEmpty());
    }
}