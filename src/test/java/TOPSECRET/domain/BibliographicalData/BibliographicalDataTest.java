package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BibliographicalDataTest {

    // ============================================================
    // ===================== SetUp ================================
    // ============================================================

    private String title;
    private List<AuthorId> authors;
    private List<AuthorId> differentAuthors;
    private IAuthorRepository authorRepo;
    private Locale language;
    private LocalDate pubDate;
    private List<GenreID> genres;
    private List<GenreID> differentGenres;
    private IGenreRepository genreRepo;
    private String chunksOfText;
    private BibliographicalDataFactory bdFactory;

    @BeforeEach
    void setup() {
        title = "The Age of AI: And Our Human Future";

        bdFactory = new  BibliographicalDataFactory();

        // Author

        authorRepo = mock(IAuthorRepository.class);

        AuthorId author1Double = mock(AuthorId.class);
        Profile profile1Double = mock(Profile.class);
        when(profile1Double.getName()).thenReturn("Daniel Huttenlocher");

        AuthorId author2Double = mock(AuthorId.class);
        Profile profile2Double = mock(Profile.class);
        when(profile2Double.getName()).thenReturn("Eric Schmidt III");

        AuthorId author3Double = mock(AuthorId.class);
        Profile profile3Double = mock(Profile.class);
        when(profile3Double.getName()).thenReturn("Daniel Huttenlocher");

        authors = List.of(author1Double, author2Double, author3Double);
        differentAuthors = List.of(author1Double, author2Double);


        // Genre
        genreRepo = mock(IGenreRepository.class);

        GenreID genreId1Double = mock(GenreID.class);
        Genre genre1Double; genre1Double = mock(Genre.class);
        when(genreRepo.findGenreById(genreId1Double)).thenReturn(Optional.ofNullable(genre1Double));
        when(genre1Double.getGenreType()).thenReturn("Fiction");

        GenreID genreId2Double = mock(GenreID.class);
        Genre genre2Double = mock(Genre.class);
        when(genreRepo.findGenreById(genreId2Double)).thenReturn(Optional.ofNullable(genre2Double));
        when(genre2Double.getGenreType()).thenReturn("Biography");

        genres = List.of(genreId1Double, genreId2Double);
        differentGenres = List.of(genreId1Double);

        // Language, Date, chunks of text
        language = Locale.ENGLISH;

        pubDate = LocalDate.of(2022, 8, 4);

        chunksOfText = "The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.";
    }

    // ============================================================
    // ===================== Tests to Constructor =================
    // ============================================================

    @Test
    void constructor_initializedMoreThanOneAuthorAndGenre() {
        // Act
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Assert
        assertEquals(title, bd.getTitle());
        assertEquals(3, bd.getAuthors().size());
        assertEquals(language, bd.getOriginalLanguage());
        assertEquals(pubDate, bd.getPublicationDate());
        assertEquals(genres, bd.getGenres());
        assertEquals(chunksOfText, bd.getChunksOfText());
    }

    @Test
    void constructor_shouldThrowWhenTitleIsNull() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                bdFactory.create(
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
                bdFactory.create(
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
                bdFactory.create(
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
                bdFactory.create(
                        title,
                        authors,
                        language,
                        pubDate,
                        List.of(),
                        chunksOfText
                )
        );
    }

    // Reconstructor tests

    @Test
    void constructor_ShouldReconstructObjectWithProvidedID(){
        // Arrange
        BibliographicalDataID bdID = mock(BibliographicalDataID.class);

        // Act
        BibliographicalData bd = bdFactory.create(bdID, title, authors, language, pubDate, genres, chunksOfText);

        // Assert
        assertEquals(bdID, bd.id());
        assertEquals(title, bd.getTitle());
        assertEquals(3, bd.getAuthors().size());
        assertEquals(language, bd.getOriginalLanguage());
        assertEquals(pubDate, bd.getPublicationDate());
        assertEquals(genres, bd.getGenres());
        assertEquals(chunksOfText, bd.getChunksOfText());
    }

    @Test
    void constructor_ShouldThrowWhenIDisNull(){
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                bdFactory.create(
                        null,
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
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd.hasGenre(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void hasGenre_shouldReturnTrueWhenGenreExists() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        GenreID existingGenreId = genres.get(0);

        // Act
        boolean result = bd.hasGenre(existingGenreId);

        // Assert
        assertTrue(result);
    }

    @Test
    void hasGenre_shouldReturnFalseWhenGenreDoesNotExist() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        GenreID nonExistingGenreIdDouble = mock(GenreID.class);

        // Act
        boolean result = bd.hasGenre(nonExistingGenreIdDouble);

        // Assert
        assertFalse(result);
    }

    // ============================================================
    // =============== Tests for equals and hashCode ==============
    // ============================================================

    @Test
    void equals_shouldBeReflexive() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd.equals(bd);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_shouldReturnTrueForObjectsWithSameContent() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertTrue(result);
    }

    @Test
    void hashCode_shouldBeEqualWhenObjectsAreEqual() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean equalsResult = bd1.equals(bd2);
        int hash1 = bd1.hashCode();
        int hash2 = bd2.hashCode();

        // Assert
        assertTrue(equalsResult);
        assertEquals(hash1, hash2);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedWithNull() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd.equals("string");

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenTitleIsDifferent() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create("Different title", authors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCode_shouldBeDifferentWhenObjectsAreNotEqual() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create("Different title", authors, language, pubDate, genres, chunksOfText);

        // Act
        int hash1 = bd1.hashCode();
        int hash2 = bd2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
    }

    @Test
    void equals_shouldReturnFalseWhenAuthorsAreDifferent() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create(title, differentAuthors, language, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenLanguageIsDifferent() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create(title, differentAuthors, Locale.FRENCH, pubDate, genres, chunksOfText);

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenPublicationDateIsDifferent() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create(title, differentAuthors, language, LocalDate.of(2026, 2, 27), genres, chunksOfText);

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenGenresAreDifferent() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create(title, authors, language, pubDate, differentGenres, chunksOfText);

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenChunksOfTextAreDifferent() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create(title, authors, language, pubDate, differentGenres, "Different text");

        // Act
        boolean result = bd1.equals(bd2);

        // Assert
        assertFalse(result);
    }

    // ============================================================
    // ===================== Tests for sameAs =====================
    // ============================================================

    @Test
    void sameAs_shouldReturnTrue_whenComparingSameInstance() {
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        assertTrue(bd.sameAs(bd));
    }

    @Test
    void sameAs_shouldReturnFalse_whenObjectIsNull() {
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        assertFalse(bd.sameAs(null));
    }

    @Test
    void sameAs_shouldReturnFalse_whenObjectIsDifferentClass() {
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        assertFalse(bd.sameAs("not a BibliographicalData"));
    }

    @Test
    void sameAs_shouldReturnFalse_whenComparingDifferentInstances() {
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        assertFalse(bd1.sameAs(bd2));
    }

    // ============================================================
    // ========= Tests for getBibliographicalDataDetails ==========
    // ============================================================

    @Test
    void getBibliographicalDataDetails_ShouldNotBeNull() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        BibliographicalData.BibliographicalDataDetails detail = bd.getBibliographicalDataDetails();

        // Assert
        assertNotNull(detail);

    }

    @Test
    void id_shouldReturnBibliographicalDataID() {
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        BibliographicalDataID id = bd.id();

        assertNotNull(id);
    }

    @Test
    void getBibliographicalDataDetails_ShouldReturnCorrectFields() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        BibliographicalData.BibliographicalDataDetails detail = bd.getBibliographicalDataDetails();

        // Assert
        assertEquals(title, detail.title());
        assertEquals(authors, detail.authors());
        assertEquals(language, detail.originalLanguage());
        assertEquals(pubDate, detail.publicationDate());
        assertEquals(genres, detail.genres());
        assertEquals(chunksOfText, detail.chunksOfText());
    }

    @Test
    void getBibliographicalDataDetails_ShouldReturnPublicationDateNull() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, null, genres, chunksOfText);

        // Act
        BibliographicalData.BibliographicalDataDetails detail = bd.getBibliographicalDataDetails();

        // Assert
        assertEquals(title, detail.title());
        assertEquals(authors, detail.authors());
        assertEquals(language, detail.originalLanguage());
        assertNull(detail.publicationDate());
        assertEquals(genres, detail.genres());
        assertEquals(chunksOfText, detail.chunksOfText());
    }

    @Test
    void getBibliographicalDataDetails_ShouldReturnChunksOfTextNull() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, null);

        // Act
        BibliographicalData.BibliographicalDataDetails detail = bd.getBibliographicalDataDetails();

        // Assert
        assertEquals(title, detail.title());
        assertEquals(authors, detail.authors());
        assertEquals(language, detail.originalLanguage());
        assertEquals(pubDate, detail.publicationDate());
        assertEquals(genres, detail.genres());
        assertNull(detail.chunksOfText());
    }

    @Test
    void getBibliographicalDataDetails_ShouldNotReflectChangesInOriginalAuthorsList() {
        // Arrange
        List<AuthorId> originalAuthorsId = new ArrayList<>(authors);
        BibliographicalData bd = bdFactory.create(title, originalAuthorsId, language, pubDate, genres, chunksOfText);

        BibliographicalData.BibliographicalDataDetails details = bd.getBibliographicalDataDetails();

        // New author
        AuthorId author4IdDouble = mock(AuthorId.class);
        Profile profile4Double = mock(Profile.class);
        when(profile4Double.getName()).thenReturn("John Green");

        // Act
        originalAuthorsId.add(author4IdDouble);

        // Assert
        assertEquals(authors.size(), details.authors().size());
    }

    @Test
    void getBibliographicalDataDetails_ShouldNotReflectChangesInOriginalGenresList() {
        // Arrange
        List<GenreID> originalGenres = new ArrayList<>(genres);
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, originalGenres, chunksOfText);

        BibliographicalData.BibliographicalDataDetails details = bd.getBibliographicalDataDetails();

        // New Genre
        GenreID genre3IdDouble = mock(GenreID.class);
        Genre genre3Double = mock(Genre.class);
        when(genreRepo.findGenreById(genre3IdDouble)).thenReturn(Optional.ofNullable(genre3Double));
        when(genre3Double.getGenreType()).thenReturn("Young Adult");

        // Act
        originalGenres.add(genre3IdDouble);

        // Assert
        assertEquals(genres.size(), details.genres().size());
    }

    // ================================================================
    // ========= Tests for BibliographicalDataDetails record ==========
    // ================================================================

    @Test
    void bibliographicalDataDetails_ShouldReturnEqualsForTheSameValues() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        BibliographicalData.BibliographicalDataDetails detail1 = bd1.getBibliographicalDataDetails();
        BibliographicalData.BibliographicalDataDetails detail2 = bd2.getBibliographicalDataDetails();

        // Assert
        assertEquals(detail1, detail2);
    }

    @Test
    void bibliographicalDataDetails_ShouldReturnNotEqualsForDifferentValues() {
        // Arrange
        BibliographicalData bd1D = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);
        BibliographicalData bd2 = bdFactory.create("Different title", authors, language, pubDate, genres, chunksOfText);

        // Act
        BibliographicalData.BibliographicalDataDetails detail1 = bd1D.getBibliographicalDataDetails();
        BibliographicalData.BibliographicalDataDetails detail2 = bd2.getBibliographicalDataDetails();

        // Assert
        assertNotEquals(detail1, detail2);
    }

    @Test
    void bibliographicalDataDetails_ShouldReturnImmutableListOfAuthors() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // New author
        AuthorId author4IdDouble = mock(AuthorId.class);
        Author author4Double = mock(Author.class);
        when(authorRepo.findById(author4IdDouble)).thenReturn(Optional.of(author4Double));
        when(author4Double.getName()).thenReturn("John Green");

        // Act
        BibliographicalData.BibliographicalDataDetails detail = bd.getBibliographicalDataDetails();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> detail.authors().add(author4IdDouble));
    }

    @Test
    void bibliographicalDataDetails_ShouldReturnImmutableListOfGenres() {
        // Arrange
        BibliographicalData bd = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // New genre
        GenreID genre3IdDouble = mock(GenreID.class);
        Genre genre3Double = mock(Genre.class);
        when(genreRepo.findGenreById(genre3IdDouble)).thenReturn(Optional.of(genre3Double));
        when(genre3Double.getGenreType()).thenReturn("Young adult");

        // Act
        BibliographicalData.BibliographicalDataDetails detail = bd.getBibliographicalDataDetails();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> detail.genres().add(genre3IdDouble));
    }

    @Test
    void bibliographicalDataDetails_ShouldReconstructEquivalentObject() {
        // Arrange
        BibliographicalData bd1 = bdFactory.create(title, authors, language, pubDate, genres, chunksOfText);

        // Act
        BibliographicalData.BibliographicalDataDetails details = bd1.getBibliographicalDataDetails();

        BibliographicalData bd2 = bdFactory.create(details.title(), details.authors(), details.originalLanguage(),
                details.publicationDate(), details.genres(), details.chunksOfText());

        // Assert
        assertEquals(bd1, bd2);
    }
}