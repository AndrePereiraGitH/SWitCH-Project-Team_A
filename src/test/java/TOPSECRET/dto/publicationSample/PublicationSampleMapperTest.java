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

import static TOPSECRET.dto.publicationSample.PublicationSampleMapper.toCondition;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationSampleMapper}.
 *
 * <p>Tests are organized into the following sections:</p>
 * <ul>
 *   <li><b>Constructor</b> — utility class guard.</li>
 *   <li><b>toDetailsDTO</b> — single and batch mapping to {@link PublicationDetailsDTO}.</li>
 *   <li><b>toPublicationDTO</b> — single and batch mapping to {@link PublicationDTO}.</li>
 *   <li><b>toReferenceDTO</b> — single and batch mapping to {@link PublicationReferenceDTO}.</li>
 *   <li><b>Value Object conversions</b> — string to domain VO mappings.</li>
 * </ul>
 */
class PublicationSampleMapperTest {

    // =========================================================================
    // Constructor
    // =========================================================================

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

    // =========================================================================
    // toDetailsDTO
    // =========================================================================

    @Test
    void toDetailsDTO_returnsCorrectDTO() {
        // Arrange
        PublicationSample pub = mockPublication("pub-1", Condition.GOOD);
        BibliographicalData bd = mockBd("Clean Code");
        List<Author> authors = List.of(mockAuthor("Robert Martin"));
        Edition edition = mockEdition("978-0132350884", "Hardcover");

        // Act
        PublicationDetailsDTO resultDto =
                PublicationSampleMapper.toDetailsDTO(pub, bd, authors, edition);

        // Assert
        assertNotNull(resultDto);
        assertEquals("pub-1", resultDto.getPublicationId());
    }

    @Test
    void toDetailsDTO_returnsCorrectAuthorNames_whenMultipleAuthors() {
        // Arrange
        PublicationSample pub = mockPublication("pub-2", Condition.NEW);
        BibliographicalData bd = mockBd("GoF");
        List<Author> authors = List.of(
                mockAuthor("Gamma"),
                mockAuthor("Helm"),
                mockAuthor("Johnson"));
        Edition edition = mockEdition("978-0201633610", "Paperback");

        // Act
        PublicationDetailsDTO resultDto =
                PublicationSampleMapper.toDetailsDTO(pub, bd, authors, edition);

        // Assert
        assertNotNull(resultDto);
        assertEquals(3, resultDto.getAuthorName().size());
        assertTrue(resultDto.getAuthorName().contains("Gamma"));
        assertTrue(resultDto.getAuthorName().contains("Helm"));
        assertTrue(resultDto.getAuthorName().contains("Johnson"));
    }

