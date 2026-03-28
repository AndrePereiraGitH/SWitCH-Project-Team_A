package TOPSECRET.domain.ValueObjects;

import TOPSECRET.domain.Author.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorIdTest {

    @Test
    void testConstructor() {
        AuthorId authorId = new AuthorId("1");

        assertNotNull(authorId);
    }

    @Test
    void equals_shouldReturnTrue_whenSameID() {
        AuthorId id1 = new AuthorId("1");
        AuthorId id2 = new AuthorId("1");

        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentIds() {
        AuthorId id1 = new AuthorId("1");
        AuthorId id2 = new AuthorId("2");

        assertNotEquals(id1, id2);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentObjects() {
        AuthorId id = new AuthorId("1");
        assertNotEquals(id, "not an authorID");
    }

    @Test
    void equals_shouldReturnFalse_whenComparedToNull() {
        AuthorId id = new AuthorId("1");
        assertNotEquals(id, null);
    }

    @Test
    void equals_shouldThrow_whenNullObject() {

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new AuthorId(null));

        assertEquals("AuthorId must be a non empty String", ex.getMessage());
    }

}