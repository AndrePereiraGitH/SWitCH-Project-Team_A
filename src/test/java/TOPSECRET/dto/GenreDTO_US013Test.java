package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreDTO_US013Test {
    @Test
    void testGenreDTOCreation_ValidGenre() {
        // Arrange
        Genre genre = new Genre("Adventure");

        // Act
        GenreDTO_US013 dto = new GenreDTO_US013(genre);

        // Assert
        assertEquals("Adventure", dto.genreName);
    }

    @Test
    void testGenreDTOCreation_PreservesGenreType() {
        // Arrange
        Genre genre = new Genre("Fantasy");

        // Act
        GenreDTO_US013 dto = new GenreDTO_US013(genre);

        // Assert
        assertEquals(genre.getTitle(), dto.genreName);
    }

}