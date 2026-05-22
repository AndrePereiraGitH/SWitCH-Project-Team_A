package TOPSECRET.dto.genre.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenreRequestDTO}.
 *
 * <p>
 * Verifies constructor behavior, getter methods and Lombok-generated
 * methods such as {@code equals()}, {@code hashCode()},
 * {@code canEqual()} and {@code toString()}.
 * </p>
 *
 * <p>
 * Tests are designed to ensure full line and mutation coverage.
 * </p>
 */
class GenreRequestDTOTest {

    @Test
    void constructorShouldSetGenreNameCorrectly() {

        // ARRANGE
        GenreRequestDTO sut =
                new GenreRequestDTO(
                        "Fantasy"
                );

        // ACT

        // ASSERT
        assertEquals(
                "Fantasy",
                sut.getGenreName()
        );
    }

    @Test
    void equalsShouldReturnTrueForSameReference() {

        // ARRANGE
        GenreRequestDTO sut =
                new GenreRequestDTO(
                        "Fantasy"
                );

        // ACT
        boolean result =
                sut.equals(sut);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnTrueForEqualObjects() {

        // ARRANGE
        GenreRequestDTO sut1 =
                new GenreRequestDTO(
                        "Fantasy"
                );

        GenreRequestDTO sut2 =
                new GenreRequestDTO(
                        "Fantasy"
                );

        // ACT
        boolean result =
                sut1.equals(sut2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnFalseForNull() {

        // ARRANGE
        GenreRequestDTO sut =
                new GenreRequestDTO(
                        "Fantasy"
                );

        // ACT
        boolean result =
                sut.equals(null);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentType() {

        // ARRANGE
        GenreRequestDTO sut =
                new GenreRequestDTO(
                        "Fantasy"
                );

        // ACT
        boolean result =
                sut.equals(
                        "Fantasy"
                );

        // ASSERT
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentGenreName() {

        // ARRANGE
        GenreRequestDTO sut1 =
                new GenreRequestDTO(
                        "Fantasy"
                );

        GenreRequestDTO sut2 =
                new GenreRequestDTO(
                        "Drama"
                );

        // ACT
        boolean result =
                sut1.equals(sut2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnTrueWhenComparedWithSubclass() {

        // ARRANGE
        GenreRequestDTO sut =
                new GenreRequestDTO(
                        "Fantasy"
                );

        GenreRequestDTO other =
                new GenreRequestDTO(
                        "Fantasy"
                ) {
                };

        // ACT
        boolean result =
                sut.equals(other);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canEqualShouldReturnTrueForSameType() {

        // ARRANGE
        GenreRequestDTO sut =
                new GenreRequestDTO(
                        "Fantasy"
                );

        // ACT
        boolean result =
                sut.canEqual(
                        new GenreRequestDTO(
                                "Drama"
                        )
                );

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canEqualShouldReturnFalseForDifferentType() {

        // ARRANGE
        GenreRequestDTO sut =
                new GenreRequestDTO(
                        "Fantasy"
                );

        // ACT
        boolean result =
                sut.canEqual(
                        "test"
                );

        // ASSERT
        assertFalse(result);
    }

    @Test
    void hashCodeShouldReturnSameValueForEqualObjects() {

        // ARRANGE
        GenreRequestDTO sut1 =
                new GenreRequestDTO(
                        "Fantasy"
                );

        GenreRequestDTO sut2 =
                new GenreRequestDTO(
                        "Fantasy"
                );

        // ACT
        int result1 =
                sut1.hashCode();

        int result2 =
                sut2.hashCode();

        // ASSERT
        assertEquals(
                result1,
                result2
        );
    }

    @Test
    void hashCodeShouldReturnDifferentValueForDifferentObjects() {

        // ARRANGE
        GenreRequestDTO sut1 =
                new GenreRequestDTO(
                        "Fantasy"
                );

        GenreRequestDTO sut2 =
                new GenreRequestDTO(
                        "Drama"
                );

        // ACT
        int result1 =
                sut1.hashCode();

        int result2 =
                sut2.hashCode();

        // ASSERT
        assertNotEquals(
                result1,
                result2
        );
    }

    @Test
    void toStringShouldContainGenreName() {

        // ARRANGE
        GenreRequestDTO sut =
                new GenreRequestDTO(
                        "Fantasy"
                );

        // ACT
        String result =
                sut.toString();

        // ASSERT
        assertTrue(
                result.contains(
                        "Fantasy"
                )
        );
    }
}