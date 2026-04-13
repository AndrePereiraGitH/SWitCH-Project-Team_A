package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US004GenreDTOTest {

    @Test
    void shouldCreateGenreDTO() {

        // ARRANGE
        GenreID id = mock(GenreID.class);

        // ACT
        US004GenreDTO dto =
                new US004GenreDTO(id, "Fantasy");

        // ASSERT
        assertEquals(id, dto.id());
        assertEquals("Fantasy", dto.title());
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