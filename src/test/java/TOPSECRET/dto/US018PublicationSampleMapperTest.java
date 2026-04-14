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

        // ARRANGE
        PublicationSample publication = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);

        when(publication.id()).thenReturn(id);

        // ACT
        US018PublicationSampleDTO dto =
                US018PublicationSampleMapper.toDTO(publication, "Title");

        // ASSERT
        assertEquals(id, dto.id());
        assertEquals("Title", dto.title());
    }

    @Test
    void shouldConvertListToDTO() {

        // ARRANGE
        PublicationSample publication = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);

        when(publication.id()).thenReturn(id);

        // ACT
        List<US018PublicationSampleDTO> result =
                US018PublicationSampleMapper.toDTOs(
                        List.of(publication),
                        List.of("Title")
                );

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
        assertEquals("Title", result.get(0).title());
    }

    @Test
    void shouldReturnEmptyListWhenInputIsNull() {

        // ACT
        List<US018PublicationSampleDTO> result =
                US018PublicationSampleMapper.toDTOs(null, List.of());

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenPublicationIsNull() {

        // ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> US018PublicationSampleMapper.toDTO(null, "Title"));
    }

    @Test
    void shouldReturnMutableListWhenPublicationsIsNull() {

        // ACT
        List<US018PublicationSampleDTO> result =
                US018PublicationSampleMapper.toDTOs(null, List.of());

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
        PublicationSample p1 = mock(PublicationSample.class);
        PublicationSample p2 = mock(PublicationSample.class);

        when(p1.id()).thenReturn(mock(PublicationSampleId.class));
        when(p2.id()).thenReturn(mock(PublicationSampleId.class));

        // ACT
        List<US018PublicationSampleDTO> result =
                US018PublicationSampleMapper.toDTOs(
                        List.of(p1, p2),
                        List.of("T1", "T2")
                );

        // ASSERT
        assertEquals(2, result.size());
    }
}