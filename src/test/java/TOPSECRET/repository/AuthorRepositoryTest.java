package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AuthorRepositoryTest {

    private final AuthorRepository authorRepository = new AuthorRepository(new ArrayList<>());

    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================

    @Test
    void shouldCreateRepositoryWithEmptyList() {
        // Assert
        assertNotNull(authorRepository);
        assertEquals(0, authorRepository.getAllAuthors().size());
    }

    @Test
    void shouldThrowExceptionWhenListIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new AuthorRepository(null)
        );
        assertEquals("Author cannot be null", exception.getMessage());
    }

    @Test
    void shouldCreateRepositoryWithExistingAuthors() {
        // Arrange
        List<Author> existingAuthors = new ArrayList<>();
        existingAuthors.add(new Author("Name", new Country("Portugal"), LocalDate.of(1990, 1, 1), "bio"));

        // Act
        AuthorRepository repository = new AuthorRepository(existingAuthors);

        // Assert
        assertEquals(1, repository.getAllAuthors().size());
    }

    // ========================================
    // CREATE AND ADD TESTS
    // ========================================

    @Test
    void shouldCreateAuthor() {
        //  Arrange
        Profile expectedProfile = new Profile("João", new Country("Portugal"), LocalDate.of(1978, 5, 12));
        Author expected = new Author(expectedProfile, "valid bio");

        // Act
        Author result = authorRepository.createAndAddAuthor("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        assertEquals(1, authorRepository.getAllAuthors().size());
        assertTrue(authorRepository.getAllAuthors().contains(result));
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        // Arrange
        authorRepository.createAndAddAuthor("repeatedName", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authorRepository.createAndAddAuthor("repeatedName", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio")
        );
        assertEquals("Author already exists", exception.getMessage());

        // Verify only one author was added
        assertEquals(1, authorRepository.getAllAuthors().size());
    }

    @Test
    void shouldAllowDifferentAuthorsWithSameNameButDifferentData() {
        // Arrange
        authorRepository.createAndAddAuthor("John", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio1");

        // Act
        Author result = authorRepository.createAndAddAuthor("John", new Country("AUSTRALIA"), LocalDate.of(1980, 1, 1), "bio2");

        // Assert
        assertNotNull(result);
        assertEquals(2, authorRepository.getAllAuthors().size());
    }

    @Test
    void shouldThrowExceptionForInvalidAuthorData() {
        // Arrange

        // Act + Assert - null name
        assertThrows(IllegalArgumentException.class,
                () -> authorRepository.createAndAddAuthor(null, new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio")
        );
        // Act + Assert - null country
        assertThrows(IllegalArgumentException.class,
                () -> authorRepository.createAndAddAuthor("Name", null, LocalDate.of(1980, 1, 1), "bio")
        );
        // Act + Assert - null birthday
        assertThrows(IllegalArgumentException.class,
                () -> authorRepository.createAndAddAuthor("Name", new Country("Portugal"), null, "bio")
        );
        // Act + Assert - null biography
        assertThrows(IllegalArgumentException.class,
                () -> authorRepository.createAndAddAuthor("Name", new Country("Portugal"), LocalDate.of(1980, 1, 1), null)
        );
        // Verify no authors were added
        assertEquals(0, authorRepository.getAllAuthors().size());
    }

    // ========================================
    // GET ALL AUTHORS TESTS
    // ========================================

    @Test
    void shouldReturnEmptyListWhenNoAuthors() {
        // Arrange

        // Act
        List<Author> result = authorRepository.getAllAuthors();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAllAuthors() {
        // Arrange
        authorRepository.createAndAddAuthor("Author1", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio1");
        authorRepository.createAndAddAuthor("Author2", new Country("AUSTRALIA"), LocalDate.of(1985, 2, 2), "bio2");

        // Act
        List<Author> result = authorRepository.getAllAuthors();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        authorRepository.createAndAddAuthor("Author1", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio");

        // Act
        List<Author> result = authorRepository.getAllAuthors();

        // Assert - trying to modify should throw exception
        assertThrows(UnsupportedOperationException.class,
                () -> result.add(new Author("Hacker", new Country("Portugal"), LocalDate.of(1990, 1, 1), "bio"))
        );
    }

    @Test
    void shouldReturnDefensiveCopy() {
        // Arrange
        authorRepository.createAndAddAuthor("Author1", new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio");

        // Act
        List<Author> result1 = authorRepository.getAllAuthors();
        List<Author> result2 = authorRepository.getAllAuthors();

        // Assert - different list instances
        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }
}