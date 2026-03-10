package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GenreDetailsDTO_US004.
 *
 * Validates:
 * - Successful creation
 * - Getter behavior
 * - Validation of null and blank values
 */
class GenreDetailsDTO_US004DependenciesTest {

    /**
     * Verifies successful creation with valid title.
     */
    @Test
    void shouldCreateDTOWithValidTitle() {

        // ARRANGE
        String expectedTitle = "Fantasy";

        // ACT
        GenreDetailsDTO_US004 dto =
                new GenreDetailsDTO_US004(expectedTitle);

        // ASSERT
        assertEquals(expectedTitle, dto.getTitle());
    }

    /**
     * Verifies exception when title is null.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new GenreDetailsDTO_US004(null));
    }

    /**
     * Verifies exception when title is blank.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsBlank() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new GenreDetailsDTO_US004(" "));
    }
}