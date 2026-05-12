package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US025AuthorMapper}.
 *
 * <p>Covers:
 * <ul>
 *   <li>{@code toDTO()} - valid conversion, null input</li>
 *   <li>{@code toDTOList()} - valid conversion, null input, empty list, null elements skipped</li>
 * </ul>
 */
class US025AuthorMapperTest {

    // =========================================================================
    // toDTO
    // =========================================================================

    /**
     * Ensures {@code toDTO()} returns a DTO with the author's name.
     */
    @Test
    void toDTOShouldReturnDTOWithAuthorName() {

        // ARRANGE
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("John Doe");

        US025AuthorMapper sut = new US025AuthorMapper();

        // ACT
        US025AuthorDTO result = sut.toDTO(authorDouble);

        // ASSERT
        assertEquals("John Doe", result.getName());
    }

    /**
     * Ensures {@code toDTO()} returns {@code null} when the author is {@code null}.
     */
    @Test
    void toDTOShouldReturnNullWhenAuthorIsNull() {

        // ARRANGE
        US025AuthorMapper sut = new US025AuthorMapper();

        // ACT
        US025AuthorDTO result = sut.toDTO(null);

        // ASSERT
        assertNull(result);
    }

    // =========================================================================
    // toDTOList
    // =========================================================================

    /**
     * Ensures {@code toDTOList()} returns a list of DTOs for a valid list of authors.
     */
    @Test
    void toDTOListShouldReturnDTOList() {

        // ARRANGE
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("John Doe");

        US025AuthorMapper sut = new US025AuthorMapper();

        // ACT
        List<US025AuthorDTO> result = sut.toDTOList(List.of(authorDouble));

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    /**
     * Ensures {@code toDTOList()} returns an empty list when input is {@code null}.
     */
    @Test
    void toDTOListShouldReturnEmptyListWhenInputIsNull() {

        // ARRANGE
        US025AuthorMapper sut = new US025AuthorMapper();

        // ACT
        List<US025AuthorDTO> result = sut.toDTOList(null);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    /**
     * Ensures {@code toDTOList()} returns an empty list when input is empty.
     */
    @Test
    void toDTOListShouldReturnEmptyListWhenInputIsEmpty() {

        // ARRANGE
        US025AuthorMapper sut = new US025AuthorMapper();

        // ACT
        List<US025AuthorDTO> result = sut.toDTOList(List.of());

        // ASSERT
        assertTrue(result.isEmpty());
    }
}