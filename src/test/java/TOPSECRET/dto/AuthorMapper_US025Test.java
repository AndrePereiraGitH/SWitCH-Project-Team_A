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
        Author author1 = new Author("José Saramago", country, LocalDate.of(2000, 11, 25), "bio");
        Author author2 = new Author("João Silva", country, LocalDate.of(2000, 11, 25), "bio");

        var dtos = mapper.toDTO(List.of(author1,author2));

        // To test if the list is not null, even if's empty
        assertNotNull(dtos);

        // To test if it returns an empty list for an empty list
        assertTrue(mapper.toDTO(List.of()).isEmpty());

        // To test if it returns null when author is null
        assertNull(mapper.toDTO((Author) null));

        // To test if it maps several authors correctly
        assertEquals(2, dtos.size());
        var firstAuthor = dtos.get(0);
        var secondAuthor = dtos.get(1);
        assertEquals("José Saramago", firstAuthor.getName());
        assertEquals("João Silva", secondAuthor.getName());
    }
}