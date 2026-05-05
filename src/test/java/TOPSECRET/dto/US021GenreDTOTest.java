package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US021GenreDTO}.
 */
class US021GenreDTOTest {

    // =====================
    // Null validation tests
    // =====================

    @Test
    void shouldThrowExceptionWhenGenreIdIsNull() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021GenreDTO(null, "Drama"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // Arrange
        GenreId genreId = new GenreId("drama");

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021GenreDTO(genreId, null));
    }

    // =====================
    // Creation tests
    // =====================

    @Test
    void shouldCreateDTOSuccessfullyWithValidArguments() {
        // Arrange
        GenreId genreId = new GenreId("drama");

        // Act
        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        // Assert
        assertEquals(genreId, dto.genreId());
        assertEquals("Drama", dto.name());
    }

    // =====================
    // equals tests
    // =====================

    @Test
    void shouldReturnTrueWhenSameObject() {
        // Arrange
        GenreId genreId = new GenreId("drama");
        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        // Act + Assert
        assertTrue(dto.equals(dto));
    }

    @Test
    void shouldReturnTrueWhenAllFieldsAreEqual() {
        // Arrange
        GenreId genreId = new GenreId("drama");
        US021GenreDTO dto1 = new US021GenreDTO(genreId, "Drama");
        US021GenreDTO dto2 = new US021GenreDTO(genreId, "Drama");

        // Act + Assert
        assertTrue(dto1.equals(dto2));
    }

    @Test
    void shouldReturnFalseWhenNameIsDifferent() {
        // Arrange
        GenreId genreId = new GenreId("drama");
        US021GenreDTO dto1 = new US021GenreDTO(genreId, "Drama");
        US021GenreDTO dto2 = new US021GenreDTO(genreId, "Fiction");

        // Act + Assert
        assertFalse(dto1.equals(dto2));
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        // Arrange
        GenreId genreId = new GenreId("drama");
        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        // Act + Assert
        assertFalse(dto.equals(null));
    }

    // =====================
    // hashCode tests
    // =====================

    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {
        // Arrange
        GenreId genreId = new GenreId("drama");
        US021GenreDTO dto1 = new US021GenreDTO(genreId, "Drama");
        US021GenreDTO dto2 = new US021GenreDTO(genreId, "Drama");

        // Act + Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    // =====================
    // toString tests
    // =====================

    @Test
    void toStringShouldNotBeNull() {
        // Arrange
        GenreId genreId = new GenreId("drama");
        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        // Act
        String result = dto.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    void toStringShouldContainAllFields() {
        // Arrange
        GenreId genreId = new GenreId("drama");
        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        // Act
        String result = dto.toString();

        // Assert
        assertTrue(result.contains("genreId"));
        assertTrue(result.contains("name"));
    }
}
