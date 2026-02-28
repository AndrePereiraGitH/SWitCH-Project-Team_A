package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.BibliographicalData;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BibliographicalDataDTO_US024Test {

    @Test
    void constructorShouldInitializeAllFields() {
        // Arrange
        String expectedTitle = "Test Publication";
        List<String> expectedAuthors = Arrays.asList("Author 1", "Author 2");
        List<String> expectedGenres = Arrays.asList("Science Fiction", "Adventure");

        // Act
        BibliographicalDataDTO_US024 result = new BibliographicalDataDTO_US024(expectedTitle, expectedAuthors, expectedGenres);

        // Assert
        assertEquals(expectedTitle, result._publicationTitle);
        assertEquals(expectedAuthors, result._authors);
        assertEquals(expectedGenres, result._publicationGenre);
    }

    @Test
    void toDTOShouldConvert() {
        // Arrange
        BibliographicalData bibliographicalData = createMockBibliographicalData(
                "Test Publication",
                List.of("Author 1", "Author 2"),
                List.of("Science Fiction", "Adventure")
        );

        // Act
        BibliographicalDataDTO_US024 result = BibliographicalDataDTO_US024.toDTO(bibliographicalData);

        // Assert
        assertEquals("Test Publication", result._publicationTitle);
        assertEquals(List.of("Author 1", "Author 2"), result._authors);
        assertEquals(List.of("Science Fiction", "Adventure"), result._publicationGenre);
    }

    @Test
    void toDTOS_shouldReturnListOfDTOS() {
        // Arrange
        BibliographicalData bd1 = createMockBibliographicalData(
                "Publication 1",
                List.of("Author 1"),
                List.of("Genre 1")
        );
        BibliographicalData bd2 = createMockBibliographicalData(
                "Publication 2",
                List.of("Author 2"),
                List.of("Genre 2")
        );

        List<BibliographicalData> bibliographicalDataList = List.of(bd1, bd2);

        // Act
        List<BibliographicalDataDTO_US024> result = BibliographicalDataDTO_US024.toDTOS(bibliographicalDataList);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Publication 1", result.get(0)._publicationTitle);
        assertEquals("Publication 2", result.get(1)._publicationTitle);
    }

    @Test
    void toDTOShouldThrowExceptionWhenPublicationIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> BibliographicalDataDTO_US024.toDTO(null));
    }


    private BibliographicalData createMockBibliographicalData(
            String title,
            List<String> authorNames,
            List<String> genreNames) {

        List<Author> authors = createMockAuthors(authorNames);
        List<Genre> genres = createMockGenres(genreNames);

        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        when(bibliographicalData.getTitle()).thenReturn(title);
        when(bibliographicalData.getAuthors()).thenReturn(authors);
        when(bibliographicalData.getGenres()).thenReturn(genres);

        return bibliographicalData;
    }


    private List<Author> createMockAuthors(List<String> authorNames) {
        return authorNames.stream()
                .map(this::createMockAuthor)
                .toList();
    }


    private Author createMockAuthor(String authorName) {
        Profile profile = createMockProfile(authorName);

        Author author = mock(Author.class);
        when(author.getProfile()).thenReturn(profile);

        return author;
    }


    private Profile createMockProfile(String name) {
        Profile profile = mock(Profile.class);
        when(profile.getName()).thenReturn(name);
        return profile;
    }


    private List<Genre> createMockGenres(List<String> genreTitles) {
        return genreTitles.stream()
                .map(this::createMockGenre)
                .toList();
    }


    private Genre createMockGenre(String genreTitle) {
        Genre genre = mock(Genre.class);
        when(genre.getTitle()).thenReturn(genreTitle);
        return genre;
    }

}
