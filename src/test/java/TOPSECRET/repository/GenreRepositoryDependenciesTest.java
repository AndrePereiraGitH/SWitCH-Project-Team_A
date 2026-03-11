package TOPSECRET.repository;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreFactory;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GenreRepository.
 * Verifies:
 * - Successful creation and addition of Genre
 * - Duplicate prevention
 * - Retrieval using findByTitle
 */

class GenreRepositoryDependenciesTest {



    @Test
    void shouldCreateRepositoryWithEmptyList() {
        // Act
        GenreRepository repository = new GenreRepository(new GenreFactory());

        // Assert
        assertNotNull(repository);
        assertEquals(0, repository.getAllGenres().size());
    }

    // ========================================
    // CREATE AND ADD TESTS
    // ========================================

    @Test
    void shouldcreateAndAddGenre() {
        // Arrange
        GenreRepository repository = new GenreRepository(new GenreFactory());

        // Act
        Genre result = repository.createAndAdd("Romance");

        // Assert
        assertNotNull(result);
        assertEquals(new GenreFactory().create("Romance"), result);
        assertEquals(1, repository.getAllGenres().size());
        assertTrue(repository.getAllGenres().contains(result));
    }

    @Test
    void shouldThrowExceptionWhenGenreAlreadyExists() {
        // Arrange
        GenreRepository repository = new GenreRepository(new GenreFactory());
        repository.createAndAdd("Terror");

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd("Terror")
        );
        assertEquals("Genre already exists", exception.getMessage());

        // Verify only one genre was added
        assertEquals(1, repository.getAllGenres().size());
    }

    @Test
    void shouldThrowExceptionForInvalidGenreName() {
        // Arrange
        GenreRepository repository = new GenreRepository(new GenreFactory());

        // Act + Assert - null
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(null)
        );

        // Act + Assert - blank
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd("")
        );

        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd("   ")
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
        GenreRepository repository = new GenreRepository(new GenreFactory());

        // Act
        List<Genre> result = repository.getAllGenres();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAllGenres() {
        // Arrange
        GenreRepository repository = new GenreRepository(new GenreFactory());
        repository.createAndAdd("Romance");
        repository.createAndAdd("Horror");
        repository.createAndAdd("Mystery");

        // Act
        List<Genre> result = repository.getAllGenres();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(new GenreFactory().create("Romance")));
        assertTrue(result.contains(new GenreFactory().create("Horror")));
        assertTrue(result.contains(new GenreFactory().create("Mystery")));
    }

    // US004 starts -----------
    /**
     * Verifies that findByTitle returns the correct Genre.
     */
    @Test
    void shouldReturnGenreWhenTitleExists() {

        // ARRANGE
        GenreRepository repository =
                new GenreRepository(new GenreFactory());

        repository.createAndAdd("Fantasy");

        // ACT
        Genre result = repository.findByTitle("Fantasy");

        // ASSERT
        assertEquals("Fantasy", result.getGenreType());
    }

    /**
     * Verifies that findByTitle throws exception when not found.
     */
    @Test
    void shouldThrowExceptionWhenGenreNotFound() {

        // ARRANGE
        GenreRepository repository =
                new GenreRepository( new GenreFactory());

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repository.findByTitle("Unknown")
        );
    }

    /**
     * Verifies that findByTitle throws exception when title is null.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        GenreRepository repo = new GenreRepository( new GenreFactory());
        assertThrows(IllegalArgumentException.class,
                () -> repo.findByTitle(null));
    }

    /**
     * Verifies that findByTitle throws exception when title is blank.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsBlank() {
        GenreRepository repo = new GenreRepository(  new GenreFactory());
        assertThrows(IllegalArgumentException.class,
                () -> repo.findByTitle(" "));
    }
    // ends ---------------------

}
