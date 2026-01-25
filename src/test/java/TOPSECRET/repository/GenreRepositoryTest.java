package TOPSECRET.repository;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreRepositoryTest {


    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================

    @Test
    void shouldCreateRepositoryWithEmptyList() {
        // Act
        GenreRepository repository = new GenreRepository(List.of());

        // Assert
        assertNotNull(repository);
        assertEquals(0, repository.getAllGenres().size());
    }

    @Test
    void shouldThrowExceptionWhenListIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new GenreRepository(null)
        );
        assertEquals("Genre List cannot be null", exception.getMessage());
    }

    @Test
    void shouldCreateRepositoryWithExistingGenres() {
        // Arrange
        List<Genre> existingGenres = new ArrayList<>();
        existingGenres.add(new Genre("Romance"));
        existingGenres.add(new Genre("Horror"));

        // Act
        GenreRepository repository = new GenreRepository(existingGenres);

        // Assert
        assertEquals(2, repository.getAllGenres().size());
    }

    // ========================================
    // CREATE AND ADD TESTS
    // ========================================

    @Test
    void shouldCreateAndAddGenre() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>());

        // Act
        Genre result = repository.createAndAddGenre("Romance");

        // Assert
        assertNotNull(result);
        assertEquals(new Genre("Romance"), result);
        assertEquals(1, repository.getAllGenres().size());
        assertTrue(repository.getAllGenres().contains(result));
    }

    @Test
    void shouldThrowExceptionWhenGenreAlreadyExists() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>());
        repository.createAndAddGenre("Terror");

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddGenre("Terror")
        );
        assertEquals("Genre already exists", exception.getMessage());

        // Verify only one genre was added
        assertEquals(1, repository.getAllGenres().size());
    }

    @Test
    void shouldThrowExceptionForInvalidGenreName() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>());

        // Act + Assert - null
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddGenre(null)
        );

        // Act + Assert - blank
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddGenre("")
        );

        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAddGenre("   ")
        );

        // Verify no genres were added
        assertEquals(0, repository.getAllGenres().size());
    }

    // ========================================
    // GET ALL GENRES TESTS
    // ========================================

    @Test
    void shouldReturnEmptyListWhenNoGenres() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>());

        // Act
        List<Genre> result = repository.getAllGenres();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAllGenres() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>());
        repository.createAndAddGenre("Romance");
        repository.createAndAddGenre("Horror");
        repository.createAndAddGenre("Mystery");

        // Act
        List<Genre> result = repository.getAllGenres();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(new Genre("Romance")));
        assertTrue(result.contains(new Genre("Horror")));
        assertTrue(result.contains(new Genre("Mystery")));
    }

    @Test
    void shouldReturnDefensiveCopy() {
        // Arrange
        List<Genre> originalList = new ArrayList<>();
        originalList.add(new Genre("Programming"));
        GenreRepository repository = new GenreRepository(originalList);

        // Act
        List<Genre> result = repository.getAllGenres();

        // Assert - different list instances
        assertNotSame(originalList, result);
        assertEquals(originalList.size(), result.size());
    }
}
