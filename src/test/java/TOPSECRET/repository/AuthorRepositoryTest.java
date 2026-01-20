package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AuthorRepositoryTest {

    @Test
    void shouldCreateAuthor() {
        //  Arrange
        AuthorRepository repository = new AuthorRepository();
        Profile profile1 = new Profile("João", new Country("Portugal"), LocalDate.of(1978, 5, 12));
        Author expected = new Author(profile1, "valid bio");

        // Act
        Author result = repository.createAndAddAuthor("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowWhenAuthorAlreadyExists() {
        AuthorRepository repository = new AuthorRepository();

        repository.createAndAddAuthor("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAuthor("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio")
        );
    }
}