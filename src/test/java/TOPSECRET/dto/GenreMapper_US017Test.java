package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GenreMapper_US017} — US017.
 *
 * <p>Verifies that the mapper correctly converts {@link Genre} objects
 * into {@link GenreDTO_US017}, and that invalid input is rejected.
 */
class GenreMapper_US017Test {

    // ============================== toDTO(Genre) ==============================

    @Test
    void toDTO_shouldThrowWhenGenreIsNull() {
        GenreMapper_US017 mapper = new GenreMapper_US017();

        assertThrows(IllegalArgumentException.class,
                () -> mapper.toDTO(null));
    }

    @Test
    void toDTO_shouldConvertSingleGenreToDTO() {
        // Arrange
        Genre genre = mock(Genre.class);
        when(genre.getGenreType()).thenReturn("Fiction");
        GenreMapper_US017 mapper = new GenreMapper_US017();

        // Act
        GenreDTO_US017 result = mapper.toDTO(genre);

        // Assert
        assertEquals("Fiction", result.name());
    }

    // ============================== toDTOList(List<Genre>) ==============================

    @Test
    void toDTOList_shouldThrowWhenGenreListIsNull() {
        GenreMapper_US017 mapper = new GenreMapper_US017();

        assertThrows(IllegalArgumentException.class,
                () -> mapper.toDTOList(null));
    }

    @Test
    void toDTOList_shouldReturnEmptyListWhenInputIsEmpty() {
        // Arrange
        GenreMapper_US017 mapper = new GenreMapper_US017();

        // Act
        List<GenreDTO_US017> result = mapper.toDTOList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOList_shouldConvertEachGenreToDTO() {
        // Arrange
        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);
        when(genre1.getGenreType()).thenReturn("Fiction");
        when(genre2.getGenreType()).thenReturn("History");

        GenreMapper_US017 mapper = new GenreMapper_US017();

        // Act
        List<GenreDTO_US017> result = mapper.toDTOList(List.of(genre1, genre2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fiction", result.get(0).name());
        assertEquals("History", result.get(1).name());
    }
}
