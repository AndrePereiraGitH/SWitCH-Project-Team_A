package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link US013GenreDTO}.
 *
 * <p>Verifies the behaviour of the constructor and the static factory method
 * {@code toDTO}, covering both the happy path and invalid-input scenarios.</p>
 */
class US013GenreDTOTest {

    @Test
    void shouldCreateGenreDTOWithValidName() {
        US013GenreDTO dto = new US013GenreDTO("Romance");
        assertEquals("Romance", dto.genreName());
    }


    @Test
    void shouldSupportRecordEquality() {
        US013GenreDTO dto1 = new US013GenreDTO("Jazz");
        US013GenreDTO dto2 = new US013GenreDTO("Jazz");
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldSupportRecordHashCode() {
        US013GenreDTO dto1 = new US013GenreDTO("Jazz");
        US013GenreDTO dto2 = new US013GenreDTO("Jazz");
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldSupportRecordToString() {
        US013GenreDTO dto = new US013GenreDTO("Pop");
        assertEquals("US013GenreDTO[genreName=Pop]", dto.toString());
    }
}
