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
        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);

        GenreId id1 = mock(GenreId.class);
        GenreId id2 = mock(GenreId.class);

        when(id1.getID()).thenReturn("1");
        when(id2.getID()).thenReturn("2");

        when(genre1.id()).thenReturn(id1);
        when(genre2.id()).thenReturn(id2);

        when(genre1.getGenreType()).thenReturn("Fantasy");
        when(genre2.getGenreType()).thenReturn("Horror");

        List<Genre> genres = List.of(genre1, genre2);
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
        Genre genre = mock(Genre.class);
        GenreId id = mock(GenreId.class);

        when(id.getID()).thenReturn("1");
        when(genre.id()).thenReturn(id);
        when(genre.getGenreType()).thenReturn("Fantasy");

        US013GenreMapper mapper = new US013GenreMapper();

        // Act
        US013GenreDTO result = mapper.toDTO(genre);

        // Assert
        assertEquals("1", result.genreId());
        assertEquals("Fantasy", result.genreName());
    }

    @Test
    void toDTOListShouldThrowExceptionWhenListIsNull() {
        // Arrange
        US013GenreMapper mapper = new US013GenreMapper();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTOList(null));
    }

    @Test
    void toDTOShouldThrowExceptionWhenGenreIsNull() {
        // Arrange
        US013GenreMapper mapper = new US013GenreMapper();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    void toDTOShouldThrowExceptionWhenGenreIdIsNull() {
        // Arrange
        Genre genre = mock(Genre.class);

        when(genre.id()).thenReturn(null);

        US013GenreMapper mapper = new US013GenreMapper();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTO(genre));
    }
}