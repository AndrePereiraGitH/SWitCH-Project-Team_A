package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US021GenreMapper}.
 */
class US021GenreMapperTest {

    // =====================
    // toDTO() tests
    // =====================

    @Test
    void shouldThrowNullPointerExceptionWhenGenreIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                US021GenreMapper.toDTO(null));
    }

    @Test
    void shouldConvertGenreToDTO() {
        // Arrange
        GenreId genreId = new GenreId("drama");
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);
        when(genre.getGenreType()).thenReturn("Drama");

        // Act
        US021GenreDTO result = US021GenreMapper.toDTO(genre);

        // Assert
        assertEquals(genreId, result.genreId());
        assertEquals("Drama", result.name());
    }

    // =====================
    // toDTOList() tests
    // =====================

    @Test
    void shouldThrowIllegalArgumentExceptionWhenGenreListIsNull() {
        // Arrange - no setup needed

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                US021GenreMapper.toDTOList(null));
    }

    @Test
    void shouldReturnEmptyListWhenGenreListIsEmpty() {
        // Arrange
        List<Genre> emptyList = new ArrayList<>();

        // Act
        List<US021GenreDTO> result = US021GenreMapper.toDTOList(emptyList);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertSingleGenreToListOfOneDTO() {
        // Arrange
        GenreId genreId = new GenreId("drama");
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);
        when(genre.getGenreType()).thenReturn("Drama");

        // Act
        List<US021GenreDTO> result = US021GenreMapper.toDTOList(List.of(genre));

        // Assert
        assertEquals(1, result.size());
        assertEquals(genreId, result.get(0).genreId());
        assertEquals("Drama", result.get(0).name());
    }

    @Test
    void shouldConvertMultipleGenresToListOfDTOs() {
        // Arrange
        GenreId genreId1 = new GenreId("drama");
        Genre genre1 = mock(Genre.class);
        when(genre1.id()).thenReturn(genreId1);
        when(genre1.getGenreType()).thenReturn("Drama");

        GenreId genreId2 = new GenreId("fiction");
        Genre genre2 = mock(Genre.class);
        when(genre2.id()).thenReturn(genreId2);
        when(genre2.getGenreType()).thenReturn("Fiction");

        // Act
        List<US021GenreDTO> result = US021GenreMapper.toDTOList(List.of(genre1, genre2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Drama", result.get(0).name());
        assertEquals("Fiction", result.get(1).name());
    }

    @Test
    void shouldMaintainOrderWhenConvertingList() {
        // Arrange
        GenreId genreId1 = new GenreId("drama");
        Genre genre1 = mock(Genre.class);
        when(genre1.id()).thenReturn(genreId1);
        when(genre1.getGenreType()).thenReturn("Drama");

        GenreId genreId2 = new GenreId("fiction");
        Genre genre2 = mock(Genre.class);
        when(genre2.id()).thenReturn(genreId2);
        when(genre2.getGenreType()).thenReturn("Fiction");

        GenreId genreId3 = new GenreId("horror");
        Genre genre3 = mock(Genre.class);
        when(genre3.id()).thenReturn(genreId3);
        when(genre3.getGenreType()).thenReturn("Horror");

        // Act
        List<US021GenreDTO> result = US021GenreMapper.toDTOList(List.of(genre1, genre2, genre3));

        // Assert
        assertEquals("Drama", result.get(0).name());
        assertEquals("Fiction", result.get(1).name());
        assertEquals("Horror", result.get(2).name());
    }
}
