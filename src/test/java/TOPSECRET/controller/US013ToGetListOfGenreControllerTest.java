package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.dto.GenreDTO_US013;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US013ToGetListOfGenreController}.
 *
 * <p>{@link GenreRepository} is mocked to isolate the controller's own logic completely.
 *
 * <p><b>What is tested here (controller logic only):</b>
 * <ul>
 *   <li>Constructor — null repository</li>
 *   <li>{@link US013ToGetListOfGenreController#getGenresList} — delegates to repository,
 *       maps each {@link Genre} to a {@link GenreDTO_US013}, returns empty list when no genres exist</li>
 * </ul>
 */
class US013ToGetListOfGenreControllerTest {

    GenreRepository genreRepository = mock(GenreRepository.class);

    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================


    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new US013ToGetListOfGenreController(null)
        );
        assertEquals("Genre Repository can't be null", exception.getMessage());
    }

    // ========================================
    // GET GENRES LIST TESTS
    // ========================================

    @Test
    void shouldReturnEmptyListWhenNoGenres() {
        // Arrange
        when(genreRepository.getAllGenres()).thenReturn(List.of());
        US013ToGetListOfGenreController controller = new US013ToGetListOfGenreController(genreRepository);

        // Act
        List<GenreDTO_US013> result = controller.getGenresList();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnDTOForEachGenreFromRepository() {
        // Arrange
        Genre mockGenre1 = mock(Genre.class);
        Genre mockGenre2 = mock(Genre.class);
        when(mockGenre1.getGenreType()).thenReturn("Romance");
        when(mockGenre2.getGenreType()).thenReturn("Horror");
        when(genreRepository.getAllGenres()).thenReturn(List.of(mockGenre1, mockGenre2));
        US013ToGetListOfGenreController controller = new US013ToGetListOfGenreController(genreRepository);

        // Act
        List<GenreDTO_US013> result = controller.getGenresList();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Romance", result.get(0).genreName);
        assertEquals("Horror", result.get(1).genreName);
    }

    @Test
    void shouldDelegateToRepositoryExactlyOnce() {
        // Arrange
        when(genreRepository.getAllGenres()).thenReturn(List.of());
        US013ToGetListOfGenreController controller = new US013ToGetListOfGenreController(genreRepository);

        // Act
        controller.getGenresList();

        // Assert
        verify(genreRepository, times(1)).getAllGenres();
    }
}