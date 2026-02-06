package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreMapper_US013Test {
    @Test
    void shouldReturnEmptyListWhenMappingEmptyList() {
        // Arrange
        GenreMapper_US013 mapper = new GenreMapper_US013();
        List<Genre> emptyList = new ArrayList<>();

        // Act
        List<GenreDTO_US013> result = mapper.toDTO(emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    void shouldMapSingleGenreToDTO() {
        // Arrange
        GenreMapper_US013 mapper = new GenreMapper_US013();
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("Mystery"));

        // Act
        List<GenreDTO_US013> result = mapper.toDTO(genres);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Mystery", result.get(0).genreName);
    }

    @Test
    void shouldMapMultipleGenresToDTOs() {
        // Arrange
        GenreMapper_US013 mapper = new GenreMapper_US013();
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("Action"));
        genres.add(new Genre("Comedy"));
        genres.add(new Genre("Drama"));

        // Act
        List<GenreDTO_US013> result = mapper.toDTO(genres);

        // Assert
        assertEquals(3, result.size());
        assertEquals("Action", result.get(0).genreName);
        assertEquals("Comedy", result.get(1).genreName);
        assertEquals("Drama", result.get(2).genreName);
    }

    @Test
    void shouldPreserveOrderWhenMapping() {
        // Arrange
        GenreMapper_US013 mapper = new GenreMapper_US013();
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("Horror"));
        genres.add(new Genre("Thriller"));
        genres.add(new Genre("Romance"));
        genres.add(new Genre("SciFi"));

        // Act
        List<GenreDTO_US013> result = mapper.toDTO(genres);

        // Assert
        assertEquals(4, result.size());
        assertEquals("Horror", result.get(0).genreName);
        assertEquals("Thriller", result.get(1).genreName);
        assertEquals("Romance", result.get(2).genreName);
        assertEquals("SciFi", result.get(3).genreName);
    }

    @Test
    void shouldCorrectlyMapGenreTypeToGenreName() {
        // Arrange
        GenreMapper_US013 mapper = new GenreMapper_US013();
        Genre genre = new Genre("Western");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        // Act
        List<GenreDTO_US013> result = mapper.toDTO(genres);

        // Assert
        assertEquals(genre.getGenreType(), result.get(0).genreName);
    }

    @Test
    void shouldCreateNewDTOInstancesForEachGenre() {
        // Arrange
        GenreMapper_US013 mapper = new GenreMapper_US013();
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("Fantasy"));
        genres.add(new Genre("Adventure"));

        // Act
        List<GenreDTO_US013> result = mapper.toDTO(genres);

        // Assert
        assertEquals(2, result.size());
        // Verify they are different DTO instances
        assertNotSame(result.get(0), result.get(1));
    }

    @Test
    void shouldHandleDifferentGenreNameCases() {
        // Arrange
        GenreMapper_US013 mapper = new GenreMapper_US013();
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("ACTION"));
        genres.add(new Genre("action"));
        genres.add(new Genre("Action"));

        // Act
        List<GenreDTO_US013> result = mapper.toDTO(genres);

        // Assert
        assertEquals(3, result.size());
        assertEquals("ACTION", result.get(0).genreName);
        assertEquals("action", result.get(1).genreName);
        assertEquals("Action", result.get(2).genreName);
    }
}