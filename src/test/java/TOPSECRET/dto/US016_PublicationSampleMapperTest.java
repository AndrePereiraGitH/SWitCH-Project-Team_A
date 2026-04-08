package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US016_PublicationSampleMapperTest {

    private IBibliographicalDataRepository bdRepository;
    private US016_PublicationSampleMapper mapper;

    @BeforeEach
    void setUp() {
        bdRepository = mock(IBibliographicalDataRepository.class);
        mapper = new US016_PublicationSampleMapper(bdRepository);
    }


    // ===================================================
    // ================= FROM DOMAIN =====================
    // ===================================================

    @Test
    void fromDomain_shouldMapAllFieldsCorrectly() {
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalData bd = mock(BibliographicalData.class);

        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        UserId ownerId = mock(UserId.class);

        AuthorId author1 = mock(AuthorId.class);
        AuthorId author2 = mock(AuthorId.class);

        GenreID genre1 = mock(GenreID.class);
        GenreID genre2 = mock(GenreID.class);

        Locale language = Locale.ENGLISH;
        Condition condition = mock(Condition.class);

        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getOwnerID()).thenReturn(ownerId);
        when(publicationSample.getCondition()).thenReturn(condition);

        when(bd.getTitle()).thenReturn("The Hobbit");
        when(bd.getAuthors()).thenReturn(List.of(author1, author2));
        when(bd.getOriginalLanguage()).thenReturn(language);
        when(bd.getGenres()).thenReturn(List.of(genre1, genre2));

        when(author1.getId()).thenReturn("Author 1");
        when(author2.getId()).thenReturn("Author 2");
        when(genre1.getID()).thenReturn("Fantasy");
        when(genre2.getID()).thenReturn("Adventure");
        when(condition.toString()).thenReturn("USED");

        US016_PublicationSampleDTO dto = US016_PublicationSampleMapper.fromDomain(publicationSample, bd);

        assertNotNull(dto);
        assertEquals(publicationSampleId, dto.publicationSampleID());
        assertEquals("The Hobbit", dto.title());
        assertEquals(List.of("Author 1", "Author 2"), dto.authorName());
        assertEquals(language, dto.language());
        assertEquals(List.of("Fantasy", "Adventure"), dto.genreType());
        assertEquals(ownerId, dto.ownerName());
        assertEquals("USED", dto.condition());
    }

    @Test
    void fromDomain_shouldMapSingleAuthorAndGenre() {
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalData bd = mock(BibliographicalData.class);

        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        UserId ownerId = mock(UserId.class);

        AuthorId author = mock(AuthorId.class);
        GenreID genre = mock(GenreID.class);

        Locale language = Locale.FRENCH;
        Condition condition = mock(Condition.class);

        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getOwnerID()).thenReturn(ownerId);
        when(publicationSample.getCondition()).thenReturn(condition);

        when(bd.getTitle()).thenReturn("Dune");
        when(bd.getAuthors()).thenReturn(List.of(author));
        when(bd.getOriginalLanguage()).thenReturn(language);
        when(bd.getGenres()).thenReturn(List.of(genre));

        when(author.getId()).thenReturn("Frank Herbert");
        when(genre.getID()).thenReturn("Sci-Fi");
        when(condition.toString()).thenReturn("NEW");

        US016_PublicationSampleDTO dto = US016_PublicationSampleMapper.fromDomain(publicationSample, bd);

        assertEquals(publicationSampleId, dto.publicationSampleID());
        assertEquals("Dune", dto.title());
        assertEquals(List.of("Frank Herbert"), dto.authorName());
        assertEquals(language, dto.language());
        assertEquals(List.of("Sci-Fi"), dto.genreType());
        assertEquals(ownerId, dto.ownerName());
        assertEquals("NEW", dto.condition());
    }

    // ===================================================
    // ================= FROM DOMAIN LIST ================
    // ===================================================

    @Test
    void fromDomainList_shouldReturnMappedDtoList() throws Exception {
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);

        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        BibliographicalDataID bibliographicalDataId = mock(BibliographicalDataID.class);
        UserId ownerId = mock(UserId.class);

        AuthorId author = mock(AuthorId.class);
        GenreID genre = mock(GenreID.class);

        Locale language = Locale.ENGLISH;
        Condition condition = mock(Condition.class);

        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getOwnerID()).thenReturn(ownerId);
        when(publicationSample.getCondition()).thenReturn(condition);

        when(bdRepository.findBibliographicalDataById(bibliographicalDataId))
                .thenReturn(Optional.of(bibliographicalData));

        when(bibliographicalData.getTitle()).thenReturn("The Hobbit");
        when(bibliographicalData.getAuthors()).thenReturn(List.of(author));
        when(bibliographicalData.getOriginalLanguage()).thenReturn(language);
        when(bibliographicalData.getGenres()).thenReturn(List.of(genre));

        when(author.getId()).thenReturn("Tolkien");
        when(genre.getID()).thenReturn("Fantasy");
        when(condition.toString()).thenReturn("USED");

        List<US016_PublicationSampleDTO> result = mapper.fromDomainList(List.of(publicationSample));

        assertNotNull(result);
        assertEquals(1, result.size());

        US016_PublicationSampleDTO dto = result.get(0);
        assertNotNull(dto);
        assertEquals(publicationSampleId, dto.publicationSampleID());
        assertEquals("The Hobbit", dto.title());
        assertEquals(List.of("Tolkien"), dto.authorName());
        assertEquals(language, dto.language());
        assertEquals(List.of("Fantasy"), dto.genreType());
        assertEquals(ownerId, dto.ownerName());
        assertEquals("USED", dto.condition());
    }

    @Test
    void fromDomainList_shouldThrowInstanceNotFoundException_whenBibliographicalDataDoesNotExist() {
        PublicationSample publicationSample = mock(PublicationSample.class);
        BibliographicalDataID bibliographicalDataId = mock(BibliographicalDataID.class);

        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(bdRepository.findBibliographicalDataById(bibliographicalDataId))
                .thenReturn(Optional.empty());

        InstanceNotFoundException ex = assertThrows(
                InstanceNotFoundException.class,
                () -> mapper.fromDomainList(List.of(publicationSample))
        );

        assertEquals(
                "BibliographicalData not found for id: " + bibliographicalDataId,
                ex.getMessage()
        );
    }
}

