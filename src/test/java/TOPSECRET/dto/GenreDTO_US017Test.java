package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreMapper_US017Test {

    private final GenreMapper_US017 mapper = new GenreMapper_US017();

    @Test
    void shouldConvertGenreToDTO() {

        Genre genre = new Genre("Fiction");

        GenreDTO_US017 dto = mapper.toDTO(genre);

        assertNotNull(dto);
        assertEquals("Fiction", dto.name);
    }

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {

        assertThrows(IllegalArgumentException.class, () -> {
            mapper.toDTO(null);
        });
    }
}
