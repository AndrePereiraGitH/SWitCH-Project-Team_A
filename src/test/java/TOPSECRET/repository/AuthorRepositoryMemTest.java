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
import java.util.UUID;

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
    private final AuthorId authorId = mock(AuthorId.class);
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
    // findAuthorById
    // ========================================

    @Test
    void findAuthorById_nullId_throwsNullPointerException() {
        // Arrange
        AuthorRepositoryMem repo = new AuthorRepositoryMem(authorFactory);
        repo.createAndAddAuthor(name, countryID,  LocalDate.of(1978, 5, 12), validBio);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> repo.findAuthorById(null)
        );
    }

    @Test
    void findAuthorById_emptyRepository_returnsEmpty() {
        // Arrange
        AuthorRepositoryMem repo = new AuthorRepositoryMem(authorFactory);

        // Act
        Optional<Author> result = repo.findAuthorById(authorId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuthorById_existingAuthor_returnsAuthor() {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);

        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(authorId);

        when(authorFactory.create(name, countryID, LocalDate.of(1978, 5, 12), validBio))
                .thenReturn(authorDouble);

        AuthorRepositoryMem repo = new AuthorRepositoryMem(authorFactory);
        Author saved = repo.createAndAddAuthor(
                name, countryID,  LocalDate.of(1978, 5, 12), validBio);

        // Act
        Optional<Author> result = repo.findAuthorById(saved.id());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(saved, result.get());
    }

    @Test
    void findAuthorById_unknownId_returnsEmpty() {
        // Arrange
        AuthorId authorId1 = mock(AuthorId.class);
        AuthorId authorId2 = mock(AuthorId.class);

        Author author1 = mock(Author.class);
        when(author1.id()).thenReturn(authorId1);

        Author author2 = mock(Author.class);
        when(author2.id()).thenReturn(authorId2);

        // Stub using the SAME arguments passed to createAndAddAuthor
        when(authorFactory.create(name, countryID, LocalDate.of(1978, 5, 12), validBio))
                .thenReturn(author1);

        AuthorRepositoryMem repo = new AuthorRepositoryMem(authorFactory);
        repo.createAndAddAuthor(name, countryID, LocalDate.of(1978, 5, 12), validBio);

        // Act
        Optional<Author> result = repo.findAuthorById(authorId2);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAuthorById_multipleAuthors_returnsCorrectOne() {
        // Arrange
        AuthorId authorId1 = mock(AuthorId.class);
        AuthorId authorId2 = mock(AuthorId.class);

        Author author1 = mock(Author.class);
        when(author1.id()).thenReturn(authorId1);

        Author author2 = mock(Author.class);
        when(author2.id()).thenReturn(authorId2);

        CountryID CountryId_1 = mock(CountryID.class);
        when(CountryId_1.getCountryID()).thenReturn(UUID.randomUUID().toString());

        CountryID CountryId_2 = mock(CountryID.class);
        when(CountryId_2.getCountryID()).thenReturn(UUID.randomUUID().toString());

        when(authorFactory.create("George Orwell", CountryId_1, LocalDate.of(1903, 6, 25), validBio))
                .thenReturn(author1);

        when(authorFactory.create("Aldous Huxley", CountryId_2, LocalDate.of(1894, 7, 26), validBio))
                .thenReturn(author2);

        AuthorRepositoryMem repo = new AuthorRepositoryMem(authorFactory);


        Author first = repo.createAndAddAuthor("George Orwell", CountryId_1, LocalDate.of(1903, 6, 25), validBio);
        Author second = repo.createAndAddAuthor("Aldous Huxley", CountryId_2, LocalDate.of(1894, 7, 26), validBio);

        // Act
        Optional<Author> result = repo.findAuthorById(second.id());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(second, result.get());
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
}