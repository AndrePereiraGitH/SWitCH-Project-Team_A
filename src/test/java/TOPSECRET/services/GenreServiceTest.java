package TOPSECRET.services;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GenreService}.
 *
 * <p>Verifies the service's behaviour in isolation by mocking both the
 * repository ({@link IGenreRepository}) and the factory ({@link GenreFactory}).
 * Tests are grouped by method under test.</p>
 *
 * <p>Each test follows the Arrange / Act / Assert structure and exercises
 * a single logical scenario.</p>
 */
@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private IGenreRepository genreRepository;

    @Mock
    private GenreFactory genreFactory;

    @InjectMocks
    private GenreService genreService;


    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new GenreService(null, genreFactory));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new GenreService(genreRepository, null));
    }


    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedGenre() {
        // Arrange
        String genreType = "Fiction";
        Genre savedGenre = mock(Genre.class);

        when(genreRepository.createAndAdd(genreType))
                .thenReturn(savedGenre);

        // Act
        Genre result = genreService.createAndSave(genreType);

        // Assert
        assertSame(savedGenre, result);
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenGenreTypeIsNull() {
        // Act + Assert
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> genreService.createAndSave(null)
        );

        assertEquals("GenreType cannot be null", exception.getMessage());
    }

    @Test
    void createAndSave_shouldPropagateException_whenRepositoryThrowsException() {
        // Arrange
        String genreType = "Fiction";

        when(genreRepository.createAndAdd(genreType))
                .thenThrow(new IllegalArgumentException("Genre already exists"));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> genreService.createAndSave(genreType)
        );

        assertEquals("Genre already exists", exception.getMessage());
    }
}