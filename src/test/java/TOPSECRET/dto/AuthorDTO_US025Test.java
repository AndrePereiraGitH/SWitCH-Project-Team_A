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
        Author author = mock(Author.class);
        Profile profile = mock(Profile.class);

        when(author.getProfile()).thenReturn(profile);
        when(profile.getName()).thenReturn("Ken Follett");

        // Act
        AuthorDTO_US025 dto = AuthorDTO_US025.fromAuthor(author);

        // Assert
        assertNotNull(dto);
        assertEquals("Ken Follett", dto.getName());
    }

    @Test
    void fromAuthor_whenAuthorHasDifferentName_shouldMapThatExactName() {

        // Arrange
        Author author = mock(Author.class);
        Profile profile = mock(Profile.class);

        when(author.getProfile()).thenReturn(profile);
        when(profile.getName()).thenReturn("Dan Brown");

        // Act
        AuthorDTO_US025 dto = AuthorDTO_US025.fromAuthor(author);

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
        Author a1 = mock(Author.class);
        Author a2 = mock(Author.class);

        Profile p1 = mock(Profile.class);
        Profile p2 = mock(Profile.class);

        when(a1.getProfile()).thenReturn(p1);
        when(a2.getProfile()).thenReturn(p2);

        when(p1.getName()).thenReturn("Author One");
        when(p2.getName()).thenReturn("Author Two");

        List<Author> authors = List.of(a1, a2);

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