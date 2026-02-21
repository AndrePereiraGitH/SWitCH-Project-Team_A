package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.ENGLISH;
import static org.junit.jupiter.api.Assertions.*;

class BibliographicalDataTest {

    // Sample Builder With Valid Attributes
    private BibliographicalData sampleBibliographicalData() {

        // arrange
        String title = "The Age of AI: And Our Human Future";
        //List<Profile> authors= new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        Country country = new Country("UNITED_STATES");
        Profile profile2 = new Profile("Henry A Kissinger", country,LocalDate.of(1923,05,27));
        Profile profile3 = new Profile("Eric Schmidt III", country,LocalDate.of(1955,04,27));
        Profile profile4 = new Profile("Daniel Huttenlocher", country,LocalDate.of(1959,01,01));
        Author author2 = new Author(profile2, "valid bio2");
        Author author3 = new Author(profile3, "valid bio3");
        Author author4 = new Author(profile4, "valid bio4");
        authors.add(author2);
        authors.add(author3);
        authors.add(author4);

        Locale language = ENGLISH;


        // publisher became an attribute of the Edition class
//        Country country1 = new Country("UNITED_KINGDOM");
//        Address address2 = new Address("Carmelite House","50", "EC4Y 0DZ", country1);
//        Email email2 = new Email("enquiries", "hachette.co.uk");
//        Contact contact2 = new Contact("2031227222", "+44", email2, address2);
//        Publisher publisher = new Publisher("John Murray Press", "GB205505305", contact2);

        LocalDate publishDate = LocalDate.of(2022, 8, 4);


        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre ("Fantasy"));

        // numberOfPages became an attribute of the PhysicalAttributes class
//        int numberOfPages = 272;

        String chunksOfText = "The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.";

        // act
//        return new BibliographicalData(title, authors, language, publisher, publishDate, genres, numberOfPages, chunksOfText);
        return new BibliographicalData(title, authors, language, publishDate, genres, chunksOfText);
    }

    // Test With More Than One Author And Genre
    @Test
    void testConstructorInitializedMoreThanOneAuthorAndGenre() {

        // act
        BibliographicalData bibliographicalData = sampleBibliographicalData();

        // assert
        assertEquals("The Age of AI: And Our Human Future", bibliographicalData.getTitle());

        assertEquals(3, bibliographicalData.getAuthors().size());
//        assertEquals("Henry A Kissinger", bibliographicalData.getAuthors().get(0).getName());
//        assertEquals("Eric Schmidt III", bibliographicalData.getAuthors().get(1).getName());
//        assertEquals("Daniel Huttenlocher", bibliographicalData.getAuthors().get(2).getName());
        assertEquals("Henry A Kissinger", bibliographicalData.getAuthors().get(0).getProfile().getName());
        assertEquals("Eric Schmidt III", bibliographicalData.getAuthors().get(1).getProfile().getName());
        assertEquals("Daniel Huttenlocher", bibliographicalData.getAuthors().get(2).getProfile().getName());

        assertEquals(Locale.ENGLISH, bibliographicalData.getOriginalLanguage());

//        assertEquals("John Murray Press", bibliographicalData.getPublisher().getName());

        assertEquals(LocalDate.of(2022, 8, 4), bibliographicalData.getPublishDate());

        assertEquals("Fantasy", bibliographicalData.getGenres().get(0).getTitle());

//        assertEquals(272, bibliographicalData.getNumberOfPages());

        assertEquals("The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.", bibliographicalData.getChunksOfText());
    }

    // Test to verify that title should not be null
    @Test
    void constructor_shouldThrow_whenTitleIsNull() {
        // Arrange
        BibliographicalData sample = sampleBibliographicalData();

        String title = null;

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new BibliographicalData(
                        title,
                        sample.getAuthors(),
                        sample.getOriginalLanguage(),
                        sample.getPublishDate(),
                        sample.getGenres(),
                        sample.getChunksOfText()
                ));
    }

    @Test
    void constructor_shouldThrow_whenTitleIsBlank() {
        // Arrange
        BibliographicalData sample = sampleBibliographicalData();

        String title = "   ";

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new BibliographicalData(
                        title,
                        sample.getAuthors(),
                        sample.getOriginalLanguage(),
                        sample.getPublishDate(),
                        sample.getGenres(),
                        sample.getChunksOfText()
                ));
    }

    // Test Number of Pages Is Positive
