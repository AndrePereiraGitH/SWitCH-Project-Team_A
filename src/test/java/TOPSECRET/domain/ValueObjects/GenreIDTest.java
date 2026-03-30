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
        //Arrange
        String id = "1";

        //Act
        GenreID genreID = new GenreID(id);

        //Assert
        assertNotNull(genreID);
    }

    // getID() must return the value that was passed in
    @Test
    void shouldReturnCorrectID() {
        //Arrange
        String id = "genre-123";

        //Act
        GenreID genreID = new GenreID(id);

        //Assert
        assertEquals(id, genreID.getID());
    }

    // Null must be rejected
    @Test
    void shouldThrowWhenIDIsNull() {

        //Arrange
        String id = null;

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new GenreID(id));
    }

    // Blank string must be rejected
    @Test
    void shouldThrowWhenIDIsBlank() {

        //Arrange
        String id = "     ";

        //Act & Asser
        assertThrows(IllegalArgumentException.class, () -> new GenreID(id));
    }

    // Empty string must be rejected
    @Test
    void shouldThrowWhenIDIsEmpty() {
        //Arrange
        String id = "";

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new GenreID(id));
    }

    // Two GenreIDs with the same value must be equal
    @Test
    void shouldBeEqualWhenSameValue() {
        //Arrange
        String id = "1";

        //Act
        GenreID id1 = new GenreID("1");
        GenreID id2 = new GenreID("1");

        assertEquals(id1, id2);
    }

    // Two GenreIDs with different values must not be equal
    @Test
    void shouldNotBeEqualWhenDifferentValue() {
        //Arrange
        String firstId = "1";
        String secondId = "2";

        //Act
        GenreID id1 = new GenreID(firstId);
        GenreID id2 = new GenreID(secondId);

        //Assert
        assertNotEquals(id1, id2);
    }


}