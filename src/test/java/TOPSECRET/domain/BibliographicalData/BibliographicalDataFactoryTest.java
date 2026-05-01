package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class BibliographicalDataFactoryTest {

    String title;
    List<AuthorId> authors;
    Locale originalLanguage;
    LocalDate publicationDate;
    List<GenreID> genres;
    String chunksOfText;

    @BeforeEach
    void setUp() {
        title = "title";
        authors = List.of(new AuthorId("author1"));
        originalLanguage = Locale.ENGLISH;
        publicationDate = LocalDate.of(2017,4,24);
        genres = List.of(new GenreID("genre1"));
        chunksOfText = "chunks of text";
    }

    // Constructor
    @Test
    void shouldReturnBibliographicalDataWithGivenValues() {

        //Arrange
        BibliographicalDataFactory factory = new BibliographicalDataFactory();

        // Act
        BibliographicalData bd = factory.create(
                title,
                authors,
                originalLanguage,
                publicationDate,
                genres,
                chunksOfText
        );

        // Assert
        assertNotNull(bd);
        assertEquals(title, bd.getTitle());
        assertEquals(authors, bd.getAuthors());
        assertEquals(originalLanguage, bd.getOriginalLanguage());
        assertEquals(publicationDate, bd.getPublicationDate());
        assertEquals(genres, bd.getGenres());
        assertEquals(chunksOfText, bd.getChunksOfText());
    }

    // Reconstructor
    @Test
    void shouldReturnReconstructedBibliographicalDataWithID() {

        // Arrange
        BibliographicalDataFactory factory = new BibliographicalDataFactory();
        BibliographicalDataID bdID = new BibliographicalDataID("1234");

        // Act
        BibliographicalData bd = factory.create(
                bdID,
                title,
                authors,
                originalLanguage,
                publicationDate,
                genres,
                chunksOfText
        );

        // Assert
        assertNotNull(bd);
        assertEquals(bdID, bd.id());
    }

}