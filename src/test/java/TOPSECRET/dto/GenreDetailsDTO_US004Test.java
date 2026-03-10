package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenreDetailsDTO_US004}.
 * These tests validate:
 *     <li>DTO creation with valid values</li>
 *     <li>Constructor validation rules</li>
 *     <li>Conversion from {@link Genre} domain objects to DTO objects</li>
 *     <li>Interaction with mocked domain collaborators</li>
 * Mockito is used to mock {@link Genre} domain objects
 * to isolate the DTO conversion behavior.
 * </p>
 */
class GenreDetailsDTO_US004Test {

    /**
     * Ensures a DTO is correctly created when a valid title is provided.
     */
    @Test
    void shouldCreateDTOWithValidTitle() {

        // ARRANGE
        String title = "Fantasy";

        // ACT
        GenreDetailsDTO_US004 dto = new GenreDetailsDTO_US004(title);

        // ASSERT
        assertEquals("Fantasy", dto.getTitle());
    }

    /**
     * Ensures an exception is thrown when the title is null.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new GenreDetailsDTO_US004(null)
        );
    }

    /**
     * Ensures an exception is thrown when the title is blank.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsBlank() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new GenreDetailsDTO_US004(" ")
        );
    }

    /**
     * Ensures domain Genre objects are correctly converted into DTO objects.
     */
    @Test
    void shouldConvertGenreToDTO() {

        // ARRANGE
        Genre genreMock = mock(Genre.class);

        when(genreMock.getGenreType()).thenReturn("Fantasy");

        // ACT
        List<GenreDetailsDTO_US004> result =
                GenreDetailsDTO_US004.genreToDTO(List.of(genreMock));

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("Fantasy", result.get(0).getTitle());
    }

    /**
     * Ensures an empty list results in an empty DTO list.
     */
    @Test
    void shouldReturnEmptyListWhenGenreListIsEmpty() {

        // ARRANGE
        List<Genre> genres = List.of();

        // ACT
        List<GenreDetailsDTO_US004> result =
                GenreDetailsDTO_US004.genreToDTO(genres);

        // ASSERT
        assertTrue(result.isEmpty());
    }
}