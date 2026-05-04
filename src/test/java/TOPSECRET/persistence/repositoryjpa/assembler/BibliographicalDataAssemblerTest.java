package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.persistence.repositoryjpa.datamodel.BibliographicalDataDM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BibliographicalDataAssemblerTest {

    @Mock
    private BibliographicalDataFactory factory;

    @Mock
    private BibliographicalData domain;

    @InjectMocks
    private BibliographicalDataAssembler assembler;

    @Test
    void toDM_shouldMapDomainToDataModel() {
        // Arrange
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        LocalDate publicationDate = LocalDate.of(2026, 4, 28);

        when(domain.id()).thenReturn(bdIdDouble);
        when(domain.getTitle()).thenReturn("Title test");
        when(domain.getOriginalLanguage()).thenReturn(Locale.ENGLISH);
        when(domain.getPublicationDate()).thenReturn(publicationDate);
        when(domain.getChunksOfText()).thenReturn("Some text");
        when(domain.getAuthors()).thenReturn(List.of(authorIdDouble));
        when(domain.getGenres()).thenReturn(List.of(genreIdDouble));

        // Act
        BibliographicalDataDM result = assembler.toDM(domain);

        // Arrange
        assertEquals(bdIdDouble, result.getId());
        assertEquals("Title test", result.getTitle());
        assertEquals(Locale.ENGLISH.toString(), result.getOriginalLanguage());
        assertEquals(publicationDate, result.getPublicationDate());
        assertEquals("Some text", result.getChunksOfText());
        assertEquals(List.of(authorIdDouble), result.getAuthorIds());
        assertEquals(List.of(genreIdDouble), result.getGenreIds());
    }

    @Test
    void toDomain_shouldMapDataModelToDomainUsingFactory() {
        // Arrange
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        LocalDate publicationDate = LocalDate.of(2024, 1, 15);

        BibliographicalDataDM dm = new BibliographicalDataDM(
                bdIdDouble,
                "Title test",
                "en",
                publicationDate,
                "Some text",
                List.of(authorIdDouble),
                List.of(genreIdDouble)
        );

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                bdIdDouble,
                "Title test",
                List.of(authorIdDouble),
                Locale.ENGLISH,
                publicationDate,
                List.of(genreIdDouble),
                "Some text"
        )).thenReturn(expectedDomain);

        // Act
        BibliographicalData result = assembler.toDomain(dm);

        // Assert
        assertSame(expectedDomain, result);
    }

    @Test
    void toDomain_shouldConvertUnderscoreLanguageToLocale() {
        // Arrange
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        BibliographicalDataDM dm = new BibliographicalDataDM(
                bdIdDouble,
                "Title",
                "pt_PT",
                null,
                null,
                List.of(),
                List.of()
        );

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                eq(bdIdDouble),
                eq("Title"),
                eq(List.of()),
                eq(Locale.forLanguageTag("pt-pt")),
                isNull(),
                eq(List.of()),
                isNull()
        )).thenReturn(expectedDomain);

        // Act
        BibliographicalData result = assembler.toDomain(dm);

        // Assert
        assertSame(expectedDomain, result);
    }

    @Test
    void toDomain_shouldTrimLanguageBeforeConvertingToLocale() {
        // Arrange
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        BibliographicalDataDM dm = new BibliographicalDataDM(
                bdIdDouble,
                "Title",
                "  EN_us  ",
                null,
                null,
                List.of(),
                List.of()
        );

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                eq(bdIdDouble),
                eq("Title"),
                eq(List.of()),
                eq(Locale.forLanguageTag("en-us")),
                isNull(),
                eq(List.of()),
                isNull()
        )).thenReturn(expectedDomain);

        // Act
        BibliographicalData result = assembler.toDomain(dm);

        // Assert
        assertSame(expectedDomain, result);
    }

    @Test
    void toDomain_shouldPassNullLocaleWhenLanguageIsBlank() {
        // Arrange
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        BibliographicalDataDM dm = new BibliographicalDataDM(
                bdIdDouble,
                "Title",
                "   ",
                null,
                null,
                List.of(),
                List.of()
        );

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                eq(bdIdDouble),
                eq("Title"),
                eq(List.of()),
                isNull(),
                isNull(),
                eq(List.of()),
                isNull()
        )).thenReturn(expectedDomain);

        // Act
        BibliographicalData result = assembler.toDomain(dm);

        // Assert
        assertSame(expectedDomain, result);
    }

    @Test
    void toDomain_shouldPassNullLocaleWhenLanguageIsNull() {
        // Arrange
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        BibliographicalDataDM dm = mock(BibliographicalDataDM.class);
        when(dm.getId()).thenReturn(bdIdDouble);
        when(dm.getTitle()).thenReturn("Title");
        when(dm.getAuthorIds()).thenReturn(List.of());
        when(dm.getOriginalLanguage()).thenReturn(null);
        when(dm.getPublicationDate()).thenReturn(null);
        when(dm.getGenreIds()).thenReturn(List.of());
        when(dm.getChunksOfText()).thenReturn(null);

        BibliographicalData expectedDomain = mock(BibliographicalData.class);

        when(factory.create(
                eq(bdIdDouble),
                eq("Title"),
                eq(List.of()),
                isNull(),
                isNull(),
                eq(List.of()),
                isNull()
        )).thenReturn(expectedDomain);

        // Act
        BibliographicalData result = assembler.toDomain(dm);

        // Assert
        assertSame(expectedDomain, result);
    }

}