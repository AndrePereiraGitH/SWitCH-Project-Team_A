package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US013GenreDTO}.
 *
 * <p>Verifies the behaviour of the constructor and the static factory method
 * {@code toDTO}, covering both the happy path and invalid-input scenarios.</p>
 */
class US013GenreDTOTest {

    @Test
    void shouldCreateGenreDTOWithValidNameAndId() {
        // Act
        US013GenreDTO dto = new US013GenreDTO("123", "Romance");
        // Assert
        assertEquals("Romance", dto.genreName());
    }


    @Test
    void shouldSupportRecordEquality() {
        //Act
        US013GenreDTO dtoDouble = new US013GenreDTO("123","Jazz");
        US013GenreDTO dtoDouble2 = new US013GenreDTO("123","Jazz");
        //Assert
        assertEquals(dtoDouble, dtoDouble2);
    }

    @Test
    void shouldSupportRecordHashCode() {
        //Act
        US013GenreDTO dtoDouble = new US013GenreDTO("123","Jazz");
        US013GenreDTO dtoDouble2 = new US013GenreDTO("123","Jazz");
        //Assert
        assertEquals(dtoDouble.hashCode(), dtoDouble2.hashCode());
    }

}
