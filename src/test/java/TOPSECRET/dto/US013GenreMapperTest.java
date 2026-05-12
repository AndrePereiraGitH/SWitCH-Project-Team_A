package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US013GenreMapperTest {

    @Test
    void toDTOListShouldReturnEmptyListWhenInputIsEmpty() {
        // Arrange
        List<Genre> genres = List.of();
        US013GenreMapper mapper = new US013GenreMapper();

        // Act
        List<US013GenreDTO> result = mapper.toDTOList(genres);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOListShouldConvertEachGenreToDTO() {
        // Arrange
        Genre genreDouble1 = mock(Genre.class);
        Genre genreDouble2 = mock(Genre.class);

        GenreId idDouble1 = mock(GenreId.class);
        GenreId idDouble2 = mock(GenreId.class);

        when(idDouble1.getID()).thenReturn("1");
        when(idDouble2.getID()).thenReturn("2");

        when(genreDouble1.id()).thenReturn(idDouble1);
        when(genreDouble2.id()).thenReturn(idDouble2);

        when(genreDouble1.getGenreType()).thenReturn("Fantasy");
        when(genreDouble2.getGenreType()).thenReturn("Horror");

        List<Genre> genres = List.of(genreDouble1, genreDouble2);
        US013GenreMapper mapper = new US013GenreMapper();

        // Act
        List<US013GenreDTO> result = mapper.toDTOList(genres);

        // Assert
        assertEquals(2, result.size());

        assertEquals("1", result.get(0).genreId());
        assertEquals("Fantasy", result.get(0).genreName());

        assertEquals("2", result.get(1).genreId());
        assertEquals("Horror", result.get(1).genreName());
    }

    @Test
    void toDTOShouldConvertGenreToDTO() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        GenreId idDouble = mock(GenreId.class);

        when(idDouble.getID()).thenReturn("1");
        when(genreDouble.id()).thenReturn(idDouble);
        when(genreDouble.getGenreType()).thenReturn("Fantasy");

        US013GenreMapper mapper = new US013GenreMapper();

        // Act
        US013GenreDTO result = mapper.toDTO(genreDouble);

        // Assert
        assertEquals("1", result.genreId());
        assertEquals("Fantasy", result.genreName());
    }

    @Test
    void toDTOShouldThrowExceptionWhenGenreIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new US013GenreMapper().toDTO(null));
    }

    @Test
    void toDTOListShouldThrowExceptionWhenListIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new US013GenreMapper().toDTOList(null));
    }
}