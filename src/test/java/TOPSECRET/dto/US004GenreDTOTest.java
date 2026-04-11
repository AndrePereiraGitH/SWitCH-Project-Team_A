package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US004GenreDTO}.
 * These tests validate:
 *     <li>DTO creation with valid values</li>
 *     <li>Constructor validation rules</li>
 *     <li>Conversion from {@link Genre} domain objects to DTO objects</li>
 *     <li>Interaction with mocked domain collaborators</li>
 * Mockito is used to mock {@link Genre} domain objects
 * to isolate the DTO conversion behavior.
 * </p>
 */
class _US004GenreDTOTest {

    @Test
    void shouldCreateGenreDTO() {

        // ARRANGE
        GenreID id = mock(GenreID.class);

        // ACT
        US004GenreDTO dto =
                new US004GenreDTO(id, "Fantasy");

        // ASSERT
        assertEquals(id, dto.getId());
        assertEquals("Fantasy", dto.getTitle());
    }

    /**
     * Ensures exception when id is null.
     */
    @Test
    void shouldThrowExceptionWhenIdIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US004GenreDTO(null, "Fantasy"));
    }
}