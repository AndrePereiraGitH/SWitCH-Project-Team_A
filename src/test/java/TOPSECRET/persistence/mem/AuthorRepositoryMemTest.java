package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuthorRepositoryMemTest {

    private final AuthorFactory authorFactory = mock(AuthorFactory.class);
    private final CountryId countryID = mock(CountryId.class);
    private final String name = "João";

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
    // SAVE TESTS
    // ========================================

    @Test
    void save_shouldAddAuthorAndReturnSameAuthor_whenAuthorDoesNotExist() throws InstanceAlreadyExistsException {
        // Arrange
        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);
        AuthorId authorId = mock(AuthorId.class);

        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(authorId);

        // Act
        Author result = repo.save(authorDouble);

        // Assert
        assertSame(authorDouble, result);
    }

    @Test
    void save_shouldAddAuthorToRepository_whenAuthorDoesNotExist() throws InstanceAlreadyExistsException {
        // Arrange
        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);
        AuthorId authorId = mock(AuthorId.class);
        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(authorId);

        // Act
        repo.save(authorDouble);

        // Assert
        assertTrue(repo.findAll().contains(authorDouble));
    }

    @Test
    void save_shouldThrowException_whenAuthorAlreadyExists() throws InstanceAlreadyExistsException {
        // Arrange
        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);
        AuthorId authorId = mock(AuthorId.class);
        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(authorId);

        repo.save(authorDouble);

        // Act + Assert
        InstanceAlreadyExistsException ex = assertThrows(
                InstanceAlreadyExistsException.class,
                () -> repo.save(authorDouble)
        );

        assertEquals("Author already exists", ex.getMessage());
    }

    // ========================================
    // EXISTS BY ID TESTS
    // ========================================

    @Test
    void existsById_shouldReturnTrue_whenAuthorIdExists() throws InstanceAlreadyExistsException {
        // Arrange
        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);
        AuthorId authorId = mock(AuthorId.class);
        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(authorId);

        repo.save(authorDouble);

        // Act
        boolean result = repo.existsById(authorId);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenAuthorIdDoesNotExist() throws InstanceAlreadyExistsException {
        // Arrange
        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);
        AuthorId existingId = mock(AuthorId.class);
        AuthorId unknownId = mock(AuthorId.class);

        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(existingId);

        repo.save(authorDouble);

        // Act
        boolean result = repo.existsById(unknownId);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenRepositoryIsEmpty() {
        // Arrange
        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);
        AuthorId authorId = mock(AuthorId.class);

        // Act
        boolean result = repo.existsById(authorId);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_shouldThrowException_whenAuthorIdIsNull() {
        // Arrange
        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.existsById(null)
        );

        assertEquals("authorId cannot be null", ex.getMessage());
    }

    // ========================================
    // GET ALL AUTHORS TESTS
    // ========================================

    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        IAuthorRepository IAuthorRepositoryMem = new AuthorRepositoryMem(authorFactory);

        // Act
        List<Author> result = IAuthorRepositoryMem.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Author.class)));
    }

    @Test
    void shouldReturnDefensiveCopy() throws InstanceAlreadyExistsException {
        // Arrange
        IAuthorRepository authorRepository = new AuthorRepositoryMem(authorFactory);
        AuthorId authorId = mock(AuthorId.class);

        Author authorDouble = mock(Author.class);
        when(authorDouble.id()).thenReturn(authorId);

        authorRepository.save(authorDouble);


        // Act
        List<Author> result1 = authorRepository.findAll();
        List<Author> result2 = authorRepository.findAll();

        // Assert
        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }


    // ========================================
    // FIND AUTHOR BY ID
    // ========================================

    @Test
    void findAuthorById_ShouldReturn_WhenIdExists() throws InstanceAlreadyExistsException {
        // Arrange
        AuthorId id1 = mock(AuthorId.class);
        AuthorId id2 = mock(AuthorId.class);
        AuthorId id3 = mock(AuthorId.class);

        Author author1Double = mock(Author.class);
        when(author1Double.id()).thenReturn(id1);
        Author author2Double = mock(Author.class);
        when(author2Double.id()).thenReturn(id2);
        Author author3Double = mock(Author.class);
        when(author3Double.id()).thenReturn(id3);

        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);

        repo.save(author1Double);
        repo.save(author2Double);
        repo.save(author3Double);

        // Act
        Optional<Author> result = repo.findById(id2);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(author2Double, result.get());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenIdDoesNotExist() throws InstanceAlreadyExistsException {
        // Arrange
        AuthorId id1 = mock(AuthorId.class);
        AuthorId id2 = mock(AuthorId.class);
        AuthorId id3 = mock(AuthorId.class);
        AuthorId unknownId = mock(AuthorId.class);

        Author author1Double = mock(Author.class);
        when(author1Double.id()).thenReturn(id1);
        Author author2Double = mock(Author.class);
        when(author2Double.id()).thenReturn(id2);
        Author author3Double = mock(Author.class);
        when(author3Double.id()).thenReturn(id3);

        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);

        repo.save(author1Double);
        repo.save(author2Double);
        repo.save(author3Double);

        // Act
        Optional<Author> result = repo.findById(unknownId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_ShouldThrowException_WhenIdIsNull() {
        // Arrange
        IAuthorRepository repo = new AuthorRepositoryMem(authorFactory);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.findById(null)
        );

        assertEquals("AuthorId cannot be null", ex.getMessage());
    }
}