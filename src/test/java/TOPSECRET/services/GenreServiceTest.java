package TOPSECRET.services;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new GenreService(null, genreFactory));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        // Act & Assert
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
        Genre genre = mock(Genre.class);
        Genre savedGenre = mock(Genre.class);

        when(genreRepository.findByTitle(genreType)).thenReturn(Optional.empty());
        when(genreFactory.create(genreType)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(savedGenre);

        // Act
        Genre result = genreService.createAndSave(genreType);

        // Assert
        assertSame(savedGenre, result);
    }

    @Test
    void createAndSave_shouldThrowException_whenGenreAlreadyExists() {
        // Arrange
        String genreType = "Fiction";
        Genre existingGenre = mock(Genre.class);

        when(genreRepository.findByTitle(genreType)).thenReturn(Optional.of(existingGenre));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> genreService.createAndSave(genreType)
        );

        assertEquals("Genre with type 'Fiction' already exists", exception.getMessage());
    }

    @Test
    void createAndSave_shouldPropagateException_whenRepositoryThrowsException() {
        // Arrange
        String genreType = "Fiction";
        Genre genre = mock(Genre.class);

        when(genreRepository.findByTitle(genreType)).thenReturn(Optional.empty());
        when(genreFactory.create(genreType)).thenReturn(genre);
        when(genreRepository.save(genre))
                .thenThrow(new IllegalArgumentException("Genre already exists"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> genreService.createAndSave(genreType)
        );

        assertEquals("Genre already exists", exception.getMessage());
    }
}