    @Test
    void toDetailsDTO_shouldThrowNullPointerException_whenPublicationIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(
                        null, mockBd("Title"), List.of(mockAuthor("A")), mockEdition("code", "fmt")));
    }

    @Test
    void toDetailsDTO_shouldThrowNullPointerException_whenBibliographicalDataIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(
                        mockPublication("id", Condition.GOOD), null, List.of(mockAuthor("A")), mockEdition("code", "fmt")));
    }

    @Test
    void toDetailsDTO_shouldThrowNullPointerException_whenAuthorsIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(
                        mockPublication("id", Condition.GOOD), mockBd("Title"), null, mockEdition("code", "fmt")));
    }

    @Test
    void toDetailsDTO_shouldThrowNullPointerException_whenEditionIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(
                        mockPublication("id", Condition.GOOD), mockBd("Title"), List.of(mockAuthor("A")), null));
    }

    // =========================================================================
    // toDetailsDTOs
    // =========================================================================

    @Test
    void toDetailsDTOs_returnsCorrectContentForEachDTO() {
        // Arrange
        List<PublicationSample> pubs = List.of(
                mockPublication("pub-1", Condition.NEW),
                mockPublication("pub-2", Condition.GOOD));
        List<BibliographicalData> bds = List.of(mockBd("Title A"), mockBd("Title B"));
        List<List<Author>> authors = List.of(
                List.of(mockAuthor("Author A")),
                List.of(mockAuthor("Author B")));
        List<Edition> editions = List.of(
                mockEdition("code-1", "Hardcover"),
                mockEdition("code-2", "Paperback"));

        // Act
        List<PublicationDetailsDTO> resultDtos =
                PublicationSampleMapper.toDetailsDTOs(pubs, bds, authors, editions);

        // Assert
        assertEquals(2, resultDtos.size());
        assertEquals("pub-1", resultDtos.get(0).getPublicationId());
        assertEquals("pub-2", resultDtos.get(1).getPublicationId());
    }

    @Test
    void toDetailsDTOs_shouldReturnEmptyList_whenPublicationsIsEmpty() {
        // Act
        List<PublicationDetailsDTO> resultDtos =
                PublicationSampleMapper.toDetailsDTOs(List.of(), List.of(), List.of(), List.of());

        // Assert
        assertTrue(resultDtos.isEmpty());
    }

    @Test
    void toDetailsDTOs_shouldThrowNullPointerException_whenPublicationsIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTOs(null, List.of(), List.of(), List.of()));
    }

    // =========================================================================
    // toPublicationDTO
    // =========================================================================

    @Test
    void toPublicationDTO_returnsCorrectDTO_withAllFields() {
        // Arrange
        PublicationSample pub = mockPublication("pub-1", Condition.GOOD);
        when(pub.getLibraryID().getId()).thenReturn("lib-123");
        when(pub.getCollecOfPubID().getId()).thenReturn("coll-456");

        BibliographicalData bd = mockBd("The Pragmatic Programmer");
        List<Author> authors = List.of(mockAuthor("Andy Hunt"));
        List<Genre> genres = List.of(mockGenre("Programming"));
        User owner = mockUser("dev_user");
        Edition edition = mockEdition("978-0132350884", "Hardcover");

        // Act
        PublicationDTO resultDto =
                PublicationSampleMapper.toPublicationDTO(pub, bd, edition, authors, genres, owner);

        // Assert
        assertNotNull(resultDto);
        assertEquals("pub-1", resultDto.getId());
    }

    @Test
    void toPublicationDTO_returnsCorrectGenreTypes() {
        // Arrange
        PublicationSample pub = mockPublication("pub-1", Condition.GOOD);
        when(pub.getLibraryID()).thenReturn(null);
        when(pub.getCollecOfPubID()).thenReturn(null);

        BibliographicalData bd = mockBd("Clean Code");
        List<Author> authors = List.of(mockAuthor("Robert Martin"));
        List<Genre> genres = List.of(mockGenre("Programming"), mockGenre("Software Engineering"));
        User owner = mockUser("dev_user");
        Edition edition = mockEdition("978-0132350884", "Hardcover");

        // Act
        PublicationDTO resultDto =
                PublicationSampleMapper.toPublicationDTO(pub, bd, edition, authors, genres, owner);

        // Assert
        assertNotNull(resultDto.getGenres());
        assertEquals(2, resultDto.getGenres().size());
        assertEquals("Programming", resultDto.getGenres().get(0));
        assertEquals("Software Engineering", resultDto.getGenres().get(1));
    }

    @Test
    void toPublicationDTO_returnsNullLibraryAndCollection_whenBothAreNull() {
        // Arrange
        PublicationSample pub = mockPublication("pub-1", Condition.POOR);
        when(pub.getLibraryID()).thenReturn(null);
        when(pub.getCollecOfPubID()).thenReturn(null);

        BibliographicalData bd = mockBd("The Pragmatic Programmer");
        List<Author> authors = List.of(mockAuthor("Andy Hunt"));
        List<Genre> genres = List.of(mockGenre("Programming"));
        User owner = mockUser("dev_user");
        Edition edition = mockEdition("978-0132350884", "Hardcover");

        // Act
        PublicationDTO resultDto =
                PublicationSampleMapper.toPublicationDTO(pub, bd, edition, authors, genres, owner);

        // Assert
        assertNotNull(resultDto);
        assertNull(resultDto.getLibraryId());
        assertNull(resultDto.getCollectionId());
    }

    @Test
    void toPublicationDTO_returnsNullEditionCode_whenEditionCodeIsNull() {
        // Arrange
        PublicationSample pub = mockPublication("pub-1", Condition.POOR);
        when(pub.getLibraryID()).thenReturn(null);
        when(pub.getCollecOfPubID()).thenReturn(null);

        BibliographicalData bd = mockBd("The Pragmatic Programmer");
        List<Author> authors = List.of(mockAuthor("Andy Hunt"));
        List<Genre> genres = List.of(mockGenre("Programming"));
        User owner = mockUser("dev_user");
        Edition edition = mockEdition(null, "Hardcover");

        // Act
        PublicationDTO resultDto =
                PublicationSampleMapper.toPublicationDTO(pub, bd, edition, authors, genres, owner);

        // Assert
        assertNotNull(resultDto);
        assertNull(resultDto.getCode());
    }

    @Test
    void toPublicationDTO_shouldThrowNullPointerException_whenPublicationIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toPublicationDTO(
                        null, mockBd("Title"), mockEdition("c", "f"), List.of(), List.of(), mockUser("u")));
    }

    @Test
    void toPublicationDTO_shouldThrowNullPointerException_whenBibliographicalDataIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toPublicationDTO(
                        mockPublication("1", Condition.NEW), null, mockEdition("c", "f"), List.of(), List.of(), mockUser("u")));
    }

    @Test
    void toPublicationDTO_shouldThrowNullPointerException_whenEditionIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toPublicationDTO(
                        mockPublication("1", Condition.NEW), mockBd("Title"), null, List.of(), List.of(), mockUser("u")));
    }

    // =========================================================================
    // toPublicationDTOs
    // =========================================================================

    @Test
    void toPublicationDTOs_returnsCorrectContentForEachDTO() {
        // Arrange
        List<PublicationSample> pubs = List.of(
                mockPublication("pub-1", Condition.NEW),
                mockPublication("pub-2", Condition.GOOD));
        List<BibliographicalData> bds = List.of(mockBd("Title A"), mockBd("Title B"));
        List<Edition> editions = List.of(mockEdition("code-A", "Hardcover"), mockEdition("code-B", "Paperback"));
        List<List<Author>> authors = List.of(
                List.of(mockAuthor("Author A")),
                List.of(mockAuthor("Author B")));
        List<List<Genre>> genres = List.of(
                List.of(mockGenre("Fiction")),
                List.of(mockGenre("Drama")));
        List<User> owners = List.of(mockUser("user_a"), mockUser("user_b"));

        // Act
        List<PublicationDTO> resultDtos =
                PublicationSampleMapper.toPublicationDTOs(pubs, bds, editions, authors, genres, owners);

        // Assert
        assertEquals(2, resultDtos.size());
        assertEquals("pub-1", resultDtos.get(0).getId());
        assertEquals("pub-2", resultDtos.get(1).getId());
    }

    @Test
    void toPublicationDTOs_shouldReturnEmptyList_whenPublicationsIsEmpty() {
        // Act
        List<PublicationDTO> resultDtos = PublicationSampleMapper.toPublicationDTOs(
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of());

        // Assert
        assertTrue(resultDtos.isEmpty());
    }

    @Test
    void toPublicationDTOs_shouldThrowNullPointerException_whenPublicationsIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toPublicationDTOs(
                        null, List.of(), List.of(), List.of(), List.of(), List.of()));
    }

    // =========================================================================
    // toReferenceDTO
    // =========================================================================

    @Test
    void toReferenceDTO_returnsCorrectDTO() {
        // Arrange
        PublicationSample pub = mockPublication("pub-1", Condition.GOOD);
        BibliographicalData bd = mockBd("The Pragmatic Programmer");

        // Act
        PublicationReferenceDTO resultDto = PublicationSampleMapper.toReferenceDTO(pub, bd);

        // Assert
        assertNotNull(resultDto);
        assertEquals("pub-1", resultDto.getId());
    }

    @Test
    void toReferenceDTO_shouldThrowNullPointerException_whenPublicationIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toReferenceDTO(null, mockBd("Title")));
    }

    @Test
    void toReferenceDTO_shouldThrowNullPointerException_whenBibliographicalDataIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toReferenceDTO(mockPublication("id", Condition.GOOD), null));
    }

    // =========================================================================
    // toReferenceDTOs
    // =========================================================================

    @Test
    void toReferenceDTOs_returnsCorrectContentForEachDTO() {
        // Arrange
        List<PublicationSample> pubs = List.of(
                mockPublication("pub-1", Condition.NEW),
                mockPublication("pub-2", Condition.GOOD));
        List<BibliographicalData> bds = List.of(mockBd("Title A"), mockBd("Title B"));

        // Act
        List<PublicationReferenceDTO> resultDtos =
                PublicationSampleMapper.toReferenceDTOs(pubs, bds);

        // Assert
        assertEquals(2, resultDtos.size());
        assertEquals("pub-1", resultDtos.get(0).getId());
        assertEquals("pub-2", resultDtos.get(1).getId());
    }

    @Test
    void toReferenceDTOs_shouldReturnEmptyList_whenPublicationsIsEmpty() {
        // Act
        List<PublicationReferenceDTO> resultDtos =
                PublicationSampleMapper.toReferenceDTOs(List.of(), List.of());

        // Assert
        assertTrue(resultDtos.isEmpty());
    }

    @Test
    void toReferenceDTOs_shouldThrowNullPointerException_whenPublicationsIsNull() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toReferenceDTOs(null, List.of()));
    }

    // =========================================================================
    // toUserId
    // =========================================================================

    @Test
    void toUserId_wrapsId() {
        // Act
        UserId id = PublicationSampleMapper.toUserId("user-123");

        // Assert
        assertNotNull(id);
    }

    // =========================================================================
    // toBibliographicalDataId / toBibliographicalDataIds
    // =========================================================================

    @Test
    void toBibliographicalDataId_wrapsId() {
        // Act
        BibliographicalDataId id = PublicationSampleMapper.toBibliographicalDataId("bd-99");

        // Assert
        assertNotNull(id);
    }

    @Test
    void toBibliographicalDataIds_returnsListOfIds() {
        // Act
        List<BibliographicalDataId> ids =
                PublicationSampleMapper.toBibliographicalDataIds(List.of("bd-1", "bd-2"));

        // Assert
        assertEquals(2, ids.size());
    }

    @Test
    void toBibliographicalDataIds_shouldReturnEmptyList_whenInputIsEmpty() {
        // Act
        List<BibliographicalDataId> ids =
                PublicationSampleMapper.toBibliographicalDataIds(List.of());

        // Assert
        assertTrue(ids.isEmpty());
    }

    // =========================================================================
    // toEditionId / toEditionIds
    // =========================================================================

    @Test
    void toEditionId_wrapsId() {
        // Act
        EditionId id = PublicationSampleMapper.toEditionId("ed-7");

        // Assert
        assertNotNull(id);
    }

    @Test
    void toEditionIds_returnsListOfIds() {
        // Act
        List<EditionId> ids = PublicationSampleMapper.toEditionIds(List.of("ed-10"));

        // Assert
        assertEquals(1, ids.size());
    }

    // =========================================================================
    // toBdId
    // =========================================================================

    @Test
    void toBdId_wrapsId() {
        // Act
        BibliographicalDataId id = PublicationSampleMapper.toBdId("bd-42");

        // Assert
        assertNotNull(id);
    }

    // =========================================================================
    // toPubId
    // =========================================================================

    @Test
    void toPubId_wrapsId() {
        // Act
        PublicationSampleId id = PublicationSampleMapper.toPubId("pub-99");

        // Assert
        assertNotNull(id);
    }

    // =========================================================================
    // toCollectionOfPublicationsId
    // =========================================================================

    @Test
    void toCollectionOfPublicationsId_wrapsId() {
        // Act
        CollectionOfPublicationsId id =
                PublicationSampleMapper.toCollectionOfPublicationsId("collection-404");

        // Assert
        assertNotNull(id);
    }

    // =========================================================================
    // toCondition
    // =========================================================================

    @Test
    void toCondition_returnsCorrectCondition_whenValidDescription() {
        assertEquals(Condition.GOOD, toCondition("Good"));
        assertEquals(Condition.VERY_GOOD, toCondition("very good"));
    }

    @Test
    void toCondition_shouldThrowIllegalArgumentException_whenInvalidString() {
        assertThrows(IllegalArgumentException.class, () -> toCondition("UNKNOWN"));
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    private PublicationSample mockPublication(String id, Condition condition) {
        PublicationSample pub = mock(PublicationSample.class, RETURNS_DEEP_STUBS);
        when(pub.id().getID()).thenReturn(id);
        when(pub.getCondition()).thenReturn(condition);
        return pub;
    }

    private BibliographicalData mockBd(String title) {
        BibliographicalData bd = mock(BibliographicalData.class, RETURNS_DEEP_STUBS);
        when(bd.getTitle()).thenReturn(title);
        when(bd.getOriginalLanguage().toString()).thenReturn("en");
        return bd;
    }

    private Edition mockEdition(String code, String mediaFormat) {
        Edition edition = mock(Edition.class, RETURNS_DEEP_STUBS);
        if (code != null) {
            when(edition.getCode().toString()).thenReturn(code);
        } else {
            when(edition.getCode()).thenReturn(null);
        }
        when(edition.getMediaFormat()).thenReturn(mediaFormat);
        return edition;
    }

    private Author mockAuthor(String name) {
        Author author = mock(Author.class);
        when(author.getName()).thenReturn(name);
        return author;
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