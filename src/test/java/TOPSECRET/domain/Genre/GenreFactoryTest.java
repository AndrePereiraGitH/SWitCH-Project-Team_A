package TOPSECRET.domain.Genre;

import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
/**
 * Unit tests for {@link GenreFactory}.
 * Verifies that the factory correctly creates Genre instances
 * and throws exceptions for invalid inputs.
 */
class GenreFactoryTest {

    private final GenreFactory genreFactory = new GenreFactory(); //Sempre que chamo genreFactory crio um novo objeto


    @Test
    void ensureFactoryCreatesGenreSuccessfully() {
        // Arrange
        String genreType = "Ergodic literature";

        // Act
        Genre genre = genreFactory.create(genreType);

        // Assert
        assertNotNull(genre);
    }


    @Test
    void ensureFactoryCreatesGenreWithCorrectType() {
        // Arrange
        String genreType = "Ergodic literature";

        // Act
        Genre genre = genreFactory.create(genreType);

        // Assert
        assertEquals(genreType, genre.getGenreType());
    }

    @Test
    void ensureFactoryThrowsExceptionWithNullGenreType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> genreFactory.create(null));
    }

    @Test
    void ensureFactoryThrowsExceptionWithBlankGenreType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> genreFactory.create("   "));
    }

    // =========================================================================
// create(GenreId, String) - reconstruction from DB
// =========================================================================

    @Test
    void ensureReconstructReturnsGenreSuccessfully() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        String genreType = "Ergodic literature";

        // Act
        Genre genre = genreFactory.create(genreId, genreType);

        // Assert
        assertNotNull(genre);
    }

    @Test
    void ensureReconstructReturnsGenreWithCorrectType() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        String genreType = "Ergodic literature";

        // Act
        Genre genre = genreFactory.create(genreId, genreType);

        // Assert
        assertEquals(genreType, genre.getGenreType());
    }

    @Test
    void ensureReconstructThrowsExceptionWithNullGenreType() {
        GenreId genreId = mock(GenreId.class);
        assertThrows(IllegalArgumentException.class, () -> genreFactory.create(genreId, null));
    }

    @Test
    void ensureReconstructThrowsExceptionWithBlankGenreType() {
        GenreId genreId = mock(GenreId.class);
        assertThrows(IllegalArgumentException.class, () -> genreFactory.create(genreId, null));
    }


    @Test
    void ensureReconstructAndCreateReturnEqualGenres() {
        // Arrange
        String genreType = "Ergodic literature";
        GenreId genreId = mock(GenreId.class);

        // Act
        Genre reconstructed = genreFactory.create(genreId, genreType);

        // Assert
        assertNotNull(reconstructed);
        assertEquals(genreType, reconstructed.getGenreType());
    }
}