package TOPSECRET.repository;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreRepositoryMemTest {

    private final GenreFactory genreFactory = mock(GenreFactory.class);
    private final Genre genre = mock(Genre.class);

    @Test
    void testCreateAndAddGenre_AddsNewGenre() {
        // Arrange
        IGenreRepository genreRepository = new GenreRepositoryMem(genreFactory);

        String genreType = "Fantasy";
        when(genre.getGenreType()).thenReturn(genreType);
        when(genreFactory.create(genreType)).thenReturn(genre);


        // Act
        Genre result = genreRepository.createAndAdd(genreType);

        // Assert
        assertEquals(1, genreRepository.getAllGenres().size());
        assertEquals(genre, result);
    }

    @Test
    void testCreateAndAddGenre_ThrowsExceptionWhenGenreExists() {
        // Arrange
        String genreType = "Fantasy";
        when(genre.getGenreType()).thenReturn(genreType);
        when(genreFactory.create(genreType)).thenReturn(genre);
        when(genre.sameAs(genre)).thenReturn(true); // mock must confirm it's a duplicate

        IGenreRepository genreRepository = new GenreRepositoryMem(genreFactory);
        genreRepository.createAndAdd(genreType);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> genreRepository.createAndAdd(genreType));
    }

    @Test
    void getAllGenres_ReturnsCorrectList() {
        // Arrange
        Genre genre2 = mock(Genre.class);
        when(genre.getGenreType()).thenReturn("Fantasy");
        when(genre2.getGenreType()).thenReturn("Science Fiction");
        when(genreFactory.create("Fantasy")).thenReturn(genre);
        when(genreFactory.create("Science Fiction")).thenReturn(genre2);


        IGenreRepository genreRepository = new GenreRepositoryMem(genreFactory);
        genreRepository.createAndAdd("Fantasy");       // adiciona ao repo
        genreRepository.createAndAdd("Science Fiction");

        // Act
        List<Genre> result = genreRepository.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(genre));
        assertTrue(result.contains(genre2));
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Genre.class)));
    }

    @Test
    void getAllGenresShouldReturnUnmodifiableList() {

        // Arrange
        IGenreRepository repo = new GenreRepositoryMem(genreFactory);

        // Act
        List<Genre> result = repo.getAllGenres();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Genre.class)));
    }

    @Test
    void getAllGenresShouldReturnEmptyListWhenRepositoryIsEmpty() {

        // Arrange
        IGenreRepository repo = new GenreRepositoryMem(genreFactory);

        // Act
        List<Genre> result = repo.getAllGenres();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnGenreWhenTitleExists() {
        // Arrange
        String genreType = "Fantasy";
        when(genre.getGenreType()).thenReturn(genreType);
        when(genreFactory.create(genreType)).thenReturn(genre);

       IGenreRepository repository = new GenreRepositoryMem(genreFactory);
        repository.createAndAdd(genreType);

        // Act
        Genre result = repository.findByTitle("Fantasy");

        // Assert
        assertEquals(genre, result);
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {
        // Arrange
        IGenreRepository repository = new GenreRepositoryMem(genreFactory);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findByTitle("Unknown"));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        // Arrange
        IGenreRepository repository = new GenreRepositoryMem(genreFactory);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findByTitle(null));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsBlank() {
        // Arrange
        IGenreRepository repository = new GenreRepositoryMem(genreFactory);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findByTitle(" "));
    }

    @Test
    void shouldReturnGenreWhenIdExists() {

        // Arrange
        IGenreRepository repository = new GenreRepositoryMem(genreFactory);
        Genre genre = mock(Genre.class);
        GenreID genreId = mock(GenreID.class);

        when(genreFactory.create("Fantasy")).thenReturn(genre);
        when(genre.id()).thenReturn(genreId);

        repository.createAndAdd("Fantasy");

        // Act
        Genre result = repository.findById(genreId);

        // Assert
        assertNotNull(result);
        assertEquals(genre, result);
    }


    @Test
    void findGenreById_shouldThrowException_whenGenreIdIsNull() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem(genreFactory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            repo.findGenreById(null);
        });
    }

    @Test
    void findGenreById_shouldReturnGenre_whenGenreExists() {
        GenreID genreId = mock(GenreID.class);
        when(genre.id()).thenReturn(genreId);
        when(genreFactory.create("Fantasy")).thenReturn(genre);

        IGenreRepository repo = new GenreRepositoryMem(genreFactory);

        Genre createdGenre = repo.createAndAdd("Fantasy");

        Optional<Genre> result = repo.findGenreById(genreId);

        assertTrue(result.isPresent());
        assertEquals(genre, result.get());
        assertEquals(genre, createdGenre);
    }

    @Test
    void findGenreById_shouldReturnEmpty_whenGenreDoesNotExist() {
        IGenreRepository repo = new GenreRepositoryMem(genreFactory);

        GenreID genreId = mock(GenreID.class);

        Optional<Genre> result = repo.findGenreById(genreId);

        assertTrue(result.isEmpty());
    }
}
