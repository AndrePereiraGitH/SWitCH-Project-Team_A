package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class US021GenreDTOTest {

    // =====================
    // Null validation tests
    // =====================

    @Test
    void shouldThrowExceptionWhenGenreIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new US021GenreDTO(null, "Drama"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        GenreID genreId = new GenreID("drama");

        assertThrows(IllegalArgumentException.class, () ->
                new US021GenreDTO(genreId, null));
    }

    // =====================
    // Creation tests
    // =====================

    @Test
    void shouldCreateDTOSuccessfullyWithValidArguments() {
        GenreID genreId = new GenreID("drama");

        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        assertEquals(genreId, dto.genreId());
        assertEquals("Drama", dto.name());
    }

    // =====================
    // equals tests
    // =====================

    @Test
    void shouldReturnTrueWhenSameObject() {
        GenreID genreId = new GenreID("drama");
        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        assertTrue(dto.equals(dto));
    }

    @Test
    void shouldReturnTrueWhenAllFieldsAreEqual() {
        GenreID genreId = new GenreID("drama");
        US021GenreDTO dto1 = new US021GenreDTO(genreId, "Drama");
        US021GenreDTO dto2 = new US021GenreDTO(genreId, "Drama");

        assertTrue(dto1.equals(dto2));
    }

    @Test
    void shouldReturnFalseWhenNameIsDifferent() {
        GenreID genreId = new GenreID("drama");
        US021GenreDTO dto1 = new US021GenreDTO(genreId, "Drama");
        US021GenreDTO dto2 = new US021GenreDTO(genreId, "Fiction");

        assertFalse(dto1.equals(dto2));
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        GenreID genreId = new GenreID("drama");
        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        assertFalse(dto.equals(null));
    }

    // =====================
    // hashCode tests
    // =====================

    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {
        GenreID genreId = new GenreID("drama");
        US021GenreDTO dto1 = new US021GenreDTO(genreId, "Drama");
        US021GenreDTO dto2 = new US021GenreDTO(genreId, "Drama");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    // =====================
    // toString tests
    // =====================

    @Test
    void toStringShouldNotBeNull() {
        GenreID genreId = new GenreID("drama");
        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        assertNotNull(dto.toString());
    }

    @Test
    void toStringShouldContainAllFields() {
        GenreID genreId = new GenreID("drama");
        US021GenreDTO dto = new US021GenreDTO(genreId, "Drama");

        String result = dto.toString();

        assertTrue(result.contains("genreId"));
        assertTrue(result.contains("name"));
    }
}
