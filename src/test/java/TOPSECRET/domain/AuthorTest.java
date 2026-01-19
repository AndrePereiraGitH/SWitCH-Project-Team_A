package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Country;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    @Test
    void shouldCreateAuthorWithProfile() {

        Profile profile = new Profile(
                "John Lewis",
                Country.BRAZIL,
                LocalDate.of(1984, 12, 3)
        );

        Author author = new Author(profile);

        assertNotNull(author);
        assertEquals(profile, author.getProfile());
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Author(null);
        });
    }
}