package TOPSECRET.dto.genre;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.dto.genre.response.GenreResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenreMapper}.
 *
 * <p>This test class verifies the correct behavior of the mapping logic
 * between the {@link Genre} domain model and its DTO representation
 * {@link GenreResponseDTO}.</p>
 *
 * <p>It ensures that:</p>
 * <ul>
 *     <li>Domain objects are correctly converted into DTOs</li>
 *     <li>Lists of domain objects are properly mapped</li>
 *     <li>Null inputs are properly handled with exceptions</li>
 * </ul>
 *
 * <p>All tests follow the AAA (Arrange, Act, Assert) pattern and aim
 * to achieve full line and mutation coverage.</p>
 */
class GenreMapperTest {

    @Test
    void toDTO_shouldMapIdCorrectly() {
        // ARRANGE
        Genre genre = mock(Genre.class);
        GenreId genreId = mock(GenreId.class);

        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("1");
        when(genre.getGenreType()).thenReturn("Drama");

        // ACT
        GenreResponseDTO result = GenreMapper.toDTO(genre);

        // ASSERT
        assertEquals("1", result.getId());
    }

    @Test
    void toDTO_shouldMapGenreTypeCorrectly() {
        // ARRANGE
        Genre genre = mock(Genre.class);
        GenreId genreId = mock(GenreId.class);

        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("10");
        when(genre.getGenreType()).thenReturn("Action");

        // ACT
        GenreResponseDTO result = GenreMapper.toDTO(genre);

        // ASSERT
        assertEquals("Action", result.getGenreName());
    }

    @Test
    void toDTO_shouldThrowException_whenGenreIsNull() {
        // ARRANGE + ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> GenreMapper.toDTO(null));
    }

    @Test
    void toDTOs_shouldMapListCorrectly() {
        // ARRANGE
        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);

        GenreId id1 = mock(GenreId.class);
        GenreId id2 = mock(GenreId.class);

        when(genre1.id()).thenReturn(id1);
        when(id1.getID()).thenReturn("1");
        when(genre1.getGenreType()).thenReturn("Drama");

        when(genre2.id()).thenReturn(id2);
        when(id2.getID()).thenReturn("2");
        when(genre2.getGenreType()).thenReturn("Comedy");

        List<Genre> list = List.of(genre1, genre2);

        // ACT
        List<GenreResponseDTO> result = GenreMapper.toDTOs(list);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void toDTOs_shouldThrowException_whenListIsNull() {
        // ARRANGE + ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> GenreMapper.toDTOs(null));
    }

    @Test
    void toDTOs_shouldMapFirstElementCorrectly() {
        // ARRANGE
        Genre genre = mock(Genre.class);
        GenreId genreId = mock(GenreId.class);

        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("99");
        when(genre.getGenreType()).thenReturn("Horror");

        List<Genre> list = List.of(genre);

        // ACT
        List<GenreResponseDTO> result = GenreMapper.toDTOs(list);

        // ASSERT
        assertEquals("99", result.get(0).getId());
    }
}