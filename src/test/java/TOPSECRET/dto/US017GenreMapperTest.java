package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Unit tests for {@link US017GenreMapper} — US017.
 *
 * <p>Verifies that the mapper correctly converts {@link Genre} objects
 * into {@link US017GenreDTO}, and that invalid input is rejected.
 */
class US017GenreMapperTest {

    // ============================== toDTO(Genre) ==============================

    @Test
    void toDTO_shouldThrowWhenGenreIsNull() {
        assertThrows(NullPointerException.class,
                () -> US017GenreMapper.toDTO(null));
    }

    @Test
    void toDTO_shouldConvertSingleGenreToDTO() {
        // Arrange
        Genre genre = mock(Genre.class);
        when(genre.getGenreType()).thenReturn("Fiction");

        // Act
        US017GenreDTO result = US017GenreMapper.toDTO(genre);

        // Assert
        assertEquals("Fiction", result.name());
    }

    // ============================== toDTOList(List<Genre>) ==============================

    @Test
    void toDTOList_shouldThrowWhenGenreListIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> US017GenreMapper.toDTOList(null));
    }

    @Test
    void toDTOList_shouldReturnEmptyListWhenInputIsEmpty() {
        // Act
        List<US017GenreDTO> result = US017GenreMapper.toDTOList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOList_shouldConvertEachGenreToDTO() {
        // Arrange
        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);
        when(genre1.getGenreType()).thenReturn("Fiction");
        when(genre2.getGenreType()).thenReturn("History");

        // Act
        List<US017GenreDTO> result = US017GenreMapper.toDTOList(List.of(genre1, genre2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fiction", result.get(0).name());
        assertEquals("History", result.get(1).name());
    }
    // ============================== Constructor ==============================

    @Test
    void constructor_shouldThrowUnsupportedOperationException() throws Exception {
        Constructor<US017GenreMapper> constructor =
                US017GenreMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception =
                assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertInstanceOf(UnsupportedOperationException.class, exception.getCause());
    }
}
