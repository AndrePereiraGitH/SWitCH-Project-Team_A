
package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    @Test
    void shouldCreateWithValidGenre() {
        Genre genre = new Genre("Romance");

        assertEquals("Romance", genre.getGenreType());
    }

    @Test
    void genreNameCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(null));
    }

    @Test
    void genreNameCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(""));
    }

    @Test
    void genreNameCannotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(" "));
    }


}


