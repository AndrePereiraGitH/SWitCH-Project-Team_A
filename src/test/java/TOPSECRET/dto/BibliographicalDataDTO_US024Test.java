package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.BibliographicalData;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link BibliographicalDataDTO_US024}.
 *
 * <p>Covers constructor field initialization, single-object conversion via
 * {@link BibliographicalDataDTO_US024#toDTO(BibliographicalData)}, bulk conversion
 * via {@link BibliographicalDataDTO_US024#toDTOS(List)}, and null-input guarding.
 * Domain dependencies are replaced with Mockito doubles to isolate DTO mapping logic.</p>
 */
class BibliographicalDataDTO_US024Test {


    /**
     * Verifies that the constructor correctly assigns all provided values
     * to their corresponding public fields.
     */
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

    /**
     * Verifies that {@link BibliographicalDataDTO_US024#toDTO(BibliographicalData)}
     * correctly maps all fields from a {@link BibliographicalData} domain object
     * to the corresponding DTO fields.
     *
     * <p>Expected mappings:</p>
     * <ul>
     *   <li>{@code _publicationTitle} ← {@code getTitle()}</li>
     *   <li>{@code _authors} ← each author's {@code getProfile().getName()}</li>
     *   <li>{@code _publicationGenre} ← each genre's {@code getTitle()}</li>
     * </ul>
     */
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

    /**
     * Verifies that {@link BibliographicalDataDTO_US024#toDTOS(List)} converts a list
     * of {@link BibliographicalData} objects into a correctly sized and ordered list
     * of DTOs, preserving each publication's title at its original index.
     */
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

    /**
     * Verifies that {@link BibliographicalDataDTO_US024#toDTO(BibliographicalData)}
     * throws a {@link NullPointerException} when passed a {@code null} argument,
     * as documented in the method contract.
     */
    @Test
    void toDTOShouldThrowExceptionWhenPublicationIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> BibliographicalDataDTO_US024.toDTO(null));
    }

    // -------------------------------------------------------------------------
    // Helper / factory methods
    // -------------------------------------------------------------------------

    /**
     * Creates a mocked {@link BibliographicalData} that returns the given title,
     * author list, and genre list.
     *
     * @param title       the value returned by {@code getTitle()}
     * @param authorNames the names used to build mocked {@link Author} objects
     * @param genreNames  the titles used to build mocked {@link Genre} objects
     * @return a configured {@link BibliographicalData} mock
     */
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

    /**
     * Creates a list of mocked {@link Author} objects from the given names.
     *
     * @param authorNames the names to convert into mocked authors
     * @return a list of configured {@link Author} mocks
     */
    private List<Author> createMockAuthors(List<String> authorNames) {
        return authorNames.stream()
                .map(this::createMockAuthor)
                .toList();
    }

    /**
     * Creates a mocked {@link Author} whose {@link Profile} returns the given name.
     *
     * @param authorName the value returned by {@code getProfile().getName()}
     * @return a configured {@link Author} mock
     */
    private Author createMockAuthor(String authorName) {
        Profile profile = createMockProfile(authorName);

        Author author = mock(Author.class);
        when(author.getProfile()).thenReturn(profile);

        return author;
    }

    /**
     * Creates a mocked {@link Profile} that returns the given name.
     *
     * @param name the value returned by {@code getName()}
     * @return a configured {@link Profile} mock
     */
    private Profile createMockProfile(String name) {
        Profile profile = mock(Profile.class);
        when(profile.getName()).thenReturn(name);
        return profile;
    }

    /**
     * Creates a list of mocked {@link Genre} objects from the given titles.
     *
     * @param genreTitles the titles to convert into mocked genres
     * @return a list of configured {@link Genre} mocks
     */
    private List<Genre> createMockGenres(List<String> genreTitles) {
        return genreTitles.stream()
                .map(this::createMockGenre)
                .toList();
    }

    /**
     * Creates a mocked {@link Genre} that returns the given title.
     *
     * @param genreTitle the value returned by {@code getTitle()}
     * @return a configured {@link Genre} mock
     */
    private Genre createMockGenre(String genreTitle) {
        Genre genre = mock(Genre.class);
        when(genre.getTitle()).thenReturn(genreTitle);
        return genre;
    }

}
