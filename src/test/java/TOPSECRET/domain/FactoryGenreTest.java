package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryGenreTest {
    private GenreFactory factory;

    @BeforeEach
    void setUp() {
        factory = new GenreFactory();
    }

    @Test
    void ensureFactoryCreatesGenreSuccessfully() {
        // Arrange
        String genreType = "Rock";

        // Act
        Genre genre = factory.newGenre(genreType);

        // Assert
        assertNotNull(genre);
    }


    @Test
    void ensureFactoryCreatesGenreWithCorrectType() {
        // Arrange
        String genreType = "Rock";

        // Act
        Genre genre = factory.newGenre(genreType);

        // Assert
        assertEquals(genreType, genre.getGenreType());
    }

    @Test
    void ensureFactoryThrowsExceptionWithNullGenreType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.newGenre(null));
    }

    @Test
    void ensureFactoryThrowsExceptionWithBlankGenreType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.newGenre("   "));
    }

}