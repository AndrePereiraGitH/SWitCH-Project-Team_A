package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreID;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GenreMapper.
 *
 * <p>
 * Ensures correct conversion from Genre domain objects to DTOs.
 * </p>
 */
class US004GenreMapperTest {

    @Test
    void shouldMapGenreToDTO() {

        // ARRANGE
        US004GenreMapper mapper = new US004GenreMapper();

        Genre genre = mock(Genre.class);
        GenreID id = mock(GenreID.class);

        when(genre.id()).thenReturn(id);
        when(genre.getGenreType()).thenReturn("Fantasy");

        // ACT
        List<US004GenreDTO> result = mapper.toDTO(List.of(genre));

        // ASSERT
        assertEquals(id, result.get(0).getId());
        assertEquals("Fantasy", result.get(0).getTitle());
    }

    @Test
    void shouldConvertGenreListToDTO() {

        // ARRANGE
        US004GenreMapper mapper = new US004GenreMapper();

        Genre genre = mock(Genre.class);
        GenreID id = mock(GenreID.class);

        when(genre.id()).thenReturn(id);
        when(genre.getGenreType()).thenReturn("Fantasy");

        List<Genre> genres = List.of(genre);

        // ACT
        List<US004GenreDTO> result = mapper.toDTO(genres);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals("Fantasy", result.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenInputIsEmpty() {

        // ARRANGE
        US004GenreMapper mapper = new US004GenreMapper();

        List<Genre> genres = List.of();

        // ACT
        List<US004GenreDTO> result = mapper.toDTO(genres);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertMultipleGenres() {

        // ARRANGE
        US004GenreMapper mapper = new US004GenreMapper();

        Genre genre1 = mock(Genre.class);
        GenreID id1 = mock(GenreID.class);
        when(genre1.id()).thenReturn(id1);
        when(genre1.getGenreType()).thenReturn("Fantasy");

        Genre genre2 = mock(Genre.class);
        GenreID id2 = mock(GenreID.class);
        when(genre2.id()).thenReturn(id2);
        when(genre2.getGenreType()).thenReturn("Horror");

        List<Genre> genres = List.of(genre1, genre2);

        // ACT
        List<US004GenreDTO> result = mapper.toDTO(genres);

        // ASSERT
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).getTitle());
        assertEquals("Horror", result.get(1).getTitle());
    }
}