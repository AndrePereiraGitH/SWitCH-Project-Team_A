package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BibliographicalDataDMTest {

    BibliographicalDataID id;
    GenreID genreId;
    AuthorId authorId;

    @BeforeEach
    void setUp() {
        id = new BibliographicalDataID("1234");
        genreId = new GenreID("5678");
        authorId = new AuthorId("3456");
    }

    @Test
    void shouldCreateBibliographicalDataDMWithAllFields() {

        // Arrange
        LocalDate publicationDate = LocalDate.of(2026, 4, 28);

        BibliographicalDataDM dm = new BibliographicalDataDM(
                id,
                "Title",
                "English",
                publicationDate,
                "Some chunks of text",
                List.of(authorId),
                List.of(genreId)
        );

        // Assert
        assertEquals(id, dm.getId());
        assertEquals("Title", dm.getTitle());
        assertEquals("English", dm.getOriginalLanguage());
        assertEquals(publicationDate, dm.getPublicationDate());
        assertEquals("Some chunks of text", dm.getChunksOfText());
        assertEquals(List.of(authorId), dm.getAuthorIds());
        assertEquals(List.of(genreId), dm.getGenreIds());
    }

    @Test
    void shouldAllowNullPublicationDateOriginalLanguageAndChunksOfText() {

        // Arrange
        BibliographicalDataDM dm = new BibliographicalDataDM(
                id,
                "Title",
                null,
                null,
                null,
                List.of(),
                List.of()
        );

        // Assert
        assertNull(dm.getPublicationDate());
        assertNull(dm.getChunksOfText());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {

        // Arrange + Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new BibliographicalDataDM(
                        null,
                        "Title",
                        null,
                        null,
                        null,
                        List.of(),
                        List.of()
                )
        );

        // Assert
        assertEquals("Id cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {

        // Arrange + Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new BibliographicalDataDM(
                        id,
                        null,
                        "English",
                        null,
                        null,
                        List.of(),
                        List.of()
                )
        );

        // Assert
        assertEquals("Title cannot be null", exception.getMessage());
    }

    @Test
    void shouldDefensivelyCopyAuthorIdsList() {

        // Arrange
        AuthorId anotherAuthorId = new AuthorId("9012");

        List<AuthorId> authorIds = new java.util.ArrayList<>();
        authorIds.add(authorId);

        BibliographicalDataDM dm = new BibliographicalDataDM(
                id,
                "Title",
                "English",
                null,
                null,
                authorIds,
                List.of()
        );

        // Act
        authorIds.add(anotherAuthorId);

        // Assert
        assertEquals(1, dm.getAuthorIds().size());
        assertEquals(List.of(authorId), dm.getAuthorIds());
    }

    @Test
    void shouldDefensivelyCopyGenreIdsList() {

        // Arrange
        GenreID anotherGenreId = new GenreID("9012");

        List<GenreID> genreIds = new java.util.ArrayList<>();
        genreIds.add(genreId);

        BibliographicalDataDM dm = new BibliographicalDataDM(
                id,
                "Title",
                "English",
                null,
                null,
                List.of(),
                genreIds
        );

        // Act
        genreIds.add(anotherGenreId);

        // Assert
        assertEquals(1, dm.getGenreIds().size());
        assertEquals(List.of(genreId), dm.getGenreIds());
    }

    @Test
    void shouldHaveProtectedNoArgsConstructorForJpa() throws Exception {

        // Arrange
        Constructor<BibliographicalDataDM> constructor =
                BibliographicalDataDM.class.getDeclaredConstructor();

        // Assert
        assertTrue(Modifier.isProtected(constructor.getModifiers()));
    }

    @Test
    void shouldThrowExceptionWhenAuthorIdsIsNull() {

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> new BibliographicalDataDM(
                        id,
                        "Title",
                        "English",
                        null,
                        null,
                        null,
                        List.of()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenGenreIdsIsNull() {

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> new BibliographicalDataDM(
                        id,
                        "Title",
                        "English",
                        null,
                        null,
                        List.of(),
                        null
                )
        );
    }
}