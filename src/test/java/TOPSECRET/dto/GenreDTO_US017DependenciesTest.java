package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link GenreDTO_US017}.
 * SUT: GenreDTO_US017 — uses real Genre objects.
 */
class GenreDTO_US017DependenciesTest {

    /**
     * Ensures the DTO is created with the correct name when genre is valid.
     */
    @Test
    void shouldConvertGenreToDTO() {
        // Arrange
        Genre genre = new Genre("Fiction");

        // Act
        GenreDTO_US017 dto = GenreDTO_US017.toDTO(genre);

        // Assert
        assertNotNull(dto);
        assertEquals("Fiction", dto.name);
    }

    /**
     * Ensures an exception is thrown when genre is null.
     */
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> GenreDTO_US017.toDTO(null));
    }
}