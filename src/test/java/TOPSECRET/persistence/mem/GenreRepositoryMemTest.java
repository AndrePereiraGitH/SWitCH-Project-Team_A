package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GenreRepositoryMemTest {

// TODO: re-enable when createAndAdd is re-introduced or US003 is refactored
//    @Test
//    void testCreateAndAddGenre_AddsNewGenre() { ... }
//    @Test
//    void testCreateAndAddGenre_ThrowsExceptionWhenGenreExists() { ... }

    @Test
    void getAllGenres_ReturnsCorrectList() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        GenreRepositoryMem genreRepository = new GenreRepositoryMem();
        Genre g1 = realFactory.create("Fantasy");
        Genre g2 = realFactory.create("Science Fiction");
        genreRepository.save(g1);
        genreRepository.save(g2);

        // Act
        List<Genre> result = genreRepository.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(g1));
        assertTrue(result.contains(g2));
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Genre.class)));
    }

    @Test
    void getAllGenresShouldReturnUnmodifiableList() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem();

        // Act
        List<Genre> result = repo.getAllGenres();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Genre.class)));
    }

    @Test
    void getAllGenresShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem();

        // Act
        List<Genre> result = repo.getAllGenres();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnGenreWhenTitleExists() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        IGenreRepository repository = new GenreRepositoryMem();
        Genre g = realFactory.create("Fantasy");
        repository.save(g);

        // Act
        Genre result = repository.findByTitle("Fantasy");

        // Assert
        assertEquals(g, result);
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        IGenreRepository repository = new GenreRepositoryMem();
        repository.save(realFactory.create("Fantasy"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findByTitle("Unknown"));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        // Arrange
        IGenreRepository repository = new GenreRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findByTitle(null));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsBlank() {
        // Arrange
        IGenreRepository repository = new GenreRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findByTitle(" "));
    }

    @Test
    void findGenreById_shouldThrowException_whenGenreIdIsNull() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findGenreById(null));
    }

    @Test
    void findGenreById_shouldReturnGenre_whenGenreExists() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        GenreRepositoryMem repo = new GenreRepositoryMem();
        Genre saved = repo.save(realFactory.create("Fantasy"));

        // Act
        Optional<Genre> result = repo.findGenreById(saved.id());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(saved, result.get());
    }

    @Test
    void findGenreById_shouldReturnEmpty_whenGenreDoesNotExist() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem();
        GenreId genreId = mock(GenreId.class);

        // Act
        Optional<Genre> result = repo.findGenreById(genreId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findGenreById_shouldReturnCorrectGenre_whenMultipleGenresExist() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        GenreRepositoryMem repo = new GenreRepositoryMem();
        repo.save(realFactory.create("Fantasy"));
        Genre genre2 = repo.save(realFactory.create("Science Fiction"));

        // Act
        Optional<Genre> result = repo.findGenreById(genre2.id());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("science fiction", result.get().id().getID());
    }

    @Test
    void save_shouldAddGenreAndReturnIt() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        GenreRepositoryMem repo = new GenreRepositoryMem();
        Genre genre = realFactory.create("Fantasy");

        // Act
        Genre result = repo.save(genre);

        // Assert
        assertEquals(genre, result);
        assertEquals(1, repo.getAllGenres().size());
    }

    @Test
    void save_shouldThrowException_whenGenreAlreadyExists() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        GenreRepositoryMem repo = new GenreRepositoryMem();
        Genre genre = realFactory.create("Fantasy");
        repo.save(genre);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(genre));
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
        assertTrue(repo.getAllGenres().isEmpty());
    }
}