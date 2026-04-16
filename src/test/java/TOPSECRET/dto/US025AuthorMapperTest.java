package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US025AuthorMapper}.
 *
 * Verifies that Author entities are correctly converted to
 * {@link US025AuthorDTO} representations, including edge cases
 * such as null input and empty lists.
 */
class US025AuthorMapperTest {

    // ===============================
    // Tests for toDTO()
    // ===============================

    @Test
    void toDTO_whenValidAuthor_shouldMapNameCorrectly() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Ken Follett");

        // Act
        US025AuthorDTO dto = US025AuthorMapper.toDTO(authorDouble);

        // Assert
        assertNotNull(dto);
        assertEquals("Ken Follett", dto.name());
    }

    @Test
    void toDTO_whenAuthorHasDifferentName_shouldMapThatExactName() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Dan Brown");

        // Act
        US025AuthorDTO dto = US025AuthorMapper.toDTO(authorDouble);

        // Assert
        assertNotNull(dto);
        assertEquals("Dan Brown", dto.name());
    }

    @Test
    void toDTO_whenAuthorIsNull_shouldThrowException() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> US025AuthorMapper.toDTO(null)
        );
    }

    // ===============================
    // Tests for toDTOList()
    // ===============================

    @Test
    void toDTOList_listOfAuthors_shouldMapAllNames() {

        // Arrange
        Author authorDouble1 = mock(Author.class);
        Author authorDouble2 = mock(Author.class);

        when(authorDouble1.getName()).thenReturn("Author One");
        when(authorDouble2.getName()).thenReturn("Author Two");

        List<Author> authors = List.of(authorDouble1, authorDouble2);

        // Act
        List<US025AuthorDTO> dtos = US025AuthorMapper.toDTOList(authors);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("Author One", dtos.get(0).name());
        assertEquals("Author Two", dtos.get(1).name());
    }

    @Test
    void toDTOList_whenNullList_shouldThrowException() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> US025AuthorMapper.toDTOList(null)
        );
    }

    @Test
    void constructor_shouldThrowUnsupportedOperationException() {
        // Arrange
        Constructor<US025AuthorMapper> constructor;

        // Act + Assert
        try {
            constructor = US025AuthorMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            assertInstanceOf(UnsupportedOperationException.class, e.getCause());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}