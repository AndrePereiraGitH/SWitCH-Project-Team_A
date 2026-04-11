package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US014PublicationTypeMapperTest {

    @Test
    void toDTOValidPublicationType() {
        // Arrange
        PublicationType pt = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);
        when(pt.getMediaFormat()).thenReturn("Book");
        when(publicationTypeId.getId()).thenReturn("1");
        when(pt.getId()).thenReturn(publicationTypeId);

        // Act
        US014PublicationTypeDTO dto =
                US014PublicationTypeMapper.toDTO(pt);

        // Assert
        assertEquals("Book", dto.mediaFormat());
        assertEquals("1", dto.id());
    }

    @Test
    void toDTOThrowsWhenNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                US014PublicationTypeMapper.toDTO(null));
    }

    @Test
    void toDTOListEmptyListReturnsEmptyList() {

        // Act
        List<US014PublicationTypeDTO> result =
                US014PublicationTypeMapper.toDTOList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOListOneElement() {
        // Arrange
        PublicationType pt = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);
        when(pt.getMediaFormat()).thenReturn("Book");
        when(publicationTypeId.getId()).thenReturn("1");
        when(pt.getId()).thenReturn(publicationTypeId);

        // Act
        List<US014PublicationTypeDTO> result =
                US014PublicationTypeMapper.toDTOList(List.of(pt));

        // Assert
        assertEquals(1, result.size());
        assertEquals("Book", result.get(0).mediaFormat());
        assertEquals("1", result.get(0).id());
    }

    @Test
    void toDTOListMultipleElements() {

        // Arrange
        PublicationType pt1 = mock(PublicationType.class);
        PublicationType pt2 = mock(PublicationType.class);

        PublicationTypeId publicationTypeId1 = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId2 = mock(PublicationTypeId.class);

        when(pt1.getMediaFormat()).thenReturn("Book");
        when(publicationTypeId1.getId()).thenReturn("1");
        when(pt1.getId()).thenReturn(publicationTypeId1);

        when(pt2.getMediaFormat()).thenReturn("Magazine");
        when(publicationTypeId2.getId()).thenReturn("2");
        when(pt2.getId()).thenReturn(publicationTypeId2);

        // Act
        List<US014PublicationTypeDTO> result =
                US014PublicationTypeMapper.toDTOList(List.of(pt1, pt2));

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void toDTOListThrowsWhenNullList() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                US014PublicationTypeMapper.toDTOList(null));
    }
}
