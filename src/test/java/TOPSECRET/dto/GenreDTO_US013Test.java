package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Unit tests for {@link GenreDTO_US013}.
 *
 * <p>Verifies the behaviour of the constructor and the static factory method
 * {@code toDTO}, covering both the happy path and invalid-input scenarios.</p>
 */
class GenreDTO_US013Test {

    @Test
    void constructorShouldInitializeGenreName() {

        // Arrange
        String expectedGenreName = "Fantasy";

        // Act
        GenreDTO_US013 result = new GenreDTO_US013(expectedGenreName);

        // Assert
        assertEquals(expectedGenreName, result.genreName);
    }

    @Test
    void constructorShouldThrowExceptionWhenGenreNameIsNullOrBlank() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new GenreDTO_US013(null));
        assertThrows(IllegalArgumentException.class, () -> new GenreDTO_US013(""));
        assertThrows(IllegalArgumentException.class, () -> new GenreDTO_US013("  "));
    }

    @Test
    void toDTOShouldConvert() {

        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fantasy");

        // Act
        GenreDTO_US013 result = GenreDTO_US013.toDTO(genreDouble);

        // Assert
        assertEquals("Fantasy", result.genreName);
    }

    @Test
    void toDTOShouldThrowExceptionWhenGenreIsNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> GenreDTO_US013.toDTO(null));
    }
}
