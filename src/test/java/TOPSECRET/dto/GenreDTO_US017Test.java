package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenreDTO_US017} — US017.
 */
class GenreDTO_US017Test {

    // ============================== construction ==============================

    @Test
    void shouldCreateDTOSuccessfully_whenNameIsValid() {
        // Arrange & Act
        GenreDTO_US017 dto = new GenreDTO_US017("Fiction");

        // Assert
        assertEquals("Fiction", dto.name());
    }

    @Test
    void shouldThrowNullPointerException_whenNameIsNull() {
        // Act & Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new GenreDTO_US017(null));

        assertEquals("Name cannot be null.", ex.getMessage());
    }

    // ============================== record contract ==============================

    @Test
    void shouldBeEqual_whenTwoDTOsHaveTheSameName() {
        // Arrange
        GenreDTO_US017 dto1 = new GenreDTO_US017("Fiction");
        GenreDTO_US017 dto2 = new GenreDTO_US017("Fiction");

        // Act & Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual_whenDTOsHaveDifferentNames() {
        // Arrange
        GenreDTO_US017 dto1 = new GenreDTO_US017("Fiction");
        GenreDTO_US017 dto2 = new GenreDTO_US017("History");

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCode_whenTwoDTOsAreEqual() {
        // Arrange
        GenreDTO_US017 dto1 = new GenreDTO_US017("Fiction");
        GenreDTO_US017 dto2 = new GenreDTO_US017("Fiction");

        // Act & Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldReturnMeaningfulToString() {
        // Arrange
        GenreDTO_US017 dto = new GenreDTO_US017("Fiction");

        // Act
        String result = dto.toString();

        // Assert
        assertTrue(result.contains("Fiction"));
    }
}
