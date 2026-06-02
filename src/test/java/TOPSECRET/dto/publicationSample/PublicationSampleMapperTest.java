package TOPSECRET.dto.publicationSample;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.publicationSample.response.PublicationDTO;
import TOPSECRET.dto.publicationSample.response.PublicationDetailsDTO;
import TOPSECRET.dto.publicationSample.response.PublicationReferenceDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static TOPSECRET.dto.publicationSample.PublicationSampleMapper.toCondition;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link PublicationSampleMapper} class.
 * <p>
 * This test class verifies the correctness of the mapping methods used to convert
 * domain entities (such as {@link PublicationSample}, {@link BibliographicalData},
 * {@link Author}, and {@link Edition}) into various Data Transfer Objects (DTOs) including
 * {@link PublicationDetailsDTO}, {@link PublicationDTO}, and {@link PublicationReferenceDTO}.
 * It also tests the conversion of basic types/Strings to Domain Value Objects.
 * </p>
 */
class PublicationSampleMapperTest {

    // -------------------------------------------------------------------------
    // constructor
    // -------------------------------------------------------------------------

    @Test
    void constructor_throwsUnsupportedOperationException() throws Exception {
        // Arrange
        var constructor = PublicationSampleMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Act
        var ex = assertThrows(java.lang.reflect.InvocationTargetException.class,
                constructor::newInstance);

        // Assert
        assertInstanceOf(UnsupportedOperationException.class, ex.getCause());
        assertEquals("Utility class", ex.getCause().getMessage());
    }


    // -------------------------------------------------------------------------
    // toDetailsDTO
    // -------------------------------------------------------------------------

    @Test
    void toDetailsDTO_returnsCorrectDTO() {
        // Arrange
        PublicationSample pub = mockPublication("pub-1", Condition.GOOD);
        BibliographicalData bd = mockBd("Clean Code");
        List<Author> authors = List.of(mockAuthor("Robert Martin"));
        Edition edition = mockEdition("978-0132350884", "Hardcover");

        // Act
        PublicationDetailsDTO dto = PublicationSampleMapper.toDetailsDTO(pub, bd, authors, edition);

        // Assert
        assertEquals("pub-1", dto.getPublicationId());
        assertEquals("Clean Code", dto.getPubTitle());
        assertEquals(List.of("Robert Martin"), dto.getAuthorName());
        assertEquals("978-0132350884", dto.getCode());
        assertEquals("Hardcover",dto.getPublicationType());
    }

    @Test
    void toDetailsDTO_multipleAuthors() {
        // Arrange
        PublicationSample pub  = mockPublication("pub-2", Condition.NEW);
        BibliographicalData bd = mockBd("GoF");
        List<Author> authors = List.of(mockAuthor("Gamma"), mockAuthor("Helm"), mockAuthor("Johnson"));
        Edition edition  = mockEdition("978-0201633610", "Paperback");

        // Act
        PublicationDetailsDTO dto = PublicationSampleMapper.toDetailsDTO(pub, bd, authors, edition);

        // Assert
        assertEquals(List.of("Gamma", "Helm", "Johnson"), dto.getAuthorName());
    }

