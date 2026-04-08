package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US013GenreMapper}.
 *
 * <p>Verifies the behaviour of {@link US013GenreMapper#toDTO(List)},
 * covering both normal and edge cases.</p>
 */
class US013GenreMapperTest {

    // ========================================
    // TO DTO TESTS
    // ========================================

    @Test
    void toDTOShouldReturnEmptyListWhenInputIsEmpty() {

        // Arrange
        List<Genre> genres = List.of();
        US013GenreMapper mapper = new US013GenreMapper();

        // Act
        List<US013GenreDTO> result = mapper.toDTO(genres);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void toDTOShouldConvertEachGenreToDTO() {

        // Arrange
        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);

        when(genre1.getGenreType()).thenReturn("Fantasy");
        when(genre2.getGenreType()).thenReturn("Horror");

        List<Genre> genres = List.of(genre1, genre2);
        US013GenreMapper mapper = new US013GenreMapper();

        // Act
        List<US013GenreDTO> result = mapper.toDTO(genres);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).genreName());
        assertEquals("Horror", result.get(1).genreName());
    }
}
