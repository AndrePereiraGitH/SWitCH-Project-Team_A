package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreRepositoryMemTest {

    @Test
    void findAll_ReturnsCorrectList() {
        // Arrange
        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);

        IGenreRepository genreRepository = new GenreRepositoryMem();
        genreRepository.save(genre2Double);
        genreRepository.save(genre1Double);

        // Act
        List<Genre> result = genreRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(genre2Double));
        assertTrue(result.contains(genre1Double));
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Genre.class)));
    }

    @Test
    void findAllShouldReturnUnmodifiableList() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem();

        // Act
        List<Genre> result = repo.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Genre.class)));
    }

    @Test
    void findAllShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem();

        // Act
        List<Genre> result = repo.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnGenreWhenTitleExists() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fantasy");

        IGenreRepository repository = new GenreRepositoryMem();
        repository.save(genreDouble);

        // Act
        Optional<Genre> result = repository.findByTitle("Fantasy");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(genreDouble, result.get());
    }

    @Test
    void shouldReturnEmptyWhenGenreNotFound() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fantasy");

        IGenreRepository repository = new GenreRepositoryMem();
        repository.save(genreDouble);

        // Act
        Optional<Genre> result = repository.findByTitle("Unknown");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenTitleIsNull() {
        // Arrange
        IGenreRepository repository = new GenreRepositoryMem();

        // Act
        Optional<Genre> result = repository.findByTitle(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenTitleIsBlank() {
        // Arrange
        IGenreRepository repository = new GenreRepositoryMem();

        // Act
        Optional<Genre> result = repository.findByTitle(" ");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findGenreById_shouldThrowException_whenIdIsNull() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findById(null));
    }

    @Test
    void findGenreById_shouldReturnGenre_whenExists() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreDouble.id()).thenReturn(genreIdDouble);

        IGenreRepository repo = new GenreRepositoryMem();
        Genre saved = repo.save(genreDouble);

        // Act
        Optional<Genre> result = repo.findById(genreIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(saved, result.get());
    }

    @Test
    void findGenreById_shouldReturnEmpty_whenDoesNotExist() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem();
        GenreId genreIdDouble = mock(GenreId.class);

        // Act
        Optional<Genre> result = repo.findById(genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findGenreById_shouldReturnCorrect_whenMultipleGenresExist() {
        // Arrange
        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);
        GenreId genreId1Double = mock(GenreId.class);
        GenreId genreId2Double = mock(GenreId.class);

        when(genre1Double.id()).thenReturn(genreId1Double);
        when(genre2Double.id()).thenReturn(genreId2Double);

        IGenreRepository repo = new GenreRepositoryMem();
        repo.save(genre1Double);
        repo.save(genre2Double);

        // Act
        Optional<Genre> result = repo.findById(genreId2Double);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(genre2Double, result.get());
    }

    @Test
    void save_shouldAddGenreAndReturnIt() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        IGenreRepository repo = new GenreRepositoryMem();

        // Act
        Genre result = repo.save(genreDouble);

        // Assert
        assertEquals(genreDouble, result);
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void save_shouldThrowException_whenGenreAlreadyExists() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        IGenreRepository repo = new GenreRepositoryMem();
        repo.save(genreDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(genreDouble));
    }

    @Test
    void save_shouldThrowException_whenGenreIsNull() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.save(null));
    }

    @Test
    void defaultConstructor_shouldCreateRepositoryWithDefaultFactory() {
        // Arrange & Act
        GenreRepositoryMem repo = new GenreRepositoryMem();

        // Assert
        assertNotNull(repo);
        assertTrue(repo.findAll().isEmpty());
    }
}