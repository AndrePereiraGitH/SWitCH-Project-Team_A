package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BibliographicalDataFactoryTest {

    String title;
    List<AuthorId> authors;
    Locale originalLanguage;
    LocalDate publicationDate;
    List<GenreId> genres;
    String chunksOfText;

    @Mock
    AuthorId authorId;

    @Mock
    GenreId genreId;

    @Mock
    BibliographicalDataId bdID;

    @BeforeEach
    void setUp() {
        title = "title";
        authors = List.of(authorId);
        originalLanguage = Locale.ENGLISH;
        publicationDate = LocalDate.of(2017,4,24);
        genres = List.of(genreId);
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
        assertEquals(title, bd.getTitle());
        assertEquals(authors, bd.getAuthors());
        assertEquals(originalLanguage, bd.getOriginalLanguage());
        assertEquals(publicationDate, bd.getPublicationDate());
        assertEquals(genres, bd.getGenres());
        assertEquals(chunksOfText, bd.getChunksOfText());
    }

}