package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthorDTO_US025 using mocks.
 */
class AuthorDTO_US025Test {


    // ===============================
    // Tests for fromAuthor()
    // ===============================

    @Test
    void fromAuthor_whenValidAuthor_shouldMapNameFromProfile() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Ken Follett");

        // Act
        AuthorDTO_US025 dto = AuthorDTO_US025.fromAuthor(authorDouble);

        // Assert
        assertNotNull(dto);
        assertEquals("Ken Follett", dto.getName());
    }

    @Test
    void fromAuthor_whenAuthorHasDifferentName_shouldMapThatExactName() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Dan Brown");

        // Act
        AuthorDTO_US025 dto = AuthorDTO_US025.fromAuthor(authorDouble);

        // Assert
        assertNotNull(dto);
        assertEquals("Dan Brown", dto.getName());
    }

    @Test
    void fromAuthor_whenAuthorIsNull_shouldReturnNull() {

        // Act
        AuthorDTO_US025 dto = AuthorDTO_US025.fromAuthor(null);

        // Assert
        assertNull(dto);
    }

    // ===============================
    // Tests for fromAuthors()
    // ===============================

    @Test
    void fromAuthors_listOfAuthors_shouldMapAllNames() {

        // Arrange
        Author authorDouble1 = mock(Author.class);
        Author authorDouble2 = mock(Author.class);

        when(authorDouble1.getName()).thenReturn("Author One");
        when(authorDouble2.getName()).thenReturn("Author Two");

        List<Author> authors = List.of(authorDouble1, authorDouble2);

        // Act
        List<AuthorDTO_US025> dtos = AuthorDTO_US025.fromAuthors(authors);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("Author One", dtos.get(0).getName());
        assertEquals("Author Two", dtos.get(1).getName());
    }

    @Test
    void fromAuthors_nullList_shouldReturnMutableList() {

        // Act
        List<AuthorDTO_US025> dtos = AuthorDTO_US025.fromAuthors(null);

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());

        // Verify list is mutable
        dtos.add(new AuthorDTO_US025("Teste"));
        assertEquals(1, dtos.size());
    }
}