package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorMapper_US025Test {

    private AuthorMapper_US025 mapper;

    @BeforeEach
    void setUp() {
        mapper = new AuthorMapper_US025();
    }

    @Test
    void toDTO_shouldMapSingleAuthorCorrectly() {

        Country country = new Country("PORTUGAL");
        Author author = new Author("José Saramago", country, LocalDate.of(2000, 11, 25), "bio");

        var dto = mapper.toDTO(List.of(author));

        assertNotNull(dto);
        assertEquals(1, dto.getAuthors().size());
        var firstAuthor = dto.getAuthors().get(0);
        assertEquals("José Saramago", firstAuthor.getName());
        assertEquals(country, firstAuthor.getCountry());
        assertEquals(LocalDate.of(2000, 11, 25), firstAuthor.getDateOfBirth());
        assertEquals("bio", firstAuthor.getBiography());
    }
}