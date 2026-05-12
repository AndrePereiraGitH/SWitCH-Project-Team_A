package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenreId}.
 * Verifies that GenreID is created correctly and that equality is based on value.
 */
class GenreIDTest {

    // A valid ID must be accepted and stored correctly
    @Test
    void shouldCreateGenreIDWithValidValue() {
        //Arrange
        String id = "1";

        //Act
        GenreId genreID = new GenreId(id);

        //Assert
        assertNotNull(genreID);
    }

    // getID() must return the value that was passed in
    @Test
    void shouldReturnCorrectID() {
        //Arrange
        String id = "genre-123";

        //Act
        GenreId genreID = new GenreId(id);

        //Assert
        assertEquals(id, genreID.getID());
    }

    // Null must be rejected
    @Test
    void shouldThrowWhenIDIsNull() {

        //Arrange
        String id = null;

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new GenreId(id));
    }

    // Blank string must be rejected
    @Test
    void shouldThrowWhenIDIsBlank() {

        //Arrange
        String id = "     ";

        //Act & Asser
        assertThrows(IllegalArgumentException.class, () -> new GenreId(id));
    }

    // Empty string must be rejected
    @Test
    void shouldThrowWhenIDIsEmpty() {
        //Arrange
        String id = "";

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new GenreId(id));
    }

    @Test
    void shouldNotBeEqualToNull() {
        // ARRANGE
        GenreId genreId = new GenreId("1");

        // ACT & ASSERT
        assertNotEquals(null, genreId);
    }

    @Test
    void shouldNotBeEqualToDifferentObjectType() {

        // ARRANGE
        GenreId genreId = new GenreId("1");

        // ACT & ASSERT
        assertNotEquals("1", genreId);
    }

    // Two GenreIDs with the same value must be equal
    @Test
    void shouldBeEqualWhenSameValue() {
        //Arrange
        String id = "1";

        //Act
        GenreId id1 = new GenreId("1");
        GenreId id2 = new GenreId("1");

        assertEquals(id1, id2);
    }

    // Two GenreIDs with different values must not be equal
    @Test
    void shouldNotBeEqualWhenDifferentValue() {
        //Arrange
        String firstId = "1";
        String secondId = "2";

        //Act
        GenreId id1 = new GenreId(firstId);
        GenreId id2 = new GenreId(secondId);

        //Assert
        assertNotEquals(id1, id2);
    }


}