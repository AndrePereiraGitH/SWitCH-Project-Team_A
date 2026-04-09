package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link US023BibliographicalDataMapper}, responsible for validating
 * the conversion of {@link BibliographicalData} domain objects into
 * {@link US023BibliographicalDataDTO} instances for use in user story US023.
 *
 * <p>
 * This test class ensures that the mapper:
 * <ul>
 *     <li>Handles valid bibliographical data and correctly extracts titles,
 *         identifiers, and associated author names.</li>
 *     <li>Interacts properly with the {@link IAuthorRepository} when resolving
 *         author identifiers into domain {@link Author} objects.</li>
 *     <li>Correctly propagates exceptional conditions such as missing authors
 *         or null inputs.</li>
 *     <li>Supports conversion of both single entities and collections.</li>
 *     <li>Accepts a null author repository when constructed, ensuring flexibility
 *         in contexts where author resolution is optional.</li>
 * </ul>
 */
class US023BibliographicalDataMapperTest {

    // =================================================================================================================
    // =============== CONSTRUCTOR =====================================================================================
    // =================================================================================================================

    @Test
    void constructor_ShouldNotThrowWhenAuthorRepositoryIsNull() throws InstanceNotFoundException {
        // Act + Assert
        assertDoesNotThrow(() ->
                new US023BibliographicalDataMapper(null));
    }


    // =================================================================================================================
    // =============== TO DTO ==========================================================================================
    // =================================================================================================================

    @Test
    void toDTO_ShouldConvertBibliographicalDataCorrectly() throws Exception {
        // Arrange
        BibliographicalDataID expectedBDId = new BibliographicalDataID("bdId1");
        String expectedTitle = "Title1";
        String expectedAuthor1 = "Author1";
        String expectedAuthor2 = "Author2";
        List<String> expectedAuthors = List.of(expectedAuthor1, expectedAuthor2);

        Author author1 = mock(Author.class);
        AuthorId authorId1 = new AuthorId("A1");
        when(author1.getName()).thenReturn(expectedAuthor1);
        Author author2 = mock(Author.class);
        AuthorId authorId2 = new AuthorId("A2");
        when(author2.getName()).thenReturn(expectedAuthor2);

        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.id()).thenReturn(expectedBDId);
        when(bd.getTitle()).thenReturn(expectedTitle);
        when(bd.getAuthors()).thenReturn(List.of(authorId1, authorId2));

        IAuthorRepository authorRepo = mock(IAuthorRepository.class);
        when(authorRepo.findAuthorById(authorId1)).thenReturn(Optional.of(author1));
        when(authorRepo.findAuthorById(authorId2)).thenReturn(Optional.of(author2));

        US023BibliographicalDataMapper mapper = new US023BibliographicalDataMapper(authorRepo);

        // Act
        US023BibliographicalDataDTO dto = mapper.toDTO(bd);

        // Assert
        assertEquals(expectedBDId, dto.bdId());
        assertEquals(expectedTitle, dto.pubTitle());
        assertEquals(expectedAuthors, dto.authorName());
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenPublicationIsNull() {
        // Arrange
        US023BibliographicalDataMapper mapper =
                new US023BibliographicalDataMapper(mock(IAuthorRepository.class));

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> mapper.toDTO(null));
    }

    @Test
    void toDTO_shouldThrowInstanceNotFoundWhenAuthorNotFound() {
        // Arrange
        IAuthorRepository authorRepo = mock(IAuthorRepository.class);
        US023BibliographicalDataMapper mapper = new US023BibliographicalDataMapper(authorRepo);

        AuthorId missingId = new AuthorId("A404");

        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.id()).thenReturn(new BibliographicalDataID("BD1"));
        when(bd.getTitle()).thenReturn("Some Book");
        when(bd.getAuthors()).thenReturn(List.of(missingId));

        when(authorRepo.findAuthorById(missingId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(bd));
    }


    // =================================================================================================================
    // =============== TO DTOs =========================================================================================
    // =================================================================================================================

    @Test
    void toDTOs_shouldConvertEntireList() throws Exception {
        // Arrange
        IAuthorRepository authorRepo = mock(IAuthorRepository.class);
        US023BibliographicalDataMapper mapper = new US023BibliographicalDataMapper(authorRepo);

        AuthorId authorId = new AuthorId("A1");
        Author author = mock(Author.class);
        when(author.getName()).thenReturn("Author One");
        when(authorRepo.findAuthorById(authorId)).thenReturn(Optional.of(author));

        BibliographicalData bd1 = mock(BibliographicalData.class);
        when(bd1.id()).thenReturn(new BibliographicalDataID("BD1"));
        when(bd1.getTitle()).thenReturn("Book 1");
        when(bd1.getAuthors()).thenReturn(List.of(authorId));

        BibliographicalData bd2 = mock(BibliographicalData.class);
        when(bd2.id()).thenReturn(new BibliographicalDataID("BD2"));
        when(bd2.getTitle()).thenReturn("Book 2");
        when(bd2.getAuthors()).thenReturn(List.of(authorId));

        List<BibliographicalData> list = List.of(bd1, bd2);

        // Act
        List<US023BibliographicalDataDTO> result = mapper.toDTOs(list);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).pubTitle());
        assertEquals("Book 2", result.get(1).pubTitle());
    }

    @Test
    void toDTOs_shouldThrowExceptionWhenListIsNull() {
        // Arrange
        US023BibliographicalDataMapper mapper =
                new US023BibliographicalDataMapper(mock(IAuthorRepository.class));

        // Act + Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTOs(null));
    }
}
