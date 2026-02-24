package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AuthorMapper_US026Test {

    private AuthorRepository authorRepo;

    @BeforeEach
    void setup() {
        authorRepo = new AuthorRepository(new ArrayList<>());
    }


    @Test
    void fromAuthor_whenValidAuthor_shouldMapNameFromProfile() {
        // arrange
        Author author = authorRepo.createAndAddAuthor(
                "Ken Follett",
                new Country("United_kingdom"),
                LocalDate.of(1949, 6, 5),
                "bio"
        );

        // act
        AuthorDTO_US026 dto = AuthorDTO_US026.fromAuthor(author);

        // assert
        assertNotNull(dto);
        assertEquals("Ken Follett", dto.name());
    }

    @Test
    void fromAuthor_whenAuthorHasDifferentName_shouldMapThatExactName() {
        // arrange
        Author author = authorRepo.createAndAddAuthor(
                "Dan Brown",
                new Country("United_kingdom"),
                LocalDate.of(1964, 6, 5),
                "bio"
        );

        // act
        AuthorDTO_US026 dto = AuthorDTO_US026.fromAuthor(author);

        // assert
        assertNotNull(dto);
        assertEquals("Dan Brown", dto.name());
    }

    @Test
    void fromAuthor_whenAuthorIsNull_shouldThrowIllegalArgException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> AuthorDTO_US026.fromAuthor(null));
        assertEquals("auhtor cannot be null", e.getMessage());
    }

}