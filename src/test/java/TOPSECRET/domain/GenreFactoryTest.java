package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenreFactory}.
 * Verifies that the factory correctly creates Genre instances
 * and throws exceptions for invalid inputs.
 */
class GenreFactoryTest {
    private GenreFactory genreFactory;

    @BeforeEach
    void setUp() {
        genreFactory = new GenreFactory();
    }

    @Test
    void ensureFactoryCreatesGenreSuccessfully() {
        // Arrange
        String genreType = "Ergodic literature";

        // Act
        Genre genre = genreFactory.createGenre(genreType);

        // Assert
        assertNotNull(genre);
    }


    @Test
    void ensureFactoryCreatesGenreWithCorrectType() {
        // Arrange
        String genreType = "Ergodic literature";

        // Act
        Genre genre = genreFactory.createGenre(genreType);

        // Assert
        assertEquals(genreType, genre.getGenreType());
    }

    @Test
    void ensureFactoryThrowsExceptionWithNullGenreType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> genreFactory.createGenre(null));
    }

    @Test
    void ensureFactoryThrowsExceptionWithBlankGenreType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> genreFactory.createGenre("   "));
    }

}