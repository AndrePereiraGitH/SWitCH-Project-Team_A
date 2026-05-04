package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US018PublicationSampleMapperTest {

    @Test
    void shouldConvertPublicationToDTO() {

        // ARRANGE
        IBibliographicalDataRepository repo = mock(IBibliographicalDataRepository.class);
        US018PublicationSampleMapper mapper = new US018PublicationSampleMapper(repo);

        PublicationSample publication = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);
        BibliographicalDataID bId = mock(BibliographicalDataID.class);
        BibliographicalData data = mock(BibliographicalData.class);

        when(publication.id()).thenReturn(id);
        when(publication.getBibliographicalDataID()).thenReturn(bId);
        when(repo.findById(bId)).thenReturn(Optional.of(data));
        when(data.getTitle()).thenReturn("Title");

        // ACT
        US018PublicationSampleDTO dto =
                mapper.toDTO(publication);

        // ASSERT
        assertEquals(id, dto.id());
        assertEquals("Title", dto.title());
    }

    @Test
    void shouldConvertListToDTO() {

        // ARRANGE
        IBibliographicalDataRepository repo = mock(IBibliographicalDataRepository.class);
        US018PublicationSampleMapper mapper = new US018PublicationSampleMapper(repo);

        PublicationSample publication = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);
        BibliographicalDataID bId = mock(BibliographicalDataID.class);
        BibliographicalData data = mock(BibliographicalData.class);

        when(publication.id()).thenReturn(id);
        when(publication.getBibliographicalDataID()).thenReturn(bId);
        when(repo.findById(bId)).thenReturn(Optional.of(data));
        when(data.getTitle()).thenReturn("Title");

        // ACT
        List<US018PublicationSampleDTO> result =
                mapper.toDTOs(List.of(publication));

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
        assertEquals("Title", result.get(0).title());
    }

    @Test
    void shouldReturnEmptyListWhenInputIsNull() {

        // ARRANGE
        US018PublicationSampleMapper mapper =
                new US018PublicationSampleMapper(mock(IBibliographicalDataRepository.class));

        // ACT
        List<US018PublicationSampleDTO> result =
                mapper.toDTOs(null);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenPublicationIsNull() {

        // ARRANGE
        US018PublicationSampleMapper mapper =
                new US018PublicationSampleMapper(mock(IBibliographicalDataRepository.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> mapper.toDTO(null));
    }

    @Test
    void shouldReturnMutableListWhenPublicationsIsNull() {

        // ARRANGE
        US018PublicationSampleMapper mapper =
                new US018PublicationSampleMapper(mock(IBibliographicalDataRepository.class));

        // ACT
        List<US018PublicationSampleDTO> result =
                mapper.toDTOs(null);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());

        assertDoesNotThrow(() ->
                result.add(mock(US018PublicationSampleDTO.class))
        );
    }

    @Test
    void shouldMapMultiplePublicationsCorrectly() {

        // ARRANGE
        IBibliographicalDataRepository repo = mock(IBibliographicalDataRepository.class);
        US018PublicationSampleMapper mapper = new US018PublicationSampleMapper(repo);

        PublicationSample p1 = mock(PublicationSample.class);
        PublicationSample p2 = mock(PublicationSample.class);

        BibliographicalDataID b1 = mock(BibliographicalDataID.class);
        BibliographicalDataID b2 = mock(BibliographicalDataID.class);

        BibliographicalData d1 = mock(BibliographicalData.class);
        BibliographicalData d2 = mock(BibliographicalData.class);

        when(p1.id()).thenReturn(mock(PublicationSampleId.class));
        when(p2.id()).thenReturn(mock(PublicationSampleId.class));

        when(p1.getBibliographicalDataID()).thenReturn(b1);
        when(p2.getBibliographicalDataID()).thenReturn(b2);

        when(repo.findById(b1)).thenReturn(Optional.of(d1));
        when(repo.findById(b2)).thenReturn(Optional.of(d2));

        when(d1.getTitle()).thenReturn("T1");
        when(d2.getTitle()).thenReturn("T2");

        // ACT
        List<US018PublicationSampleDTO> result =
                mapper.toDTOs(List.of(p1, p2));

        // ASSERT
        assertEquals(2, result.size());
        assertEquals("T1", result.get(0).title());
        assertEquals("T2", result.get(1).title());
    }

    @Test
    void shouldThrowExceptionWhenBibliographicalDataNotFound() {

        // ARRANGE
        IBibliographicalDataRepository repo = mock(IBibliographicalDataRepository.class);
        US018PublicationSampleMapper mapper = new US018PublicationSampleMapper(repo);

        PublicationSample publication = mock(PublicationSample.class);
        BibliographicalDataID bId = mock(BibliographicalDataID.class);

        when(publication.getBibliographicalDataID()).thenReturn(bId);
        when(repo.findById(bId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> mapper.toDTO(publication));
    }
}