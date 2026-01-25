package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AuthorRepositoryTest {

    private final AuthorRepository authorRepository = new AuthorRepository(new ArrayList<>());

    @Test
    void shouldCreateAuthor() {
        //  Arrange
        Profile profile1 = new Profile("João", new Country("Portugal"), LocalDate.of(1978, 5, 12));
        Author expected = new Author(profile1, "valid bio");

        // Act
        Author result = authorRepository.createAndAddAuthor("João", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowWhenAuthorAlreadyExists() {

        authorRepository.createAndAddAuthor("repeatedName", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio");

        assertThrows(IllegalArgumentException.class, () ->
                authorRepository.createAndAddAuthor("repeatedName", new Country("Portugal"), LocalDate.of(1978, 5, 12), "valid bio")
        );
    }
}