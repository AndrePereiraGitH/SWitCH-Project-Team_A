package TOPSECRET.dto.bibliographicalData;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BibliographicalDataMapperTest {

    private IAuthorRepository authorRepo;
    private BibliographicalDataMapper mapper;

    @BeforeEach
    void setUp() {
        authorRepo = mock(IAuthorRepository.class);
        mapper = new BibliographicalDataMapper(authorRepo);
    }

    @Test
    void constructor_nullAuthorRepo_throwsNullPointerException() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new BibliographicalDataMapper(null)
        );

        // Assert
        assertEquals("Author repository cannot be null", exception.getMessage());
    }

    @Test
    void toDTO_nullBibliographicalData_throwsIllegalArgumentException() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toDTO(null)
        );

        // Assert
        assertEquals("Bibliographical data cannot be null", exception.getMessage());
    }

    @Test
    void toDTO_validBibliographicalData_returnsDTO() throws InstanceNotFoundException {
        // Arrange
        BibliographicalData bd = mock(BibliographicalData.class);

        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        AuthorId authorId = mock(AuthorId.class);
        Author author = mock(Author.class);

        when(bd.id()).thenReturn(bdId);
        when(bdId.getID()).thenReturn("BD1");
        when(bd.getTitle()).thenReturn("Title example");
        when(bd.getAuthors()).thenReturn(List.of(authorId));

        when(authorRepo.findById(authorId)).thenReturn(Optional.of(author));
        when(author.getName()).thenReturn("Author example");

        // Act
        responseBibliographicalDataDTO dto = mapper.toDTO(bd);

        // Arrange
        assertEquals("BD1", dto.getBdId());
        assertEquals("Title example", dto.getPubTitle());
        assertEquals(List.of("Author example"), dto.getAuthorName());
    }

    @Test
    void toDTO_authorNotFound_throwsInstanceNotFoundException() {
        BibliographicalData bd = mock(BibliographicalData.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        AuthorId authorId = mock(AuthorId.class);

        when(bd.id()).thenReturn(bdId);
        when(bdId.toString()).thenReturn("BD1");
        when(bd.getTitle()).thenReturn("Some title");
        when(bd.getAuthors()).thenReturn(List.of(authorId));

        when(authorRepo.findById(authorId)).thenReturn(Optional.empty());

        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> mapper.toDTO(bd)
        );

        assertTrue(exception.getMessage().contains("Author was not found:"));
    }

    @Test
    void toDTOs_nullList_throwsIllegalArgumentException() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toDTOs(null)
        );

        // Assert
        assertEquals("Bibliographical data cannot be null", exception.getMessage());
    }

    @Test
    void toDTOs_validList_returnsDTOList() throws InstanceNotFoundException {
        // Arrange
        BibliographicalData bd1 = mock(BibliographicalData.class);
        BibliographicalData bd2 = mock(BibliographicalData.class);

        AuthorId authorId1 = mock(AuthorId.class);
        AuthorId authorId2 = mock(AuthorId.class);

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);

        when(bd1.id()).thenReturn(new BibliographicalDataId("BD1"));
        when(bd1.getTitle()).thenReturn("Book One");
        when(bd1.getAuthors()).thenReturn(List.of(authorId1));

        when(bd2.id()).thenReturn(new BibliographicalDataId("BD2"));
        when(bd2.getTitle()).thenReturn("Book Two");
        when(bd2.getAuthors()).thenReturn(List.of(authorId2));

        when(authorRepo.findById(authorId1)).thenReturn(Optional.of(author1));
        when(authorRepo.findById(authorId2)).thenReturn(Optional.of(author2));

        when(author1.getName()).thenReturn("Author One");
        when(author2.getName()).thenReturn("Author Two");

        // Act
        List<responseBibliographicalDataDTO> result = mapper.toDTOs(List.of(bd1, bd2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("BD1", result.get(0).getBdId());
        assertEquals("Book One", result.get(0).getPubTitle());
        assertEquals(List.of("Author One"), result.get(0).getAuthorName());

        assertEquals("BD2", result.get(1).getBdId());
        assertEquals("Book Two", result.get(1).getPubTitle());
        assertEquals(List.of("Author Two"), result.get(1).getAuthorName());
    }

    @Test
    void toDTOs_emptyList_returnsEmptyList() throws InstanceNotFoundException {
        // Act
        List<responseBibliographicalDataDTO> result = mapper.toDTOs(List.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}