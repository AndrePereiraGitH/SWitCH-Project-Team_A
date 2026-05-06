package TOPSECRET.domain.Genre;

import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Genre}.
 * Genre has no dependencies, so no mocks are needed — just direct object creation.
 */
class GenreTest {

    // --------------------------------- US003 starts ---------------------------------

    @Test
    void shouldCreateWithValidGenre() {
        // Arrange
        String genreType = "Romance";

        //Act
        Genre genre = new Genre(genreType);

        // Assert
        assertEquals("Romance", genre.getGenreType());
    }

    @Test
    void genreTypeCannotBeNull() {
        // Arrange
        String genreType = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Genre(null));
    }

    @Test
    void genreTypeCannotBeEmpty() {
        //Arrange
        String genreType = "";

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Genre(""));
    }

    @Test
    void genreTypeCannotBeBlank() {
        //Arrange
        String genreType = " ";

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Genre(" "));
    }

    // --------------------------------- US003 ends ---------------------------------

     @Test
    void genreShouldEqualItself() {
        //Arrange
        Genre g1 = new Genre("Fantasy");
        //Act & Assert
        assertEquals(g1, g1);
    }

    // equals() — two genres with the same type ARE now equal (ID is based on genreType)
    @Test
    void genresWithSameTypeShouldBeEqual() {
        // Arrange
        Genre g1 = new Genre("Fantasy");
        Genre g2 = new Genre("Fantasy");

        // Assert
        assertEquals(g1, g2);
    }

    @Test
    void genresWithDifferentTypeShouldNotBeEqual() {
        //Arrange
        Genre g1 = new Genre("Fantasy");
        Genre g2 = new Genre("Romance");

        //Assert
        assertNotEquals(g1, g2);
    }

    // US004 — two genres with the same name (different case) must be considered equal
    @Test
    void genresWithSameTypeShouldBeEqualIgnoringCase() {
        //Arrange
        Genre g1 = new Genre("Fantasy");
        Genre g2 = new Genre("fantasy");

        //Assert
        assertTrue(g1.sameAs(g2));
        assertEquals(g1, g2);
    }

    // US004 — equal genres must have the same hash code (required by Java contract)
    @Test
    void equalGenresShouldHaveSameHashCode() {
        // Arrange
        Genre g1 = new Genre("SciFi");
        Genre g2 = new Genre("SciFi");

        // Assert
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    void equals_shouldReturnFalse_forNull_orDifferentObjects() {
        //Arrange
        Genre genre = new Genre("Fiction");

        //Assert
        assertFalse(genre.equals(null));
        assertFalse(genre.equals("not a genre"));
    }

    // Kills NO_COVERAGE on id()
    @Test
    void idShouldReturnGenreID() {
        // Arrange
        Genre genre = new Genre("Fantasy");

        // Act
        GenreId id = genre.id();

        // Assert
        assertNotNull(id);
    }

    // Kills SURVIVED on sameAs() returns true
    @Test
    void sameAsShouldReturnFalseForDifferentGenres() {
        // Arrange
        Genre genre1 = new Genre("Fantasy");
        Genre genre2 = new Genre("Science Fiction");

        // Act & Assert
        assertFalse(genre1.sameAs(genre2));
    }

    // Kills NO_COVERAGE on sameAs() line 48
    @Test
    void sameAsShouldReturnFalseForNonGenreObject() {
        // Arrange
        Genre genre = new Genre("Fantasy");

        // Act & Assert
        assertFalse(genre.sameAs("not a genre"));
    }
    @Test
    void hashCodeShouldNotBeZeroForValidGenre() {
        // Arrange
        Genre genre = new Genre("Fantasy");

        // Act
        int hashCode = genre.hashCode();

        // Assert
        assertNotEquals(0, hashCode);
    }

    @Test
    void hashCodeShouldBeConsistentWithEquals() {
        // Arrange
        Genre genre1 = new Genre("Fantasy");
        Genre genre2 = new Genre("Fantasy");
        Genre genre3 = new Genre("Science Fiction");

        // Act & Assert
        assertEquals(genre1.hashCode(), genre2.hashCode());
        assertNotEquals(genre1.hashCode(), genre3.hashCode());
    }
}