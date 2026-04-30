package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.persistence.repositoryjpa.datamodel.BibliographicalDataDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BibliographicalDataAssemblerTest {

    private BibliographicalDataFactory factory;
    private BibliographicalData domain;
    private BibliographicalDataAssembler assembler;

    @BeforeEach
    void setUp() {
        factory = mock(BibliographicalDataFactory.class);
        domain = mock(BibliographicalData.class);
        assembler = new BibliographicalDataAssembler(factory);
    }

    @Test
    void toDM_shouldMapDomainToDataModel() {
        BibliographicalDataID id = new BibliographicalDataID("1234");
        AuthorId authorId = new AuthorId("5678");
        GenreID genreId = new GenreID("9012");
        LocalDate publicationDate = LocalDate.of(2026, 4, 28);

        when(domain.id()).thenReturn(id);
        when(domain.getTitle()).thenReturn("Title test");
        when(domain.getOriginalLanguage()).thenReturn(Locale.ENGLISH);
        when(domain.getPublicationDate()).thenReturn(publicationDate);
        when(domain.getChunksOfText()).thenReturn("Some text");
        when(domain.getAuthors()).thenReturn(List.of(authorId));
        when(domain.getGenres()).thenReturn(List.of(genreId));

        BibliographicalDataDM result = assembler.toDM(domain);

        assertEquals(id, result.getId());
        assertEquals("Title test", result.getTitle());
        assertEquals(Locale.ENGLISH.toString(), result.getOriginalLanguage());
        assertEquals(publicationDate, result.getPublicationDate());
        assertEquals("Some text", result.getChunksOfText());
        assertEquals(List.of(authorId), result.getAuthorIds());
        assertEquals(List.of(genreId), result.getGenreIds());
    }

    @Test
    void toDomain_shouldMapDataModelToDomainUsingFactory() {
        BibliographicalDataID id = new BibliographicalDataID("1234");
        AuthorId authorId = new AuthorId();
        GenreID genreId = new GenreID();
        LocalDate publicationDate = LocalDate.of(2024, 1, 15);

        BibliographicalDataDM dm = new BibliographicalDataDM(
                id,
                "Title test",
                "en",
                publicationDate,
                "Some text",
                List.of(authorId),
                List.of(genreId)
        );

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                id,
                "Title test",
                List.of(authorId),
                Locale.ENGLISH,
                publicationDate,
                List.of(genreId),
                "Some text"
        )).thenReturn(expectedDomain);

        BibliographicalData result = assembler.toDomain(dm);

        assertSame(expectedDomain, result);

        verify(factory).create(
                id,
                "Title test",
                List.of(authorId),
                Locale.ENGLISH,
                publicationDate,
                List.of(genreId),
                "Some text"
        );
    }

    @Test
    void toDomain_shouldConvertUnderscoreLanguageToLocale() {
        BibliographicalDataID id = new BibliographicalDataID("1234");

        BibliographicalDataDM dm = new BibliographicalDataDM(
                id,
                "Title",
                "pt_PT",
                null,
                null,
                List.of(),
                List.of()
        );

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                eq(id),
                eq("Title"),
                eq(List.of()),
                eq(Locale.forLanguageTag("pt-pt")),
                isNull(),
                eq(List.of()),
                isNull()
        )).thenReturn(expectedDomain);

        BibliographicalData result = assembler.toDomain(dm);

        assertSame(expectedDomain, result);

        verify(factory).create(
                eq(id),
                eq("Title"),
                eq(List.of()),
                eq(Locale.forLanguageTag("pt-pt")),
                isNull(),
                eq(List.of()),
                isNull()
        );
    }

    @Test
    void toDomain_shouldTrimLanguageBeforeConvertingToLocale() {
        BibliographicalDataID id = new BibliographicalDataID("1234");

        BibliographicalDataDM dm = new BibliographicalDataDM(
                id,
                "Title",
                "  EN_us  ",
                null,
                null,
                List.of(),
                List.of()
        );

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                eq(id),
                eq("Title"),
                eq(List.of()),
                eq(Locale.forLanguageTag("en-us")),
                isNull(),
                eq(List.of()),
                isNull()
        )).thenReturn(expectedDomain);

        BibliographicalData result = assembler.toDomain(dm);

        assertSame(expectedDomain, result);
    }

    @Test
    void toDomain_shouldPassNullLocaleWhenLanguageIsBlank() {
        BibliographicalDataID id = new BibliographicalDataID("1234");

        BibliographicalDataDM dm = new BibliographicalDataDM(
                id,
                "Title",
                "   ",
                null,
                null,
                List.of(),
                List.of()
        );

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                eq(id),
                eq("Title"),
                eq(List.of()),
                isNull(),
                isNull(),
                eq(List.of()),
                isNull()
        )).thenReturn(expectedDomain);

        BibliographicalData result = assembler.toDomain(dm);

        assertSame(expectedDomain, result);
    }

    @Test
    void toDomain_shouldPassNullLocaleWhenLanguageIsNull() {
        BibliographicalDataID id = new BibliographicalDataID("1234");

        BibliographicalDataDM dm = mock(BibliographicalDataDM.class);
        when(dm.getId()).thenReturn(id);
        when(dm.getTitle()).thenReturn("Title");
        when(dm.getAuthorIds()).thenReturn(List.of());
        when(dm.getOriginalLanguage()).thenReturn(null);
        when(dm.getPublicationDate()).thenReturn(null);
        when(dm.getGenreIds()).thenReturn(List.of());
        when(dm.getChunksOfText()).thenReturn(null);

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                eq(id),
                eq("Title"),
                eq(List.of()),
                isNull(),
                isNull(),
                eq(List.of()),
                isNull()
        )).thenReturn(expectedDomain);

        BibliographicalData result = assembler.toDomain(dm);

        assertSame(expectedDomain, result);
    }

}