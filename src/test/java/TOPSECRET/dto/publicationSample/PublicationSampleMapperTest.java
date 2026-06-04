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
 * Unit tests for the {@link PublicationSampleMapper} class.
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
        PublicationDetailsDTO resultDto = PublicationSampleMapper.toDetailsDTO(pub, bd, authors, edition);

        // Assert
        assertNotNull(resultDto);
        assertEquals("pub-1", resultDto.getPublicationId());
    }

    @Test
    void toDetailsDTO_multipleAuthors() {
        // Arrange
        PublicationSample pub  = mockPublication("pub-2", Condition.NEW);
        BibliographicalData bd = mockBd("GoF");
        List<Author> authors = List.of(mockAuthor("Gamma"), mockAuthor("Helm"), mockAuthor("Johnson"));
        Edition edition  = mockEdition("978-0201633610", "Paperback");

        // Act
        PublicationDetailsDTO resultDto = PublicationSampleMapper.toDetailsDTO(pub, bd, authors, edition);

        // Assert
        assertNotNull(resultDto);
        assertEquals(3, resultDto.getAuthorName().size());
    }

    @Test
    void toDetailsDTO_throwsOnNullParameters() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(null, mockBd("Title"), List.of(mockAuthor("A")), mockEdition("code", "fmt")));

        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(mockPublication("id", Condition.GOOD), null, List.of(mockAuthor("A")), mockEdition("code", "fmt")));

        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(mockPublication("id", Condition.GOOD), mockBd("Title"), null, mockEdition("code", "fmt")));

        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toDetailsDTO(mockPublication("id", Condition.GOOD), mockBd("Title"), List.of(mockAuthor("A")), null));
    }

    // -------------------------------------------------------------------------
    // toDetailsDTOs
    // -------------------------------------------------------------------------

    @Test
    void toDetailsDTOs_returnsOneDTOPerPublication() {
        // Arrange
        List<PublicationSample> pubs = List.of(mockPublication("pub-1", Condition.NEW), mockPublication("pub-2", Condition.GOOD));
        List<BibliographicalData> bds = List.of(mockBd("Title A"), mockBd("Title B"));
        List<List<Author>> authors = List.of(List.of(mockAuthor("Author A")), List.of(mockAuthor("Author B")));
        List<Edition> editions = List.of(mockEdition("code-1", "Hardcover"), mockEdition("code-2", "Paperback"));

        // Act
        List<PublicationDetailsDTO> resultDtos = PublicationSampleMapper.toDetailsDTOs(pubs, bds, authors, editions);

        // Assert
        assertEquals(2, resultDtos.size());
        assertEquals("pub-1", resultDtos.get(0).getPublicationId());
    }

    @Test
    void toDetailsDTOs_emptyListReturnsEmpty() {
        List<PublicationDetailsDTO> resultDtos = PublicationSampleMapper.toDetailsDTOs(List.of(), List.of(), List.of(), List.of());
        assertTrue(resultDtos.isEmpty());
    }

    @Test
    void toDetailsDTOs_throwsOnNullPublications() {
        assertThrows(NullPointerException.class, () -> PublicationSampleMapper.toDetailsDTOs(null, List.of(), List.of(), List.of()));
    }

    // -------------------------------------------------------------------------
    // toPublicationDTO
    // -------------------------------------------------------------------------

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
        PublicationDTO resultDto = PublicationSampleMapper.toPublicationDTO(pub, bd, edition, authors, genres, owner);

        // Assert
        assertNotNull(resultDto);
        assertEquals("pub-1", resultDto.getId());
    }

    @Test
    void toPublicationDTO_returnsCorrectDTO_whenLibraryAndCollectionAreNull() {
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
        PublicationDTO resultDto = PublicationSampleMapper.toPublicationDTO(pub, bd, edition, authors, genres, owner);

        // Assert
        assertNotNull(resultDto);
    }

    @Test
    void toPublicationDTO_returnsCorrectDTO_whenEditionCodeIsNull() {
        // Arrange
        PublicationSample pub = mockPublication("pub-1", Condition.POOR);
        when(pub.getLibraryID()).thenReturn(null);
        when(pub.getCollecOfPubID()).thenReturn(null);

        BibliographicalData bd = mockBd("The Pragmatic Programmer");
        List<Author> authors = List.of(mockAuthor("Andy Hunt"));
        List<Genre> genres = List.of(mockGenre("Programming"));
        User owner = mockUser("dev_user");

        // Pass NULL code to safely test: edition.getCode() != null
        Edition edition = mockEdition(null, "Hardcover");

        // Act
        PublicationDTO resultDto = PublicationSampleMapper.toPublicationDTO(pub, bd, edition, authors, genres, owner);

        // Assert
        assertNotNull(resultDto);
    }

    @Test
    void toPublicationDTO_throwsOnNullParameters() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toPublicationDTO(null, mockBd("Title"), mockEdition("c", "f"), List.of(), List.of(), mockUser("u")));

        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toPublicationDTO(mockPublication("1", Condition.NEW), null, mockEdition("c", "f"), List.of(), List.of(), mockUser("u")));

        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toPublicationDTO(mockPublication("1", Condition.NEW), mockBd("Title"), null, List.of(), List.of(), mockUser("u")));
    }

    // -------------------------------------------------------------------------
    // toPublicationDTOs
    // -------------------------------------------------------------------------

    @Test
    void toPublicationDTOs_returnsOneDTOPerPublication() {
        // Arrange
        List<PublicationSample> pubs = List.of(mockPublication("pub-1", Condition.NEW));
        List<BibliographicalData> bds = List.of(mockBd("Title A"));
        List<Edition> editions = List.of(mockEdition("code-A", "Hardcover"));
        List<List<Author>> authors = List.of(List.of(mockAuthor("Author A")));
        List<List<Genre>> genres = List.of(List.of(mockGenre("Fiction")));
        List<User> owners = List.of(mockUser("user_a"));

        // Act
        List<PublicationDTO> resultDtos = PublicationSampleMapper.toPublicationDTOs(pubs, bds, editions, authors, genres, owners);

        // Assert
        assertEquals(1, resultDtos.size());
        assertEquals("pub-1", resultDtos.get(0).getId());
    }

    @Test
    void toPublicationDTOs_emptyListReturnsEmpty() {
        assertTrue(PublicationSampleMapper.toPublicationDTOs(List.of(), List.of(), List.of(), List.of(), List.of(), List.of()).isEmpty());
    }

    @Test
    void toPublicationDTOs_throwsOnNullPublications() {
        assertThrows(NullPointerException.class, () ->
                PublicationSampleMapper.toPublicationDTOs(null, List.of(), List.of(), List.of(), List.of(), List.of()));
    }

    // -------------------------------------------------------------------------
    // toReferenceDTO / toReferenceDTOs
    // -------------------------------------------------------------------------

    @Test
    void toReferenceDTO_returnsCorrectDTO() {
        PublicationSample pub  = mockPublication("pub-1", Condition.GOOD);
        BibliographicalData bd = mockBd("The Pragmatic Programmer");

        PublicationReferenceDTO resultDto = PublicationSampleMapper.toReferenceDTO(pub, bd);

        assertNotNull(resultDto);
        assertEquals("pub-1", resultDto.getId());
    }

    @Test
    void toReferenceDTO_throwsOnNullParameters() {
        assertThrows(NullPointerException.class, () -> PublicationSampleMapper.toReferenceDTO(null, mockBd("Title")));
        assertThrows(NullPointerException.class, () -> PublicationSampleMapper.toReferenceDTO(mockPublication("id", Condition.GOOD), null));
    }

    @Test
    void toReferenceDTOs_returnsOneDTOPerPublication() {
        List<PublicationSample> pubs = List.of(mockPublication("pub-1", Condition.NEW));
        List<BibliographicalData> bds = List.of(mockBd("Title A"));

        List<PublicationReferenceDTO> resultDtos = PublicationSampleMapper.toReferenceDTOs(pubs, bds);

        assertEquals(1, resultDtos.size());
    }

    @Test
    void toReferenceDTOs_emptyListReturnsEmpty() {
        assertTrue(PublicationSampleMapper.toReferenceDTOs(List.of(), List.of()).isEmpty());
    }

    // -------------------------------------------------------------------------
    // Value Objects / Primitives conversion
    // -------------------------------------------------------------------------

    @Test
    void toPubId_wrapsId() {
        PublicationSampleId id = PublicationSampleMapper.toPubId("pub-99");
        assertNotNull(id);
    }

    @Test
    void toBdId_wrapsId() {
        BibliographicalDataId id = PublicationSampleMapper.toBdId("bd-42");
        assertNotNull(id);
    }

    @Test
    void toEditionId_wrapsId() {
        EditionId id = PublicationSampleMapper.toEditionId("ed-7");
        assertNotNull(id);
    }

    @Test
    void toCondition_validDescription_returnsCorrectCondition() {
        assertEquals(Condition.GOOD, toCondition("Good"));
        assertEquals(Condition.VERY_GOOD, toCondition("very good"));
    }

    @Test
    void toCondition_invalidString_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> toCondition("UNKNOWN"));
    }

    @Test
    void toUserId_wrapsId() {
        UserId id = PublicationSampleMapper.toUserId("user-123");
        assertNotNull(id);
    }

    @Test
    void toBibliographicalDataIds_returnsListOfIds() {
        List<BibliographicalDataId> ids = PublicationSampleMapper.toBibliographicalDataIds(List.of("bd-1", "bd-2"));
        assertEquals(2, ids.size());
    }

    @Test
    void toBibliographicalDataIds_emptyListReturnsEmpty() {
        assertTrue(PublicationSampleMapper.toBibliographicalDataIds(List.of()).isEmpty());
    }

    @Test
    void toBibliographicalDataId_wrapsId() {
        BibliographicalDataId id = PublicationSampleMapper.toBibliographicalDataId("bd-99");
        assertNotNull(id);
    }

    @Test
    void toEditionIds_returnsListOfIds() {
        List<EditionId> ids = PublicationSampleMapper.toEditionIds(List.of("ed-10"));
        assertEquals(1, ids.size());
    }

    @Test
    void toCollectionOfPublicationsId_wrapsId() {
        CollectionOfPublicationsId id = PublicationSampleMapper.toCollectionOfPublicationsId("collection-404");
        assertNotNull(id);
    }

    // -------------------------------------------------------------------------
    // Helpers (Refactored to use deep stubs to prevent ClassCastExceptions)
    // -------------------------------------------------------------------------

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