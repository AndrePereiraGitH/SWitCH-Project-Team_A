package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreID;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US004GenreMapperTest {

    @Test
    void shouldMapGenreToDTO() {

        // ARRANGE
        Genre genre = mock(Genre.class);
        GenreID id = mock(GenreID.class);

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
        GenreID id = mock(GenreID.class);

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
        GenreID id1 = mock(GenreID.class);
        when(genre1.id()).thenReturn(id1);
        when(genre1.getGenreType()).thenReturn("Fantasy");

        Genre genre2 = mock(Genre.class);
        GenreID id2 = mock(GenreID.class);
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
    void shouldExtractGenreIDsFromDTOs() {

        // ARRANGE
        GenreID id1 = mock(GenreID.class);
        GenreID id2 = mock(GenreID.class);

        US004GenreDTO dto1 = new US004GenreDTO(id1, "Fantasy");
        US004GenreDTO dto2 = new US004GenreDTO(id2, "Horror");

        // ACT
        List<GenreID> result = US004GenreMapper.getIds(List.of(dto1, dto2));

        // ASSERT
        assertEquals(2, result.size());
        assertEquals(id1, result.get(0));
        assertEquals(id2, result.get(1));
    }
}