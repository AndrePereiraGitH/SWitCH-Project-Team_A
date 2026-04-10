package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US017GenreDTO} — US017.
 */
class US017GenreDTOTest {

    // ============================== construction ==============================

    @Test
    void shouldCreateDTOSuccessfully_whenNameIsValid() {
        // Arrange & Act
        US017GenreDTO dto = new US017GenreDTO("Fiction");

        // Assert
        assertEquals("Fiction", dto.name());
    }

    @Test
    void shouldThrowNullPointerException_whenNameIsNull() {
        // Act & Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new US017GenreDTO(null));

        assertEquals("Name cannot be null.", ex.getMessage());
    }

    // ============================== record contract ==============================

    @Test
    void shouldBeEqual_whenTwoDTOsHaveTheSameName() {
        // Arrange
        US017GenreDTO dto1 = new US017GenreDTO("Fiction");
        US017GenreDTO dto2 = new US017GenreDTO("Fiction");

        // Act & Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual_whenDTOsHaveDifferentNames() {
        // Arrange
        US017GenreDTO dto1 = new US017GenreDTO("Fiction");
        US017GenreDTO dto2 = new US017GenreDTO("History");

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCode_whenTwoDTOsAreEqual() {
        // Arrange
        US017GenreDTO dto1 = new US017GenreDTO("Fiction");
        US017GenreDTO dto2 = new US017GenreDTO("Fiction");

        // Act & Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldReturnMeaningfulToString() {
        // Arrange
        US017GenreDTO dto = new US017GenreDTO("Fiction");

        // Act
        String result = dto.toString();

        // Assert
        assertTrue(result.contains("Fiction"));
    }
}
