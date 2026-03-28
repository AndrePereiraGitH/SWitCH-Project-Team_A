package TOPSECRET.domain.ValueObjects;

import TOPSECRET.domain.Author.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorIdTest {


    // ---- constructor
    @Test
    void testConstructor() {
        // arrange + act
        AuthorId authorId = new AuthorId("1");
        //assert
        assertNotNull(authorId);
    }

    // ------ Get

    @Test
    void getId_shouldReturnNotNull() {
       // arrange
        AuthorId authorId = new AuthorId("1");

        // act + assert
        assertNotNull(authorId.getId());
        assertEquals(authorId.getId(), authorId.getId());
    }


    // ----- Equals

    @Test
    void equals_shouldReturnTrue_whenSameID() {
       // arrange
        AuthorId id1 = new AuthorId("1");
        AuthorId id2 = new AuthorId("1");

        // assert
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentIds() {
        // arrange
        AuthorId id1 = new AuthorId("1");
        AuthorId id2 = new AuthorId("2");
        // assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentObjects() {
        // arrange
        AuthorId id = new AuthorId("1");
        // assert
        assertNotEquals(id, "not an authorID");
    }

    @Test
    void equals_shouldReturnFalse_whenComparedToNull() {
        // arrange
        AuthorId id = new AuthorId("1");
        // assert
        assertNotEquals(id, null);
    }

    @Test
    void equals_shouldThrow_whenNullObject() {
        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new AuthorId(null));

        assertEquals("AuthorId must be a non empty String", ex.getMessage());
    }



}