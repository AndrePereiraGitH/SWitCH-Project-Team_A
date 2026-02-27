package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BibliographicalDataTest {

    // ============================================================
    // ===================== SetUp ================================
    // ============================================================

    private String title;
    private List<Author> authors;
    private List<Author> differentAuthors;
    private Locale language;
    private LocalDate pubDate;
    private List<Genre> genres;
    private List<Genre> differentGenres;
    private String chunksOfText;

    @BeforeEach
    void setup() {
        title = "The Age of AI: And Our Human Future";

        Author author1Double = mock(Author.class);
        Profile profile1Double = mock(Profile.class);
        when(author1Double.getProfile()).thenReturn(profile1Double);
        when(profile1Double.getName()).thenReturn("Daniel Huttenlocher");

        Author author2Double = mock(Author.class);
        Profile profile2Double = mock(Profile.class);
        when(author2Double.getProfile()).thenReturn(profile2Double);
        when(profile2Double.getName()).thenReturn("Eric Schmidt III");

        Author author3Double = mock(Author.class);
        Profile profile3Double = mock(Profile.class);
        when(author3Double.getProfile()).thenReturn(profile3Double);
        when(profile3Double.getName()).thenReturn("Daniel Huttenlocher");

        authors = List.of(author1Double, author2Double, author3Double);
        differentAuthors = List.of(author1Double, author2Double);

        language = Locale.ENGLISH;

        pubDate = LocalDate.of(2022, 8, 4);

        Genre genre1Double = mock(Genre.class);
        when(genre1Double.getTitle()).thenReturn("Fiction");
        Genre genre2Double = mock(Genre.class);
        when(genre2Double.getTitle()).thenReturn("Biography");
        genres = List.of(genre1Double, genre2Double);
        differentGenres = List.of(genre1Double);

        chunksOfText = "The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.";
    }

    // ============================================================
    // ===================== Tests to Constructor =================
    // ============================================================

    @Test
    void constructor_initializedMoreThanOneAuthorAndGenre() {
        // Act
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);

        // Assert
        assertEquals(title, bd1Double.getTitle());
        assertEquals(3, bd1Double.getAuthors().size());
        assertEquals(language, bd1Double.getOriginalLanguage());
        assertEquals(pubDate, bd1Double.getPublicationDate());
        assertEquals(genres, bd1Double.getGenres());
        assertEquals(chunksOfText, bd1Double.getChunksOfText());
    }

    @Test
    void constructor_shouldThrowWhenTitleIsNull() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                new BibliographicalData(
                        null,
                        authors,
                        language,
                        pubDate,
                        genres,
                        chunksOfText
                )
        );
    }

    @Test
    void constructor_shouldThrowWhenTitleIsBlank() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                new BibliographicalData(
                        " ",
                        authors,
                        language,
                        pubDate,
                        genres,
                        chunksOfText
                )
        );
    }

    @Test
    void constructor_ThrowsExceptionWhenAuthorsListIsEmpty() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                new BibliographicalData(
                        title,
                        List.of(),
                        language,
                        pubDate,
                        genres,
                        chunksOfText
                )
        );
    }

    @Test
    void constructor_ThrowsExceptionWhenGenresListIsEmpty() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                new BibliographicalData(
                        title,
                        authors,
                        language,
                        pubDate,
                        List.of(),
                        chunksOfText
                )
        );
    }

    // ============================================================
    // ===================== Tests for hasGenre ===================
    // ============================================================

    @Test
    void hasGenre_shouldReturnFalseWhenGenreIsNull() {
        // Arrange
        BibliographicalData bdDouble = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bdDouble.hasGenre(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void hasGenre_shouldReturnTrueWhenGenreExists() {
        // Arrange
        BibliographicalData bdDouble = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        Genre existingGenre = genres.get(0);                                                // gets an existing genre

        // Act
        boolean result = bdDouble.hasGenre(existingGenre);

        // Assert
        assertTrue(result);
    }

    @Test
    void hasGenre_shouldReturnFalseWhenGenreDoesNotExist() {
        // Arrange
        BibliographicalData bdDouble = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        Genre nonExistingGenreDouble = mock(Genre.class);                    // we can't compare mocks with real objects
        when(nonExistingGenreDouble.getTitle()).thenReturn("Sci-Fi");

        // Act
        boolean result = bdDouble.hasGenre(nonExistingGenreDouble);

        // Assert
        assertFalse(result);
    }

    // ============================================================
    // =============== Tests for equals and hashCode ==============
    // ============================================================

    @Test
    void equals_shouldBeReflexive() {
        // Arrange
        BibliographicalData bdDouble = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bdDouble.equals(bdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_shouldReturnTrueForObjectsWithSameContent() {
        // Arrange
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd1Double.equals(bd2Double);

        // Assert
        assertTrue(result);
    }

    @Test
    void hashCode_shouldBeEqualWhenObjectsAreEqual() {
        // Arrange
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean equalsResult = bd1Double.equals(bd2Double);
        int hash1 = bd1Double.hashCode();
        int hash2 = bd2Double.hashCode();

        // Assert
        assertTrue(equalsResult);
        assertEquals(hash1, hash2);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedWithNull() {
        // Arrange
        BibliographicalData bdDouble = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bdDouble.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        BibliographicalData bdDouble = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bdDouble.equals("string");

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenTitleIsDifferent() {
        // Arrange
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2Double = new BibliographicalData("Different title", authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd1Double.equals(bd2Double);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCode_shouldBeDifferentWhenObjectsAreNotEqual() {
        // Arrange
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2Double = new BibliographicalData("Different title", authors, language, pubDate, genres, chunksOfText);

        // Act
        int hash1 = bd1Double.hashCode();
        int hash2 = bd2Double.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
    }

    @Test
    void equals_shouldReturnFalseWhenAuthorsAreDifferent() {
        // Arrange
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2Double = new BibliographicalData(title, differentAuthors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd1Double.equals(bd2Double);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenLanguageIsDifferent() {
        // Arrange
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2Double = new BibliographicalData(title, differentAuthors, Locale.FRENCH, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd1Double.equals(bd2Double);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenPublicationDateIsDifferent() {
        // Arrange
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2Double = new BibliographicalData(title, differentAuthors, language, LocalDate.of(2026, 2, 27), genres, chunksOfText);

        // Act
        boolean result = bd1Double.equals(bd2Double);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenGenresAreDifferent() {
        // Arrange
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2Double = new BibliographicalData(title, authors, language, pubDate, differentGenres, chunksOfText);

        // Act
        boolean result = bd1Double.equals(bd2Double);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenChunksOfTextAreDifferent() {
        // Arrange
        BibliographicalData bd1Double = new BibliographicalData(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2Double = new BibliographicalData(title, authors, language, pubDate, differentGenres, "Different text");

        // Act
        boolean result = bd1Double.equals(bd2Double);

        // Assert
        assertFalse(result);
    }
}