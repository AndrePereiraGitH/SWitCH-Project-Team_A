package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GenreDTO_US017}.
 * SUT: GenreDTO_US017 — Genre is mocked.
 */
class GenreDTO_US017Test {

    /**
     * Ensures an exception is thrown when genre is null.
     */
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> GenreDTO_US017.toDTO(null));
    }

    /**
     * Ensures the DTO is created with the correct name when genre is valid.
     */
    @Test
    void shouldConvertGenreToDTO() {
        // Arrange
        Genre mockGenre = mock(Genre.class);
        when(mockGenre.getGenreType()).thenReturn("Fiction");

        // Act
        GenreDTO_US017 result = GenreDTO_US017.toDTO(mockGenre);

        // Assert
        assertNotNull(result);
        assertEquals("Fiction", result.name);
    }
}