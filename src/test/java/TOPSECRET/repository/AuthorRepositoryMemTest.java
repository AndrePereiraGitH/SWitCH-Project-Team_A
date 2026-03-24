package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorFactory;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorRepositoryMem}.
 *
 * <p>Both collaborators — {@link AuthorFactory} and the backing {@link List} —
 * are mocked, isolating the repository's own logic completely.
 *
 * <p><b>What is tested here (repository logic only):</b>
 * <ul>
 *   <li>Constructor — null list, null factory</li>
 *   <li>{@link AuthorRepositoryMem#createAndAddAuthor} — delegates to factory,
 *       adds result to list, rejects duplicates</li>
 *   <li>{@link AuthorRepositoryMem#getAllAuthors} — returns unmodifiable copy</li>
 * </ul>
 *
 */
class AuthorRepositoryMemTest {

    private final AuthorFactory authorFactory = mock(AuthorFactory.class);
    private final Country country = mock(Country.class);
    private final String name = "João";
    String validBio = "bio";

    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================
    @Test
    void shouldThrowExceptionWhenFactoryIsNull() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuthorRepositoryMem(null)
        );

        // Assert
        assertEquals("AuthorFactory cannot be null", exception.getMessage());
    }

    // ========================================
    // CREATE AND ADD TESTS
    // ========================================

    @Test
    void shouldDelegateCreationToFactoryAndAddToList() {
        // Arrange
        Author mockAuthor = mock(Author.class);
        when(authorFactory.create(name, country, LocalDate.of(1978, 5, 12), validBio))
                .thenReturn(mockAuthor);
        IAuthorRepository IAuthorRepository = new AuthorRepositoryMem(authorFactory);

        // Act
        Author result = IAuthorRepository.createAndAddAuthor(name, country, LocalDate.of(1978, 5, 12), validBio);

        // Assert
        assertSame(mockAuthor, result);
        verify(authorFactory).create(name, country, LocalDate.of(1978, 5, 12), validBio);
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        // Arrange
        Author mockAuthor = mock(Author.class);
        when(authorFactory.create(name, country, LocalDate.of(1978, 5, 12), validBio))
                .thenReturn(mockAuthor);

        IAuthorRepository IAuthorRepository = new AuthorRepositoryMem(authorFactory);
        IAuthorRepository.createAndAddAuthor(name, country, LocalDate.of(1978, 5, 12), validBio);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> IAuthorRepository.createAndAddAuthor(name, country, LocalDate.of(1978, 5, 12), validBio)
        );

        assertEquals("Author already exists", exception.getMessage());
    }

    // ========================================
    // GET ALL AUTHORS TESTS
    // ========================================

    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        IAuthorRepository IAuthorRepositoryMem = new AuthorRepositoryMem(authorFactory);

        // Act
        List<Author> result = IAuthorRepositoryMem.getAllAuthors();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Author.class)));
    }

    @Test
    void shouldReturnDefensiveCopy() {
        // Arrange
        IAuthorRepository IAuthorRepository = new AuthorRepositoryMem(authorFactory);
        Author mockAuthor = mock(Author.class);

        when(authorFactory.create(name, country, LocalDate.of(1978, 5, 12), validBio))
                .thenReturn(mockAuthor);

        IAuthorRepository.createAndAddAuthor(name, country, LocalDate.of(1978, 5, 12), validBio);

        // Act
        List<Author> result1 = IAuthorRepository.getAllAuthors();
        List<Author> result2 = IAuthorRepository.getAllAuthors();

        // Assert
        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }
}