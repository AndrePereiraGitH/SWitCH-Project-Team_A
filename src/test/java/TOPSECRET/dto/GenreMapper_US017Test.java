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
 * <p>Verifies that the mapper correctly converts a list of {@link Genre} objects
 * into a list of {@link GenreDTO_US017}, and that invalid input is rejected.
 */
class GenreMapper_US017Test {

    @Test
    void shouldThrowWhenGenreListIsNull() {
        GenreMapper_US017 mapper = new GenreMapper_US017();

        assertThrows(IllegalArgumentException.class,
                () -> mapper.toDTO(null));
    }

    @Test
    void shouldReturnEmptyListWhenInputIsEmpty() {
        GenreMapper_US017 mapper = new GenreMapper_US017();

        List<GenreDTO_US017> result = mapper.toDTO(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertEachGenreToDTO() {
        // Arrange
        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);
        when(genre1.getGenreType()).thenReturn("Fiction");
        when(genre2.getGenreType()).thenReturn("History");

        GenreMapper_US017 mapper = new GenreMapper_US017();

        // Act
        List<GenreDTO_US017> result = mapper.toDTO(List.of(genre1, genre2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fiction", result.get(0).name);
        assertEquals("History", result.get(1).name);
    }
}
