package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.ValueObjects.AuthorId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US026AuthorMapper}.
 */
class US026AuthorMapperTest {

    // ===========================================================
    // ================= Tests for fromDomain ====================
    // ===========================================================

    @Test
    void fromDomain_shouldReturnDto_whenAuthorIsValid() {
        // Arrange
        Author author = mock(Author.class);
        AuthorId authorId = mock(AuthorId.class);

        when(author.id()).thenReturn(authorId);
        when(author.getName()).thenReturn("José Saramago");

        // Act
        US026AuthorDTO result = US026AuthorMapper.fromDomain(author);

        // Assert
        assertNotNull(result);
        assertEquals(authorId, result.id());
        assertEquals("José Saramago", result.name());
    }

    @Test
    void fromDomain_shouldThrowIllegalArgumentException_whenAuthorIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> US026AuthorMapper.fromDomain(null)
        );

        assertEquals("author cannot be null", exception.getMessage());
    }

    // ===========================================================
    // ================= Tests for fromDomainList ================
    // ===========================================================

    @Test
    void fromDomainList_shouldReturnDtoList_whenAuthorsListIsValid() {
        // Arrange
        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);

        AuthorId authorId1 = mock(AuthorId.class);
        AuthorId authorId2 = mock(AuthorId.class);

        when(author1.id()).thenReturn(authorId1);
        when(author1.getName()).thenReturn("José Saramago");

        when(author2.id()).thenReturn(authorId2);
        when(author2.getName()).thenReturn("Fernando Pessoa");

        List<Author> authors = List.of(author1, author2);

        // Act
        List<US026AuthorDTO> result = US026AuthorMapper.fromDomainList(authors);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(authorId1, result.get(0).id());
        assertEquals("José Saramago", result.get(0).name());

        assertEquals(authorId2, result.get(1).id());
        assertEquals("Fernando Pessoa", result.get(1).name());
    }

    @Test
    void fromDomainList_shouldReturnEmptyList_whenAuthorsListIsEmpty() {
        // Act
        List<US026AuthorDTO> result = US026AuthorMapper.fromDomainList(List.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void fromDomainList_shouldThrowIllegalArgumentException_whenAuthorsListIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> US026AuthorMapper.fromDomainList(null)
        );

        assertEquals("list of authors cannot be null", exception.getMessage());
    }

    @Test
    void fromDomainList_shouldThrowIllegalArgumentException_whenAuthorsListContainsNull() {
        // Arrange
        Author author = mock(Author.class);
        AuthorId authorId = mock(AuthorId.class);

        when(author.id()).thenReturn(authorId);
        when(author.getName()).thenReturn("José Saramago");

        List<Author> authors = java.util.Arrays.asList(author, null);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> US026AuthorMapper.fromDomainList(authors)
        );

        assertEquals("author cannot be null", exception.getMessage());
    }
}