    @Test
    void toDetailsDTO_throwsOnNullPublication() {
        // Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(
                        null,
                        mockBd("Title"),
                        List.of(mockAuthor("Author")),
                        mockEdition("code", "Format")));
    }

    @Test
    void toDetailsDTO_throwsOnNullBd() {
        // Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(
                        mockPublication("id", Condition.GOOD),
                        null,
                        List.of(mockAuthor("Author")),
                        mockEdition("code", "Format")));
    }

    @Test
    void toDetailsDTO_throwsOnNullAuthors() {
        // Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(
                        mockPublication("id", Condition.GOOD),
                        mockBd("Title"),
                        null,
                        mockEdition("code", "Format")));
    }

    @Test
    void toDetailsDTO_throwsOnNullEdition() {
        // Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(
                        mockPublication("id", Condition.GOOD),
                        mockBd("Title"),
                        List.of(mockAuthor("Author")),
                        null));
    }

    // -------------------------------------------------------------------------
    // toDetailsDTOs
    // -------------------------------------------------------------------------

    @Test
    void toDetailsDTOs_returnsOneDTOPerPublication() {
        // Arrange
        List<PublicationSample> pubs = List.of(
                mockPublication("pub-1", Condition.NEW),
                mockPublication("pub-2", Condition.GOOD));

        List<BibliographicalData> bds = List.of(
                mockBd("Title A"),
                mockBd("Title B"));

        List<List<Author>> authors = List.of(
                List.of(mockAuthor("Author A")),
                List.of(mockAuthor("Author B")));

        List<Edition> editions = List.of(
                mockEdition("code-1", "Hardcover"),
                mockEdition("code-2", "Paperback"));

        // Act
        List<PublicationDetailsDTO> dtos =
                PublicationSampleMapper.toDetailsDTOs(pubs, bds, authors, editions);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("pub-1", dtos.get(0).getPublicationId());
        assertEquals("Title A", dtos.get(0).getPubTitle());
        assertEquals("pub-2",dtos.get(1).getPublicationId());
        assertEquals("Title B", dtos.get(1).getPubTitle());
    }

    @Test
    void toDetailsDTOs_emptyListReturnsEmpty() {
        // Arrange

        // Act
        List<PublicationDetailsDTO> dtos =
                PublicationSampleMapper.toDetailsDTOs(
                        List.of(), List.of(), List.of(), List.of());

        // Assert
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDetailsDTOs_throwsOnNullPublications() {
        // Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTOs(
                        null, List.of(), List.of(), List.of()));
    }

    // -------------------------------------------------------------------------
    // toPublicationDTOs
    // -------------------------------------------------------------------------

    @Test
    void toPublicationDTOs_returnsOneDTOPerPublication() {
        // Arrange
        List<PublicationSample> pubs = List.of(
                mockPublication("pub-1", Condition.NEW),
                mockPublication("pub-2", Condition.POOR));
        List<BibliographicalData> bds = List.of(
                mockBd("Title A"),
                mockBd("Title B"));
        List<List<Author>> authors = List.of(
                List.of(mockAuthor("Author A")),
                List.of(mockAuthor("Author B")));
        List<List<Genre>> genres = List.of(
                List.of(mockGenre("Fiction")),
                List.of(mockGenre("Science")));
        List<User> owners = List.of(mockUser("user_a"), mockUser("user_b"));

        // Act
        List<PublicationDTO> dtos =
                PublicationSampleMapper.toPublicationDTOs(pubs, bds, authors, genres, owners);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("pub-1", dtos.get(0).getId());
        assertEquals("user_a", dtos.get(0).getOwnerName());
        assertEquals("pub-2", dtos.get(1).getId());
        assertEquals("user_b", dtos.get(1).getOwnerName());
        assertEquals(List.of("Fiction"), dtos.get(0).getGenres());
    }

    // -------------------------------------------------------------------------
    // toReferenceDTO
    // -------------------------------------------------------------------------

    @Test
    void toReferenceDTO_returnsCorrectDTO() {
        // Arrange
        PublicationSample pub  = mockPublication("pub-1", Condition.GOOD);
        BibliographicalData bd = mockBd("The Pragmatic Programmer");

        // Act
        PublicationReferenceDTO dto = PublicationSampleMapper.toReferenceDTO(pub, bd);

        // Assert
        assertEquals("pub-1", dto.getId());
        assertEquals("The Pragmatic Programmer", dto.getPubTitle());
    }

    @Test
    void toReferenceDTO_throwsOnNullPublication() {
        // Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toReferenceDTO(null, mockBd("Title")));
    }

    @Test
    void toReferenceDTO_throwsOnNullBd() {
        // Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toReferenceDTO(mockPublication("id", Condition.GOOD), null));
    }

    // -------------------------------------------------------------------------
    // toReferenceDTOs
    // -------------------------------------------------------------------------

    @Test
    void toReferenceDTOs_returnsOneDTOPerPublication() {
        // Arrange
        List<PublicationSample> pubs = List.of(
                mockPublication("pub-1", Condition.NEW),
                mockPublication("pub-2", Condition.GOOD));
        List<BibliographicalData> bds = List.of(
                mockBd("Title A"),
                mockBd("Title B"));

        // Act
        List<PublicationReferenceDTO> dtos =
                PublicationSampleMapper.toReferenceDTOs(pubs, bds);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("pub-1", dtos.get(0).getId());
        assertEquals("Title A", dtos.get(0).getPubTitle());
        assertEquals("pub-2", dtos.get(1).getId());
        assertEquals("Title B", dtos.get(1).getPubTitle());
    }

    @Test
    void toReferenceDTOs_emptyListReturnsEmpty() {
        // Arrange

        // Act & Assert
        assertTrue(PublicationSampleMapper.toReferenceDTOs(List.of(), List.of()).isEmpty());
    }


    // -------------------------------------------------------------------------
    // toPubId / toBdId / toEditionId
    // -------------------------------------------------------------------------

    @Test
    void toPubId_wrapsId() {
        // Arrange

        // Act
        PublicationSampleId id = PublicationSampleMapper.toPubId("pub-99");

        // Assert
        assertEquals("pub-99", id.getID());
    }

    @Test
    void toBdId_wrapsId() {
        // Arrange

        // Act
        BibliographicalDataId id = PublicationSampleMapper.toBdId("bd-42");

        // Assert
        assertEquals("bd-42", id.getID());
    }

    @Test
    void toEditionId_wrapsId() {
        // Arrange

        // Act
        EditionId id = PublicationSampleMapper.toEditionId("ed-7");

        // Assert
        assertEquals("ed-7", id.getID());
    }

    @Test
    void toCondition_validDescription_returnsCorrectCondition() {
        // Arrange
        String conditionString = "Good";

        // Act
        Condition result = toCondition(conditionString);

        // Assert
        assertEquals(Condition.GOOD, result);
    }

    @Test
    void toCondition_caseInsensitive_returnsCorrectCondition() {
        // Arrange
        String conditionString = "very good";

        // Act
        Condition result = toCondition(conditionString);

        // Assert
        assertEquals(Condition.VERY_GOOD, result);
    }

    @Test
    void toCondition_invalidString_throwsIllegalArgumentException() {
        // Arrange
        String conditionString = "UNKNOWN";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> toCondition(conditionString));
    }

    @Test
    void toUserId_wrapsId() {
        // Arrange
        String rawUserId = "user-123";

        // Act
        UserId id = PublicationSampleMapper.toUserId(rawUserId);

        // Assert
        assertNotNull(id, "The returned UserId should not be null");
        assertEquals("user-123", id.getUserId(), "The wrapped ID value should match the input string");
    }

    // -------------------------------------------------------------------------
    // toBibliographicalDataIds / toBibliographicalDataId
    // -------------------------------------------------------------------------

    @Test
    void toBibliographicalDataIds_returnsListOfIds() {
        // Arrange
        List<String> rawIds = List.of("bd-1", "bd-2", "bd-3");

        // Act
        List<BibliographicalDataId> ids = PublicationSampleMapper.toBibliographicalDataIds(rawIds);

        // Assert
        assertEquals(3, ids.size(), "Should return exactly 3 BibliographicalDataId objects");
        assertEquals("bd-1", ids.get(0).getID());
        assertEquals("bd-2", ids.get(1).getID());
        assertEquals("bd-3", ids.get(2).getID());
    }

    @Test
    void toBibliographicalDataIds_emptyListReturnsEmpty() {
        // Arrange
        List<String> rawIds = List.of();

        // Act
        List<BibliographicalDataId> ids = PublicationSampleMapper.toBibliographicalDataIds(rawIds);

        // Assert
        assertTrue(ids.isEmpty(), "An empty input list should result in an empty output list");
    }

    @Test
    void toBibliographicalDataId_wrapsId() {
        // Arrange
        String rawId = "bd-99";

        // Act
        BibliographicalDataId id = PublicationSampleMapper.toBibliographicalDataId(rawId);

        // Assert
        assertNotNull(id, "The returned BibliographicalDataId should not be null");
        assertEquals("bd-99", id.getID(), "The wrapped ID value should match the input string");
    }

    // -------------------------------------------------------------------------
    // toEditionIds
    // -------------------------------------------------------------------------

    @Test
    void toEditionIds_returnsListOfIds() {
        // Arrange
        List<String> rawIds = List.of("ed-10", "ed-20");

        // Act
        List<EditionId> ids = PublicationSampleMapper.toEditionIds(rawIds);

        // Assert
        assertEquals(2, ids.size(), "Should return exactly 2 EditionId objects");
        assertEquals("ed-10", ids.get(0).getID());
        assertEquals("ed-20", ids.get(1).getID());
    }

    @Test
    void toEditionIds_emptyListReturnsEmpty() {
        // Arrange
        List<String> rawIds = List.of();

        // Act
        List<EditionId> ids = PublicationSampleMapper.toEditionIds(rawIds);

        // Assert
        assertTrue(ids.isEmpty(), "An empty input list should result in an empty output list");
    }

    // -------------------------------------------------------------------------
    // toCollectionOfPublicationsId
    // -------------------------------------------------------------------------

    @Test
    void toCollectionOfPublicationsId_wrapsId() {
        // Arrange
        String rawId = "collection-404";

        // Act
        CollectionOfPublicationsId id = PublicationSampleMapper.toCollectionOfPublicationsId(rawId);

        // Assert
        assertNotNull(id, "The returned CollectionOfPublicationsId should not be null");
        assertEquals("collection-404", id.getId(), "The wrapped ID value should match the input string");
    }


    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private PublicationSample mockPublication(String id, Condition condition) {
        PublicationSample pub = mock(PublicationSample.class);
        PublicationSampleId pubId = mock(PublicationSampleId.class);
        when(pubId.getID()).thenReturn(id);
        when(pub.id()).thenReturn(pubId);
        when(pub.getCondition()).thenReturn(Condition.NEW);
        return pub;
    }

    private BibliographicalData mockBd(String title) {
        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn(title);
        when(bd.getOriginalLanguage()).thenReturn(Locale.ENGLISH);
        return bd;
    }

    private Author mockAuthor(String name) {
        Author author = mock(Author.class);
        when(author.getName()).thenReturn(name);
        return author;
    }

    private Edition mockEdition(String code, String mediaFormat) {
        Edition edition = mock(Edition.class);
        Code mockCode = mock(Code.class);
        when(mockCode.toString()).thenReturn(code);
        when(edition.getCode()).thenReturn(mockCode);
        when(edition.getMediaFormat()).thenReturn(mediaFormat);
        return edition;
    }

    private Genre mockGenre(String genreType) {
        Genre genre = mock(Genre.class);
        when(genre.getGenreType()).thenReturn(genreType);
        return genre;
    }

    private User mockUser(String username) {
        User user = mock(User.class);
        when(user.getUserName()).thenReturn(username);
        return user;
    }
}