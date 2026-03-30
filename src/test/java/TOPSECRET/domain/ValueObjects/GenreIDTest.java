package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenreID}.
 * Verifies that GenreID is created correctly and that equality is based on value.
 */
class GenreIDTest {

    // A valid ID must be accepted and stored correctly
    @Test
    void shouldCreateGenreIDWithValidValue() {
        GenreID genreID = new GenreID("1");

        assertNotNull(genreID);
    }

    // getID() must return the value that was passed in
    @Test
    void shouldReturnCorrectID() {
        GenreID genreID = new GenreID("genre-123");

        assertEquals("genre-123", genreID.getID());
    }

    // Null must be rejected
    @Test
    void shouldThrowWhenIDIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new GenreID(null));
    }

    // Blank string must be rejected
    @Test
    void shouldThrowWhenIDIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new GenreID("   "));
    }

    // Empty string must be rejected
    @Test
    void shouldThrowWhenIDIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new GenreID(""));
    }

    // Two GenreIDs with the same value must be equal
    @Test
    void shouldBeEqualWhenSameValue() {
        GenreID id1 = new GenreID("1");
        GenreID id2 = new GenreID("1");

        assertEquals(id1, id2);
    }

    // Two GenreIDs with different values must not be equal
    @Test
    void shouldNotBeEqualWhenDifferentValue() {
        GenreID id1 = new GenreID("1");
        GenreID id2 = new GenreID("2");

        assertNotEquals(id1, id2);
    }


}