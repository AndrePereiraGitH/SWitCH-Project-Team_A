package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorDTO_US026Test {

    @Test
    void fromAuthor_shouldThrow_whenAuthorNull() {

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> AuthorDTO_US026.fromAuthor(null));

        assertEquals("author cannot be null", exception.getMessage());
    }

    @Test
    void fromAuthor_shouldReturnAuthorDTO_whenValidAuthorGiven() {

        // arrange
        Author authorDouble = createAuthorDouble();

        // act
        AuthorDTO_US026 result = AuthorDTO_US026.fromAuthor(authorDouble);

        // assert
        assertEquals(authorDouble.getName(), result.name());
    }

    private Author createAuthorDouble() {
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn("Rodolph");

        return  authorDouble;

    }

}