package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreDTO_US022Test {


    @Test
    void fromDomainList_shouldReturnEmptyListWhenInputIsEmpty() {

        List<GenreDTO_US022> result =
                GenreDTO_US022.fromDomainList(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void fromDomainListGetListOfItemsOnAuctionOfGivenGenreEmptyList(){
        // Arrange
        List<Genre> genres = new ArrayList<>();

        // Act
        List<GenreDTO_US022> result = GenreDTO_US022.fromDomainList(genres);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void fromDomainListGetListOfItemsOnAuctionOfGivenGenreOneElement() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Comedy");

        List<Genre> genres = new ArrayList<>();
        genres.add(genreDouble);

        // Act
        List<GenreDTO_US022> result = GenreDTO_US022.fromDomainList(genres);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Comedy", result.get(0).name);
    }

    @Test
    void fromDomainListGetListOfItemsOnAuctionOfGivenGenreTwoElement() {
        // Arrange
        Genre genreDouble1 = mock(Genre.class);
        Genre genreDouble2 = mock(Genre.class);

        when(genreDouble1.getGenreType()).thenReturn("Fantasy");
        when(genreDouble2.getGenreType()).thenReturn("Romance");

        List<Genre> genres = new ArrayList<>();
        genres.add(genreDouble1);
        genres.add(genreDouble2);

        // Act
        List<GenreDTO_US022> result = GenreDTO_US022.fromDomainList(genres);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).name);
        assertEquals("Romance", result.get(1).name);
    }

    @Test
    void fromDomainListGetListOfItemsOnAuctionOfGivenGenreInvalid() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fantasy");

        List<Genre> genres = new ArrayList<>();
        genres.add(genreDouble);

        // Act
        List<GenreDTO_US022> result = GenreDTO_US022.fromDomainList(genres);

        // Assert
        assertNotSame(result, result.get(0));
    }

    @Test
    void fromDomainList_shouldConvertGenresToDTO() {

        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);

        when(genre1Double.getGenreType()).thenReturn("Fantasy");
        when(genre2Double.getGenreType()).thenReturn("Romance");

        List<GenreDTO_US022> result =
                GenreDTO_US022.fromDomainList(List.of(genre1Double, genre2Double));

        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).name);
        assertEquals("Romance", result.get(1).name);
    }

}

