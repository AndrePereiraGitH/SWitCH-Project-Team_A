package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Genre}.
 * Genre has no dependencies, so no mocks are needed — just direct object creation.
 */
class GenreTest {

    // --------------------------------- US003 starts ---------------------------------

    // A valid genre name must be accepted and stored correctly
    @Test
    void shouldCreateWithValidGenre() {
        Genre genre = new Genre("Romance");

        assertEquals("Romance", genre.getGenreType());
    }

    // Null must be rejected — a genre without a name makes no sense
    @Test
    void genreTypeCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(null));
    }

    // Empty string must be rejected
    @Test
    void genreTypeCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(""));
    }

    // Blank string (only spaces) must also be rejected
    @Test
    void genreTypeCannotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(" "));
    }

    // --------------------------------- US003 ends ---------------------------------

    // US004 — two genres with the same name (different case) must be considered equal
    @Test
    void genresWithSameTypeShouldBeEqualIgnoringCase() {
        Genre g1 = new Genre("Fantasy");
        Genre g2 = new Genre("fantasy");

        assertEquals(g1, g2);
    }

    // US004 — equal genres must have the same hash code (required by Java contract)
    @Test
    void equalGenresShouldHaveSameHashCode() {
        Genre g1 = new Genre("SciFi");
        Genre g2 = new Genre("scifi");

        assertEquals(g1.hashCode(), g2.hashCode());
    }
}