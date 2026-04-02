//package TOPSECRET.dto;
//
//import TOPSECRET.domain.Author.Author;
//import TOPSECRET.domain.BibliographicalData.BibliographicalData;
//import TOPSECRET.domain.Genre.Genre;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * Unit tests for {@link BibliographicalDataDTO_US024}.
// *
// * <p>Covers constructor field initialization, single-object conversion via
// * {@link BibliographicalDataDTO_US024#toDTO(BibliographicalData)}, bulk conversion
// * via {@link BibliographicalDataDTO_US024#toDTOS(List)}, and null-input guarding.
// * Domain dependencies are replaced with test doubles to isolate DTO mapping logic.</p>
// */
//class BibliographicalDataDTO_US024Test {
//
//    /**
//     * Verifies that the constructor correctly assigns all provided values
//     * to their corresponding public fields.
//     */
//    @Test
//    void constructorShouldInitializeAllFields() {
//        // Arrange
//        String expectedTitle = "Test Publication";
//        List<String> expectedAuthors = Arrays.asList("Author 1", "Author 2");
//        List<String> expectedGenres = Arrays.asList("Science Fiction", "Adventure");
//
//        // Act
//        BibliographicalDataDTO_US024 result = new BibliographicalDataDTO_US024(expectedTitle, expectedAuthors, expectedGenres);
//
//        // Assert
//        assertEquals(expectedTitle, result._publicationTitle);
//        assertEquals(expectedAuthors, result._authors);
//        assertEquals(expectedGenres, result._publicationGenre);
//    }
//
//    /**
//     * Verifies that {@link BibliographicalDataDTO_US024#toDTO(BibliographicalData)}
//     * correctly maps all fields from a {@link BibliographicalData} domain object
//     * to the corresponding DTO fields.
//     *
//     * <p>Expected mappings:</p>
//     * <ul>
//     *   <li>{@code _publicationTitle} ← {@code getTitle()}</li>
//     *   <li>{@code _authors} ← each author's {@code getName()}</li>
//     *   <li>{@code _publicationGenre} ← each genre's {@code getGenreType()}</li>
//     * </ul>
//     */
//    @Test
//    void toDTOShouldConvert() {
//        // Arrange
//        BibliographicalData bibliographicalData = createMockBibliographicalData(
//                "Test Publication",
//                List.of("Author 1", "Author 2"),
//                List.of("Science Fiction", "Adventure")
//        );
//
//        // Act
//        BibliographicalDataDTO_US024 result = BibliographicalDataDTO_US024.toDTO(bibliographicalData);
//
//        // Assert
//        assertEquals("Test Publication", result._publicationTitle);
//        assertEquals(List.of("Author 1", "Author 2"), result._authors);
//        assertEquals(List.of("Science Fiction", "Adventure"), result._publicationGenre);
//    }
//
//    /**
//     * Verifies that {@link BibliographicalDataDTO_US024#toDTOS(List)} converts a list
//     * of {@link BibliographicalData} objects into a correctly sized and ordered list
//     * of DTOs, preserving each publication's title at its original index.
//     */
//    @Test
//    void toDTOS_shouldReturnListOfDTOS() {
//        // Arrange
//        BibliographicalData bd1 = createMockBibliographicalData(
//                "Publication 1",
//                List.of("Author 1"),
//                List.of("Genre 1")
//        );
//        BibliographicalData bd2 = createMockBibliographicalData(
//                "Publication 2",
//                List.of("Author 2"),
//                List.of("Genre 2")
//        );
//
//        List<BibliographicalData> bibliographicalDataList = List.of(bd1, bd2);
//
//        // Act
//        List<BibliographicalDataDTO_US024> result = BibliographicalDataDTO_US024.toDTOS(bibliographicalDataList);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals("Publication 1", result.get(0)._publicationTitle);
//        assertEquals("Publication 2", result.get(1)._publicationTitle);
//    }
//
//    /**
//     * Verifies that {@link BibliographicalDataDTO_US024#toDTO(BibliographicalData)}
//     * throws a {@link NullPointerException} when passed a {@code null} argument,
//     * as documented in the method contract.
//     */
//    @Test
//    void toDTOShouldThrowExceptionWhenPublicationIsNull() {
//        // Act & Assert
//        assertThrows(NullPointerException.class, () -> BibliographicalDataDTO_US024.toDTO(null));
//    }
//
//    // -------------------------------------------------------------------------
//    // Helper / factory methods
//    // -------------------------------------------------------------------------
//
//    /**
//     * Creates a test double for {@link BibliographicalData} that returns the given
//     * title, author list, and genre list.
//     *
//     * @param title       the value returned by {@code getTitle()}
//     * @param authorNames the names used to build {@link Author} test doubles
//     * @param genreNames  the types used to build {@link Genre} test doubles
//     * @return a configured {@link BibliographicalData} test double
//     */
//    private BibliographicalData createMockBibliographicalData(
//            String title,
//            List<String> authorNames,
//            List<String> genreNames) {
//
//        List<Author> authors = createMockAuthors(authorNames);
//        List<Genre> genres = createMockGenres(genreNames);
//
//        BibliographicalData bdDouble = mock(BibliographicalData.class);
//        when(bdDouble.getTitle()).thenReturn(title);
//        when(bdDouble.getAuthors()).thenReturn(authors);
//        when(bdDouble.getGenres()).thenReturn(genres);
//
//        return bdDouble;
//    }
//
//    /**
//     * Creates a list of {@link Author} test doubles from the given names.
//     *
//     * @param authorNames the names to convert into {@link Author} test doubles
//     * @return a list of configured {@link Author} test doubles
//     */
//    private List<Author> createMockAuthors(List<String> authorNames) {
//        return authorNames.stream()
//                .map(this::createMockAuthor)
//                .toList();
//    }
//
//    /**
//     * Creates an {@link Author} test double whose {@code getName()} returns
//     * the given name.
//     *
//     * @param authorName the value returned by {@code getName()}
//     * @return a configured {@link Author} test double
//     */
//    private Author createMockAuthor(String authorName) {
//        Author authorDouble = mock(Author.class);
//        when(authorDouble.getName()).thenReturn(authorName);
//        return authorDouble;
//    }
//
//    /**
//     * Creates a list of {@link Genre} test doubles from the given types.
//     *
//     * @param genreTypes the types to convert into {@link Genre} test doubles
//     * @return a list of configured {@link Genre} test doubles
//     */
//    private List<Genre> createMockGenres(List<String> genreTypes) {
//        return genreTypes.stream()
//                .map(this::createMockGenre)
//                .toList();
//    }
//
//    /**
//     * Creates a {@link Genre} test double whose {@code getGenreType()} returns
//     * the given type.
//     *
//     * @param genreType the value returned by {@code getGenreType()}
//     * @return a configured {@link Genre} test double
//     */
//    private Genre createMockGenre(String genreType) {
//        Genre genreDouble = mock(Genre.class);
//        when(genreDouble.getGenreType()).thenReturn(genreType);
//        return genreDouble;
//    }
//}