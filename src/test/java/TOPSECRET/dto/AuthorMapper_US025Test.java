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

        assertNotNull(dtos);
        assertTrue(mapper.toDTO(List.of()).isEmpty());
        assertNull(mapper.toDTO((Author) null));
        assertEquals(2, dtos.size());
        
        var firstAuthor = dtos.get(0);
        var secondAuthor = dtos.get(1);

        assertEquals("José Saramago", firstAuthor.getName());
        assertEquals("João Silva", secondAuthor.getName());
    }
}