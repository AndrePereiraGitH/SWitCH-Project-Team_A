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

    @Test
    void shouldThrowExceptionWhenIdIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US004GenreDTO(null, "Fantasy"));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {

        // ARRANGE
        GenreID id = mock(GenreID.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US004GenreDTO(id, null));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsBlank() {

        // ARRANGE
        GenreID id = mock(GenreID.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US004GenreDTO(id, " "));
    }
}