package TOPSECRET.controller;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.dto.US013GenreDTO;
import TOPSECRET.dto.US013GenreMapper;
import TOPSECRET.repository.GenreRepositoryMem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US013ToGetListOfGenreController}.
 *
 * <p>{@link GenreRepositoryMem} and {@link US013GenreMapper} are replaced by test doubles
 * to isolate the controller's own logic completely.
 *
 * <p><b>What is tested here (controller logic only):</b>
 * <ul>
 *   <li>Constructor — null repository</li>
 *   <li>Constructor — null mapper</li>
 *   <li>{@link US013ToGetListOfGenreController#getGenresList()} — delegates to repository
 *       and mapper, returns the mapper result, and handles empty lists correctly</li>
 * </ul>
 */
class US013ToGetListOfGenreControllerTest {

    GenreRepositoryMem genreRepository = mock(GenreRepositoryMem.class);
    US013GenreMapper mapper = mock(US013GenreMapper.class);


    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================


    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new US013ToGetListOfGenreController(null, mapper)
        );

        assertEquals("Genre Repository can't be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMapperIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new US013ToGetListOfGenreController(genreRepository, null)
        );

        assertEquals("GenreMapper can't be null", exception.getMessage());
    }


    // ========================================
    // GET GENRES LIST TESTS
    // ========================================
    @Test
    void shouldReturnEmptyListWhenNoGenres() {
        // Arrange
        List<Genre> allGenreList = List.of();
        List<US013GenreDTO> expected = List.of();

        when(genreRepository.getAllGenres()).thenReturn(allGenreList);

        when(mapper.toDTO(allGenreList)).thenReturn(expected);

        US013ToGetListOfGenreController controller =
                new US013ToGetListOfGenreController(genreRepository, mapper);

        // Act
        List<US013GenreDTO> result = controller.getGenresList();

        // Assert
        assertEquals(0, result.size());
        assertSame(expected, result);
    }

    @Test
    void shouldReturnMapperResultForGenresFromRepository() {
        // Arrange
        Genre mockGenre1 = mock(Genre.class);
        Genre mockGenre2 = mock(Genre.class);

        List<Genre> allGenreList = List.of(mockGenre1, mockGenre2);
        US013GenreDTO dto1 = mock(US013GenreDTO.class);
        US013GenreDTO dto2 = mock(US013GenreDTO.class);

        List<US013GenreDTO> expected = List.of(dto1, dto2);

        when(genreRepository.getAllGenres()).thenReturn(allGenreList);
        when(mapper.toDTO(allGenreList)).thenReturn(expected);

        US013ToGetListOfGenreController controller =
                new US013ToGetListOfGenreController(genreRepository, mapper);

        // Act
        List<US013GenreDTO> result = controller.getGenresList();

        // Assert
        assertEquals(2, result.size());
        assertSame(expected, result);
    }

    @Test
    void shouldDelegateToRepositoryExactlyOnce() {
        // Arrange
        List<Genre> allGenreList = List.of();

        when(genreRepository.getAllGenres()).thenReturn(allGenreList);
        when(mapper.toDTO(allGenreList)).thenReturn(List.of());

        US013ToGetListOfGenreController controller =
                new US013ToGetListOfGenreController(genreRepository, mapper);

        // Act
        controller.getGenresList();

        // Assert
        verify(genreRepository, times(1)).getAllGenres();
    }

    @Test
    void shouldDelegateToMapperExactlyOnceWithRepositoryResult() {
        // Arrange
        List<Genre> allGenreList = List.of();

        when(genreRepository.getAllGenres()).thenReturn(allGenreList);
        when(mapper.toDTO(allGenreList)).thenReturn(List.of());

        US013ToGetListOfGenreController controller =
                new US013ToGetListOfGenreController(genreRepository, mapper);

        // Act
        controller.getGenresList();

        // Assert
        verify(mapper, times(1)).toDTO(allGenreList);
    }
}