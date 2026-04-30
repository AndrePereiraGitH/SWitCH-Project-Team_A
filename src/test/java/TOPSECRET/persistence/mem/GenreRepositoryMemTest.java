package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreRepositoryMemTest {

    private final GenreFactory genreFactory = mock(GenreFactory.class);
    private final Genre genre = mock(Genre.class);

// TODO: re-enable when createAndAdd is re-introduced or US003 is refactored
//    @Test
//    void testCreateAndAddGenre_AddsNewGenre() {
//        // Arrange
//        IGenreRepository genreRepository = new GenreRepositoryMem(genreFactory);
//        String genreType = "Fantasy";
//        when(genre.getGenreType()).thenReturn(genreType);
//        when(genreFactory.create(genreType)).thenReturn(genre);
//        // Act
//        Genre result = genreRepository.createAndAdd(genreType);
//        // Assert
//        assertEquals(1, genreRepository.getAllGenres().size());
//        assertEquals(genre, result);
//    }

//    @Test
//    void testCreateAndAddGenre_ThrowsExceptionWhenGenreExists() {
//        // Arrange
//        String genreType = "Fantasy";
//        when(genre.getGenreType()).thenReturn(genreType);
//        when(genreFactory.create(genreType)).thenReturn(genre);
//        when(genre.sameAs(genre)).thenReturn(true);
//        IGenreRepository genreRepository = new GenreRepositoryMem(genreFactory);
//        genreRepository.createAndAdd(genreType);
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> genreRepository.createAndAdd(genreType));
//    }

    @Test
    void getAllGenres_ReturnsCorrectList() {
        // Arrange
        Genre genre2 = mock(Genre.class);
        when(genre.getGenreType()).thenReturn("Fantasy");
        when(genre2.getGenreType()).thenReturn("Science Fiction");
        when(genreFactory.create("Fantasy")).thenReturn(genre);
        when(genreFactory.create("Science Fiction")).thenReturn(genre2);


        IGenreRepository genreRepository = new GenreRepositoryMem(genreFactory);
        genreRepository.save(genre);       // adiciona ao repo
        genreRepository.save(genre2);

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
        repository.save(genre);

        // Act
        Genre result = repository.findByTitle("Fantasy");

        // Assert
        assertEquals(genre, result);
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        IGenreRepository repository = new GenreRepositoryMem(realFactory);
        repository.save(realFactory.create("Fantasy"));

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
        GenreId genreId = mock(GenreId.class);
        when(genre.id()).thenReturn(genreId);
        when(genreFactory.create("Fantasy")).thenReturn(genre);

        IGenreRepository repo = new GenreRepositoryMem(genreFactory);

        Genre createdGenre = repo.save(genre);

        Optional<Genre> result = repo.findGenreById(genreId);

        assertTrue(result.isPresent());
        assertEquals(genre, result.get());
        assertEquals(genre, createdGenre);
    }

    @Test
    void findGenreById_shouldReturnEmpty_whenGenreDoesNotExist() {
        IGenreRepository repo = new GenreRepositoryMem(genreFactory);

        GenreId genreId = mock(GenreId.class);

        Optional<Genre> result = repo.findGenreById(genreId);

        assertTrue(result.isEmpty());
    }
    @Test
    void findGenreById_shouldReturnCorrectGenre_whenMultipleGenresExist() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        GenreRepositoryMem repo = new GenreRepositoryMem(realFactory);

        repo.save(realFactory.create("Fantasy"));
        Genre genre2 = repo.save(realFactory.create("Science Fiction"));

        GenreId id2 = genre2.id();

        // Act
        Optional<Genre> result = repo.findGenreById(id2);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("science fiction", result.get().id().getID());
    }

    @Test
    void shouldThrowWhenGenreFactoryIsNull() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new GenreRepositoryMem(null));
    }
    @Test
    void save_shouldAddGenreAndReturnIt() {
        // Arrange
        GenreFactory realFactory = new GenreFactory();
        GenreRepositoryMem repo = new GenreRepositoryMem(realFactory);
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
        GenreRepositoryMem repo = new GenreRepositoryMem(realFactory);
        Genre genre = realFactory.create("Fantasy");
        repo.save(genre);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(genre));
    }

    @Test
    void save_shouldThrowException_whenGenreIsNull() {
        // Arrange
        IGenreRepository repo = new GenreRepositoryMem(genreFactory);

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
