
package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    @Test
    void shouldCreateWithValidGenre() {
        Genre genre = new Genre("Romance");

        assertEquals("Romance", genre.getTitle());
    }

    @Test
    void genreTypeCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(null));
    }

    @Test
    void genreTypeCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(""));
    }

    @Test
    void genreTypeCannotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(" "));
    }


    //US004
    @Test
    void genresWithSameTypeShouldBeEqualIgnoringCase() {
        Genre g1 = new Genre("Fantasy");
        Genre g2 = new Genre("fantasy");

        assertEquals(g1, g2);
    }

    @Test
    void equalGenresShouldHaveSameHashCode() {
        Genre g1 = new Genre("SciFi");
        Genre g2 = new Genre("scifi");

        assertEquals(g1.hashCode(), g2.hashCode());
    }
}


