package TOPSECRET.dto;

import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US018PublicationSampleMapperTest {

    @Test
    void shouldConvertPublicationToDTO() {

        PublicationSample publication = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);

        when(publication.id()).thenReturn(id);

        US018PublicationSampleDTO dto =
                US018PublicationSampleMapper.toDTO(publication, "Title");

        assertEquals(id, dto.id());
        assertEquals("Title", dto.title());
    }

    @Test
    void shouldConvertListToDTO() {

        PublicationSample publication = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);

        when(publication.id()).thenReturn(id);

        List<US018PublicationSampleDTO> result =
                US018PublicationSampleMapper.toDTOs(
                        List.of(publication),
                        List.of("Title")
                );

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
        assertEquals("Title", result.get(0).title());
    }

    @Test
    void shouldReturnEmptyListWhenInputIsNull() {

        List<US018PublicationSampleDTO> result =
                US018PublicationSampleMapper.toDTOs(null, List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenPublicationIsNull() {

        assertThrows(IllegalArgumentException.class,
                () -> US018PublicationSampleMapper.toDTO(null, "Title"));
    }

    @Test
    void shouldReturnMutableListWhenPublicationsIsNull() {

        List<US018PublicationSampleDTO> result =
                US018PublicationSampleMapper.toDTOs(null, List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());

        assertDoesNotThrow(() ->
                result.add(mock(US018PublicationSampleDTO.class))
        );
    }

    @Test
    void shouldMapMultiplePublicationsCorrectly() {

        PublicationSample p1 = mock(PublicationSample.class);
        PublicationSample p2 = mock(PublicationSample.class);

        when(p1.id()).thenReturn(mock(PublicationSampleId.class));
        when(p2.id()).thenReturn(mock(PublicationSampleId.class));

        List<US018PublicationSampleDTO> result =
                US018PublicationSampleMapper.toDTOs(
                        List.of(p1, p2),
                        List.of("T1", "T2")
                );

        assertEquals(2, result.size());
    }
}