package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreDTO_US017Test {

    @Test
    void shouldConvertGenreToDTO() {
        Genre genre = new Genre("Fiction");

        GenreDTO_US017 dto = GenreDTO_US017.toDTO(genre);

        assertNotNull(dto);
        assertEquals("Fiction", dto.name);
    }

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            GenreDTO_US017.toDTO(null);
        });
    }
}