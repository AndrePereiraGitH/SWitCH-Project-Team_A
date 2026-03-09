package TOPSECRET.dto;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreDTO_US013DependenciesTest {
    @Test
    void testGenreDTOCreation_ValidGenre() {
        // Arrange
        Genre genre = new Genre("Adventure");

        // Act
        GenreDTO_US013 dto = new GenreDTO_US013(genre.getGenreType());

        // Assert
        assertEquals("Adventure", dto.genreName);
    }

    @Test
    void testGenreDTOCreation_PreservesGenreType() {
        // Arrange
        Genre genre = new Genre("Fantasy");

        // Act
        GenreDTO_US013 dto = new GenreDTO_US013(genre.getGenreType());

        // Assert
        assertEquals(genre.getGenreType(), dto.genreName);
    }

    @Test
    void constructor_withValidName_shouldCreateDTO() {
        GenreDTO_US013 dto = new GenreDTO_US013("Horror");
        assertEquals("Horror", dto.genreName);
    }

    @Test
    void constructor_withNullName_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new GenreDTO_US013(null)
        );
        assertEquals("Genre name cannot be null or blank", exception.getMessage());
    }

    @Test
    void constructor_withBlankName_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new GenreDTO_US013("   ")
        );
        assertEquals("Genre name cannot be null or blank", exception.getMessage());
    }

    @Test
    void constructor_withEmptyString_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new GenreDTO_US013("")
        );
        assertEquals("Genre name cannot be null or blank", exception.getMessage());
    }

//    // --- toDTO tests ---
//
//    @Test
//    void toDTO_withValidGenre_shouldReturnDTOWithCorrectName() {
//        Genre genre = mock(Genre.class);
//        when(genre.getTitle()).thenReturn("Comedy");
//
//        GenreDTO_US013 dto = GenreDTO_US013.toDTO(genre);
//
//        assertNotNull(dto);
//        assertEquals("Comedy", dto.genreName);
//    }
//
//    @Test
//    void toDTO_withNullGenre_shouldThrowIllegalArgumentException() {
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> GenreDTO_US013.toDTO(null)
//        );
//        assertEquals("Genre cannot be null", exception.getMessage());
//    }
//
//    @Test
//    void toDTO_whenGenreTitleIsNull_shouldThrowIllegalArgumentException() {
//        Genre genre = mock(Genre.class);
//        when(genre.getTitle()).thenReturn(null);
//
//        assertThrows(IllegalArgumentException.class, () -> GenreDTO_US013.toDTO(genre));
//    }
//
//    @Test
//    void toDTO_whenGenreTitleIsBlank_shouldThrowIllegalArgumentException() {
//        Genre genre = mock(Genre.class);
//        when(genre.getTitle()).thenReturn("  ");
//
//        assertThrows(IllegalArgumentException.class, () -> GenreDTO_US013.toDTO(genre));
//    }
}