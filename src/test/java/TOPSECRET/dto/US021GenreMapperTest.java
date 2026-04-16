package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US021GenreMapperTest {

    // =====================
    // toDTO() tests
    // =====================

    @Test
    void shouldThrowNullPointerExceptionWhenGenreIsNull() {
        assertThrows(NullPointerException.class, () ->
                US021GenreMapper.toDTO(null));
    }

    @Test
    void shouldConvertGenreToDTO() {
        GenreID genreId = new GenreID("drama");
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);
        when(genre.getGenreType()).thenReturn("Drama");

        US021GenreDTO result = US021GenreMapper.toDTO(genre);

        assertEquals(genreId, result.genreId());
        assertEquals("Drama", result.name());
    }

    // =====================
    // toDTOList() tests
    // =====================

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGenreListIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                US021GenreMapper.toDTOList(null));
    }

    @Test
    void shouldReturnEmptyListWhenGenreListIsEmpty() {
        List<US021GenreDTO> result = US021GenreMapper.toDTOList(new ArrayList<>());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertSingleGenreToListOfOneDTO() {
        GenreID genreId = new GenreID("drama");
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);
        when(genre.getGenreType()).thenReturn("Drama");

        List<US021GenreDTO> result = US021GenreMapper.toDTOList(List.of(genre));

        assertEquals(1, result.size());
        assertEquals(genreId, result.get(0).genreId());
        assertEquals("Drama", result.get(0).name());
    }

    @Test
    void shouldConvertMultipleGenresToListOfDTOs() {
        GenreID genreId1 = new GenreID("drama");
        Genre genre1 = mock(Genre.class);
        when(genre1.id()).thenReturn(genreId1);
        when(genre1.getGenreType()).thenReturn("Drama");

        GenreID genreId2 = new GenreID("fiction");
        Genre genre2 = mock(Genre.class);
        when(genre2.id()).thenReturn(genreId2);
        when(genre2.getGenreType()).thenReturn("Fiction");

        List<US021GenreDTO> result = US021GenreMapper.toDTOList(List.of(genre1, genre2));

        assertEquals(2, result.size());
        assertEquals("Drama", result.get(0).name());
        assertEquals("Fiction", result.get(1).name());
    }

    @Test
    void shouldMaintainOrderWhenConvertingList() {
        GenreID genreId1 = new GenreID("drama");
        Genre genre1 = mock(Genre.class);
        when(genre1.id()).thenReturn(genreId1);
        when(genre1.getGenreType()).thenReturn("Drama");

        GenreID genreId2 = new GenreID("fiction");
        Genre genre2 = mock(Genre.class);
        when(genre2.id()).thenReturn(genreId2);
        when(genre2.getGenreType()).thenReturn("Fiction");

        GenreID genreId3 = new GenreID("horror");
        Genre genre3 = mock(Genre.class);
        when(genre3.id()).thenReturn(genreId3);
        when(genre3.getGenreType()).thenReturn("Horror");

        List<US021GenreDTO> result = US021GenreMapper.toDTOList(List.of(genre1, genre2, genre3));

        assertEquals("Drama", result.get(0).name());
        assertEquals("Fiction", result.get(1).name());
        assertEquals("Horror", result.get(2).name());
    }
}
