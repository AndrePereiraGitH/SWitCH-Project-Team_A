package TOPSECRET.repository;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    private final CountryID countryID = mock(CountryID.class);
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
        when(authorFactory.create(name, countryID, LocalDate.of(1978, 5, 12), validBio))
                .thenReturn(mockAuthor);
        IAuthorRepository IAuthorRepository = new AuthorRepositoryMem(authorFactory);

        // Act
        Author result = IAuthorRepository.createAndAddAuthor(name, countryID, LocalDate.of(1978, 5, 12), validBio);

        // Assert
        assertSame(mockAuthor, result);
        verify(authorFactory).create(name, countryID, LocalDate.of(1978, 5, 12), validBio);
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        // Arrange
        Author mockAuthor = mock(Author.class);
        when(authorFactory.create(name, countryID, LocalDate.of(1978, 5, 12), validBio))
                .thenReturn(mockAuthor);

        IAuthorRepository IAuthorRepository = new AuthorRepositoryMem(authorFactory);
        IAuthorRepository.createAndAddAuthor(name, countryID, LocalDate.of(1978, 5, 12), validBio);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> IAuthorRepository.createAndAddAuthor(name, countryID, LocalDate.of(1978, 5, 12), validBio)
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

        when(authorFactory.create(name, countryID, LocalDate.of(1978, 5, 12), validBio))
                .thenReturn(mockAuthor);

        IAuthorRepository.createAndAddAuthor(name, countryID, LocalDate.of(1978, 5, 12), validBio);

        // Act
        List<Author> result1 = IAuthorRepository.getAllAuthors();
        List<Author> result2 = IAuthorRepository.getAllAuthors();

        // Assert
        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }


    // ========================================
    // FIND AUTHOR BY ID
    // ========================================

    @Test
    void findAuthorById_ShouldReturnAuthor_WhenIdExists() {
        // Arrange
        AuthorId id1 = new AuthorId("a1");
        AuthorId id2 = new AuthorId("a2");
        AuthorId id3 = new AuthorId("a3");

        Author author1Double = mock(Author.class);
        when(author1Double.id()).thenReturn(id1);
        Author author2Double = mock(Author.class);
        when(author2Double.id()).thenReturn(id2);
        Author author3Double = mock(Author.class);
        when(author3Double.id()).thenReturn(id3);

        AuthorRepositoryMem repo = new AuthorRepositoryMem(authorFactory);

        when(authorFactory.create(any(),  any(), any(), any())).thenReturn(author1Double, author2Double, author3Double);

        repo.createAndAddAuthor("author1", countryID, LocalDate.of(1978, 5, 12), validBio);
        repo.createAndAddAuthor("author2", countryID, LocalDate.of(1978, 5, 12), validBio);
        repo.createAndAddAuthor("author3", countryID, LocalDate.of(1978, 5, 12), validBio);

        // Act
        Optional<Author> result = repo.findAuthorById(id2);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(author2Double, result.get());
    }

    @Test
    void findAuthorById_ShouldReturnEmptyOptional_WhenIdDoesNotExist() {
        // Arrange
        AuthorId id1 = new AuthorId("a1");
        AuthorId id2 = new AuthorId("a2");
        AuthorId id3 = new AuthorId("a3");
        AuthorId unknownId = new AuthorId("unknown");

        Author author1Double = mock(Author.class);
        when(author1Double.id()).thenReturn(id1);
        Author author2Double = mock(Author.class);
        when(author2Double.id()).thenReturn(id2);
        Author author3Double = mock(Author.class);
        when(author3Double.id()).thenReturn(id3);

        AuthorRepositoryMem repo = new AuthorRepositoryMem(authorFactory);

        when(authorFactory.create(any(),  any(), any(), any())).thenReturn(author1Double, author2Double, author3Double);

        repo.createAndAddAuthor("author1", countryID, LocalDate.of(1978, 5, 12), validBio);
        repo.createAndAddAuthor("author2", countryID, LocalDate.of(1978, 5, 12), validBio);
        repo.createAndAddAuthor("author3", countryID, LocalDate.of(1978, 5, 12), validBio);;

        // Act
        Optional<Author> result = repo.findAuthorById(unknownId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuthorById_ShouldThrowException_WhenIdIsNull() {
        // Arrange
        AuthorRepositoryMem repo = new AuthorRepositoryMem(authorFactory);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.findAuthorById(null)
        );

        assertEquals("AuthorId cannot be null", ex.getMessage());
    }
}