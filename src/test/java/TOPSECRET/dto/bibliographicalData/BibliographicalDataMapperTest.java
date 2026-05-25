package TOPSECRET.dto.bibliographicalData;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import TOPSECRET.services.BibliographicalDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the BibliographicalDataMapper class.
 * <p>
 * This test suite validates the behavior of the mapper responsible for
 * converting BibliographicalData domain objects into
 * responseBibliographicalDataDTO objects. It covers constructor validation,
 * null argument handling, successful DTO mappings, exception scenarios when
 * authors are not found, and list conversion operations.
 */
@ExtendWith(MockitoExtension.class)
class BibliographicalDataMapperTest {

    @Mock
    private BibliographicalDataService service;

    @InjectMocks
    private BibliographicalDataMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BibliographicalDataMapper(service);
    }

    @Test
    void constructor_nullService_throwsNullPointerException() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new BibliographicalDataMapper(null)
        );

        // Assert
        assertEquals("Service cannot be null", exception.getMessage());
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

        when(bd.id()).thenReturn(bdId);
        when(bdId.getID()).thenReturn("BD1");
        when(bd.getTitle()).thenReturn("Title example");

        when(service.getAuthors(bd)).thenReturn(List.of("Author name"));

        // Act
        responseBibliographicalDataDTO dto = mapper.toDTO(bd);

        // Assert
        assertEquals("BD1", dto.getBdId());
        assertEquals("Title example", dto.getPubTitle());
        assertEquals(List.of("Author name"), dto.getAuthorName());
    }

    @Test
    void toDTO_authorNotFound_throwsInstanceNotFoundException() throws InstanceNotFoundException {
        // Arrange
        BibliographicalData bd = mock(BibliographicalData.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        when(bd.id()).thenReturn(bdId);
        when(service.getAuthors(bd)).thenThrow(new InstanceNotFoundException("Author was not found:"));

        // Act
        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> mapper.toDTO(bd)
        );

        // Assert
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

        when(bd1.id()).thenReturn(new BibliographicalDataId("BD1"));
        when(bd1.getTitle()).thenReturn("Book One");

        when(bd2.id()).thenReturn(new BibliographicalDataId("BD2"));
        when(bd2.getTitle()).thenReturn("Book Two");

        when(service.getAuthors(bd1)).thenReturn(List.of("Author One"));
        when(service.getAuthors(bd2)).thenReturn(List.of("Author Two"));

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