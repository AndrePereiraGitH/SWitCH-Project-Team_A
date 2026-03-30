package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GenreDTO_US017} — US017.
 *
 * <p>These tests use a <b>mock {@link Genre}</b> instead of a real one.
 * This means the test only checks the logic inside {@code GenreDTO_US017.toDTO()} —
 * not the behaviour of the {@code Genre} class itself.
 * If a test fails here, the bug is in the DTO conversion, not in {@code Genre}.
 *
 * <p>We use Mockito to create a fake {@code Genre} and tell it what to return
 * when {@code getGenreType()} is called. This way we control the input
 * and can check the output without depending on the real {@code Genre} implementation.
 *
 * <p>In US017, the controller calls {@code toDTO()} on every genre from the repository
 * before sending the list to the UI. These tests verify that the method works correctly
 * and rejects invalid input.
 */
class GenreDTO_US017Test {


    // ============================== US017 ==============================

    // The toDTO() method must reject null — it cannot convert something that does not exist.
    // No mock is needed here because the null check happens before getGenreType() is called.
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Act & Assert — null must be rejected immediately, before any conversion runs
        assertThrows(IllegalArgumentException.class,
                () -> GenreDTO_US017.toDTO(null));
    }

    // The main job of toDTO() is to read the genre name and put it into a DTO.
    // We use a mock Genre that returns "Fiction" when getGenreType() is called.
    // This lets us test only the DTO conversion logic — not what Genre does internally.
    // If the DTO has the right name, toDTO() read from the Genre and stored it correctly.
    @Test
    void shouldConvertGenreToDTO() {
        // Arrange — mock Genre that returns "Fiction" when asked for its type
        Genre mockGenre = mock(Genre.class);
        when(mockGenre.getGenreType()).thenReturn("Fiction");

        // Act — convert the mock Genre into a DTO
        GenreDTO_US017 result = GenreDTO_US017.toDTO(mockGenre);

        // Assert — the DTO must exist and carry the correct name from the mock
        assertNotNull(result);
        assertEquals("Fiction", result.name);
    }

    // ============================== end US017 ==============================

}