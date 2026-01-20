package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.Enum.Countries;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorRepositoryTest {

    @Test
    void shouldCreateAuthor() {
        //  Arrange
        AuthorRepository repository = new AuthorRepository();
        Country country = new Country("AFGHANISTAN");
        Profile profile1 = new Profile("João", country, LocalDate.of(1978, 5, 12));
        Author expected = new Author(profile1, "valid bio");

        // Act
        Author result = repository.addAuthor("João", country, LocalDate.of(1978, 5, 12));

        // Assert
        assertEquals(expected, result);
    }


}