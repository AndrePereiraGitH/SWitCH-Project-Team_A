//package TOPSECRET.repository;
//
//import TOPSECRET.domain.Author;
//import TOPSECRET.domain.AuthorFactory;
//import TOPSECRET.domain.Country;
//import TOPSECRET.domain.Profile;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Integration tests for {@link AuthorRepository}.
// *
// * <p>This test class uses <b>real collaborators</b> ({@link AuthorFactory} and {@link ArrayList})
// * rather than mocks, making these integration tests — they verify that {@link AuthorRepository},
// * {@link AuthorFactory}, {@link Author}, and {@link Profile} work correctly together.
// *
// * <p>If you want to unit test {@link AuthorRepository} in isolation, replace
// * {@link AuthorFactory} with a mock and inject it via the constructor.
// *
// * <p><b>What is tested:</b>
// * <ul>
// *   <li>Constructor — valid construction, null list, null factory</li>
// *   <li>{@link AuthorRepository#createAndAddAuthor} — happy path, duplicate prevention,
// *       same name with different data, invalid inputs</li>
// *   <li>{@link AuthorRepository#getAllAuthors} — empty list, multiple authors,
// *       unmodifiability, defensive copy</li>
// * </ul>
// */
//
//public class AuthorRepositoryDependenciesTest {
//
//    private final AuthorRepository authorRepository = new AuthorRepository(new ArrayList<>(), new AuthorFactory());
//    private final AuthorFactory authorFactory = new AuthorFactory();
//    private final String name = "João";
//
//
//    // ========================================
//    // CONSTRUCTOR TESTS
//    // ========================================
//
//    @Test
//    void shouldCreateRepositoryWithEmptyList() {
//        // Assert
//        assertNotNull(authorRepository);
//        assertEquals(0, authorRepository.getAllAuthors().size());
//    }
//
//    @Test
//    void shouldCreateRepositoryWithExistingAuthors() {
//        // Arrange
//        List<Author> existingAuthors = new ArrayList<>();
//        existingAuthors.add(authorFactory.create(name, new Country("Portugal"), LocalDate.of(1990, 1, 1), "bio"));
//
//        // Act
//        AuthorRepository repository = new AuthorRepository(existingAuthors, authorFactory);
//
//        // Assert
//        assertEquals(1, repository.getAllAuthors().size());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenListIsNull() {
//        // Act + Assert
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class, () -> new AuthorRepository(null, authorFactory)
//        );
//        assertEquals("Author cannot be null", exception.getMessage());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenFactoryIsNull() {
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> new AuthorRepository(new ArrayList<>(), null)
//        );
//        assertEquals("AuthorFactory cannot be null", exception.getMessage());
//    }
//
//    // ========================================
//    // CREATE AND ADD TESTS
//    // ========================================
//
//    @Test
//    void shouldCreateAuthor() {
//        // Act
//        Author result = authorRepository.createAndAddAuthor(name, new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("João", result.getProfile().getName());
//        assertEquals(new Country("Portugal"), result.getProfile().getCountry());
//        assertEquals(LocalDate.of(1978, 5, 12), result.getProfile().getBirthday());
//        assertEquals("valid bio", result.getBiography());
//        assertEquals(1, authorRepository.getAllAuthors().size());
//        assertTrue(authorRepository.getAllAuthors().contains(result));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenAuthorAlreadyExists() {
//        // Arrange
//        authorRepository.createAndAddAuthor(name, new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");
//
//        // Act + Assert
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> authorRepository.createAndAddAuthor(name, new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio")
//        );
//        assertEquals("Author already exists", exception.getMessage());
//
//        // Verify only one author was added
//        assertEquals(1, authorRepository.getAllAuthors().size());
//    }
//
//    @Test
//    void shouldAllowDifferentAuthorsWithSameNameButDifferentData() {
//        // Arrange
//        authorRepository.createAndAddAuthor(name, new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio1");
//
//        // Act
//        Author result = authorRepository.createAndAddAuthor(name, new Country("AUSTRALIA"), LocalDate.of(1980, 1, 1), "bio2");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, authorRepository.getAllAuthors().size());
//    }
//
//    @Test
//    void shouldThrowExceptionForInvalidAuthorData() {
//        // Arrange
//
//        // Act + Assert - null name
//        assertThrows(IllegalArgumentException.class,
//                () -> authorRepository.createAndAddAuthor(null, new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio")
//        );
//        // Act + Assert - null country
//        assertThrows(IllegalArgumentException.class,
//                () -> authorRepository.createAndAddAuthor(name, null, LocalDate.of(1980, 1, 1), "bio")
//        );
//        // Act + Assert - null birthday
//        assertThrows(IllegalArgumentException.class,
//                () -> authorRepository.createAndAddAuthor(name, new Country("Portugal"), null, "bio")
//        );
//        // Act + Assert - null biography
//        assertThrows(IllegalArgumentException.class,
//                () -> authorRepository.createAndAddAuthor(name, new Country("Portugal"), LocalDate.of(1980, 1, 1), null)
//        );
//        // Verify no authors were added
//        assertEquals(0, authorRepository.getAllAuthors().size());
//    }
//
//    // ========================================
//    // GET ALL AUTHORS TESTS
//    // ========================================
//
//    @Test
//    void shouldReturnEmptyListWhenNoAuthors() {
//        // Arrange
//
//        // Act
//        List<Author> result = authorRepository.getAllAuthors();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(0, result.size());
//    }
//
//    @Test
//    void shouldReturnAllAuthors() {
//        // Arrange
//        authorRepository.createAndAddAuthor(name, new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio1");
//        authorRepository.createAndAddAuthor("Author2", new Country("AUSTRALIA"), LocalDate.of(1985, 2, 2), "bio2");
//
//        // Act
//        List<Author> result = authorRepository.getAllAuthors();
//
//        // Assert
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    void shouldReturnUnmodifiableList() {
//        // Arrange
//        authorRepository.createAndAddAuthor(name, new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio");
//
//        // Act
//        List<Author> result = authorRepository.getAllAuthors();
//
//        // Assert - trying to modify should throw exception
//        assertThrows(UnsupportedOperationException.class,
//                () -> result.add(authorFactory.create(name, new Country("Portugal"), LocalDate.of(1990, 1, 1), "bio"))
//        );
//    }
//
//    @Test
//    void shouldReturnDefensiveCopy() {
//        // Arrange
//        authorRepository.createAndAddAuthor(name, new Country("Portugal"), LocalDate.of(1980, 1, 1), "bio");
//
//        // Act
//        List<Author> result1 = authorRepository.getAllAuthors();
//        List<Author> result2 = authorRepository.getAllAuthors();
//
//        // Assert - different list instances
//        assertNotSame(result1, result2);
//        assertEquals(result1, result2);
//    }
//}