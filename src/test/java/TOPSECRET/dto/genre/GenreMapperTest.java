package TOPSECRET.dto.genre;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.dto.genre.response.GenreResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenreMapperTest {

    @Test
    void toDTOShouldReturnIdCorrectly() {

        // ARRANGE
        GenreMapper sut = new GenreMapper();
        Genre genre = mock(Genre.class);
        GenreId id = mock(GenreId.class);

        when(genre.id()).thenReturn(id);
        when(id.getID()).thenReturn("1");
        when(genre.getGenreType()).thenReturn("Fantasy");

        // ACT
        GenreResponseDTO result = sut.toDTO(genre);

        // ASSERT
        assertEquals("1", result.getId());
    }

    @Test
    void toDTOShouldReturnGenreNameCorrectly() {

        // ARRANGE
        GenreMapper sut = new GenreMapper();
        Genre genre = mock(Genre.class);
        GenreId id = mock(GenreId.class);

        when(genre.id()).thenReturn(id);
        when(id.getID()).thenReturn("1");
        when(genre.getGenreType()).thenReturn("Fantasy");

        // ACT
        GenreResponseDTO result = sut.toDTO(genre);

        // ASSERT
        assertEquals("Fantasy", result.getGenreName());
    }

    @Test
    void toDTOShouldThrowWhenGenreIsNull() {

        // ARRANGE
        GenreMapper sut = new GenreMapper();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.toDTO(null)
        );
    }

    @Test
    void toDTOsShouldReturnListSizeCorrectly() {

        // ARRANGE
        GenreMapper sut = new GenreMapper();

        Genre genre = mock(Genre.class);
        GenreId id = mock(GenreId.class);

        when(genre.id()).thenReturn(id);
        when(id.getID()).thenReturn("1");
        when(genre.getGenreType()).thenReturn("Fantasy");

        List<Genre> genres = List.of(genre);

        // ACT
        List<GenreResponseDTO> result = sut.toDTOs(genres);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void toDTOsShouldThrowWhenListIsNull() {

        // ARRANGE
        GenreMapper sut = new GenreMapper();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.toDTOs(null)
        );
    }
}