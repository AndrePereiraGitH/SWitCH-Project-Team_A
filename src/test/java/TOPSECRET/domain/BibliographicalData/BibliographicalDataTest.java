//package TOPSECRET.domain.BibliographicalData;
//
//import TOPSECRET.domain.Author.Author;
//import TOPSECRET.domain.Genre.Genre;
//import TOPSECRET.domain.ValueObjects.AuthorId;
//import TOPSECRET.domain.ValueObjects.GenreID;
//import TOPSECRET.domain.ValueObjects.Profile;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class BibliographicalDataTest {
//
//    // ============================================================
//    // ===================== SetUp ================================
//    // ============================================================
//
//    private String title;
//    private List<AuthorId> authors;
//    private List<AuthorId> differentAuthors;
//    private Locale language;
//    private LocalDate pubDate;
//    private List<GenreID> genres;
//    private List<GenreID> differentGenres;
//    private String chunksOfText;
//    private BibliographicalDataFactory bdFactory;
//
//    @BeforeEach
//    void setup() {
//        title = "The Age of AI: And Our Human Future";
//
//        bdFactory = new  BibliographicalDataFactory();
//
//        AuthorId author1Double = mock(AuthorId.class);
//        Profile profile1Double = mock(Profile.class);
//        when(profile1Double.getName()).thenReturn("Daniel Huttenlocher");
//
//        AuthorId author2Double = mock(AuthorId.class);
//        Profile profile2Double = mock(Profile.class);
//        when(profile2Double.getName()).thenReturn("Eric Schmidt III");
//
//        AuthorId author3Double = mock(AuthorId.class);
//        Profile profile3Double = mock(Profile.class);
//        when(profile3Double.getName()).thenReturn("Daniel Huttenlocher");
//
//        authors = List.of(author1Double, author2Double, author3Double);
//        differentAuthors = List.of(author1Double, author2Double);
//
//        language = Locale.ENGLISH;
//
//        pubDate = LocalDate.of(2022, 8, 4);
//
//        GenreID genre1Double = mock(GenreID.class);
//        when(genre1Double.getGenreType()).thenReturn("Fiction");
//        Genre genre2Double = mock(Genre.class);
//        when(genre2Double.getGenreType()).thenReturn("Biography");
//        genres = List.of(genre1Double, genre2Double);
//        differentGenres = List.of(genre1Double);
//
//        chunksOfText = "The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.";
//    }
//
//    // ============================================================
//    // ===================== Tests to Constructor =================
//    // ============================================================
//
//    @Test
//    void constructor_initializedMoreThanOneAuthorAndGenre() {
//        // Act
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Assert
//        assertEquals(title, bd1Double.getTitle());
//        assertEquals(3, bd1Double.getAuthors().size());
//        assertEquals(language, bd1Double.getOriginalLanguage());
//        assertEquals(pubDate, bd1Double.getPublicationDate());
//        assertEquals(genres, bd1Double.getGenres());
//        assertEquals(chunksOfText, bd1Double.getChunksOfText());
//    }
//
//    @Test
//    void constructor_shouldThrowWhenTitleIsNull() {
//        // Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                bdFactory.create(
//                        null,
//                        authors,
//                        language,
//                        pubDate,
//                        genres,
//                        chunksOfText
//                )
//        );
//    }
//
//    @Test
//    void constructor_shouldThrowWhenTitleIsBlank() {
//        // Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                bdFactory.create(
//                        " ",
//                        authors,
//                        language,
//                        pubDate,
//                        genres,
//                        chunksOfText
//                )
//        );
//    }
//
//    @Test
//    void constructor_ThrowsExceptionWhenAuthorsListIsEmpty() {
//        // Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                bdFactory.create(
//                        title,
//                        List.of(),
//                        language,
//                        pubDate,
//                        genres,
//                        chunksOfText
//                )
//        );
//    }
//
//    @Test
//    void constructor_ThrowsExceptionWhenGenresListIsEmpty() {
//        // Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                bdFactory.create(
//                        title,
//                        authors,
//                        language,
//                        pubDate,
//                        List.of(),
//                        chunksOfText
//                )
//        );
//    }
//
//    // ============================================================
//    // ===================== Tests for hasGenre ===================
//    // ============================================================
//
//    @Test
//    void hasGenre_shouldReturnFalseWhenGenreIsNull() {
//        // Arrange
//        BibliographicalData bdDouble = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        boolean result = bdDouble.hasGenre(null);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void hasGenre_shouldReturnTrueWhenGenreExists() {
//        // Arrange
//        BibliographicalData bdDouble = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        Genre existingGenre = genres.get(0);                                                // gets an existing genre
//
//        // Act
//        boolean result = bdDouble.hasGenre(existingGenre);
//
//        // Assert
//        assertTrue(result);
//    }
//
//    @Test
//    void hasGenre_shouldReturnFalseWhenGenreDoesNotExist() {
//        // Arrange
//        BibliographicalData bdDouble = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        Genre nonExistingGenreDouble = mock(Genre.class);                    // we can't compare mocks with real objects
//        when(nonExistingGenreDouble.getGenreType()).thenReturn("Sci-Fi");
//
//        // Act
//        boolean result = bdDouble.hasGenre(nonExistingGenreDouble);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    // ============================================================
//    // =============== Tests for equals and hashCode ==============
//    // ============================================================
//
//    @Test
//    void equals_shouldBeReflexive() {
//        // Arrange
//        BibliographicalData bdDouble = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        boolean result = bdDouble.equals(bdDouble);
//
//        // Assert
//        assertTrue(result);
//    }
//
//    @Test
//    void equals_shouldReturnTrueForObjectsWithSameContent() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        boolean result = bd1Double.equals(bd2Double);
//
//        // Assert
//        assertTrue(result);
//    }
//
//    @Test
//    void hashCode_shouldBeEqualWhenObjectsAreEqual() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        boolean equalsResult = bd1Double.equals(bd2Double);
//        int hash1 = bd1Double.hashCode();
//        int hash2 = bd2Double.hashCode();
//
//        // Assert
//        assertTrue(equalsResult);
//        assertEquals(hash1, hash2);
//    }
//
//    @Test
//    void equals_shouldReturnFalseWhenComparedWithNull() {
//        // Arrange
//        BibliographicalData bdDouble = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        boolean result = bdDouble.equals(null);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void equals_shouldReturnFalseWhenComparedWithDifferentType() {
//        // Arrange
//        BibliographicalData bdDouble = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        boolean result = bdDouble.equals("string");
//
//        // Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void equals_shouldReturnFalseWhenTitleIsDifferent() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create("Different title", authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        boolean result = bd1Double.equals(bd2Double);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void hashCode_shouldBeDifferentWhenObjectsAreNotEqual() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create("Different title", authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        int hash1 = bd1Double.hashCode();
//        int hash2 = bd2Double.hashCode();
//
//        // Assert
//        assertNotEquals(hash1, hash2);
//    }
//
//    @Test
//    void equals_shouldReturnFalseWhenAuthorsAreDifferent() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create(title, differentAuthors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        boolean result = bd1Double.equals(bd2Double);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void equals_shouldReturnFalseWhenLanguageIsDifferent() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create(title, differentAuthors, Locale.FRENCH, pubDate, genres, chunksOfText);
//
//        // Act
//        boolean result = bd1Double.equals(bd2Double);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void equals_shouldReturnFalseWhenPublicationDateIsDifferent() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create(title, differentAuthors, language, LocalDate.of(2026, 2, 27), genres, chunksOfText);
//
//        // Act
//        boolean result = bd1Double.equals(bd2Double);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void equals_shouldReturnFalseWhenGenresAreDifferent() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create(title, authors, language, pubDate, differentGenres, chunksOfText);
//
//        // Act
//        boolean result = bd1Double.equals(bd2Double);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void equals_shouldReturnFalseWhenChunksOfTextAreDifferent() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create(title, authors, language, pubDate, differentGenres, "Different text");
//
//        // Act
//        boolean result = bd1Double.equals(bd2Double);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    // ============================================================
//    // ========= Tests for getBibliographicalDataDetails ==========
//    // ============================================================
//
//    @Test
//    void getBibliographicalDataDetails_ShouldNotBeNull() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails detail = bd1Double.getBibliographicalDataDetails();
//
//        // Assert
//        assertNotNull(detail);
//
//    }
//
//    @Test
//    void getBibliographicalDataDetails_ShouldReturnCorrectFields() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails detail = bd1Double.getBibliographicalDataDetails();
//
//        // Assert
//        assertEquals(title, detail.title());
//        assertEquals(authors, detail.authors());
//        assertEquals(language, detail.originalLanguage());
//        assertEquals(pubDate, detail.publicationDate());
//        assertEquals(genres, detail.genres());
//        assertEquals(chunksOfText, detail.chunksOfText());
//    }
//
//    @Test
//    void getBibliographicalDataDetails_ShouldReturnOriginalLanguageNull() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, null, pubDate, genres, chunksOfText);
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails detail = bd1Double.getBibliographicalDataDetails();
//
//        // Assert
//        assertEquals(title, detail.title());
//        assertEquals(authors, detail.authors());
//        assertEquals(null, detail.originalLanguage());
//        assertEquals(pubDate, detail.publicationDate());
//        assertEquals(genres, detail.genres());
//        assertEquals(chunksOfText, detail.chunksOfText());
//    }
//
//    @Test
//    void getBibliographicalDataDetails_ShouldReturnPublicationDateNull() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, null, genres, chunksOfText);
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails detail = bd1Double.getBibliographicalDataDetails();
//
//        // Assert
//        assertEquals(title, detail.title());
//        assertEquals(authors, detail.authors());
//        assertEquals(language, detail.originalLanguage());
//        assertEquals(null, detail.publicationDate());
//        assertEquals(genres, detail.genres());
//        assertEquals(chunksOfText, detail.chunksOfText());
//    }
//
//    @Test
//    void getBibliographicalDataDetails_ShouldReturnChunksOfTextNull() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, null);
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails detail = bd1Double.getBibliographicalDataDetails();
//
//        // Assert
//        assertEquals(title, detail.title());
//        assertEquals(authors, detail.authors());
//        assertEquals(language, detail.originalLanguage());
//        assertEquals(pubDate, detail.publicationDate());
//        assertEquals(genres, detail.genres());
//        assertEquals(null, detail.chunksOfText());
//    }
//
//    @Test
//    void getBibliographicalDataDetails_ShouldNotReflectChangesInOriginalAuthorsList() {
//        // Arrange
//        List<Author> originalAuthors = new ArrayList<>(authors);
//        BibliographicalData bd = bdFactory.create(title, originalAuthors, language, pubDate, genres, chunksOfText);
//
//        BibliographicalData.BibliographicalDataDetails details = bd.getBibliographicalDataDetails();
//
//        // New author
//        Author author4Double = mock(Author.class);
//        Profile profile4Double = mock(Profile.class);
//        when(profile4Double.getName()).thenReturn("John Green");
//
//        // Act
//        originalAuthors.add(author4Double);
//
//        // Assert
//        assertEquals(authors.size(), details.authors().size());
//    }
//
//    @Test
//    void getBibliographicalDataDetails_ShouldNotReflectChangesInOriginalGenresList() {
//        // Arrange
//        List<Genre> originalGenres = new ArrayList<>(genres);
//        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, originalGenres, chunksOfText);
//
//        BibliographicalData.BibliographicalDataDetails details = bd.getBibliographicalDataDetails();
//
//        // New Genre
//        Genre genre3Double = mock(Genre.class);
//        when(genre3Double.getGenreType()).thenReturn("Young Adult");
//
//        // Act
//        originalGenres.add(genre3Double);
//
//        // Assert
//        assertEquals(genres.size(), details.genres().size());
//    }
//
//    // ================================================================
//    // ========= Tests for BibliographicalDataDetails record ==========
//    // ================================================================
//
//    @Test
//    void bibliographicalDataDetails_ShouldReturnEqualsForTheSameValues() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails detail1 = bd1Double.getBibliographicalDataDetails();
//        BibliographicalData.BibliographicalDataDetails detail2 = bd2Double.getBibliographicalDataDetails();
//
//        // Assert
//        assertEquals(detail1, detail2);
//    }
//
//    @Test
//    void bibliographicalDataDetails_ShouldReturnNotEqualsForDifferentValues() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//        BibliographicalData bd2Double = bdFactory.create("Different title", authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails detail1 = bd1Double.getBibliographicalDataDetails();
//        BibliographicalData.BibliographicalDataDetails detail2 = bd2Double.getBibliographicalDataDetails();
//
//        // Assert
//        assertNotEquals(detail1, detail2);
//    }
//
//    @Test
//    void bibliographicalDataDetails_ShouldReturnImmutableListOfAuthors() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // New author
//        Author author4Double = mock(Author.class);
//        when(author4Double.getName()).thenReturn("John Green");
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails detail = bd1Double.getBibliographicalDataDetails();
//
//        // Assert
//        assertThrows(UnsupportedOperationException.class, () -> detail.authors().add(author4Double));
//    }
//
//    @Test
//    void bibliographicalDataDetails_ShouldReturnImmutableListOfGenres() {
//        // Arrange
//        BibliographicalData bd1Double = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // New genre
//        Genre genre3Double = mock(Genre.class);
//        when(genre3Double.getGenreType()).thenReturn("Young adult");
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails detail = bd1Double.getBibliographicalDataDetails();
//
//        // Assert
//        assertThrows(UnsupportedOperationException.class, () -> detail.genres().add(genre3Double));
//    }
//
//    @Test
//    void bibliographicalDataDetails_ShouldReconstructEquivalentObject() {
//        // Arrange
//        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
//
//        // Act
//        BibliographicalData.BibliographicalDataDetails details = bd1.getBibliographicalDataDetails();
//
//        BibliographicalData bd2 = bdFactory.create(details.title(), details.authors(), details.originalLanguage(),
//                details.publicationDate(), details.genres(), details.chunksOfText());
//
//        // Assert
//        assertEquals(bd1, bd2);
//    }
//}