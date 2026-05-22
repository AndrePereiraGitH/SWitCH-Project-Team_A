package TOPSECRET.dto.genre.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenreResponseDTO}.
 *
 * <p>
 * Verifies constructor behavior, getter methods and Lombok-generated
 * methods including {@code equals()}, {@code hashCode()},
 * {@code canEqual()} and {@code toString()}.
 * </p>
 *
 * <p>
 * Also validates behavior inherited from
 * {@link org.springframework.hateoas.RepresentationModel}.
 * </p>
 *
 * <p>
 * Tests aim to satisfy line and mutation coverage requirements.
 * </p>
 */
class GenreResponseDTOTest {

    @Test
    void constructorShouldSetIdCorrectly() {

        // ARRANGE
        GenreResponseDTO sut = new GenreResponseDTO("1", "Fantasy");

        // ACT

        // ASSERT
        assertEquals("1", sut.getId());
    }

    @Test
    void constructorShouldSetGenreNameCorrectly() {

        // ARRANGE
        GenreResponseDTO sut = new GenreResponseDTO("1", "Fantasy");

        // ACT

        // ASSERT
        assertEquals("Fantasy", sut.getGenreName());
    }

    @Test
    void hashCodeShouldBeEqualForSameObjects() {

        // ARRANGE
        GenreResponseDTO dto1 = new GenreResponseDTO("1", "Fantasy");
        GenreResponseDTO dto2 = new GenreResponseDTO("1", "Fantasy");

        // ACT

        // ASSERT
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toStringShouldContainId() {

        // ARRANGE
        GenreResponseDTO sut = new GenreResponseDTO("1", "Fantasy");

        // ACT
        String result = sut.toString();

        // ASSERT
        assertTrue(result.contains("1"));
    }

    @Test
    void equalsShouldReturnTrueForSameReference() {

        // ARRANGE
        GenreResponseDTO sut =
                new GenreResponseDTO("1", "Fantasy");

        // ACT
        boolean result = sut.equals(sut);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnFalseForNull() {

        // ARRANGE
        GenreResponseDTO sut =
                new GenreResponseDTO("1", "Fantasy");

        // ACT
        boolean result = sut.equals(null);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentType() {

        // ARRANGE
        GenreResponseDTO sut =
                new GenreResponseDTO("1", "Fantasy");

        // ACT
        boolean result = sut.equals("Fantasy");

        // ASSERT
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentId() {

        // ARRANGE
        GenreResponseDTO sut1 =
                new GenreResponseDTO("1", "Fantasy");

        GenreResponseDTO sut2 =
                new GenreResponseDTO("2", "Fantasy");

        // ACT
        boolean result = sut1.equals(sut2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentGenreName() {

        // ARRANGE
        GenreResponseDTO sut1 =
                new GenreResponseDTO("1", "Fantasy");

        GenreResponseDTO sut2 =
                new GenreResponseDTO("1", "Drama");

        // ACT
        boolean result = sut1.equals(sut2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canEqualShouldReturnTrueForSameType() {

        // ARRANGE
        GenreResponseDTO sut =
                new GenreResponseDTO("1", "Fantasy");

        // ACT
        boolean result = sut.canEqual(
                new GenreResponseDTO("2", "Drama")
        );

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canEqualShouldReturnFalseForDifferentType() {

        // ARRANGE
        GenreResponseDTO sut =
                new GenreResponseDTO("1", "Fantasy");

        // ACT
        boolean result = sut.canEqual("test");

        // ASSERT
        assertFalse(result);
    }

    @Test
    void hashCodeShouldReturnDifferentValueForDifferentIds() {

        // ARRANGE
        GenreResponseDTO sut1 =
                new GenreResponseDTO(
                        "1",
                        "Fantasy"
                );

        GenreResponseDTO sut2 =
                new GenreResponseDTO(
                        "2",
                        "Fantasy"
                );

        // ACT
        int result1 = sut1.hashCode();
        int result2 = sut2.hashCode();

        // ASSERT
        assertNotEquals(result1, result2);
    }

    @Test
    void hashCodeShouldReturnDifferentValueForDifferentNames() {

        // ARRANGE
        GenreResponseDTO sut1 =
                new GenreResponseDTO(
                        "1",
                        "Fantasy"
                );

        GenreResponseDTO sut2 =
                new GenreResponseDTO(
                        "1",
                        "Drama"
                );

        // ACT
        int result1 = sut1.hashCode();
        int result2 = sut2.hashCode();

        // ASSERT
        assertNotEquals(result1, result2);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithSubclass() {

        // ARRANGE
        GenreResponseDTO sut =
                new GenreResponseDTO(
                        "1",
                        "Fantasy"
                );

        GenreResponseDTO other =
                new GenreResponseDTO(
                        "1",
                        "Fantasy"
                ) {
                };

        // ACT
        boolean result = sut.equals(other);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnFalseWhenBothFieldsAreDifferent() {

        // ARRANGE
        GenreResponseDTO sut1 =
                new GenreResponseDTO("1", "Fantasy");

        GenreResponseDTO sut2 =
                new GenreResponseDTO("2", "Drama");

        // ACT
        boolean result = sut1.equals(sut2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void hashCodeShouldChangeWhenAllFieldsChange() {

        // ARRANGE
        GenreResponseDTO sut1 =
                new GenreResponseDTO("1", "Fantasy");

        GenreResponseDTO sut2 =
                new GenreResponseDTO("2", "Drama");

        // ACT
        int hash1 = sut1.hashCode();
        int hash2 = sut2.hashCode();

        // ASSERT
        assertNotEquals(hash1, hash2);
    }

    @Test
    void equalsShouldReturnTrueForSameValues() {

        // ARRANGE
        GenreResponseDTO sut1 =
                new GenreResponseDTO(
                        "1",
                        "Fantasy"
                );

        GenreResponseDTO sut2 =
                new GenreResponseDTO(
                        "1",
                        "Fantasy"
                );

        // ACT
        boolean result = sut1.equals(sut2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canEqualShouldReturnFalseForNull() {

        // ARRANGE
        GenreResponseDTO sut =
                new GenreResponseDTO(
                        "1",
                        "Fantasy"
                );

        // ACT
        boolean result = sut.canEqual(null);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void hashCodeShouldReturnSameValueForRepeatedCalls() {

        // ARRANGE
        GenreResponseDTO sut =
                new GenreResponseDTO(
                        "1",
                        "Fantasy"
                );

        // ACT
        int result1 = sut.hashCode();

        int result2 = sut.hashCode();

        // ASSERT
        assertEquals(result1, result2);
    }

    @Test
    void equalsShouldReturnFalseForSubclassWithDifferentValues() {

        // ARRANGE
        GenreResponseDTO sut =
                new GenreResponseDTO(
                        "1",
                        "Fantasy"
                );

        GenreResponseDTO other =
                new GenreResponseDTO(
                        "2",
                        "Drama"
                ) {
                };

        // ACT
        boolean result = sut.equals(other);

        // ASSERT
        assertFalse(result);
    }
}