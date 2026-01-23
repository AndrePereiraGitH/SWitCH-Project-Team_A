package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreMapper_US022Test {

    @Test
    void toDTOGetListOfItemsOnAuctionOfGivenGenreEmptyList(){
        // Arrange
        GenreMapper_US022 genreMapper = new GenreMapper_US022();
        List<Genre> genres = new ArrayList<>();

        // Act
        List<GenreDTO_US022> result = genreMapper.toDTO(genres);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void toDTOGetListOfItemsOnAuctionOfGivenGenreOneElement() {
        // Arrange
        GenreMapper_US022 genreMapper = new GenreMapper_US022();
        Genre genre = new Genre("Comedy");

        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        // Act
        List<GenreDTO_US022> result = genreMapper.toDTO(genres);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Comedy", result.get(0).name);
    }

    @Test
    void toDTOGetListOfItemsOnAuctionOfGivenGenreTwoElement() {
        // Arrange
        GenreMapper_US022 genreMapper = new GenreMapper_US022();
        Genre genre1 = new Genre("Fantasy");
        Genre genre2 = new Genre("Romance");

        List<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        genres.add(genre2);

        // Act
        List<GenreDTO_US022> result = genreMapper.toDTO(genres);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).name);
        assertEquals("Romance", result.get(1).name);
    }

    @Test
    void toDTOGetListOfItemsOnAuctionOfGivenGenreInvalid() {
        // Arrange
        GenreMapper_US022 genreMapper = new GenreMapper_US022();
        Genre genre1 = new Genre("Fantasy");

        List<Genre> genres = new ArrayList<>();
        genres.add(genre1);

        // Act
        List<GenreDTO_US022> result = genreMapper.toDTO(genres);

        // Assert
        assertNotSame(result, result.get(0));
    }

}