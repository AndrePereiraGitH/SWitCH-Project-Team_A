package TOPSECRET.dto.PublicationType;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.PublicationType.response.PublicationTypeResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationTypeMapperTest {

    @Test
    void toDTOValidPublicationType() {

        // Arrange
        PublicationTypeMapper mapper = new PublicationTypeMapper();

        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(publicationType.id()).thenReturn(publicationTypeId);
        when(publicationTypeId.getId()).thenReturn("1");
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        PublicationTypeResponseDTO dto = mapper.toDTO(publicationType);

        // Assert
        assertNotNull(dto);
        assertEquals("1", dto.getId());
        assertEquals("Book", dto.getMediaFormat());
    }

    @Test
    void toDTOThrowsWhenPublicationTypeIsNull() {

        // Arrange
        PublicationTypeMapper mapper = new PublicationTypeMapper();

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> mapper.toDTO(null)
        );
    }

    @Test
    void toDTOListThrowsWhenListIsNull() {

        // Arrange
        PublicationTypeMapper mapper = new PublicationTypeMapper();

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> mapper.toDTOList(null)
        );
    }

    @Test
    void toDTOListReturnsEmptyList() {

        // Arrange
        PublicationTypeMapper mapper = new PublicationTypeMapper();

        // Act
        List<PublicationTypeResponseDTO> result =
                mapper.toDTOList(List.of());

        // Assert
        assertNotNull(result);

        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOListOneElement() {

        // Arrange
        PublicationTypeMapper mapper = new PublicationTypeMapper();

        PublicationType publicationType = mock(PublicationType.class);

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(publicationType.id()).thenReturn(publicationTypeId);
        when(publicationTypeId.getId()).thenReturn("1");
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        List<PublicationTypeResponseDTO> result =
                mapper.toDTOList(
                        List.of(publicationType)
                );

        // Assert
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Book", result.get(0).getMediaFormat());
    }

    @Test
    void toDTOListMultipleElements() {

        // Arrange
        PublicationTypeMapper mapper = new PublicationTypeMapper();

        PublicationType pt1 = mock(PublicationType.class);
        PublicationType pt2 = mock(PublicationType.class);

        PublicationTypeId id1 = mock(PublicationTypeId.class);
        PublicationTypeId id2 = mock(PublicationTypeId.class);

        when(pt1.id()).thenReturn(id1);
        when(id1.getId()).thenReturn("1");
        when(pt1.getMediaFormat()).thenReturn("Book");

        when(pt2.id()).thenReturn(id2);
        when(id2.getId()).thenReturn("2");
        when(pt2.getMediaFormat()).thenReturn("Magazine");

        // Act
        List<PublicationTypeResponseDTO> result =
                mapper.toDTOList(List.of(pt1, pt2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Book", result.get(0).getMediaFormat());
        assertEquals("2", result.get(1).getId());
        assertEquals("Magazine", result.get(1).getMediaFormat());
    }
}