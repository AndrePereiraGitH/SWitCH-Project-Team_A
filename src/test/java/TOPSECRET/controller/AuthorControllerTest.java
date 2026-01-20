package TOPSECRET.controller;


import TOPSECRET.domain.Author;
import TOPSECRET.domain.Enum.Countries;
import TOPSECRET.domain.Profile;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorControllerTest {
    @Test
    void shouldAddAuthor() {
        AuthorRepository repository = new AuthorRepository();

        US005_AuthorController authorController = new US005_AuthorController(repository);
        Profile profile1 = new Profile("João", Countries.AFGHANISTAN, LocalDate.of(1978, 5, 12));
        Author expected = new Author(profile1, "valid bio");

        // Act + Assert
        Author result = repository.addAuthor("João", Countries.AFGHANISTAN, LocalDate.of(1978, 5, 12));

        assertEquals(expected, result);

    }

}