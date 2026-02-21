package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GenreDetailsDTO_US004.
 */
class GenreDetailsDTO_US004Test {

    /**
     * Verifies conversion from domain to DTO.
     */
    @Test
    void shouldConvertGenreToDTO() {

        // Arrange
        Genre genre = new Genre("Fantasy");
        GenreDetailsDTO_US004 dto = new GenreDetailsDTO_US004("temp");

        // Act
        GenreDetailsDTO_US004 result = dto.genreToDTO(genre);

        // Assert
        assertEquals("Fantasy", result.getTitle());
    }

    /**
     * Verifies list conversion.
     */
    @Test
    void shouldConvertGenreListToDTO() {

        // Arrange
        GenreDetailsDTO_US004 dto = new GenreDetailsDTO_US004("temp");

        // Act
        List<GenreDetailsDTO_US004> result =
                dto.genreListToDTO(List.of(new Genre("A"), new Genre("B")));

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Verifies conversion from DTO to domain.
     */
    @Test
    void shouldConvertDTOToGenre() {

        // Arrange
        GenreDetailsDTO_US004 dto = new GenreDetailsDTO_US004("Fantasy");

        // Act
        Genre genre = dto.dtoToGenre();

        // Assert
        assertEquals("Fantasy", genre.getTitle());
    }
}