package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class US026AuthorDTOTest {

    @Test
    void shouldThrow_whenIdIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new US026AuthorDTO(null, "José Saramago"));

        assertEquals("auctionId cannot be null or blank", ex.getMessage());
    }

    @Test
    void shouldThrow_whenIdIsBlank() {

        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new US026AuthorDTO("", "José Saramago"));

        assertEquals("auctionId cannot be null or blank", ex.getMessage());
    }

    @Test
    void shouldThrow_whenNameIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new US026AuthorDTO("authorId", null));

        assertEquals("name cannot be null or blank", ex.getMessage());
    }

    @Test
    void shouldThrow_whenNameIsBlank() {

        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new US026AuthorDTO("authorId", " "));

        assertEquals("name cannot be null or blank", ex.getMessage());
    }

    @Test
    void shuoldCreateValidDto_whenAllFieldsAreValid() {
        // act
        US026AuthorDTO dto = new US026AuthorDTO("authorId", "José Saramago");

        // assert
        assertNotNull(dto);
        assertEquals("authorId", dto.authorId());
        assertEquals("José Saramago", dto.name());
    }

}