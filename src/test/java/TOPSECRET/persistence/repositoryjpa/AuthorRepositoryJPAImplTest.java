package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.persistence.repositoryjpa.assembler.AuthorAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuthorDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IAuthorRepositoryJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorRepositoryJPAImplTest {

    private IAuthorRepositoryJPA authorRepositoryJpa;
    private AuthorAssembler authorAssembler;
    private AuthorRepositoryJPAImpl repository;

    @BeforeEach
    void setUp() {
        authorRepositoryJpa = mock(IAuthorRepositoryJPA.class);
        authorAssembler = mock(AuthorAssembler.class);

        repository = new AuthorRepositoryJPAImpl(authorRepositoryJpa, authorAssembler);
    }

    // ====================================
    // =============== SAVE ===============
    // ====================================
    @Test
    void save_ShouldReturnPersistedAuthor() throws InstanceAlreadyExistsException {
        // Arrange
        Author author = mock(Author.class);
        AuthorId authorId = mock(AuthorId.class);

        AuthorDM authorDM = mock(AuthorDM.class);
        AuthorDM persistedAuthorDM = mock(AuthorDM.class);
        Author persistedAuthor = mock(Author.class);

        when(author.id()).thenReturn(authorId);
        when(authorRepositoryJpa.existsById(authorId)).thenReturn(false);
        when(authorAssembler.toDM(author)).thenReturn(authorDM);
        when(authorRepositoryJpa.save(authorDM)).thenReturn(persistedAuthorDM);
        when(authorAssembler.toDomain(persistedAuthorDM)).thenReturn(persistedAuthor);

        // Act
        Author result = repository.save(author);

        // Assert
        assertEquals(persistedAuthor, result);
    }

    @Test
    void save_ShouldThrowException_WhenAuthorIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );

        assertEquals("author cannot be null", exception.getMessage());
    }

    @Test
    void save_ShouldThrowException_WhenAuthorAlreadyExists() {
        // Arrange
        Author author = mock(Author.class);
        AuthorId authorId = mock(AuthorId.class);

        when(author.id()).thenReturn(authorId);
        when(authorRepositoryJpa.existsById(authorId)).thenReturn(true);

        // Act + Assert
        InstanceAlreadyExistsException exception = assertThrows(
                InstanceAlreadyExistsException.class,
                () -> repository.save(author)
        );

        assertEquals("AuthorId already exists", exception.getMessage());
    }

    // =============================================
    // =============== EXISTS BY ID ===============
    // ============================================

    @Test
    void existsById_ShouldReturnTrue_WhenAuthorExists() {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);

        when(authorRepositoryJpa.existsById(authorId)).thenReturn(true);

        // Act
        boolean result = repository.existsById(authorId);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_ShouldReturnFalse_WhenAuthorDoesNotExist() {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);

        when(authorRepositoryJpa.existsById(authorId)).thenReturn(false);

        // Act
        boolean result = repository.existsById(authorId);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_ShouldThrowException_WhenAuthorIdIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById(null)
        );

        assertEquals("authorId cannot be null", exception.getMessage());
    }

    // ========================================
    // =============== FIND ALL ===============
    // ========================================

    @Test
    void findAll_shouldReturnAllAuthors() {
        // Arrange
        AuthorDM authorDM1 = mock(AuthorDM.class);
        AuthorDM authorDM2 = mock(AuthorDM.class);

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);

        when(authorRepositoryJpa.findAll()).thenReturn(List.of(authorDM1, authorDM2));
        when(authorAssembler.toDomain(authorDM1)).thenReturn(author1);
        when(authorAssembler.toDomain(authorDM2)).thenReturn(author2);

        // Act
        List<Author> result = repository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(List.of(author1, author2), result);
    }

    @Test
    void findAll_shouldReturnEmptyList_WhenNoAuthorsExist() {
        // Arrange
        when(authorRepositoryJpa.findAll()).thenReturn(List.of());

        // Act
        List<Author> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ============================================
    // =============== EXISTS BY ID ===============
    // ============================================

    @Test
    void findByIdShouldReturnAuthorWhenAuthorExists() {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        AuthorDM authorDM = mock(AuthorDM.class);
        Author expectedAuthor = mock(Author.class);

        when(authorRepositoryJpa.findById(authorId)).thenReturn(Optional.of(authorDM));
        when(authorAssembler.toDomain(authorDM)).thenReturn(expectedAuthor);

        // Act
        Optional<Author> result = repository.findById(authorId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedAuthor, result.get());
    }

    @Test
    void findByIdShouldReturnEmptyOptionalWhenAuthorDoesNotExist() {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);

        when(authorRepositoryJpa.findById(authorId)).thenReturn(Optional.empty());

        // Act
        Optional<Author> result = repository.findById(authorId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdShouldThrowExceptionWhenIdIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(null)
        );

        assertEquals("id cannot be null", exception.getMessage());
    }



}