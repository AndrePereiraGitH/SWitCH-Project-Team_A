package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link GenreDTO_US017} — US017.
 *
 * <p>These tests use a <b>real {@link Genre} object</b> instead of a mock.
 * This checks that the conversion from a real {@code Genre} to a {@code GenreDTO_US017}
 * works correctly end-to-end — not just in isolation.
 *
 * <p>In US017, the controller needs to give the UI a list of genres to choose from.
 * It fetches real {@code Genre} objects from the repository and converts each one
 * into a {@code GenreDTO_US017} using the static {@code toDTO()} method.
 * These tests verify that conversion works and that invalid input is rejected.
 */
class GenreDTO_US017DependenciesTest {


    // ============================== US017 ==============================

    // The main purpose of this DTO is to carry the genre name to the UI.
    // We pass a real Genre object and check that the DTO has the correct name.
    // We also check that the DTO itself is not null — toDTO() must always return an object.
    @Test
    void shouldConvertGenreToDTO() {
        // Arrange — a real Genre with name "Fiction"
        Genre genre = new GenreFactory().create("Fiction");

        // Act — convert the Genre into a DTO using the static factory method
        GenreDTO_US017 dto = GenreDTO_US017.toDTO(genre);

        // Assert — the DTO must exist and carry the correct name
        assertNotNull(dto);
        assertEquals("Fiction", dto.name);
    }

    // The toDTO() method must reject null — it cannot convert something that does not exist.
    // Calling genre.getGenreType() on a null would cause a NullPointerException,
    // so the method checks for null first and throws a clear exception instead.
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Act & Assert — null genre must throw before any conversion is attempted
        assertThrows(IllegalArgumentException.class,
                () -> GenreDTO_US017.toDTO(null));
    }

    // ============================== end US017 ==============================

}