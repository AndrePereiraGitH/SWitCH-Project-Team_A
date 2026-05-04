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

class US016PublicationSampleMapperTest {

    private IBibliographicalDataRepository bdRepository;
    private US016PublicationSampleMapper mapper;

    private PublicationSample publicationSample;
    private BibliographicalData bd;

    private PublicationSampleId publicationSampleId;
    private BibliographicalDataID bibliographicalDataId;
    private UserId ownerId;

    private AuthorId author;
    private AuthorId author1;
    private AuthorId author2;

    private GenreId genre;
    private GenreId genre1;
    private GenreId genre2;

    private Condition condition;

    @BeforeEach
    void setUp() {
        bdRepository = mock(IBibliographicalDataRepository.class);
        mapper = new US016PublicationSampleMapper(bdRepository);

        publicationSample = mock(PublicationSample.class);
        bd = mock(BibliographicalData.class);

        publicationSampleId = mock(PublicationSampleId.class);
        bibliographicalDataId = mock(BibliographicalDataID.class);
        ownerId = mock(UserId.class);

        author = mock(AuthorId.class);
        author1 = mock(AuthorId.class);
        author2 = mock(AuthorId.class);

        genre = mock(GenreId.class);
        genre1 = mock(GenreId.class);
        genre2 = mock(GenreId.class);

        condition = mock(Condition.class);
    }

    // ===================================================
    // ================= FROM DOMAIN =====================
    // ===================================================

    @Test
    void fromDomain_shouldThrow_whenPublicationSampleIsNull() {
        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> US016PublicationSampleMapper.fromDomain(null, bd));

        assertEquals("PublicationSample cannot be null", ex.getMessage());
    }

    @Test
    void fromDomain_shouldThrow_whenBibliographicalDataIsNull() {
        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> US016PublicationSampleMapper.fromDomain(publicationSample, null));

        assertEquals("BibliographicalData is missing.", ex.getMessage());
    }

    @Test
    void fromDomain_shouldMapAllFieldsCorrectly() {

        // arrange
        Locale language = Locale.ENGLISH;

        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSampleId.getID()).thenReturn("publicationId");
        when(publicationSample.getOwnerID()).thenReturn(ownerId);
        when(ownerId.getUsername()).thenReturn("ownerID");
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

        // act
        US016PublicationSampleDTO dto = US016PublicationSampleMapper.fromDomain(publicationSample, bd);

        // assert
        assertNotNull(dto);
        assertEquals(publicationSampleId.getID(), dto.publicationSampleID());
        assertEquals("The Hobbit", dto.title());
        assertEquals(List.of("Author 1", "Author 2"), dto.authorName());
        assertEquals(language, dto.language());
        assertEquals(List.of("Fantasy", "Adventure"), dto.genreType());
        assertEquals(ownerId.getUsername(), dto.ownerName());
        assertEquals("USED", dto.condition());
    }

    @Test
    void fromDomain_shouldMapSingleAuthorAndGenre() {

        // arrange
        Locale language = Locale.FRENCH;

        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSampleId.getID()).thenReturn("publicationId");
        when(publicationSample.getOwnerID()).thenReturn(ownerId);
        when(ownerId.getUsername()).thenReturn("ownerID");
        when(publicationSample.getCondition()).thenReturn(condition);

        when(bd.getTitle()).thenReturn("Dune");
        when(bd.getAuthors()).thenReturn(List.of(author));
        when(bd.getOriginalLanguage()).thenReturn(language);
        when(bd.getGenres()).thenReturn(List.of(genre));

        when(author.getId()).thenReturn("Frank Herbert");
        when(genre.getID()).thenReturn("Sci-Fi");
        when(condition.toString()).thenReturn("NEW");

        // act
        US016PublicationSampleDTO dto = US016PublicationSampleMapper.fromDomain(publicationSample, bd);

        // assert
        assertEquals(publicationSampleId.getID(), dto.publicationSampleID());
        assertEquals("Dune", dto.title());
        assertEquals(List.of("Frank Herbert"), dto.authorName());
        assertEquals(language, dto.language());
        assertEquals(List.of("Sci-Fi"), dto.genreType());
        assertEquals(ownerId.getUsername(), dto.ownerName());
        assertEquals("NEW", dto.condition());
    }

    // ===================================================
    // ================= FROM DOMAIN LIST ================
    // ===================================================

    @Test
    void fromDomainList_shouldReturnMappedDtoList() throws Exception {

        // arrange
        Locale language = Locale.ENGLISH;

        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSampleId.getID()).thenReturn("publicationId");
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getOwnerID()).thenReturn(ownerId);
        when(ownerId.getUsername()).thenReturn("ownerID");
        when(publicationSample.getCondition()).thenReturn(condition);

        when(bdRepository.findById(bibliographicalDataId))
                .thenReturn(Optional.of(bd));

        when(bd.getTitle()).thenReturn("The Hobbit");
        when(bd.getAuthors()).thenReturn(List.of(author));
        when(bd.getOriginalLanguage()).thenReturn(language);
        when(bd.getGenres()).thenReturn(List.of(genre));

        when(author.getId()).thenReturn("Tolkien");
        when(genre.getID()).thenReturn("Fantasy");
        when(condition.toString()).thenReturn("USED");

        // act
        List<US016PublicationSampleDTO> result = mapper.fromDomainList(List.of(publicationSample));

        // assert
        assertNotNull(result);
        assertEquals(1, result.size());

        US016PublicationSampleDTO dto = result.get(0);
        assertNotNull(dto);
        assertEquals(publicationSampleId.getID(), dto.publicationSampleID());
        assertEquals("The Hobbit", dto.title());
        assertEquals(List.of("Tolkien"), dto.authorName());
        assertEquals(language, dto.language());
        assertEquals(List.of("Fantasy"), dto.genreType());
        assertEquals(ownerId.getUsername(), dto.ownerName());
        assertEquals("USED", dto.condition());
    }

    @Test
    void fromDomainList_shouldThrowInstanceNotFoundException_whenBibliographicalDataDoesNotExist() {

        // arrange
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(bdRepository.findById(bibliographicalDataId))
                .thenReturn(Optional.empty());

        // act
        InstanceNotFoundException ex = assertThrows(
                InstanceNotFoundException.class,
                () -> mapper.fromDomainList(List.of(publicationSample))
        );

        // assert
        assertEquals(
                "BibliographicalData not found for auctionId: " + bibliographicalDataId,
                ex.getMessage()
        );
    }
}