//  @Test
//  void testNumberOfPagesIsPositive() {
//        BibliographicalData data = sampleBibliographicalData();
//        assertTrue(data.getNumberOfPages() > 0);
//  }


    // Sample Builder With Empty Authors List
    private BibliographicalData sampleBibliographicalDataWithEmptyAuthorsList() {

        // act
        BibliographicalData bibliographicalData = sampleBibliographicalData();

//      return new BibliographicalData(bibliographicalData.getTitle(), List.of(), bibliographicalData.getLanguage(),
//                    bibliographicalData.getPublisher(), bibliographicalData.getPublishDate(), bibliographicalData.getGenres(), bibliographicalData.getNumberOfPages(),
//                    bibliographicalData.getChunksOfText());
          return new BibliographicalData(bibliographicalData.getTitle(), List.of(), bibliographicalData.getOriginalLanguage(),
                    bibliographicalData.getPublishDate(), bibliographicalData.getGenres(), bibliographicalData.getChunksOfText());
    }

    // Test To Throw Error If Authors Lists Is Empty
    @Test
    void testConstructorThrowsExceptionWhenAuthorsListIsEmpty() {

        // assert
        assertThrows(IllegalArgumentException.class, this::sampleBibliographicalDataWithEmptyAuthorsList);
    }

    // Sample Builder With Empty Genres List
    private BibliographicalData sampleBibliographicalDataWithEmptyGenresList() {

        // act
        BibliographicalData bibliographicalData = sampleBibliographicalData();

//      return new BibliographicalData(bibliographicalData.getTitle(), bibliographicalData.getAuthors(), bibliographicalData.getLanguage(),
//                    bibliographicalData.getPublisher(), bibliographicalData.getPublishDate(), List.of(), bibliographicalData.getNumberOfPages(),
//                    bibliographicalData.getChunksOfText());
        return new BibliographicalData(bibliographicalData.getTitle(), bibliographicalData.getAuthors(), bibliographicalData.getOriginalLanguage(),
                bibliographicalData.getPublishDate(), List.of(),bibliographicalData.getChunksOfText());
    }

    // Test To Throw Error If Genre Lists Is Empty
    @Test
    void testConstructorThrowsExceptionWhenGenresListIsEmpty() {

        // assert
        assertThrows(IllegalArgumentException.class, this::sampleBibliographicalDataWithEmptyGenresList);
    }

    // Sample Builder With Number of Pages Not Positive
//  private BibliographicalData sampleBibliographicalDataWithNumberOfPagesNotPositive() {
//
//      // act
//      BibliographicalData bibliographicalData = sampleBibliographicalData();
//
//      return new BibliographicalData(bibliographicalData.getTitle(), bibliographicalData.getAuthors(), bibliographicalData.getLanguage(),
//                    bibliographicalData.getPublisher(), bibliographicalData.getPublishDate(), bibliographicalData.getGenres(), 0,
//                    bibliographicalData.getChunksOfText());
//  }

    // Test To Throw Error If NumberOfPages Is Not Positive
