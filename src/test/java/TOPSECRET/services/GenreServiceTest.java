package TOPSECRET.services;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        assertThrows(NullPointerException.class,
                () -> new GenreService(null, genreFactory));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new GenreService(genreRepository, null));
    }

    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedGenre() {
        String genreType = "Fiction";
        Genre genre = mock(Genre.class);
        Genre savedGenre = mock(Genre.class);

        when(genreRepository.findByTitle(genreType))
                .thenThrow(new IllegalArgumentException("Genre not found."));
        when(genreFactory.create(genreType)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(savedGenre);

        Genre result = genreService.createAndSave(genreType);

        assertSame(savedGenre, result);
    }

    @Test
    void createAndSave_shouldThrowException_whenGenreAlreadyExists() {
        String genreType = "Fiction";
        Genre existingGenre = mock(Genre.class);

        when(genreRepository.findByTitle(genreType)).thenReturn(existingGenre);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> genreService.createAndSave(genreType)
        );

        assertEquals("Genre with type 'Fiction' already exists", exception.getMessage());
    }

    @Test
    void createAndSave_shouldPropagateException_whenRepositoryThrowsException() {
        String genreType = "Fiction";
        Genre genre = mock(Genre.class);

        when(genreRepository.findByTitle(genreType))
                .thenThrow(new IllegalArgumentException("Genre not found."));
        when(genreFactory.create(genreType)).thenReturn(genre);
        when(genreRepository.save(genre))
                .thenThrow(new IllegalArgumentException("Genre already exists"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> genreService.createAndSave(genreType)
        );

        assertEquals("Genre already exists", exception.getMessage());
    }

    // =========================================================================
    // getAllGenres
    // =========================================================================

    @Test
    void getAllGenres_shouldReturnListFromRepository() {
        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);
        when(genreRepository.getAllGenres()).thenReturn(List.of(genre1, genre2));

        List<Genre> result = genreService.getAllGenres();

        assertEquals(2, result.size());
        verify(genreRepository).getAllGenres();
    }

    // =========================================================================
    // findByTitle
    // =========================================================================

    @Test
    void findByTitle_shouldReturnGenre_whenFound() {
        Genre genre = mock(Genre.class);
        when(genreRepository.findByTitle("Fantasy")).thenReturn(genre);

        Genre result = genreService.findByTitle("Fantasy");

        assertSame(genre, result);
        verify(genreRepository).findByTitle("Fantasy");
    }

    @Test
    void findByTitle_shouldThrow_whenTitleIsNull() {
        assertThrows(NullPointerException.class,
                () -> genreService.findByTitle(null));
    }

    // =========================================================================
    // findGenreById
    // =========================================================================

    @Test
    void findGenreById_shouldReturnGenre_whenFound() {
        GenreId genreId = mock(GenreId.class);
        Genre genre = mock(Genre.class);
        when(genreRepository.findGenreById(genreId)).thenReturn(Optional.of(genre));

        Optional<Genre> result = genreService.findGenreById(genreId);

        assertTrue(result.isPresent());
        assertSame(genre, result.get());
    }

    @Test
    void findGenreById_shouldReturnEmpty_whenNotFound() {
        GenreId genreId = mock(GenreId.class);
        when(genreRepository.findGenreById(genreId)).thenReturn(Optional.empty());

        Optional<Genre> result = genreService.findGenreById(genreId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findGenreById_shouldThrow_whenIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> genreService.findGenreById(null));
    }
}