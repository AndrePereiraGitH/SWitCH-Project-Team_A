package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
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
 * Tests for US023PublicationSampleMapper.
 *
 * This suite verifies that the mapper correctly converts PublicationSample
 * domain objects into US023PublicationSampleDTO instances, and that it
 * properly handles missing data by throwing the expected exceptions.
 *
 * It also checks constructor validation and the behavior of mapping
 * collections of PublicationSample objects.
 */
class US023PublicationSampleMapperTest {

    //==================================================================================================================
    //=========== SET UP ===============================================================================================
    //==================================================================================================================

    private IBibliographicalDataRepository bdRepo;
    private IAuthorRepository authorRepo;
    private IEditionRepository editionRepo;
    private US023PublicationSampleMapper mapper;

    @BeforeEach
    void setUp() {
        bdRepo = mock(IBibliographicalDataRepository.class);
        authorRepo = mock(IAuthorRepository.class);
        editionRepo = mock(IEditionRepository.class);
        mapper = new US023PublicationSampleMapper(bdRepo, authorRepo, editionRepo);
    }

    //==================================================================================================================
    //================ CONSTRUCTOR =====================================================================================
    //==================================================================================================================

    @Test
    void constructor_shouldThrowException_whenBdRepoIsNull() {
        assertThrows(NullPointerException.class, () ->
                new US023PublicationSampleMapper(null, authorRepo, editionRepo));
    }

    @Test
    void constructor_shouldThrowException_whenAuthorRepoIsNull() {
        assertThrows(NullPointerException.class, () ->
                new US023PublicationSampleMapper(bdRepo, null, editionRepo));
    }

    @Test
    void constructor_shouldThrowException_whenEditionRepoIsNull() {
        assertThrows(NullPointerException.class, () ->
                new US023PublicationSampleMapper(bdRepo, authorRepo, null));
    }
    
    //==================================================================================================================
    //========= toDTO ==================================================================================================
    //==================================================================================================================

    @Test
    void toDTO_shouldReturnCorrectDTO() throws InstanceNotFoundException {
        // Arrange
        String expectedTitle = "Title1";
        List<String> expectedAuthors = List.of("Author1", "Author2");
        String expectedCode = "12345678910";
        String expectedPubType = "Book";
        String expectedSeller = "user123";
        String expectedPubStatus = "FOR_SALE";
        String expectedCondition = "NEW";

        PublicationSample publication = createPublicationSampleDouble(expectedTitle, expectedAuthors, expectedCode,
                expectedPubType, expectedSeller, expectedPubStatus, expectedCondition);

        // Act
        US023PublicationSampleDTO result = mapper.toDTO(publication);

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
        BibliographicalData bd = createBibliographicalDataDouble(expectedTitle, expectedAuthors);

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
        Edition edition = createEditionDouble("12345678910", "Book");

        PublicationSample publication = mock(PublicationSample.class);
        when(publication.getBibliographicalDataID()).thenReturn(bdId);
        when(publication.getEditionID()).thenReturn(editionId);
        when(bdRepo.findBibliographicalDataById(bdId)).thenReturn(Optional.of(bd));
        when(editionRepo.findEditionById(editionId)).thenReturn(Optional.of(edition));
        when(authorRepo.findAuthorById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(publication));
    }

    //==================================================================================================================
    //========= toDTOs =================================================================================================
    //==================================================================================================================

    @Test
    void toDTOs_shouldReturnCorrectListOfDTOs() throws InstanceNotFoundException {
        // Arrange
        String title1 = "Title1";
        List<String> authors1 = List.of("Author1", "Author2");
        String code1 = "123456789";
        String seller1 = "user123";
        String pubStatus1 = "FOR_SALE";
        String condition1 = "NEW";

        String title2 = "Title2";
        List<String> authors2 = List.of("Author3", "Author4");
        String code2 = "987654321";
        String seller2 = "user321";
        String pubStatus2 = "FOR_SALE";
        String condition2 = "LIKE_NEW";

        PublicationSample pub1 = createPublicationSampleDouble(title1, authors1, code1, "Book", seller1, pubStatus1, condition1);
        PublicationSample pub2 = createPublicationSampleDouble(title2, authors2, code2, "Book", seller2, pubStatus2, condition2);

        // Act
        List<US023PublicationSampleDTO> result = mapper.toDTOs(List.of(pub1, pub2));

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
        List<US023PublicationSampleDTO> result = mapper.toDTOs(List.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    
    
    
    //==================================================================================================================
    //========= HELPERS ================================================================================================
    //==================================================================================================================

    private PublicationSample createPublicationSampleDouble(String title, List<String> authorNames, String code,
                                                          String pubType, String seller, String pubStatus, String condition) {
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        EditionID editionId = mock(EditionID.class);
        UserId sellerId = mock(UserId.class);

        BibliographicalData bd = createBibliographicalDataDouble(title, authorNames);
        Edition edition = createEditionDouble(code, pubType);

        PublicationSample publication = mock(PublicationSample.class);
        when(publication.getBibliographicalDataID()).thenReturn(bdId);
        when(publication.getEditionID()).thenReturn(editionId);
        when(bdRepo.findBibliographicalDataById(bdId)).thenReturn(Optional.of(bd));
        when(editionRepo.findEditionById(editionId)).thenReturn(Optional.of(edition));
        when(publication.getOwnerID()).thenReturn(sellerId);
        when(publication.getPublicationStatus()).thenReturn(PublicationStatus.FOR_SALE);
        when(publication.getCondition()).thenReturn(Condition.LIKE_NEW);

        return publication;
    }

    private BibliographicalData createBibliographicalDataDouble(String title, List<String> authorNames) {
        List<AuthorId> authorIds = authorNames.stream()
                .map(name -> {
                    AuthorId authorId = mock(AuthorId.class);
                    Author author = mock(Author.class);
                    when(author.getName()).thenReturn(name);
                    when(authorRepo.findAuthorById(authorId)).thenReturn(Optional.of(author));
                    return authorId;
                })
                .toList();

        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn(title);
        when(bd.getAuthors()).thenReturn(authorIds);

        return bd;
    }

    private Edition createEditionDouble(String code, String pubType) {
        Code codeDouble = mock(Code.class);
        when(codeDouble.toString()).thenReturn(code);

        Edition edition = mock(Edition.class);
        when(edition.getCode()).thenReturn(codeDouble);
        when(edition.getMediaFormat()).thenReturn(pubType);

        return edition;
    }
}