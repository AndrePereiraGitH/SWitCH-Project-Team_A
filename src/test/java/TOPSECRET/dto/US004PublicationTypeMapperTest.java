package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US004PublicationTypeMapperTest {

    @Test
    void shouldMapPublicationTypeToDTO() {

        // ARRANGE
        PublicationType type = mock(PublicationType.class);
        PublicationTypeId id = mock(PublicationTypeId.class);

        when(type.id()).thenReturn(id);
        when(type.getMediaFormat()).thenReturn("Book");

        // ACT
        US004PublicationTypeDTO result = US004PublicationTypeMapper.toDTO(type);

        // ASSERT
        assertEquals(id, result.id());
        assertEquals("Book", result.mediaFormat());
    }

    @Test
    void shouldConvertPublicationTypesToDTO() {

        // ARRANGE
        PublicationType type = mock(PublicationType.class);
        PublicationTypeId id = mock(PublicationTypeId.class);

        when(type.id()).thenReturn(id);
        when(type.getMediaFormat()).thenReturn("Book");

        List<PublicationType> types = List.of(type);

        // ACT
        List<US004PublicationTypeDTO> result = US004PublicationTypeMapper.toDTOs(types);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
        assertEquals("Book", result.get(0).mediaFormat());
    }

    @Test
    void shouldReturnEmptyListWhenInputIsEmpty() {

        // ARRANGE
        List<PublicationType> types = List.of();

        // ACT
        List<US004PublicationTypeDTO> result = US004PublicationTypeMapper.toDTOs(types);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertMultiplePublicationTypes() {

        // ARRANGE
        PublicationType type1 = mock(PublicationType.class);
        PublicationTypeId id1 = mock(PublicationTypeId.class);
        when(type1.id()).thenReturn(id1);

        PublicationType type2 = mock(PublicationType.class);
        PublicationTypeId id2 = mock(PublicationTypeId.class);
        when(type2.id()).thenReturn(id2);

        when(type1.getMediaFormat()).thenReturn("Book");
        when(type2.getMediaFormat()).thenReturn("Magazine");

        List<PublicationType> types = List.of(type1, type2);

        // ACT
        List<US004PublicationTypeDTO> result = US004PublicationTypeMapper.toDTOs(types);

        // ASSERT
        assertEquals(2, result.size());
        assertEquals("Book", result.get(0).mediaFormat());
        assertEquals("Magazine", result.get(1).mediaFormat());
    }

    @Test
    void shouldThrowExceptionWhenPublicationTypeIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> US004PublicationTypeMapper.toDTO(null));
    }
}