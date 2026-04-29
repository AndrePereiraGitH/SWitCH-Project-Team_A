package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US012PublicationSampleMapper}.
 *
 * <p>Verifies the mapping behaviour from {@link PublicationSample} domain objects
 * to {@link US012PublicationDetailsDTO} data transfer objects, covering both
 * single and bulk conversions.</p>
 *
 * <p>All external dependencies ({@link IBibliographicalDataRepository},
 * {@link IAuthorRepository}, {@link IEditionRepository}) are replaced with
 * Mockito doubles to isolate the mapper from persistence concerns.</p>
 *
 * <p>Test coverage includes:</p>
 * <ul>
 *   <li>Constructor — null-guard validation for each injected repository</li>
 *   <li>{@link US012PublicationSampleMapper#toDTO} — happy path and missing-entity scenarios</li>
 *   <li>{@link US012PublicationSampleMapper#toDTOs} — list conversion and empty-input handling</li>
 * </ul>
 */
class US012PublicationSampleMapperTest {

    // -------------------------------------------------------------------------
    // Shared doubles
    // -------------------------------------------------------------------------

    private IBibliographicalDataRepository bdRepo;
    private IAuthorRepository authorRepo;
    private IEditionRepository editionRepo;
    private US012PublicationSampleMapper mapper;

    @BeforeEach
    void setUp() {
        bdRepo = mock(IBibliographicalDataRepository.class);
        authorRepo = mock(IAuthorRepository.class);
        editionRepo = mock(IEditionRepository.class);
        mapper = new US012PublicationSampleMapper(bdRepo, authorRepo, editionRepo);
    }

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    @Test
    void constructor_shouldThrowException_whenBdRepoIsNull() {
        assertThrows(NullPointerException.class, () ->
                new US012PublicationSampleMapper(null, authorRepo, editionRepo));
    }

    @Test
    void constructor_shouldThrowException_whenAuthorRepoIsNull() {
        assertThrows(NullPointerException.class, () ->
                new US012PublicationSampleMapper(bdRepo, null, editionRepo));
    }

    @Test
    void constructor_shouldThrowException_whenEditionRepoIsNull() {
        assertThrows(NullPointerException.class, () ->
                new US012PublicationSampleMapper(bdRepo, authorRepo, null));
    }

    // -------------------------------------------------------------------------
    // toDTO tests
    // -------------------------------------------------------------------------

    @Test
    void toDTO_shouldReturnCorrectDTO() throws InstanceNotFoundException {
        // Arrange
        String expectedTitle = "Title1";
        List<String> expectedAuthors = List.of("Author1", "Author2");
        String expectedCode = "12345678910";
        String expectedPubType = "Book";

        PublicationSample publication = createMockPublicationSample(expectedTitle, expectedAuthors, expectedCode, expectedPubType);

        // Act
        US012PublicationDetailsDTO result = mapper.toDTO(publication);

        // Assert
        assertEquals(expectedTitle, result.pubTitle());
        assertEquals(expectedAuthors, result.authorName());
        assertEquals(expectedCode, result.code());
        assertEquals(expectedPubType, result.publicationType());
    }

    @Test
    void toDTO_shouldThrowException_whenBibliographicalDataNotFound() {
        // Arrange
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        PublicationSample publication = mock(PublicationSample.class);
        when(publication.getBibliographicalDataID()).thenReturn(bdId);
        when(bdRepo.findBibliographicalDataById(bdId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(publication));
    }

    @Test
    void toDTO_shouldThrowException_whenEditionNotFound() {
        // Arrange
        String expectedTitle = "Title1";
        List<String> expectedAuthors = List.of("Author1");

        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        BibliographicalData bd = createMockBibliographicalData(expectedTitle, expectedAuthors);

        EditionID editionId = mock(EditionID.class);
        PublicationSample publication = mock(PublicationSample.class);

        when(publication.getBibliographicalDataID()).thenReturn(bdId);
        when(publication.getEditionID()).thenReturn(editionId);
        when(bdRepo.findBibliographicalDataById(bdId)).thenReturn(Optional.of(bd));
        when(editionRepo.findEditionById(editionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(publication));
    }

    @Test
    void toDTO_shouldThrowException_whenAuthorNotFound() {
        // Arrange
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        AuthorId authorId = mock(AuthorId.class);

        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn("Title1");
        when(bd.getAuthors()).thenReturn(List.of(authorId));

        EditionID editionId = mock(EditionID.class);
        Edition edition = createMockEdition("12345678910", "Book");

        PublicationSample publication = mock(PublicationSample.class);
        when(publication.getBibliographicalDataID()).thenReturn(bdId);
        when(publication.getEditionID()).thenReturn(editionId);
        when(bdRepo.findBibliographicalDataById(bdId)).thenReturn(Optional.of(bd));
        when(editionRepo.findEditionById(editionId)).thenReturn(Optional.of(edition));
        when(authorRepo.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(publication));
    }

    // -------------------------------------------------------------------------
    // toDTOs tests
    // -------------------------------------------------------------------------

    @Test
    void toDTOs_shouldReturnCorrectListOfDTOs() throws InstanceNotFoundException {
        // Arrange
        String title1 = "Title1";
        List<String> authors1 = List.of("Author1", "Author2");
        String code1 = "123456789";

        String title2 = "Title2";
        List<String> authors2 = List.of("Author3", "Author4");
        String code2 = "987654321";

        PublicationSample pub1 = createMockPublicationSample(title1, authors1, code1, "Book");
        PublicationSample pub2 = createMockPublicationSample(title2, authors2, code2, "Book");

        // Act
        List<US012PublicationDetailsDTO> result = mapper.toDTOs(List.of(pub1, pub2));

        // Assert
        assertEquals(2, result.size());
        assertEquals(title1, result.get(0).pubTitle());
        assertEquals(title2, result.get(1).pubTitle());
        assertEquals(authors1, result.get(0).authorName());
        assertEquals(authors2, result.get(1).authorName());
        assertEquals(code1, result.get(0).code());
        assertEquals(code2, result.get(1).code());
    }

    @Test
    void toDTOs_shouldReturnEmptyList_whenInputIsEmpty() throws InstanceNotFoundException {
        // Act
        List<US012PublicationDetailsDTO> result = mapper.toDTOs(List.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------------

    private PublicationSample createMockPublicationSample(String title, List<String> authorNames, String code, String pubType) {
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        EditionID editionId = mock(EditionID.class);

        BibliographicalData bd = createMockBibliographicalData(title, authorNames);
        Edition edition = createMockEdition(code, pubType);

        PublicationSample publication = mock(PublicationSample.class);
        when(publication.getBibliographicalDataID()).thenReturn(bdId);
        when(publication.getEditionID()).thenReturn(editionId);
        when(bdRepo.findBibliographicalDataById(bdId)).thenReturn(Optional.of(bd));
        when(editionRepo.findEditionById(editionId)).thenReturn(Optional.of(edition));

        return publication;
    }

    private BibliographicalData createMockBibliographicalData(String title, List<String> authorNames) {
        List<AuthorId> authorIds = authorNames.stream()
                .map(name -> {
                    AuthorId authorId = mock(AuthorId.class);
                    Author author = mock(Author.class);
                    when(author.getName()).thenReturn(name);
                    when(authorRepo.findById(authorId)).thenReturn(Optional.of(author));
                    return authorId;
                })
                .toList();

        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn(title);
        when(bd.getAuthors()).thenReturn(authorIds);

        return bd;
    }

    private Edition createMockEdition(String code, String pubType) {
        Code codeDouble = mock(Code.class);
        when(codeDouble.toString()).thenReturn(code);

        Edition edition = mock(Edition.class);
        when(edition.getCode()).thenReturn(codeDouble);
        when(edition.getMediaFormat()).thenReturn(pubType);

        return edition;
    }
}