//    @Test
//    void testConstructorThrowsExceptionWhenNumberOfPagesNotPositive() {
//
//        // assert
//        assertThrows(IllegalArgumentException.class, this::sampleBibliographicalDataWithNumberOfPagesNotPositive);
//    }
//
// ============================================================
// ===================== Tests for hasGenre ====================
// ============================================================

    @Test
    void hasGenre_shouldReturnFalse_whenGenreIsNull() {
        // Arrange
        BibliographicalData bd = sampleBibliographicalData();

        // Act
        boolean result = bd.hasGenre(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void hasGenre_shouldReturnTrue_whenGenreExists() {
        // Arrange
        BibliographicalData bd = sampleBibliographicalData();
        Genre existing = bd.getGenres().get(0); // já vem com "Fantasy"

        // Act
        boolean result = bd.hasGenre(existing);

        // Assert
        assertTrue(result);
    }

    @Test
    void hasGenre_shouldReturnFalse_whenGenreDoesNotExist() {
        // Arrange
        BibliographicalData bd = sampleBibliographicalData();
        Genre nonExisting = new Genre("Sci-Fi");

        // Act
        boolean result = bd.hasGenre(nonExisting);

        // Assert
        assertFalse(result);
    }

// ============================================================
// =============== Tests for equals and hashCode ===============
// ============================================================

    @Test
    void equals_shouldBeReflexive() {
        // Arrange
        BibliographicalData bd = sampleBibliographicalData();

        // Act
        boolean result = bd.equals(bd);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_shouldReturnTrue_forObjectsWithSameContent() {
        // Arrange
        BibliographicalData bd1 = sampleBibliographicalData();
        BibliographicalData bd2 = sampleBibliographicalData();

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertTrue(result);
    }

    //TODO - este teste falha porque a classe Author não tem hashCode implementado. Rever
//    @Test
//    void hashCode_shouldBeEqual_whenObjectsAreEqual() {
//        // Arrange
//        BibliographicalData bd1 = sampleBibliographicalData();
//        BibliographicalData bd2 = sampleBibliographicalData();
//
//        // Act
//        boolean equalsResult = bd1.equals(bd2);
//        int hash1 = bd1.hashCode();
//        int hash2 = bd2.hashCode();
//
//        // Assert
//        assertTrue(equalsResult);
//        assertEquals(hash1, hash2);
//    }

    @Test
    void equals_shouldReturnFalse_whenComparedWithNull() {
        // Arrange
        BibliographicalData bd = sampleBibliographicalData();

        // Act
        boolean result = bd.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange
        BibliographicalData bd = sampleBibliographicalData();

        // Act
        boolean result = bd.equals("string");

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenTitleIsDifferent() {
        // Arrange
        BibliographicalData bd1 = sampleBibliographicalData();
        BibliographicalData bd2 = new BibliographicalData(
                "Different Title",
                bd1.getAuthors(),
                bd1.getOriginalLanguage(),
                bd1.getPublishDate(),
                bd1.getGenres(),
                bd1.getChunksOfText()
        );

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCode_shouldBeDifferent_whenObjectsAreNotEqual() {
        // Arrange
        BibliographicalData bd1 = sampleBibliographicalData();
        BibliographicalData bd2 = new BibliographicalData(
                "Different Title",
                bd1.getAuthors(),
                bd1.getOriginalLanguage(),
                bd1.getPublishDate(),
                bd1.getGenres(),
                bd1.getChunksOfText()
        );

        // Act
        int hash1 = bd1.hashCode();
        int hash2 = bd2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
    }

    @Test
    void equals_shouldReturnFalse_whenAuthorsAreDifferent() {
        // Arrange
        BibliographicalData bd1 = sampleBibliographicalData();

        Country country = new Country("UNITED_STATES");
        Profile p = new Profile("Different Author", country, LocalDate.of(1990, 1, 1));
        Author a = new Author(p, "bio");

        BibliographicalData bd2 = new BibliographicalData(
                bd1.getTitle(),
                List.of(a),
                bd1.getOriginalLanguage(),
                bd1.getPublishDate(),
                bd1.getGenres(),
                bd1.getChunksOfText()
        );

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenLanguageIsDifferent() {
        // Arrange
        BibliographicalData bd1 = sampleBibliographicalData();

        BibliographicalData bd2 = new BibliographicalData(
                bd1.getTitle(),
                bd1.getAuthors(),
                Locale.FRENCH,
                bd1.getPublishDate(),
                bd1.getGenres(),
                bd1.getChunksOfText()
        );

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenPublishDateIsDifferent() {
        // Arrange
        BibliographicalData bd1 = sampleBibliographicalData();

        BibliographicalData bd2 = new BibliographicalData(
                bd1.getTitle(),
                bd1.getAuthors(),
                bd1.getOriginalLanguage(),
                LocalDate.of(1999, 1, 1),
                bd1.getGenres(),
                bd1.getChunksOfText()
        );

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenGenresAreDifferent() {
        // Arrange
        BibliographicalData bd1 = sampleBibliographicalData();

        BibliographicalData bd2 = new BibliographicalData(
                bd1.getTitle(),
                bd1.getAuthors(),
                bd1.getOriginalLanguage(),
                bd1.getPublishDate(),
                List.of(new Genre("Sci-Fi")),
                bd1.getChunksOfText()
        );

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenChunksOfTextAreDifferent() {
        // Arrange
        BibliographicalData bd1 = sampleBibliographicalData();

        BibliographicalData bd2 = new BibliographicalData(
                bd1.getTitle(),
                bd1.getAuthors(),
                bd1.getOriginalLanguage(),
                bd1.getPublishDate(),
                bd1.getGenres(),
                "Different text"
        );

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }
}