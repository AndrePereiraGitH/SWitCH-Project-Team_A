package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.BibliographicalData;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BibliographicalDataDTO_US023Test {

    // ========================================================
    // =============== Set Up =================================
    // ========================================================

    List<Author> authors;
    List<Genre> genres;
    BibliographicalData bdDouble;

    @BeforeEach
    void setup() {

        bdDouble = mock(BibliographicalData.class);

        Author author1Double = mock(Author.class);
        Profile profile1Double = mock(Profile.class);
        Author author2Double = mock(Author.class);
        Profile profile2Double = mock(Profile.class);
        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);

        when(bdDouble.getTitle()).thenReturn("title1");

        when(author1Double.getProfile()).thenReturn(profile1Double);
        when(profile1Double.getName()).thenReturn("author1 name");
        when(author2Double.getProfile()).thenReturn(profile2Double);
        when(profile2Double.getName()).thenReturn("author2 name");
        authors = List.of(author1Double,author2Double);
        when(bdDouble.getAuthors()).thenReturn(authors);

        when(genre1Double.getTitle()).thenReturn("genre1");
        when(genre2Double.getTitle()).thenReturn("genre2");
        genres = List.of(genre1Double,genre2Double);
        when(bdDouble.getGenres()).thenReturn(genres);
    }


    // ========================================================
    // =============== Tests to Constructor ===================
    // ========================================================

    @Test
    void constructor_ShouldNotThrowWhenValidDataProvided() {
        // Act + Assert
        assertDoesNotThrow(() ->
                new BibliographicalDataDTO_US023("title1", List.of("author1", "author2"),
                        List.of("genre1", "genre2"))
        );
    }

    @Test
    void constructor_ShouldInitializeAllFields() {
        // Arrange
        List<String> authorsDTO = List.of("author1 name", "author2 name");
        List<String> genresDTO = List.of("genre1", "genre2");

        // Act
        BibliographicalDataDTO_US023 dto = new BibliographicalDataDTO_US023("title1", authorsDTO, genresDTO);

        // Assert
        assertEquals("title1", dto.getTitle());
        assertEquals(authorsDTO, dto.getAuthors());
        assertEquals(genresDTO, dto.getGenres());
    }

    // ========================================================
    // =============== Tests to toDTO =========================
    // ========================================================

    @Test
    void toDTO_ShouldConvertBibliographicalDataCorrectly() {
        // Act
        BibliographicalDataDTO_US023 dto = BibliographicalDataDTO_US023.toDTO(bdDouble);

        // Assert
        assertEquals("title1", dto.getTitle());
        assertEquals(List.of("author1 name", "author2 name"), dto.getAuthors());
        assertEquals(List.of("genre1", "genre2"), dto.getGenres());
    }

    @Test
    void toDTO_ShouldThrowWhenPublicationIsNull() {
        // Assert
        assertThrows(NullPointerException.class,
                () -> BibliographicalDataDTO_US023.toDTO(null));
    }

    @Test
    void toDTO_ShouldNotReflectChangesInOriginalLists() {
        // Arrange
        List<Author> mutableAuthors = new ArrayList<>(authors);
        List<Genre> mutableGenres = new ArrayList<>(genres);

        when(bdDouble.getAuthors()).thenReturn(mutableAuthors);
        when(bdDouble.getGenres()).thenReturn(mutableGenres);

        // Act
        BibliographicalDataDTO_US023 dto = BibliographicalDataDTO_US023.toDTO(bdDouble);

        mutableAuthors.add(mock(Author.class));
        mutableGenres.add(mock(Genre.class));

        // Assert
        assertEquals(2, dto.getAuthors().size());
        assertEquals(2, dto.getGenres().size());
    }


    // ========================================================
    // =============== Tests to toDTOs ========================
    // ========================================================

    @Test
    void toDTOs_ShouldConvertListAndPreserveOrder() {
        // Arrange
        BibliographicalData bd1Double = mock(BibliographicalData.class);
        BibliographicalData bd2Double = mock(BibliographicalData.class);

        when(bd1Double.getTitle()).thenReturn("A");
        when(bd1Double.getAuthors()).thenReturn(authors);
        when(bd1Double.getGenres()).thenReturn(genres);

        when(bd2Double.getTitle()).thenReturn("B");
        when(bd2Double.getAuthors()).thenReturn(authors);
        when(bd2Double.getGenres()).thenReturn(genres);

        // Act
        List<BibliographicalDataDTO_US023> dtoList = BibliographicalDataDTO_US023.toDTOs(List.of(bd1Double, bd2Double));

        // Assert
        assertEquals(2, dtoList.size());
        assertEquals("A", dtoList.get(0).getTitle());
        assertEquals("B", dtoList.get(1).getTitle());
    }

    @Test
    void toDTOs_ShouldReturnEmptyListWhenInputIsEmpty() {
        // Act
        List<BibliographicalDataDTO_US023> dtoList = BibliographicalDataDTO_US023.toDTOs(List.of());

        // Assert
        assertTrue(dtoList.isEmpty());
    }

    @Test
    void toDTOs_ShouldThrowWhenListContainsNull() {
        // Arrange
        List<BibliographicalData> list = new ArrayList<>();
        list.add(bdDouble);
        list.add(null);

        // Assert
        assertThrows(NullPointerException.class,
                () -> BibliographicalDataDTO_US023.toDTOs(list));
    }
}
